package com.ojassoft.astrosage.jinterface;

import com.ojassoft.astrosage.beans.BeanDateTime;

/**
 * Created by ojas on १/७/१६.
 */
public interface IHandleSavedCards {

    void deleteCard(int position);

    void ProceedPay(String cvv,int position);
}
