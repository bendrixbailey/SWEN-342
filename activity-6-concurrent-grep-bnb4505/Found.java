import java.util.List;


/**
 * This class represents a pattern within a specific file. It stores the name of the file
 * and the list of pattern matches within that file.
 * 
 * @author bendrixb
 */
public class Found {
    private String fileName;
    private List<String> patternMatches;

    /**
     * Constructor for object
     * @param fileName  name of file thats being analyzed
     * @param pattern   list of pattern matches in specified file
     */
    public Found(String fileName, List<String> pattern){
        this.fileName = fileName;
        this.patternMatches = pattern;
    }

    /**
     * @return pattern of matches within this file
     */
    public List<String> getPattern(){
        return this.patternMatches;
    }

    /**
     * @return filename of this object
     */
    public String getFileName(){
        return this.fileName;
    }
}
