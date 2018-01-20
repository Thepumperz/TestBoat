package com.example.a57050358.testboat.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


import com.example.a57050358.testboat.Fragment.FragmentMap;
import com.example.a57050358.testboat.R;

/**
 * Created by 57050358 on 25/12/2560.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.mainContainer, FragmentMap.newInstance())
                    .commit();
        }
    }
    /*
        private void init() {
            Btn1 = findViewById(R.id.Btn1); Btn1.setOnClickListener(this);
            Btn2 = findViewById(R.id.Btn2); Btn2.setOnClickListener(this);
            Btn3 = findViewById(R.id.Btn3); Btn3.setOnClickListener(this);
            Btn4 = findViewById(R.id.Btn4); Btn3.setOnClickListener(this);
    }
    */

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

}
