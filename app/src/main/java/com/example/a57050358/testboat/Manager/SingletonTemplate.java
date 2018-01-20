package com.example.a57050358.testboat.Manager;

import android.content.Context;

import com.inthecheesefactory.thecheeselibrary.manager.Contextor;

/**
 * Created by 57050358 on 25/12/2560.
 */


public class SingletonTemplate {

        private static SingletonTemplate instance;

        public static SingletonTemplate getInstance() {
            if (instance == null)
                instance = new SingletonTemplate();
            return instance;
        }

        private Context mContext;

        private SingletonTemplate() {
            mContext = Contextor.getInstance().getContext();
        }

}
