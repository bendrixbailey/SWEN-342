import java.util.Random;

import akka.actor.ActorRef;


/**
 * This class represents an actor that scans baggage. It processes baggage,
 * and has a 1/5 chance that the baggage will fail. If the baggage fails,
 * then the baggage is marked as failed, and is sent to line security. IF
 * the baggage passes, then it is just normally sent to security
 */
public class BagScan extends LoggingActor {

    private Random random = new Random();
    private ActorRef lineSecurity;
    private int line;

    /**
     * Constructor for bag scanner. Needs line number, and reference to
     * line security.
     * @param line line
     * @param lineSecurity line security
     */
    public BagScan(int line, ActorRef lineSecurity) {
        super("BagScan-" + line);
        this.lineSecurity = lineSecurity;
        this.line = line;
    }

    /**
     * This is the main method that handles message processing. It 
     * can take either a bag object, or close. If the bag object is recieved,
     * then it calculates a 1/5 chance, and reinstances the bag with that chance.
     * If the bag passes, it is sent to security and the respective message is printed.
     * If bag fails, then it is sent to security and the respective message is printed.
     * 
     * If a close is recieved, then it prints a message, and sends a close message to the
     * security.
     * 
     * If message type is not the two above types, a message is printed.
     */
    @Override
    public void onReceive(Object message) throws Throwable {
        if(message instanceof Baggage) {
			boolean passed = (random.nextInt(5) > 3);
			Baggage baggageMessage = (Baggage) message;
            Baggage baggage = new Baggage(baggageMessage.toString(), passed);
			receiveMessage(baggage);
			processMessage(baggage);
            if (passed) {
                System.out.println(baggage + "'s baggage failed the security check");
                sendMessage(baggage, lineSecurity, "Security-" + line);
            }
            else {
                System.out.println(baggage + "'s baggage passed the security check.");
                sendMessage(baggage, lineSecurity, "Security-" + line);
            }
		}
        else if(message instanceof Close) {
			receiveMessage((Close) message);
            sendMessage((Close) message, lineSecurity, "Security-" + line);
            close();
		}
        else {
			System.out.println("Bad message type recieved by BagScan-" + line);
		}
    }
}
