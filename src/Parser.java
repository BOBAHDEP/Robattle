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
    private static final String VALIDATE_STRING_MOVE = "^(\\s*)(moveX|moveY|MoveX|MoveY)(\\s*)(-\\d+|\\d+)(\\s*)($)";
    private static final String SEPARATE_ARGUMENTS_IN_MOVE_OR_ATTACK = "\\s*(attackX|AttackX|attackY|AttackY|moveX|moveY|MoveX|MoveY)\\s*(\\d+|-\\d+)\\s*";
    private static final String MOVE_X_SIGNATURE_1 = "moveX";
    private static final String MOVE_X_SIGNATURE_2 = "MoveX";
    private static final String MOVE_Y_SIGNATURE_1 = "moveY";
    private static final String MOVE_Y_SIGNATURE_2 = "MoveY";
    //attack
    private static final String VALIDATE_STRING_ATTACK = "^(\\s*)(attackX|attackY|AttackX|AttackY)(\\s*)(1|-1)(\\s*)($)";
    private static final String SEPARATE_ARGUMENTS_IN_ATTACK = "\\s*(attackX|AttackX|attackY|AttackY)\\s*(\\d|-\\d)\\s*";
    private static final int D_LIFE_LEVEL = 1;
    //NaN
    private static final String VALIDATE_STRING_NAN = "^(Nan|\\s*)$";
    //IF ELSE
    private static final String VALIDATE_STRING_IF_ELSE = "^(IF|IF\\s+)(\\()(\\s*)((FIELD)(\\[|\\s+\\[|\\[\\s+|\\s+\\[\\s+)(\\d+|-\\d+)\\s*(\\]|\\s+\\]|\\]\\s+)(\\[|\\s+\\[|\\[\\s+)(\\d+|-\\d+)(]|\\s+]|\\]\\s+)\\s*|\\s*true\\s*|\\s*false\\s*)\\)\\s*((moveX|MoveX|moveY|MoveY|attackX|attackY|AttackX|AttackX)(\\d+|\\s+\\d+|\\s+\\d+\\s+)|\\s*PRINT\\s*\".*?\"\\s*)($|(ELSE|ELSE\\s+)((moveX|MoveX|moveY|MoveY|attackX|attackY|AttackX|AttackX)(\\s+\\d+|\\d+)\\s*($)|\\s*PRINT\\s*\".*?\"\\s*$))";
    private static final String CHOOSE_COORDINATES_IF_ELSE = "\\[\\s*(.*?)\\s*\\]";   //FIELD[0][1] -> 0,1
    private static final String CHOOSE_FIRST_COMMAND_IF_ELSE = "\\)\\s*(.*?)($|\\s*E)";   //IF (FIELD[0][1]) moveX 1 ELSE moveY 3 -> moveX 1
    private static final String CHOOSE_SECOND_COMMAND_IN_IF_ELSE = "ELSE\\s*(.*?)\\s*$";    // -> moveY 3
    //replace constants and variables
    private static final String REPLACE_FIELD_SIZE = "FIELD_SIZE";
    private static final String REPLACE_X_COORDINATE = "MY_X";
    private static final String REPLACE_Y_COORDINATE = "MY_Y";
    //replace math operations
    private static final String REPLACE_PLUS = "(\\d+)\\s*\\+\\s*(\\d+)";
    private static final String REPLACE_MULTIPLY = "(\\d+)\\s*\\*\\s*(\\d+)";
    private static final String REPLACE_MINUS = "(\\d+)\\s*\\*\\s*(\\d+)";
    private static final String REPLACE_DIVIDE = "(\\d+)\\s*\\/\\s*(\\d+)";

    private static final String REPLACE_OPERATION_FIRST_PART = "(\\d+)\\s*\\";
    private static final String REPLACE_OPERATION_SECOND_PART = "\\s*(\\d+)";

    private static final String COMMENT_ONE_Str = "(##.*?)$";
    private static final String COMMENT_TWO_Str = "#(.*?)#";
    //printout
    private static final String PRINT_COMMAND = "^\\s*PRINT\\s*\"(.*?)\"\\s*$";

    public static boolean parse(String command, Entity entity, PlayMap map) throws InputMismatchException{     //true - OK, next string

        command = replaceComments(command);                                                                                                //false - stop, perform again
        command = compileVariables(command, entity);
        command = compileMathOperations(command);

        if (validateMove(command)) {
            /*
            String args[] = command.split(Parser.SEPARATE_ARGUMENTS_IN_MOVE_OR_ATTACK);
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
            if (command.contains("true")) {
                Matcher matcher = Pattern.compile(CHOOSE_FIRST_COMMAND_IF_ELSE).matcher(command);
                if(!matcher.find()) {
                    throw new InputMismatchException(command);
                }
                performCommand(matcher.group(1), entity, map);
                return true;
            } else if (command.contains("false")) {
                if (command.contains("ELSE")) {
                    Matcher matcher = Pattern.compile(CHOOSE_SECOND_COMMAND_IN_IF_ELSE).matcher(command);
                    if(!matcher.find()) {
                        throw new InputMismatchException();
                    }
                    performCommand(matcher.group(1), entity, map);
                    return true;
                }
            }
            List<Integer> coordinates = new ArrayList<Integer>();   //IF (FIELD[0][1]) moveX 1 ELSE attack 2 -> 0,1
            Matcher matcher = Pattern.compile(CHOOSE_COORDINATES_IF_ELSE).matcher(command);
            while(matcher.find()){
                coordinates.add(Integer.parseInt(matcher.group(1)));
            }
            matcher = Pattern.compile(CHOOSE_FIRST_COMMAND_IF_ELSE).matcher(command);
            if(!matcher.find()) {
                throw new InputMismatchException();
            }
            String commandInIF = matcher.group(1);

            boolean performedFirstCommand = false;
            if (map.isTaken(coordinates.get(0), coordinates.get(1))) {
                performCommand(commandInIF, entity, map);
                performedFirstCommand = true;
            }
            if (command.contains("ELSE") && !performedFirstCommand) {
                matcher = Pattern.compile(CHOOSE_SECOND_COMMAND_IN_IF_ELSE).matcher(command);
                if(!matcher.find()) {
                    throw new InputMismatchException(command + " !matcher.find()#1");
                }
                performCommand(matcher.group(1), entity, map);
            }
        } else if (validatePrint(command)) {
            Matcher matcher = Pattern.compile(PRINT_COMMAND).matcher(command);
            if(!matcher.find()) {
                throw new InputMismatchException();
            }
            String stringToPrint = matcher.group(1);
            entity.say(stringToPrint);
        } else {
            throw new InputMismatchException(command + " #3");
        }
        return true;
    }
    private static boolean validateMove(String command) {
        return command.matches(VALIDATE_STRING_MOVE);
    }
    private static boolean validateAttack(String command) {
        return command.matches(VALIDATE_STRING_ATTACK);
    }
    private static boolean validateNaN(String command) {
        return command.matches(VALIDATE_STRING_NAN);
    }
    private static boolean validateIfElse(String command) {
        return command.matches(VALIDATE_STRING_IF_ELSE);
    }
    private static void performCommand(String command, Entity entity, PlayMap map) throws InputMismatchException {  //move or attack or print
        if (validatePrint(command)) {
            Matcher matcher = Pattern.compile(PRINT_COMMAND).matcher(command);
            if (!matcher.find()) {
                throw new InputMismatchException();
            }
            String stringToPrint = matcher.group(1);
            entity.say(stringToPrint);
            return;
        }
        Matcher matcher = Pattern.compile(SEPARATE_ARGUMENTS_IN_MOVE_OR_ATTACK).matcher(command); //  move or attack
        if(!matcher.find()) {
            throw new InputMismatchException(command + " !matcher.find()#2");
        }
        String args[] = {matcher.group(1), matcher.group(2)};
//        System.out.println(args[0]);
//        System.out.println(args[1]);
        if (args.length != 2) {
            throw new InputMismatchException("args.length != 2");
        }
        if (args[0].equals(MOVE_X_SIGNATURE_1) || args[0].equals(MOVE_X_SIGNATURE_2) ) {
            entity.move(Integer.parseInt(args[1]), 0, map);
        } else if (args[0].equals(MOVE_Y_SIGNATURE_1) || args[0].equals(MOVE_Y_SIGNATURE_2) ) {
            entity.move(0, Integer.parseInt(args[1]), map);
        } else if (validateAttack(command)) {
            matcher = Pattern.compile(SEPARATE_ARGUMENTS_IN_ATTACK).matcher(command);
            if(!matcher.find()) {
                throw new InputMismatchException(command + " !matcher.find()#2");
            }
            args[0] = matcher.group(1);
            args[1] = matcher.group(2);
            if (Integer.parseInt(args[1]) != 1 | Integer.parseInt(args[1]) != -1) {
                throw new InputMismatchException();
            }
            if (args[0].endsWith("X")) {
                map.attack(entity.x + Integer.parseInt(args[1]), entity.y, D_LIFE_LEVEL);
            } else if (args[0].endsWith("Y")) {
                map.attack(entity.x, entity.y + Integer.parseInt(args[1]), D_LIFE_LEVEL);
            }
        }else {
            throw new InputMismatchException();
        }
    }
    private static String replaceInAllStr(String whereToReplace, String whatToReplace, String replaceWithWhat) {
        return whereToReplace.replaceAll(whatToReplace, replaceWithWhat);
    }
    private static String compileVariables(String command, Entity entity) {
        String result;
        result = replaceInAllStr(command, REPLACE_FIELD_SIZE, String.valueOf(PlayMap.MAX_FIELD_SIZE));
        result = replaceInAllStr(result, REPLACE_X_COORDINATE, String.valueOf(entity.x));
        result = replaceInAllStr(result, REPLACE_Y_COORDINATE, String.valueOf(entity.y));
        return result;
    }
    private static String compileMathOperations(String command) {
        String res;
        res = replaceMathUniqueOperation('*', command);
        res = replaceMathUniqueOperation('/', res);
        res = replaceMathUniqueOperation('+', res);
        res = replaceMathUniqueOperation('-', res);
        res = replaceMathUniqueOperation('=', res);
        res = replaceMathUniqueOperation('^', res);
        return res;
    }
    private static String replaceMathUniqueOperation(char operation, String command) {
        String regEx = REPLACE_OPERATION_FIRST_PART + String.valueOf(operation) + REPLACE_OPERATION_SECOND_PART;
        Matcher matcher = Pattern.compile(regEx).matcher(command);
        while(matcher.find()){
            String resultValue = "0";
            switch (operation) {
                case '+':
                    resultValue = String.valueOf(Integer.parseInt(matcher.group(1)) + Integer.parseInt(matcher.group(2)));
                    break;
                case '*':
                    resultValue = String.valueOf(Integer.parseInt(matcher.group(1)) * Integer.parseInt(matcher.group(2)));
                    break;
                case '/':
                    resultValue = String.valueOf(Integer.parseInt(matcher.group(1)) / Integer.parseInt(matcher.group(2)));
                    break;
                case '-':
                    resultValue = String.valueOf(Integer.parseInt(matcher.group(1)) - Integer.parseInt(matcher.group(2)));
                    break;
                case '=':
                    resultValue = "(" + String.valueOf(Integer.parseInt(matcher.group(1)) == Integer.parseInt(matcher.group(2))) + ")";
                    break;
                case '^':
                    resultValue = "(" + String.valueOf(Integer.parseInt(matcher.group(1)) != Integer.parseInt(matcher.group(2))) + ")";
            }
            command = command.replaceFirst(regEx, resultValue);
        }
        return command;
    }
    private static String replaceComments(String command) {
        command = command.replaceAll(COMMENT_ONE_Str, "\n");
        command = command.replaceAll(COMMENT_TWO_Str, "");
        return command;
    }
    private static boolean validatePrint(String command) {
        return command.matches(PRINT_COMMAND);
    }
}
