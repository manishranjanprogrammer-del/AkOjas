package com.ojassoft.astrosage.jinterface;

import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;

/**
 * Created by ojas on 4/5/16.
 */
public interface ISearchBirthDeatils {

    void confirmDeleteKundli(BeanHoroPersonalInfo  beanHoroPersonalInfo,boolean isCheckForDeletePersonalKundli,boolean isLocal);
    void selectPersonalMyKundali(BeanHoroPersonalInfo beanHoroPersonalInfo);
    void openConfirmationDialogForPersonalKundaliCallBack(BeanHoroPersonalInfo beanHoroPersonalInfo);

}
