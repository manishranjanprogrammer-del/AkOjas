package com.astrosagekundli.junit;

import junit.framework.TestCase;



import com.ojassoft.kpextension.CKpRefactorExtension;
import com.ojassoft.kpextension.GlobalVariablesKPExtension;

/**
 * This class is used to test KP Planet Significator
 * 
 * @author Bijendra(17-sep-13)
 * 
 */
public class KPPlanetSignificatorTest extends TestCase {

	CKpRefactorExtension kpr1;
	CKpRefactorExtension kpr2;
	CKpRefactorExtension kpr3;

	
	public void setUp() throws Exception {
		super.setUp();
		kpr1 = new CKpRefactorExtension(
				GlobalVariablesKPExtensionTest.PLANST_VALUES_PUNIT,
				GlobalVariablesKPExtensionTest.KP_CUSP_VALUES_PUNIT);
		kpr2 = new CKpRefactorExtension(
				GlobalVariablesKPExtensionTest.PLANST_VALUES_HUKUM,
				GlobalVariablesKPExtensionTest.KP_CUSP_VALUES_HUKUM);
		kpr3 = new CKpRefactorExtension(
				GlobalVariablesKPExtensionTest.PLANST_VALUES_NEERAJ,
				GlobalVariablesKPExtensionTest.KP_CUSP_VALUES_NEERAJ);
	}

	public void tearDown() throws Exception {
		super.tearDown();
		kpr1 = null;
		kpr2 = null;
		kpr3 = null;
	}

	public void testgetPlanetSignificationLevel1() {
		// KUNDLI FOR PUNIT
		assertEquals(
				kpr1.getPlanetSignificationLevel1(GlobalVariablesKPExtension.SUN),
				6);
		assertEquals(
				kpr1.getPlanetSignificationLevel1(GlobalVariablesKPExtension.MOON),
				12);
		assertEquals(
				kpr1.getPlanetSignificationLevel1(GlobalVariablesKPExtension.MARS),
				11);
		assertEquals(
				kpr1.getPlanetSignificationLevel1(GlobalVariablesKPExtension.MERCURY),
				10);
		assertEquals(
				kpr1.getPlanetSignificationLevel1(GlobalVariablesKPExtension.JUPITER),
				11);
		assertEquals(
				kpr1.getPlanetSignificationLevel1(GlobalVariablesKPExtension.VENUS),
				10);
		assertEquals(
				kpr1.getPlanetSignificationLevel1(GlobalVariablesKPExtension.SAT),
				5);

		// KUNDLI FOR HUKUM
		assertEquals(
				kpr2.getPlanetSignificationLevel1(GlobalVariablesKPExtension.SUN),
				4);
		assertEquals(
				kpr2.getPlanetSignificationLevel1(GlobalVariablesKPExtension.MOON),
				11);
		assertEquals(
				kpr2.getPlanetSignificationLevel1(GlobalVariablesKPExtension.MARS),
				11);
		assertEquals(
				kpr2.getPlanetSignificationLevel1(GlobalVariablesKPExtension.MERCURY),
				9);
		assertEquals(
				kpr2.getPlanetSignificationLevel1(GlobalVariablesKPExtension.JUPITER),
				2);
		assertEquals(
				kpr2.getPlanetSignificationLevel1(GlobalVariablesKPExtension.VENUS),
				9);
		assertEquals(
				kpr2.getPlanetSignificationLevel1(GlobalVariablesKPExtension.SAT),
				11);

		// KUNDLI FOR NEERAJ
		assertEquals(
				kpr3.getPlanetSignificationLevel1(GlobalVariablesKPExtension.SUN),
				5);
		assertEquals(
				kpr3.getPlanetSignificationLevel1(GlobalVariablesKPExtension.MOON),
				6);
		assertEquals(
				kpr3.getPlanetSignificationLevel1(GlobalVariablesKPExtension.MARS),
				6);
		assertEquals(
				kpr3.getPlanetSignificationLevel1(GlobalVariablesKPExtension.MERCURY),
				7);
		assertEquals(
				kpr3.getPlanetSignificationLevel1(GlobalVariablesKPExtension.JUPITER),
				10);
		assertEquals(
				kpr3.getPlanetSignificationLevel1(GlobalVariablesKPExtension.VENUS),
				10);
		assertEquals(
				kpr3.getPlanetSignificationLevel1(GlobalVariablesKPExtension.SAT),
				4);

	}

	public void testgetPlanetSignificationLevel2() {
		// KUNDLI FOR PUNIT
		assertEquals(
				kpr1.getPlanetSignificationLevel2(GlobalVariablesKPExtensionTest.PLANST_VALUES_PUNIT[GlobalVariablesKPExtension.SUN]),
				7);
		assertEquals(
				kpr1.getPlanetSignificationLevel2(GlobalVariablesKPExtensionTest.PLANST_VALUES_PUNIT[GlobalVariablesKPExtension.MOON]),
				12);
		assertEquals(
				kpr1.getPlanetSignificationLevel2(GlobalVariablesKPExtensionTest.PLANST_VALUES_PUNIT[GlobalVariablesKPExtension.MARS]),
				6);
		assertEquals(
				kpr1.getPlanetSignificationLevel2(GlobalVariablesKPExtensionTest.PLANST_VALUES_PUNIT[GlobalVariablesKPExtension.MERCURY]),
				6);
		assertEquals(
				kpr1.getPlanetSignificationLevel2(GlobalVariablesKPExtensionTest.PLANST_VALUES_PUNIT[GlobalVariablesKPExtension.JUPITER]),
				10);
		assertEquals(
				kpr1.getPlanetSignificationLevel2(GlobalVariablesKPExtensionTest.PLANST_VALUES_PUNIT[GlobalVariablesKPExtension.VENUS]),
				5);
		assertEquals(
				kpr1.getPlanetSignificationLevel2(GlobalVariablesKPExtensionTest.PLANST_VALUES_PUNIT[GlobalVariablesKPExtension.SAT]),
				11);

		// KUNDLI FOR HUKUM
		assertEquals(
				kpr2.getPlanetSignificationLevel2(GlobalVariablesKPExtensionTest.PLANST_VALUES_HUKUM[GlobalVariablesKPExtension.SUN]),
				12);
		assertEquals(
				kpr2.getPlanetSignificationLevel2(GlobalVariablesKPExtensionTest.PLANST_VALUES_HUKUM[GlobalVariablesKPExtension.MOON]),
				12);
		assertEquals(
				kpr2.getPlanetSignificationLevel2(GlobalVariablesKPExtensionTest.PLANST_VALUES_HUKUM[GlobalVariablesKPExtension.MARS]),
				12);
		assertEquals(
				kpr2.getPlanetSignificationLevel2(GlobalVariablesKPExtensionTest.PLANST_VALUES_HUKUM[GlobalVariablesKPExtension.MERCURY]),
				11);
		assertEquals(
				kpr2.getPlanetSignificationLevel2(GlobalVariablesKPExtensionTest.PLANST_VALUES_HUKUM[GlobalVariablesKPExtension.JUPITER]),
				9);
		assertEquals(
				kpr2.getPlanetSignificationLevel2(GlobalVariablesKPExtensionTest.PLANST_VALUES_HUKUM[GlobalVariablesKPExtension.VENUS]),
				12);
		assertEquals(
				kpr2.getPlanetSignificationLevel2(GlobalVariablesKPExtensionTest.PLANST_VALUES_HUKUM[GlobalVariablesKPExtension.SAT]),
				4);

		// KUNDLI FOR NEERAJ
		assertEquals(
				kpr3.getPlanetSignificationLevel2(GlobalVariablesKPExtensionTest.PLANST_VALUES_NEERAJ[GlobalVariablesKPExtension.SUN]),
				7);
		assertEquals(
				kpr3.getPlanetSignificationLevel2(GlobalVariablesKPExtensionTest.PLANST_VALUES_NEERAJ[GlobalVariablesKPExtension.MOON]),
				5);
		assertEquals(
				kpr3.getPlanetSignificationLevel2(GlobalVariablesKPExtensionTest.PLANST_VALUES_NEERAJ[GlobalVariablesKPExtension.MARS]),
				9);
		assertEquals(
				kpr3.getPlanetSignificationLevel2(GlobalVariablesKPExtensionTest.PLANST_VALUES_NEERAJ[GlobalVariablesKPExtension.MERCURY]),
				6);
		assertEquals(
				kpr3.getPlanetSignificationLevel2(GlobalVariablesKPExtensionTest.PLANST_VALUES_NEERAJ[GlobalVariablesKPExtension.JUPITER]),
				8);
		assertEquals(
				kpr3.getPlanetSignificationLevel2(GlobalVariablesKPExtensionTest.PLANST_VALUES_NEERAJ[GlobalVariablesKPExtension.VENUS]),
				8);
		assertEquals(
				kpr3.getPlanetSignificationLevel2(GlobalVariablesKPExtensionTest.PLANST_VALUES_NEERAJ[GlobalVariablesKPExtension.SAT]),
				9);

	}

	public void testgetPlanetSignificationLevel3() {
		// KUNDLI FOR PUNIT
		int[] level3SunPunit = kpr1
				.getPlanetSignificationLevel3(GlobalVariablesKPExtensionTest.PLANST_VALUES_PUNIT[GlobalVariablesKPExtension.SUN]);
		for (int i = 0; i < level3SunPunit.length; i++)
			assertTrue(level3SunPunit[i] == 10 || level3SunPunit[i] == 1);

		int[] level3MoonPunit = kpr1
				.getPlanetSignificationLevel3(GlobalVariablesKPExtensionTest.PLANST_VALUES_PUNIT[GlobalVariablesKPExtension.MOON]);
		for (int i = 0; i < level3MoonPunit.length; i++)
			assertTrue(level3MoonPunit[i] == 11);

		int[] level3MarsPunit = kpr1
				.getPlanetSignificationLevel3(GlobalVariablesKPExtensionTest.PLANST_VALUES_PUNIT[GlobalVariablesKPExtension.MARS]);
		for (int i = 0; i < level3MarsPunit.length; i++)
			assertTrue(level3MarsPunit[i] == 5 || level3MarsPunit[i] == 6);

		int[] level3MercuryPunit = kpr1
				.getPlanetSignificationLevel3(GlobalVariablesKPExtensionTest.PLANST_VALUES_PUNIT[GlobalVariablesKPExtension.MERCURY]);
		for (int i = 0; i < level3MercuryPunit.length; i++)
			assertTrue(level3MercuryPunit[i] == 4 || level3MercuryPunit[i] == 7);

		int[] level3JupiterPunit = kpr1
				.getPlanetSignificationLevel3(GlobalVariablesKPExtensionTest.PLANST_VALUES_PUNIT[GlobalVariablesKPExtension.JUPITER]);
		for (int i = 0; i < level3JupiterPunit.length; i++)
			assertTrue(level3JupiterPunit[i] == 5 || level3JupiterPunit[i] == 6);

		int[] level3VenusPunit = kpr1
				.getPlanetSignificationLevel3(GlobalVariablesKPExtensionTest.PLANST_VALUES_PUNIT[GlobalVariablesKPExtension.VENUS]);
		for (int i = 0; i < level3VenusPunit.length; i++)
			assertTrue(level3VenusPunit[i] == 4 || level3VenusPunit[i] == 7);

		int[] level3SaturnPunit = kpr1
				.getPlanetSignificationLevel3(GlobalVariablesKPExtensionTest.PLANST_VALUES_PUNIT[GlobalVariablesKPExtension.SAT]);
		for (int i = 0; i < level3SaturnPunit.length; i++)
			assertTrue(level3SaturnPunit[i] == 2 || level3SaturnPunit[i] == 9);

		// KUNDLI FOR HUKUM
		int[] level3SunHukum = kpr2
				.getPlanetSignificationLevel3(GlobalVariablesKPExtensionTest.PLANST_VALUES_HUKUM[GlobalVariablesKPExtension.SUN]);
		for (int i = 0; i < level3SunHukum.length; i++)
			assertTrue(level3SunHukum[i] == 7 || level3SunHukum[i] == 8);

		int[] level3MoonHukum = kpr2
				.getPlanetSignificationLevel3(GlobalVariablesKPExtensionTest.PLANST_VALUES_HUKUM[GlobalVariablesKPExtension.MOON]);
		for (int i = 0; i < level3MoonHukum.length; i++)
			assertTrue(level3MoonHukum[i] == 3 || level3MoonHukum[i] == 12);

		int[] level3MarsHukum = kpr2
				.getPlanetSignificationLevel3(GlobalVariablesKPExtensionTest.PLANST_VALUES_HUKUM[GlobalVariablesKPExtension.MARS]);
		for (int i = 0; i < level3MarsHukum.length; i++)
			assertTrue(level3MarsHukum[i] == 3 || level3MarsHukum[i] == 12);

		int[] level3MercuryHukum = kpr2
				.getPlanetSignificationLevel3(GlobalVariablesKPExtensionTest.PLANST_VALUES_HUKUM[GlobalVariablesKPExtension.MERCURY]);
		for (int i = 0; i < level3MercuryHukum.length; i++)
			assertTrue(level3MercuryHukum[i] == 6 || level3MercuryHukum[i] == 9);

		int[] level3JupiterHukum = kpr2
				.getPlanetSignificationLevel3(GlobalVariablesKPExtensionTest.PLANST_VALUES_HUKUM[GlobalVariablesKPExtension.JUPITER]);
		assertTrue(level3JupiterHukum.length == 0);

		int[] level3VenusHukum = kpr2
				.getPlanetSignificationLevel3(GlobalVariablesKPExtensionTest.PLANST_VALUES_HUKUM[GlobalVariablesKPExtension.VENUS]);
		for (int i = 0; i < level3VenusHukum.length; i++)
			assertTrue(level3VenusHukum[i] == 6 || level3VenusHukum[i] == 9);

		int[] level3SaturnHukum = kpr2
				.getPlanetSignificationLevel3(GlobalVariablesKPExtensionTest.PLANST_VALUES_HUKUM[GlobalVariablesKPExtension.SAT]);
		for (int i = 0; i < level3SaturnHukum.length; i++)
			assertTrue(level3SaturnHukum[i] == 3 || level3SaturnHukum[i] == 12);

		// KUNDLI FOR HUKUM
		int[] level3SunNeeraj = kpr3
				.getPlanetSignificationLevel3(GlobalVariablesKPExtensionTest.PLANST_VALUES_NEERAJ[GlobalVariablesKPExtension.SUN]);
		for (int i = 0; i < level3SunNeeraj.length; i++)
			assertTrue(level3SunNeeraj[i] == 9);

		int[] level3MoonNeeraj = kpr3
				.getPlanetSignificationLevel3(GlobalVariablesKPExtensionTest.PLANST_VALUES_NEERAJ[GlobalVariablesKPExtension.MOON]);
		for (int i = 0; i < level3MoonNeeraj.length; i++)
			assertTrue(level3MoonNeeraj[i] == 8 || level3MoonNeeraj[i] == 11);

		int[] level3MarsNeeraj = kpr3
				.getPlanetSignificationLevel3(GlobalVariablesKPExtensionTest.PLANST_VALUES_NEERAJ[GlobalVariablesKPExtension.MARS]);
		for (int i = 0; i < level3MarsHukum.length; i++)
			assertTrue(level3MarsNeeraj[i] == 8 || level3MarsNeeraj[i] == 11);

		int[] level3MercuryNeeraj = kpr3
				.getPlanetSignificationLevel3(GlobalVariablesKPExtensionTest.PLANST_VALUES_NEERAJ[GlobalVariablesKPExtension.MERCURY]);
		for (int i = 0; i < level3MercuryNeeraj.length; i++)
			assertTrue(level3MercuryNeeraj[i] == 10);

		int[] level3JupiterNeeraj = kpr3
				.getPlanetSignificationLevel3(GlobalVariablesKPExtensionTest.PLANST_VALUES_NEERAJ[GlobalVariablesKPExtension.JUPITER]);
		assertTrue(level3JupiterNeeraj.length == 0);

		int[] level3VenusNeeraj = kpr3
				.getPlanetSignificationLevel3(GlobalVariablesKPExtensionTest.PLANST_VALUES_NEERAJ[GlobalVariablesKPExtension.VENUS]);
		assertTrue(level3VenusNeeraj.length == 0);

		int[] level3SaturnNeeraj = kpr3
				.getPlanetSignificationLevel3(GlobalVariablesKPExtensionTest.PLANST_VALUES_NEERAJ[GlobalVariablesKPExtension.SAT]);
		assertTrue(level3SaturnNeeraj.length == 0);

	}

	public void testgetPlanetSignificationLevel4() {
		// KUNDLI FOR PUNIT
		int[] level4SunPunit = kpr1
				.getPlanetSignificationLevel4(GlobalVariablesKPExtension.SUN);
		for (int i = 0; i < level4SunPunit.length; i++)
			assertTrue(level4SunPunit[i] == 12);

		int[] level4MoonPunit = kpr1
				.getPlanetSignificationLevel4(GlobalVariablesKPExtension.MOON);
		for (int i = 0; i < level4MoonPunit.length; i++)
			assertTrue(level4MoonPunit[i] == 11);

		int[] level4MarsPunit = kpr1
				.getPlanetSignificationLevel4(GlobalVariablesKPExtension.MARS);
		for (int i = 0; i < level4MarsPunit.length; i++)
			assertTrue(level4MarsPunit[i] == 8 || level4MarsPunit[i] == 3);

		int[] level4MercuryPunit = kpr1
				.getPlanetSignificationLevel4(GlobalVariablesKPExtension.MERCURY);
		for (int i = 0; i < level4MercuryPunit.length; i++)
			assertTrue(level4MercuryPunit[i] == 10
					|| level4MercuryPunit[i] == 1);

		int[] level4JupiterPunit = kpr1
				.getPlanetSignificationLevel4(GlobalVariablesKPExtension.JUPITER);
		for (int i = 0; i < level4JupiterPunit.length; i++)
			assertTrue(level4JupiterPunit[i] == 4 || level4JupiterPunit[i] == 7);

		int[] level4VenusPunit = kpr1
				.getPlanetSignificationLevel4(GlobalVariablesKPExtension.VENUS);
		for (int i = 0; i < level4VenusPunit.length; i++)
			assertTrue(level4VenusPunit[i] == 2 || level4VenusPunit[i] == 9);

		int[] level4SatPunit = kpr1
				.getPlanetSignificationLevel4(GlobalVariablesKPExtension.SAT);
		for (int i = 0; i < level4SatPunit.length; i++)
			assertTrue(level4SatPunit[i] == 5 || level4SatPunit[i] == 6);

		// KUNDLI FOR HUKUM
		int[] level4SunHukum = kpr2
				.getPlanetSignificationLevel4(GlobalVariablesKPExtension.SUN);
		for (int i = 0; i < level4SunHukum.length; i++)
			assertTrue(level4SunHukum[i] == 2);

		int[] level4MoonHukum = kpr2
				.getPlanetSignificationLevel4(GlobalVariablesKPExtension.MOON);
		for (int i = 0; i < level4MoonHukum.length; i++)
			assertTrue(level4MoonHukum[i] == 1);

		int[] level4MarsHukum = kpr2
				.getPlanetSignificationLevel4(GlobalVariablesKPExtension.MARS);
		for (int i = 0; i < level4MarsHukum.length; i++)
			assertTrue(level4MarsHukum[i] == 5 || level4MarsHukum[i] == 10);

		int[] level4MercuryHukum = kpr2
				.getPlanetSignificationLevel4(GlobalVariablesKPExtension.MERCURY);
		for (int i = 0; i < level4MercuryHukum.length; i++)
			assertTrue(level4MercuryHukum[i] == 3
					|| level4MercuryHukum[i] == 12);

		int[] level4JupiterHukum = kpr2
				.getPlanetSignificationLevel4(GlobalVariablesKPExtension.JUPITER);
		for (int i = 0; i < level4JupiterHukum.length; i++)
			assertTrue(level4JupiterHukum[i] == 6 || level4JupiterHukum[i] == 9);

		int[] level4VenusHukum = kpr2
				.getPlanetSignificationLevel4(GlobalVariablesKPExtension.VENUS);
		for (int i = 0; i < level4VenusHukum.length; i++)
			assertTrue(level4VenusHukum[i] == 4 || level4VenusHukum[i] == 11);

		int[] level4SatHukum = kpr2
				.getPlanetSignificationLevel4(GlobalVariablesKPExtension.SAT);
		for (int i = 0; i < level4SatHukum.length; i++)
			assertTrue(level4SatHukum[i] == 7 || level4SatHukum[i] == 8);

		// KUNDLI FOR NEERAJ
		int[] level4SunNeeraj = kpr3
				.getPlanetSignificationLevel4(GlobalVariablesKPExtension.SUN);
		for (int i = 0; i < level4SunNeeraj.length; i++)
			assertTrue(level4SunNeeraj[i] == 10);

		int[] level4MoonNeeraj = kpr3
				.getPlanetSignificationLevel4(GlobalVariablesKPExtension.MOON);
		for (int i = 0; i < level4MoonNeeraj.length; i++)
			assertTrue(level4MoonNeeraj[i] == 9);

		int[] level4MarsNeeraj = kpr3
				.getPlanetSignificationLevel4(GlobalVariablesKPExtension.MARS);
		for (int i = 0; i < level4MarsNeeraj.length; i++)
			assertTrue(level4MarsNeeraj[i] == 1 || level4MarsNeeraj[i] == 6);

		int[] level4MercuryNeeraj = kpr3
				.getPlanetSignificationLevel4(GlobalVariablesKPExtension.MERCURY);
		for (int i = 0; i < level4MercuryNeeraj.length; i++)
			assertTrue(level4MercuryNeeraj[i] == 8
					|| level4MercuryNeeraj[i] == 11);

		int[] level4JupiterNeeraj = kpr3
				.getPlanetSignificationLevel4(GlobalVariablesKPExtension.JUPITER);
		for (int i = 0; i < level4JupiterNeeraj.length; i++)
			assertTrue(level4JupiterNeeraj[i] == 2
					|| level4JupiterNeeraj[i] == 5);

		int[] level4VenusNeeraj = kpr3
				.getPlanetSignificationLevel4(GlobalVariablesKPExtension.VENUS);
		for (int i = 0; i < level4VenusNeeraj.length; i++)
			assertTrue(level4VenusNeeraj[i] == 7 || level4VenusNeeraj[i] == 12);

		int[] level4SatNeeraj = kpr3
				.getPlanetSignificationLevel4(GlobalVariablesKPExtension.SAT);
		for (int i = 0; i < level4SatNeeraj.length; i++)
			assertTrue(level4SatNeeraj[i] == 3 || level4SatNeeraj[i] == 4);

	}

	// STRENGTH
	public void testgetPlanetStrength() {

		// PUNIT
		int[] plntStrengthPunit = kpr1.getPlanetStrength();
		assertTrue(plntStrengthPunit[0] == 1);// SUN
		assertTrue(plntStrengthPunit[1] == 1);// MOON
		assertTrue(plntStrengthPunit[2] == 1);// MARS
		assertTrue(plntStrengthPunit[3] == 0);// MERCURY
		assertTrue(plntStrengthPunit[4] == 0);// JUPITER
		assertTrue(plntStrengthPunit[5] == 0);// VENUS
		assertTrue(plntStrengthPunit[6] == 0);// SATURN

		// HUKUM
		int[] plntStrengthHukum = kpr2.getPlanetStrength();
		assertTrue(plntStrengthHukum[0] == 1);// SUN
		assertTrue(plntStrengthHukum[1] == 0);// MOON
		assertTrue(plntStrengthHukum[2] == 1);// MARS
		assertTrue(plntStrengthHukum[3] == 0);// MERCURY
		assertTrue(plntStrengthHukum[4] == 0);// JUPITER
		assertTrue(plntStrengthHukum[5] == 1);// VENUS
		assertTrue(plntStrengthHukum[6] == 0);// SATURN

		// NEERAJ
		int[] plntStrengthNeeraj = kpr3.getPlanetStrength();
		assertTrue(plntStrengthNeeraj[0] == 0);// SUN
		assertTrue(plntStrengthNeeraj[1] == 0);// MOON
		assertTrue(plntStrengthNeeraj[2] == 1);// MARS
		assertTrue(plntStrengthNeeraj[3] == 0);// MERCURY
		assertTrue(plntStrengthNeeraj[4] == 1);// JUPITER
		assertTrue(plntStrengthNeeraj[5] == 1);// VENUS
		assertTrue(plntStrengthNeeraj[6] == 0);// SATURN

	}

}
