package com.ktds.queuing_app.Util.beacon;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.ktds.queuing_app.MainActivity;
import com.ktds.queuing_app.SplashActivity;
import com.ktds.queuing_app.lating.LatingActivity;

import java.util.List;

/**
 * Created by MinChang Jang on 2016-06-23.
 */
public class MyApplication extends Application{

    private static BeaconManagerStore beaconManagerStore = BeaconManagerStore.getInstance();

    private static BeaconManager beaconManager;

    /**
     * Application을 설치할 때 실행됨.
     */
    @Override
    public void onCreate() {
        super.onCreate();

        beaconManager = new BeaconManager(getApplicationContext());

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(beaconManagerStore.BEACON_1);
                beaconManager.startMonitoring(beaconManagerStore.BEACON_2);
                beaconManager.startMonitoring(beaconManagerStore.BEACON_3);
            }
        });

        beaconManagerStore.add("beacon", beaconManager);

        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {

            @Override
            //수신감도
            public void onEnteredRegion(Region region, List<Beacon> list) {

                // 동일 액티비티가 이미 실행되어 있는 상태라면, 비콘 신호가 들어오더라도 실행되지 않는다.
                if ( !isAlreadyRunActivity()) {
                    Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("executeType", "beacon");
                    getApplicationContext().startActivity(intent);
                }

            }
            @Override
            public void onExitedRegion(Region region) {
                Intent intent = new Intent(getApplicationContext(), LatingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        });

    }

    /**
     * Notification으로 Beacon 의 신호가 연결되거나 끊겼음을 알림.
     * @param title
     * @param message
     */
    public void showNotification(String title, String message, int code) {
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[] { notifyIntent }, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                //Ticker를 사용하려면 setPriority를 적용시켜주어야 적용된다.
                .setTicker("[Chameleon] 접속이 되었습니다.")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                //우선순위 조절로 Notification을 정의
                .setPriority(Notification.PRIORITY_HIGH)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(code, notification);
    }

    public boolean isAlreadyRunActivity() {
        // 만약 어플리케이션이 실행이 되어있다면, 다시 실행할 필요 없이, 사용자에게 재실행 시킬지의 여부를 알려준다.
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfos = activityManager.getRunningTasks(9999);

        String activityName = "";
        for(int i = 0; i < taskInfos.size(); i++) {
            activityName = taskInfos.get(i).topActivity.getPackageName();

            if(activityName.startsWith("com.ktds.queuing_app")) {
                return true;
            }
        }
        return false;
    }

    public static void stopMonitoring() {
        beaconManager.stopMonitoring(beaconManagerStore.BEACON_1);
        beaconManager.stopMonitoring(beaconManagerStore.BEACON_2);
        beaconManager.stopMonitoring(beaconManagerStore.BEACON_3);
    }

}