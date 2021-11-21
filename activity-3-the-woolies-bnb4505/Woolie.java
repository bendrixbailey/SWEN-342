/**
 * Woolie class for woolies assignment.
 * 
 * @author Bendrix Bailey
 */


public class Woolie extends Thread{
    private String name;
    private int crossingTime;
    private String destination;
    private Bridge targetBridge;


    public Woolie(String name, int crossingTime, String destination, Bridge bridge){
        super(name);
        this.name = name;
        this.crossingTime = crossingTime;
        this.destination = destination;
        this.targetBridge = bridge;
    }

    public void run(){

        this.targetBridge.enterBridge(this);

        System.out.println(this.name + " has arrived at the bridge");
        for(int time = 0; time < crossingTime; time++){
            if(time == 0){
                System.out.println(this.name + " is starting to cross the bridge");
            }else{
                System.out.println("    " + this.name + " " + time + " seconds.");
            }

            try{
                sleep(1000);
            }catch(InterruptedException e){}
        }

        System.out.println(this.name + " leaves at city: " + this.destination);

        this.targetBridge.leaveBridge(this);
    }
}
