package com.example.a57050358.testboat;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.inthecheesefactory.thecheeselibrary.manager.Contextor;

public class MainActivity extends Application {
        @Override
        public void onCreate() {
            super.onCreate();
            Contextor.getInstance().init(getApplicationContext());
        }

        @Override
        public void onTerminate() {
            super.onTerminate();
        }
}
