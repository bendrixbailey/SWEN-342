import java.util.ArrayList;
import akka.actor.ActorRef;

/**
 * This class represents the actor that conducts security
 * for a single line. This houses the logic to check the passenger
 * AND bag for failure, and either send passenger to jail, or
 * let the passenger and their bag through to go to the flight.
 */
public class Security extends LoggingActor {

    private final ActorRef jail;
    private int active_scanners = 2;
    private ArrayList<Passenger> passengers; // the collection of waiting passengers
	private ArrayList<Baggage> baggage;    // the collection of waiting baggage
	private int line;

	/**
	 * The constructor needs reference to the jail, so they can send
	 * passengers there, as well as the line the security serves for.
	 * @param line line the security resides in
	 * @param jail main jail for airport terminal
	 */
    public Security(int line, ActorRef jail) {
        super("Security-" + line);
        this.jail = jail;
		this.line = line;
        passengers = new ArrayList<Passenger>();
		baggage = new ArrayList<Baggage>();
    }

	/**
	 * This function processes messages and stores the passengers/baggage
	 * in lists until their baggage/body comes through. Then it checks both,
	 * and if either has failed, sends passenger to jail. If both have passed, it
	 * sends the passenger on their way.
	 */
    @Override
    public void onReceive(Object message) throws Throwable {
        Message current_passenger = null;
		Message current_bag = null;

		if(message instanceof Passenger) {
			Passenger my_passenger = (Passenger) message;
			receiveMessage(my_passenger);
			processMessage(my_passenger);
			current_bag = checkWaitingBaggage(baggage, my_passenger);

			// if a corresponding bag is found, determine where the passenger will go
			// otherwise, place the passenger into the waiting collection
			if (current_bag != null) {
				baggage.remove(current_bag);
				if(current_bag.getFailed() || my_passenger.getFailed()) { // if bag failed check
					sendMessage(my_passenger, jail, "Jail"); 
				}
				else { // if bag passed check
					System.out.println("------" + my_passenger.toString() + " passed the security check! Happy flying!------");
				}
			}
            else {
				passengers.add(my_passenger);
			}
		}
        else if(message instanceof Baggage) {
			Baggage bag = (Baggage) message;
			receiveMessage(bag);
			processMessage(bag);
			current_passenger = checkWaitingPassengers(passengers, bag);

			// if a matching passenger is found, determine where that passenger will go.
			// otherwise, add the baggage to the waiting baggage list.
			if (current_passenger != null) {
				passengers.remove(current_passenger);
				if (current_passenger.getFailed() || bag.getFailed()) {
					sendMessage(current_passenger, jail, "Jail");
				}
				else {
					System.out.println("-----" + current_passenger.toString() + " passed security check! Happy flying!-----");
				}
			}
            else {
				baggage.add(bag);
			}
		} 
		else if(message instanceof Close) {
			receiveMessage((Close) message);
			active_scanners--;  //decremant the countdown counter

			// if the shutdown_counter is empty, then the BodyScan and BagScan objects
			// for this line are shutdown. Therefore, the Security object can begin shuting down
			// and pass the message to the Jail object.			
			if (active_scanners == 0) {
                sendMessage((Close) message, jail, "Jail");
                close();
			}
		}
        else {
			System.out.println("Security-" + line + " recieved a bad message type");
		}
    }

    /*
	 * This method is used to search the waiting baggage list for a matching bag for the current passenger
	 *
	 * @param bags     the collection of waiting bags
	 * @param pass     the current passenger at the checkpoint
	 *
	 * @return my_bag  the matching bag, or null if no bag was found
	 */

	private static Message checkWaitingBaggage(ArrayList<Baggage> bags, Passenger curPassenger) {
		boolean owns_it = false;
		Message my_bag = null;

		// Check each waiting bag until a matching bag is found or all bags have been searched.
		for(Baggage current_bag : bags) {
			// if-else-if statement used to determine if the current bag being checked
			// is a regular Baggage object or a FaildBaggage object, then check accordingly

			if (current_bag.getFailed()) {
				owns_it = curPassenger.owns(current_bag);
			}
			else {
				owns_it = curPassenger.owns(current_bag);
			}

			if (owns_it) {
				my_bag = current_bag;
				break;
			}
		}
		return my_bag;
	}

	
	/*
	 * This method checks the waiting passenger collection for a matching passenger for the current passenger.
	 *
	 * @param passengers    the collection of passengers
	 * @param bag		the current bag
	 *
	 * @return		the owner of the bag, or null if no passenger was found
	 */

	private static Message checkWaitingPassengers(ArrayList<Passenger> passengers, Baggage bag) {
		boolean owns_me = false;
		Message owner = null;

		// check every passenger waiting in the passenger collection until a matching
		// passenger is found or the entire collection was searched

		for(Passenger current_pass : passengers) {

			// if-else-if determines if the current passenger has failed or not
			// and checks for matching accordingly.
			if (current_pass.getFailed()) {
				owns_me = current_pass.owns(bag);
			}
			else {
				owns_me = current_pass.owns(bag);
			}

			
			if (owns_me) {
				owner = current_pass;
				break;
			}
		}
		return owner;
	}
}
