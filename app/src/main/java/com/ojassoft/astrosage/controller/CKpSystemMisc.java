package com.ojassoft.astrosage.controller;

import com.ojassoft.astrosage.beans.BeanKpCIL;
import com.ojassoft.astrosage.beans.BeanKpKhullarCIL;
import com.ojassoft.astrosage.beans.BeanKpPlanetSigWithStrenght;
import com.ojassoft.astrosage.beans.BeanNakshtraNadi;
import com.ojassoft.astrosage.beans.InKPPlanetsAndCusp;
import com.ojassoft.astrosage.beans.InKpPlanetSignification;
import com.ojassoft.astrosage.beans.NadiValues;
import com.ojassoft.astrosage.beans.OutKpHouseSignificators;
import com.ojassoft.astrosage.beans.OutKpMisc;
import com.ojassoft.astrosage.beans.OutKpRulingPlanets;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.kpextension.BeanKP4Step;
import com.ojassoft.kpextension.CKp4StepCalculation;
import com.ojassoft.kpextension.CKpRefactorExtension;
import com.ojassoft.kpextension.GlobalVariablesKPExtension;

public class CKpSystemMisc {
    /**
     * This function return Kp Cuspal Positions array according to passed
     * InKPPlanetsAndCusp object.
     *
     * @param obj
     * @return String [][]
     * @throws Exception
     */
    public String[][] getKpCuspalPositions(InKPPlanetsAndCusp obj, String[] RasiLord, String[] NakLord, String DegSign, String MinSign, String SecSign)
            throws Exception {

        String[][] kpCuspalPositions = new String[12][2];
        try {
            kpCuspalPositions[0][0] = CUtils.FormatDMSInStringWithSign(obj
                    .getKpCusp1(), DegSign, MinSign, SecSign);
            kpCuspalPositions[0][1] = CUtils.getRasiNakSubSub(obj.getKpCusp1(), RasiLord, NakLord);

            kpCuspalPositions[1][0] = CUtils.FormatDMSInStringWithSign(obj
                    .getKpCusp2(), DegSign, MinSign, SecSign);
            kpCuspalPositions[1][1] = CUtils.getRasiNakSubSub(obj.getKpCusp2(), RasiLord, NakLord);

            kpCuspalPositions[2][0] = CUtils.FormatDMSInStringWithSign(obj
                    .getKpCusp3(), DegSign, MinSign, SecSign);
            kpCuspalPositions[2][1] = CUtils.getRasiNakSubSub(obj.getKpCusp3(), RasiLord, NakLord);

            kpCuspalPositions[3][0] = CUtils.FormatDMSInStringWithSign(obj
                    .getKpCusp4(), DegSign, MinSign, SecSign);
            kpCuspalPositions[3][1] = CUtils.getRasiNakSubSub(obj.getKpCusp4(), RasiLord, NakLord);

            kpCuspalPositions[4][0] = CUtils.FormatDMSInStringWithSign(obj
                    .getKpCusp5(), DegSign, MinSign, SecSign);
            kpCuspalPositions[4][1] = CUtils.getRasiNakSubSub(obj.getKpCusp5(), RasiLord, NakLord);

            kpCuspalPositions[5][0] = CUtils.FormatDMSInStringWithSign(obj
                    .getKpCusp6(), DegSign, MinSign, SecSign);
            kpCuspalPositions[5][1] = CUtils.getRasiNakSubSub(obj.getKpCusp6(), RasiLord, NakLord);

            kpCuspalPositions[6][0] = CUtils.FormatDMSInStringWithSign(obj
                    .getKpCusp7(), DegSign, MinSign, SecSign);
            kpCuspalPositions[6][1] = CUtils.getRasiNakSubSub(obj.getKpCusp7(), RasiLord, NakLord);

            kpCuspalPositions[7][0] = CUtils.FormatDMSInStringWithSign(obj
                    .getKpCusp8(), DegSign, MinSign, SecSign);
            kpCuspalPositions[7][1] = CUtils.getRasiNakSubSub(obj.getKpCusp8(), RasiLord, NakLord);

            kpCuspalPositions[8][0] = CUtils.FormatDMSInStringWithSign(obj
                    .getKpCusp9(), DegSign, MinSign, SecSign);
            kpCuspalPositions[8][1] = CUtils.getRasiNakSubSub(obj.getKpCusp9(), RasiLord, NakLord);

            kpCuspalPositions[9][0] = CUtils.FormatDMSInStringWithSign(obj
                    .getKpCusp10(), DegSign, MinSign, SecSign);
            kpCuspalPositions[9][1] = CUtils
                    .getRasiNakSubSub(obj.getKpCusp10(), RasiLord, NakLord);

            kpCuspalPositions[10][0] = CUtils.FormatDMSInStringWithSign(obj
                    .getKpCusp11(), DegSign, MinSign, SecSign);
            kpCuspalPositions[10][1] = CUtils.getRasiNakSubSub(obj
                    .getKpCusp11(), RasiLord, NakLord);

            kpCuspalPositions[11][0] = CUtils.FormatDMSInStringWithSign(obj
                    .getKpCusp12(), DegSign, MinSign, SecSign);
            kpCuspalPositions[11][1] = CUtils.getRasiNakSubSub(obj
                    .getKpCusp12(), RasiLord, NakLord);
        } catch (Exception e) {
            throw e;
        }

        return kpCuspalPositions;
    }

    /**
     * This function return KP Planet position array according to passed
     * InKPPlanetsAndCusp object.
     *
     * @param obj
     * @return String [][]
     * @throws Exception
     */
    public String[][] getKpPlanetaryPositions(InKPPlanetsAndCusp obj, String[] RasiLord, String[] NakLord, String DegSign, String MinSign, String SecSign)
            throws Exception {
        String[][] kpPlanetPosition = new String[13][2];
        try {

            ////Log.e("CUSP", String.valueOf(obj.getKpCusp1()));
            kpPlanetPosition[0][0] = CUtils.FormatDMSInStringWithSign(obj
                    .getKpCusp1(), DegSign, MinSign, SecSign);
            kpPlanetPosition[0][1] = CUtils.getRasiNakSubSub(obj.getKpCusp1(), RasiLord, NakLord);

            kpPlanetPosition[1][0] = CUtils.FormatDMSInStringWithSign(obj
                    .getSun(), DegSign, MinSign, SecSign);
            kpPlanetPosition[1][1] = CUtils.getRasiNakSubSub(obj.getSun(), RasiLord, NakLord);

            kpPlanetPosition[2][0] = CUtils.FormatDMSInStringWithSign(obj
                    .getMoon(), DegSign, MinSign, SecSign);
            kpPlanetPosition[2][1] = CUtils.getRasiNakSubSub(obj.getMoon(), RasiLord, NakLord);

            kpPlanetPosition[3][0] = CUtils.FormatDMSInStringWithSign(obj
                    .getMarsh(), DegSign, MinSign, SecSign);
            kpPlanetPosition[3][1] = CUtils.getRasiNakSubSub(obj.getMarsh(), RasiLord, NakLord);

            kpPlanetPosition[4][0] = CUtils.FormatDMSInStringWithSign(obj
                    .getMercury(), DegSign, MinSign, SecSign);
            kpPlanetPosition[4][1] = CUtils.getRasiNakSubSub(obj.getMercury(), RasiLord, NakLord);

            kpPlanetPosition[5][0] = CUtils.FormatDMSInStringWithSign(obj
                    .getJup(), DegSign, MinSign, SecSign);
            kpPlanetPosition[5][1] = CUtils.getRasiNakSubSub(obj.getJup(), RasiLord, NakLord);

            kpPlanetPosition[6][0] = CUtils.FormatDMSInStringWithSign(obj
                    .getVenus(), DegSign, MinSign, SecSign);
            kpPlanetPosition[6][1] = CUtils.getRasiNakSubSub(obj.getVenus(), RasiLord, NakLord);

            kpPlanetPosition[7][0] = CUtils.FormatDMSInStringWithSign(obj
                    .getSat(), DegSign, MinSign, SecSign);
            kpPlanetPosition[7][1] = CUtils.getRasiNakSubSub(obj.getSat(), RasiLord, NakLord);

            kpPlanetPosition[8][0] = CUtils.FormatDMSInStringWithSign(obj
                    .getRahu(), DegSign, MinSign, SecSign);
            kpPlanetPosition[8][1] = CUtils.getRasiNakSubSub(obj.getRahu(), RasiLord, NakLord);

            kpPlanetPosition[9][0] = CUtils.FormatDMSInStringWithSign(obj
                    .getKetu(), DegSign, MinSign, SecSign);
            kpPlanetPosition[9][1] = CUtils.getRasiNakSubSub(obj.getKetu(), RasiLord, NakLord);

            kpPlanetPosition[10][0] = CUtils.FormatDMSInStringWithSign(obj
                    .getUranus(), DegSign, MinSign, SecSign);
            kpPlanetPosition[10][1] = CUtils.getRasiNakSubSub(obj.getUranus(), RasiLord, NakLord);

            kpPlanetPosition[11][0] = CUtils.FormatDMSInStringWithSign(obj
                    .getNeptune(), DegSign, MinSign, SecSign);
            kpPlanetPosition[11][1] = CUtils.getRasiNakSubSub(obj.getNeptune(), RasiLord, NakLord);

            kpPlanetPosition[12][0] = CUtils.FormatDMSInStringWithSign(obj
                    .getPluto(), DegSign, MinSign, SecSign);
            kpPlanetPosition[12][1] = CUtils.getRasiNakSubSub(obj.getPluto(), RasiLord, NakLord);
        } catch (Exception e) {
            throw e;
        }
        return kpPlanetPosition;
    }

    /**
     * This function put KpPlanetSignificators into array for further
     * operations.
     *
     * @param obj
     * @return String[]
     * @throws Exception
     */
    public String[] getKpPlanetSignificators(InKpPlanetSignification obj)
            throws Exception {
        String[] kpPlanetSig = new String[9];
        try {
            for (int i = 0; i < 9; i++)
                kpPlanetSig[i] = new String();
            kpPlanetSig[0] = obj.getSunSignification();
            kpPlanetSig[1] = obj.getMoonSignification();
            kpPlanetSig[2] = obj.getMarsSignification();
            kpPlanetSig[3] = obj.getMercurySignification();
            kpPlanetSig[4] = obj.getJupiterSignification();
            kpPlanetSig[5] = obj.getVenusSignification();
            kpPlanetSig[6] = obj.getSaturnSignification();
            kpPlanetSig[7] = obj.getRahuSignification();
            kpPlanetSig[8] = obj.getKetuSignification();
        } catch (Exception e) {
            throw e;
        }
        return kpPlanetSig;

    }

    /**
     * This function return filled object of OutKpHouseSignificators to show on
     * Kp House Significators on UI according to passed InKpPlanetSignification
     * object values .
     *
     * @param obj
     * @return OutKpHouseSignificators
     * @throws Exception
     */
    public OutKpHouseSignificators getKpHouseSignificators(
            InKpPlanetSignification obj) throws Exception {
        String[] strTempArr = new String[9];
        OutKpHouseSignificators _outKpHouseSignificators = new OutKpHouseSignificators();
        try {
            strTempArr[0] = obj.getSunSignification();
            strTempArr[1] = obj.getMoonSignification();
            strTempArr[2] = obj.getMarsSignification();
            strTempArr[3] = obj.getMercurySignification();
            strTempArr[4] = obj.getJupiterSignification();
            strTempArr[5] = obj.getVenusSignification();
            strTempArr[6] = obj.getSaturnSignification();
            strTempArr[7] = obj.getRahuSignification();
            strTempArr[8] = obj.getKetuSignification();

            for (int j = 0; j < 9; j++) {
                String[] tArray = strTempArr[j].split(" ");

                for (int i = 0; i < tArray.length; i++) {

                    switch (Integer.valueOf(tArray[i])) {
                        case 1:
                            _outKpHouseSignificators.getBhav1().add(
                                    String.valueOf(j));

                            break;
                        case 2:
                            _outKpHouseSignificators.getBhav2().add(
                                    String.valueOf(j));

                            break;
                        case 3:
                            _outKpHouseSignificators.getBhav3().add(
                                    String.valueOf(j));

                            break;
                        case 4:
                            _outKpHouseSignificators.getBhav4().add(
                                    String.valueOf(j));

                            break;
                        case 5:
                            _outKpHouseSignificators.getBhav5().add(
                                    String.valueOf(j));

                            break;
                        case 6:
                            _outKpHouseSignificators.getBhav6().add(
                                    String.valueOf(j));

                            break;
                        case 7:
                            _outKpHouseSignificators.getBhav7().add(
                                    String.valueOf(j));

                            break;
                        case 8:
                            _outKpHouseSignificators.getBhav8().add(
                                    String.valueOf(j));

                            break;
                        case 9:
                            _outKpHouseSignificators.getBhav9().add(
                                    String.valueOf(j));

                            break;
                        case 10:
                            _outKpHouseSignificators.getBhav10().add(
                                    String.valueOf(j));

                            break;
                        case 11:
                            _outKpHouseSignificators.getBhav11().add(
                                    String.valueOf(j));

                            break;
                        case 12:
                            _outKpHouseSignificators.getBhav12().add(
                                    String.valueOf(j));

                            break;
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }

        return _outKpHouseSignificators;
    }

    // ADDED BY BIJENDRA ON 16-MARCH-2013
    public BeanKpPlanetSigWithStrenght[] getPlanetSignWithStrengthBeansArray(
            InKPPlanetsAndCusp plntAndCusp) {

        double[] kpcups = getKpCuspArray(plntAndCusp);
        double[] kpplnts = getKpPlanetArray(plntAndCusp);
        int[] plntStrength = null;
        BeanKpPlanetSigWithStrenght bean[] = new BeanKpPlanetSigWithStrenght[9];

        for (int i = 0; i < bean.length; i++)

            bean[i] = new BeanKpPlanetSigWithStrenght();

        CKpRefactorExtension kpr = new CKpRefactorExtension(kpplnts, kpcups);
        plntStrength = kpr.getPlanetStrength();

        bean[0].setPlanet(GlobalVariablesKPExtension.SUN);
        bean[0].setLevel1(kpr
                .getPlanetSignificationLevel1(GlobalVariablesKPExtension.SUN));
        bean[0].setLevel2(kpr
                .getPlanetSignificationLevel2(kpplnts[GlobalVariablesKPExtension.SUN]));
        bean[0].setLevel3(kpr
                .getPlanetSignificationLevel3(kpplnts[GlobalVariablesKPExtension.SUN]));
        bean[0].setLevel4(kpr
                .getPlanetSignificationLevel4(GlobalVariablesKPExtension.SUN));
        bean[0].setPlanetStrength(plntStrength[0]);

        bean[1].setPlanet(GlobalVariablesKPExtension.MOON);
        bean[1].setLevel1(kpr
                .getPlanetSignificationLevel1(GlobalVariablesKPExtension.MOON));
        bean[1].setLevel2(kpr
                .getPlanetSignificationLevel2(kpplnts[GlobalVariablesKPExtension.MOON]));
        bean[1].setLevel3(kpr
                .getPlanetSignificationLevel3(kpplnts[GlobalVariablesKPExtension.MOON]));
        bean[1].setLevel4(kpr
                .getPlanetSignificationLevel4(GlobalVariablesKPExtension.MOON));
        bean[1].setPlanetStrength(plntStrength[1]);

        bean[2].setPlanet(GlobalVariablesKPExtension.MARS);
        bean[2].setLevel1(kpr
                .getPlanetSignificationLevel1(GlobalVariablesKPExtension.MARS));
        bean[2].setLevel2(kpr
                .getPlanetSignificationLevel2(kpplnts[GlobalVariablesKPExtension.MARS]));
        bean[2].setLevel3(kpr
                .getPlanetSignificationLevel3(kpplnts[GlobalVariablesKPExtension.MARS]));
        bean[2].setLevel4(kpr
                .getPlanetSignificationLevel4(GlobalVariablesKPExtension.MARS));
        bean[2].setPlanetStrength(plntStrength[2]);

        bean[3].setPlanet(GlobalVariablesKPExtension.MERCURY);
        bean[3].setLevel1(kpr
                .getPlanetSignificationLevel1(GlobalVariablesKPExtension.MERCURY));
        bean[3].setLevel2(kpr
                .getPlanetSignificationLevel2(kpplnts[GlobalVariablesKPExtension.MERCURY]));
        bean[3].setLevel3(kpr
                .getPlanetSignificationLevel3(kpplnts[GlobalVariablesKPExtension.MERCURY]));
        bean[3].setLevel4(kpr
                .getPlanetSignificationLevel4(GlobalVariablesKPExtension.MERCURY));
        bean[3].setPlanetStrength(plntStrength[3]);

        bean[4].setPlanet(GlobalVariablesKPExtension.JUPITER);
        bean[4].setLevel1(kpr
                .getPlanetSignificationLevel1(GlobalVariablesKPExtension.JUPITER));
        bean[4].setLevel2(kpr
                .getPlanetSignificationLevel2(kpplnts[GlobalVariablesKPExtension.JUPITER]));
        bean[4].setLevel3(kpr
                .getPlanetSignificationLevel3(kpplnts[GlobalVariablesKPExtension.JUPITER]));
        bean[4].setLevel4(kpr
                .getPlanetSignificationLevel4(GlobalVariablesKPExtension.JUPITER));
        bean[4].setPlanetStrength(plntStrength[4]);

        bean[5].setPlanet(GlobalVariablesKPExtension.VENUS);
        bean[5].setLevel1(kpr
                .getPlanetSignificationLevel1(GlobalVariablesKPExtension.VENUS));
        bean[5].setLevel2(kpr
                .getPlanetSignificationLevel2(kpplnts[GlobalVariablesKPExtension.VENUS]));
        bean[5].setLevel3(kpr
                .getPlanetSignificationLevel3(kpplnts[GlobalVariablesKPExtension.VENUS]));
        bean[5].setLevel4(kpr
                .getPlanetSignificationLevel4(GlobalVariablesKPExtension.VENUS));
        bean[5].setPlanetStrength(plntStrength[5]);

        bean[6].setPlanet(GlobalVariablesKPExtension.SAT);
        bean[6].setLevel1(kpr
                .getPlanetSignificationLevel1(GlobalVariablesKPExtension.SAT));
        bean[6].setLevel2(kpr
                .getPlanetSignificationLevel2(kpplnts[GlobalVariablesKPExtension.SAT]));
        bean[6].setLevel3(kpr
                .getPlanetSignificationLevel3(kpplnts[GlobalVariablesKPExtension.SAT]));
        bean[6].setLevel4(kpr
                .getPlanetSignificationLevel4(GlobalVariablesKPExtension.SAT));
        bean[6].setPlanetStrength(plntStrength[6]);

        bean[7].setPlanet(GlobalVariablesKPExtension.RAHU);
        bean[7].setLevel1(kpr
                .getPlanetSignificationLevel1(GlobalVariablesKPExtension.RAHU));
        bean[7].setLevel2(kpr
                .getPlanetSignificationLevel2(kpplnts[GlobalVariablesKPExtension.RAHU]));
        bean[7].setLevel3(kpr
                .getPlanetSignificationLevel3(kpplnts[GlobalVariablesKPExtension.RAHU]));
        bean[7].setLevel4(kpr
                .getPlanetSignificationLevel4(GlobalVariablesKPExtension.RAHU));
        bean[7].setPlanetStrength(plntStrength[7]);

        bean[8].setPlanet(GlobalVariablesKPExtension.KETU);
        bean[8].setLevel1(kpr
                .getPlanetSignificationLevel1(GlobalVariablesKPExtension.KETU));
        bean[8].setLevel2(kpr
                .getPlanetSignificationLevel2(kpplnts[GlobalVariablesKPExtension.KETU]));
        bean[8].setLevel3(kpr
                .getPlanetSignificationLevel3(kpplnts[GlobalVariablesKPExtension.KETU]));
        bean[8].setLevel4(kpr
                .getPlanetSignificationLevel4(GlobalVariablesKPExtension.KETU));
        bean[8].setPlanetStrength(plntStrength[8]);

        return bean;
    }

    /**
     * This function return kp cusp array
     *
     * @param plntAndCusp
     * @return double[]
     */
    public double[] getKpCuspArray(InKPPlanetsAndCusp plntAndCusp) {
        double[] kpCuspArray = new double[12];
        kpCuspArray[0] = plntAndCusp.getKpCusp1();
        kpCuspArray[1] = plntAndCusp.getKpCusp2();
        kpCuspArray[2] = plntAndCusp.getKpCusp3();
        kpCuspArray[3] = plntAndCusp.getKpCusp4();
        kpCuspArray[4] = plntAndCusp.getKpCusp5();
        kpCuspArray[5] = plntAndCusp.getKpCusp6();
        kpCuspArray[6] = plntAndCusp.getKpCusp7();
        kpCuspArray[7] = plntAndCusp.getKpCusp8();
        kpCuspArray[8] = plntAndCusp.getKpCusp9();
        kpCuspArray[9] = plntAndCusp.getKpCusp10();
        kpCuspArray[10] = plntAndCusp.getKpCusp11();
        kpCuspArray[11] = plntAndCusp.getKpCusp12();

        return kpCuspArray;
    }

    public double[] getKpPlanetArray(InKPPlanetsAndCusp plntAndCusp) {
        double[] kpPlanetArray = new double[9];
        kpPlanetArray[0] = plntAndCusp.getSun();
        kpPlanetArray[1] = plntAndCusp.getMoon();
        kpPlanetArray[2] = plntAndCusp.getMarsh();
        kpPlanetArray[3] = plntAndCusp.getMercury();
        kpPlanetArray[4] = plntAndCusp.getJup();
        kpPlanetArray[5] = plntAndCusp.getVenus();
        kpPlanetArray[6] = plntAndCusp.getSat();
        kpPlanetArray[7] = plntAndCusp.getRahu();
        kpPlanetArray[8] = plntAndCusp.getKetu();

        return kpPlanetArray;
    }

    /**
     * This function return filled object of
     * OutKpMisc to show on KP Misc on UI
     * according to passed InKPPlanetsAndCusp
     * object values .
     *
     * @param obj
     * @return OutKpMisc
     * @throws Exception
     */
    public OutKpMisc getKpMisc(InKPPlanetsAndCusp obj, String[] RasiLordName, String[] NakLordName) throws Exception {
        OutKpMisc _outKpMisc = new OutKpMisc();
        try {

            //_outKpMisc.setFortunaRasit( CGlobalVariables.rasiLord[CUtils.getRasiNumber(obj.getKpFortuna())]);
            _outKpMisc.setFortunaRasit(RasiLordName[CUtils.getRasiNumber(obj.getKpFortuna())]);
            _outKpMisc.setFortunaDeg(obj.getKpFortuna());
            //_outKpMisc.setFortunaSubSub( CGlobalVariables.nakLord[CUtils.getSubLordNumber(obj.getKpFortuna())]);
            _outKpMisc.setFortunaSubSub(NakLordName[CUtils.getSubLordNumber(obj.getKpFortuna())]);
            _outKpMisc.setKpAyan(obj.getKpAyan());
        } catch (Exception e) {
            throw e;
        }

        return _outKpMisc;
    }

    public BeanNakshtraNadi[] getNakshtraNadi(InKPPlanetsAndCusp plntAndCusp) {
        BeanNakshtraNadi[] nadis = new BeanNakshtraNadi[9];
        for (int i = 0; i < 9; i++) {
            nadis[i] = new BeanNakshtraNadi();
        }
        double[] kpcups = getKpCuspArray(plntAndCusp);
        double[] kpplnts = getKpPlanetArray(plntAndCusp);


        CKpRefactorExtension kpr = new CKpRefactorExtension(kpplnts, kpcups);
        nadis[0].setPlanetNakstraNadi(new NadiValues(GlobalVariablesKPExtension.SUN, kpr.getPlanetNadi(GlobalVariablesKPExtension.SUN)));
        nadis[0].setPlanetStarLordNadi(new NadiValues(kpr.getStarLord(kpplnts[GlobalVariablesKPExtension.SUN]), kpr.getPlanetStarLordNadi(GlobalVariablesKPExtension.SUN)));
        nadis[0].setPlanetSubLordNadi(new NadiValues(kpr.getSubLord(kpplnts[GlobalVariablesKPExtension.SUN]), kpr.getPlanetSubLordNadi(GlobalVariablesKPExtension.SUN)));


        nadis[1].setPlanetNakstraNadi(new NadiValues(GlobalVariablesKPExtension.MOON, kpr.getPlanetNadi(GlobalVariablesKPExtension.MOON)));
        nadis[1].setPlanetStarLordNadi(new NadiValues(kpr.getStarLord(kpplnts[GlobalVariablesKPExtension.MOON]), kpr.getPlanetStarLordNadi(GlobalVariablesKPExtension.MOON)));
        nadis[1].setPlanetSubLordNadi(new NadiValues(kpr.getSubLord(kpplnts[GlobalVariablesKPExtension.MOON]), kpr.getPlanetSubLordNadi(GlobalVariablesKPExtension.MOON)));

        nadis[2].setPlanetNakstraNadi(new NadiValues(GlobalVariablesKPExtension.MARS, kpr.getPlanetNadi(GlobalVariablesKPExtension.MARS)));
        nadis[2].setPlanetStarLordNadi(new NadiValues(kpr.getStarLord(kpplnts[GlobalVariablesKPExtension.MARS]), kpr.getPlanetStarLordNadi(GlobalVariablesKPExtension.MARS)));
        nadis[2].setPlanetSubLordNadi(new NadiValues(kpr.getSubLord(kpplnts[GlobalVariablesKPExtension.MARS]), kpr.getPlanetSubLordNadi(GlobalVariablesKPExtension.MARS)));

        nadis[3].setPlanetNakstraNadi(new NadiValues(GlobalVariablesKPExtension.MERCURY, kpr.getPlanetNadi(GlobalVariablesKPExtension.MERCURY)));
        nadis[3].setPlanetStarLordNadi(new NadiValues(kpr.getStarLord(kpplnts[GlobalVariablesKPExtension.MERCURY]), kpr.getPlanetStarLordNadi(GlobalVariablesKPExtension.MERCURY)));
        nadis[3].setPlanetSubLordNadi(new NadiValues(kpr.getSubLord(kpplnts[GlobalVariablesKPExtension.MERCURY]), kpr.getPlanetSubLordNadi(GlobalVariablesKPExtension.MERCURY)));

        nadis[4].setPlanetNakstraNadi(new NadiValues(GlobalVariablesKPExtension.JUPITER, kpr.getPlanetNadi(GlobalVariablesKPExtension.JUPITER)));
        nadis[4].setPlanetStarLordNadi(new NadiValues(kpr.getStarLord(kpplnts[GlobalVariablesKPExtension.JUPITER]), kpr.getPlanetStarLordNadi(GlobalVariablesKPExtension.JUPITER)));
        nadis[4].setPlanetSubLordNadi(new NadiValues(kpr.getSubLord(kpplnts[GlobalVariablesKPExtension.JUPITER]), kpr.getPlanetSubLordNadi(GlobalVariablesKPExtension.JUPITER)));

        nadis[5].setPlanetNakstraNadi(new NadiValues(GlobalVariablesKPExtension.VENUS, kpr.getPlanetNadi(GlobalVariablesKPExtension.VENUS)));
        nadis[5].setPlanetStarLordNadi(new NadiValues(kpr.getStarLord(kpplnts[GlobalVariablesKPExtension.VENUS]), kpr.getPlanetStarLordNadi(GlobalVariablesKPExtension.VENUS)));
        nadis[5].setPlanetSubLordNadi(new NadiValues(kpr.getSubLord(kpplnts[GlobalVariablesKPExtension.VENUS]), kpr.getPlanetSubLordNadi(GlobalVariablesKPExtension.VENUS)));

        nadis[6].setPlanetNakstraNadi(new NadiValues(GlobalVariablesKPExtension.SAT, kpr.getPlanetNadi(GlobalVariablesKPExtension.SAT)));
        nadis[6].setPlanetStarLordNadi(new NadiValues(kpr.getStarLord(kpplnts[GlobalVariablesKPExtension.SAT]), kpr.getPlanetStarLordNadi(GlobalVariablesKPExtension.SAT)));
        nadis[6].setPlanetSubLordNadi(new NadiValues(kpr.getSubLord(kpplnts[GlobalVariablesKPExtension.SAT]), kpr.getPlanetSubLordNadi(GlobalVariablesKPExtension.SAT)));

        nadis[7].setPlanetNakstraNadi(new NadiValues(GlobalVariablesKPExtension.RAHU, kpr.getPlanetNadi(GlobalVariablesKPExtension.RAHU)));
        nadis[7].setPlanetStarLordNadi(new NadiValues(kpr.getStarLord(kpplnts[GlobalVariablesKPExtension.RAHU]), kpr.getPlanetStarLordNadi(GlobalVariablesKPExtension.RAHU)));
        nadis[7].setPlanetSubLordNadi(new NadiValues(kpr.getSubLord(kpplnts[GlobalVariablesKPExtension.RAHU]), kpr.getPlanetSubLordNadi(GlobalVariablesKPExtension.RAHU)));

        nadis[8].setPlanetNakstraNadi(new NadiValues(GlobalVariablesKPExtension.KETU, kpr.getPlanetNadi(GlobalVariablesKPExtension.KETU)));
        nadis[8].setPlanetStarLordNadi(new NadiValues(kpr.getStarLord(kpplnts[GlobalVariablesKPExtension.KETU]), kpr.getPlanetStarLordNadi(GlobalVariablesKPExtension.KETU)));
        nadis[8].setPlanetSubLordNadi(new NadiValues(kpr.getSubLord(kpplnts[GlobalVariablesKPExtension.KETU]), kpr.getPlanetSubLordNadi(GlobalVariablesKPExtension.KETU)));


        return nadis;
    }

    /**
     * This function is used to return KCIL bean after array calculation
     *
     * @param inKPPlanetsAndCuspObject
     * @return array of BeanKpKCIL
     */
    public BeanKpKhullarCIL[] getKCILBeansArray(InKPPlanetsAndCusp inKPPlanetsAndCuspObject) {
        double[] kpcups = getKpCuspArray(inKPPlanetsAndCuspObject);
        double[] kpplnts = getKpPlanetArray(inKPPlanetsAndCuspObject);
        BeanKpKhullarCIL[] bean = new BeanKpKhullarCIL[9];
        for (int i = 0; i < 9; i++) {
            bean[i] = new BeanKpKhullarCIL();
            bean[i].setPlanet(i);
        }

        CKpRefactorExtension kpr = new CKpRefactorExtension(kpplnts, kpcups);
        //SUN
        bean[0].setPlanet(GlobalVariablesKPExtension.SUN);
        bean[0].setKCILType1(kpr.getKCILType1(GlobalVariablesKPExtension.SUN));
        bean[0].setKCILType2(kpr.getKCILType2(GlobalVariablesKPExtension.SUN));
        bean[0].setKCILType3(kpr.getKCILType3(GlobalVariablesKPExtension.SUN));
        bean[0].setKCILType4(kpr.getKCILType4(GlobalVariablesKPExtension.SUN));

        //MOON
        bean[1].setPlanet(GlobalVariablesKPExtension.MOON);
        bean[1].setKCILType1(kpr.getKCILType1(GlobalVariablesKPExtension.MOON));
        bean[1].setKCILType2(kpr.getKCILType2(GlobalVariablesKPExtension.MOON));
        bean[1].setKCILType3(kpr.getKCILType3(GlobalVariablesKPExtension.MOON));
        bean[1].setKCILType4(kpr.getKCILType4(GlobalVariablesKPExtension.MOON));

        //MARS
        bean[2].setPlanet(GlobalVariablesKPExtension.MARS);
        bean[2].setKCILType1(kpr.getKCILType1(GlobalVariablesKPExtension.MARS));
        bean[2].setKCILType2(kpr.getKCILType2(GlobalVariablesKPExtension.MARS));
        bean[2].setKCILType3(kpr.getKCILType3(GlobalVariablesKPExtension.MARS));
        bean[2].setKCILType4(kpr.getKCILType4(GlobalVariablesKPExtension.MARS));

        //MERCURY
        bean[3].setPlanet(GlobalVariablesKPExtension.MERCURY);
        bean[3].setKCILType1(kpr.getKCILType1(GlobalVariablesKPExtension.MERCURY));
        bean[3].setKCILType2(kpr.getKCILType2(GlobalVariablesKPExtension.MERCURY));
        bean[3].setKCILType3(kpr.getKCILType3(GlobalVariablesKPExtension.MERCURY));
        bean[3].setKCILType4(kpr.getKCILType4(GlobalVariablesKPExtension.MERCURY));

        //JUPITER
        bean[4].setPlanet(GlobalVariablesKPExtension.JUPITER);
        bean[4].setKCILType1(kpr.getKCILType1(GlobalVariablesKPExtension.JUPITER));
        bean[4].setKCILType2(kpr.getKCILType2(GlobalVariablesKPExtension.JUPITER));
        bean[4].setKCILType3(kpr.getKCILType3(GlobalVariablesKPExtension.JUPITER));
        bean[4].setKCILType4(kpr.getKCILType4(GlobalVariablesKPExtension.JUPITER));

        //VENUS
        bean[5].setPlanet(GlobalVariablesKPExtension.VENUS);
        bean[5].setKCILType1(kpr.getKCILType1(GlobalVariablesKPExtension.VENUS));
        bean[5].setKCILType2(kpr.getKCILType2(GlobalVariablesKPExtension.VENUS));
        bean[5].setKCILType3(kpr.getKCILType3(GlobalVariablesKPExtension.VENUS));
        bean[5].setKCILType4(kpr.getKCILType4(GlobalVariablesKPExtension.VENUS));

        //SAT
        bean[6].setPlanet(GlobalVariablesKPExtension.SAT);
        bean[6].setKCILType1(kpr.getKCILType1(GlobalVariablesKPExtension.SAT));
        bean[6].setKCILType2(kpr.getKCILType2(GlobalVariablesKPExtension.SAT));
        bean[6].setKCILType3(kpr.getKCILType3(GlobalVariablesKPExtension.SAT));
        bean[6].setKCILType4(kpr.getKCILType4(GlobalVariablesKPExtension.SAT));

        //RAHU
        bean[7].setPlanet(GlobalVariablesKPExtension.RAHU);
        bean[7].setKCILType1(kpr.getKCILType1(GlobalVariablesKPExtension.RAHU));
        bean[7].setKCILType2(kpr.getKCILType2(GlobalVariablesKPExtension.RAHU));
        bean[7].setKCILType3(kpr.getKCILType3(GlobalVariablesKPExtension.RAHU));
        bean[7].setKCILType4(kpr.getKCILType4(GlobalVariablesKPExtension.RAHU));

        //KETU
        bean[8].setPlanet(GlobalVariablesKPExtension.KETU);
        bean[8].setKCILType1(kpr.getKCILType1(GlobalVariablesKPExtension.KETU));
        bean[8].setKCILType2(kpr.getKCILType2(GlobalVariablesKPExtension.KETU));
        bean[8].setKCILType3(kpr.getKCILType3(GlobalVariablesKPExtension.KETU));
        bean[8].setKCILType4(kpr.getKCILType4(GlobalVariablesKPExtension.KETU));
        return bean;
    }

    public BeanKP4Step[] getBeanKp4StepBeansArrayNew(InKPPlanetsAndCusp inKPPlanetsAndCuspObject) {
        double[] kpcups = getKpCuspArray(inKPPlanetsAndCuspObject);
        double[] kpplnts = getKpPlanetArray(inKPPlanetsAndCuspObject);


        BeanKP4Step[] bean = new BeanKP4Step[9];
        for (int i = 0; i < bean.length; i++)
            bean[i] = new BeanKP4Step();

        CKp4StepCalculation kp4Step = new CKp4StepCalculation(kpplnts, kpcups);
        kp4Step.calculate();

        bean[0] = kp4Step.getKp4StepForPlanet(GlobalVariablesKPExtension.SUN);
        bean[1] = kp4Step.getKp4StepForPlanet(GlobalVariablesKPExtension.MOON);
        bean[2] = kp4Step.getKp4StepForPlanet(GlobalVariablesKPExtension.MARS);
        bean[3] = kp4Step.getKp4StepForPlanet(GlobalVariablesKPExtension.MERCURY);
        bean[4] = kp4Step.getKp4StepForPlanet(GlobalVariablesKPExtension.JUPITER);
        bean[5] = kp4Step.getKp4StepForPlanet(GlobalVariablesKPExtension.VENUS);
        bean[6] = kp4Step.getKp4StepForPlanet(GlobalVariablesKPExtension.SAT);
        bean[7] = kp4Step.getKp4StepForPlanet(GlobalVariablesKPExtension.RAHU);
        bean[8] = kp4Step.getKp4StepForPlanet(GlobalVariablesKPExtension.KETU);

			/*for(int i=0;i<bean.length;i++)
				//Log.e("printStep1",String.valueOf(bean[i].getKp4Step1().getPlanet()));
			*/


        return bean;
    }

    /**
     * This function return the array of KP cuspal interlink based on cusp
     *
     * @param inKPPlanetsAndCuspObject
     * @return array of BeanKpCIL
     */
    public BeanKpCIL[] getKPCILBeansArray(InKPPlanetsAndCusp inKPPlanetsAndCuspObject) {
        double[] kpcups = getKpCuspArray(inKPPlanetsAndCuspObject);
        double[] kpplnts = getKpPlanetArray(inKPPlanetsAndCuspObject);
        BeanKpCIL[] bean = new BeanKpCIL[12];
        for (int i = 0; i < 12; i++) {
            bean[i] = new BeanKpCIL();
            //bean[i].setCusp(i);
        }

        CKpRefactorExtension kpr = new CKpRefactorExtension(kpplnts, kpcups);

        //CUSP 1
        bean[0].setCusp(GlobalVariablesKPExtension.CUSP1);
        bean[0].setKpCILType1(kpr.getKP_CIL_Type1(GlobalVariablesKPExtension.CUSP1));
        bean[0].setKpCILType2(kpr.getKP_CIL_Type2(GlobalVariablesKPExtension.CUSP1));
        bean[0].setKpCILType3(kpr.getKP_CIL_Type3(GlobalVariablesKPExtension.CUSP1));
        bean[0].setKpCILType4(kpr.getKP_CIL_Type4(GlobalVariablesKPExtension.CUSP1));

        //CUSP 2
        bean[1].setCusp(GlobalVariablesKPExtension.CUSP2);
        bean[1].setKpCILType1(kpr.getKP_CIL_Type1(GlobalVariablesKPExtension.CUSP2));
        bean[1].setKpCILType2(kpr.getKP_CIL_Type2(GlobalVariablesKPExtension.CUSP2));
        bean[1].setKpCILType3(kpr.getKP_CIL_Type3(GlobalVariablesKPExtension.CUSP2));
        bean[1].setKpCILType4(kpr.getKP_CIL_Type4(GlobalVariablesKPExtension.CUSP2));

        //CUSP 3
        bean[2].setCusp(GlobalVariablesKPExtension.CUSP3);
        bean[2].setKpCILType1(kpr.getKP_CIL_Type1(GlobalVariablesKPExtension.CUSP3));
        bean[2].setKpCILType2(kpr.getKP_CIL_Type2(GlobalVariablesKPExtension.CUSP3));
        bean[2].setKpCILType3(kpr.getKP_CIL_Type3(GlobalVariablesKPExtension.CUSP3));
        bean[2].setKpCILType4(kpr.getKP_CIL_Type4(GlobalVariablesKPExtension.CUSP3));

        //CUSP 4
        bean[3].setCusp(GlobalVariablesKPExtension.CUSP4);
        bean[3].setKpCILType1(kpr.getKP_CIL_Type1(GlobalVariablesKPExtension.CUSP4));
        bean[3].setKpCILType2(kpr.getKP_CIL_Type2(GlobalVariablesKPExtension.CUSP4));
        bean[3].setKpCILType3(kpr.getKP_CIL_Type3(GlobalVariablesKPExtension.CUSP4));
        bean[3].setKpCILType4(kpr.getKP_CIL_Type4(GlobalVariablesKPExtension.CUSP4));

        //CUSP 5
        bean[4].setCusp(GlobalVariablesKPExtension.CUSP5);
        bean[4].setKpCILType1(kpr.getKP_CIL_Type1(GlobalVariablesKPExtension.CUSP5));
        bean[4].setKpCILType2(kpr.getKP_CIL_Type2(GlobalVariablesKPExtension.CUSP5));
        bean[4].setKpCILType3(kpr.getKP_CIL_Type3(GlobalVariablesKPExtension.CUSP5));
        bean[4].setKpCILType4(kpr.getKP_CIL_Type4(GlobalVariablesKPExtension.CUSP5));

        //CUSP 6
        bean[5].setCusp(GlobalVariablesKPExtension.CUSP6);
        bean[5].setKpCILType1(kpr.getKP_CIL_Type1(GlobalVariablesKPExtension.CUSP6));
        bean[5].setKpCILType2(kpr.getKP_CIL_Type2(GlobalVariablesKPExtension.CUSP6));
        bean[5].setKpCILType3(kpr.getKP_CIL_Type3(GlobalVariablesKPExtension.CUSP6));
        bean[5].setKpCILType4(kpr.getKP_CIL_Type4(GlobalVariablesKPExtension.CUSP6));

        //CUSP 7
        bean[6].setCusp(GlobalVariablesKPExtension.CUSP7);
        bean[6].setKpCILType1(kpr.getKP_CIL_Type1(GlobalVariablesKPExtension.CUSP7));
        bean[6].setKpCILType2(kpr.getKP_CIL_Type2(GlobalVariablesKPExtension.CUSP7));
        bean[6].setKpCILType3(kpr.getKP_CIL_Type3(GlobalVariablesKPExtension.CUSP7));
        bean[6].setKpCILType4(kpr.getKP_CIL_Type4(GlobalVariablesKPExtension.CUSP7));

        //CUSP 8
        bean[7].setCusp(GlobalVariablesKPExtension.CUSP8);
        bean[7].setKpCILType1(kpr.getKP_CIL_Type1(GlobalVariablesKPExtension.CUSP8));
        bean[7].setKpCILType2(kpr.getKP_CIL_Type2(GlobalVariablesKPExtension.CUSP8));
        bean[7].setKpCILType3(kpr.getKP_CIL_Type3(GlobalVariablesKPExtension.CUSP8));
        bean[7].setKpCILType4(kpr.getKP_CIL_Type4(GlobalVariablesKPExtension.CUSP8));

        //CUSP 9
        bean[8].setCusp(GlobalVariablesKPExtension.CUSP9);
        bean[8].setKpCILType1(kpr.getKP_CIL_Type1(GlobalVariablesKPExtension.CUSP9));
        bean[8].setKpCILType2(kpr.getKP_CIL_Type2(GlobalVariablesKPExtension.CUSP9));
        bean[8].setKpCILType3(kpr.getKP_CIL_Type3(GlobalVariablesKPExtension.CUSP9));
        bean[8].setKpCILType4(kpr.getKP_CIL_Type4(GlobalVariablesKPExtension.CUSP9));

        //CUSP 10
        bean[9].setCusp(GlobalVariablesKPExtension.CUSP10);
        bean[9].setKpCILType1(kpr.getKP_CIL_Type1(GlobalVariablesKPExtension.CUSP10));
        bean[9].setKpCILType2(kpr.getKP_CIL_Type2(GlobalVariablesKPExtension.CUSP10));
        bean[9].setKpCILType3(kpr.getKP_CIL_Type3(GlobalVariablesKPExtension.CUSP10));
        bean[9].setKpCILType4(kpr.getKP_CIL_Type4(GlobalVariablesKPExtension.CUSP10));

        //CUSP 11
        bean[10].setCusp(GlobalVariablesKPExtension.CUSP11);
        bean[10].setKpCILType1(kpr.getKP_CIL_Type1(GlobalVariablesKPExtension.CUSP11));
        bean[10].setKpCILType2(kpr.getKP_CIL_Type2(GlobalVariablesKPExtension.CUSP11));
        bean[10].setKpCILType3(kpr.getKP_CIL_Type3(GlobalVariablesKPExtension.CUSP11));
        bean[10].setKpCILType4(kpr.getKP_CIL_Type4(GlobalVariablesKPExtension.CUSP11));

        //CUSP 12

        bean[11].setCusp(GlobalVariablesKPExtension.CUSP12);
        bean[11].setKpCILType1(kpr.getKP_CIL_Type1(GlobalVariablesKPExtension.CUSP12));
        bean[11].setKpCILType2(kpr.getKP_CIL_Type2(GlobalVariablesKPExtension.CUSP12));
        bean[11].setKpCILType3(kpr.getKP_CIL_Type3(GlobalVariablesKPExtension.CUSP12));
        bean[11].setKpCILType4(kpr.getKP_CIL_Type4(GlobalVariablesKPExtension.CUSP12));


        return bean;

    }

    /**
     * This function return filled object of
     * OutKpRulingPlanets to show on KP Ruling Planets on UI
     * according to passed InKPPlanetsAndCusp
     * object values .
     *
     * @param obj
     * @return OutKpRulingPlanets
     * @throws Exception
     */
    public OutKpRulingPlanets getKpRulingPlanets(InKPPlanetsAndCusp obj, String[] RasiLordFullName, String[] NakLordFullName) throws Exception {
        OutKpRulingPlanets _OutKpRulingPlanets = new OutKpRulingPlanets();

        try {
            _OutKpRulingPlanets.setBirthDayLord(obj.getDayLord());


            _OutKpRulingPlanets.setAscSignLord(RasiLordFullName[CUtils.getRasiNumber(obj.getAscedent())]);
            _OutKpRulingPlanets.setAscNakLord(NakLordFullName[CUtils.getNakshatraNumber(obj.getAscedent())]);
            _OutKpRulingPlanets.setAscSubLord(NakLordFullName[CUtils.getSubLordNumber(obj.getAscedent())]);
            _OutKpRulingPlanets.setMoonSignLord(RasiLordFullName[CUtils.getRasiNumber(obj.getMoon())]);
            _OutKpRulingPlanets.setMoonNakLord(NakLordFullName[CUtils.getNakshatraNumber(obj.getMoon())]);
            _OutKpRulingPlanets.setMoonSubLord(NakLordFullName[CUtils.getSubLordNumber(obj.getMoon())]);
            _OutKpRulingPlanets.setFortunaRasit(RasiLordFullName[CUtils.getRasiNumber(obj.getKpFortuna())]);
            _OutKpRulingPlanets.setFortunaDeg(obj.getKpFortuna());
            _OutKpRulingPlanets.setFortunaSubSub(NakLordFullName[CUtils.getSubLordNumber(obj.getKpFortuna())]);
            _OutKpRulingPlanets.setKpAyan(obj.getKpAyan());

        } catch (Exception e) {
            throw e;
        }
        return _OutKpRulingPlanets;

    }

    /**
     * This function return Kp planet degree array
     *
     * @param obj
     * @return double[]
     * @throws Exception
     */

    public double[] getKPPlanetsDegreeArray(InKPPlanetsAndCusp obj) throws Exception {
        double[] _planetDegree = new double[13];

        _planetDegree[0] = obj.getSun();
        _planetDegree[1] = obj.getMoon();
        _planetDegree[2] = obj.getMarsh();
        _planetDegree[3] = obj.getMercury();
        _planetDegree[4] = obj.getJup();
        _planetDegree[5] = obj.getVenus();
        _planetDegree[6] = obj.getSat();
        _planetDegree[7] = obj.getRahu();
        _planetDegree[8] = obj.getKetu();
        _planetDegree[9] = obj.getUranus();
        _planetDegree[10] = obj.getNeptune();
        _planetDegree[11] = obj.getPluto();
        _planetDegree[12] = obj.getLagna();


        return _planetDegree;
    }

    /**
     * This function is used to return planets rashi array for KP lagna chart
     *
     * @param obj
     * @return int []
     * @throws Exception
     */

    public int[] getKPLagnaRashiKundliPlanetsRashiArray(InKPPlanetsAndCusp obj)
            throws Exception {
        // TODO Auto-generated method stub
        int[] planestRashi = new int[13];

        planestRashi[0] = (int) (obj.getSun() / 30.00) + 1;
        planestRashi[1] = (int) (obj.getMoon() / 30.00) + 1;
        planestRashi[2] = (int) (obj.getMarsh() / 30.00) + 1;
        planestRashi[3] = (int) (obj.getMercury() / 30.00) + 1;
        planestRashi[4] = (int) (obj.getJup() / 30.00) + 1;
        planestRashi[5] = (int) (obj.getVenus() / 30.00) + 1;
        planestRashi[6] = (int) (obj.getSat() / 30.00) + 1;
        planestRashi[7] = (int) (obj.getRahu() / 30.00) + 1;
        planestRashi[8] = (int) (obj.getKetu() / 30.00) + 1;
        planestRashi[9] = (int) (obj.getUranus() / 30.00) + 1;
        planestRashi[10] = (int) (obj.getNeptune() / 30.00) + 1;
        planestRashi[11] = (int) (obj.getPluto() / 30.00) + 1;
        planestRashi[12] = (int) (obj.getLagna() / 30.00) + 1;


        return planestRashi;
    }


}
