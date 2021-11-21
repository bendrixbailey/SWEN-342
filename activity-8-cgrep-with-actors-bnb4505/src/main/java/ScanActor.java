import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;


/**
 * This class acts as an actor to read in standard input whether it be a file
 * or a line, and create a Found object for it. It then sends the found object to the 
 * sender of the command that gave it the file to look at.
 */
public class ScanActor extends UntypedAbstractActor {
    private Configure task;
    private final Pattern pattern;

    /**
     * Constructor of this scan actor
     * @param incomingPattern Configuration with pattern and file info.
     */
    public ScanActor(Pattern incomingPattern){
        pattern = incomingPattern;
    }


    /**
     * This method is used to recieve and process a message from another actor.
     * Calls the respective functions depending on what type of input was specified in the
     * message.
     * If the object recieved is not a Configure object, print an error and exit
     */
    @Override
    public void onReceive(Object message) throws Throwable {
        if(message.getClass() == Configure.class){
            task = (Configure) message;
            Found patterns = null;

            if(task.getFileName() != null){
                patterns = readFile();
            }else{
                patterns = readInput();
            }

            task.getRef().tell(patterns, ActorRef.noSender());
        }else{
            System.out.println("Message passed was not of type: Configure");
            return;
        }
    }

    /**
     * This function handles the reading of a file. Creates
     * buffered reader, reads in lines, and if they match a pattern,
     * then it stores the lines in a linked list.
     * 
     * @return Returns Found object with linked list of all patterns inside.
     */
    private Found readFile(){
        BufferedReader reader = null;
        LinkedList<String> lineMatches = new LinkedList<String>();
        int lineNumber = 0;
        Matcher matcher = null;
        Found patternResults;
        String lineContents = null;

        try{
            reader = new BufferedReader(new FileReader(task.getFileName()));
            lineContents = reader.readLine();
            while(lineContents != null){
                lineNumber ++;
                matcher = pattern.matcher(lineContents);
                if(matcher.find()){
                    lineMatches.add(lineNumber + " " + lineContents);
                }
                lineContents = reader.readLine();
            }
        }catch (IOException e){
            System.out.println(e);
        }

        patternResults = new Found(lineMatches, task.getFileName());
        return patternResults;
    }

    /**
     * This function handles the reading of a single standard input line.
     * It creates a buffered reader, reads in lines, and if they match, it stores that line
     * in a linked list.
     * 
     * @return Found object containing all the line matches in the standard input
     */
    private Found readInput(){
        BufferedReader reader = null;
        LinkedList<String> lineMatches = new LinkedList<String>();
        Matcher matcher = null;
        Found patternResults;
        String lineContents = null;

        try{
            reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.printf("Enter text to search.");
            lineContents = reader.readLine();
            //while()
            matcher = pattern.matcher(lineContents);
            if(matcher.find()){
                lineMatches.add(lineContents);
            }
        }catch(IOException e){
            System.out.println(e);
        }

        patternResults = new Found(lineMatches, "-");
        return patternResults;
    }
    
}
