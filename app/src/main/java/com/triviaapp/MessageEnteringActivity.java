package com.triviaapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MessageEnteringActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_entering);

        Button backButton = (Button)findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent back = new Intent();
                back.setClass(MessageEnteringActivity.this, ScrollingActivity.class);
                startActivity(back);
            }
        });

        Button nextButton = (Button)findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent next = new Intent();
                // need to revise
                next.setClass(MessageEnteringActivity.this, MessageMainActivity.class);
                startActivity(next);
            }
        });
    }


}
