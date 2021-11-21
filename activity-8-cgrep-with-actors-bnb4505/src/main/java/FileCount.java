
/**
 * Message for replaying the count of the number
 * of files to be read in
 */
public class FileCount {
    private final int fileCount;
    
    public FileCount(int count){
        fileCount = count;
    }

    public int getCount(){
        return fileCount;
    }
}
