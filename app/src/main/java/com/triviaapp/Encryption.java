package com.triviaapp;

/**
 * Created by yu2749luca on 4/11/16.
 */

public class Encryption {

    int asiccCeiling=126;
    int asiccRange=95;
    int asiccFloor=32;
    int [] pattern;

    int passLength;
    int special1;
    int special2;
    int special3;
    int special4;
    int andyAge=22;
    int [] blocks;

    String tail="";

    public Encryption (String password){
        insertPassword(password);
        arrangePattern(password);
        tail(password);
    }

    public String encrypt (String message){
        assert message.length()>0:"error, message not exist";

        String encryptM="";
        int count=0;
        char changedC=' ';
        //System.out.print("block encrypt ");
        while (!message.isEmpty()){
            if (count>pattern.length-1){
                count=count-pattern.length;
            }
            int patternV=pattern[count];
            int blockN=blocks [patternV];
            //System.out.print(" "+blockN);
            changedC=changeC(message.charAt(0),blockN);
            encryptM=encryptM+changedC;
            message=message.substring(1);
            count++;

        }
        //System.out.println();
        return encryptM+tail;
    }

    public String decrypt(String message){
        String decryptM="";
        int count=0;
        char returnC=' ';
        message=message.substring(0, message.length()-special4);
        //System.out.print("block decrypt ");
        while (!message.isEmpty()){
            if (count>pattern.length-1){
                count=count-pattern.length;
            }
            int patternV=pattern[count];
            int blockN=blocks [patternV];
            //System.out.print(" "+blockN);
            returnC=returnC(message.charAt(0),blockN);
            decryptM=decryptM+returnC;
            message=message.substring(1);
            count++;

        }
        //System.out.println();
        return decryptM;
    }

    private void tail (String password){

        for (int i=0;i<special4;i++){
            double drandom= Math.random()*30;
            int intRan=(int) drandom;
            char addC=changeC('a',intRan);
            tail=tail+addC;
        }
    }

    //each position in the array determines how much a character is shifted
    //eventually pass the password on to set the pattern
    public void insertPassword (String password){
        passLength=password.length();
        int passwordValue=passwordValue(password);

        special1=password.length()+passwordValue;
        special2=passwordValue;
        String allCap=password.toUpperCase();
        int allCapValue=passwordValue(allCap);
        special3=allCapValue;
        special4=special1%passLength+1;

        this.blocks=new int [] {special1,special3,andyAge,special2,passLength,special4};
        //this.blocks=new int [] {1,2,3,4,5,6};
    }

    private void arrangePattern (String password){
        int size=2;
        if (password.length()>size){
            size=password.length();
        }
        pattern=new int [size];
        int passwordValue=passwordValue(password);

        for (int i=0;i<pattern.length;i++){
            int wordValue=(int) password.charAt(i)+passwordValue;
            int remain=wordValue%blocks.length;
            pattern [i]=remain;
        }
        //pattern=new int [] {1,2,4,5,3};
        //System.out.println("pattern :"+pattern [2]);
    }
    private int passwordValue (String password){
        int value=0;
        while (!password.isEmpty()){
            value=value+(int)password.charAt(0);
            password=password.substring(1);
        }

        return value;
    }


    private char changeC (char messageC, int shifted){

        int asiccCode=(int) messageC+shifted;

        while (asiccCode>asiccCeiling){
            asiccCode=asiccCode-asiccRange;
        }
        char changeC=(char) asiccCode;
        //System.out.print(" "+asiccCode+" ");
        return changeC;
    }

    private char returnC(char messageC, int shifted){
        int asiccCode=(int) messageC-shifted;

        while (asiccCode<asiccFloor){
            asiccCode=asiccCode+asiccRange;
        }

        char returnC=(char) asiccCode;
        //System.out.print(" "+asiccCode+" ");
        return returnC;
    }
<<<<<<< HEAD
=======




<<<<<<< Updated upstream
>>>>>>> c60b18105c457b9944cb5cd01b4cdc7fbfe08f61
}

=======
}
>>>>>>> Stashed changes
