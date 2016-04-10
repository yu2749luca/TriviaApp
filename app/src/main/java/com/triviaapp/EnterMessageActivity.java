package com.triviaapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EnterMessageActivity extends Activity {

    private String alias;
    private String question;
    private String answer;

    String error = "Field cannot be left blank";

    private String message;

    View dialogView;

    private EditText aliasInput;
    private EditText questionInput;
    private EditText answerInput;

    String a = "<b>Alias</b> - Enter a unique identifier for your message. This will be used to search for it.<br/><br/>";
    String b = "<b>Trivia Question</b> - Enter a trivia question less 100 characters.<br/><br/>";
    String c = "<b>Trivia Answer</b> - Enter the answer for the trivia question. Answers are automatically lowercase.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_message);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        showEditDialog(false);
        Button send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText m = (EditText) findViewById(R.id.message);
                message = m.getText().toString();
                if(message.isEmpty())
                {
                    Toast.makeText(EnterMessageActivity.this, "Message cannot be blank!", Toast.LENGTH_LONG).show();
                }
                else {
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        showEditDialog(true);
    }

    private void showEditDialog(boolean edit) {
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
        });

        dialogBuilder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        dialogBuilder.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    finish();
                    arg0.dismiss();
                }
                return true;
            }
        });

        if(edit) {
            aliasInput.setText(alias);
            questionInput.setText(question);
            answerInput.setText(answer);
        }

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

                        if (alias.isEmpty() || question.isEmpty() || answer.isEmpty()) {
                            if (alias.isEmpty()) {
                                aliasInput.setError(error);
                            }
                            if (question.isEmpty()) {
                                questionInput.setError(error);
                            }
                            if(answer.isEmpty())
                            {
                                answerInput.setError(error);
                            }
                        } else {
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
