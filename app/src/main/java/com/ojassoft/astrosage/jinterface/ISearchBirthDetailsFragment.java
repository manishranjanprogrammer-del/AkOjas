package com.ojassoft.astrosage.jinterface;

import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;

public interface ISearchBirthDetailsFragment {
    void selectedKundli(BeanHoroPersonalInfo beanHoroPersonalInfo,int position);
    void oneChartDeleted(long kundliId, boolean isOnlineChart);
}
