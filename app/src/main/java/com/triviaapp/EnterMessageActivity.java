package com.triviaapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
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

public class EnterMessageActivity extends AppCompatActivity {

    private String alias;
    private String question;
    private String answer;

    String insertURL = "http://donherwig.com/insertMessage.php";
    RequestQueue requestQueue;

    String error1="Can't leave blank. No foreign characters allowed";
    String error2="Input must be longer than two characters or no sequel injection allowed";
    String error3="Answer must be longer than two characters";

    private String message;

    View dialogView;

    private EditText aliasInput;
    private EditText questionInput;
    private EditText answerInput;

    String a = "<b>Alias</b> - Enter an alias less than 20 characters. This will be used to search and identify your message.<br/><br/>";
    String b = "<b>Trivia Question</b> - Enter a trivia question less 150 characters.<br/><br/>";
    String c = "<b>Trivia Answer</b> - Enter the answer for the trivia question. Answers are automatically lowercase.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);

        if (toolbar != null) {
            toolbar.setTitle("Create Message");
            toolbar.setTitleTextColor(Color.WHITE);
        }

        setSupportActionBar(toolbar);
        showEditDialog();

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        Button send = (Button) findViewById(R.id.send_button);
        if (send != null) {
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText m = (EditText) findViewById(R.id.message);
                    message = m != null ? m.getText().toString() : "";
                    CheckInputValidity checkM=new CheckInputValidity();
                    boolean checkMessage=checkM.checkAsicc(message);
                    if(message.isEmpty()||!checkMessage)
                    {

                        if (m != null) {
                            m.setError(error1);
                        }
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
                        final Encryption userMessage = new Encryption(answer);
                        StringRequest request = new StringRequest(Request.Method.POST, insertURL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //listen for response from server and let user know if alias is already used
                                if (!response.isEmpty()) {
                                    Toast.makeText(EnterMessageActivity.this, response, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(EnterMessageActivity.this, "Message Sent!", Toast.LENGTH_SHORT).show();
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
                                parameters.put("message", userMessage.encrypt(message));
                                parameters.put("question", question);

                                return parameters;
                            }
                        };
                        requestQueue.add(request);
                    }

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        showEditDialog();
    }

    private void showEditDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.CreateDialogTheme);
        LayoutInflater inflater = this.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.create_dialog, null);

        aliasInput = (EditText) dialogView.findViewById(R.id.alias);
        questionInput = (EditText) dialogView.findViewById(R.id.question);
        answerInput = (EditText) dialogView.findViewById(R.id.answer);

        dialogBuilder.setView(dialogView).setTitle("Please enter the following").setMessage(Html.fromHtml(a + b + c));

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        }).setPositiveButton("Next", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        }).setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    finish();
                    arg0.dismiss();
                } else if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    return false;
                }
                return false;
            }
        });

        aliasInput.setText(alias);
        questionInput.setText(question);
        answerInput.setText(answer);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alias = aliasInput.getText().toString();
                        question = questionInput.getText().toString();
                        answer = answerInput.getText().toString();
                        CheckInputValidity checkA=new CheckInputValidity();
                        boolean checkAlias=checkA.overallCheck(alias);
                        CheckInputValidity checkQ=new CheckInputValidity();
                        boolean checkQuestion=checkQ.overallCheck(question);
                        CheckInputValidity checkR=new CheckInputValidity();
                        boolean checkAnswer=checkR.checkPassword(answer);
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

