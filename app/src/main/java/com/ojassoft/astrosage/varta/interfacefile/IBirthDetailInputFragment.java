package com.ojassoft.astrosage.varta.interfacefile;


import com.ojassoft.astrosage.varta.model.BeanDateTime;
import com.ojassoft.astrosage.varta.model.BeanPlace;

public interface IBirthDetailInputFragment {
    void openCalendar(BeanDateTime beanDateTime);

    void openTimePicker(BeanDateTime beanDateTime);

    void openSearchPlace(BeanPlace b);

    void birthDetailInputFragmentCreated();
}
