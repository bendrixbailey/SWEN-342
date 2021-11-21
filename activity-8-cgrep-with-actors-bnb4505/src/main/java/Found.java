import java.util.List;

/**
 * This object represents all the patterns found within a filename.
 * Simply used for transferring the data to and from actors.
 */
public class Found {
    private final List<String> matches;
    private final String filename;

    //Constructor, takes in matches, and file name.
    public Found(List<String> incomingMatches, String file){
        this.matches = incomingMatches;
        this.filename = file;
    }

    //returns list of matches
    public List<String> getMatches(){
        return matches;
    }

    //returns filename
    public String getFilename(){
        return filename;
    }
}
