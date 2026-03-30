package com.ojassoft.astrosage.controller;


import com.ojassoft.astrosage.beans.BeanDasa;
import com.ojassoft.astrosage.beans.PlanetData;
import com.ojassoft.astrosage.jinterface.IDasaVimsottri;

public class CDasaVimsottri implements IDasaVimsottri {
    /**
     * These are the local variables use  to store local values
     * which will be used in miscellaneous  operations.
     */
    private int iSecondLevelClickedPlanetNo = -1;
    private double balance, periodSpan;
    private int level = 0;
    private int nakshatra = 0;
    private double dob = 0;
    private double lastEndDate;
    private int total = 120;
    private int yearOfVimPlanets[] = {7, 20, 6, 10, 7, 18, 16, 19, 17};

    /**
     * Constructor without parameter.
     */
    public CDasaVimsottri() {

    }

    /**
     * Constructor with parameters.
     *
     * @param _dob
     * @param _lastEndDate
     */

    public CDasaVimsottri(double _dob, double _lastEndDate) {
        dob = _dob;
        lastEndDate = _lastEndDate;

    }


    @Override
    public BeanDasa[] firstLevel(PlanetData obj) throws Exception {
        // TODO Auto-generated method stub
        BeanDasa[] mahaDasa = null;
        try {
            lastEndDate = dob;
            balance = calculateBalance(obj.getMoon());
            nakshatra = getNakshatra(obj.getMoon());
            mahaDasa = getMahaDasa();
        } catch (Exception e) {

            throw e;
        }


        return mahaDasa;
    }

    @Override
    public BeanDasa[] vimOtherLevelDasha(int iPos, BeanDasa[] objArrPrDasa)
            throws Exception {
        BeanDasa[] dasaSecondLevel = null;
        try {
            dasaSecondLevel = getVimOtherLevelDasa(iPos, objArrPrDasa);
        } catch (Exception e) {

            throw e;
        }
        return dasaSecondLevel;
    }

    /**
     * This function is used to get balance of dasha
     *
     * @param degMoon
     * @return double
     * @throws Exception
     */
    public double getBalanceOfDasa(double degMoon) throws Exception {
        return calculateBalance(degMoon);
    }


    /**
     * This function is used to return planet for which the dasa is remaine
     *
     * @param degMoon
     * @return planet number
     * @throws Exception
     */
    public int getPlanetForBalanceOfDasa(double degMoon) throws Exception {
        return getNakshatra(degMoon);
    }

    /**
     * This function calculate the balance dasha.
     *
     * @param moon
     * @return double(balance dasha time)
     */
    private double calculateBalance(double moon) {

        double d0, n0, balance; //double

        int q, dd, mm, yy; //int

        d0 = moon;

        d0 = 9.0 * fract(d0 / 120);

        n0 = fract(d0);

        q = (int) (d0);

        balance = (1 - n0) * yearOfVimPlanets[q];

        return balance;

    }

    /**
     * This function return fractional values
     * according to passed double value.
     *
     * @param doubleValue
     * @return double
     */
    private double fract(double doubleValue) {

        return doubleValue - (int) (doubleValue);

    }


    /**
     * This function return nakshatra number according to
     * passed moon value.
     *
     * @param moon
     * @return int(nakshatra number)
     */
    private int getNakshatra(double moon) {

        int nak = (int) (moon * 0.075);

        if (nak >= 18)

            nak -= 18;

        if (nak >= 9)

            nak -= 9;

        return nak;

    }

    /**
     * This function calculate vimshottri maha dasha.
     *
     * @return Dasa[]
     */
    private BeanDasa[] getMahaDasa()//level=0
    {
        //CDasaDuration obj=null;
        BeanDasa[] _dasaFirstLevel = new BeanDasa[9];
        int index = 0;
        int iPlNo = nakshatra;

        //double sTime=balance;
        do {
            _dasaFirstLevel[index] = new BeanDasa();
            _dasaFirstLevel[index].setPlanetNo(iPlNo);

            _dasaFirstLevel[index].setDasaTime(calculateDasaPeriod(iPlNo));
            //cMahaDasa[index]=obj;

            iPlNo++;
            if (iPlNo >= 9)
                iPlNo -= 9;

            index++;

        } while (index < 9);

        return _dasaFirstLevel;
    }

    /**
     * This function calculate dasha period.
     *
     * @param i
     * @return double(dasha time)
     */
    private double calculateDasaPeriod(int i) {

        if (level == 0) {

            if (i == nakshatra)

                lastEndDate = lastEndDate + balance;

            else

                lastEndDate = lastEndDate + yearOfVimPlanets[i];

        } else {

            lastEndDate = lastEndDate + periodSpan * yearOfVimPlanets[i] / 120;

        }
        return lastEndDate;

    }

    /**
     * This  is a private function to calculate other
     * vimshottri dasha level.
     *
     * @param iPos
     * @param objArrPrDasa
     * @return Dasa[]
     */
    private BeanDasa[] getVimOtherLevelDasa(int iPos, BeanDasa[] objArrPrDasa)//level=1
    {
        //periodSpan = tillDate[startPlanet] - tillDate[startPlanet - 1];
        int index = 0;
        int iPlNo = -1;
        BeanDasa[] _dasaSecondLevel = new BeanDasa[9];
        BeanDasa[] tempArrPrDasa = null;

        tempArrPrDasa = objArrPrDasa;

        level = 1;
        if (iPos == 0) {

            periodSpan = yearOfVimPlanets[tempArrPrDasa[iPos].getPlanetNo()];
            lastEndDate = tempArrPrDasa[iPos].getDasaTime() - yearOfVimPlanets[tempArrPrDasa[iPos].getPlanetNo()];

        } else {

            periodSpan = tempArrPrDasa[iPos].getDasaTime() - tempArrPrDasa[iPos - 1].getDasaTime();
            lastEndDate = tempArrPrDasa[iPos - 1].getDasaTime();

        }
        iSecondLevelClickedPlanetNo = tempArrPrDasa[iPos].getPlanetNo();

        iPlNo = iSecondLevelClickedPlanetNo;
        total = total * yearOfVimPlanets[iSecondLevelClickedPlanetNo] / 120;

        if (periodSpan < 0) {

            periodSpan += total;

            lastEndDate -= total;

        }

        do {
            _dasaSecondLevel[index] = new BeanDasa();
            _dasaSecondLevel[index].setPlanetNo(iPlNo);
            _dasaSecondLevel[index].setDasaTime(calculateDasaPeriod(iPlNo));

            iPlNo++;
            if (iPlNo >= 9)
                iPlNo -= 9;

            index++;

        } while (index < 9);


        return _dasaSecondLevel;

    }

    /**
     * This  is a function to calculate other
     * vimshottri dasha level.
     *
     * @param iPos
     * @param objArrPrDasa
     * @param _lastdashatime
     * @return Dasa[]
     * @throws Exception
     */

    public BeanDasa[] getNewVimOtherLevelDasa(int iPos, BeanDasa[] objArrPrDasa, double _lastdashatime) throws Exception//level=1
    {
        //periodSpan = tillDate[startPlanet] - tillDate[startPlanet - 1];
        int index = 0;
        int iPlNo = -1;
        BeanDasa[] _dasaSecondLevel = new BeanDasa[9];
        BeanDasa[] tempArrPrDasa = null;
        try {
            tempArrPrDasa = objArrPrDasa;

            level = 1;
            if (iPos == 0) {
                periodSpan = tempArrPrDasa[0].getDasaTime() - _lastdashatime;
                lastEndDate = _lastdashatime;

            } else {

                periodSpan = tempArrPrDasa[iPos].getDasaTime() - tempArrPrDasa[iPos - 1].getDasaTime();
                lastEndDate = tempArrPrDasa[iPos - 1].getDasaTime();

            }
            iSecondLevelClickedPlanetNo = tempArrPrDasa[iPos].getPlanetNo();

            iPlNo = iSecondLevelClickedPlanetNo;
            total = total * yearOfVimPlanets[iSecondLevelClickedPlanetNo] / 120;

            if (periodSpan < 0) {

                periodSpan += total;

                lastEndDate -= total;

            }

            do {
                _dasaSecondLevel[index] = new BeanDasa();
                _dasaSecondLevel[index].setPlanetNo(iPlNo);
                _dasaSecondLevel[index].setDasaTime(calculateDasaPeriod(iPlNo));

                iPlNo++;
                if (iPlNo >= 9)
                    iPlNo -= 9;

                index++;

            } while (index < 9);
        } catch (Exception e) {

            throw e;
        }

        return _dasaSecondLevel;

    }


}
