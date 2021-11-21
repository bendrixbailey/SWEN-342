import akka.actor.UntypedAbstractActor;
import akka.actor.ActorSystem;
import java.util.List;

/**
 * This class collects and prints the scan results.
 * Stops once it has printed all the existing files.
 */
public class CollectionActor extends UntypedAbstractActor{

    private int numFiles = 0;
    private int acceptedFiles = 0;
    private ActorSystem system;

    public CollectionActor(ActorSystem system){
        this.system = system;
    }

    /**
     * This class recieves two types of messages. One to specify
     * how many files are going to be read in, and another
     * that contains the pattern results from one file.
     * Once the accepted files matches the num files, we exit as
     * all the files have been read.
     */
    @Override
    public void onReceive(Object message) throws Throwable {
        if(message instanceof FileCount){
            numFiles = ((FileCount)message).getCount();
        }else if(message instanceof Found){
            acceptedFiles ++;
            Found found = (Found) message;
            String fileName = found.getFilename();

            if(fileName != null){
                System.out.println(fileName + ":");
            }else{
                System.out.println("-");
            }
            List<String> matches = found.getMatches();
            if(matches.size() == 0){
                System.out.println("    No matches found");
            }else{
                for(int i = 0; i < matches.size(); i ++){
                    System.out.println("    " + matches.get(i));
                }
            }

            if(acceptedFiles == numFiles){
                system.terminate();
            }

        }else{
            System.out.println("Wrong message type recieved. Was not 'FileCount' or 'Found'");
        }
        
    }
    
}
