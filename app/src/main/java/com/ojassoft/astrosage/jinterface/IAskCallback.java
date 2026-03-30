package com.ojassoft.astrosage.jinterface;

import com.ojassoft.astrosage.utils.CUtils;

/**
 * Created by ojas on १४/२/१७.
 */
public interface IAskCallback {

    void getCallBack(String result, CUtils.callBack callback,String priceInDollor, String priceInRs);
    void getCallBackForChat(String [] result, CUtils.callBack callback,String priceInDollor, String priceInRs);
}
