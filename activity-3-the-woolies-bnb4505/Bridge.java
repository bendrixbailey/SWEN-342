/**
 * Bridge class for woolies threading assignment
 * 
 * 
 * 
 * @author Bendrix Bailey
 */


public class Bridge {
    private java.util.LinkedList<Woolie> woolies = new java.util.LinkedList<Woolie>();

    private int MAX_CAPACITY = 3;
    private int currentCapacity = 0;

    public Bridge(){}


    public synchronized void enterBridge(Woolie woolie){
        while ( currentCapacity == MAX_CAPACITY ) {
            try {
                wait();
            } catch ( InterruptedException iex ) {
            }
        }
        woolies.add(woolie);
        currentCapacity++;
        notifyAll();
    }

    public synchronized void leaveBridge(Woolie woolie){
        if ( currentCapacity > 0 ) {
            currentCapacity--;
        }
        woolies.pop();
        notifyAll();
    }
}