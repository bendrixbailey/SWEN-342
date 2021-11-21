import java.util.ArrayList;

/**
 * This class represents the main controller of the program. It instantiates a banker,
 * and then creates a set number of clients, and starts them all. Then it calls .join
 * on all the clients which makes main() wait until all the clients are done executing,
 * then finishes. 
 */
public class Driver {

    //this variable dictates the number of resources that a banker can start out with.
    //change if you want banker to start with more or less
    private static int bankerResources = 100;

    private static ArrayList<Client> clients = new ArrayList<Client>();

    /**
     * @see numClients this variable controls the number of clients that will be interacting with the banker
     * @see minSleep this is the minimum number of milliseconds a thread can sleep for before it attempts to request another claim
     * @see maxSleep this is the maximum number of milliseconds a thread can sleep for before it attempts to request another claim
     * @see minNUnits this is the minimum number of units a client could request for. Once client is created, this doesnt change
     * @see maxNUnits this is the maximum number of units a client could request for. Once client is created, this doesnt change.
     * @see minNRequests this is the miniumum total requests a client will make. Does not change once client is created
     * @see maxNRequests this is the maximum total requests a client will make. Does not change once client is created.
     */
    private static int numClients = 5;
    private static long minSleep = 10;
    private static long maxSleep = 1000;
    private static int minNUnits = 1;
    private static int maxNUnits = 50;
    private static int minNRequests = 1;
    private static int maxNRequests = 10;

    public static void main(String[] args) {
        Banker mBanker = new Banker(bankerResources);

        //Create clients
        for(int i = 0; i < numClients; i++){
            Client tempClient = new Client(
            "Client-" + i,                                                          //name the client
            mBanker,                                                                //set banker
            (int) ((Math.random() * (maxNUnits - minNUnits)) + minNUnits),          //set random unit desire
            (int) ((Math.random() * (maxNRequests - minNRequests)) + minNRequests), //set random request limit
            minSleep,                                                               //set minimum sleep limit
            maxSleep);                                                              //set maximum sleep limit

            clients.add(tempClient);
            tempClient.start();
        }

        for(int i = 0; i < numClients; i ++){
            try {
                clients.get(i).join();                                              //exit once all the threads are finished.
            } catch (InterruptedException e) {}
        }

        

    }
}
