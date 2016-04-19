package com.triviaapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



/*
this page is for searching trivia question by using Alias (ID)
this page connects to the server;
 */
public class SearchActivity extends AppCompatActivity {

    // URL to server
    String getMessage = "http://donherwig.com/readMessage.php";
    // Variable to hold encrypted message
    String encryptedMessage = "";
    // Mechanism for get requests
    RequestQueue requestQueue;

    // References to text views on layout
    TextView qView;
    TextView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set up layout and tool bar
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);

        setSupportActionBar(toolbar);
        SearchView search = (SearchView) findViewById(R.id.searchView);

        // Retrieve alias passed if any is given
        String dataFromMessageList = getIntent().getStringExtra("alias");

        if (toolbar != null) {
            toolbar.setTitle("Message Search");
            toolbar.setTitleTextColor(Color.WHITE);
        }

        // Set up mechanism for get requests
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        // Set references to text views
        qView = (TextView)findViewById(R.id.search_question);
        mView = (TextView)findViewById(R.id.show_message);

        // If alias was passed in, use it in search query to automatically set up for user
        if(dataFromMessageList != null){
            if (search != null) {
                search.setQuery(dataFromMessageList, false);
            }
        }

        // No alias is passed. Therefore, fresh activity instance
        if (search != null) {
            // On Query listener for actions on searching
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(final String query) {
                    // Check network connection is available
                    if(isNetworkConnected())
                    {
                        // Set up searching dialog
                        final ProgressDialog dialog = new ProgressDialog(SearchActivity.this);
                        dialog.setTitle("Please Wait");
                        dialog.setMessage("Searching for your message");
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();

                        // Create handler to check if query was successful
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Once one second has passed, dismiss loading dialog
                                dialog.dismiss();
                                // Check if query is empty
                                if (query.isEmpty()) {
                                    // Set up snackbar to tell user the query cannot be empty
                                    Snackbar snack = Snackbar.make(findViewById(R.id.searchView), "Search cannot be blank!", Snackbar.LENGTH_SHORT)
                                            .setActionTextColor(Color.WHITE);
                                    View snackView = snack.getView();
                                    snackView.setBackgroundColor(Color.parseColor("#00CCFF"));
                                    TextView text = (TextView) snackView.findViewById(android.support.design.R.id.snackbar_text);
                                    text.setTextColor(Color.WHITE);
                                    text.setGravity(Gravity.CENTER_HORIZONTAL);
                                    snack.show();
                                } else {
                                    // Create get request to find the message associated with given alias
                                    StringRequest request = new StringRequest(Request.Method.POST, getMessage, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            //server will return "Alias doesn't exist if the alias isn't in database
                                            if (!response.equals("Alias doesn't exist!")) {
                                                JSONObject answer;
                                                //create a JsonObject from returned JSON string from server
                                                try {
                                                    answer = new JSONObject(response);
                                                    JSONArray temp = answer.getJSONArray("serverData");
                                                    JSONObject tempMessage = temp.getJSONObject(0);
                                                    encryptedMessage = tempMessage.getString("message");
                                                    JSONObject question = temp.getJSONObject(0);
                                                    String questionString = question.getString("question");

                                                    if (qView != null) {
                                                        qView.setText(questionString);
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            } else {
                                                // Show alert dialog to tell user search was invalid
                                                final AlertDialog d = new AlertDialog.Builder(SearchActivity.this)
                                                        .setTitle("Invalid Alias")
                                                        .setMessage("Alias does not exist")
                                                        .setPositiveButton("OK", null)
                                                        .create();
                                                d.show();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            // Toast for error
                                            Toast.makeText(SearchActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }) {
                                        // Alias gets sent to server for database query
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String, String> parameters = new HashMap<>();
                                            parameters.put("alias", query);
                                            return parameters;
                                        }
                                    };
                                    requestQueue.add(request);
                                }
                            }
                        }, 1000);
                        return false;
                    }
                    else {
                        // Show alert dialog to prompt user to establish internet connection
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(SearchActivity.this);
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
                    // Check if there's an encrypted message to even decrypt
                    if (encryptedMessage.isEmpty()) {
                        // Show snackbar to tell user there isn't anything to decrypt yet
                        Snackbar snack = Snackbar.make(findViewById(R.id.searchView), "Nothing to decrypt!", Snackbar.LENGTH_SHORT)
                                .setActionTextColor(Color.WHITE);
                        View snackView = snack.getView();
                        snackView.setBackgroundColor(Color.parseColor("#00CCFF"));
                        TextView text = (TextView) snackView.findViewById(android.support.design.R.id.snackbar_text);
                        text.setTextColor(Color.WHITE);
                        text.setGravity(Gravity.CENTER_HORIZONTAL);
                        snack.show();
                    } else {
                        showDialog();
                    }
                }
            });
        }
    }

    // Check internet connection is available
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    // Method to show alert dialog to answer trivia question
    private void showDialog() {
        final EditText input = new EditText(SearchActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);

        // Build alert dialog
        final AlertDialog d = new AlertDialog.Builder(SearchActivity.this)
                .setView(input)
                .setTitle("Trivia Answer")
                .setPositiveButton("OK", null)
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
                        // Check if answer is given or not
                        final String ans = input.getText().toString().toLowerCase();
                        if (!ans.isEmpty()) {
                            // If answer is given, then show loading message
                            dialog.dismiss();
                            final ProgressDialog progress = new ProgressDialog(SearchActivity.this);
                            progress.setTitle("Please Wait");
                            progress.setMessage("Attempting to decrypt");
                            progress.setCanceledOnTouchOutside(false);
                            progress.show();

                            // Create handler to launch action after one second
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    progress.dismiss();
                                    View search = findViewById(R.id.searchView);
                                    if (search != null) {
                                        // Check if answer's length is greater than 1 and encrypted message isn't empty
                                        if (ans.length() > 1 && !encryptedMessage.isEmpty()) {
                                            // Attempt to decrypt message with user's given answer
                                            Encryption mes = new Encryption(ans);
                                            mView.setText(mes.decode(encryptedMessage));
                                        } else {
                                            // Show toast to prompt user input is invalid
                                            Toast.makeText(SearchActivity.this, "Answer must be at least 2 characters!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            }, 1000);
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
