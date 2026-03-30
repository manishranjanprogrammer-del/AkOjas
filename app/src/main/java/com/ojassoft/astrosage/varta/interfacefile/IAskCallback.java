package com.ojassoft.astrosage.varta.interfacefile;


import com.ojassoft.astrosage.varta.utils.CUtils;

/**
 * Created by ojas on १४/२/१७.
 */
public interface IAskCallback {

    void getCallBack(String result, CUtils.callBack callback, String priceInDollor, String priceInRs);
    void getCallBackForChat(String [] result, CUtils.callBack callback,String priceInDollor, String priceInRs);
}
