package com.triviaapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class DisplayDecryptedMessageActivity extends AppCompatActivity {

   protected void OnCreate(Bundle savedInstanceState){
       super.onCreate(savedInstanceState);
       setContentView(R.layout.decrypted_message_display);

       Button back2menu = (Button) findViewById(R.id.back2menu);
       back2menu.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent back2menu = new Intent();
               back2menu.setClass(DisplayDecryptedMessageActivity.this, ScrollingActivity.class);
               startActivity(back2menu);
           }
       });
   }
}
