import akka.actor.ActorRef;

/**
 * This class represents an actor that processes passengers
 * for that specific line, and passes them off to the body scanner,
 * and creates bags for them, and passes the bags to the bag scanner.
 */
public class ScannerQueue extends LoggingActor {

    private int line;
    private ActorRef bodyScanner;
    private ActorRef bagScanner;

    /**
     * Constructor for scanner queue. Needs the line number,
     * references to the bodyscanner and bag scanner in that line
     * @param line line the scanner resides in
     * @param bodyScanner body scanner for the line
     * @param bagScanner baggage scanner for the line
     */
    public ScannerQueue(int line, ActorRef bodyScanner, ActorRef bagScanner) {
        super("ScannerQueue-" + line);
        this.line = line;
        this.bodyScanner = bodyScanner;
        this.bagScanner = bagScanner;
    }

    /**
     * This class processes the various message types this actor can recieve. If
     * it recieves a passenger, then it sends the passenger off to the Body scanner,
     * and grabs the passengers bag, and sends that off to the baggage scanner.
     * 
     * If the message is close, then it prints a close message, and sends the 
     * close message to both the baggage and body scanner.
     * 
     * IF the message is unknown, it prints the error.
     */
    @Override
    public void onReceive(Object message) throws Throwable {
        if(message instanceof Passenger) {
			Passenger passenger = (Passenger) message;
			receiveMessage(passenger);
			processMessage(passenger);
			sendMessage(passenger, bodyScanner, "BodyScan-" + line);
            sendMessage(passenger.getBaggage(), bagScanner, "BagScan-" + line);
		}
        else if(message instanceof Close) {
			receiveMessage((Close) message);
			close();
            sendMessage(new Close(), bodyScanner, "BodyScan-" + line);
            sendMessage(new Close(), bagScanner, "BagScan-" + line);
		}
        else {
			System.out.println("Bad message type recieved by ScannerQueue-" + line);
		}
    }
}
