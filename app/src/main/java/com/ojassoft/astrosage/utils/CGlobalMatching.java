package com.ojassoft.astrosage.utils;

import java.io.Serializable;

import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.BeanOutMatchmakingNorth;

public class CGlobalMatching implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static CGlobalMatching cGlobalMatching;

    BeanHoroPersonalInfo boy, girl;
    BeanOutMatchmakingNorth beanOutMatchmakingNorth;

    private CGlobalMatching() {
        boy = new BeanHoroPersonalInfo();
        girl = new BeanHoroPersonalInfo();
        boy.setGender("M");
        girl.setGender("F");
    }

    public static CGlobalMatching getCGlobalMatching() {
        if (cGlobalMatching == null)
            cGlobalMatching = new CGlobalMatching();

        return cGlobalMatching;
    }

    public static void setCGlobalMatching(CGlobalMatching _CGlobalMatching) {
        cGlobalMatching = _CGlobalMatching;
    }

    public void setBoyPersonalDetail(BeanHoroPersonalInfo boy) {
        this.boy = boy;
    }

    public void setGirlPersonalDetail(BeanHoroPersonalInfo girl) {
        this.girl = girl;
    }

    public BeanHoroPersonalInfo getBoyPersonalDetail() {
        return this.boy;
    }

    public BeanHoroPersonalInfo getGirlPersonalDetail() {
        return this.girl;
    }

    public void setBeanOutMatchmakingNorth(BeanOutMatchmakingNorth beanOutMatchmakingNorth) {
        this.beanOutMatchmakingNorth = beanOutMatchmakingNorth;
    }

    public BeanOutMatchmakingNorth getBeanOutMatchmakingNorth() {
        return this.beanOutMatchmakingNorth;
    }
}
