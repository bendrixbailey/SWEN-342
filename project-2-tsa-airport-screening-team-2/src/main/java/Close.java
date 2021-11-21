/**
 * This class represents a close message. 
 * This tells the object that recieved this message
 * that its time to close down shop.
 */
public class Close extends Message {

    public Close() {
        super("Close");
    }
    
}
