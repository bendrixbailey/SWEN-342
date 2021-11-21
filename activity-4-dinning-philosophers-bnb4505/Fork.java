public class Fork implements IFork{
    
    public boolean used = false;

    public Fork(){

    }

    @Override
    public synchronized void acquire() {
        try {
            while(used){
                wait();
                this.used = true;
            }
        } catch (InterruptedException e) {}
        
    }

    @Override
    public synchronized void release() {
        this.used = false;
        notify();
    }
    
}
