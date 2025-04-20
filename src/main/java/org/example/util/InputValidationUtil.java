package org.example.util;

import org.example.constants.NumConstants;

import java.util.ArrayList;
import static org.example.constants.InfoMessage.*;

public class InputValidationUtil {
    private static ArrayList<Character> digits;
    private static ArrayList<Character> latin;
    private static ArrayList<Character> nameCharacters;
    private static ArrayList<Character> loginCharacters;
    private static ArrayList<Character> passwordCharacters;
    private static ArrayList<Character> emailCharacters;


    public InputValidationUtil() {
        digits = new ArrayList<>(10);
        digits.add('0'); digits.add('1'); digits.add('2'); digits.add('3'); digits.add('4');
        digits.add('5'); digits.add('6'); digits.add('7'); digits.add('8'); digits.add('9');

        latin = new ArrayList<>(52);
        latin.add('A'); latin.add('B'); latin.add('C'); latin.add('D'); latin.add('E'); latin.add('F'); latin.add('G');
        latin.add('H'); latin.add('I'); latin.add('J'); latin.add('K'); latin.add('L'); latin.add('M'); latin.add('N');
        latin.add('O'); latin.add('P'); latin.add('Q'); latin.add('R'); latin.add('S'); latin.add('T'); latin.add('U');
        latin.add('V'); latin.add('W'); latin.add('X'); latin.add('Y'); latin.add('Z'); latin.add('a'); latin.add('b');
        latin.add('c'); latin.add('d'); latin.add('e'); latin.add('f'); latin.add('g'); latin.add('h'); latin.add('i');
        latin.add('j'); latin.add('k'); latin.add('l'); latin.add('m'); latin.add('n'); latin.add('o'); latin.add('p');
        latin.add('q'); latin.add('r'); latin.add('s'); latin.add('t'); latin.add('u'); latin.add('v'); latin.add('w');
        latin.add('x'); latin.add('y'); latin.add('z');

        loginCharacters = new ArrayList<>(62);
        loginCharacters.addAll(digits);
        loginCharacters.addAll(latin);

        passwordCharacters = new ArrayList<>(62);
        passwordCharacters.addAll(digits);
        passwordCharacters.addAll(latin);
    }

    private static boolean areAllSymbolsInSet(String input, ArrayList<Character> pattern) {
        if (input == null || input.isBlank()) return true;
        char[] chars = input.toCharArray();
        for (char aChar : chars) {
            if (!pattern.contains(aChar)) return false;
        }
        return true;
    }


    public String acceptableLogin(String login) {
        if (login == null || login.isBlank()) return LOGIN_CANNOT_BE_EMPTY.getValue();
        if (login.length() < NumConstants.MIN_LOGIN_LENGTH)
            return "The minimum login length must be " + NumConstants.MIN_LOGIN_LENGTH + " symbols";
        if (login.length() > NumConstants.MAX_LOGIN_LENGTH)
            return "The maximum login length must be " + NumConstants.MAX_LOGIN_LENGTH + " symbols";
        if (!areAllSymbolsInSet(login, loginCharacters)) {
            return INVALID_LOGIN_CHARACTERS.getValue();
        } else {
            return "";
        }
    }

    public String acceptablePassword(String password) {
        if (password == null || password.isBlank()) return PASSWORD_CANNOT_BE_EMPTY.getValue();
        if (password.length() < NumConstants.MIN_PASSWORD_LENGTH)
            return "The minimum password length must be " + NumConstants.MIN_PASSWORD_LENGTH + " symbols";
        if (password.length() > NumConstants.MAX_PASSWORD_LENGTH)
            return "The maximum password length must be " + NumConstants.MAX_PASSWORD_LENGTH + " symbols";
        if (!areAllSymbolsInSet(password, passwordCharacters)) {
            return INVALID_PASSWORD_CHARACTERS.getValue();
        } else {
            return "";
        }
    }

}
