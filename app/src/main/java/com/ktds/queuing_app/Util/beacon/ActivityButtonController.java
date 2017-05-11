package com.ktds.queuing_app.Util.beacon;

import com.ktds.queuing_app.vo.QueuingVO;

/**
 * Created by 206-013 on 2016-07-21.
 */
public interface ActivityButtonController {

    public void buttonEnable();

    public void buttonDisable();

    public void action(QueuingVO queuingVO);

}
