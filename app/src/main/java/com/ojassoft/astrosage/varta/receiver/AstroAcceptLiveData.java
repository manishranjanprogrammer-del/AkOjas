package com.ojassoft.astrosage.varta.receiver;

import androidx.lifecycle.LiveData;

public class AstroAcceptLiveData extends LiveData<String> {

    public void setView(String message){
        postValue(message);
    }
}
