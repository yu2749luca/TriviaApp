package com.triviaapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

public class SearchActivity extends AppCompatActivity {

    boolean open = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Message Search");
            toolbar.setTitleTextColor(Color.WHITE);
        }
        setSupportActionBar(toolbar);
        SearchView search = (SearchView) findViewById(R.id.searchView);
        if (search != null) {
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(final String query) {
                    final ProgressDialog dialog = new ProgressDialog(SearchActivity.this);
                    dialog.setTitle("Please Wait");
                    dialog.setMessage("Searching for your message");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            dialog.dismiss();
                            if(query.isEmpty())
                            {
                                Snackbar snack = Snackbar.make(findViewById(R.id.searchView), "Search cannot be blank!", Snackbar.LENGTH_SHORT)
                                        .setActionTextColor(Color.WHITE);
                                View snackView = snack.getView();
                                snackView.setBackgroundColor(Color.parseColor("#00CCFF"));
                                TextView text = (TextView)snackView.findViewById(android.support.design.R.id.snackbar_text);
                                text.setTextColor(Color.WHITE);
                                text.setGravity(Gravity.CENTER_HORIZONTAL);
                                snack.show();
                            }
                            else {

                            }
                        }
                    }, 3000);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
        Button answer = (Button) findViewById(R.id.answer_submit);
        if (answer != null) {
            answer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog();
                }
            });
        }
    }

    private void showDialog() {
        final EditText input = new EditText(SearchActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);

        final AlertDialog d = new AlertDialog.Builder(SearchActivity.this)
                .setView(input)
                .setTitle("Trivia Answer")
                .setPositiveButton("OK", null) //Set to null. We override the onclick
                .setNegativeButton("Cancel", null)
                .create();

        d.setCanceledOnTouchOutside(false);
        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!input.getText().toString().isEmpty()) {
                            dialog.dismiss();
                            final ProgressDialog progress = new ProgressDialog(SearchActivity.this);
                            progress.setTitle("Please Wait");
                            progress.setMessage("Attempting to decrypt");
                            progress.setCanceledOnTouchOutside(false);
                            progress.show();

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    progress.dismiss();
                                    View search = findViewById(R.id.searchView);
                                    if (search != null) {
                                        Snackbar snack = Snackbar.make(search, "Successfully decrypted!", Snackbar.LENGTH_SHORT)
                                                .setActionTextColor(Color.WHITE);
                                        View snackView = snack.getView();
                                        snackView.setBackgroundColor(Color.parseColor("#00CCFF"));
                                        TextView text = (TextView) snackView.findViewById(android.support.design.R.id.snackbar_text);
                                        text.setTextColor(Color.WHITE);
                                        text.setGravity(Gravity.CENTER_HORIZONTAL);
                                        snack.show();
                                    }
                                }
                            }, 3000);
                        } else {
                            input.setError("Answer cannot be blank!");
                        }
                    }
                });
                Button n = d.getButton(AlertDialog.BUTTON_NEGATIVE);
                n.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        d.show();
    }
}
