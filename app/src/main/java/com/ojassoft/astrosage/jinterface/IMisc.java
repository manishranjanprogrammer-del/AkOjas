package com.ojassoft.astrosage.jinterface;

import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.OutBirthDetail;

/**
 * This interface is used in CMisc.java class
 *
 * @author Bijendra
 * @version 1.0.0
 * @date 11 may 2012
 * @modify -
 */
public interface IMisc {

    /**
     * This function initialize OutBirthDetail
     * object from the passed  HoroPersonalInfo object
     * and return  OutBirthDetail object.
     *
     * @param obj
     * @return OutBirthDetail
     */
    OutBirthDetail getBirthDetail(BeanHoroPersonalInfo obj);

}
