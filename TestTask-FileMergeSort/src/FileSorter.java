import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


public class FileSorter {
    private static Logger logger = Logger.getLogger(FileSorter.class.getName());
    private SortingOrder order;
    private BufferedWriter outputWriter;
    private InputDataType dataType;
    private Map<BufferedReader, Data> readersList; //Reader and last read line

    public FileSorter(File outputFile, List<File> inputFiles, InputDataType dataType, SortingOrder order) throws OutputFileException {
        this.order = order;
        try {
            this.outputWriter = new BufferedWriter(new FileWriter(outputFile));
        } catch (IOException e) {
            throw new OutputFileException(e.getMessage());
        }
        this.readersList = new HashMap<>();

        initFilesInfoMaps(inputFiles);
        this.dataType = dataType;
    }

    private void initFilesInfoMaps(List<File> files) {
        for (File file: files){
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(file));
            } catch (FileNotFoundException e) {
                logger.warning("Cant find file: " + file);
            }
            if (reader != null) {
                this.readersList.put(reader, null);
            }
        }
    }

    private int updateAllLines() throws WrongOrder{ //Update all lines
        int exceptionsAmount = 0;
        for (Iterator<Map.Entry<BufferedReader, Data>> it = this.readersList.entrySet().iterator(); it.hasNext(); ){
            Map.Entry<BufferedReader, Data> entry = it.next();
            try {
                updateLines(entry.getKey());
            }
            catch (StringToIntCastException ex){
                it.remove();
                exceptionsAmount++;
            }
        }
        return exceptionsAmount;
    }

    private void updateLines(BufferedReader needToUpdateReader) throws WrongOrder, StringToIntCastException {
        String inputLine;
        try {
            if ((inputLine = needToUpdateReader.readLine()) == null){
                removeFileFromList(needToUpdateReader);
                return;
            }
        } catch (IOException e) {
            logger.warning("One file has reading error, Stop getting data from this file");
            removeFileFromList(needToUpdateReader);
            return;
        }
        Data data;
        data = new Data(inputLine, this.dataType);
        if (this.readersList.get(needToUpdateReader) == null){
            this.readersList.put(needToUpdateReader, data);
            return;
        }
        int comparingNewAndLastData = data.compareTo(this.readersList.get(needToUpdateReader));
        switch (this.order){
            case DESCENDING:
                if (comparingNewAndLastData > 0 ){
                    removeFileFromList(needToUpdateReader);
                    throw new WrongOrder("File is not descending sorted");
                }
                break;
            case ASCENDING:
                if (comparingNewAndLastData < 0 ){
                    removeFileFromList(needToUpdateReader);
                    throw new WrongOrder("File is not ascending sorted");
                }
                break;
        }
        this.readersList.put(needToUpdateReader, data);
    }

    private void removeFileFromList(BufferedReader reader){
        try {
            reader.close();
        } catch (IOException ignored) {}
        this.readersList.remove(reader);
    }

    private Map.Entry<BufferedReader, Data> getNextOutputValue(){
        Map.Entry<BufferedReader, Data> nextOutputValue = this.readersList.entrySet().iterator().next();
        switch (this.order){
            case DESCENDING:
                for (Map.Entry<BufferedReader, Data> entry: this.readersList.entrySet()){
                    if (nextOutputValue.getValue().compareTo(entry.getValue()) < 0){
                        nextOutputValue = entry;
                    }
                }
                break;
            case ASCENDING:
                for (Map.Entry<BufferedReader, Data> entry: this.readersList.entrySet()){
                    if (nextOutputValue.getValue().compareTo(entry.getValue()) > 0){
                        nextOutputValue = entry;
                    }
                }
                break;
        }
        return nextOutputValue;
    }


    public void sort() throws OutputFileException {
        int filesAmountWithCastException = 0;
        try {
            filesAmountWithCastException = updateAllLines();
        }
        catch (WrongOrder ignored){} //ignored because reading first line from files and cant get WrongOrder
        if (filesAmountWithCastException > 0){
            logger.warning(filesAmountWithCastException + " files has string when dataType == INTEGER. Stop getting data from this files");
        }
        try {
            while (readersList.size() > 0) {
                Map.Entry<BufferedReader, Data> nextOutputValue = getNextOutputValue();
                outputWriter.write(nextOutputValue.getValue().getString());
                outputWriter.newLine();
                try {
                    updateLines(nextOutputValue.getKey());
                } catch (WrongOrder ex) {
                    logger.warning( "One file has wrong order. Stop getting data from this file");
                } catch (StringToIntCastException ex) {
                    removeFileFromList(nextOutputValue.getKey());
                    logger.warning("One file has string when dataType == INTEGER. Stop getting data from this file");
                }
            }
            outputWriter.flush();
        }catch (IOException ex){
            throw new OutputFileException(ex.getMessage());
        }
    }


}
