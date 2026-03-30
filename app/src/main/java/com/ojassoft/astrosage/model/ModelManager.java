package com.ojassoft.astrosage.model;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.BeanOutMatchmakingNorth;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.beans.InKPPlanetsAndCusp;
import com.ojassoft.astrosage.beans.InKpPlanetSignification;
import com.ojassoft.astrosage.beans.OutPanchang;
import com.ojassoft.astrosage.beans.OutShodashvarga;
import com.ojassoft.astrosage.beans.PlanetData;
import com.ojassoft.astrosage.customexceptions.NoInternetException;
import com.ojassoft.astrosage.jinterface.ICalculationProxy;
import com.ojassoft.astrosage.jinterface.IModelManager;
import com.ojassoft.astrosage.jinterface.matching.IProxyKundliMatchingNorth;
import com.ojassoft.astrosage.misc.CalculateKundli;
import com.ojassoft.astrosage.misc.OnSuccessKundliCalculation;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.ui.fragments.TransitFragment;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.ArrayList;
import java.util.Map;


public class ModelManager implements IModelManager {

    BeanHoroPersonalInfo beanHoroPersonalInfo;
    Context context;

    /**
     * This function is used to calculate kundli data
     *
     * @throws Exception
     */
    public void calculateKundliData(BeanHoroPersonalInfo obj, boolean isOnline, boolean isInternetAvailble) throws Exception {
        ICalculationProxy _ICalculationProxy = null;
        beanHoroPersonalInfo = obj;
        if (isOnline) {
            if (isInternetAvailble) {
                _ICalculationProxy = new COnlineDataCalculation(obj);
            } else {
                throw new NoInternetException(CGlobalVariables.INTERNET_IS_NOT_WORKING);
            }

        } else {
            _ICalculationProxy = new CLocalCalculation(obj);
        }
        _ICalculationProxy.calculate(_ICalculationProxy);
        separateKundliData(_ICalculationProxy);
    }

    public void calculateKundliData(CalculateKundli calculateKundli, Context context, BeanHoroPersonalInfo obj, boolean isOnline, boolean isInternetAvailble) throws Exception {
        ICalculationProxy _ICalculationProxy = null;
        beanHoroPersonalInfo = obj;
        this.context = context;
        if (isOnline) {
            //if (isInternetAvailble) {
            _ICalculationProxy = new COnlineDataCalculation(calculateKundli, context, obj);
           /* } else {
                throw new NoInternetException(CGlobalVariables.INTERNET_IS_NOT_WORKING);
            }*/

        } else {
            _ICalculationProxy = new CLocalCalculation(obj);
        }
        _ICalculationProxy.calculate(_ICalculationProxy);
        // separateKundliData(_ICalculationProxy);
    }
    public void calculateKundliData(OnSuccessKundliCalculation onSuccessKundliCalculation, Context context, BeanHoroPersonalInfo obj, boolean isOnline, boolean isInternetAvailble) throws Exception {
        ICalculationProxy _ICalculationProxy = null;
        beanHoroPersonalInfo = obj;
        this.context = context;
        if (isOnline) {
            //if (isInternetAvailble) {
            _ICalculationProxy = new COnlineDataCalculation(onSuccessKundliCalculation, context, obj);
           /* } else {
                throw new NoInternetException(CGlobalVariables.INTERNET_IS_NOT_WORKING);
            }*/

        } else {
            _ICalculationProxy = new CLocalCalculation(obj);
        }
        _ICalculationProxy.calculate(_ICalculationProxy);
        // separateKundliData(_ICalculationProxy);
    }


    private void separateKundliData(ICalculationProxy _ICalculationProxy) throws Exception {

        InKpPlanetSignification _inKpPlanetSignification = new InKpPlanetSignification();
        InKPPlanetsAndCusp _inKPPlanetsAndCusp = new InKPPlanetsAndCusp();
        PlanetData _planetDegree = new PlanetData();
        OutShodashvarga _outShodashvarga = new OutShodashvarga();
        OutPanchang _outOutPanchang = new OutPanchang();
        _inKPPlanetsAndCusp.setLagna(_ICalculationProxy.getLagna2());
        _planetDegree.setLagna(_ICalculationProxy.getLagna2());

        _inKPPlanetsAndCusp.setSun(_ICalculationProxy.getSun());
        _planetDegree.setSun(_ICalculationProxy.getSun());

        _inKPPlanetsAndCusp.setMoon(_ICalculationProxy.getMoon());
        _planetDegree.setMoon(_ICalculationProxy.getMoon());

        _inKPPlanetsAndCusp.setMarsh(_ICalculationProxy.getMars());
        _planetDegree.setMarsh(_ICalculationProxy.getMars());

        _inKPPlanetsAndCusp.setMercury(_ICalculationProxy.getMercury());
        _planetDegree.setMercury(_ICalculationProxy.getMercury());

        _inKPPlanetsAndCusp.setJup(_ICalculationProxy.getJupitor());
        _planetDegree.setJup(_ICalculationProxy.getJupitor());

        _inKPPlanetsAndCusp.setVenus(_ICalculationProxy.getVenus());
        _planetDegree.setVenus(_ICalculationProxy.getVenus());

        _inKPPlanetsAndCusp.setSat(_ICalculationProxy.getSaturn());
        _planetDegree.setSat(_ICalculationProxy.getSaturn());

        _inKPPlanetsAndCusp.setRahu(_ICalculationProxy.getRahu());
        _planetDegree.setRahu(_ICalculationProxy.getRahu());

        _inKPPlanetsAndCusp.setKetu(_ICalculationProxy.getKetu());
        _planetDegree.setKetu(_ICalculationProxy.getKetu());

        _inKPPlanetsAndCusp.setUranus(_ICalculationProxy.getUranus());
        _planetDegree.setUranus(_ICalculationProxy.getUranus());

        _inKPPlanetsAndCusp.setNeptune(_ICalculationProxy.getNeptune());
        _planetDegree.setNeptune(_ICalculationProxy.getNeptune());

        _inKPPlanetsAndCusp.setPluto(_ICalculationProxy.getPluto());
        _planetDegree.setPluto(_ICalculationProxy.getPluto());

        _inKPPlanetsAndCusp.setAscedent(_ICalculationProxy
                .getKPCuspLongitude(CGlobalVariables.ASCEDENT + 1));

        _inKPPlanetsAndCusp.setKpCusp1(_ICalculationProxy
                .getKPCuspLongitude(CGlobalVariables.KPCUSP1 + 1));

        _inKPPlanetsAndCusp.setKpCusp2(_ICalculationProxy
                .getKPCuspLongitude(CGlobalVariables.KPCUSP2 + 1));

        _inKPPlanetsAndCusp.setKpCusp3(_ICalculationProxy
                .getKPCuspLongitude(CGlobalVariables.KPCUSP3 + 1));

        _inKPPlanetsAndCusp.setKpCusp4(_ICalculationProxy
                .getKPCuspLongitude(CGlobalVariables.KPCUSP4 + 1));

        _inKPPlanetsAndCusp.setKpCusp5(_ICalculationProxy
                .getKPCuspLongitude(CGlobalVariables.KPCUSP5 + 1));

        _inKPPlanetsAndCusp.setKpCusp6(_ICalculationProxy
                .getKPCuspLongitude(CGlobalVariables.KPCUSP6 + 1));

        _inKPPlanetsAndCusp.setKpCusp7(_ICalculationProxy
                .getKPCuspLongitude(CGlobalVariables.KPCUSP7 + 1));

        _inKPPlanetsAndCusp.setKpCusp8(_ICalculationProxy
                .getKPCuspLongitude(CGlobalVariables.KPCUSP8 + 1));

        _inKPPlanetsAndCusp.setKpCusp9(_ICalculationProxy
                .getKPCuspLongitude(CGlobalVariables.KPCUSP9 + 1));

        _inKPPlanetsAndCusp.setKpCusp10(_ICalculationProxy
                .getKPCuspLongitude(CGlobalVariables.KPCUSP10 + 1));

        _inKPPlanetsAndCusp.setKpCusp11(_ICalculationProxy
                .getKPCuspLongitude(CGlobalVariables.KPCUSP11 + 1));

        _inKPPlanetsAndCusp.setKpCusp12(_ICalculationProxy
                .getKPCuspLongitude(CGlobalVariables.KPCUSP12 + 1));

        _inKpPlanetSignification
                .setSunSignification(CUtils.getFormattedSignification(_ICalculationProxy
                        .getKPPlanetSignification(CGlobalVariables.SUN_SIG + 1)));

        _inKpPlanetSignification
                .setMoonSignification(CUtils.getFormattedSignification(_ICalculationProxy
                        .getKPPlanetSignification(CGlobalVariables.Moon_SIG + 1)));

        _inKpPlanetSignification
                .setMarsSignification(CUtils.getFormattedSignification(_ICalculationProxy
                        .getKPPlanetSignification(CGlobalVariables.MARS_SIG + 1)));

        _inKpPlanetSignification
                .setMercurySignification(CUtils.getFormattedSignification(_ICalculationProxy
                        .getKPPlanetSignification(CGlobalVariables.MERCURY_SIG + 1)));

        _inKpPlanetSignification
                .setJupiterSignification(CUtils.getFormattedSignification(_ICalculationProxy
                        .getKPPlanetSignification(CGlobalVariables.JUPITER_SIG + 1)));

        _inKpPlanetSignification
                .setVenusSignification(CUtils.getFormattedSignification(_ICalculationProxy
                        .getKPPlanetSignification(CGlobalVariables.VENUS_SIG + 1)));

        _inKpPlanetSignification
                .setSaturnSignification(CUtils.getFormattedSignification(_ICalculationProxy
                        .getKPPlanetSignification(CGlobalVariables.SATURN_SIG + 1)));

        _inKpPlanetSignification
                .setRahuSignification(CUtils.getFormattedSignification(_ICalculationProxy
                        .getKPPlanetSignification(CGlobalVariables.RAHU_SIG + 1)));

        _inKpPlanetSignification
                .setKetuSignification(CUtils.getFormattedSignification(_ICalculationProxy
                        .getKPPlanetSignification(CGlobalVariables.KETU_SIG + 1)));

        _inKPPlanetsAndCusp.setKpAyan(_ICalculationProxy
                .getKPAyanamsaLongitude());

        _inKPPlanetsAndCusp.setKpFortuna(_ICalculationProxy.getFortuna());

        // CODE UPDATED ON 16-SEP-13 (BIJENDRA)
        // _inKPPlanetsAndCusp.setDayLord(CUtils.getFullNameofDayLord(CGlobal.getCGlobalObject().getContext(),
        // _ICalculationProxy.getKPDayLordName()));
        _inKPPlanetsAndCusp.setDayLord(_ICalculationProxy.getKPDayLordName());
        // END

        _outShodashvarga.set_lagna(CUtils
                .getFormattedShodashAndAshtak(_ICalculationProxy
                        .getPositionForShodasvarg(CGlobalVariables.LAGNA)));
        _outShodashvarga.set_hora(CUtils
                .getFormattedShodashAndAshtak(_ICalculationProxy
                        .getPositionForShodasvarg(CGlobalVariables.HORA)));
        _outShodashvarga.set_drekkana(CUtils
                .getFormattedShodashAndAshtak(_ICalculationProxy
                        .getPositionForShodasvarg(CGlobalVariables.DREKKANA)));
        _outShodashvarga
                .set_chaturthamsha(CUtils.getFormattedShodashAndAshtak(_ICalculationProxy
                        .getPositionForShodasvarg(CGlobalVariables.CHATURTHAMSHA)));
        _outShodashvarga
                .set_saptamamsha(CUtils.getFormattedShodashAndAshtak(_ICalculationProxy
                        .getPositionForShodasvarg(CGlobalVariables.SAPTAMAMSHA)));
        _outShodashvarga.set_navamsha(CUtils
                .getFormattedShodashAndAshtak(_ICalculationProxy
                        .getPositionForShodasvarg(CGlobalVariables.NAVAMSHA)));
        _outShodashvarga
                .set_dashamamsha(CUtils.getFormattedShodashAndAshtak(_ICalculationProxy
                        .getPositionForShodasvarg(CGlobalVariables.DASHAMAMSHA)));
        _outShodashvarga
                .set_dwadashamamsha(CUtils.getFormattedShodashAndAshtak(_ICalculationProxy
                        .getPositionForShodasvarg(CGlobalVariables.DWADASHAMAMSHA)));
        _outShodashvarga
                .set_shodashamsha(CUtils.getFormattedShodashAndAshtak(_ICalculationProxy
                        .getPositionForShodasvarg(CGlobalVariables.SHODASHAMSHA)));
        _outShodashvarga
                .set_vimshamsha(CUtils.getFormattedShodashAndAshtak(_ICalculationProxy
                        .getPositionForShodasvarg(CGlobalVariables.VIMSHAMSHA)));
        _outShodashvarga
                .set_chaturvimshamsha(CUtils.getFormattedShodashAndAshtak(_ICalculationProxy
                        .getPositionForShodasvarg(CGlobalVariables.CHATURVIMSHAMSHA)));
        _outShodashvarga
                .set_saptavimshamsha(CUtils.getFormattedShodashAndAshtak(_ICalculationProxy
                        .getPositionForShodasvarg(CGlobalVariables.SAPTAVIMSHAMSHA)));
        _outShodashvarga
                .set_trimshamsha(CUtils.getFormattedShodashAndAshtak(_ICalculationProxy
                        .getPositionForShodasvarg(CGlobalVariables.TRIMSHAMSHA)));
        _outShodashvarga
                .set_khavedamsha(CUtils.getFormattedShodashAndAshtak(_ICalculationProxy
                        .getPositionForShodasvarg(CGlobalVariables.KHAVEDAMSHA)));
        _outShodashvarga
                .set_akshvedamsha(CUtils.getFormattedShodashAndAshtak(_ICalculationProxy
                        .getPositionForShodasvarg(CGlobalVariables.AKSHVEDAMSHA)));
        _outShodashvarga
                .set_shastiamsha(CUtils.getFormattedShodashAndAshtak(_ICalculationProxy
                        .getPositionForShodasvarg(CGlobalVariables.SHASTIAMSHA)));

        CGlobal.getCGlobalObject().setOutShodashvargaObject(_outShodashvarga);

        int[][] astak = new int[7][12];

        for (int j = 0; j <= 6; j++) {
            for (int i = 0; i <= 11; i++) {
                astak[j][i] = _ICalculationProxy
                        .getAshtakvargaBinduForSignAndPlanet(j, i);
            }
        }

        CGlobal.getCGlobalObject()
                .getOutAstakvargaTableObject()
                .setSun(CUtils
                        .getFormattedShodashAndAshtak(astak[CGlobalVariables.SUN_ASTAK]));
        CGlobal.getCGlobalObject()
                .getOutAstakvargaTableObject()
                .setMoon(
                        CUtils.getFormattedShodashAndAshtak(astak[CGlobalVariables.Moon_ASTAK]));
        CGlobal.getCGlobalObject()
                .getOutAstakvargaTableObject()
                .setMars(
                        CUtils.getFormattedShodashAndAshtak(astak[CGlobalVariables.MARS_ASTAK]));
        CGlobal.getCGlobalObject()
                .getOutAstakvargaTableObject()
                .setMercury(
                        CUtils.getFormattedShodashAndAshtak(astak[CGlobalVariables.MERCURY_ASTAK]));
        CGlobal.getCGlobalObject()
                .getOutAstakvargaTableObject()
                .setJupiter(
                        CUtils.getFormattedShodashAndAshtak(astak[CGlobalVariables.JUPITER_ASTAK]));
        CGlobal.getCGlobalObject()
                .getOutAstakvargaTableObject()
                .setVenus(
                        CUtils.getFormattedShodashAndAshtak(astak[CGlobalVariables.VENUS_ASTAK]));
        CGlobal.getCGlobalObject()
                .getOutAstakvargaTableObject()
                .setSat(CUtils
                        .getFormattedShodashAndAshtak(astak[CGlobalVariables.SATURN_ASTAK]));

        int[] total = _ICalculationProxy.getTotalAshtakVargaValue();
        CGlobal.getCGlobalObject().getOutAstakvargaTableObject()
                .setTotal(CUtils.getFormattedShodashAndAshtak(total));

        _outOutPanchang.set_SunRise(_ICalculationProxy.getSunRiseTimeHms());
        _outOutPanchang.set_SunSet(_ICalculationProxy.getSunSetTimeHms());
        _outOutPanchang.setTitAtBirth(_ICalculationProxy.getTithiName());
        _outOutPanchang.set_HinduWeekDay(String.valueOf(_ICalculationProxy.getHinduWeekday()));
        _outOutPanchang.setPaksha(_ICalculationProxy.getPakshaName());
        _outOutPanchang.setYoga(_ICalculationProxy.getYoganame());
        _outOutPanchang.setKaran(_ICalculationProxy.getKaranName());
        CGlobal.getCGlobalObject().setOutPanchangObject(_outOutPanchang);

        _planetDegree.setAyan(Double.valueOf(_ICalculationProxy.getAyanamsa()));

        String isCum = "";
        for (int i = 1; i < 12; i++) {
            if (_ICalculationProxy.isCombust(i)) {
                if (i == 1)
                    isCum += "1";
                else
                    isCum += ",1";
            } else {
                if (i == 1)
                    isCum += "0";
                else
                    isCum += ",0";
            }
        }

        _planetDegree.setCombust(isCum);

        String isDir = "";
        for (int i = 1; i < 13; i++) {
            if (_ICalculationProxy.isPlanetDirect(i)) {
                if (i == 1)
                    isDir += "1";
                else
                    isDir += ",1";
            } else {
                if (i == 1)
                    isDir += "0";
                else
                    isDir += ",0";
            }
        }

        _planetDegree.setDirect(isDir);
        // fillKPPlanets(_planetDegree, _inKPPlanetsAndCusp);

        CGlobal.getCGlobalObject().setPlanetDataObject(_planetDegree);
        CGlobal.getCGlobalObject().setInKPPlanetsAndCuspObject(
                fillKPPlanets(_planetDegree, _inKPPlanetsAndCusp));
        CGlobal.getCGlobalObject().setInKpPlanetSignificationObject(
                _inKpPlanetSignification);
    }

    /**
     * This function is used to fill kp planets value in the object ,fetched from server after
     * executing request.
     *
     * @return void
     */
    private InKPPlanetsAndCusp fillKPPlanets(PlanetData planetDegree, InKPPlanetsAndCusp inKPPlanetsAndCusp) {
        PlanetData _planetDegree = planetDegree;
        InKPPlanetsAndCusp _inKPPlanetsAndCusp = inKPPlanetsAndCusp;
        double tempCalculation = 0;
        double ayanDiff = _planetDegree.getAyan() - _inKPPlanetsAndCusp.getKpAyan();

        // for sun
        tempCalculation = _planetDegree.getSun() + ayanDiff;
        if (tempCalculation < 0) {
            // planetDegree can not be less than 0.
            tempCalculation += 360.00;
        } else if (tempCalculation >= 360) {
            // planetDegree can not be equals or more than 360.
            tempCalculation -= 360.00;
        }
        _inKPPlanetsAndCusp.setSun(tempCalculation);


        // for moon
        tempCalculation = _planetDegree.getMoon() + ayanDiff;
        if (tempCalculation < 0) {
            tempCalculation += 360.00;
        } else if (tempCalculation >= 360) {
            tempCalculation -= 360.00;
        }
        _inKPPlanetsAndCusp.setMoon(tempCalculation);


        //for marsh
        tempCalculation = _planetDegree.getMarsh() + ayanDiff;
        if (tempCalculation < 0) {
            tempCalculation += 360.00;
        } else if (tempCalculation >= 360) {
            tempCalculation -= 360.00;
        }
        _inKPPlanetsAndCusp.setMarsh(tempCalculation);

        //for mercury
        tempCalculation = _planetDegree.getMercury() + ayanDiff;
        if (tempCalculation < 0) {
            tempCalculation += 360.00;
        } else if (tempCalculation >= 360) {
            tempCalculation -= 360.00;
        }
        _inKPPlanetsAndCusp.setMercury(tempCalculation);


        //for jupiter
        tempCalculation = _planetDegree.getJup() + ayanDiff;
        if (tempCalculation < 0) {
            tempCalculation += 360.00;
        } else if (tempCalculation >= 360) {
            tempCalculation -= 360.00;
        }
        _inKPPlanetsAndCusp.setJup(tempCalculation);

        // for venus
        tempCalculation = _planetDegree.getVenus() + ayanDiff;
        if (tempCalculation < 0) {
            tempCalculation += 360.00;
        } else if (tempCalculation >= 360) {
            tempCalculation -= 360.00;
        }
        _inKPPlanetsAndCusp.setVenus(tempCalculation);

        //for sat
        tempCalculation = _planetDegree.getSat() + ayanDiff;
        if (tempCalculation < 0) {
            tempCalculation += 360.00;
        } else if (tempCalculation >= 360) {
            tempCalculation -= 360.00;
        }
        _inKPPlanetsAndCusp.setSat(tempCalculation);

        // for rahu
        tempCalculation = _planetDegree.getRahu() + ayanDiff;
        if (tempCalculation < 0) {
            tempCalculation += 360.00;
        } else if (tempCalculation >= 360) {
            tempCalculation -= 360.00;
        }
        _inKPPlanetsAndCusp.setRahu(tempCalculation);

        // for ketu
        tempCalculation = _planetDegree.getKetu() + ayanDiff;
        if (tempCalculation < 0) {
            tempCalculation += 360.00;
        } else if (tempCalculation >= 360) {
            tempCalculation -= 360.00;
        }
        _inKPPlanetsAndCusp.setKetu(tempCalculation);

        // for nept
        tempCalculation = _planetDegree.getNeptune() + ayanDiff;
        if (tempCalculation < 0) {
            tempCalculation += 360.00;
        } else if (tempCalculation >= 360) {
            tempCalculation -= 360.00;
        }
        _inKPPlanetsAndCusp.setNeptune(tempCalculation);

        //for pluto
        tempCalculation = _planetDegree.getPluto() + ayanDiff;
        if (tempCalculation < 0) {
            tempCalculation += 360.00;
        } else if (tempCalculation >= 360) {
            tempCalculation -= 360.00;
        }
        _inKPPlanetsAndCusp.setPluto(tempCalculation);

        //for uranus
        tempCalculation = _planetDegree.getUranus() + ayanDiff;
        if (tempCalculation < 0) {
            tempCalculation += 360.00;
        } else if (tempCalculation >= 360) {
            tempCalculation -= 360.00;
        }
        _inKPPlanetsAndCusp.setUranus(tempCalculation);

        return _inKPPlanetsAndCusp;

    }

    /**
     * This function is used to return  Database Helper Object
     *
     * @param context
     * @return CDatabaseHelper
     * @throws Exception
     */
    public CDatabaseHelper getDatabaseHelperObject(Context context)
            throws Exception {
        try {
            return (new CDatabaseHelper(context));
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * This function return the array of city id and name
     *
     * @param sqLiteDatabase
     * @param cityName
     * @return String[][]
     * @throws Exception
     * @author Bijendra 31-may-13
     */
    @Override
    public String[][] searchCity(SQLiteDatabase sqLiteDatabase, String cityName)
            throws Exception {

        return new CDataOperations().searchCity(sqLiteDatabase, cityName);
    }

    /**
     * This function is used to fatch city detail based on city id
     *
     * @param sqLiteDatabase
     * @param cityId
     * @return BeanPlace
     * @throws Exception
     */
    @Override
    public BeanPlace getCityById(SQLiteDatabase sqLiteDatabase, int cityId)
            throws Exception {
        // TODO Auto-generated method stub
        return new CDataOperations().getCityById(sqLiteDatabase, cityId);
    }

    @Override
    public BeanPlace getTimZoneId(SQLiteDatabase sqLiteDatabase, int timeZoneId)
            throws Exception {
        return new CDataOperations().getTimZoneId(sqLiteDatabase, timeZoneId);
    }

    /**
     * This function return array of time zones from database
     * according to passed time zone name
     *
     * @param sqLiteDatabase
     * @return String array contain time zone id and name
     * @throws Exception
     */
    @Override
    public String[][] searchTimeZone(SQLiteDatabase sqLiteDatabase,
                                     String searchText) throws Exception {
        return new CDataOperations().searchTimeZone(sqLiteDatabase, searchText);
    }

    /**
     * This function expose the functionality to
     * verify login .
     *
     * @return int(status)
     * @throws Exception
     */

	/*@Override
    public int verifyLogin(String _uid, String _pwd) throws Exception {

			return ( new COnlineChartOperations().verifyLogin(_uid, _pwd));

	}*/
    @Override
    public ArrayList<BeanHoroPersonalInfo> getOnlineChartList(Context context, String kundliName, String uid,
                                                              String pwd, String key, String isapi) throws Exception {
        // TODO Auto-generated method stub

        return (new COnlineChartOperations().getOnlineChartList(context, kundliName, uid, pwd, key, isapi));
    }

    /**
     * This function is used to delete kundli from AstroSage server
     *
     * @param kundliId
     * @param uid
     * @param pwd
     * @return
     * @throws Exception
     */
   /* @Override
    public int deleteOnlineKundli(long kundliId, String uid, String pwd)
            throws Exception {
        // TODO Auto-generated method stub
        return (new COnlineChartOperations().deleteOnlineKundli(kundliId, uid, pwd));
    }*/

    /**
     * This function is used to to return online  kundli detail from AstroSage server
     *
     * @param kundliId
     * @param uid
     * @param pwd
     * @return BeanHoroPersonalInfo
     * @throws Exception
     */

   /* @Override
    public BeanHoroPersonalInfo getOnlineKundliDetail(SQLiteDatabase sqLiteDatabase, long kundliId, String uid,
                                                      String pwd) throws Exception {
        // TODO Auto-generated method stub
        return (new COnlineChartOperations().getOnlineKundliDetail(sqLiteDatabase, kundliId, uid, pwd));
    }*/

    /**
     * This function return array of charts with in database according to
     * passed kundli name.
     *
     * @param sqLiteDatabase
     * @param kundliName
     * @param genderType
     * @return 2D  chart array contain chart id and name
     * @throws Exception
     */
    @Override
    public Map<String, String> searchLocalKundliList(SQLiteDatabase sqLiteDatabase,
                                                     String kundliName, int genderType, int noOfRecords) throws Exception {
        // TODO Auto-generated method stub
        return new CDataOperations().searchLocalKundliList(sqLiteDatabase, kundliName, genderType, noOfRecords);
    }

    /**
     * This function is used to delete local kundli
     *
     * @param sqLiteDatabase
     * @param kundliId
     * @return boolean
     * @throws Exception
     */
    @Override
    public boolean deleteLocalKundli(SQLiteDatabase sqLiteDatabase, long kundliId)
            throws Exception {
        return new CDataOperations().deleteLocalKundli(sqLiteDatabase, kundliId);
    }

    @Override
    public BeanHoroPersonalInfo getLocalKundliDetail(
            SQLiteDatabase sqLiteDatabase, long kundliId) throws Exception {
        // TODO Auto-generated method stub
        return new CDataOperations().getLocalKundliDetail(sqLiteDatabase, kundliId);
    }


    /**
     * This function is sued to calculate tajik varshphal
     *
     * @param objBirthDetail
     * @param yearNumber
     * @return boolean
     * @throws Exception
     */

    /*@Override
    public boolean calculateTajikVarshaphal(
            BeanHoroPersonalInfo objBirthDetail, String yearNumber)
            throws Exception {

        boolean isSuccess = true;
        ICalculationProxy icpd = null;

        try {
            icpd = new CWebServiceTajikVarshaphal(objBirthDetail, yearNumber);
            isSuccess = icpd.calculate();
        } catch (Exception e) {
            Log.d("Error_TAJIK", e.getMessage());
            throw e;
        }

        return isSuccess;


    }*/

    //BIJENDRA 24-12-13

    /**
     * This function is used to add/edit kundli personal details
     *
     * @param sqLiteDatabase
     * @param beanHoroPersonalInfo
     * @return (long)kundli local id
     * @throws Exception
     */
    @Override
    public long addEditHoroPersonalInfo(SQLiteDatabase sqLiteDatabase,
                                        BeanHoroPersonalInfo beanHoroPersonalInfo) throws Exception {
        // TODO Auto-generated method stub
        return new CDataOperations().addEditHoroPersonalInfo(sqLiteDatabase, beanHoroPersonalInfo);
    }

    /**
     * This function is used to save  kundli detail on AstroSage cloud
     *
     * @param beanHoroPersonalInfo
     * @throws Exception
     * @returnint[][online kundli id,message id]
     */
    /*@Override
    public long[] saveChartOnServer(BeanHoroPersonalInfo beanHoroPersonalInfo,
                                    String userId, String pwd) throws Exception {
        // TODO Auto-generated method stub
        return (new COnlineChartOperations().saveChartOnServer(beanHoroPersonalInfo, userId, pwd));
    }*/


    /*private BeanOutMatchmakingNorth calculateNorthMatchMakingResult(BeanHoroPersonalInfo boy, BeanHoroPersonalInfo girl, int languageCode, boolean iS_APP_ONLINE, boolean internetAvailable) throws Exception {
        BeanOutMatchmakingNorth bommn = new BeanOutMatchmakingNorth();

        IProxyKundliMatchingNorth okmn = null;
        //FOR ONLINE  MATCH MAKING
        if (iS_APP_ONLINE) {
            if (internetAvailable) {
                okmn = new OnlineKundliMatchingNorth(boy, girl, languageCode);
            } else {
                throw new NoInternetException(CGlobalVariables.INTERNET_IS_NOT_WORKING);
            }
        }
        //FOR OFFLINE  MATCH MAKING
        else {

        }
        if (okmn.initialize()) {

            bommn.setBoyMangalDosha(okmn.boyMangalDosh());
            bommn.setGirlMangalDosha(okmn.girlMangalDosh());

            bommn.setMatchPointVarna(okmn.matchVarnaGuna(0, 0));
            bommn.setMatchPointVasya(okmn.matchVashyaGuna(0, 0));
            bommn.setMatchPointTara(okmn.matchTaraGuna(0, 0));
            bommn.setMatchPointYoni(okmn.matchYoniGuna(0, 0));
            bommn.setMatchPointMaitri(okmn.matchGrahaMitraGuna(0, 0));
            bommn.setMatchPointBhakoot(okmn.matchBhakutGuna(0, 0));
            bommn.setMatchPointNadi(okmn.matchNadiGuna(0, 0));
            bommn.setMatchPointGana(okmn.matchGanaGuna(0, 0));
            bommn.setConclusion(okmn.getConclusion());
            //ADDED BY BIJENDRA ON 19-06-14
            bommn.setBoyRasiNumber(okmn.getBoyRashiNumber());
            bommn.setGirlRasiNumber(okmn.getGirlRashiNumber());
            //END

            bommn.setVarnaPrediction(okmn.subGetMatchMakingInterpretation(CGlobalVariables.MM_PREDICTION_VARNA, 0, 0));
            bommn.setVasyaPrediction(okmn.subGetMatchMakingInterpretation(CGlobalVariables.MM_PREDICTION_VASYA, 0, 0));
            bommn.setTaraPrediction(okmn.subGetMatchMakingInterpretation(CGlobalVariables.MM_PREDICTION_TARA, 0, 0));
            bommn.setYoniPrediction(okmn.subGetMatchMakingInterpretation(CGlobalVariables.MM_PREDICTION_YONI, 0, 0));
            bommn.setMaitriPrediction(okmn.subGetMatchMakingInterpretation(CGlobalVariables.MM_PREDICTION_MAITRI, 0, 0));
            bommn.setBhakootPrediction(okmn.subGetMatchMakingInterpretation(CGlobalVariables.MM_PREDICTION_BHAKUT, 0, 0));
            bommn.setNadiPrediction(okmn.subGetMatchMakingInterpretation(CGlobalVariables.MM_PREDICTION_NADI, 0, 0));
            bommn.setGanaPrediction(okmn.subGetMatchMakingInterpretation(CGlobalVariables.MM_PREDICTION_GANA, 0, 0));
            // END ONLINE DATA CALUCLATION

            //ADDED BY BIJENDRA ON 28-04-15
            bommn.setBoyMoonDegree(okmn.getBoyMoonDegree());
            bommn.setGirlMoonDegree(okmn.getGirlMoonDegree());

            //END

        } else {
            throw new Exception("Some internal Error occurred");
        }

        return bommn;
    }*/

    /*@Override
    public BeanOutMatchmakingNorth getMatchMakingResultNorth(
            BeanHoroPersonalInfo boy, BeanHoroPersonalInfo girl,
            int languageCode, boolean iS_APP_ONLINE, boolean internetStatus) throws Exception {
        // TODO Auto-generated method stub
        return calculateNorthMatchMakingResult(boy, girl, languageCode, iS_APP_ONLINE, internetStatus);

    }*/


    /**
     * This function share kundli name online
     *
     * @param userId
     * @param pwd
     * @param name
     * @param chartId
     * @return String[]
     * @throws Exception
     */
    /*@Override
    public String[] shareKundliNameOnline(String userId, String pwd,
                                          String name, String chartId) throws Exception {
        // TODO Auto-generated method stub
        return new COnlineChartOperations().shareKundliNameOnline(userId, pwd, name, chartId);
    }*/


    /**
     * This function is used to check the available name to share kundli
     *
     * @param kundliName
     * @return String[]
     * @throws Exception
     */

   /* @Override
    public String[] checkAvailableKundliNameToShare(String kundliName)
            throws Exception {
        return new COnlineChartOperations().checkAvailableKundliNameToShare(kundliName);
    }*/
    public boolean deleteOnlineChartIdFromLocalDatabase(SQLiteDatabase sQLiteDatabase, String onlineChartId) {
        return new CDataOperations().deleteOnlineChartIdFromLocalDatabase(sQLiteDatabase, onlineChartId);
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

        new CGCMRegistrationInfoSaveOnOjas().saveUserGCMRegistrationInformationOnOjasServer(context,
                regid, languageCode, loginId);

    }


   /* public String[] verifyLoginWithUserPurchasedPlan(String _uid, String _pwd, String key)
            throws Exception {
        // TODO Auto-generated method stub
        return (new COnlineChartOperations().verifyLoginWithUserPurchasedPlan(_uid, _pwd, key));
    }*/

    @Override
    public String[][] searchCityOperation(Context context, String cityName)
            throws Exception {
        // TODO Auto-generated method stub
        return new CDatabaseHelperOperations(context).searchCityOperation(cityName);
    }

    @Override
    public BeanPlace getCityByIdOperation(Context context, int cityId)
            throws Exception {
        // TODO Auto-generated method stub
        return new CDatabaseHelperOperations(context).getCityByIdOperation(cityId);

    }

    @Override
    public BeanPlace getTimZoneIdOperation(Context context, int timeZoneId)
            throws Exception {
        // TODO Auto-generated method stub
        return new CDatabaseHelperOperations(context).getTimZoneIdOperation(timeZoneId);
    }

    @Override
    public String[][] searchTimeZoneOperation(Context context, String searchText)
            throws Exception {
        // TODO Auto-generated method stub
        return new CDatabaseHelperOperations(context).searchTimeZoneOperation(searchText);
    }

    @Override
    public Map<String, String> searchLocalKundliListOperation(Context context,
                                                              String kundliName, int genderType, int noOfRecords) throws Exception {
        // TODO Auto-generated method stub
        return new CDatabaseHelperOperations(context).searchLocalKundliListOperation(kundliName, genderType, noOfRecords);
    }

    @Override
    public ArrayList<BeanHoroPersonalInfo> searchLocalKundliListOperation(Context context,
                                                                          int startIndex, int endIndex) throws Exception {
        // TODO Auto-generated method stub
        return new CDatabaseHelperOperations(context).loadAllLocalKundliPagination(startIndex, endIndex);
    }

    @Override
    public boolean deleteLocalKundliOperation(Context context, long kundliId)
            throws Exception {
        // TODO Auto-generated method stub
        return new CDatabaseHelperOperations(context).deleteLocalKundliOperation(kundliId);
    }

    @Override
    public boolean deleteLocalKundliOperation(Context context, long kundliId, long onlineChartId)
            throws Exception {
        // TODO Auto-generated method stub
        return new CDatabaseHelperOperations(context).deleteLocalKundliOperation(kundliId, onlineChartId);
    }

    @Override
    public BeanHoroPersonalInfo getLocalKundliDetailOperation(Context context,
                                                              long kundliId) throws Exception {
        // TODO Auto-generated method stub
        return new CDatabaseHelperOperations(context).getLocalKundliDetailOperation(kundliId);
    }

    @Override
    public long addEditHoroPersonalInfoOperation(Context context,
                                                 BeanHoroPersonalInfo beanHoroPersonalInfo) throws Exception {
        // TODO Auto-generated method stub
        return new CDatabaseHelperOperations(context).addEditHoroPersonalInfoOperation(beanHoroPersonalInfo);
    }

    @Override
    public boolean deleteOnlineChartIdFromLocalDatabaseOperation(Context context,
                                                                 String onlineChartId) {
        // TODO Auto-generated method stub
        return new CDatabaseHelperOperations(context).deleteOnlineChartIdFromLocalDatabaseOperation(onlineChartId);
    }

    /*@Override
    public BeanHoroPersonalInfo getOnlineKundliDetailNew(Context context,
                                                         long kundliId, String uid, String pwd) throws Exception {
        // TODO Auto-generated method stub
        return (new COnlineChartOperationsUpdate().getOnlineKundliDetail(context, kundliId, uid, pwd));
    }*/

    /**
     * This function is used to register the user on servers
     *
     * @param _uid,_pwd 18-Jan-2016
     */
    /*@Override
    public String[] userSignUp(String _uid, String _pwd, String key)
            throws Exception {
        // TODO Auto-generated method stub
        return (new COnlineChartOperations().userSignUp(_uid, _pwd, key));
    }*/

    /**
     * this function is used to register user to using emailid only
     *
     * @param _uid
     * @param key
     * @return
     * @throws Exception
     */
    /*@Override
    public String[] userSignUp(String _uid, String key)
            throws Exception {
        // TODO Auto-generated method stub
        return (new COnlineChartOperations().userSignUp(_uid, key));
    }*/

    /*@Override
    public String UpdateDefaultKundliOnServer(String kundliId, String userId,
                                              String password) {

        return new COnlineChartOperations().UpdateDefaultKundliOnServer(
                kundliId, userId, password);
    }*/

    /*public String changePasswordAndProfile(String emailId, String newPassword, String oldPassword, String userName, String key) {
        return new COnlineChartOperations().changePasswordAndProfile(emailId, newPassword, oldPassword, userName, key);
    }*/
   /* @Override
    public String saveNotesOnserver(Context context, String userid, String password, String usercomment, String onlinechartid) {
        return new COnlineDataCalculation().saveNotesOnserver(context, userid, password, usercomment, onlinechartid);
    }*/


    /*
     * Calculate the transit kundli data
     * */
    public void calculateTransitKundliData(TransitFragment transitFragment, Context context, BeanHoroPersonalInfo obj, boolean isOnline, boolean isInternetAvailble) throws Exception {

        ICalculationProxy _ICalculationProxy = null;
        this.context = context;
        beanHoroPersonalInfo = obj;
        if (isInternetAvailble) {
            //if (isInternetAvailble) {
            _ICalculationProxy = new TransitOnlineDataCalculation(transitFragment, context, obj);
            _ICalculationProxy.calculate(_ICalculationProxy);
           /* } else {
                throw new NoInternetException(CGlobalVariables.INTERNET_IS_NOT_WORKING);
            }*/

        } else {
           /* if (isOnline) {
                getData(null, transitFragment);
            } else {*/
            transitFragment.dismissProgressbar();
            MyCustomToast mct2 = new MyCustomToast(
                    context,
                    ((Activity) context).getLayoutInflater(),
                    (Activity) context, ((BaseInputActivity) context).regularTypeface);
            mct2.show(context.getResources().getString(R.string.no_internet));
            // }


            //_ICalculationProxy = new CLocalCalculation(obj);
        }
        /*ICalculationProxy _ICalculationProxy = null;
        if (isOnline) {
            //if (isInternetAvailble) {
            _ICalculationProxy = new TransitOnlineDataCalculation(transitFragment,context, obj);

           *//* } else {
                throw new NoInternetException(CGlobalVariables.INTERNET_IS_NOT_WORKING);
            }*//*

        } else {
            _ICalculationProxy = new CLocalCalculation(obj);
        }
        _ICalculationProxy.calculate(_ICalculationProxy);*/
        // separateKundliData(_ICalculationProxy);
    }

    /*@Override
    public String syncChartOnServer(Context context, String userName, String password, String jsonString) {
        return new COnlineChartOperations().syncChartOnServer(context, userName, password, jsonString);
    }*/

    @Override
    public long[] getSaveOnlineChartId(String chartSavedId, String updatedOnlineChartId) throws Exception {

        return new COnlineChartOperations().getSaveOnlineChartId(chartSavedId, updatedOnlineChartId);
    }
}