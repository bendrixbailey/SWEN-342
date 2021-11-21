/**
 * This class represents a bag that will go through 
 * the system. Inherits both messages from
 * abstract class. Simply used to distinguish type,
 * and to check for message class type.
 */

public class Baggage extends Message {

    public Baggage(String name) {
        super(name);
        //TODO Auto-generated constructor stub
    }

    public Baggage(String name, boolean failed){
        super(name, failed);
    }
}

