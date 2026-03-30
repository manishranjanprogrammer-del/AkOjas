package com.astrosagekundli.junit;

import junit.framework.TestCase;
import com.ojassoft.kpextension.CKBPlanetSignificator;
import com.ojassoft.kpextension.GlobalVariablesKPExtension;

public class KBPlanetSignificatorTest extends TestCase{
	
	CKBPlanetSignificator cPunit;
	CKBPlanetSignificator cNeeraj;
	CKBPlanetSignificator cHukum;
	
	protected void setUp() throws Exception {
		
		cPunit=new CKBPlanetSignificator(GlobalVariablesKPExtensionTest.PLANST_VALUES_PUNIT, GlobalVariablesKPExtensionTest.KP_CUSP_VALUES_PUNIT);
		cNeeraj=new CKBPlanetSignificator(GlobalVariablesKPExtensionTest.PLANST_VALUES_NEERAJ, GlobalVariablesKPExtensionTest.KP_CUSP_VALUES_NEERAJ);
		cHukum=new CKBPlanetSignificator(GlobalVariablesKPExtensionTest.PLANST_VALUES_HUKUM, GlobalVariablesKPExtensionTest.KP_CUSP_VALUES_HUKUM);
		
		super.setUp();
	}
	protected void tearDown() throws Exception {
		
		cPunit=null;
		cNeeraj=null;
		cHukum=null;
				
		super.tearDown();
	}
	
	public void testGetKB_PlanetSignificatorHukum() {
		
		System.out.print("\n---------Hukum-----------");
		//SUN
		int[] resultSun = cHukum.getKB_PlanetSignificator(GlobalVariablesKPExtension.SUN);
		assertNotNull(resultSun);
		assertTrue(resultSun.length == 1);
		
		System.out.print("\nSUN--");
		for(int i=0;i<resultSun.length;i++) {
			System.out.print(resultSun[i]+",");
		}
		for(int i=0;i<resultSun.length;i++) {
			assertTrue(resultSun[i]==10);
		}
		
		
		//Moon
		int[] resultMoon = cHukum.getKB_PlanetSignificator(GlobalVariablesKPExtension.MOON);
		assertNotNull(resultMoon);
		assertTrue(resultMoon.length == 1);
		
		System.out.print("\nMoon--");
		for(int i=0;i<resultMoon.length;i++) {
			System.out.print(resultMoon[i]+",");
		}
		for(int i=0;i<resultMoon.length;i++) {
			assertTrue(resultMoon[i]==12);
		}
		
		
		
		//MArs
		int[] resultMARS = cHukum.getKB_PlanetSignificator(GlobalVariablesKPExtension.MARS);
		assertNotNull(resultMARS);
		assertTrue(resultMARS.length == 3);
		
		System.out.print("\nMars--");
		for(int i=0;i<resultMARS.length;i++) {
			System.out.print(resultMARS[i]+",");
		}
		for(int i=0;i<resultMARS.length;i++) {
			assertTrue(resultMARS[i]==2 ||resultMARS[i]==8 ||resultMARS[i]==4);
		}
		
		
		
		//MERCURY
		int[] resultMERCURY = cHukum.getKB_PlanetSignificator(GlobalVariablesKPExtension.MERCURY);
		assertNotNull(resultMERCURY);
		assertTrue(resultMERCURY.length == 2);
		
		System.out.print("\nMer--");
		for(int i=0;i<resultMERCURY.length;i++) {
			System.out.print(resultMERCURY[i]+",");
		}
		for(int i=0;i<resultMERCURY.length;i++) {
			assertTrue(resultMERCURY[i]==2 ||resultMERCURY[i]==8);
		}
		
		
		
		//JUPITER
		int[] resultJUPITER = cHukum.getKB_PlanetSignificator(GlobalVariablesKPExtension.JUPITER);
		assertNotNull(resultJUPITER);
		assertTrue(resultJUPITER.length == 2);
		
		System.out.print("\nJup--");
		for(int i=0;i<resultJUPITER.length;i++) {
			System.out.print(resultJUPITER[i]+",");
		}
		for(int i=0;i<resultJUPITER.length;i++) {
			assertTrue(resultJUPITER[i]==2 ||resultJUPITER[i]==8);
		}
		
		
		
//		VENUS
		int[] resultVENUS = cHukum.getKB_PlanetSignificator(GlobalVariablesKPExtension.VENUS);
		assertNotNull(resultVENUS);
		assertTrue(resultVENUS.length == 1);
		
		System.out.print("\nVenus--");
		for(int i=0;i<resultVENUS.length;i++) {
			System.out.print(resultVENUS[i]+",");
		}
		for(int i=0;i<resultVENUS.length;i++) {
			assertTrue(resultVENUS[i]==4);
		}
		
		
		
//		SAT
		int[] resultSAT = cHukum.getKB_PlanetSignificator(GlobalVariablesKPExtension.SAT);
		assertNotNull(resultSAT);
		assertTrue(resultSAT.length == 4);
		
		System.out.print("\nSat--");
		for(int i=0;i<resultSAT.length;i++) {
			System.out.print(resultSAT[i]+",");
		}
		
		for(int i=0;i<resultSAT.length;i++) {
			assertTrue(resultSAT[i]==1 || resultSAT[i]==5 || resultSAT[i]==7 || resultSAT[i]==11);
		}
		
		
		
//		RAHU
		int[] resultRAHU = cHukum.getKB_PlanetSignificator(GlobalVariablesKPExtension.RAHU);
		assertNotNull(resultRAHU);
		assertTrue(resultRAHU.length == 3);
		
		System.out.print("\nRahu--");
		for(int i=0;i<resultRAHU.length;i++) {
			System.out.print(resultRAHU[i]+",");
		}
		
		for(int i=0;i<resultRAHU.length;i++) {
			assertTrue((resultRAHU[i]==3) || (resultRAHU[i]==6) || (resultRAHU[i]==9));
		}
		
//		KETU
		int[] resultKETU = cHukum.getKB_PlanetSignificator(GlobalVariablesKPExtension.KETU);
		assertNotNull(resultKETU);
		assertTrue(resultKETU.length == 4);
		
		System.out.print("\nKetu--");
		for(int i=0;i<resultKETU.length;i++) {
			System.out.print(resultKETU[i]+",");
		}
		for(int i=0;i<resultKETU.length;i++) {
			assertTrue(resultKETU[i]==12 || resultKETU[i]==3 || resultKETU[i]==6 || resultKETU[i]==9);
		}
	}
	public void testGetKB_PlanetSignificatorPunit() {
		
		System.out.print("\n---------Punit-----------");
		//SUN
		int[] resultSun = cPunit.getKB_PlanetSignificator(GlobalVariablesKPExtension.SUN);
		assertNotNull(resultSun);
		assertTrue(resultSun.length == 4);
		
		System.out.print("\nSUN--");
		for(int i=0;i<resultSun.length;i++) {
			System.out.print(resultSun[i]+",");
		}
		for(int i=0;i<resultSun.length;i++) {
			assertTrue(resultSun[i]==4 || resultSun[i]==10 || resultSun[i]==5 || resultSun[i]==11);
		}
		
		
		//Moon
		int[] resultMoon = cPunit.getKB_PlanetSignificator(GlobalVariablesKPExtension.MOON);
		assertNotNull(resultMoon);
		assertTrue(resultMoon.length == 1);
		
		System.out.print("\nMoon--");
		for(int i=0;i<resultMoon.length;i++) {
			System.out.print(resultMoon[i]+",");
		}
		for(int i=0;i<resultMoon.length;i++) {
			assertTrue(resultMoon[i]==12);
		}
		
		
		
		//MArs
		int[] resultMARS = cPunit.getKB_PlanetSignificator(GlobalVariablesKPExtension.MARS);
		assertNotNull(resultMARS);
		assertTrue(resultMARS.length == 2);
		
		System.out.print("\nMars--");
		for(int i=0;i<resultMARS.length;i++) {
			System.out.print(resultMARS[i]+",");
		}
		for(int i=0;i<resultMARS.length;i++) {
			assertTrue(resultMARS[i]==3 ||resultMARS[i]==9);
		}
		
		
		
		//MERCURY
		int[] resultMERCURY = cPunit.getKB_PlanetSignificator(GlobalVariablesKPExtension.MERCURY);
		assertNotNull(resultMERCURY);
		assertTrue(resultMERCURY.length == 2);
		
		System.out.print("\nMer--");
		for(int i=0;i<resultMERCURY.length;i++) {
			System.out.print(resultMERCURY[i]+",");
		}
		for(int i=0;i<resultMERCURY.length;i++) {
			assertTrue(resultMERCURY[i]==4 ||resultMERCURY[i]==10);
		}
		
		
		
		//JUPITER
		int[] resultJUPITER = cPunit.getKB_PlanetSignificator(GlobalVariablesKPExtension.JUPITER);
		assertNotNull(resultJUPITER);
		assertTrue(resultJUPITER.length == 2);
		
		System.out.print("\nJup--");
		for(int i=0;i<resultJUPITER.length;i++) {
			System.out.print(resultJUPITER[i]+",");
		}
		for(int i=0;i<resultJUPITER.length;i++) {
			assertTrue(resultJUPITER[i]==5 ||resultJUPITER[i]==11);
		}
		
		
		
//		VENUS
		int[] resultVENUS = cPunit.getKB_PlanetSignificator(GlobalVariablesKPExtension.VENUS);
		assertNotNull(resultVENUS);
		assertTrue(resultVENUS.length == 1);
		
		System.out.print("\nVenus--");
		for(int i=0;i<resultVENUS.length;i++) {
			System.out.print(resultVENUS[i]+",");
		}
		for(int i=0;i<resultVENUS.length;i++) {
			assertTrue(resultVENUS[i]==6);
		}
		
		
		
//		SAT
		int[] resultSAT = cPunit.getKB_PlanetSignificator(GlobalVariablesKPExtension.SAT);
		assertNotNull(resultSAT);
		assertTrue(resultSAT.length == 2);
		
		System.out.print("\nSat--");
		for(int i=0;i<resultSAT.length;i++) {
			System.out.print(resultSAT[i]+",");
		}
		
		for(int i=0;i<resultSAT.length;i++) {
			assertTrue(resultSAT[i]==2 || resultSAT[i]==8);
		}
		
		
		
//		RAHU
		int[] resultRAHU = cPunit.getKB_PlanetSignificator(GlobalVariablesKPExtension.RAHU);
		assertNotNull(resultRAHU);
		assertTrue(resultRAHU.length == 2);
		
		System.out.print("\nRahu--");
		for(int i=0;i<resultRAHU.length;i++) {
			System.out.print(resultRAHU[i]+",");
		}
		
		for(int i=0;i<resultRAHU.length;i++) {
			assertTrue((resultRAHU[i]==1) || (resultRAHU[i]==7));
		}
		
//		KETU
		int[] resultKETU = cPunit.getKB_PlanetSignificator(GlobalVariablesKPExtension.KETU);
		assertNotNull(resultKETU);
		assertTrue(resultKETU.length == 4);
		
		System.out.print("\nKetu--");
		for(int i=0;i<resultKETU.length;i++) {
			System.out.print(resultKETU[i]+",");
		}
		for(int i=0;i<resultKETU.length;i++) {
			assertTrue(resultKETU[i]==5 || resultKETU[i]==11 || resultKETU[i]==2 || resultKETU[i]==8);
		}
	
		
	}
	public void testGetKB_PlanetSignificatorNeeraj() {
		System.out.print("\n---------Neeraj-----------");
		//SUN
		int[] resultSun = cNeeraj.getKB_PlanetSignificator(GlobalVariablesKPExtension.SUN);
		assertNotNull(resultSun);
		assertTrue(resultSun.length == 1);
		
		System.out.print("\nSUN--");
		for(int i=0;i<resultSun.length;i++) {
			System.out.print(resultSun[i]+",");
		}
		for(int i=0;i<resultSun.length;i++) {
			assertTrue(resultSun[i]==8);
		}
		
		
		//Moon
		int[] resultMoon = cNeeraj.getKB_PlanetSignificator(GlobalVariablesKPExtension.MOON);
		assertNotNull(resultMoon);
		assertTrue(resultMoon.length == 1);
		
		System.out.print("\nMoon--");
		for(int i=0;i<resultMoon.length;i++) {
			System.out.print(resultMoon[i]+",");
		}
		for(int i=0;i<resultMoon.length;i++) {
			assertTrue(resultMoon[i]==8);
		}
		
		
		
		//MArs
		int[] resultMARS = cNeeraj.getKB_PlanetSignificator(GlobalVariablesKPExtension.MARS);
		assertNotNull(resultMARS);
		assertTrue(resultMARS.length == 5);
		
		System.out.print("\nMars--");
		for(int i=0;i<resultMARS.length;i++) {
			System.out.print(resultMARS[i]+",");
		}
		for(int i=0;i<resultMARS.length;i++) {
			assertTrue(resultMARS[i]==1 ||resultMARS[i]==4 ||resultMARS[i]==6 ||resultMARS[i]==7 ||resultMARS[i]==12);
		}
		
		
		
		//MERCURY
		int[] resultMERCURY = cNeeraj.getKB_PlanetSignificator(GlobalVariablesKPExtension.MERCURY);
		assertNotNull(resultMERCURY);
		assertTrue(resultMERCURY.length == 5);
		
		System.out.print("\nMer--");
		for(int i=0;i<resultMERCURY.length;i++) {
			System.out.print(resultMERCURY[i]+",");
		}
		for(int i=0;i<resultMERCURY.length;i++) {
			assertTrue(resultMERCURY[i]==1 ||resultMERCURY[i]==4 || resultMERCURY[i]==6 ||resultMERCURY[i]==7 ||resultMERCURY[i]==12);
		}
		
		
		
		//JUPITER
		int[] resultJUPITER = cNeeraj.getKB_PlanetSignificator(GlobalVariablesKPExtension.JUPITER);
		assertNotNull(resultJUPITER);
		assertTrue(resultJUPITER.length == 2);
		
		System.out.print("\nJup--");
		for(int i=0;i<resultJUPITER.length;i++) {
			System.out.print(resultJUPITER[i]+",");
		}
		for(int i=0;i<resultJUPITER.length;i++) {
			assertTrue(resultJUPITER[i]==5 ||resultJUPITER[i]==11);
		}
		
		
		
//		VENUS
		int[] resultVENUS = cNeeraj.getKB_PlanetSignificator(GlobalVariablesKPExtension.VENUS);
		assertNotNull(resultVENUS);
		assertTrue(resultVENUS.length == 3);
		
		System.out.print("\nVenus--");
		for(int i=0;i<resultVENUS.length;i++) {
			System.out.print(resultVENUS[i]+",");
		}
		for(int i=0;i<resultVENUS.length;i++) {
			assertTrue(resultVENUS[i]==3 || resultVENUS[i]==9 || resultVENUS[i]==10);
		}
		
		
		
//		SAT
		int[] resultSAT = cNeeraj.getKB_PlanetSignificator(GlobalVariablesKPExtension.SAT);
		assertNotNull(resultSAT);
		assertTrue(resultSAT.length == 3);
		
		System.out.print("\nSat--");
		for(int i=0;i<resultSAT.length;i++) {
			System.out.print(resultSAT[i]+",");
		}
		
		for(int i=0;i<resultSAT.length;i++) {
			assertTrue(resultSAT[i]==3 || resultSAT[i]==9 || resultSAT[i]==10);
		}
		
		
		
//		RAHU
		int[] resultRAHU = cNeeraj.getKB_PlanetSignificator(GlobalVariablesKPExtension.RAHU);
		assertNotNull(resultRAHU);
		assertTrue(resultRAHU.length == 1);
		
		System.out.print("\nRahu--");
		for(int i=0;i<resultRAHU.length;i++) {
			System.out.print(resultRAHU[i]+",");
		}
		
		for(int i=0;i<resultRAHU.length;i++) {
			assertTrue(resultRAHU[i]==2);
		}
		
//		KETU
		int[] resultKETU = cNeeraj.getKB_PlanetSignificator(GlobalVariablesKPExtension.KETU);
		assertNotNull(resultKETU);
		assertTrue(resultKETU.length == 3);
		
		System.out.print("\nKetu--");
		for(int i=0;i<resultKETU.length;i++) {
			System.out.print(resultKETU[i]+",");
		}
		for(int i=0;i<resultKETU.length;i++) {
			assertTrue(resultKETU[i]==3 || resultKETU[i]==9 || resultKETU[i]==10);
		}
	
		
	}
}
