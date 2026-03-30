package com.ojassoft.astrosage.beans;

import java.util.ArrayList;

import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;


public class OutShodashvarga implements IOutBean {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int _horaRashi1 = 0;
    private int _horaRashi2 = 0;

    private ArrayList<Integer> Bhav1 = new ArrayList<Integer>();
    private ArrayList<Integer> Bhav2 = new ArrayList<Integer>();
    private ArrayList<Integer> Bhav3 = new ArrayList<Integer>();
    private ArrayList<Integer> Bhav4 = new ArrayList<Integer>();
    private ArrayList<Integer> Bhav5 = new ArrayList<Integer>();
    private ArrayList<Integer> Bhav6 = new ArrayList<Integer>();
    private ArrayList<Integer> Bhav7 = new ArrayList<Integer>();
    private ArrayList<Integer> Bhav8 = new ArrayList<Integer>();
    private ArrayList<Integer> Bhav9 = new ArrayList<Integer>();
    private ArrayList<Integer> Bhav10 = new ArrayList<Integer>();
    private ArrayList<Integer> Bhav11 = new ArrayList<Integer>();
    private ArrayList<Integer> Bhav12 = new ArrayList<Integer>();


    private ArrayList<Integer> PlanetsInRashi1 = new ArrayList<Integer>();
    private ArrayList<Integer> PlanetsInRashi2 = new ArrayList<Integer>();
    private ArrayList<Integer> PlanetsInRashi3 = new ArrayList<Integer>();
    private ArrayList<Integer> PlanetsInRashi4 = new ArrayList<Integer>();
    private ArrayList<Integer> PlanetsInRashi5 = new ArrayList<Integer>();
    private ArrayList<Integer> PlanetsInRashi6 = new ArrayList<Integer>();
    private ArrayList<Integer> PlanetsInRashi7 = new ArrayList<Integer>();
    private ArrayList<Integer> PlanetsInRashi8 = new ArrayList<Integer>();
    private ArrayList<Integer> PlanetsInRashi9 = new ArrayList<Integer>();
    private ArrayList<Integer> PlanetsInRashi10 = new ArrayList<Integer>();
    private ArrayList<Integer> PlanetsInRashi11 = new ArrayList<Integer>();
    private ArrayList<Integer> PlanetsInRashi12 = new ArrayList<Integer>();

    private int _lagnaRashi = 0;
    private String[] _planetInRashi = new String[13];


    private String _lagna = "0";
    private String _hora = "0";
    private String _drekkana = "0";
    private String _chaturthamsha = "0";
    private String _saptamamsha = "0";
    private String _navamsha = "0";
    private String _dashamamsha = "0";
    private String _dwadashamamsha = "0";
    private String _shodashamsha = "0";
    private String _vimshamsha = "0";
    private String _chaturvimshamsha = "0";
    private String _saptavimshamsha = "0";
    private String _trimshamsha = "0";
    private String _khavedamsha = "0";
    private String _akshvedamsha = "0";
    private String _shastiamsha = "0";

    public void set_lagna(String _lagna) {
        this._lagna = _lagna;
    }

    public String get_lagna() {
        return _lagna;
    }

    public void set_hora(String _hora) {
        this._hora = _hora;
    }

    public String get_hora() {
        return _hora;
    }

    public void set_drekkana(String _drekkana) {
        this._drekkana = _drekkana;
    }

    public String get_drekkana() {
        return _drekkana;
    }

    public void set_chaturthamsha(String _chaturthamsha) {
        this._chaturthamsha = _chaturthamsha;
    }

    public String get_chaturthamsha() {
        return _chaturthamsha;
    }

    public void set_saptamamsha(String _saptamamsha) {
        this._saptamamsha = _saptamamsha;
    }

    public String get_saptamamsha() {
        return _saptamamsha;
    }

    public void set_navamsha(String _navamsha) {
        this._navamsha = _navamsha;
    }

    public String get_navamsha() {
        return _navamsha;
    }

    public void set_dashamamsha(String _dashamamsha) {
        this._dashamamsha = _dashamamsha;
    }

    public String get_dashamamsha() {
        return _dashamamsha;
    }

    public void set_dwadashamamsha(String _dwadashamamsha) {
        this._dwadashamamsha = _dwadashamamsha;
    }

    public String get_dwadashamamsha() {
        return _dwadashamamsha;
    }

    public void set_shodashamsha(String _shodashamsha) {
        this._shodashamsha = _shodashamsha;
    }

    public String get_shodashamsha() {
        return _shodashamsha;
    }

    public void set_vimshamsha(String _vimshamsha) {
        this._vimshamsha = _vimshamsha;
    }

    public String get_vimshamsha() {
        return _vimshamsha;
    }

    public void set_chaturvimshamsha(String _chaturvimshamsha) {
        this._chaturvimshamsha = _chaturvimshamsha;
    }

    public String get_chaturvimshamsha() {
        return _chaturvimshamsha;
    }

    public void set_saptavimshamsha(String _saptavimshamsha) {
        this._saptavimshamsha = _saptavimshamsha;
    }

    public String get_saptavimshamsha() {
        return _saptavimshamsha;
    }

    public void set_trimshamsha(String _trimshamsha) {
        this._trimshamsha = _trimshamsha;
    }

    public String get_trimshamsha() {
        return _trimshamsha;
    }

    public void set_khavedamsha(String _khavedamsha) {
        this._khavedamsha = _khavedamsha;
    }

    public String get_khavedamsha() {
        return _khavedamsha;
    }

    public void set_akshvedamsha(String _akshvedamsha) {
        this._akshvedamsha = _akshvedamsha;
    }

    public String get_akshvedamsha() {
        return _akshvedamsha;
    }

    public void set_shastiamsha(String _shastiamsha) {
        this._shastiamsha = _shastiamsha;
    }

    public String get_shastiamsha() {
        return _shastiamsha;
    }

    public ArrayList<Integer> getBhav1() {
        return Bhav1;
    }

    public ArrayList<Integer> getBhav2() {
        return Bhav2;
    }

    public ArrayList<Integer> getBhav3() {
        return Bhav3;
    }

    public ArrayList<Integer> getBhav4() {
        return Bhav4;
    }

    public ArrayList<Integer> getBhav5() {
        return Bhav5;
    }

    public ArrayList<Integer> getBhav6() {
        return Bhav6;
    }

    public ArrayList<Integer> getBhav7() {
        return Bhav7;
    }

    public ArrayList<Integer> getBhav8() {
        return Bhav8;
    }

    public ArrayList<Integer> getBhav9() {
        return Bhav9;
    }

    public ArrayList<Integer> getBhav10() {
        return Bhav10;
    }

    public ArrayList<Integer> getBhav11() {
        return Bhav11;
    }

    public ArrayList<Integer> getBhav12() {
        return Bhav12;
    }


    public void set_lagnaRashi(int _lagnaRashi) {
        this._lagnaRashi = _lagnaRashi;
    }

    public int get_lagnaRashi() {
        return _lagnaRashi;
    }


    private void clearAllBhava() {
        Bhav1.clear();
        Bhav2.clear();
        Bhav3.clear();
        Bhav4.clear();
        Bhav5.clear();
        Bhav6.clear();
        Bhav7.clear();
        Bhav8.clear();
        Bhav9.clear();
        Bhav10.clear();
        Bhav11.clear();
        Bhav12.clear();

    }

    private void clearAllInRashi() {
        PlanetsInRashi1.clear();
        PlanetsInRashi2.clear();
        PlanetsInRashi3.clear();
        PlanetsInRashi4.clear();
        PlanetsInRashi5.clear();
        PlanetsInRashi6.clear();
        PlanetsInRashi7.clear();
        PlanetsInRashi8.clear();
        PlanetsInRashi9.clear();
        PlanetsInRashi10.clear();
        PlanetsInRashi11.clear();
        PlanetsInRashi12.clear();

    }

    public void calculatePlanetsInRashi(CGlobalVariables.enuShodashvarga enuSho) {
        clearAllInRashi();
        _planetInRashi = null;
        switch (enuSho) {
            case SHODASHVARGA_LAGNA:
                _planetInRashi = get_lagna().split(",");
                break;
            //case SHODASHVARGA_HORA:
            //_planetInRashi=get_hora().split(",");

            //break;
            case SHODASHVARGA_DREKKANA:
                _planetInRashi = get_drekkana().split(",");
                break;
            case SHODASHVARGA_CHATURTHAMSHA:
                _planetInRashi = get_chaturthamsha().split(",");
                break;
            case SHODASHVARGA_SAPTAMAMSHA:
                _planetInRashi = get_saptamamsha().split(",");
                break;
            case SHODASHVARGA_NAVAMSHA:
                _planetInRashi = get_navamsha().split(",");
                break;
            case SHODASHVARGA_DASHAMAMSHA:
                _planetInRashi = get_dashamamsha().split(",");
                break;
            case SHODASHVARGA_DWADASHAMAMSHA:
                _planetInRashi = get_dwadashamamsha().split(",");
                break;
            case SHODASHVARGA_SHODASHAMSHA:
                _planetInRashi = get_shodashamsha().split(",");
                break;
            case SHODASHVARGA_VIMSHAMSHA:
                _planetInRashi = get_vimshamsha().split(",");
                break;
            case SHODASHVARGA_CHATURVIMSHAMSHA:
                _planetInRashi = get_chaturvimshamsha().split(",");
                break;
            case SHODASHVARGA_SAPTAVIMSHAMSHA:
                _planetInRashi = get_saptavimshamsha().split(",");
                break;
            case SHODASHVARGA_TRIMSHAMSHA:
                _planetInRashi = get_trimshamsha().split(",");
                break;
            case SHODASHVARGA_KHAVEDAMSHA:
                _planetInRashi = get_khavedamsha().split(",");
                break;
            case SHODASHVARGA_AKSHVEDAMSHA:
                _planetInRashi = get_akshvedamsha().split(",");
                break;

            case SHODASHVARGA_SHASHTIAMSHA:
                _planetInRashi = get_shastiamsha().split(",");
                break;

        }
        _lagnaRashi = CUtils.getStringToInteger(_planetInRashi[0]);

        putPlanetsInRashi();
    }

    private void putPlanetsInRashi() {

        putPlanetIntoRashi(0, CUtils.getStringToInteger(_planetInRashi[1]));

        //MOON-1
        putPlanetIntoRashi(1, CUtils.getStringToInteger(_planetInRashi[2]));

        //MARS-2
        putPlanetIntoRashi(2, CUtils.getStringToInteger(_planetInRashi[3]));

        //MERCURY-3
        putPlanetIntoRashi(3, CUtils.getStringToInteger(_planetInRashi[4]));

        //JUPITER-4
        putPlanetIntoRashi(4, CUtils.getStringToInteger(_planetInRashi[5]));

        //VENUS-5
        putPlanetIntoRashi(5, CUtils.getStringToInteger(_planetInRashi[6]));

        //SAT-6
        putPlanetIntoRashi(6, CUtils.getStringToInteger(_planetInRashi[7]));

        //RAHU-7
        putPlanetIntoRashi(7, CUtils.getStringToInteger(_planetInRashi[8]));

        //KETU-8
        putPlanetIntoRashi(8, CUtils.getStringToInteger(_planetInRashi[9]));

        //URANUS-9
        putPlanetIntoRashi(9, CUtils.getStringToInteger(_planetInRashi[10]));

        //NEPTUN-10
        putPlanetIntoRashi(10, CUtils.getStringToInteger(_planetInRashi[11]));

        //PLUTO -11
        putPlanetIntoRashi(11, CUtils.getStringToInteger(_planetInRashi[12]));

    }


    private void putPlanetIntoRashi(int planetNumber, int rashiNumber) {
        switch (rashiNumber) {
            case 1:
                PlanetsInRashi1.add(planetNumber);
                break;
            case 2:
                PlanetsInRashi2.add(planetNumber);
                break;
            case 3:
                PlanetsInRashi3.add(planetNumber);
                break;
            case 4:
                PlanetsInRashi4.add(planetNumber);
                break;
            case 5:
                PlanetsInRashi5.add(planetNumber);
                break;
            case 6:
                PlanetsInRashi6.add(planetNumber);
                break;
            case 7:
                PlanetsInRashi7.add(planetNumber);
                break;
            case 8:
                PlanetsInRashi8.add(planetNumber);
                break;
            case 9:
                PlanetsInRashi9.add(planetNumber);
                break;
            case 10:
                PlanetsInRashi10.add(planetNumber);
                break;
            case 11:
                PlanetsInRashi11.add(planetNumber);
                break;
            case 12:
                PlanetsInRashi12.add(planetNumber);
                break;

        }
    }

    public void calculatePlanetsInBhav(CGlobalVariables.enuShodashvarga enuSho) {
        clearAllBhava();
        _planetInRashi = null;
        switch (enuSho) {
            case SHODASHVARGA_LAGNA:
                _planetInRashi = get_lagna().split(",");
                break;
            case SHODASHVARGA_HORA:
                _planetInRashi = get_hora().split(",");

                break;
            case SHODASHVARGA_DREKKANA:
                _planetInRashi = get_drekkana().split(",");
                break;
            case SHODASHVARGA_CHATURTHAMSHA:
                _planetInRashi = get_chaturthamsha().split(",");
                break;
            case SHODASHVARGA_SAPTAMAMSHA:
                _planetInRashi = get_saptamamsha().split(",");
                break;
            case SHODASHVARGA_NAVAMSHA:
                _planetInRashi = get_navamsha().split(",");
                break;
            case SHODASHVARGA_DASHAMAMSHA:
                _planetInRashi = get_dashamamsha().split(",");
                break;
            case SHODASHVARGA_DWADASHAMAMSHA:
                _planetInRashi = get_dwadashamamsha().split(",");
                break;
            case SHODASHVARGA_SHODASHAMSHA:
                _planetInRashi = get_shodashamsha().split(",");
                break;
            case SHODASHVARGA_VIMSHAMSHA:
                _planetInRashi = get_vimshamsha().split(",");
                break;
            case SHODASHVARGA_CHATURVIMSHAMSHA:
                _planetInRashi = get_chaturvimshamsha().split(",");
                break;
            case SHODASHVARGA_SAPTAVIMSHAMSHA:
                _planetInRashi = get_saptavimshamsha().split(",");
                break;
            case SHODASHVARGA_TRIMSHAMSHA:
                _planetInRashi = get_trimshamsha().split(",");
                break;
            case SHODASHVARGA_KHAVEDAMSHA:
                _planetInRashi = get_khavedamsha().split(",");
                break;
            case SHODASHVARGA_AKSHVEDAMSHA:
                _planetInRashi = get_akshvedamsha().split(",");
                break;

            case SHODASHVARGA_SHASHTIAMSHA:
                _planetInRashi = get_shastiamsha().split(",");
                break;

        }
        if (_planetInRashi != null) {
            _lagnaRashi = CUtils.getStringToInteger(_planetInRashi[0]);

            if (enuSho == CGlobalVariables.enuShodashvarga.SHODASHVARGA_HORA) {
                calculateHoraRashi();
                putPlanetsInBhavInHora();
            } else
                putPlanetsInBhav();
        }

    }

    private void calculateHoraRashi() {
        boolean _secondHoraRahis = false;
        int index = 0;
        this._horaRashi1 = CUtils.getStringToInteger(_planetInRashi[0]);
        do {
            ++index;
            if (index > 11)
                index = 1;
            if (this._horaRashi1 != CUtils.getStringToInteger(_planetInRashi[index])) {
                this._horaRashi2 = CUtils.getStringToInteger(_planetInRashi[index]);
                _secondHoraRahis = true;
            }

        } while (!_secondHoraRahis);

    }

    private void putPlanetsInBhavInHora() {
        int _lagna = CUtils.getStringToInteger(_planetInRashi[0]);
        //SUN -0
        //putPlanetIntoBhav(0,getCalculatePlanetBhav(_lagna,getStringToInteger(_planetInRashi[1])));
        if (_lagna == CUtils.getStringToInteger(_planetInRashi[1]))//0
            Bhav1.add(0);
        else
            Bhav2.add(0);

        if (_lagna == CUtils.getStringToInteger(_planetInRashi[2]))//1
            Bhav1.add(1);
        else
            Bhav2.add(1);

        if (_lagna == CUtils.getStringToInteger(_planetInRashi[3]))//2
            Bhav1.add(2);
        else
            Bhav2.add(2);

        if (_lagna == CUtils.getStringToInteger(_planetInRashi[4]))//3
            Bhav1.add(3);
        else
            Bhav2.add(3);

        if (_lagna == CUtils.getStringToInteger(_planetInRashi[5]))//4
            Bhav1.add(4);
        else
            Bhav2.add(4);


        if (_lagna == CUtils.getStringToInteger(_planetInRashi[6]))//5
            Bhav1.add(5);
        else
            Bhav2.add(5);

        if (_lagna == CUtils.getStringToInteger(_planetInRashi[7]))//6
            Bhav1.add(6);
        else
            Bhav2.add(6);

        if (_lagna == CUtils.getStringToInteger(_planetInRashi[8]))//7
            Bhav1.add(7);
        else
            Bhav2.add(7);


        if (_lagna == CUtils.getStringToInteger(_planetInRashi[9]))//8
            Bhav1.add(8);
        else
            Bhav2.add(8);


        if (_lagna == CUtils.getStringToInteger(_planetInRashi[10]))//9
            Bhav1.add(9);
        else
            Bhav2.add(9);


        if (_lagna == CUtils.getStringToInteger(_planetInRashi[11]))//10
            Bhav1.add(10);
        else
            Bhav2.add(10);

        if (_lagna == CUtils.getStringToInteger(_planetInRashi[12]))//11
            Bhav1.add(11);
        else
            Bhav2.add(11);


    }

    private void putPlanetsInBhav() {
        int _lagna = CUtils.getStringToInteger(_planetInRashi[0]);
        //SUN -0
        putPlanetIntoBhav(0, getCalculatePlanetBhav(_lagna, CUtils.getStringToInteger(_planetInRashi[1])));

        //MOON-1
        putPlanetIntoBhav(1, getCalculatePlanetBhav(_lagna, CUtils.getStringToInteger(_planetInRashi[2])));

        //MARS-2
        putPlanetIntoBhav(2, getCalculatePlanetBhav(_lagna, CUtils.getStringToInteger(_planetInRashi[3])));

        //MERCURY-3
        putPlanetIntoBhav(3, getCalculatePlanetBhav(_lagna, CUtils.getStringToInteger(_planetInRashi[4])));

        //JUPITER-4
        putPlanetIntoBhav(4, getCalculatePlanetBhav(_lagna, CUtils.getStringToInteger(_planetInRashi[5])));

        //VENUS-5
        putPlanetIntoBhav(5, getCalculatePlanetBhav(_lagna, CUtils.getStringToInteger(_planetInRashi[6])));

        //SAT-6
        putPlanetIntoBhav(6, getCalculatePlanetBhav(_lagna, CUtils.getStringToInteger(_planetInRashi[7])));

        //RAHU-7
        putPlanetIntoBhav(7, getCalculatePlanetBhav(_lagna, CUtils.getStringToInteger(_planetInRashi[8])));

        //KETU-8
        putPlanetIntoBhav(8, getCalculatePlanetBhav(_lagna, CUtils.getStringToInteger(_planetInRashi[9])));

        //URANUS-9
        putPlanetIntoBhav(9, getCalculatePlanetBhav(_lagna, CUtils.getStringToInteger(_planetInRashi[10])));

        //NEPTUN-10
        putPlanetIntoBhav(10, getCalculatePlanetBhav(_lagna, CUtils.getStringToInteger(_planetInRashi[11])));

        //PLUTO -11
        putPlanetIntoBhav(11, getCalculatePlanetBhav(_lagna, CUtils.getStringToInteger(_planetInRashi[12])));


    }

    private void putPlanetIntoBhav(int planetNumber, int bhavanumber) {
        //Toast.makeText(context,String.valueOf(objPlanet.getPlanetBhav()), Toast.LENGTH_LONG).show();

        switch (bhavanumber) {
            case 1:
                Bhav1.add(planetNumber);
                break;
            case 2:
                Bhav2.add(planetNumber);
                break;
            case 3:
                Bhav3.add(planetNumber);
                break;
            case 4:
                Bhav4.add(planetNumber);
                break;
            case 5:
                Bhav5.add(planetNumber);
                break;
            case 6:
                Bhav6.add(planetNumber);
                break;
            case 7:
                Bhav7.add(planetNumber);
                break;
            case 8:
                Bhav8.add(planetNumber);
                break;
            case 9:
                Bhav9.add(planetNumber);
                break;
            case 10:
                Bhav10.add(planetNumber);
                break;
            case 11:
                Bhav11.add(planetNumber);
                break;
            case 12:
                Bhav12.add(planetNumber);
                break;

        }
    }

    private int getCalculatePlanetBhav(int lRasi, int pRasi) {
        int _iTemo = 0;

        try {
            _iTemo = pRasi - lRasi;
            if (_iTemo < 0)
                _iTemo += 12;
            _iTemo += 1;
        } catch (Exception e) {

        }
        return _iTemo;
    }

    public void set_horaRashi1(int _horaRashi1) {
        this._horaRashi1 = _horaRashi1;
    }

    public int get_horaRashi1() {
        return _horaRashi1;
    }

    public void set_horaRashi2(int _horaRashi2) {
        this._horaRashi2 = _horaRashi2;
    }

    public int get_horaRashi2() {
        return _horaRashi2;
    }

    public void setPlanetsInRashi1(ArrayList<Integer> planetsInRashi1) {
        PlanetsInRashi1 = planetsInRashi1;
    }

    public ArrayList<Integer> getPlanetsInRashi1() {
        return PlanetsInRashi1;
    }

    public void setPlanetsInRashi2(ArrayList<Integer> planetsInRashi2) {
        PlanetsInRashi2 = planetsInRashi2;
    }

    public ArrayList<Integer> getPlanetsInRashi2() {
        return PlanetsInRashi2;
    }

    public void setPlanetsInRashi3(ArrayList<Integer> planetsInRashi3) {
        PlanetsInRashi3 = planetsInRashi3;
    }

    public ArrayList<Integer> getPlanetsInRashi3() {
        return PlanetsInRashi3;
    }

    public void setPlanetsInRashi4(ArrayList<Integer> planetsInRashi4) {
        PlanetsInRashi4 = planetsInRashi4;
    }

    public ArrayList<Integer> getPlanetsInRashi4() {
        return PlanetsInRashi4;
    }

    public void setPlanetsInRashi5(ArrayList<Integer> planetsInRashi5) {
        PlanetsInRashi5 = planetsInRashi5;
    }

    public ArrayList<Integer> getPlanetsInRashi5() {
        return PlanetsInRashi5;
    }

    public void setPlanetsInRashi6(ArrayList<Integer> planetsInRashi6) {
        PlanetsInRashi6 = planetsInRashi6;
    }

    public ArrayList<Integer> getPlanetsInRashi6() {
        return PlanetsInRashi6;
    }

    public void setPlanetsInRashi7(ArrayList<Integer> planetsInRashi7) {
        PlanetsInRashi7 = planetsInRashi7;
    }

    public ArrayList<Integer> getPlanetsInRashi7() {
        return PlanetsInRashi7;
    }

    public void setPlanetsInRashi8(ArrayList<Integer> planetsInRashi8) {
        PlanetsInRashi8 = planetsInRashi8;
    }

    public ArrayList<Integer> getPlanetsInRashi8() {
        return PlanetsInRashi8;
    }

    public void setPlanetsInRashi9(ArrayList<Integer> planetsInRashi9) {
        PlanetsInRashi9 = planetsInRashi9;
    }

    public ArrayList<Integer> getPlanetsInRashi9() {
        return PlanetsInRashi9;
    }

    public void setPlanetsInRashi10(ArrayList<Integer> planetsInRashi10) {
        PlanetsInRashi10 = planetsInRashi10;
    }

    public ArrayList<Integer> getPlanetsInRashi10() {
        return PlanetsInRashi10;
    }

    public void setPlanetsInRashi11(ArrayList<Integer> planetsInRashi11) {
        PlanetsInRashi11 = planetsInRashi11;
    }

    public ArrayList<Integer> getPlanetsInRashi11() {
        return PlanetsInRashi11;
    }

    public void setPlanetsInRashi12(ArrayList<Integer> planetsInRashi12) {
        PlanetsInRashi12 = planetsInRashi12;
    }

    public ArrayList<Integer> getPlanetsInRashi12() {
        return PlanetsInRashi12;
    }

    //NAVMANSHA CALCULATION

    public void calculateNavamshaNorthChart() {
        clearAllBhava();
        _planetInRashi = null;

        _planetInRashi = get_navamsha().split(",");
        if (_planetInRashi != null) {
            //_lagnaRashi=getStringToInteger(_planetInRashi[0]);
            try {
                _lagnaRashi = Integer.parseInt(_planetInRashi[0]);
                putPlanetsInBhav();
            } catch (Exception e) {

            }
        }
    }

    public void calculateNavamshaSouthChart() {
        clearAllInRashi();
        _planetInRashi = null;

        _planetInRashi = get_navamsha().split(",");
        if (_planetInRashi != null) {
            try {
                _lagnaRashi = Integer.parseInt(_planetInRashi[0]);
                putPlanetsInRashi();
            } catch (Exception e) {

            }
        }
    }


}
