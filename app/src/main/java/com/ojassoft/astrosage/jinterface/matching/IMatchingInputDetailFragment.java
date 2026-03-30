package com.ojassoft.astrosage.jinterface.matching;

import com.ojassoft.astrosage.beans.BeanDateTime;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.BeanPlace;

public interface IMatchingInputDetailFragment {
    void openCalendar(BeanDateTime beanDateTime, int personType);

    void openTimePicker(BeanDateTime beanDateTime, int personType);

    void openSavedKundli(int personType);

    void openSearchPlace(BeanPlace b, int personType);

    void calculateMatching(BeanHoroPersonalInfo beanHoroPersonalInfoPerson1, BeanHoroPersonalInfo beanHoroPersonalInfoPerson2, boolean isSaveDetail);

    void matchingInputDetailFragmentCreated();

    void setPersonVariableValue(int personType);
}
