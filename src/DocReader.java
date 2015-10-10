import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by Вова on 09.10.2015.
 */
public class DocReader {

    private Scanner scanner;

    public DocReader(String filePath) throws FileNotFoundException {
        this.scanner = new Scanner(new File(filePath));
    }

    public String getNextString() {
        if (scanner.hasNextLine()) {
            return scanner.nextLine();
        } else {
            return "endPoint";
        }
    }
}
