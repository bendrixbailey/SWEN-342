import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

public class CGrep{
    public static void main(String[] args){
        if(args.length < 2){
            System.out.println("No file or pattern input provided. Exiting...");
        }else{
            //if a single pattern is entered
            if(args.length == 2 && !args[1].endsWith(".txt")){
                Finder.find(Pattern.compile(args[0]), args[1]);
            //if a pattern is entered, and a file is in the line
            }else if(args.length > 2 && args[1].endsWith(".txt")){
                Collection<File> files = new ArrayList<File>(); //REDO WITH LINES NOT FILES
                for(int i = 1; i < args.length; i ++){
                    files.add(new File(args[i]));

                }
                Finder.find(Pattern.compile(args[0]), files);
            //if the person enters in a long file in the command line
            }else{
                //gotta implement stuff in here
            }
        }
    }

}