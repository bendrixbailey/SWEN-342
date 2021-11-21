package Models;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Developer extends Thread implements Employee, Comparable<Developer> {

    private String name;
    private int teamNumber;
    private int employeeNumber;
    private int randStartDelay = (int) ((Math.random() * (150 - 0)) + 0);
    private int randLunchStartTime = (int) ((Math.random() * (3000 - 2400)) + 2400);  // random lunch start time between 12 - 1
    private int randLunchTime = (int) ((Math.random() * ((600-randStartDelay) - 300)) + 300);    //lunch can be anywhere from 30-60 minutes
    private int endDayMillis = randStartDelay + randLunchTime + 4800;       //end time will be 8 hours from start time not including lunch
    private int randNumberOfQuestions = (int) ((Math.random() * (3 - 0)) + 0);         // random number of questions between 0 and 3
    private int randQuestionStartTime = 900 + (int) ((Math.random() * (600 - 0)) + 0);

    private int questionDelay = 450;
    private int lastQuestionAskedTime = 0;
    private boolean enterQuestionMeeting = false;


    private TeamLead teamLead;

    private CyclicBarrier standupBarrier;
    private CyclicBarrier eodBarrier;
    private CyclicBarrier questionBarrier;
    private SystemClock sysClock;

    /**
     * Constructor for a developer
     * @param name                  name of dev
     * @param sysClock              sys clock reference
     * @param teamNumber            team number that dev is on
     * @param employeeNumber        employee number of dev
     * @param standupBarrier        barrier for standup meeting
     * @param eodBarrier            barrier for eod meeting
     * @param questioningBarrier    barrier for asking manager questions
     * @param teamLead              team lead of dev
     * @param manager               manager
     */
    public Developer(String name, 
                        SystemClock sysClock, 
                        int teamNumber, 
                        int employeeNumber, 
                        CyclicBarrier standupBarrier, 
                        CyclicBarrier eodBarrier,
                        CyclicBarrier questioningBarrier, 
                        TeamLead teamLead, 
                        Manager manager){
        super(name);
        this.name = name;
        this.sysClock = sysClock;
        this.teamNumber = teamNumber;
        this.employeeNumber = employeeNumber;
        this.standupBarrier = standupBarrier;
        this.eodBarrier = eodBarrier;
        this.questionBarrier = questioningBarrier;
        this.eodBarrier = eodBarrier;
        this.teamLead = teamLead;
        //this.manager = manager;
    }

    @Override
    //handles printing of arrival
    public void arrive() {
        System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| " + "Developer number " + this.employeeNumber + " of Team " + this.teamNumber + ": " + this.name + " begins work");
    }

    @Override
    //handles initial standup with team lead
    public void standUp() {
        System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| " + "Developer number " + this.employeeNumber + " of Team " + this.teamNumber + ": " + this.name + " enters standup");
        try {
            sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| " + "Developer number " + this.employeeNumber + " of Team " + this.teamNumber + ": " + this.name + " leaves standup");
    }

    @Override
    /**
     * This function handles the daily schedule of a dev. Begins with the meeting, then loops checking times for lunch, asking questions, etc.
     */
    public void run() {
        try {
            sleep(this.randStartDelay);
            this.arrive();
            standupBarrier.await();
            this.standUp();
            while(sysClock.GetCurrentTimeLong() < endDayMillis){    // while worker is still on the clock

                //if manager notifies to enter meeting, enter meeting
                if(this.enterQuestionMeeting){
                    this.questionBarrier.await();
                    sleep(100);
                    this.enterQuestionMeeting = false;
                }
                //handles lunch timing
                if(sysClock.GetCurrentTimeLong() >= randLunchStartTime && sysClock.GetCurrentTimeLong() < randLunchStartTime + 9) { // take lunch
                    System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| " + "Developer number " + this.employeeNumber + " of Team " + this.teamNumber + ": " + this.name + " is going out to lunch.");
                    try {
                        sleep(randLunchTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| " + "Developer number " + this.employeeNumber + " of Team " + this.teamNumber + ": " + this.name + " goes back to work after lunch.");
                }
                //handles EOD meeting
                if(sysClock.GetCurrentTimeLong() == 4800){ // enter EOD meeting
                    eodBarrier.await(); 
                    System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| " + "Developer number " + this.employeeNumber + " of Team " + this.teamNumber + ": " + this.name + " enters EOD meeting");
                    sleep(150);
                    System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| " + "Developer number " + this.employeeNumber + " of Team " + this.teamNumber + ": " + this.name + " leaves EOD meeting");
                }
                //handles question asking
                if(this.randNumberOfQuestions > 0 && 
                        (sysClock.GetCurrentTimeLong() > this.randQuestionStartTime) && //> 900
                        (sysClock.GetCurrentTimeLong() > this.lastQuestionAskedTime + this.questionDelay)){ //450
                    this.questioning();
                    this.lastQuestionAskedTime = (int) sysClock.GetCurrentTimeLong();
                }

            }
            this.leave();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    @Override
    //handles leave printing
    public void leave() {
        System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| " + "Developer number " + this.employeeNumber + " of Team " + this.teamNumber + ": " + this.name + " ends his shift");
    }

    /**
     * This function is called by the team lead when the manager
     * is free to answer their question. Dont have to print anything here as the team lead/manager prints everything.
     */
    public void startAwaitQuestion(){
        this.enterQuestionMeeting = true;
    }

    @Override
    /**
     * This function is called when the developer has a question for the team lead
     */
    public void questioning() {
        Boolean hasAnswer = Math.random() < 0.5;
        System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| " + "Developer number " + this.employeeNumber + " of Team " + this.teamNumber + ": " + this.name + " is asking their team lead a question");
        if(!hasAnswer) {
            //if team lead cant answer question, pass to team lead to pass to manager
            System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| The team lead cannot answer the question for " + this.name + ", so they must ask the manager");
            this.teamLead.questionForManager(this);
        }
        else {
            this.teamLead.answeredQuestion(this.name);
        }
        this.randNumberOfQuestions--;
    }

    //getter for team number
    public int getTeam(){
        return this.teamNumber;
    }

    //getter for team lead
    public TeamLead getTeamLead(){
        return this.teamLead;
    }

    //getter for question barrier for manager
    public CyclicBarrier getQuestionBarrier(){
        return this.questionBarrier;
    }

    @Override
    //comparator needed for the questioning queue
    public int compareTo(Developer o) {
        if(this.name == o.name){
            return 0;
        }else{
            return 1;
        }
    }
    
}