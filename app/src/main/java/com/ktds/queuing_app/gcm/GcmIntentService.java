package com.ktds.queuing_app.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.ktds.queuing_app.MainActivity;
import com.ktds.queuing_app.R;
import com.ktds.queuing_app.gcm.notification.ReceiveNotification;
import com.ktds.queuing_app.lating.LatingActivity;

public class GcmIntentService extends IntentService implements ReceiveNotification {

    public static final String TAG = "GcmIntentService";
    public static final int NOTIFICATION_ID = 1;

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;

    public GcmIntentService() {
//        Used to name the worker thread, important only for debugging.
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.

                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                // Post notification of received message.
                sendNotification("Received: " + extras.toString());

                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);

    }

    //TODO : 작업해야함.
    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (msg.contains(R_N_1)) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
            actionNotification(S_C_1, contentIntent);
        } else if (msg.contains(R_N_2)) {

            Intent intent = new Intent(this, LatingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
            actionNotification(S_C_2, contentIntent);
        }
    }

    private void actionNotification (String msg, PendingIntent contentIntent) {
        mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.gcm)
                .setContentTitle("GCM ReceiveNotification")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(msg))
                .setContentText(msg);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
