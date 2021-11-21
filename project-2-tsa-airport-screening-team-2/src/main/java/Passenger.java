/**
 * This class represents a passenger that will go through 
 * the system. Inherits both messages from
 * abstract class. Simply used to distinguish type,
 * and to check for message class type.
 */

public class Passenger extends Message {
    private final Baggage baggage;

    public Passenger(String name) {
        super(name);
        this.baggage = new Baggage(name);
    }

    //for when passenger fails check.
    public Passenger(String name, boolean failed){
        super(name, failed);
        this.baggage = new Baggage(name);
    }

    //returns baggage of this passenger
    public Baggage getBaggage() {
        return baggage;
	}
	
	// check that the given Baggage is owned by this Passenger
	public boolean owns( Baggage baggage ) {
		return this.baggage.getName() == baggage.getName();
	}

}
