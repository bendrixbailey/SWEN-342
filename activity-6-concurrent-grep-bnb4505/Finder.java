import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Generic finder class to look for a specified pattern in a specific file.
 */

public class Finder implements Callable<Found>{

    private static int THREADS = 3;
    private String input;
    public static Pattern pattern;

    /**
     * Constructor for the finder object, takes the pattern 
     * @param pattern   pattern to look for
     * @param input     either file or line
     */
    public Finder(Pattern pattern, String input){
        this(pattern);
        this.input = input;
    }

    protected Finder(Pattern pattern){
        Finder.pattern = pattern;
    }

    /**
     * This function generates a report based on a Found object.
     * Looks through the Found object, which only contains patterns
     * that were found inside a specific file.
     * @param found Found object of found patterns within a file
     */
    private static void report(Found found){
        String fileName = found.getFileName();
        if(fileName == ""){
            System.out.println("Results for input are not in a file");
        }else{
            System.out.println(found.getFileName());
        }
        List<String> matches = found.getPattern();
        if(matches.size() == 0){
            System.out.println("No matches found in file or input");
        }else{
            for(int i = 0; i < matches.size(); i++){
                System.out.println(matches.get(i));
            }
        }
    }

    /**
     * This function searches through a file to look for files, and once done,
     * shuts down the executor service once the finders are done. It then
     * generates a report on each of the finder's results.
     * @param ex executor to be converted to completion service
     * @param finders list of finders for each file
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private static void search(Executor ex, Collection<Finder> finders) throws InterruptedException, ExecutionException {
        CompletionService<Found> completionService = new ExecutorCompletionService<Found>(ex);
        for(Finder f: finders){
            completionService.submit(f);
        }
        ((ExecutorService)ex).shutdown();
        int finderSize = finders.size();
        for(int i = 0; i < finderSize; ++i){
            Found found = completionService.take().get();
            if(found != null){
                report(found);
            }
        }
    }

    /**
     * This function finds a pattern within a string input.
     * @param pattern pattern to look for
     * @param input   input to search
     */
    public static void find(Pattern pattern, String input){
        ExecutorService eService = Executors.newFixedThreadPool(THREADS);
        Collection<Finder> finders = new ArrayList<Finder>(1);
        finders.add(new Finder(pattern, input));
        try {
            search(eService, finders);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        
    }

    /**
     * Function to find pattern within a list of files. Attaches
     * a finder for every single file.
     * @param pattern   pattern to look for
     * @param files     list of files to look through
     */
    public static void find(Pattern pattern, Collection<File> files){
        ExecutorService eService = Executors.newFixedThreadPool(THREADS);
        Collection<Finder> finders = new ArrayList<Finder>(files.size());
        for(File file: files){
            finders.add(new FileFinder(pattern, file));
        }

        try {
            search(eService, finders);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


    /**
     * This function handles the actual matching of the patterns when the finder
     * is called. It checks to see if it mathces, and then if it does, 
     * adds the matches to a Found object, and returns it.
     */
    @Override
    public Found call() throws Exception {
        ArrayList<String> matches = new ArrayList<String>();
        Matcher matcher = pattern.matcher(input);
        if(pattern.toString().startsWith("^") && pattern.toString().endsWith("$")){
            if(matcher.matches()){
                matches.add(input);
            }
        }else{
            if(matcher.find()){
                matches.add(input);
            }
        }
        Found found = new Found("", matches);
        return found;
    }
    
}
