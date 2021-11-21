import java.util.ArrayList;
import java.util.List;

import akka.actor.ActorSystem;

public class Jail extends LoggingActor {

    private List<Passenger> inmates = new ArrayList<Passenger>();
    private ActorSystem system;
    private int lineCount;

    public Jail(int lineCount, ActorSystem system) {
        super("Jail");
        this.lineCount = lineCount;
        this.system = system;
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if(message instanceof Passenger) {
            Passenger inmate = (Passenger) message;
            receiveMessage(inmate);
            inmates.add(inmate);

        }
        else if(message instanceof Close) {
            Close close = (Close) message;
            receiveMessage(close);
            processMessage(close);
            lineCount --;
            if(lineCount == 0){
                System.out.println("Here are the passengers going to the slammer today:");
                for(Passenger pass : inmates) {
                    System.out.println("    -" + pass);
                }
                close();
                system.terminate();
            }
        }
        else {
            System.out.println("Bad message type recieved by jail.");
        }
        
    }
    
}
