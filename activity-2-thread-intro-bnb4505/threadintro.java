class ThreadTester extends Thread{
    private Thread t;
    private String threadName;

    ThreadTester(String name){
        threadName = name;
        System.out.println(threadName + " is being created");
    }

    public void run() {
        System.out.println("Running " + threadName);
        try{
            for(int i = 0; i < 10; i++ ){
                System.out.println(i + "|" + threadName);
                Thread.sleep(1000);
            }
        }catch(InterruptedException e){
            System.out.println("Thread " + threadName + " interrupted...");
        }
        System.out.println("DONE! " + threadName);
    }

    public void start(){
        if(t==null){
            t = new Thread(this, threadName);
            t.start();
        }
    }
}


class ThreadingRunner{
    public static void main(String args[]){
        
        ThreadTester thread1 = new ThreadTester("T1");
        thread1.start();

        try {
            Thread.sleep((int) ((Math.random() * (1000 - 0)) + 0));
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ThreadTester thread2 = new ThreadTester("T2");
        thread2.start();
    }
}