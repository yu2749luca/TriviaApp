package com.triviaapp;

/**
 *
 * this class is meant to encrypt and decrypt the message that users input
 *
 *
 * Created by yu2749luca on 4/11/16.
 */

public class Encryption {

    int asiccCeiling = 126;
    int asiccRange = 95;
    int asiccFloor = 32;
    int[] pattern; //pattern of the blocks that are being used.


    /*values for the blocks that are shifting
    andyAge is also being used as a hard-coded value; rest are all variables depending on the input password/answer
     */
    int passLength;
    int special1;
    int special2;
    int special3;
    int special4;
    int andyAge = 22;
    int[] blocks;

    String tail = ""; //string added to the ending encrypted message


    /*
    make sure the method is called only when the password (answer for trivia question) is inputted.
     */
    public Encryption(String password) {
        password=password.toLowerCase();
        insertPassword(password);
        arrangePattern(password);
        tail(password);
    }
    /*
    this method will break the message into arrays of strings without "nextLine";
    then it will call the encrypt method and encrypt each line of string;
    last, it will form the all the encrypted messages back to what it supposed to look like

    @param message that is meant to be encrypted
    @return encrypted message
     */
    public String encode (String message){
        String [] seperate=message.split("\n"); //meant to break the message that has "nextLin"
        String result="";
        for (int i=0;i<seperate.length;i++){
            String read=seperate[i];
            if(!read.isEmpty()){
            String encrypt=encrypt(read);
            result=result+"\n"+encrypt;}
        }

        return result;
    }

      /*
    this method will break the message into arrays of strings without "nextLine";
    then it will call the decrypt method and decrypt each line of string;
    last, it will form the all the decrypted messages back to what it supposed to look like


    @param message that is meant to be decrypted
    @return decrypted message


     */

    public String decode (String message){
        String [] seperate=message.split("\n");
        String result="";
        for (int i=0;i<seperate.length;i++){
            String read=seperate[i];
            if(!read.isEmpty()){
            String decrypt=decrypt(read);
            result=result+"\n"+decrypt;}
        }

        return result;
    }


    /*
    @param a line of message
    @return encrypted line of the message
     */
    private String encrypt(String message) {
        //assert message.length() > 0 : "error, message not exist";
        String encryptM = "";
        int count = 0;
        char changedC = ' ';
        while (!message.isEmpty()) {
            if (count > pattern.length - 1) {
                count = count - pattern.length;
            }
            int patternV = pattern[count];
            int blockN = blocks[patternV];
            //System.out.print(" "+blockN);
            changedC = changeC(message.charAt(0), blockN);
            encryptM = encryptM + changedC;
            message = message.substring(1);
            count++;
        }
        //System.out.println();
        return encryptM + tail;
    }

    private String decrypt(String message) {
        String decryptM = "";
        int count = 0;
        char returnC = ' ';
        message = message.substring(0, message.length() - special4);
        //System.out.print("block decrypt ");
        while (!message.isEmpty()) {
            if (count > pattern.length - 1) {
                count = count - pattern.length;
            }
            int patternV = pattern[count];
            int blockN = blocks[patternV];
            //System.out.print(" "+blockN);
            returnC = returnC(message.charAt(0), blockN);
            decryptM = decryptM + returnC;
            message = message.substring(1);
            count++;
        }
        //System.out.println();
        return decryptM;
    }


    /*
    add random amount of characters at the end the encrypted message to prevent reverse engineering
     */
    private void tail(String password) {
        for (int i = 0; i < special4; i++) {
            double drandom = Math.random() * 30;
            int intRan = (int) drandom;
            char addC = changeC('a', intRan);
            tail = tail + addC;
        }
    }

    //each position in the array determines how much a character is shifted
    //eventually pass the password on to set the pattern
    public void insertPassword(String password) {
        passLength = password.length();
        int passwordValue = passwordValue(password);

        String allCap = password.toUpperCase();

        special1 = password.length() + passwordValue;
        special2 = passwordValue;
        special3 = passwordValue(allCap);
        special4 = special1 % passLength + 1;

        this.blocks = new int[]{special1, special3, andyAge, special2, passLength, special4};
        //this.blocks=new int [] {1,2,3,4,5,6};
    }


    /*
    this generates a pattern of how the blocks are being used

    @param input password
    @return an array of number that can be used to determine the pattern of which blocks to be used
     */
    private void arrangePattern(String password) {
        int size = 2; //set minimum length of password
        if (password.length() > size) {
            size = password.length();
        }
        pattern = new int[size];
        int passwordValue = passwordValue(password);

        for (int i = 0; i < pattern.length; i++) {
            int wordValue = (int) password.charAt(i) + passwordValue;
            int remain = wordValue % blocks.length;
            pattern[i] = remain;
            // there are 6 blocks; thus, remain cannot be greater than block length
        }

    }
    /*
    sum of the value of password's Asicc code

    @param password
    @return sum of the passwords' Asicc Code
     */
    private int passwordValue(String password) {
        int value = 0;
        while (!password.isEmpty()) {
            value = value + (int) password.charAt(0);
            password = password.substring(1);
        }

        return value;
    }

    /*
    change the character based on the value (encrypt)

    @param (original character, value of how much needs to be shifted)
    @return character of the shifted character
     */
    private char changeC(char messageC, int shifted) {

        int asiccCode = (int) messageC + shifted;

        while (asiccCode > asiccCeiling) {
            asiccCode = asiccCode - asiccRange;
        }
        //so if the value is over the ceiling, it will minus the range

        return (char) asiccCode;
    }

        /*
    change the character based on the value (decrypt)
    reverse method changeC

    @param (encrypted character, value of how much to shift back)
    @return character after shifted
     */

    private char returnC(char messageC, int shifted) {
        int asiccCode = (int) messageC - shifted;

        while (asiccCode < asiccFloor) {
            asiccCode = asiccCode + asiccRange;
        }

        System.out.print(" "+asiccCode+" ");
        return (char) asiccCode;
    }
}
