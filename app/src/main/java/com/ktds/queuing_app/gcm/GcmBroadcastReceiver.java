package com.ktds.queuing_app.gcm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by 206-013 on 2016-07-11.
 *
 * 안드로이드 시스템에서 발생하는 수많은 액션(배터리 용량 부족, SMS 수신 등등)이 발생하면 이를 수신해 처리하는 역할을 한다.
 * 여기에서는 안드로이드 기기가 GCM 메시지를 수신하면 이를 수신하는 데 사용한다.
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        for (String key : bundle.keySet())
        {
            Object value = bundle.get(key);
            Log.d("GCM", "|" + String.format("%s : %s (%s)", key, value.toString(), value.getClass().getName()) + "|");
            if (key.equalsIgnoreCase("registration_id"))
            {
                String regId = bundle.getString(key);
                Log.d("REG_ID", regId);
                //PreferenceUtil.instance(context).putRedId(regId);
            }
        }

        // Explicitly specify that GcmIntentService will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(),
                GcmIntentService.class.getName());
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));

        setResultCode(Activity.RESULT_OK);
    }
}
