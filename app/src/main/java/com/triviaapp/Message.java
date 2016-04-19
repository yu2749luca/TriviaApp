package com.triviaapp;

/**
 *
 * This is for the list view on the home page. List view items need to encapsulate this class object
 *
 * Created by Andy on 4/12/2016.
 */
public class Message {

    // Holds alias and question
    private String alias;
    private String question;

    // Initializes the private variables to class above
    public Message(String alias, String question){
        this.alias = alias;
        this.question = question;
    }

    // Must override toString() in order to correctly display list view item on home page of app
    @Override
    public String toString(){
        return "Alias: " + alias + "\nQuestion: " + question;
    }

    // Gets the alias of the list view item
    public String getAlias(){
        return this.alias;
    }

    // Gets the question of the list view item
    public String getQuestion(){
        return this.question;
    }
}
