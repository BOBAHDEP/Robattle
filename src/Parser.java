import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Вова on 08.10.2015.
 */
public class Parser {

    //move for several steps
    static final String VALIDATE_STRING_MOVE = "^(moveX|moveY|MoveX|MoveY)(\\s+)(-\\d|\\d)($)";
    static final String SEPARATE_ARGUMENTS_IN_MOVE = "\\s+";
    static final String MOVE_X_SIGNATURE_1 = "moveX";
    static final String MOVE_X_SIGNATURE_2 = "MoveX";
    static final String MOVE_Y_SIGNATURE_1 = "moveY";
    static final String MOVE_Y_SIGNATURE_2 = "MoveY";
    //attack
    static final String VALIDATE_STRING_ATTACK = "^(attackX|attackY|AttackX|AttackY)(\\s+)(1|-1)($)";
    static final String SEPARATE_ARGUMENTS_IN_ATTACK = "\\s+";
    static final int D_LIFE_LEVEL = 1;
    //NaN
    static final String VALIDATE_STRING_NAN = "^(Nan)$";
    //IF ELSE
    static final String VALIDATE_STRING_IF_ELSE = "^(IF|IF\\s+)(\\()(\\s+F|F)(IELD)(\\[|\\s+\\[|\\[\\s+|\\s+\\[\\s+)(\\d|-\\d)(\\]|\\s+\\]|\\]\\s+)(\\[|\\s+\\[|\\[\\s+)(\\d|-\\d)(]|\\s+]|\\]\\s+)(\\s+\\)\\s+|\\)|\\)\\s+|\\s+\\))(moveX|MoveX|moveY|MoveY|attackX|attackY|AttackX|AttackX)(\\d|\\s+\\d|\\s+\\d\\s+)($|(ELSE|ELSE\\s+)(moveX|MoveX|moveY|MoveY|attackX|attackY|AttackX|AttackX)(\\s+\\d|\\d)($))";
    static final String CHOOSE_COORDINATES_IF_ELSE = "\\[\\s*(.*?)\\s*\\]";   //FIELD[0][1] -> 0,1
    static final String CHOOSE_FIRST_COMMAND_IF_ELSE = "\\)\\s*(.*?)\\s*\\d";   //IF (FIELD[0][1]) moveX 1 ELSE moveY 3 -> moveX

    static void parse(String command, Entity entity, PlayMap map) throws InputMismatchException{
        if (validateMove(command)) {
            /*
            String args[] = command.split(Parser.SEPARATE_ARGUMENTS_IN_MOVE);
            if (args.length != 2) {
                throw new InputMismatchException();
            }
            /*
            if (args[0].equals(MOVE_X_SIGNATURE_1) || args[0].equals(MOVE_X_SIGNATURE_2) ) {
                entity.move(Integer.parseInt(args[1]), 0, map);
            } else if (args[0].equals(MOVE_Y_SIGNATURE_1) || args[0].equals(MOVE_Y_SIGNATURE_2) ) {
                entity.move(0, Integer.parseInt(args[1]), map);
            } else {
                throw new InputMismatchException();
            }
            */
            performCommand(command, entity, map);
        } else if (validateAttack(command)) {
            performCommand(command, entity, map);
        } else if (validateNaN(command)) {
            //Nothing
        } else if (validateIfElse(command)) {
            //todo a lot
            List<Integer> coordinates = new ArrayList<Integer>();
            Matcher matcher = Pattern.compile(CHOOSE_COORDINATES_IF_ELSE).matcher(command);
            while(matcher.find()){
                coordinates.add(Integer.parseInt(matcher.group(1)));
            }
            matcher = Pattern.compile(CHOOSE_FIRST_COMMAND_IF_ELSE).matcher(command);
            matcher.find();//?
            System.out.println(matcher.group(1));
        } else {
            throw new InputMismatchException();
        }
    }
    static boolean validateMove(String command) {
        return command.matches(VALIDATE_STRING_MOVE);
    }
    static boolean validateAttack(String command) {
        return command.matches(VALIDATE_STRING_ATTACK);
    }
    static boolean validateNaN(String command) {
        return command.matches(VALIDATE_STRING_NAN);
    }
    static boolean validateIfElse(String command) {
        return command.matches(VALIDATE_STRING_IF_ELSE);
    }
    static void performCommand(String command, Entity entity, PlayMap map) throws InputMismatchException {  //move or attack
        String args[] = command.split(Parser.SEPARATE_ARGUMENTS_IN_MOVE);
        if (args.length != 2) {
            throw new InputMismatchException();
        }
        if (args[0].equals(MOVE_X_SIGNATURE_1) || args[0].equals(MOVE_X_SIGNATURE_2) ) {
            entity.move(Integer.parseInt(args[1]), 0, map);
        } else if (args[0].equals(MOVE_Y_SIGNATURE_1) || args[0].equals(MOVE_Y_SIGNATURE_2) ) {
            entity.move(0, Integer.parseInt(args[1]), map);
        } else if (validateAttack(command)) {
            args = command.split(Parser.SEPARATE_ARGUMENTS_IN_ATTACK);
            if (args[0].endsWith("X")) {
                map.attack(entity.x + Integer.parseInt(args[1]), entity.y, D_LIFE_LEVEL);
            }
        }else {
            throw new InputMismatchException();
        }
    }
}
