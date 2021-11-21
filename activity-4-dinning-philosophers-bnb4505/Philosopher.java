import java.util.Random;

public class Philosopher extends Thread{
    private int id;
    private Fork left;
    private Fork right;
    private boolean rHanded;
    private int nTimes;
    private long thinkMillis;
    private long eatMillis;


    private int currLoop = 0;
    private boolean loopInfinitely = false;

    public Philosopher(int id, Fork left, Fork right, boolean rHanded, int nTimes, long thinkMillis, long eatMillis){
        this.id = id;
        this.left = left;
        this.right = right;
        this.rHanded = rHanded;
        this.nTimes = nTimes;
        this.thinkMillis = thinkMillis;
        this.eatMillis = eatMillis;
    }

    public void run(){
        if(nTimes == 0){
            this.loopInfinitely = true;
        }
        while (currLoop <= nTimes){
            int ranInt = (int) (Math.random() * (0 - thinkMillis));
            System.out.println("Philosopher " + this.id + "thinks for " + ranInt + " time units");
            try {
                sleep(ranInt);
            } catch (InterruptedException e) {}
            Thread.yield();

            System.out.println("Philosopher " + this.id + "goes for left fork");
            left.acquire();
            System.out.println("Philosopher " + this.id + "has left fork");
            Thread.yield();
            if(!rHanded){
                System.out.println("Philosopher " + this.id + "goes for right fork");
                right.acquire();
                System.out.println("Philosopher " + this.id + "has right fork");
            }
            int t = (int) (Math.random() * (0 - eatMillis));
            System.out.println("Philosopher " + this.id + "eats for " + t + " time units");
            try {
                sleep(t);
            } catch (InterruptedException e) {}
            
            right.release();
            System.out.println("Philosopher " + this.id + "releases right fork");
            left.release();
            System.out.println("Philosopher " + this.id + "releases left fork");


            if(!loopInfinitely){
                currLoop ++;
            }
        }
    }
}
