package com.ktds.queuing_app.Util.beacon.uuid;

import com.estimote.sdk.Region;

import java.util.UUID;

/**
 * Created by 206-013 on 2016-07-20.
 */
public interface BeaconUUIDs {

    public final static String BRANCH_1 = "1";

    public final static String BRANCH_2 = "2";

    public final static String BRANCH_3 = "3";

    // 본인이 연결할 Beacon의 ID와 Major / Minor Code를 알아야 한다.
    public final static Region BEACON_1 = new Region("ranged region",UUID.fromString("74278BDA-B644-4520-8F0C-720EAF059935"), 0, 0);

    public final static Region BEACON_2 = new Region("ranged region",UUID.fromString("20CAE8A0-A9CF-11E3-A5E2-0800200C9A66"), 87, 60872);

    public final static Region BEACON_3 = new Region("ranged region",UUID.fromString("20CAE8A0-A9CF-11E3-A5E2-0800200C9A66"), 87, 59355);


}
