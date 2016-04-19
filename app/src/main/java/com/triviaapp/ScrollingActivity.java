package com.triviaapp;

import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/*

this is the main page; it calls for server and requests for all the trivia questions that have been entered

 */
public class ScrollingActivity extends ListActivity {

    // Mechanism for get requests
    RequestQueue requestQueue;

    // Holds list of messages for list view on homepage
    ArrayList<Message> messageList = new ArrayList<>();

    // Adapter for list view
    ArrayAdapter<Message> adapter;

    // URL of the server
    String getAllMessagesURL = "http://donherwig.com/getAll.php";

    // JSON object request to create the actual get request
    JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set up layout and tool bar
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("TriviaApp");

        // Create and assign on click listeners for floating buttons
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clicking on magnifying glass will send to SearchActivity
                Intent search = new Intent(ScrollingActivity.this, SearchActivity.class);
                startActivity(search);
            }
        });

        // Create and assign on click listeners for floating buttons
        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.add);
        assert add != null;
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clicking on add button will send to EnterMessageActivity
                Intent message = new Intent(ScrollingActivity.this, EnterMessageActivity.class);
                startActivity(message);
            }
        });

        // Set up mechanism sending get requests to server
        requestQueue = Volley.newRequestQueue(ScrollingActivity.this);

        // Create get request to send to server
        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getAllMessagesURL, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    // Iterates through all messages received from database and adds to array list
                    JSONArray messages = response.getJSONArray("messages");
                    for (int i = 0; i < messages.length(); i++) {
                        JSONObject message = messages.getJSONObject(i);
                        Message temp = new Message(message.getString("alias"), message.getString("question"));
                        messageList.add(temp);
                    }
                    // Once finished, it will attach to array adapter and sets list view on homepage
                    adapter = new ArrayAdapter<>(ScrollingActivity.this, android.R.layout.simple_list_item_1, messageList);
                    setListAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse (VolleyError error){
                // Show error toast
                Toast.makeText(ScrollingActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Check network connection is available
        if(isNetworkConnected())
        {
            // Refresh list view on homepage for any newly created messages
            messageList.clear();
            requestQueue.add(jsonObjectRequest);
        }
        else {
            // Set up alert dialog to prompt user to establish internet connection
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Internet connection unavailable");
            builder.setMessage("Please turn on your connection");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }

    // Check network connection is available
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    // If list view item is clicked on, send to SearchActivity with the alias clicked on
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Message m = (Message) l.getAdapter().getItem(position);

        Intent i = new Intent(ScrollingActivity.this, SearchActivity.class);
        i.putExtra("alias", m.getAlias());
        ScrollingActivity.this.startActivity(i);

    }

}
