import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args){
        int argsIndex = 0;
        if (args.length < 3){
            logger.log(Level.SEVERE, "Incorrect arguments amount");
            System.exit(1);
        }
        SortingOrder sortingOrder = SortingOrder.ASCENDING;
        if (args[argsIndex].equals("-d")){
            sortingOrder = SortingOrder.DESCENDING;
            argsIndex++;
        }
        else if (args[argsIndex].equals("-a")){
            argsIndex++;
        }
        InputDataType dataType = null;
        if (args[argsIndex].equals("-i")){
            dataType = InputDataType.INTEGER;
            argsIndex++;
        }
        else if (args[argsIndex].equals("-s")){
            dataType = InputDataType.STRING;
            argsIndex++;
        }
        else{
            logger.log(Level.SEVERE, "Unknown argument: " + args[argsIndex]);
            System.exit(2);
        }
        File outputFile = new File(args[argsIndex++]);
        ArrayList<File> inputFiles = new ArrayList<>();
        for (; argsIndex < args.length; ++argsIndex){
            inputFiles.add(new File(args[argsIndex]));
        }
        if (inputFiles.size() == 0){
            logger.log(Level.SEVERE, "At least one input file is required");
            System.exit(3);
        }
        FileSorter fileSorter;
        try {
            fileSorter = new FileSorter(outputFile, inputFiles, dataType, sortingOrder);
            fileSorter.sort();
        } catch (OutputFileException e) {
            logger.log(Level.SEVERE, "Output file: " + outputFile + " does not work correctly: " + e.getMessage());
            System.exit(4);
        }

    }
}
