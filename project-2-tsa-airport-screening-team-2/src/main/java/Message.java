/**
 * This abstract class represents a message
 * sent between actors in this system.
 * The detailed messages will either be of type Passenger
 * or Baggage. The status of the security checks will be
 * stored in here. Default is passed
 */

public abstract class Message {

    private String name;
    private boolean failed = false;  //set default to true

    //Initial constructor before passenger/bag goes through security
    public Message(String name) {
        this.name = name;
    }

    //constructor once passenger/bag has status
    public Message(String name, boolean failed) {
        this.name = name;
        this.failed = failed;
    }


    //getters for both values.

    public String toString() {
        // if(this.failed) {
        //     return "Failed " + name;
        // }
        return name;
    }

    public String getName(){
        return name;
    }

    public boolean getFailed() {
        return failed;
    }
}
