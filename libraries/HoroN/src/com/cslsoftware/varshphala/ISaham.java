package com.cslsoftware.varshphala;

public interface ISaham {

	/**
	 * Insert the method's description here.
	 * Creation date: (25/03/2003 12:15:09 PM)
	 */
	public abstract void calculateSaham(double planet1, double planet2,
			double planet3, int sahamNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (25/03/2003 12:15:09 PM)
	 */
	public abstract void create(double asc, double sun, double moon,
			double mars, double mercury, double jupiter, double venus,
			double saturn, double bhav1, double bhav2, double bhav3,
			double bhav4, double bhav5, double bhav6, double bhav7,
			double bhav8, double bhav9, double bhav10, double bhav11,
			double bhav12, double varshparveshTime, double sunriseTime,
			double sunsetTime);

	/**
	 * Insert the method's description here.
	 * Creation date: (25/03/2003 12:15:09 PM)
	 */
	public abstract void create(int ascNo, int planetInRashi[], double asc,
			double plnt[], double f2[], double varshparveshTime,
			double sunriseTime, double sunsetTime);

	/**
	 * Insert the method's description here.
	 * Creation date: (25/03/2003 12:15:09 PM)
	 */
	public abstract double getLordDegree(int houseNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (25/03/2003 12:15:09 PM)
	 */
	public abstract double[] getSaham();

	/**
	 * Insert the method's description here.
	 * Creation date: (25/03/2003 12:15:09 PM)
	 */
	public abstract double getSunMoonSignLordDeg(int planetNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (25/03/2003 12:15:09 PM)
	 */
	public abstract void initializeSaham();

	/**
	 * Insert the method's description here.
	 * Creation date: (25/03/2003 12:15:09 PM)
	 */
	public abstract boolean isItDayvarshparvesh();

}