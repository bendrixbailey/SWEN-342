import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Driver {
    public static void main(String[] args) {
        try {
            PrintStream fileOut = new PrintStream("./Trace.txt");
            System.setOut(fileOut);
        } catch (FileNotFoundException e) {}

        int np = 4;
        int nt = 10;
        int tm = 0;
        int em = 0;

        boolean lhanded = false;

        int largs = args.length;

        switch (largs) {
            case 1:
                if(args[0] == "-l"){
                    lhanded = true;
                }else{
                    np = Integer.parseInt(args[1]);
                }
                break;
            case 2:
                if(args[0] == "-l"){
                    lhanded = true;
                    np = Integer.parseInt(args[1]);
                }else{
                    np = Integer.parseInt(args[1]);
                    nt = Integer.parseInt(args[2]);
                }
                break;

            case 3:
                if(args[0] == "-l"){
                    lhanded = true;
                    np = Integer.parseInt(args[1]);
                    nt = Integer.parseInt(args[2]);
                }else{
                    np = Integer.parseInt(args[1]);
                    nt = Integer.parseInt(args[2]);
                    tm = Integer.parseInt(args[3]);
                }
                break;

            case 4:
                if(args[0] == "-l"){
                    lhanded = true;
                    np = Integer.parseInt(args[1]);
                    nt = Integer.parseInt(args[2]);
                    tm = Integer.parseInt(args[3]);
                }else{
                    np = Integer.parseInt(args[1]);
                    nt = Integer.parseInt(args[2]);
                    tm = Integer.parseInt(args[3]);
                    em = Integer.parseInt(args[4]);
                }
                break;

            case 5:
                lhanded = true;
                np = Integer.parseInt(args[1]);
                nt = Integer.parseInt(args[2]);
                tm = Integer.parseInt(args[3]);
                em = Integer.parseInt(args[4]);
                break;

            default:
                break;
        }

        Fork[] forks = new Fork[np];
        for(int i = 0; i < np; i ++){
            Fork tfork = new Fork();
            forks[i] = tfork;
        }

        Philosopher[] philosophers = new Philosopher[np];
        for(int i = 0; i < np; i ++){
            if(i % 2 == 0 == lhanded){
                Philosopher tphil = new Philosopher(i, forks[i], forks[(np + i - 1)%np], false, nt, tm, em);
                philosophers[i] = tphil;
            }else{
                Philosopher tphil = new Philosopher(i, forks[i], forks[(np + i - 1)%np], true, nt, tm, em);
                philosophers[i] = tphil;
            }
        }

        for(Philosopher p: philosophers){
            p.start();
        }
    }
}
