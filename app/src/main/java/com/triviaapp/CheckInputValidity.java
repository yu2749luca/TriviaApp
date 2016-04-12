package com.triviaapp;

/**
 * Created by yu2749luca on 4/11/16.
 */
public class CheckInputValidity {

    int asiccCeiling = 126;
    int asiccRange = 95;
    int asiccFloor = 32;

    int firstQ = 58;
    int lastQ = 62;

    private boolean checkAsicc = true;
    private boolean checkLength = true;
    private boolean checkDrop = true;
    private boolean checkEquation = true;
    private boolean checkAt = true;
    private boolean checkCompare = true;

    private boolean overall = false;
    private boolean checkPassword = false;

    public String testString = "ALL TRUE";

    public CheckInputValidity() {


    }

    public boolean overallCheck(String input) {
        checkAsicc = checkAsicc(input);
        checkLength = checkLength(input);
        checkDrop = checkDrop(input);
        checkEquation = checkEquation(input);
        checkAt = checkAt(input);
        checkCompare = checkCompare(input);
        overall = (checkAsicc) && (checkLength) && (checkLength) && (checkDrop) && (checkEquation) && (checkAt) && (checkCompare);
        if (!overall) {
            testString = "FALSE";
        }
        return overall;
    }

    public boolean checkPassword(String input) {
        checkAsicc = checkAsicc(input);
        checkLength = checkLength(input);
        if ((checkAsicc) && (checkLength)) {
            checkPassword = true;
        }

        return checkPassword;
    }

    public String alterMessage(String input) {
        String alterMessage = input;


        return alterMessage;
    }

    public boolean checkAsicc(String input) {


        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if ((c > asiccCeiling) || (c < asiccFloor)) {
                checkAsicc = false;
            }
        }
        return checkAsicc;

    }

    private boolean checkLength(String input) {
        if (input.length() < 2) {
            checkLength = false;
        }

        return checkLength;
    }

    private boolean checkDrop(String input) {

        String message = input.replace(" ", "");
        message = message.toLowerCase();
        String check = ";droptable";
        if (message.contains(check)) {
            checkDrop = false;
        }


        return checkDrop;
    }

    private boolean checkEquation(String input) {


        String message = input.replace(" ", "");
        message = message.toLowerCase();

        for (int i = 1; i < message.length() - 1; i++) {
            char c = message.charAt(i);
            int cV = (int) c;
            if ((cV >= firstQ) && (cV <= lastQ)) {
                char checkPrevious = message.charAt(i - 1);
                char checkAfter = message.charAt(i + 1);

                if ((Character.isDigit(checkPrevious) && (Character.isDigit(checkAfter)))) {
                    checkEquation = false;
                    i = message.length();
                }

            }

        }


        return checkEquation;
    }

    private boolean checkCompare(String input) {
        boolean checkSigns = false;
        String message = input.replace(" ", "");
        message = message.toLowerCase();
        for (int i = 2; i < message.length() - 1; i++) {

            if (message.charAt(i) == '=') {
                char previous2 = message.charAt(i - 2);
                char previous = message.charAt(i - 1);
                char after = message.charAt(i + 1);
                boolean checkFrontDigit = Character.isDigit(previous2);
                boolean checkAfterDigit = Character.isDigit(after);

                if ((previous == '!') || (previous == '>') || (previous == '<')) {
                    checkSigns = true;
                }
                if (checkAfterDigit && checkFrontDigit && checkSigns) {
                    checkCompare = false;
                    i = message.length();
                }
            }


        }


        return checkCompare;
    }

    private boolean checkAt(String input) {

        String message = input.replace(" ", "");
        message = message.toLowerCase();
        String check = "@";

        for (int i = 0; i < message.length() - 1; i++) {
            char c = message.charAt(i);
            if (c == '@') {
                char after = message.charAt(i + 1);
                if (Character.isDigit(after)) {
                    checkAt = !Character.isDigit(after);
                    i = message.length();
                }

            }


        }


        return checkAt;
    }

}
