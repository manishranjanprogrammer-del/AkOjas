package com.ojassoft.astrosage.utils;

import android.content.Context;
import android.content.Intent;

import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.OutBirthDetail;
import com.ojassoft.astrosage.controller.ControllerManager;
import com.ojassoft.astrosage.jinterface.IMisc;

/**
 * This class is used for miscellaneous operations.
 *
 * @author Bijendra
 * @version 1.0.0
 * @date 10 may 2012
 * @modify -
 */

public class CMisc implements IMisc {


    /**
     * Constructor of the class
     */
    public CMisc() {

    }


    /**
     * This function initialize OutBirthDetail object from the passed
     * HoroPersonalInfo object and return OutBirthDetail object.
     *
     * @param obj
     * @return OutBirthDetail
     */

    public OutBirthDetail getBirthDetail(BeanHoroPersonalInfo obj) {
        OutBirthDetail _outBirthDetail = new OutBirthDetail();
        try {

            String strTime = "";


            strTime = CUtils.pad(CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getDateTime().getHour());
            strTime += ":"
                    + CUtils.pad(CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getDateTime().getMin());
            strTime += ":"
                    + CUtils.pad(CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getDateTime().getSecond());


            _outBirthDetail.setName(CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getName());
            _outBirthDetail.setDateOfBirth(getFormattedBirthDate(CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getDateTime().getYear(), CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getDateTime().getMonth(), CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getDateTime().getDay()));
            _outBirthDetail.setTimeOfBirth(strTime);
            _outBirthDetail.setPlace(CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getPlace().getCityName());


            if (CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getGender().equalsIgnoreCase("M")||CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getGender().equalsIgnoreCase("Male"))
                _outBirthDetail.setGender("Male");
            else
                _outBirthDetail.setGender("Female");

            // _outBirthDetail.setAyanType(CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getAyan());
            // THIS CODE IS ADDED BY BIJENDRA ON 29-NOV-2012
            _outBirthDetail.setAyanType(String.valueOf(CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getAyanIndex()));
            // END

            _outBirthDetail.setDst(String.valueOf(CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getDST()));

            _outBirthDetail.setMangldoshPoint(new ControllerManager().returnManglikPonits(CGlobal.getCGlobalObject().getPlanetDataObject()));
            _outBirthDetail.setRashi(new ControllerManager().getLagnaKundliPlanetsRashiArray(CGlobal.getCGlobalObject().getPlanetDataObject())[1]);


        } catch (Exception e) {

        }

        return _outBirthDetail;

    }

    /**
     * This function return formatted Date of Birth
     *
     * @param
     * @return String
     */
    private String getFormattedBirthDate(int mYear, int mMonth, int mDay) {

        String dateStr = CUtils.FormatDateDDMMYYYY(mYear, (mMonth + 1 > 12 ? 1
                : mMonth + 1), mDay);

        return dateStr;
    }

    public static void sendEMail(Context context, String msg) {
        try {

            Intent shareKundliIntent = new Intent(
                    android.content.Intent.ACTION_SEND);

            shareKundliIntent.setType("plain/text");

            shareKundliIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                    "Email-is");

            shareKundliIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                    "Share Kundli");

            shareKundliIntent.putExtra(android.content.Intent.EXTRA_TEXT, msg);
            context.startActivity(Intent.createChooser(shareKundliIntent,
                    "Send mail..."));
        } catch (Exception e) {

        }
        return;
    }


}
