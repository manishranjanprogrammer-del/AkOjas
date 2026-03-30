
package com.ojassoft.astrosage.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payoption implements Serializable{

    @SerializedName("payOptions")
    @Expose
    private List<PaySubOption> payOptions = new ArrayList<PaySubOption>();

    /**
     * 
     * @return
     *     The payOptions
     */
    public List<PaySubOption> getPayOptions() {
        return payOptions;
    }

    /**
     * 
     * @param payOptions
     *     The payOptions
     */
    public void setPayOptions(List<PaySubOption> payOptions) {
        this.payOptions = payOptions;
    }

}
