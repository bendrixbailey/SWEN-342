import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileFinder extends Finder{

    private File file;


    /**
     * The below constructor builds a finder for a file,
     * passes the pattern to the superclass
     * @param pattern pattern to look for
     * @param file  file to look in
     */
    public FileFinder(Pattern pattern, File file) {
        super(pattern);
        this.file = file; 
    }


    /**
     * Call handles the functionality of the Finder.call() method, but 
     * simply implements this respective logic for finding within a single file.
     * Searches through each line of the file and looks for patterns, 
     * if found, will store.
     */

    public Found call(){
        ArrayList<String> matches = new ArrayList<String>();
        BufferedReader reader = null;
        int line = 0;
        try{
            reader = new BufferedReader(new FileReader(file));
            String input;
            while((input = reader.readLine()) != null){
                Matcher matcher = pattern.matcher(input);
                //if they match
                if(pattern.toString().startsWith("^") && pattern.toString().endsWith("$")){
                    if(matcher.matches()){
                        matches.add(line + " " + input);
                    }
                }else{
                    if(matcher.find()){
                        matches.add(line + " " + input);
                    }
                }
                ++line;
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            try{
                if(reader != null){
                    reader.close();
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        //add matches to the new found class
        Found found = new Found(file.toString(), matches);
        return found;
    }
    
}
