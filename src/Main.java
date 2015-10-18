import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Вова on 08.10.2015.
 */
public class Main {
    public static void main(String[] args) {
        PlayMap map = new PlayMap();
        Entity player1 = new Entity(7, 8, 9);  /*
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
        try {
            DocReader docReader = new DocReader("C:\\IDEA_Projects\\Robattle\\1.rbtl");
            map.addEntity(player1);
            map.consolePrintMap();
            while (docReader.perform(player1, map)) {
                //map.consolePrintMap();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

                              /*
        String s = "moveX 1";
        Parser.performCommand(s, player1, map);      /*
        String a = "moveX -5";
        System.out.println(Parser.validateMove(a));  */ /*
        String rs = "asfdadFIELD_SIZEdvklnkFIELD_SIZE";
        rs.replaceAll("FIELD_SIZE", "9");
        System.out.println(rs); */


    }
}
