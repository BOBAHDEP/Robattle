import javax.annotation.processing.FilerException;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.FileSystemException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by Вова on 09.10.2015.
 */
public class DocReader {

    private Scanner scanner;

    private boolean needNewCommand = true;
    private String previousCommand = null;

    public DocReader(String filePath) throws FileNotFoundException {
        this.scanner = new Scanner(new File(filePath));
    }

    private String getNextString() {
        if (scanner.hasNextLine()) {
            return scanner.nextLine();
        } else {
            return "endPoint";
        }
    }

    private boolean executeNextString (boolean needNewString, String previousCommand, Entity entity, PlayMap map) throws FileSystemException{  //false = endOfFile
        if (!needNewString) {
            Parser.parse(previousCommand, entity, map);
        } else {
            String command = getNextString();
            this.previousCommand = command;
            if (command.equals("endPoint")) {
                return false;
            }
//            try {
              return needNewCommand = Parser.parse(command, entity, map);
//            } catch (InputMismatchException e){
//                throw new FileSystemException("Wrong string");
//            }
        }

        return true;
    }

    public boolean perform(Entity entity, PlayMap map) throws FileSystemException{
        if (previousCommand == null) {  //first run
            return executeNextString(true, "", entity, map);
        }
        return executeNextString(needNewCommand, previousCommand, entity, map);
    }
}
