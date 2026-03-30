package com.ojassoft.astrosage.utils;

import java.io.Serializable;

import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.BeanTajikVarshaphal;
import com.ojassoft.astrosage.beans.InKPPlanetsAndCusp;
import com.ojassoft.astrosage.beans.InKpPlanetSignification;
import com.ojassoft.astrosage.beans.OutAstakvargaTable;
import com.ojassoft.astrosage.beans.OutPanchang;
import com.ojassoft.astrosage.beans.OutShodashvarga;
import com.ojassoft.astrosage.beans.PlanetData;

public class CGlobal implements Serializable {

    /**
     *
     */
    private PlanetData planetDataTransit;
    private static final long serialVersionUID = 1L;
    private PlanetData planetData;
    private OutPanchang outPanchang;
    private OutAstakvargaTable outAstakvargaTable;
    private OutShodashvarga outShodashvarga;
    private InKPPlanetsAndCusp inKPPlanetsAndCusp;
    private InKpPlanetSignification inKpPlanetSignification;
    private BeanHoroPersonalInfo horoPersonalInfo = new BeanHoroPersonalInfo();
    private BeanTajikVarshaphal beanTajikVarshaphal = new BeanTajikVarshaphal();

    private static CGlobal cGlobal;
    // Initialize panchang,astakvarga, shodasvarga and kpplanet in global object for OutpputMasterMctivity
    private CGlobal() {
        outPanchang = new OutPanchang();
        outAstakvargaTable = new OutAstakvargaTable();
        outShodashvarga = new OutShodashvarga();
        inKPPlanetsAndCusp = new InKPPlanetsAndCusp();
    }

    /**
     * This is a static function used return
     * CGlobal object.
     *
     * @return CGlobal
     */
    public static CGlobal getCGlobalObject() {
        if (cGlobal == null)
            cGlobal = new CGlobal();

        return cGlobal;
    }

    public static void setCGlobalObject(CGlobal _cGlobal) {
        cGlobal = _cGlobal;
    }

    /**
     * This is a setter of OutAstakvargaTable object.
     *
     * @param OutAstakvargaTable
     */
    public void setOutAstakvargaTableObject(OutAstakvargaTable outAstakvargaTable) {
        this.outAstakvargaTable = outAstakvargaTable;
    }

    /**
     * This is getter of OutAstakvargaTable object.
     *
     * @return OutAstakvargaTable
     */
    public OutAstakvargaTable getOutAstakvargaTableObject() {
        return outAstakvargaTable;
    }

    /**
     * This is a setter of PlanetData object.
     *
     * @param planetData
     */
    public void setPlanetDataObject(PlanetData planetData) {
        this.planetData = planetData;
    }

    /**
     * This is getter of PlanetData object.
     *
     * @return PlanetData
     */
    public PlanetData getPlanetDataObject() {
        return planetData;
    }

    /**
     * This is a setter of OutPanchang object.
     *
     * @param OutPanchang
     */
    public void setOutPanchangObject(OutPanchang outPanchang) {
        this.outPanchang = outPanchang;
    }

    /**
     * This is getter of OutPanchang object.
     *
     * @return OutPanchang
     */
    public OutPanchang getOutPanchangObject() {
        return outPanchang;
    }

    /**
     * This is a setter of InKPPlanetsAndCusp object.
     *
     * @param InKPPlanetsAndCusp
     */
    public void setInKPPlanetsAndCuspObject(InKPPlanetsAndCusp inKPPlanetsAndCusp) {
        this.inKPPlanetsAndCusp = inKPPlanetsAndCusp;
    }

    /**
     * This is getter of InKPPlanetsAndCusp object.
     *
     * @return InKPPlanetsAndCusp
     */
    public InKPPlanetsAndCusp getInKPPlanetsAndCuspObject() {
        return inKPPlanetsAndCusp;
    }

    /**
     * This is a setter of InKpPlanetSignification object.
     *
     * @param InKpPlanetSignification
     */
    public void setInKpPlanetSignificationObject(InKpPlanetSignification inKpPlanetSignification) {
        this.inKpPlanetSignification = inKpPlanetSignification;
    }

    /**
     * This is getter of InKpPlanetSignification object.
     *
     * @return InKpPlanetSignification
     */
    public InKpPlanetSignification getInKpPlanetSignificationObject() {
        return inKpPlanetSignification;
    }

    /**
     * This is a setter of OutShodashvarga object.
     *
     * @param OutShodashvarga
     */
    public void setOutShodashvargaObject(OutShodashvarga outShodashvarga) {
        this.outShodashvarga = outShodashvarga;
    }

    /**
     * This is getter of OutShodashvarga object.
     *
     * @return OutShodashvarga
     */
    public OutShodashvarga getOutShodashvargaObject() {
        return outShodashvarga;
    }

    /**
     * This is a setter of HoroPersonalInfo object.
     *
     * @param HoroPersonalInfo
     */
    public void setHoroPersonalInfoObject(BeanHoroPersonalInfo horoPersonalInfo) {
        this.horoPersonalInfo = horoPersonalInfo;
    }

    /**
     * This is getter of HoroPersonalInfo object.
     *
     * @return HoroPersonalInfo
     */
    public BeanHoroPersonalInfo getHoroPersonalInfoObject() {
        return horoPersonalInfo;
    }

    public BeanTajikVarshaphal getBeanTajikVarshaphal() {
        return beanTajikVarshaphal;
    }

    public void setBeanTajikVarshaphal(BeanTajikVarshaphal beanTajikVarshaphal) {
        this.beanTajikVarshaphal = beanTajikVarshaphal;
    }

    public void setPlanetDataObjectTransit(PlanetData planetDataTransit) {
        this.planetDataTransit = planetDataTransit;
    }

    public PlanetData getPlanetDataObjectTransit() {
        return planetDataTransit;
    }
}
