package com.ojassoft.astrosage.jinterface;

public interface ICalculationProxy extends com.cslsoftware.enghoro.IEngHoro, com.cslsoftware.horo.IHoro, com.cslsoftware.purehoro.IPureHoro {
    /**
     * This is a main function of the class is responsible to
     * calculate kundli data
     *
     * @return boolean
     * @throws Exception
     */
    boolean calculate(ICalculationProxy _ICalculationProxy) throws Exception;

    boolean calculate() throws Exception;

    /**
     * To calculate lagna
     *
     * @return
     */
    double getLagna2();

}
