package com.triviaapp;

/**
 * Created by Andy on 4/12/2016.
 */
public class Message {

    private String alias;
    private String question;

    public Message(String alias, String question){
        this.alias = alias;
        this.question = question;
    }

    @Override
    public String toString(){
        return "Alias: " + alias + "\nQuestion:" + question;
    }

    public String getAlias(){
        return this.alias;
    }

    public String getQuestion(){
        return this.question;
    }
}
