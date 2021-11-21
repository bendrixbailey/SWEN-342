import java.util.Hashtable;

/**
 * This class represents a banker who is responsible for allocating
 * units to client classes that request access to them. It SHOULD properly allocate
 * and restrict clients from getting units every time so as not to get into a deadlock.
 * 
 * @author Bendrix Bailey
 */

public class Banker {
    private int nUnits;
    private int nAllocated;

    // Format of dict {ThreadName, [requested units, allocated units]}
    private Hashtable<String, Integer[]> clientel = new Hashtable<String, Integer[]>();

    

    /**
     * Constructor for Banker class
     * @param nUnits    total units this banker will control access to
     */
    public Banker(int nUnits){
        this.nUnits = nUnits;
    }

    /**
     * This class controls the initial claim that clients make on certain units.
     * This is where the banker does logic to see if there are available units, and if not
     * tells the thread thats requesting access to wait.
     * @param nReqUnits     The number of units this client requests access to.
     */
    public synchronized void setClaim(int nReqUnits){
        Thread curThread = Thread.currentThread();
        if(clientel.contains(curThread) || nReqUnits < 0 || nReqUnits > this.nUnits){
            System.exit(1);
        }
        clientel.put(curThread.getName(), new Integer[]{nReqUnits, 0});       //add claim to nunits, and make current claimed to 0
        System.out.println("Thread " + curThread.getName() + " sets a claim for " + nReqUnits + " units");
    }

    /**
     * This function handles the request to allocate nUnits to a certain client. It must check
     * to see if the allocation would result in a safe or unsafe state. If invalid thread or units, exit,
     * otherwise it will check if allocation results in less units avaiable than 0. If units can be allocated,
     * it allocates them, otherwise it forces the client to wait.
     * Once the thread is notified it can continue, it will then allocate the amount of units to that client
     * and then exit.
     * @param nUnits    number of units the given client is requesting
     * @return          returns once an operation was successful. 
     */
    public synchronized boolean request(int nUnits){
        Thread curThread = Thread.currentThread();          //get current thread name
        if(!clientel.contains(curThread.getName()) || nUnits < 0 || nUnits > this.nUnits){
            System.exit(1);                 //exit if things are invalid
        }
        System.out.println("Thread " + curThread.getName() + " requests " + nUnits + " units");

        if(this.nUnits - this.nAllocated >= nUnits){    
            System.out.println("Thread " + curThread.getName() + " has " + nUnits + " allocated.");
            clientel.put(curThread.getName(), new Integer[]{clientel.get(curThread.getName())[0], nUnits});
            this.nAllocated += nUnits;      //increase the total allocated units
            return true;
        }else{
            System.out.println("Thread " + curThread.getName() + " waits.");
            try {
                curThread.wait();           //tell current thread to wait
            } catch (InterruptedException e) {}
            System.out.println("Thread " + curThread.getName() + " awakens.");

            if(this.nUnits - this.nAllocated >= nUnits){
                System.out.println("Thread " + curThread.getName() + " has " + nUnits + " allocated.");
                clientel.put(curThread.getName(), new Integer[]{clientel.get(curThread.getName())[0], nUnits});
                this.nAllocated += nUnits;  //increase total allocated units
                return true;
            }
        }
        return true;                        //return value doesnt matter.
    }

    /**
     * This function controls the deallocation of units from a given thread. This function is called
     * by a client. If the thread has no claim, is trying to free negative units, or currently has no units allocated,
     * the entire program will exit, as this would be an issue. Otherwise, it will remove the number
     * of allocated units from the thread, remove them from the bankers allocated units, and then notify
     * any waiting threads that there are more units up for grabs.
     * @param nUnits    number of units the thread wants to release
     */
    public synchronized void release(int nUnits){
        Thread curThread = Thread.currentThread();          //get current thread name
        if(!clientel.contains(curThread.getName()) || nUnits < 0 || nUnits > clientel.get(curThread.getName())[1]){
            System.exit(1);     //exit if some things are invalid
        }
        System.out.println("Thread " + curThread.getName() + " releases " + nUnits);
        clientel.put(curThread.getName(), new Integer[]{clientel.get(curThread.getName())[0], 0});  //replace allocated with 0
        this.nAllocated -= nUnits;      //remove nUnits from the allocated ones, so now theyre free
        notifyAll();                    //notify all waiting threads that more units are available
    }

    /**
     * This function simply returns the number of currently allocated units to other clients
     * @return integer, number of allocated units.
     */
    public synchronized int allocated(){
        return this.nAllocated;
    }

    /**
     * This function simply returns the remaining units this banker can allocate.
     * @return integer, number of remaining free units
     */
    public synchronized int remaining(){
        return this.nUnits - this.nAllocated;
    }
} 