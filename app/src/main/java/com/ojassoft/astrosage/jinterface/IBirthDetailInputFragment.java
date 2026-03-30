package com.ojassoft.astrosage.jinterface;

import com.ojassoft.astrosage.beans.BeanDateTime;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.BeanPlace;

public interface IBirthDetailInputFragment {
    void openCalendar(BeanDateTime beanDateTime);

    void openTimePicker(BeanDateTime beanDateTime);

    void openSavedKundli();

    void openSearchPlace(BeanPlace b);

    void calculateKundli(BeanHoroPersonalInfo beanHoroPersonalInfo, boolean isSaveDetail);

    void birthDetailInputFragmentCreated();
}
