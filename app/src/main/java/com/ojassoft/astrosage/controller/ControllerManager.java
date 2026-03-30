package com.ojassoft.astrosage.controller;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ojassoft.astrosage.beans.BeanDasa;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.BeanKpCIL;
import com.ojassoft.astrosage.beans.BeanKpKhullarCIL;
import com.ojassoft.astrosage.beans.BeanKpPlanetSigWithStrenght;
import com.ojassoft.astrosage.beans.BeanNakshtraNadi;
import com.ojassoft.astrosage.beans.BeanOutMatchmakingNorth;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.beans.InKPPlanetsAndCusp;
import com.ojassoft.astrosage.beans.InKpPlanetSignification;
import com.ojassoft.astrosage.beans.KundliPlanetArray;
import com.ojassoft.astrosage.beans.OutBirthDetail;
import com.ojassoft.astrosage.beans.OutKpHouseSignificators;
import com.ojassoft.astrosage.beans.OutKpMisc;
import com.ojassoft.astrosage.beans.OutKpRulingPlanets;
import com.ojassoft.astrosage.beans.PlanetData;
import com.ojassoft.astrosage.customexceptions.NoInternetException;
import com.ojassoft.astrosage.customexceptions.UICOnlineChartOperationException;
import com.ojassoft.astrosage.customexceptions.UICOnlineNorthMatchMakingOperationException;
import com.ojassoft.astrosage.customexceptions.UICTajikVarshaphalOperationException;
import com.ojassoft.astrosage.customexceptions.UIDasaVimsottriException;
import com.ojassoft.astrosage.customexceptions.UIDataOperationException;
import com.ojassoft.astrosage.customexceptions.UIKpSystemMiscException;
import com.ojassoft.astrosage.customexceptions.UIKundliMiscCalculationException;
import com.ojassoft.astrosage.jinterface.IControllerManager;
import com.ojassoft.astrosage.misc.CalculateKundli;
import com.ojassoft.astrosage.misc.OnSuccessKundliCalculation;
import com.ojassoft.astrosage.model.CDatabaseHelper;
import com.ojassoft.astrosage.model.ModelManager;
import com.ojassoft.astrosage.ui.fragments.TransitFragment;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CMisc;
import com.ojassoft.kpextension.BeanKP4Step;

public class ControllerManager implements IControllerManager {
    Context context;
    CalculateKundli calculateKundli;
    OnSuccessKundliCalculation onSuccessKundliCalculation;
    TransitFragment transitFragment;

    public ControllerManager() {

    }

    public ControllerManager(TransitFragment transitFragment, Context context) {
        this.transitFragment = transitFragment;
        this.context = context;

    }

    public ControllerManager(CalculateKundli calculateKundli, Context context) {
        this.context = context;
        this.calculateKundli = calculateKundli;
    }

    public ControllerManager(OnSuccessKundliCalculation calculateKundli, Context context) {
        this.context = context;
        this.onSuccessKundliCalculation = calculateKundli;
    }

    @Override
    public void calculateKundliData(BeanHoroPersonalInfo obj, boolean isOnline, boolean internetStatus) throws UICOnlineChartOperationException, NoInternetException {
        try {
            if(onSuccessKundliCalculation != null) {
                new ModelManager().calculateKundliData(onSuccessKundliCalculation, context, obj, isOnline, internetStatus);
            }else{
                new ModelManager().calculateKundliData(calculateKundli, context, obj, isOnline, internetStatus);
            }

        } catch (NoInternetException e) {
            throw new NoInternetException(CGlobalVariables.INTERNET_IS_NOT_WORKING);
        } catch (Exception e) {
            throw new UICOnlineChartOperationException(e.getMessage());
        }

    }


    /**
     * This function return Kp Cuspal Positions used to show on screen.
     *
     * @param obj
     * @return String[][]
     * @throws UIKpSystemMiscException
     */

    public String[][] getKpCuspalPositions(InKPPlanetsAndCusp obj, String[] RasiLord, String[] NakLord, String DegSign, String MinSign, String SecSign)
            throws UIKpSystemMiscException {
        // TODO Auto-generated method stub
        try {
            return (new CKpSystemMisc().getKpCuspalPositions(obj, RasiLord, NakLord, DegSign, MinSign, SecSign));
        } catch (Exception e) {
            /*Log.d(" KpSystemMisc", "Unable to calculate Kp Cuspal Positions.");*/
            throw (new UIKpSystemMiscException(
                    "Unable to calculate Kp Cuspal Positions.", e));

        }
    }

    /**
     * This function return Kp Planetary Positions used to show on screen.
     *
     * @param obj
     * @return String[][]
     * @throws UIKpSystemMiscException
     */
    public String[][] getKpPlanetaryPositions(InKPPlanetsAndCusp obj, String[] RasiLord, String[] NakLord, String DegSign, String MinSign, String SecSign)
            throws UIKpSystemMiscException {
        // TODO Auto-generated method stub
        try {
            return (new CKpSystemMisc().getKpPlanetaryPositions(obj, RasiLord, NakLord, DegSign, MinSign, SecSign));
        } catch (Exception e) {
            /*Log.d(" KpSystemMisc",
                    "Unable to calculate Kp Planetary Positions.");*/
            throw (new UIKpSystemMiscException(
                    "Unable to calculate Kp Planetary Positions.", e));

        }
    }


    /**
     * This function put  KpPlanetSignificators into  array
     * for further operations.
     *
     * @param obj
     * @return String[]
     * @throws UIKpSystemMiscException
     */
    @Override
    public String[] getKpPlanetSignificators(InKpPlanetSignification obj)
            throws UIKpSystemMiscException {
        try {
            return (new CKpSystemMisc().getKpPlanetSignificators(obj));
        } catch (Exception e) {
            /*Log.d(" KpSystemMisc",
                    "Unable to calculate Kp Planet Significators.");*/
            throw (new UIKpSystemMiscException(
                    "Unable to calculate Kp Planet Significators.", e));

        }
    }


    /**
     * This function return Kp House Significators used to show on screen.
     *
     * @param obj
     * @return OutKpHouseSignificators
     * @throws UIKpSystemMiscException
     */

    public OutKpHouseSignificators getKpHouseSignificators(
            InKpPlanetSignification obj) throws UIKpSystemMiscException {
        try {
            return (new CKpSystemMisc().getKpHouseSignificators(obj));
        } catch (Exception e) {
            throw (new UIKpSystemMiscException(
                    "Unable to calculate Kp House Significators.", e));

        }
    }


    public BeanKpPlanetSigWithStrenght[] getPlanetSignWithStrengthBeansArray(
            InKPPlanetsAndCusp plntAndCusp) throws UIKpSystemMiscException {
        try {
            return new CKpSystemMisc()
                    .getPlanetSignWithStrengthBeansArray(plntAndCusp);
        } catch (Exception e) {
            throw (new UIKpSystemMiscException(
                    "Unable to calculate Kp planet strength.", e));
        }
    }


    /**
     * This function return Kp Misc information used to show on screen.
     *
     * @param obj
     * @return OutKpMisc
     * @throws UIKpSystemMiscException
     */

    public OutKpMisc getKpMisc(InKPPlanetsAndCusp obj, String[] RasiLordName, String[] NakLordName)
            throws UIKpSystemMiscException {
        // TODO Auto-generated method stub
        try {
            return (new CKpSystemMisc().getKpMisc(obj, RasiLordName, NakLordName));
        } catch (Exception e) {
            //Log.d(" KpSystemMisc", "Unable to calculate Kp miscleneous values.");
            throw (new UIKpSystemMiscException(
                    "Unable to calculate Kp miscleneous values.", e));

        }
        // return null;
    }

    public BeanNakshtraNadi[] getNakshtraNadi(InKPPlanetsAndCusp plntAndCusp)
            throws UIKpSystemMiscException {
        try {
            return new CKpSystemMisc().getNakshtraNadi(plntAndCusp);
        } catch (Exception e) {
            //Log.d("CKpNakshtraNadi", "Unable to calculate Nakshtra Nadi.");
            throw (new UIKpSystemMiscException(
                    "Unable to calculate Nakshtra Nadi.", e));

        }
    }

    /**
     * This function is used to return KCIL bean after array calculation
     *
     * @param inKPPlanetsAndCuspObject
     * @return array of BeanKpKCIL
     */

    public BeanKpKhullarCIL[] getKCILBeansArray(
            InKPPlanetsAndCusp inKPPlanetsAndCuspObject)
            throws UIKpSystemMiscException {
        try {
            return new CKpSystemMisc()
                    .getKCILBeansArray(inKPPlanetsAndCuspObject);
        } catch (Exception e) {
            //Log.d("CKpSystemMisc.java", "Unable to calculate KCIL.");
            throw (new UIKpSystemMiscException("Unable to calculate KCIL.", e));
        }
    }

    public BeanKP4Step[] getBeanKp4StepBeansArrayNew(
            InKPPlanetsAndCusp inKPPlanetsAndCuspObject)
            throws UIKpSystemMiscException {
        try {
            return new CKpSystemMisc()
                    .getBeanKp4StepBeansArrayNew(inKPPlanetsAndCuspObject);
        } catch (Exception e) {
            //Log.d("CKpSystemMisc.java", "Unable to calculate Kp 4 Step.");
            throw (new UIKpSystemMiscException(
                    "Unable to calculate Kp 4 Step.", e));
        }
    }

    /**
     * This function return the array of KP cuspal interlink based on cusp
     *
     * @param inKPPlanetsAndCuspObject
     * @return array of BeanKpCIL
     */
    public BeanKpCIL[] getKPCILBeansArray(
            InKPPlanetsAndCusp inKPPlanetsAndCuspObject)
            throws UIKpSystemMiscException {
        try {
            return new CKpSystemMisc()
                    .getKPCILBeansArray(inKPPlanetsAndCuspObject);
        } catch (Exception e) {
            /*Log.d("CKpSystemMisc.java",
                    "Unable to calculate Kp cuspal interlink(CIL).");*/
            throw (new UIKpSystemMiscException(
                    "Unable to calculate Kp cuspal interlink(CIL).", e));
        }
    }

    /**
     * This function return ruling planets for KP system.
     *
     * @param obj
     * @return OutKpRulingPlanets
     * @throws UIKpSystemMiscException
     */

    public OutKpRulingPlanets getKpRulingPlanets(InKPPlanetsAndCusp obj, String[] RasiLordFullName, String[] NakLordFullName)
            throws UIKpSystemMiscException {
        // TODO Auto-generated method stub
        try {
            return (new CKpSystemMisc().getKpRulingPlanets(obj, RasiLordFullName, NakLordFullName));
        } catch (Exception e) {
            //Log.d(" KpSystemMisc", "Unable to calculate Kp Ruling Planets.");
            throw (new UIKpSystemMiscException(
                    "Unable to calculate Kp Ruling Planets.", e));

        }

        // return null;
    }

    /**
     * This function return Kp planet degree array
     *
     * @param obj
     * @return double[]
     * @throws UIKpSystemMiscException
     */

    public double[] getKPPlanetsDegreeArray(InKPPlanetsAndCusp obj)
            throws UIKpSystemMiscException {
        // TODO Auto-generated method stub
        try {
            return (new CKpSystemMisc().getKPPlanetsDegreeArray(obj));
        } catch (Exception e) {
            /*Log.d("CKundliMiscCalculation",
                    "Unable to calculate KP Planets Degree  .");*/
            throw (new UIKpSystemMiscException(
                    "Unable to calculate KP Planets Degree .", e));

        }
    }


    /**
     * This function return kp cusp array
     *
     * @param plntAndCusp
     * @return double[]
     */
    public double[] getKpCuspArray(InKPPlanetsAndCusp plntAndCusp)
            throws UIKpSystemMiscException {
        // TODO Auto-generated method stub
        try {
            return (new CKpSystemMisc().getKpCuspArray(plntAndCusp));
        } catch (Exception e) {
                    /*Log.d("CKundliMiscCalculation",
                            "Unable to calculate KP Planets Degree  .");*/
            throw (new UIKpSystemMiscException(
                    "Unable to calculate KP Planets Degree .", e));

        }
    }

    /**
     * This function calculate KP Rashi kundli and return KundliPlanetArray
     * object.
     *
     * @return KundliPlanetArray
     * @throws UIKpSystemMiscException
     */

    public int[] getKPLagnaRashiKundliPlanetsRashiArray(InKPPlanetsAndCusp obj)
            throws UIKpSystemMiscException {
        // TODO Auto-generated method stub
        try {
            return (new CKpSystemMisc()
                    .getKPLagnaRashiKundliPlanetsRashiArray(obj));
        } catch (Exception e) {
            //Log.d("CKpSystemMisc", "Unable to calculate Kp Lagna chart.");
            throw (new UIKpSystemMiscException(
                    "Unable to calculate Kp Lagna chart.", e));

        }

    }


    public int[] getLagnaKundliPlanetsRashiArray(PlanetData obj)
            throws Exception {
        try {
            return (new CKundliMiscCalculation()
                    .getLagnaKundliPlanetsRashiArray(obj));
        } catch (Exception e) {
            throw (new Exception(
                    "Unable to calculate Lagna chart.", e));

        }
    }

    /**
     * This function return planet degree array
     *
     * @param obj
     * @return double[]
     * @throws UIKundliMiscCalculationException
     */

    public double[] getPlanetsDegreeArray(PlanetData obj)
            throws Exception {
        try {
            return (new CKundliMiscCalculation().getPlanetsDegreeArray(obj));
        } catch (Exception e) {
            throw (new Exception(
                    "Unable to calculate  Planets Degree .", e));

        }
    }

    public int[] getPlanetsInRashiFromShodashvarga(String value)
            throws Exception {
        try {
            return (new CKundliMiscCalculation()
                    .getPlanetsInRashiFromShodashvarga(value));
        } catch (Exception e) {
            throw (new Exception(
                    "Unable to calculate Shodashvarga chart.", e));

        }
    }

    public int[] getChandraKundliPlanetsRashiArray(PlanetData obj)
            throws Exception {
        try {
            return (new CKundliMiscCalculation().getChandraKundliPlanetsRashiArray(obj));
        } catch (Exception e) {
            throw (new Exception("Unable to calculate Moon chart.", e));
        }
    }


    /**
     * This function return vimsottri Maha Dasha used to show on screen.
     *
     * @param _dob
     * @param _lastEndDate
     * @param obj
     * @return BeanDasa[]
     * @throws UIDasaVimsottriException
     */
    public BeanDasa[] vimsottriMahaDasha(double _dob, double _lastEndDate,
                                         PlanetData obj) throws UIDasaVimsottriException {
        // TODO Auto-generated method stub
        try {
            return (new CDasaVimsottri(_dob, _dob).firstLevel(obj));
        } catch (Exception e) {
                    /*Log.d("DasaVimsottri-Calculation",
                            "Mahadasha could not be calculated.");*/
            throw (new UIDasaVimsottriException(
                    "Mahadasha could not be calculated.", e));

        }
    }


    /**
     * This function return vimsottri Other Dasha Level used to show on screen.
     *
     * @param _dob
     * @param _lastEndDate
     * @param iPos
     * @param objArrPrDasa
     * @return BeanDasa[]
     */

    public BeanDasa[] vimOtherLevelDasha(double _dob, double _lastEndDate,
                                         int iPos, BeanDasa[] objArrPrDasa) throws UIDasaVimsottriException {
        try {
            return (new CDasaVimsottri(_dob, _lastEndDate).vimOtherLevelDasha(
                    iPos, objArrPrDasa));
        } catch (Exception e) {
            //Log.d("DasaVimsottri-Calculation", "Dasha could not be calculated.");
            throw (new UIDasaVimsottriException(
                    "Dasha could not be calculated.", e));

        }
    }

    /**
     * This is other new function return vimsottri Other Dasha Level used to
     * show on screen.
     *
     * @param _dob
     * @param _lastEndDate
     * @param iPos
     * @param objArrPrDasa
     * @param _lastdashatime
     * @return BeanDasa[]
     * @throws UIDasaVimsottriException
     */

    public BeanDasa[] getNewVimOtherLevelDasa(double _dob, double _lastEndDate,
                                              int iPos, BeanDasa[] objArrPrDasa, double _lastdashatime)
            throws UIDasaVimsottriException {
        // TODO Auto-generated method stub
        try {
            return (new CDasaVimsottri(_dob, _lastEndDate)
                    .getNewVimOtherLevelDasa(iPos, objArrPrDasa, _lastdashatime));
        } catch (Exception e) {
            //Log.d("DasaVimsottri-Calculation", "Dasha could not be calculated.");
            throw (new UIDasaVimsottriException(
                    "Dasha could not be calculated.", e));

        }
    }

    public double[] getCuspsDegreeArray(InKPPlanetsAndCusp cups, PlanetData obj)
            throws Exception {
        try {
            return new CKundliMiscCalculation().getCuspsDegreeArray(cups, obj);
        } catch (Exception e) {
            throw (new Exception("Unable to calculate  cusps Degree .", e));
        }
    }

    public double[] getCuspsMidDegreeArrayForChalit(InKPPlanetsAndCusp cups,
                                                    PlanetData obj) throws Exception {
        try {
            return new CKundliMiscCalculation().getCuspsMidDegreeArrayForChalit(cups, obj);
        } catch (Exception e) {
            throw (new Exception("Unable to calculate  cusps mid  Degree .", e));
        }
    }

    /**
     * This function return filled KundliPlanetArray object to show lagna kundli
     * on Screen .
     *
     * @param obj
     * @return KundliPlanetArray
     * @throws UIKundliMiscCalculationException
     */

    public KundliPlanetArray getLagnaKundli(PlanetData obj)
            throws Exception {
        try {
            return (new CKundliMiscCalculation().getLagnaKundli(obj));
        } catch (Exception e) {
            throw (new Exception("Unable to calculate Lagna chart.", e));

        }
    }

    /**
     * This function is used to return Manglik points for kundli
     *
     * @param obj
     * @return int []
     * @throws UIKundliMiscCalculationException
     */

    public int returnManglikPonits(PlanetData obj)
            throws Exception {
        try {
            return (new CKundliMiscCalculation().returnManglikPonits(obj));
        } catch (Exception e) {
            throw (new Exception(
                    "Unable to calculate Mangldosh .", e));

        }
    }

    /**
     * This function return Birth Detail used to show on screen.
     *
     * @param obj
     * @return OutBirthDetail
     * @throws UIKundliMiscCalculationException
     */

    public OutBirthDetail getBirthDetail(BeanHoroPersonalInfo obj)
            throws Exception {
        try {
            return (new CMisc().getBirthDetail(obj));
        } catch (Exception e) {
            //Log.e("KpSystemMisc", e.getMessage());
            throw (new Exception("Unable to calculate Mangal Dosh.", e));

        }
    }

    /**
     * This function is used to return planet for which the dasa is remaine
     *
     * @param degMoon
     * @return planet number
     * @throws UIDasaVimsottriException
     */
    public int getPlanetForBalanceOfDasa(double degMoon)
            throws UIDasaVimsottriException {
        try {
            return (new CDasaVimsottri().getPlanetForBalanceOfDasa(degMoon));
        } catch (Exception e) {
            throw (new UIDasaVimsottriException("Unable to planet  for  balance of dasa.", e));
        }
    }

    /**
     * This function is used to get balance of dasha
     *
     * @param degMoon
     * @return double
     * @throws UIDasaVimsottriException
     */
    public double getBalanceOfDasa(double degMoon)
            throws UIDasaVimsottriException {
        try {
            return (new CDasaVimsottri().getBalanceOfDasa(degMoon));
        } catch (Exception e) {
            throw (new UIDasaVimsottriException("Unable to calculate balance of dasa.", e));

        }
    }
    /*
     * This function is used to return lalkitab kundli planets array
     * @param obj
     * @param yearNo
     * @return int []
     * @throws UIKundliMiscCalculationException
     */

    public int[] getLalKitabKundliPlanetsRashiArray(PlanetData obj, int yearNo)
            throws UIKundliMiscCalculationException {
        // TODO Auto-generated method stub
        try {
            return (new CKundliMiscCalculation()
                    .getLalKitabKundliPlanetsRashiArray(obj, yearNo));
        } catch (Exception e) {
        /*	Log.d("CKundliMiscCalculation",
                    "Unable to calculate Lalkitab Kundli .");*/
            throw (new UIKundliMiscCalculationException(
                    "Unable to calculate Lalkitab Kundli .", e));

        }
    }


    /**
     * This function is sued to create or init database
     *
     * @param context
     * @return SQLiteDatabase
     * @throws Exception
     */
    public CDatabaseHelper getDatabaseHelperObject(Context context)
            throws UIDataOperationException {
        try {
            return (new ModelManager().getDatabaseHelperObject(context));
        } catch (Exception e) {
            throw (new UIDataOperationException(
                    "Unable to create database .", e));
        }

    }

    /**
     * This function return the array of city id and name
     *
     * @param sqLiteDatabase
     * @param cityName
     * @return String[][]
     * @throws UIDataOperationException
     * @author Bijendra 31-may-13
     */
    @Override
    public String[][] searchCity(SQLiteDatabase sqLiteDatabase, String cityName)
            throws UIDataOperationException {
        try {

            return new ModelManager().searchCity(sqLiteDatabase, cityName);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw (new UIDataOperationException("City  not found", e));
        }
    }

    /**
     * This function is used to fatch city detail based on city id
     *
     * @param sqLiteDatabase
     * @param cityId
     * @return BeanPlace
     * @throws UIDataOperationException
     */
    @Override
    public BeanPlace getCityById(SQLiteDatabase sqLiteDatabase, int cityId)
            throws UIDataOperationException {
        try {
            return new ModelManager().getCityById(sqLiteDatabase, cityId);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw (new UIDataOperationException("City not found", e));
        }
    }

    /**
     * This function return Place object filled with time zone values(name/value/id)
     * according to passed time zone id that contain in time zone table
     *
     * @return BeanPlace
     * @throws UIDataOperationException
     */
    @Override
    public BeanPlace getTimZoneId(SQLiteDatabase sqLiteDatabase, int getTimZoneId)
            throws UIDataOperationException {
        try {
            return new ModelManager().getTimZoneId(sqLiteDatabase, getTimZoneId);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw (new UIDataOperationException("Time zone not found", e));
        }
    }

    /**
     * This function return array of time zones from database
     * according to passed time zone name
     *
     * @param sqLiteDatabase
     * @return String array contain time zone id and name
     * @throws UIDataOperationException
     */
    @Override
    public String[][] searchTimeZone(SQLiteDatabase sqLiteDatabase,
                                     String searchText) throws UIDataOperationException {
        try {
            return (new ModelManager().searchTimeZone(sqLiteDatabase, searchText));
        } catch (Exception e) {
            //Log.d("Database-operations", "Time Zone list not found");
            throw (new UIDataOperationException("Time Zone list not found", e));

        }
    }


    /**
     * This function expose the functionality to verify login .
     *
     * @return int(status)
     * @throws UICOnlineChartOperationException
     */

	/*public int verifyLogin(String _uid, String _pwd)
            throws UICOnlineChartOperationException {
		// TODO Auto-generated method stub
		try {
			return (new ModelManager().verifyLogin(_uid, _pwd));
		} catch (Exception e) {
			Log.d("OnlineChartOperation",
					"Could not verify login from  server.");
			throw (new UICOnlineChartOperationException(
					"Could not verify login from  server.", e));
		}
	}*/
    @Override
    public ArrayList<BeanHoroPersonalInfo> getOnlineChartList(Context context, String kundliName, String uid, String pwd, String key, String isapi) throws UICOnlineChartOperationException {
        try {
            return (new ModelManager().getOnlineChartList(context, kundliName, uid, pwd, key, isapi));
        } catch (Exception e) {
            throw (new UICOnlineChartOperationException(
                    "Could not get kundli list from  server.", e));
        }
    }


   /* @Override
    public int deleteOnlineKundli(long kundliId, String uid, String pwd)
            throws UICOnlineChartOperationException {
        try {
            return (new ModelManager().deleteOnlineKundli(kundliId, uid, pwd));
        } catch (Exception e) {
            //Log.d("OnlineChartOperation", e.getMessage());
            throw (new UICOnlineChartOperationException(
                    "Could not delete kundli on server.", e));
        }
    }*/


   /* @Override
    public BeanHoroPersonalInfo getOnlineKundliDetail(SQLiteDatabase sqLiteDatabase, long _kundliId, String uid,
                                                      String pwd) throws UICOnlineChartOperationException {
        try {
            return (new ModelManager().getOnlineKundliDetail(sqLiteDatabase, _kundliId, uid, pwd));
        } catch (Exception e) {
            //Log.d("OnlineChartOperation", e.getMessage());
            throw (new UICOnlineChartOperationException(
                    "Could not get kundli detail from  server.", e));
        }
    }*/

    /**
     * This function return array of charts with in database according to
     * passed kundli name.
     *
     * @param sqLiteDatabase
     * @param kundliName
     * @param genderType
     * @return 2D  chart array contain chart id and name
     * @throws UIDataOperationException
     */
    @Override
    public Map<String, String> searchLocalKundliList(SQLiteDatabase sqLiteDatabase,
                                                     String kundliName, int genderType, int noOfRecords) throws UIDataOperationException {
        try {
            return new ModelManager().searchLocalKundliList(sqLiteDatabase, kundliName, genderType, noOfRecords);
        } catch (Exception e) {
            //Log.d("OnlineChartOperation", e.getMessage());
            throw (new UIDataOperationException(
                    "Could not get kundli list.", e));
        }

    }

    /**
     * This function is used to delete local kundli
     *
     * @param sqLiteDatabase
     * @param kundliId
     * @return boolean
     * @throws UIDataOperationException
     */
    @Override
    public boolean deleteLocalKundli(SQLiteDatabase sqLiteDatabase, long kundliId)
            throws UIDataOperationException {
        try {
            return new ModelManager().deleteLocalKundli(sqLiteDatabase, kundliId);
        } catch (Exception e) {
            //Log.d("OnlineChartOperation", e.getMessage());
            throw (new UIDataOperationException("Could not delete kundli .", e));
        }
    }


    /**
     * This function is used to get local kundli detail
     *
     * @param sqLiteDatabase
     * @param kundliId
     * @return BeanHoroPersonalInfo
     * @throws UIDataOperationException
     */
    @Override
    public BeanHoroPersonalInfo getLocalKundliDetail(
            SQLiteDatabase sqLiteDatabase, long kundliId)
            throws UIDataOperationException {
        // TODO Auto-generated method stub
        try {
            return (new ModelManager().getLocalKundliDetail(sqLiteDatabase, kundliId));
        } catch (Exception e) {
            //Log.d("OnlineChartOperation", e.getMessage());
            throw (new UIDataOperationException(
                    "Could not get kundli detail .", e));
        }
    }


    /*@Override
    public boolean calculateTajikVarshaphal(
            BeanHoroPersonalInfo objBirthDetail, String yearNumber)
            throws UICTajikVarshaphalOperationException {
        try {
            return (new ModelManager().calculateTajikVarshaphal(objBirthDetail, yearNumber));
        } catch (Exception e) {
            //Log.d("OnlineChartOperation", e.getMessage());
            throw (new UICTajikVarshaphalOperationException(
                    "Could not calculate tajik varshphal .", e));
        }
    }*/


    //BIJENDRA 24-12-13

    /**
     * This function is used to add/edit kundli personal details
     *
     * @param sqLiteDatabase
     * @param beanHoroPersonalInfo
     * @return (long)kundli local id
     * @throws UIDataOperationException
     */
    @Override
    public long addEditHoroPersonalInfo(SQLiteDatabase sqLiteDatabase,
                                        BeanHoroPersonalInfo beanHoroPersonalInfo)
            throws UIDataOperationException {
        try {
            return new ModelManager().addEditHoroPersonalInfo(sqLiteDatabase, beanHoroPersonalInfo);
        } catch (Exception e) {
            //Log.d("OnlineChartOperation", e.getMessage());
            throw (new UIDataOperationException(
                    "Could not save kundli personal  details", e));
        }
    }

    /**
     * This function is used to save  kundli detail on AstroSage cloud
     *
     * @param beanHoroPersonalInfo
     * @param userId
     * @param pwd
     * @return int[][online kundli id,message id]
     * @throws UICOnlineChartOperationException
     */

    /*@Override
    public long[] saveChartOnServer(BeanHoroPersonalInfo beanHoroPersonalInfo,
                                    String userId, String pwd) throws UICOnlineChartOperationException {
        try {
            return (new ModelManager().saveChartOnServer(beanHoroPersonalInfo, userId, pwd));
        } catch (Exception e) {
            throw (new UICOnlineChartOperationException("Could not save chart on server.", e));
        }
    }*/


    /*@Override
    public BeanOutMatchmakingNorth getMatchMakingResultNorth(
            BeanHoroPersonalInfo boy, BeanHoroPersonalInfo girl, int languageCode, boolean iS_APP_ONLINE, boolean internetStatus)
            throws UICOnlineNorthMatchMakingOperationException, NoInternetException {
        try {
            return (new ModelManager().getMatchMakingResultNorth(boy, girl, languageCode, iS_APP_ONLINE, internetStatus));
        } catch (NoInternetException e) {
            throw new NoInternetException(CGlobalVariables.INTERNET_IS_NOT_WORKING);
        } catch (Exception e) {
            throw (new UICOnlineNorthMatchMakingOperationException("Server error. Unable to calculate match making calculation.", e));
        }

    }*/

    /**
     * This function share kundli name online
     *
     * @param userId
     * @param pwd
     * @param name
     * @param chartId
     * @return String[]
     * @throws UICOnlineChartOperationException
     */
    /*@Override
    public String[] shareKundliNameOnline(String userId, String pwd,
                                          String name, String chartId)
            throws UICOnlineChartOperationException {
        try {
            return (new ModelManager().shareKundliNameOnline(userId, pwd, name,
                    chartId));

        } catch (Exception ce) {
        *//*	Log.d("COnlineChartOperations",
					"Unable unable to share kundali name Online.");*//*
            throw (new UICOnlineChartOperationException(
                    "Unable unable to share kundali name Online.", ce));

        }
    }*/

    /**
     * This function is used to check the available name to share kundli
     *
     * @return String[]
     * @throws UICOnlineChartOperationException
     */
   /* @Override
    public String[] checkAvailableKundliNameToShare(String kundliName)
            throws UICOnlineChartOperationException {
        try {
            return (new ModelManager()
                    .checkAvailableKundliNameToShare(kundliName));

        } catch (Exception ce) {
		*//*	Log.d("COnlineChartOperations",
					"Unable check available Kundli Name to share.");*//*
            throw (new UICOnlineChartOperationException(
                    "Unable check available Kundli Name to share.", ce));

        }
    }*/
    public boolean deleteOnlineChartIdFromLocalDatabase(SQLiteDatabase sQLiteDatabase, String onlineChartId) {
        return new ModelManager().deleteOnlineChartIdFromLocalDatabase(sQLiteDatabase, onlineChartId);
    }

    /**
     * This function is used to save user  user GCM  registration information on ojas  server
     *
     * @param context
     * @param regid
     * @param languageCode
     * @param loginId      26-Dec-2014
     */

    @Override
    public void saveUserGCMRegistrationInformationOnOjasServer(Context context,
                                                               String regid, int languageCode, String loginId) {
        // TODO Auto-generated method stub

        new ModelManager().saveUserGCMRegistrationInformationOnOjasServer(context,
                regid, languageCode, loginId);

    }

    /**
     * This function is used to save user  user GCM  registration information on ojas  server
     */


   /* @Override
    public String[] verifyLoginWithUserPurchasedPlan(String _uid, String _pwd, String key)
            throws UICOnlineChartOperationException {
        // TODO Auto-generated method stub
        try {
            return (new ModelManager().verifyLoginWithUserPurchasedPlan(_uid, _pwd, key));
        } catch (Exception e) {
            Log.d("OnlineChartOperation",
                    "Could not verify login from  server.");
            throw (new UICOnlineChartOperationException(
                    "Could not verify login from  server.", e));
        }
    }*/
    @Override
    public String[][] searchCityOperation(Context context, String cityName)
            throws UIDataOperationException {
        // TODO Auto-generated method stub
        try {

            return new ModelManager().searchCityOperation(context, cityName);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw (new UIDataOperationException("City  not found", e));
        }
    }


    @Override
    public BeanPlace getCityByIdOperation(Context context, int cityId)
            throws UIDataOperationException {
        // TODO Auto-generated method stub
        try {
            return new ModelManager().getCityByIdOperation(context, cityId);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw (new UIDataOperationException("City not found", e));
        }
    }


    @Override
    public BeanPlace getTimZoneIdOperation(Context context, int timeZoneId)
            throws UIDataOperationException {
        // TODO Auto-generated method stub
        try {
            return new ModelManager().getTimZoneIdOperation(context, timeZoneId);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw (new UIDataOperationException("Time zone not found", e));
        }
    }


    @Override
    public String[][] searchTimeZoneOperation(Context context, String searchText)
            throws UIDataOperationException {
        // TODO Auto-generated method stub
        try {
            return (new ModelManager().searchTimeZoneOperation(context, searchText));
        } catch (Exception e) {
            //Log.d("Database-operations", "Time Zone list not found");
            throw (new UIDataOperationException("Time Zone list not found", e));

        }
    }


    @Override
    public Map<String, String> searchLocalKundliListOperation(Context context,
                                                              String kundliName, int genderType, int noOfRecords)
            throws UIDataOperationException {
        // TODO Auto-generated method stub
        try {
            return new ModelManager().searchLocalKundliListOperation(context, kundliName, genderType, noOfRecords);
        } catch (Exception e) {
            //Log.d("OnlineChartOperation", e.getMessage());
            throw (new UIDataOperationException(
                    "Could not get kundli list.", e));
        }
    }

    @Override
    public ArrayList<BeanHoroPersonalInfo> searchLocalKundliListOperation(Context context, int genderType, int noOfRecords)
            throws UIDataOperationException {
        // TODO Auto-generated method stub
        try {
            return new ModelManager().searchLocalKundliListOperation(context, genderType, noOfRecords);
        } catch (Exception e) {
            //Log.d("OnlineChartOperation", e.getMessage());
            throw (new UIDataOperationException(
                    "Could not get kundli list.", e));
        }
    }


    @Override
    public boolean deleteLocalKundliOperation(Context context, long kundliId)
            throws UIDataOperationException {
        // TODO Auto-generated method stub
        try {
            return new ModelManager().deleteLocalKundliOperation(context, kundliId);
        } catch (Exception e) {
            //Log.d("OnlineChartOperation", e.getMessage());
            throw (new UIDataOperationException("Could not delete kundli .", e));
        }
    }

    @Override
    public boolean deleteLocalKundliOperation(Context context, long kundliId, long onlineChartId)
            throws UIDataOperationException {
        // TODO Auto-generated method stub
        try {
            return new ModelManager().deleteLocalKundliOperation(context, kundliId, onlineChartId);
        } catch (Exception e) {
            //Log.d("OnlineChartOperation", e.getMessage());
            throw (new UIDataOperationException("Could not delete kundli .", e));
        }
    }


    @Override
    public BeanHoroPersonalInfo getLocalKundliDetailOperation(Context context,
                                                              long kundliId) throws UIDataOperationException {
        // TODO Auto-generated method stub
        try {
            return (new ModelManager().getLocalKundliDetailOperation(context, kundliId));
        } catch (Exception e) {
            //Log.d("OnlineChartOperation", e.getMessage());
            throw (new UIDataOperationException(
                    "Could not get kundli detail .", e));
        }
    }


    @Override
    public long addEditHoroPersonalInfoOperation(Context context,
                                                 BeanHoroPersonalInfo beanHoroPersonalInfo)
            throws UIDataOperationException {
        // TODO Auto-generated method stub
        try {
            return new ModelManager().addEditHoroPersonalInfoOperation(context, beanHoroPersonalInfo);
        } catch (Exception e) {
            //Log.d("OnlineChartOperation", e.getMessage());
            throw (new UIDataOperationException(
                    "Could not save kundli personal  details", e));
        }
    }


    @Override
    public boolean deleteOnlineChartIdFromLocalDatabaseOperation(
            Context context, String onlineChartId) {
        // TODO Auto-generated method stub
        return new ModelManager().deleteOnlineChartIdFromLocalDatabaseOperation(context, onlineChartId);
    }


    /*@Override
    public BeanHoroPersonalInfo getOnlineKundliDetailNew(Context context,
                                                         long kundliId, String uid, String pwd)
            throws UICOnlineChartOperationException {
        try {
            return (new ModelManager().getOnlineKundliDetailNew(context, kundliId, uid, pwd));
        } catch (Exception e) {
            //Log.d("OnlineChartOperation", e.getMessage());
            throw (new UICOnlineChartOperationException(
                    "Could not get kundli detail from  server.", e));
        }
    }*/

    /**
     * This function is used to register the user on servers
     *
     * @param _uid,_pwd 18-Jan-2016
     */
    /*@Override
    public String[] userSignUp(String _uid, String _pwd, String key)
            throws UICOnlineChartOperationException {
        // TODO Auto-generated method stub
        try {
            return (new ModelManager().userSignUp(_uid, _pwd, key));
        } catch (Exception e) {
            Log.d("OnlineChartOperation",
                    "Could not verify login from  server.");
            throw (new UICOnlineChartOperationException(
                    "Could not verify login from  server.", e));
        }
    }*/

    /*@Override
    public String[] userSignUp(String _uid, String key) throws UICOnlineChartOperationException {
        try {
            return (new ModelManager().userSignUp(_uid, key));
        } catch (Exception e) {
            Log.d("OnlineChartOperation",
                    "Could not verify login from  server.");
            throw (new UICOnlineChartOperationException(
                    "Could not verify login from  server.", e));
        }
    }*/

    /**
     * this function is used to update default kundli on server
     */

   /* @Override
    public String UpdateDefaultKundliOnServer(String kundaliId, String userId,
                                              String password) {
        return new ModelManager().UpdateDefaultKundliOnServer(kundaliId,
                userId, password);
    }*/

   /* public String changePasswordAndProfile(String emailId, String newPassword, String oldPassword, String userName, String key) {
        return new ModelManager().changePasswordAndProfile(emailId, newPassword, oldPassword, userName, key);
    }*/
    /*@Override
    public String saveNotesOnserver(Context context, String userid, String password, String usercomment, String onlinechartid) {
        return new ModelManager().saveNotesOnserver(context, userid, password, usercomment, onlinechartid);
    }*/
    @Override
    public void calculateTransitKundliData(BeanHoroPersonalInfo obj, boolean isOnline, boolean internetStatus) {
        try {
            new ModelManager().calculateTransitKundliData(transitFragment, context, obj, isOnline, internetStatus);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int[] getTransitKundliPlanetsRashiArray(PlanetData obj)
            throws Exception {
        try {
            return (new CKundliMiscCalculation()
                    .getTransitKundliPlanetsRashiArray(obj));
        } catch (Exception e) {
            throw (new Exception(
                    "Unable to calculate Lagna chart.", e));

        }
    }

    public double[] getTransitPlanetsDegreeArray(PlanetData obj)
            throws Exception {
        try {
            return (new CKundliMiscCalculation().getTransitPlanetsDegreeArray(obj));
        } catch (Exception e) {
            throw (new Exception(
                    "Unable to calculate  Planets Degree .", e));

        }
    }

    public int[] getTransitKundliPlanetsRashiArrayForMoon(PlanetData obj)
            throws Exception {
        try {
            return (new CKundliMiscCalculation()
                    .getTransitKundliPlanetsRashiArrayForMoon(obj));
        } catch (Exception e) {
            throw (new Exception(
                    "Unable to calculate Lagna chart.", e));

        }
    }

    public double[] getTransitPlanetsDegreeArrayForMoon(PlanetData obj)
            throws Exception {
        try {
            return (new CKundliMiscCalculation().getTransitPlanetsDegreeArrayForMoon(obj));
        } catch (Exception e) {
            throw (new Exception(
                    "Unable to calculate  Planets Degree .", e));

        }
    }

    /*@Override
    public String syncChartOnServer(Context context, String userName, String password, String jsonString) {
        return new ModelManager().syncChartOnServer(context, userName, password, jsonString);
    }*/

    @Override
    public long[] getSaveOnlineChartId(String chartSavedId, String updatedOnlineChartId) throws Exception {

        return new ModelManager().getSaveOnlineChartId(chartSavedId, updatedOnlineChartId);
    }
}