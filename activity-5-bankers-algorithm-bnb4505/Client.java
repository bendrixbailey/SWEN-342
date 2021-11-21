/**
 * This class simulates a client who wishes to allocate a set amount of units from a banker.
 * The client attempts to set a claim to a certain number of units, and if not allowed, is
 * forced by the banker to wait. 
 * It tries to allocate and release these units a certain number of times, while waiting for a 
 * random amount of time each loop. It then terminates. This is mainly to test how well
 * the banker can allocate and deallocate units to manage threads so no deadlock is encountered
 * 
 * @author Bendrix Bailey
 */

public class Client extends Thread{

    private Banker banker;
    private int nUnits;
    private int nRequests;
    private long minSleepMillis;
    private long maxSleepMillis;


    /**
     * Constructor for Client
     * 
     * @param name              Client name
     * @param banker            Banker that this client will be requesting units from
     * @param nUnits            Number of units to be requested
     * @param nRequests         Number of times to attempt request
     * @param minSleepMillis    Minimum amount of sleep time allowed
     * @param maxSleepMillis    Maximum amount of sleep time allowed
     */
    public Client(String name, Banker banker, int nUnits, int nRequests, long minSleepMillis, long maxSleepMillis){
        super(name);
        this.banker = banker;
        this.nUnits = nUnits;
        this.nRequests = nRequests;
        this.minSleepMillis = minSleepMillis;
        this.maxSleepMillis = maxSleepMillis;
    }

    /**
     * Main method that simply tries to set a claim, and then checks to see if it needs to release them
     * Tries to set initial claim, and if no claim is able to be made, is told to wait.
     * Once claim is made, 
     */
    public void run(){
        banker.setClaim(this.nUnits);               //attempt to set a claim to this number of units
        for(int i = 0; i < this.nRequests; i++){    //for nRequests
            if(this.banker.remaining() == 0){       //check if banker has no units remaining
                this.banker.release(this.nUnits);   //if so, release all units
            }else{
                this.banker.request(this.nUnits);
            }
            try {   //sleep for random time
                sleep(this.minSleepMillis + (long) (Math.random() * (this.maxSleepMillis - this.minSleepMillis)));
            } catch (InterruptedException e) {}
        }
        this.banker.release(this.nUnits);           //once nRequests has been done, release all units
        return;                                     //and end program
    }   
}
