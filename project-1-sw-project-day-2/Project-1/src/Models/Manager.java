package Models;
import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.PriorityBlockingQueue;

public class Manager extends Thread implements Employee {

    private String name;
    //private List<TeamLead> teamLeads;
    private Boolean inMeeting = false;

    private SystemClock sysClock;
    private Queue<Developer> questionQueue;
    private CyclicBarrier standupBarrier;
    private CyclicBarrier eodBarrier;

    /**
     * Manager constructor
     * @param clock             clock instance
     * @param name              name of manager
     * @param standupBarrier    barrier for manager standup
     * @param eodBarrier        barrier for eod meeting
     */
    public Manager(SystemClock clock, 
                    String name, 
                    CyclicBarrier standupBarrier,
                    CyclicBarrier eodBarrier){
        super(name);
        this.name = name;
        this.sysClock = clock;
        this.questionQueue = new PriorityBlockingQueue<>();
        this.standupBarrier = standupBarrier;
        this.eodBarrier = eodBarrier;
    }

    @Override
    //print arrival activities
    public void arrive() {
        System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| " + "Manager: " + this.name + " begins work");
    }


    @Override
    //print standup activities
    public void standUp() {
        System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| " + "Manager: " + this.name + " enters standup");
        try {
            sleep(150);
        } catch (InterruptedException e) {}
        System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| " + "Manager: " + this.name + " leaves standup");
    }

    //somewhat unused.
    public void questioning(){
        System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| " + "Manager: " + this.name + " answers a question.");
        try {
            sleep(100);
        } catch (InterruptedException e) {}
    }

    /**
     * Function used by other classes to check if manager is busy
     */
    public boolean isBusy(){
        return this.inMeeting;
    }

    /**
     * This function is used to check meetings and lunch for the manager, and set his availability for the devs/team leads
     * @throws InterruptedException instead of catching it itll just throw it.
     */
    private void checkMeetings() throws InterruptedException{
        if((1199 < sysClock.GetCurrentTimeLong()) 
            && (sysClock.GetCurrentTimeLong() < 1801)
            //lunch
            || (2399 < sysClock.GetCurrentTimeLong())
            && (sysClock.GetCurrentTimeLong() < 3001)
            //afternoon admin meeting
            || (3599 < sysClock.GetCurrentTimeLong())
            && (sysClock.GetCurrentTimeLong() < 4201)){

                this.inMeeting = true;

                //Enter meeting/lunch messages
                if(sysClock.GetCurrentTimeLong() == 1200){
                //if(sysClock.GetCurrentTimeHoursMinutes() == "10:00 am"){
                    System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| " + "Manager " + this.name + " enters executive meeting");
                    sleep(1);
                }
                if(sysClock.GetCurrentTimeLong() == 2400){
                //if(sysClock.GetCurrentTimeHoursMinutes() == "12:00 pm"){
                    System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| " + "Manager " + this.name + " goes to lunch");
                    sleep(1);
                }
                if(sysClock.GetCurrentTimeLong() == 3600){
                //if(sysClock.GetCurrentTimeHoursMinutes() == "2:00 pm"){
                    System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| " + "Manager " + this.name + " enters executive meeting");
                    sleep(1);
                }

                //exit meeting/lunch functions
                if(sysClock.GetCurrentTimeLong() == 1800){
                //if(sysClock.GetCurrentTimeHoursMinutes() == "11:00 am"){
                    System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| " + "Manager " + this.name + " leaves executive meeting");
                    sleep(1);
                    //notifyAll();
                }
                if(sysClock.GetCurrentTimeLong() == 3000){
                //if(sysClock.GetCurrentTimeHoursMinutes() == "1:00 pm"){
                    System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| " + "Manager " + this.name + " goes back to work after lunch");
                    sleep(1);
                    //notifyAll();
                }
                if(sysClock.GetCurrentTimeLong() == 4200){
                //if(sysClock.GetCurrentTimeHoursMinutes() == "3:00 pm"){
                    System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| " + "Manager " + this.name + " leaves executive meeting");
                    sleep(1);
                    //notifyAll();
                }


        }else{
            this.inMeeting = false;
        }
    }


    @Override
    /**
     * This function handles the main logic of the manager. It handles meeting times, EOD meetings,
     * and answering questions
     */
    public void run() {
        System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| " + "Manager " + this.name + " does work and waits for team leads to come to standup");
        try {
            //wait for all team leads to join
            standupBarrier.await();
            this.standUp();
            while(sysClock.GetCurrentTimeLong() < 5400){
                
                checkMeetings();        //manager only has to handle his meetings
                
                if(sysClock.GetCurrentTimeLong() >= 4800 && sysClock.GetCurrentTimeLong() < 4810){
                    System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| " + "Manager " + this.name + " calls end of day meeting");
                    eodBarrier.await();
                    System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| " + "Manager " + this.name + " begins end of day meeting");
                    sleep(150);
                    System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| " + "Manager " + this.name + " ends end of day meeting and goes back to work");
                }
                //if manager has time to answer a question, and if one exists, go to meeting room.
                if(!this.questionQueue.isEmpty() &&
                    ((sysClock.GetCurrentTimeLong() > 600 && sysClock.GetCurrentTimeLong() < 1100) ||
                    (sysClock.GetCurrentTimeLong() > 1800 && sysClock.GetCurrentTimeLong() < 2300) ||
                    (sysClock.GetCurrentTimeLong() > 4200 && sysClock.GetCurrentTimeLong() < 4700))){
                    answerNextQuestion(this.questionQueue.peek());
                    this.questionQueue.remove();        //remove question once question has been answered
                }
            }
            this.leave();

        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public void leave() {
        System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| " + "Manager: " + this.name + " ends his shift");
    }

    /**
     * This function adds a question that a team lead is asking to the question queue
     * @param developer
     */
    public void addQuestionToQueue(Developer developer){
        this.questionQueue.add(developer);
    }


    /**
     * This function is called when the manager is free and needs to answer a question.
     * This checks to see which team is asking the question, tells them hes ready, and then awaits on that team's
     * questioning barrier. Once await is done, it sleeps for 10 minutes while answering the 
     * developer's question
     * @param team team that is asking question
     */
    public void answerNextQuestion(Developer developer){   //await on question barrier
        try {
            this.inMeeting = true;
            System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| " + "Manager: " + this.name + " enters meeting with team "+ developer.getTeam() + " lead and " + developer.getName() + " to answer their question");

            sleep(100);
            System.out.println(sysClock.GetCurrentTimeHoursMinutes() + "| " + "Manager: " + this.name + " leaves meeting with team "+ developer.getTeam() + " lead and " + developer.getName() + " after answering their question");
            this.inMeeting = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
