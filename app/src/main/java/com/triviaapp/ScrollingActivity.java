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

    RequestQueue requestQueue;
    ArrayList<Message> messageList = new ArrayList<>();
    ArrayAdapter<Message> adapter;
    String getAllMessagesURL = "http://donherwig.com/getAll.php";

    JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("TriviaApp");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent search = new Intent(ScrollingActivity.this, SearchActivity.class);
                startActivity(search);
            }
        });

        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.add);
        assert add != null;
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent message = new Intent(ScrollingActivity.this, EnterMessageActivity.class);
                startActivity(message);
            }
        });


        requestQueue = Volley.newRequestQueue(ScrollingActivity.this);

        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getAllMessagesURL, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray messages = response.getJSONArray("messages");
                    for (int i = 0; i < messages.length(); i++) {
                        JSONObject message = messages.getJSONObject(i);
                        Message temp = new Message(message.getString("alias"), message.getString("question"));
                        messageList.add(temp);
                    }
                    adapter = new ArrayAdapter<>(ScrollingActivity.this, android.R.layout.simple_list_item_1, messageList);
                    setListAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse (VolleyError error){
                Toast.makeText(ScrollingActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isNetworkConnected())
        {
            messageList.clear();
            requestQueue.add(jsonObjectRequest);
        }
        else {
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

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Message m = (Message) l.getAdapter().getItem(position);

        Intent i = new Intent(ScrollingActivity.this, SearchActivity.class);
        i.putExtra("alias", m.getAlias());
        ScrollingActivity.this.startActivity(i);

    }

}
