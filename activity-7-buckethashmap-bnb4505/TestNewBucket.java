import java.util.Random;

public class TestNewBucket {

    ConcurrentBucketHashMap<String, String> bhm = new ConcurrentBucketHashMap<String, String>(3);

    private class T extends Thread {
        private String name;
        private Random r = new Random();

        public T (String name){
            this.name = name;
        }

        @Override
        public void run(){
            for(int i = 0; i < 5; i ++){

                bhm.put(name + i, name + "-" + i);
                System.out.println("Added " + name + i + " to the bucket map");

                if(r.nextBoolean()){
                    try{
                        sleep(1000);
                    }catch(InterruptedException e){

                    }
                }else{
                    try{
                        sleep(500);
                    }catch(InterruptedException e){

                    }
                }

                if(bhm.containsKey(name + i)){
                    bhm.remove(name + i);
                    System.out.println("Removed " + name + i + " from the bucket map");
                }
            }
        }
    }

    private class SizeChecker extends Thread {
        private boolean alive;

        public SizeChecker(){
            this.alive = true;
        }

        public void kill(){
            alive = false;
        }

        @Override
        public void run(){
            while(alive){
                System.out.println("Size of the bucket hash map is:" + bhm.size());

                try{
                    sleep(1000);
                }catch(InterruptedException e){

                }
            }
        }
    }

    public static void main(String[] args) {
        TestNewBucket testNewBucket = new TestNewBucket();

        SizeChecker sizeChecker = testNewBucket.new SizeChecker();

        T t1 = testNewBucket.new T("TesterMc.Gee");
        T t2 = testNewBucket.new T("Schmooze");


        sizeChecker.start();
        t1.start();
        t2.start();
        

        try {
            Thread.sleep(5500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        sizeChecker.kill();


    }
    
}
