package com.ktds.queuing_app.Util.beacon;

import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.ktds.queuing_app.Util.beacon.uuid.BeaconUUIDs;
import com.ktds.queuing_app.vo.QueuingVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 206-013 on 2016-07-18.
 */
public class BeaconManagerStore implements BeaconUUIDs {

    private static volatile BeaconManagerStore beaconManagerStore;

    public boolean isIssuedToken = false;

    private boolean isConnected;

    private QueuingVO queuingVO;

    public QueuingVO getQueuingVO() {
        return queuingVO;
    }

    private BeaconManager beaconManager;

    // branchId with beacons
    private Map<String, BeaconManager> beaconManagers;

    private BeaconManagerStore() {
        beaconManagers = new HashMap<String, BeaconManager>();
        queuingVO = new QueuingVO();
    }

    public static synchronized BeaconManagerStore getInstance() {
        if (beaconManagerStore == null ) {
            beaconManagerStore = new BeaconManagerStore();
        }
        return beaconManagerStore;
    }

    public void add (String branchId, BeaconManager beaconManager) {
        beaconManagers.put(branchId, beaconManager);
    }

    public BeaconManager get (String branchId) {
        return beaconManagers.get(branchId);
    }


    public void startRangingAllBeacon(final ActivityButtonController controller) {

            beaconManager = this.beaconManagers.get("beacon");

            beaconManager.setRangingListener(new BeaconManager.RangingListener() {

                @Override
                public void onBeaconsDiscovered(Region region, List<Beacon> list) {

                    if (!list.isEmpty()) {
                        Beacon nearestBeacon = list.get(0);

                        // nearestBean.getRssi() : 비콘의 수신강도
                        Log.d("Airport", "Nearest places: " + nearestBeacon.getRssi() + " / " + isConnected);

                        if ( (!isConnected && nearestBeacon.getRssi() > -70) && !isIssuedToken) {
                            isConnected = true;

                            queuingVO.setBranchId(getBranchCode(region));
                            queuingVO.setCancel(false);

                            // 비콘에 연결이 되면 SQLite의 BeaconId를 수정한다.
                            //버튼 활성화
                            controller.buttonEnable();
                            controller.action(queuingVO);

                        } else if (nearestBeacon.getRssi() < -70) {
                            //버튼 비활성화
                            controller.buttonDisable();
                        }
                    }
                }
            });

            beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
                @Override
                public void onServiceReady() {
                    beaconManager.startRanging(BEACON_1);
                    beaconManager.startRanging(BEACON_2);
                    beaconManager.startRanging(BEACON_3);
                }
            });

    }

    public void stopRaging() {
        isConnected = !isConnected;
        beaconManager.stopRanging(BEACON_1);
        beaconManager.stopRanging(BEACON_2);
        beaconManager.stopRanging(BEACON_3);
    }

    private String getBranchCode(Region region) {

        if ( region == BEACON_1 ) {
            return BRANCH_1;
        }
        else if ( region == BEACON_2 ) {
            return BRANCH_2;
        }
        else if ( region == BEACON_3 ) {
            return BRANCH_3;
        }
        else {
            return null;
        }

    }

}
