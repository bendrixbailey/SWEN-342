import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;


/**
 * This class is the abstract actor class that includes logging
 * so that all message transactions can be logged in the console.
 * Also contains a shutdown function (which doesnt close the actor)
 * but instead just prints out that the actor is done for the day
 */
public abstract class LoggingActor extends UntypedAbstractActor {
    private String name;

    //Just need to keep track of name. Lane is object specific.
    public LoggingActor(String name){
        this.name = name;
    }

    /**
     * Called when the actor recieves a message. Prints
     * message recieved.
     * @param message that the actor recieved
     */
    public void receiveMessage(Message message) {
        if(message instanceof Baggage){
            System.out.println(this + " recieves " + message + "'s baggage");
        }
        System.out.println(this + " recieves " + message);
    }

    /**
     * Called when actor processes the message recieved.
     * @param message message being processed
     */
    public void processMessage(Message message) {
        if(message instanceof Baggage){
            System.out.println(this + " processes " + message + "'s baggage");
        }else{
            System.out.println(this + " processes " + message);
        }
    }

    /**
     * Called when a message is to be sent to another actor.
     * Prints out the transaction, and then sends other actor
     * the message.
     * @param message       message object to be sent
     * @param target        target to recieve message
     * @param targetName    name of target recieving message
     */
    public void sendMessage(Message message, ActorRef target, String targetName) {
        if(message instanceof Baggage){
            System.out.println(this.name + " sent " + message + "'s baggage" + " to " + targetName);
        }else{
            System.out.println(this.name + " sent " + message.toString() + " to " + targetName);
        }
        target.tell(message, ActorRef.noSender());
    }

    /**
     * Function used to simply print that the actor is closing
     * and will soon be shut off.
     */
    public void close() {
        System.out.println(this + " is closing for the day.");
    }

    

    public String toString() {
        return this.name;
    }

}
