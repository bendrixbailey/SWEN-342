import java.util.Collection;
import java.util.regex.Pattern;

import akka.actor.*;

public class CGrep {
    
    public static void main(String[] args) {
        // Replace with your code
        if(args.length < 1){
            System.out.println("Incorrect usage. Format is: java CGrep pattern [file...]");
            System.out.println("or: java Cgrep pattern [line...]");
        }else if(args.length == 1){
            
        }
        System.out.println ("CGrep with Actors");
    }

    public static void findInInput(final Pattern pattern){
        ActorSystem system = ActorSystem.create();
        ActorRef collectionActor = system.actorOf(Props.create(CollectionActor.class, system));
        collectionActor.tell(new FileCount(1), collectionActor);

        ActorRef scanActor = system.actorOf(Props.create(ScanActor.class, pattern));
        scanActor.tell(new Configure(null, collectionActor), collectionActor);
        //scanner.tell();
    }

    public static void findInFiles(final Pattern pattern, Collection<String> files){
        ActorSystem system = ActorSystem.create();
        ActorRef collectionActor = system.actorOf(Props.create(CollectionActor.class, system));
        collectionActor.tell(new FileCount(files.size()), collectionActor);

        for(String fileName : files){
            ActorRef scanActor = system.actorOf(Props.create(ScanActor.class, pattern));
            scanActor.tell(new Configure(fileName, collectionActor), collectionActor);
        }

    }


}
