import java.io.FileNotFoundException;

/**
 * Created by Вова on 08.10.2015.
 */
public class Main {
    public static void main(String[] args) {
        /*PlayMap map = new PlayMap();
        Entity player1 = new Entity(7, 8, 9);
        Entity player2 = new Entity(8, 8, 9);
        map.addEntity(player1);
        map.addEntity(player2);
        map.consolePrintMap();
        //Parser.parse("moveX 1", player1, map);
        //Parser.parse("moveY 1", player2, map);
        //Parser.parse("attackX 1", player1, map);
        //map.consolePrintMap();
        DocReader docReader = null;
        try {
            docReader = new DocReader("C:\\IDEA_Projects\\Robattle\\1.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }    /*
        System.out.println(docReader.getNextString());
        System.out.println(docReader.getNextString());
        System.out.println(docReader.getNextString());  */   /*
        String command = "0";
        while (!command.equals("endPoint")) {
            command = docReader.getNextString();
            Parser.parse(command, player1, map);
            map.consolePrintMap();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }       */
        String s = "IF (FIELD[0][1]) moveX 1";
        Parser.parse(s, null, null);
    }
}
