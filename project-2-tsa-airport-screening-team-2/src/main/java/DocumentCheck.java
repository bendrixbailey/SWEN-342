import java.util.List;
import java.util.Random;

import akka.actor.ActorRef;

/**
 * This class contains logic for the single document checker that will
 * be run in this program. 
 */
public class DocumentCheck extends LoggingActor {
 
    private int currentQueue = 0;
    private int numScannerQueues;
    
    private List<ActorRef> scannerQueue;
    private Random rand = new Random();

    /**
     * Initialize class with array of all
     * scanner queues. Because the actor structure is constructed from
     * jail to here, we can reference the next objects in the chain easily.
     * We will use the array to send passengers to different queues.
     * @param scannerQueue array of scannerQueue objects that we will be dealing with
     */
    public DocumentCheck(List<ActorRef> scannerQueue) {
        super("Document Checker");
        this.scannerQueue = scannerQueue;
        numScannerQueues = this.scannerQueue.size();
    }

    /**
     * This function handles the recieve method for the document checker.
     * It can only handle a passenger object or a close object. Rolls the dice on 
     * turning away the passenger, and if they dont get turned away, they get
     * sent to one of the scannerQueue objects for further processing.
     */
    @Override
    public void onReceive(Object message) throws Throwable {
        if(message instanceof Passenger){
            Passenger newPassenger = (Passenger) message;   //cast to passenger
            receiveMessage(newPassenger);                   //function from abstract class
            processMessage(newPassenger);
            if(rand.nextInt(5) > 0) {                        //80% chance passenger doesnt get turned away
                ActorRef nextScanQueue = scannerQueue.get(currentQueue);
                sendMessage(newPassenger, nextScanQueue, "Scanner-" + currentQueue);
                if(currentQueue == numScannerQueues - 1){    //if next would be out of range
                    currentQueue = 0;                       //reset to 0
                }
                else{                                      //otherwise
                    currentQueue ++;                       //increase index by 1
                }
            }
            else{
                System.out.println("-----" + this + " had to turn away " + newPassenger + "-----");
            }
        }
        else if (message instanceof Close){
            receiveMessage((Close) message);    //cast to close object
            close();                            //print close
            for(int i = 0; i < numScannerQueues; i ++){
                sendMessage(new Close(), scannerQueue.get(i), "ScannerQueue-" + i);
            }
            //might need to send close message to other objects.
        }
        else{      //Print if bad message type is recieved
            System.out.print("Incorrect message format recieved. Shutting down.");
        }
    }
}
