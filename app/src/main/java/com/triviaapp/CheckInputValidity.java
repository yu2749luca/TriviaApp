package com.triviaapp;

/**
 * This is meant to check SQL injection in the interface; (We already have internet codes to check SQL injection in the back);
 * This section is primary meant to strengthen our UI.
 *
 * Created by yu2749luca on 4/11/16.
 */
public class CheckInputValidity {

    int asiccCeiling = 126;
    int asiccFloor = 32;

    int firstQ = 58;
    int lastQ = 62;

    private boolean checkAsicc = true;
    private boolean checkLength = true;
    private boolean checkDrop = true;
    private boolean checkEquation = true;
    private boolean checkAt = true;
    private boolean checkCompare = true;
    private String altermessage;
    boolean overall = false;
    private boolean checkPassword = false;
    boolean checkMessage=true;
    public String testString = "ALL TRUE";

    public CheckInputValidity() {

    }

    //we need to make sure input messages are within the assicCode that we accept
    public boolean checkMessage (String input){
        altermessage=alterMessage(input);
        checkAsicc=checkAsicc(altermessage);
        checkLength=checkLength(input);
        checkMessage=checkAsicc&&checkLength;

        return checkMessage;
    }


    //this is mean to check all the return methods are TRUE
    public boolean overallCheck(String input) {
        altermessage=alterMessage(input);
        checkAsicc = checkAsicc(altermessage);
        checkLength = checkLength(input);
        checkDrop = checkDrop(input);
        checkEquation = checkEquation(input);
        checkAt = checkAt(input);
        checkCompare = checkCompare(input);
        overall = (checkAsicc) && (checkLength) && (checkDrop) && (checkEquation) && (checkAt) && (checkCompare);
        if (!overall) {
            testString = "FALSE";
        }
        return overall;
    }

    /*this is meant to check if the input passwords have the met the requirement of Length>2 and use only assic code


     */
    public boolean checkPassword(String input) {
        checkAsicc = checkAsicc(input);
        checkLength = checkLength(input);
        if ((checkAsicc) && (checkLength)) {
            checkPassword = true;
        }

        return checkPassword;
    }


    /*
    this is a very crucial method. This method is meant to take out "nextLine" in a message; because asiccCode do not read these.
    this method is called first before it is checked if all the characters are in the asicc code
     */
    public String alterMessage(String input) {
        String alterMessage = input;
        alterMessage=alterMessage.replace("\n"," ");
        alterMessage=alterMessage.replace("\r"," ");
        alterMessage=alterMessage.replace("\t"," ");

        return alterMessage;
    }


    /*
    check if all the characters within the messages are within asicc code value that we want
     */
    private boolean checkAsicc(String input) {
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if ((c > asiccCeiling) || (c < asiccFloor)) {
                checkAsicc = false;
            }
        }
        return checkAsicc;

    }

    /*
    make sure the length the mssage >2
     */
    private boolean checkLength(String input) {
        if (input.length() < 2) {
            checkLength = false;
        }

        return checkLength;
    }

    /*

    make sure the message contain no SQL injection of ";droptable"
     */
    private boolean checkDrop(String input) {
        String message = input.replace(" ", "");
        message = message.toLowerCase();
        String check = ";droptable";
        if (message.contains(check)) {
            checkDrop = false;
        }

        return checkDrop;
    }

    /*

  make sure the message contain no SQL injection of "1=1" or "1=5";
  so the users cannot try to input an equation
   */
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
    /*

   make sure the message contain no SQL injection of "4>1" or other kinds;
   so the users cannot try to input an equation
    */
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
    /*

   make sure the message contain no SQL injection of "@ digit";

    */
    private boolean checkAt(String input) {
        String message = input.replace(" ", "");
        message = message.toLowerCase();

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
