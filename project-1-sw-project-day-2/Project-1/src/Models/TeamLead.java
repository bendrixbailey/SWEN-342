package Models;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class TeamLead extends Thread implements Employee {

    private String name;
    private int teamNumber;
    private int randStartDelay = (int) ((Math.random() * (150 - 0)) + 0);
    private int randLunchStartTime = (int) ((Math.random() * (3000 - 2400)) + 2400);  // random lunch start time between 12 - 1
    private int randLunchTime = (int) ((Math.random() * ((600-randStartDelay) - 300)) + 300);    //lunch can be anywhere from 30-60 minutes
    private int endDayMillis = randStartDelay + randLunchTime + 4800;       //end time will be 8 hours from start time not including lunch
    private boolean enterQuestionMeeting = false;
    private Manager manager;

    private CyclicBarrier standupBarrier;
    private CyclicBarrier teamStandupBarrier;
    private CyclicBarrier eodBarrier;
    private CyclicBarrier questionBarrier;
    private SystemClock sysClock;

    /**
     * Constructor for team lead
     * @param name                  name for team lead
     * @param sysClock              sys clock instance
     * @param teamNumber            team number
     * @param standupBarrier        barrier for manager standup
     * @param teamStandupBarrier    barrier for team standup
     * @param eodBarrier            barrier for EOD standup
     * @param questioningBarrier    barrier for questioning
     * @param manager               manager
     */
    public TeamLead(String name, 
                    SystemClock sysClock, 
                    int teamNumber, 
                    CyclicBarrier standupBarrier,
                    CyclicBarrier teamStandupBarrier,
                    CyclicBarrier eodBarrier, 
                    CyclicBarrier questioningBarrier, 
                    Manager manager){
        super(name);
        this.name = name;
        this.sysClock = sysClock;
        this.teamNumber = teamNumber;
        this.standupBarrier = standupBarrier;
        this.teamStandupBarrier = teamStandupBarrier;
        this.eodBarrier = eodBarrier;
        this.questionBarrier = questioningBarrier;
        this.manager = manager;
    }
    
    @Override
    //print arrival activity
    public void arrive() {
        System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| " + "Team " + this.teamNumber + " lead: " + this.name + " begins work");
    }

    @Override
    //print manager stand up activity
    public void standUp() {
        System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| " + "Team " + this.teamNumber + " lead: " + this.name + " enters standup");
        try {
            sleep(150);
        } catch (InterruptedException e) {}
        System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| " + "Team " + this.teamNumber + " lead: " + this.name + " leaves standup");
    }

    //print team standup activity
    private void teamStandup(){
        System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| " + "Team " + this.teamNumber + " lead: " + this.name + " enters team standup");
        try {
            sleep(150);
        } catch (InterruptedException e) {}
        System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| " + "Team " + this.teamNumber + " lead: " + this.name + " leaves team standup");
    }

    @Override
    /**
     * This function handles the daily schedule of the team lead. Begins with both standups,
     * then handles question answering, lunch, and EOD meeting.
     */
    public void run() {
        try {
            sleep(this.randStartDelay);
            this.arrive();
            //wait on barrier for manager standup
            standupBarrier.await();
            this.standUp();
            //wait on barrier for team standup
            this.teamStandupBarrier.await();
            this.teamStandup();
            while(sysClock.GetCurrentTimeLong() < endDayMillis){    // while worker is still on the clock
                //enter the question meeting
                if(this.enterQuestionMeeting){
                    this.questionBarrier.await();
                    sleep(100);
                    this.enterQuestionMeeting = false;
                }
                //take lunch 
                if(sysClock.GetCurrentTimeLong() >= randLunchStartTime && sysClock.GetCurrentTimeLong() < randLunchStartTime + 9) { // take lunch
                    System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| " + "Team " + this.teamNumber + " lead: " + this.name + " is going out to lunch.");
                    try {
                        sleep(randLunchTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| " + "Team " + this.teamNumber + " lead: " + this.name + " goes back to work after lunch.");
                }
                //enter EOD meeting
                if(sysClock.GetCurrentTimeLong() == 4800){ // enter EOD meeting
                    eodBarrier.await(); 
                    System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| " + "Team " + this.teamNumber + " lead: " + this.name + " enters EOD meeting");
                    sleep(150);
                    System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| " + "Team " + this.teamNumber + " lead: " + this.name + " leaves EOD meeting");
                }

            }
            this.leave();
        } catch(InterruptedException | BrokenBarrierException e){
            e.printStackTrace();
        }
    }

    @Override
    //prints out that this team lead's day is done
    public void leave() {
        System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| " + "Team " + this.teamNumber + " lead: " + this.name + " ends his shift");
    }

    @Override
    //unused
    public void questioning() {
        
    }

    /**
     * This function is used to await this thread on the question barrier so the manager can answer the question
     */
    public void startAwaitQuestion(){
        this.enterQuestionMeeting = true;
    }


    /**
     * This function gets called by the developer if this team lead is unable to answer 
     * their question. If the manager is busy, it adds the question to a queue for when the manager
     * isnt busy anymore. If the manager isnt busy, then tell the manager, this thread, and the developer
     * that asked a question to wait on the question barrier, then everyone sleeps for 10 minutes
     * @param developer developer that asks the question
     */
    public void questionForManager(Developer developer){
        if(this.manager.isBusy()){
            System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| " + "The manager is busy, so Team " + this.teamNumber + " lead: " + this.name + " holds " + developer.getName() + "'s question for later");
            this.manager.addQuestionToQueue(developer);
        }else{
            //make all members responsible for question wait on the barrier
            System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| " + "The manager is free, so Team " + this.teamNumber + " lead: " + this.name + " sends " + developer.getName() + "'s question to the manager");
            this.manager.addQuestionToQueue(developer);
        }
    }

    //Function just to print out the fact that the team lead was able to answer the question
    public void answeredQuestion(String name){
        System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| " + "Team " + this.teamNumber + " lead: " + this.name + " answers question for " + name);
    }
    
}
