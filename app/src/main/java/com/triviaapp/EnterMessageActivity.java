package com.triviaapp;

/*
* This is the
*
*
* */


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


/* this class is for users entering alias (ID), question, answer (password); then last, message
* All the SQL Injection and encryption classes are called here.
*
* */

public class EnterMessageActivity extends AppCompatActivity {

    // These will be used to hold the user's input for the alias, trivia question and answer
    private String alias;
    private String question;
    private String answer;

    // URL of the server
    String insertURL = "http://donherwig.com/insertMessage.php";
    // Mechanism for sending off HTTP GET and POST calls
    RequestQueue requestQueue;

    // Error messages used for validating input from user for alias, trivia question and answer
    String error1="Can't leave blank. No foreign characters allowed";
    String error2="Input must be longer than two characters or no sequel injection allowed";
    String error3="Answer must be longer than two characters";

    // This will hold the message created from user
    private String message;

    // Reference to alert dialog view to retrieve user input
    View dialogView;

    // References to the edit text inputs of the alert dialog
    private EditText aliasInput;
    private EditText questionInput;
    private EditText answerInput;

    // These are the error messages for input validation
    String a = "<b>Alias</b> - Enter an alias less than 20 characters. This will be used to search and identify your message.<br/><br/>";
    String b = "<b>Trivia Question</b> - Enter a trivia question less than 150 characters.<br/><br/>";
    String c = "<b>Trivia Answer</b> - Enter the answer for the trivia question. Answer must be less than 50 characters.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the activity's layout
        setContentView(R.layout.activity_enter_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        // Set up tool bar's title and text color
        if (toolbar != null) {
            toolbar.setTitle("Create Message");
            toolbar.setTitleTextColor(Color.WHITE);
        }

        // Set the tool bar on activity page
        setSupportActionBar(toolbar);
        // Show the alert dialog for user input on creating message
        showEditDialog();

        // Set up post and get mechanism
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        Button send = (Button) findViewById(R.id.send_button);
        // Set up on click listeners for the alert dialog
        if (send != null) {
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get input from user for message
                    EditText m = (EditText) findViewById(R.id.message);
                    message = m != null ? m.getText().toString() : "";
                    // Check the message
                    CheckInputValidity checkM=new CheckInputValidity();
                    boolean checkMessage=checkM.checkMessage(message);
                    // Check message is empty or invalid
                    if(message.isEmpty()||!checkMessage)
                    {
                        // Set error messages
                        if (m != null) {
                            m.setError(error1);
                        }

                        // Set up snackbar and show it after error
                        Snackbar snack = Snackbar.make(v, "Message cannot be blank!", Snackbar.LENGTH_SHORT)
                                .setActionTextColor(Color.WHITE);
                        View snackView = snack.getView();
                        snackView.setBackgroundColor(Color.parseColor("#00CCFF"));
                        TextView  text = (TextView)snackView.findViewById(android.support.design.R.id.snackbar_text);
                        text.setTextColor(Color.WHITE);
                        text.setGravity(Gravity.CENTER_HORIZONTAL);
                        snack.show();
                    }
                    else
                    {
                        // Check network connection
                        if(isNetworkConnected())
                        {
                            // Set up encryption
                            final Encryption userMessage = new Encryption(answer);
                            // Set up HTTP Post request
                            StringRequest request = new StringRequest(Request.Method.POST, insertURL, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    //listen for response from server and let user know if alias is already used
                                    if (!response.isEmpty()) {
                                        Toast.makeText(EnterMessageActivity.this, response, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(EnterMessageActivity.this, "Message Sent!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                //this returns error if there is runtime error on server
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(EnterMessageActivity.this, "server error", Toast.LENGTH_SHORT).show();
                                }
                            }) {
                                //These are the values sent to the server
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> parameters = new HashMap<>();
                                    parameters.put("alias", alias);
                                    parameters.put("message", userMessage.encode(message));
                                    parameters.put("question", question);

                                    return parameters;
                                }
                            };
                            requestQueue.add(request);
                        }
                        else
                        {
                            // Show dialog to prompt user there is no internet connection
                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(EnterMessageActivity.this);
                            builder.setTitle("Internet connection unavailable");
                            builder.setMessage("Please turn on your connection");

                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.show();
                        }
                    }

                }
            });
        }
    }

    // This checks if network connection is established
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    // If back button is pressed, show edit dialog
    @Override
    public void onBackPressed() {
        showEditDialog();
    }

    // This creates alert dialog for getting user input to alias, question and answer
    private void showEditDialog() {
        // Set up layout for dialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.CreateDialogTheme);
        LayoutInflater inflater = this.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.create_dialog, null);

        // Get references to edit text inputs of dialog
        aliasInput = (EditText) dialogView.findViewById(R.id.alias);
        questionInput = (EditText) dialogView.findViewById(R.id.question);
        answerInput = (EditText) dialogView.findViewById(R.id.answer);

        // Set up view, title and message
        dialogBuilder.setView(dialogView).setTitle("Please enter the following").setMessage(Html.fromHtml(a + b + c));

        // Set onclick listener for buttons on dialog
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {}
        }).setPositiveButton("Next", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {}
        }).setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                // If back button is pressed, exit activity
                // If not, return false to do nothing
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    finish();
                    arg0.dismiss();
                } else if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    return false;
                }
                return false;
            }
        });

        // On resume to dialog, load user's input back into edit text
        aliasInput.setText(alias);
        questionInput.setText(question);
        answerInput.setText(answer);

        // Set up positive and negative buttons on alert dialog
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // Get alias, question and answer from user input
                        alias = aliasInput.getText().toString();
                        question = questionInput.getText().toString();
                        answer = answerInput.getText().toString().toLowerCase();

                        // Check if input is valid
                        CheckInputValidity check=new CheckInputValidity();
                        boolean checkAlias=check.overallCheck(alias);
                        boolean checkQuestion=check.overallCheck(question);
                        boolean checkAnswer=check.checkPassword(answer);

                        // If not valid, set the error messages appropriately
                        if (!checkAlias || !checkQuestion || !checkAnswer) {
                            if (!checkAlias) {
                                aliasInput.setError(error2);
                            }
                            if (!checkQuestion) {
                                questionInput.setError(error2);
                            }
                            if (!checkAnswer) {
                                answerInput.setError(error3);
                            }
                        }
                        else {
                            dialog.dismiss();
                        }
                    }
                });

                Button n = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                n.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                        dialog.dismiss();
                    }
                });
            }
        });

        alertDialog.show();
    }

}

