package com.ojassoft.astrosage.beans;

import java.io.Serializable;

public class CBookMarkItem implements Serializable {

    private static final long serialVersionUID = 1L;
    public int ModuleId;
    public int ScreenId;

    public CBookMarkItem(int moduleId, int screenId) {
        ModuleId = moduleId;
        ScreenId = screenId;
    }
}
