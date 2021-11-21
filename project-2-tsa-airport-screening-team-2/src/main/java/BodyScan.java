import java.util.Random;
import akka.actor.ActorRef;

/**
 * This class represents an actor that does the scanning of the passenger.
 * It recieves a passenger, calculates a 1/5 chance the passenger fails,
 * and then passes it on to security.
 */
public class BodyScan extends LoggingActor {

    private Random random = new Random();

    private ActorRef lineSecurity;
    private int line;

    /**
     * The constructor sets the two private variables, and
     * needs reference to the line, and the security for this line.
     * @param line line this bodyscan resides in
     * @param lineSecurity security for this line
     */
    public BodyScan(int line, ActorRef lineSecurity) {
        super("BodyScan-" + line);
        this.lineSecurity = lineSecurity;
        this.line = line;
    }

    /**
     * This function handles the processing of messages recieved by this actor.
     * If a passenger is recieved, then it calculates a random 1/5 chance the passenger
     * fails, and if they do, prints the respective message and sends the passenger to
     * security. If the passenger passes, then it prints that message and sends it to 
     * security.
     * 
     * If a close message is recieved, it prints the shutdown, and sends a close
     * message to security.
     * 
     * If neither message type is recieved, it prints the error line.
     */
    @Override
    public void onReceive(Object message) throws Throwable {
        if(message instanceof Passenger) {
            boolean passed = (random.nextInt(5) > 3);
			Passenger passengerMessage = (Passenger) message;
            Passenger passenger = new Passenger(passengerMessage.toString(), passed);
			receiveMessage(passenger);
			processMessage(passenger);
            if (passed) {
                System.out.println(passenger + " failed the security check");
                sendMessage(passenger, lineSecurity, "Security-" + line);
            }
            else {
                System.out.println(passenger + " passed the security check");
                sendMessage(passenger, lineSecurity, "Security-" + line);
            }
		}
        else if(message instanceof Close) {
			receiveMessage((Close) message);
            sendMessage((Close) message, lineSecurity, "Security-" + line);
            close();
		}
        else {
			System.out.println("Bad message type recieved by BodyScan-" + line);
		}   
    }
}
