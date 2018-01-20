package com.example.a57050358.testboat.Util;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import com.example.a57050358.testboat.Activity.MainActivity;
import com.example.a57050358.testboat.R;

import static java.security.AccessController.getContext;

/**
 * Created by 57050358 on 25/12/2560.
 */

public class ProximityIntentReceiver extends BroadcastReceiver {
    static int NOTIFICATION_ID = 1000;
    int id =NOTIFICATION_ID;
    boolean noti = false;

    int short_gap = 200;    // Length of Gap Between dots/dashes
    int long_gap = 1000;    // Length of Gap Between Words
    long[] pattern = {
            0, long_gap, short_gap, long_gap, short_gap, long_gap
    };

    private Vibrator vibrate;

    @Override
    public void onReceive(Context context, Intent intent) {
        String key = LocationManager.KEY_PROXIMITY_ENTERING;
        Boolean entering = intent.getBooleanExtra(key, false);
        String msg;
        if (entering) {
            Log.d(getClass().getSimpleName(), "entering");
            msg = "คุณกำลังเข้าใกล้ตำแหน่ง";noti=true;

        } else {
            Log.d(getClass().getSimpleName(), "exiting");
            msg = "คุณกำลังออกจากตำแหน่ง";noti=true;
        }

        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        final Notification notification = createNotification();


        notification.contentIntent = pendingIntent;


        if (id==NOTIFICATION_ID) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, 2);
            
            builder.setTitle("  Alert Me    ");
            builder.setMessage("          " + msg + "     ");
            builder.setIcon(R.drawable.icn3);
            builder.setCancelable(false);
            //builder.setPositiveButton("Cancel", null);
            vibrate = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrate.vibrate(pattern, 0);
            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    vibrate.cancel();
                    notification.flags = Notification.FLAG_AUTO_CANCEL;


                }
            });
            builder.show();

            id = NOTIFICATION_ID++;
        }
    }




    private Notification createNotification() {
        Notification notification = new Notification();
        notification.when = System.currentTimeMillis();
        notification.flags |= Notification.FLAG_INSISTENT;
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.ledARGB = Color.WHITE;
        notification.ledOnMS = 1500;
        notification.ledOffMS = 1500;
        return notification;
    }

}