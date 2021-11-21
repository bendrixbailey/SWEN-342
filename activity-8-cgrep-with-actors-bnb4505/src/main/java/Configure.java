import akka.actor.ActorRef;

/**
 * This is one of the message classes that are sent between actors. This is sent between the main
 * file and the ScanActor, containing the actor that sent it, and the filename
 * to read in from. Will be null if the standard input is to be used.
 */
public class Configure {
    private final ActorRef collectionActorRef;
    private final String filename;

    //construtor, all fields are final, so boom, immutability
    public Configure(String name, ActorRef actorRef){
        filename = name;
        collectionActorRef = actorRef;
    }

    //return the actor that sent the message
    public ActorRef getRef(){
        return collectionActorRef;
    }

    //gets the filename of the file to be read in
    public String getFileName(){
        return filename;
    }


}
