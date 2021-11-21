import java.util.ArrayList;
import java.util.List;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Main {
    /**
     * This is the main class that builds the actors, starts them, and feeds the passengers in
     * It creates it back to front, so it goes 
     * jail -> [line1 security -> line1 bagScan, line1 bodyScan -> line1queue] -> documentcheck
     * @param args
     */
    public static void main(String[] args) {
        
        int numLines = 2;
        int numPassengers = 5;

        List<ActorRef> scannerQueues = new ArrayList<ActorRef>();

        //create jail first
        ActorSystem system = ActorSystem.create();
        ActorRef jail = system.actorOf(Props.create(Jail.class, numLines, system));
        //for each line, create security, baggage/body check, and the queue
        for(int i = 0; i < numLines; i++){
            ActorRef lineSecurity = system.actorOf(Props.create(Security.class, i, jail));

            ActorRef lineBaggageCheck = system.actorOf(Props.create(BagScan.class, i, lineSecurity));
            ActorRef lineBodyCheck = system.actorOf(Props.create(BodyScan.class, i, lineSecurity));

            ActorRef lineScanner = system.actorOf(Props.create(ScannerQueue.class, i, lineBodyCheck, lineBaggageCheck));
            scannerQueues.add(lineScanner);
        }
        //finally create document checker
        ActorRef documentCheck = system.actorOf(Props.create(DocumentCheck.class, scannerQueues));
        //send all passengers
        for(int i = 0; i < numPassengers; i ++){
            documentCheck.tell(new Passenger("Passenger-" + i), ActorRef.noSender());
        }
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //After some time, send message to close down system/
        documentCheck.tell(new Close(), ActorRef.noSender());

    
    }
    
}
