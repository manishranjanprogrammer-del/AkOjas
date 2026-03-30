package com.ojassoft.astrosage.utils;

import android.content.Context;

import com.ojassoft.astrosage.model.NumerologyCalculatorModel;

/**
 * This class is used to Numerology Calculation
 */

public class NumerologyUtils {

    private String[] yantraLink = {"surya-yantra", "chandra-yantra", "guru-yantra", "rahu-yantra", "budh-yantra", "shukra-yantra", "ketu-yantra", "shani-yantra", "mangal-yantar"};
    private String[] yantra = {"surya_yantra.jpg", "chandra_yantra.jpg", "brihaspati_yantra.jpg", "rahu_yantra.jpg", "budh_yantra.jpg", "shukra_yantra.jpg", "ketu_yantra.jpg", "grah_peera_niwarak_shani_yantra.jpg", "mangal_yantra.jpg"};

    /**
     * this constructor define all the array from string array file.
     *
     * @param context
     */
    public NumerologyUtils(Context context) {

    }

    /**
     * This function is used to calculate the Numerology data
     *
     * @param nameNumber
     * @return NumerologyCalculatorModel
     */
    public NumerologyCalculatorModel getNumerologyCalculatorModel(int nameNumber) {

        NumerologyCalculatorModel calculatorModel = new NumerologyCalculatorModel();

        nameNumber = nameNumber - 1; //for getting values from array

        // Here 0 is minimum nameNumber and 9 is maximum nameNumber
        if (nameNumber >= 0 && nameNumber < 9) {

            calculatorModel.setYantra(yantra[nameNumber]);
            calculatorModel.setYantraLink(yantraLink[nameNumber]);
        }
        return calculatorModel;
    }
}
