package com.ktds.queuing_app.Util.beacon;

import android.util.Log;

/**
 * Created by 206-013 on 2016-07-21.
 */
public class BeaconRagingMonitoring {

    private BeaconManagerStore beaconManagerStore;

    public BeaconRagingMonitoring() {
        beaconManagerStore = BeaconManagerStore.getInstance();
    }

    public void start(final ActivityButtonController controller) {
        Log.d("Airport", "시작");

        beaconManagerStore.startRangingAllBeacon(controller);
    }

}
