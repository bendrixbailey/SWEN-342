import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

import Models.*;


/**
 * This is the main runner for the app. Controls the starting of all the worker threads,
 * the starting of the clock and monitors the statistics.
 */

public class App {

    private static final int numTeamLeads = 3;

    public static void main(String[] args) throws Exception {
        CyclicBarrier managerStandupBarrier = new CyclicBarrier(1 + numTeamLeads);
        CyclicBarrier eodBarrier = new CyclicBarrier(1 + numTeamLeads + 9);
        SystemClock sysClock = new SystemClock();       //start clock for work.

        CyclicBarrier team1StandupBarrier = new CyclicBarrier(4);
        CyclicBarrier team2StandupBarrier = new CyclicBarrier(4);
        CyclicBarrier team3StandupBarrier = new CyclicBarrier(4);

        CyclicBarrier team1QuestionBarrier = new CyclicBarrier(3);
        CyclicBarrier team2QuestionBarrier = new CyclicBarrier(3);
        CyclicBarrier team3QuestionBarrier = new CyclicBarrier(3);

        Manager mgr = new Manager(sysClock, "Malachowsky", managerStandupBarrier, eodBarrier);

        //team lead creation
        TeamLead tl1 = new TeamLead("Sam", sysClock, 1, managerStandupBarrier, team1StandupBarrier, eodBarrier, team1QuestionBarrier, mgr);
        TeamLead tl2 = new TeamLead("Bryce", sysClock, 2, managerStandupBarrier, team2StandupBarrier, eodBarrier, team2QuestionBarrier, mgr);
        TeamLead tl3 = new TeamLead("Annie", sysClock, 3, managerStandupBarrier, team3StandupBarrier, eodBarrier, team3QuestionBarrier, mgr);

        //team 1 creation
        Developer t1d1 = new Developer("Ron", sysClock, 1, 1, team1StandupBarrier, eodBarrier, team1QuestionBarrier, tl1, mgr );
        Developer t1d2 = new Developer("Ben", sysClock, 1, 2, team1StandupBarrier, eodBarrier, team1QuestionBarrier, tl1, mgr );
        Developer t1d3 = new Developer("Ryan", sysClock, 1, 3, team1StandupBarrier, eodBarrier, team1QuestionBarrier, tl1, mgr );

        //team 2 creation
        Developer t2d1 = new Developer("Sue", sysClock, 2, 4, team2StandupBarrier, eodBarrier, team2QuestionBarrier, tl2, mgr );
        Developer t2d2 = new Developer("Jen", sysClock, 2, 5, team2StandupBarrier, eodBarrier, team2QuestionBarrier, tl2, mgr );
        Developer t2d3 = new Developer("Ronnie", sysClock, 2, 6, team2StandupBarrier, eodBarrier, team2QuestionBarrier, tl2, mgr );

        //team 3 creation
        Developer t3d1 = new Developer("Hannah", sysClock, 3, 7, team3StandupBarrier, eodBarrier, team3QuestionBarrier, tl3, mgr );
        Developer t3d2 = new Developer("Michele", sysClock, 3, 8, team3StandupBarrier, eodBarrier, team3QuestionBarrier, tl3, mgr );
        Developer t3d3 = new Developer("Ross", sysClock, 3, 9, team3StandupBarrier, eodBarrier, team3QuestionBarrier, tl3, mgr );

        List<TeamLead> teamLeads = new ArrayList<>(List.of(
            tl1, tl2, tl3
        ));

        List<Developer> devs = new ArrayList<>(List.of(
            t1d1, t1d2, t1d3, t2d1, t2d2, t2d3, t3d1, t3d2, t3d3
        ));
        
        
        sysClock.restart();     //start day
        mgr.start();            //start manager
        for (TeamLead teamLead : teamLeads) {
            teamLead.start();   //start team leads
        }

        for(Developer dev: devs){
            dev.start();
        }
    }
}
