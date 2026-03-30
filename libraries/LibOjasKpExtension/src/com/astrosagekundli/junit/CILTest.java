package com.astrosagekundli.junit;

import com.ojassoft.kpextension.CKpRefactorExtension;
import com.ojassoft.kpextension.GlobalVariablesKPExtension;

import junit.framework.TestCase;

public class CILTest extends TestCase {
	CKpRefactorExtension cPunit, cNeeraj, cHukum;

	@Override
	protected void setUp() throws Exception {
		cPunit = new CKpRefactorExtension(
				GlobalVariablesKPExtensionTest.PLANST_VALUES_PUNIT,
				GlobalVariablesKPExtensionTest.KP_CUSP_VALUES_PUNIT);
		cNeeraj = new CKpRefactorExtension(
				GlobalVariablesKPExtensionTest.PLANST_VALUES_NEERAJ,
				GlobalVariablesKPExtensionTest.KP_CUSP_VALUES_NEERAJ);
		cHukum = new CKpRefactorExtension(
				GlobalVariablesKPExtensionTest.PLANST_VALUES_HUKUM,
				GlobalVariablesKPExtensionTest.KP_CUSP_VALUES_HUKUM);
	}

	@Override
	protected void tearDown() throws Exception {
		cPunit = null;
		cNeeraj = null;
		cHukum = null;
	}

	public void testgetKB_CIL_Type1() {
		testgetKB_CIL_Type1_Punit();
		testgetKB_CIL_Type1_Neeraj();
		testgetKB_CIL_Type1_Hukum();
	}

	public void testgetKB_CIL_Type2() {
		testgetKB_CIL_Type2_Punit();
		testgetKB_CIL_Type2_Neeraj();
		testgetKB_CIL_Type2_Hukum();
	}

	public void testgetKB_CIL_Type3() {
		testgetKB_CIL_Type3_Punit();
		testgetKB_CIL_Type3_Neeraj();
		testgetKB_CIL_Type3_Hukum();
	}

	public void testgetKB_CIL_Type4() {
		testgetKB_CIL_Type4_Punit();
		testgetKB_CIL_Type4_Neeraj();
		testgetKB_CIL_Type4_Hukum();
	}

	public void testgetKB_CIL_Type1_Punit() {
		int[] cusp1 = cPunit.getKB_CIL_Type1(GlobalVariablesKPExtension.CUSP1);
		for (int i = 0; i < cusp1.length; i++)
			assertTrue(cusp1[i] == 2 || cusp1[i] == 6 || cusp1[i] == 8);

		int[] cusp2 = cPunit.getKB_CIL_Type1(GlobalVariablesKPExtension.CUSP2);
		for (int i = 0; i < cusp2.length; i++)
			assertTrue(cusp2[i] == 6);

		int[] cusp3 = cPunit.getKB_CIL_Type1(GlobalVariablesKPExtension.CUSP3);
		for (int i = 0; i < cusp3.length; i++)
			assertTrue(cusp3[i] == 2 || cusp3[i] == 6 || cusp3[i] == 8);

		int[] cusp4 = cPunit.getKB_CIL_Type1(GlobalVariablesKPExtension.CUSP4);
		for (int i = 0; i < cusp4.length; i++)
			assertTrue(cusp4[i] == 1 || cusp4[i] == 5 || cusp4[i] == 7
					|| cusp4[i] == 11);

		int[] cusp5 = cPunit.getKB_CIL_Type1(GlobalVariablesKPExtension.CUSP5);
		for (int i = 0; i < cusp5.length; i++)
			assertTrue(cusp5[i] == 2 || cusp5[i] == 4 || cusp5[i] == 8
					|| cusp5[i] == 10);

		int[] cusp6 = cPunit.getKB_CIL_Type1(GlobalVariablesKPExtension.CUSP6);
		for (int i = 0; i < cusp6.length; i++)
			assertTrue(cusp6[i] == 2 || cusp6[i] == 5 || cusp6[i] == 8
					|| cusp6[i] == 11);

		int[] cusp7 = cPunit.getKB_CIL_Type1(GlobalVariablesKPExtension.CUSP7);
		for (int i = 0; i < cusp7.length; i++)
			assertTrue(cusp7[i] == 2 || cusp7[i] == 6 || cusp7[i] == 8);

		int[] cusp8 = cPunit.getKB_CIL_Type1(GlobalVariablesKPExtension.CUSP8);
		for (int i = 0; i < cusp8.length; i++)
			assertTrue(cusp8[i] == 6);

		int[] cusp9 = cPunit.getKB_CIL_Type1(GlobalVariablesKPExtension.CUSP9);
		for (int i = 0; i < cusp9.length; i++)
			assertTrue(cusp9[i] == 2 || cusp9[i] == 6 || cusp9[i] == 8);

		int[] cusp10 = cPunit
				.getKB_CIL_Type1(GlobalVariablesKPExtension.CUSP10);

		for (int i = 0; i < cusp10.length; i++)
			assertTrue(cusp10[i] == 1 || cusp10[i] == 5 || cusp10[i] == 7
					|| cusp10[i] == 11);

		int[] cusp11 = cPunit
				.getKB_CIL_Type1(GlobalVariablesKPExtension.CUSP11);
		for (int i = 0; i < cusp11.length; i++)
			assertTrue(cusp11[i] == 2 || cusp11[i] == 4 || cusp11[i] == 8
					|| cusp11[i] == 10);

		int[] cusp12 = cPunit
				.getKB_CIL_Type1(GlobalVariablesKPExtension.CUSP12);

		for (int i = 0; i < cusp12.length; i++)
			assertTrue(cusp12[i] == 2 || cusp12[i] == 8 || cusp12[i] == 12);

	}

	public void testgetKB_CIL_Type2_Punit() {
		int[] cusp1 = cPunit.getKB_CIL_Type2(GlobalVariablesKPExtension.CUSP1);
		for (int i = 0; i < cusp1.length; i++)
			assertTrue(cusp1[i] == 6);

		int[] cusp2 = cPunit.getKB_CIL_Type2(GlobalVariablesKPExtension.CUSP2);
		if (cusp2 != null)
			for (int i = 0; i < cusp2.length; i++)
				assertTrue(cusp2[i] == 6);

		int[] cusp3 = cPunit.getKB_CIL_Type2(GlobalVariablesKPExtension.CUSP3);
		if (cusp3 != null)
			for (int i = 0; i < cusp3.length; i++)
				assertTrue(cusp3[i] == 2 || cusp3[i] == 8);

		int[] cusp4 = cPunit.getKB_CIL_Type2(GlobalVariablesKPExtension.CUSP4);
		if (cusp4 != null)
			for (int i = 0; i < cusp4.length; i++)
				assertTrue(cusp4[i] == 5 || cusp4[i] == 11);

		int[] cusp5 = cPunit.getKB_CIL_Type2(GlobalVariablesKPExtension.CUSP5);
		if (cusp5 != null)
			for (int i = 0; i < cusp5.length; i++)
				assertTrue(cusp5[i] == 2 || cusp5[i] == 8);

		int[] cusp6 = cPunit.getKB_CIL_Type2(GlobalVariablesKPExtension.CUSP6);
		if (cusp6 != null)
			for (int i = 0; i < cusp6.length; i++)
				assertTrue(cusp6[i] == 5 || cusp6[i] == 11);

		int[] cusp7 = cPunit.getKB_CIL_Type2(GlobalVariablesKPExtension.CUSP7);
		if (cusp7 != null)
			for (int i = 0; i < cusp7.length; i++)
				assertTrue(cusp7[i] == 6);

		int[] cusp8 = cPunit.getKB_CIL_Type2(GlobalVariablesKPExtension.CUSP8);
		if (cusp8 != null)
			for (int i = 0; i < cusp8.length; i++)
				assertTrue(cusp8[i] == 6);

		int[] cusp9 = cPunit.getKB_CIL_Type2(GlobalVariablesKPExtension.CUSP9);
		if (cusp9 != null)
			for (int i = 0; i < cusp9.length; i++)
				assertTrue(cusp9[i] == 2 || cusp9[i] == 8);

		int[] cusp10 = cPunit
				.getKB_CIL_Type2(GlobalVariablesKPExtension.CUSP10);
		if (cusp10 != null)
			for (int i = 0; i < cusp10.length; i++)
				assertTrue(cusp10[i] == 5 || cusp10[i] == 11);

		int[] cusp11 = cPunit
				.getKB_CIL_Type2(GlobalVariablesKPExtension.CUSP11);
		if (cusp11 != null)
			for (int i = 0; i < cusp11.length; i++)
				assertTrue(cusp11[i] == 2 || cusp11[i] == 8);

		int[] cusp12 = cPunit
				.getKB_CIL_Type2(GlobalVariablesKPExtension.CUSP12);
		assertNull(cusp12);

	}

	public void testgetKB_CIL_Type4_Punit() {
		int cusp1 = cPunit.getKB_CIL_Type4(GlobalVariablesKPExtension.CUSP1);
		assertEquals(5, cusp1);

		int cusp2 = cPunit.getKB_CIL_Type4(GlobalVariablesKPExtension.CUSP2);
		assertEquals(5, cusp2);

		int cusp3 = cPunit.getKB_CIL_Type4(GlobalVariablesKPExtension.CUSP3);
		assertEquals(11, cusp3);

		int cusp4 = cPunit.getKB_CIL_Type4(GlobalVariablesKPExtension.CUSP4);
		assertEquals(10, cusp4);

		int cusp5 = cPunit.getKB_CIL_Type4(GlobalVariablesKPExtension.CUSP5);
		assertEquals(11, cusp5);

		int cusp6 = cPunit.getKB_CIL_Type4(GlobalVariablesKPExtension.CUSP6);
		assertEquals(10, cusp6);

		int cusp7 = cPunit.getKB_CIL_Type4(GlobalVariablesKPExtension.CUSP7);
		assertEquals(5, cusp7);

		int cusp8 = cPunit.getKB_CIL_Type4(GlobalVariablesKPExtension.CUSP8);
		assertEquals(5, cusp8);

		int cusp9 = cPunit.getKB_CIL_Type4(GlobalVariablesKPExtension.CUSP9);
		assertEquals(11, cusp9);

		int cusp10 = cPunit.getKB_CIL_Type4(GlobalVariablesKPExtension.CUSP10);
		assertEquals(10, cusp10);

		int cusp11 = cPunit.getKB_CIL_Type4(GlobalVariablesKPExtension.CUSP11);
		assertEquals(11, cusp11);

		int cusp12 = cPunit.getKB_CIL_Type4(GlobalVariablesKPExtension.CUSP12);
		assertEquals(12, cusp12);
	}

	public void testgetKB_CIL_Type3_Punit() {
		int[] cusp1 = cPunit.getKB_CIL_Type3(GlobalVariablesKPExtension.CUSP1);
		assertNull(cusp1);

		int[] cusp2 = cPunit.getKB_CIL_Type3(GlobalVariablesKPExtension.CUSP2);

		for (int i = 0; i < cusp2.length; i++)
			assertTrue(cusp2[i] == 3 || cusp2[i] == 5 || cusp2[i] == 9
					|| cusp2[i] == 11);

		int[] cusp3 = cPunit.getKB_CIL_Type3(GlobalVariablesKPExtension.CUSP3);
		assertNull(cusp3);

		int[] cusp4 = cPunit.getKB_CIL_Type3(GlobalVariablesKPExtension.CUSP4);
		assertNull(cusp4);

		int[] cusp5 = cPunit.getKB_CIL_Type3(GlobalVariablesKPExtension.CUSP5);
		for (int i = 0; i < cusp5.length; i++)
			assertTrue(cusp5[i] == 4 || cusp5[i] == 6 || cusp5[i] == 10);

		int[] cusp6 = cPunit.getKB_CIL_Type3(GlobalVariablesKPExtension.CUSP6);
		for (int i = 0; i < cusp6.length; i++)
			assertTrue(cusp6[i] == 1 || cusp6[i] == 2 || cusp6[i] == 7
					|| cusp6[i] == 8);

		int[] cusp7 = cPunit.getKB_CIL_Type3(GlobalVariablesKPExtension.CUSP7);
		assertNull(cusp7);

		int[] cusp8 = cPunit.getKB_CIL_Type3(GlobalVariablesKPExtension.CUSP8);
		for (int i = 0; i < cusp8.length; i++)
			assertTrue(cusp8[i] == 3 || cusp8[i] == 5 || cusp8[i] == 9
					|| cusp8[i] == 11);

		int[] cusp9 = cPunit.getKB_CIL_Type3(GlobalVariablesKPExtension.CUSP9);
		assertNull(cusp9);

		int[] cusp10 = cPunit
				.getKB_CIL_Type3(GlobalVariablesKPExtension.CUSP10);
		assertNull(cusp10);

		int[] cusp11 = cPunit
				.getKB_CIL_Type3(GlobalVariablesKPExtension.CUSP11);
		for (int i = 0; i < cusp11.length; i++)
			assertTrue(cusp11[i] == 4 || cusp11[i] == 6 || cusp11[i] == 10);

		int[] cusp12 = cPunit
				.getKB_CIL_Type3(GlobalVariablesKPExtension.CUSP12);
		assertNull(cusp12);
	}

	public void testgetKB_CIL_Type1_Neeraj() {
		int[] cusp1 = cNeeraj.getKB_CIL_Type1(GlobalVariablesKPExtension.CUSP1);
		for (int i = 0; i < cusp1.length; i++)
			assertTrue(cusp1[i] == 5 || cusp1[i] == 11);

		int[] cusp2 = cNeeraj.getKB_CIL_Type1(GlobalVariablesKPExtension.CUSP2);
		for (int i = 0; i < cusp2.length; i++)
			assertTrue(cusp2[i] == 3 || cusp2[i] == 9 || cusp2[i] == 10);

		int[] cusp3 = cNeeraj.getKB_CIL_Type1(GlobalVariablesKPExtension.CUSP3);
		for (int i = 0; i < cusp3.length; i++)
			assertTrue(cusp3[i] == 2 || cusp3[i] == 8);

		int[] cusp4 = cNeeraj.getKB_CIL_Type1(GlobalVariablesKPExtension.CUSP4);
		for (int i = 0; i < cusp4.length; i++)
			assertTrue(cusp4[i] == 5 || cusp4[i] == 11);

		int[] cusp5 = cNeeraj.getKB_CIL_Type1(GlobalVariablesKPExtension.CUSP5);
		for (int i = 0; i < cusp5.length; i++)
			assertTrue(cusp5[i] == 2 || cusp5[i] == 3 || cusp5[i] == 9
					|| cusp5[i] == 10);

		int[] cusp6 = cNeeraj.getKB_CIL_Type1(GlobalVariablesKPExtension.CUSP6);
		for (int i = 0; i < cusp6.length; i++)
			assertTrue(cusp6[i] == 5 || cusp6[i] == 11);

		int[] cusp7 = cNeeraj.getKB_CIL_Type1(GlobalVariablesKPExtension.CUSP7);
		for (int i = 0; i < cusp7.length; i++)
			assertTrue(cusp7[i] == 5 || cusp7[i] == 11);

		int[] cusp8 = cNeeraj.getKB_CIL_Type1(GlobalVariablesKPExtension.CUSP8);
		for (int i = 0; i < cusp8.length; i++)
			assertTrue(cusp8[i] == 1 || cusp8[i] == 4 || cusp8[i] == 6
					|| cusp8[i] == 7 || cusp8[i] == 12);

		int[] cusp9 = cNeeraj.getKB_CIL_Type1(GlobalVariablesKPExtension.CUSP9);
		for (int i = 0; i < cusp9.length; i++)
			assertTrue(cusp9[i] == 2 || cusp9[i] == 8);

		int[] cusp10 = cNeeraj
				.getKB_CIL_Type1(GlobalVariablesKPExtension.CUSP10);

		for (int i = 0; i < cusp10.length; i++)
			assertTrue(cusp10[i] == 2 || cusp10[i] == 8);

		int[] cusp11 = cNeeraj
				.getKB_CIL_Type1(GlobalVariablesKPExtension.CUSP11);
		for (int i = 0; i < cusp11.length; i++)
			assertTrue(cusp11[i] == 2 || cusp11[i] == 3 || cusp11[i] == 9
					|| cusp11[i] == 10);

		int[] cusp12 = cNeeraj
				.getKB_CIL_Type1(GlobalVariablesKPExtension.CUSP12);

		for (int i = 0; i < cusp12.length; i++)
			assertTrue(cusp12[i] == 5 || cusp12[i] == 11);

	}

	public void testgetKB_CIL_Type2_Neeraj() {
		int[] cusp1 = cNeeraj.getKB_CIL_Type2(GlobalVariablesKPExtension.CUSP1);
		assertNull(cusp1);

		int[] cusp2 = cNeeraj.getKB_CIL_Type2(GlobalVariablesKPExtension.CUSP2);
		assertNull(cusp2);

		int[] cusp3 = cNeeraj.getKB_CIL_Type2(GlobalVariablesKPExtension.CUSP3);
		if (cusp3 != null)
			for (int i = 0; i < cusp3.length; i++)
				assertTrue(cusp3[i] == 2);

		int[] cusp4 = cNeeraj.getKB_CIL_Type2(GlobalVariablesKPExtension.CUSP4);
		assertNull(cusp4);

		int[] cusp5 = cNeeraj.getKB_CIL_Type2(GlobalVariablesKPExtension.CUSP5);
		if (cusp5 != null)
			for (int i = 0; i < cusp5.length; i++)
				assertTrue(cusp5[i] == 2);

		int[] cusp6 = cNeeraj.getKB_CIL_Type2(GlobalVariablesKPExtension.CUSP6);
		assertNull(cusp6);

		int[] cusp7 = cNeeraj.getKB_CIL_Type2(GlobalVariablesKPExtension.CUSP7);
		assertNull(cusp7);

		int[] cusp8 = cNeeraj.getKB_CIL_Type2(GlobalVariablesKPExtension.CUSP8);
		if (cusp8 != null)
			for (int i = 0; i < cusp8.length; i++)
				assertTrue(cusp8[i] == 1 || cusp8[i] == 4 || cusp8[i] == 6
						|| cusp8[i] == 7 || cusp8[i] == 12);

		int[] cusp9 = cNeeraj.getKB_CIL_Type2(GlobalVariablesKPExtension.CUSP9);
		if (cusp9 != null)
			for (int i = 0; i < cusp9.length; i++)
				assertTrue(cusp9[i] == 2);

		int[] cusp10 = cNeeraj
				.getKB_CIL_Type2(GlobalVariablesKPExtension.CUSP10);
		if (cusp10 != null)
			for (int i = 0; i < cusp10.length; i++)
				assertTrue(cusp10[i] == 2);

		int[] cusp11 = cNeeraj
				.getKB_CIL_Type2(GlobalVariablesKPExtension.CUSP11);
		if (cusp11 != null)
			for (int i = 0; i < cusp11.length; i++)
				assertTrue(cusp11[i] == 2);

		int[] cusp12 = cNeeraj
				.getKB_CIL_Type2(GlobalVariablesKPExtension.CUSP12);
		assertNull(cusp12);

	}

	public void testgetKB_CIL_Type3_Neeraj() {
		int[] cusp1 = cNeeraj.getKB_CIL_Type3(GlobalVariablesKPExtension.CUSP1);
		if (cusp1 != null)
			for (int i = 0; i < cusp1.length; i++)
				assertTrue(cusp1[i] == 8);

		int[] cusp2 = cNeeraj.getKB_CIL_Type3(GlobalVariablesKPExtension.CUSP2);
		if (cusp2 != null)
			for (int i = 0; i < cusp2.length; i++)
				assertTrue(cusp2[i] == 3 || cusp2[i] == 5 || cusp2[i] == 9
						|| cusp2[i] == 10 || cusp2[i] == 11);

		int[] cusp3 = cNeeraj.getKB_CIL_Type3(GlobalVariablesKPExtension.CUSP3);
		assertNull(cusp3);

		int[] cusp4 = cNeeraj.getKB_CIL_Type3(GlobalVariablesKPExtension.CUSP4);
		if (cusp4 != null)
			for (int i = 0; i < cusp4.length; i++)
				assertTrue(cusp4[i] == 8);

		int[] cusp5 = cNeeraj.getKB_CIL_Type3(GlobalVariablesKPExtension.CUSP5);
		assertNull(cusp5);

		int[] cusp6 = cNeeraj.getKB_CIL_Type3(GlobalVariablesKPExtension.CUSP6);
		if (cusp6 != null)
			for (int i = 0; i < cusp6.length; i++)
				assertTrue(cusp6[i] == 8);

		int[] cusp7 = cNeeraj.getKB_CIL_Type3(GlobalVariablesKPExtension.CUSP7);
		if (cusp7 != null)
			for (int i = 0; i < cusp7.length; i++)
				assertTrue(cusp7[i] == 8);

		int[] cusp8 = cNeeraj.getKB_CIL_Type3(GlobalVariablesKPExtension.CUSP8);
		assertNull(cusp8);

		int[] cusp9 = cNeeraj.getKB_CIL_Type3(GlobalVariablesKPExtension.CUSP9);
		assertNull(cusp9);

		int[] cusp10 = cNeeraj
				.getKB_CIL_Type3(GlobalVariablesKPExtension.CUSP10);
		assertNull(cusp10);

		int[] cusp11 = cNeeraj
				.getKB_CIL_Type3(GlobalVariablesKPExtension.CUSP11);
		assertNull(cusp11);

		int[] cusp12 = cNeeraj
				.getKB_CIL_Type3(GlobalVariablesKPExtension.CUSP12);
		if (cusp12 != null)
			for (int i = 0; i < cusp12.length; i++)
				assertTrue(cusp12[i] == 8);

	}

	public void testgetKB_CIL_Type4_Neeraj() {
		int cusp1 = cNeeraj.getKB_CIL_Type4(GlobalVariablesKPExtension.CUSP1);
		assertEquals(7, cusp1);

		int cusp2 = cNeeraj.getKB_CIL_Type4(GlobalVariablesKPExtension.CUSP2);
		assertEquals(7, cusp2);

		int cusp3 = cNeeraj.getKB_CIL_Type4(GlobalVariablesKPExtension.CUSP3);
		assertEquals(10, cusp3);

		int cusp4 = cNeeraj.getKB_CIL_Type4(GlobalVariablesKPExtension.CUSP4);
		assertEquals(7, cusp4);

		int cusp5 = cNeeraj.getKB_CIL_Type4(GlobalVariablesKPExtension.CUSP5);
		assertEquals(10, cusp5);

		int cusp6 = cNeeraj.getKB_CIL_Type4(GlobalVariablesKPExtension.CUSP6);
		assertEquals(7, cusp6);

		int cusp7 = cNeeraj.getKB_CIL_Type4(GlobalVariablesKPExtension.CUSP7);
		assertEquals(7, cusp7);

		int cusp8 = cNeeraj.getKB_CIL_Type4(GlobalVariablesKPExtension.CUSP8);
		assertEquals(6, cusp8);

		int cusp9 = cNeeraj.getKB_CIL_Type4(GlobalVariablesKPExtension.CUSP9);
		assertEquals(10, cusp9);

		int cusp10 = cNeeraj.getKB_CIL_Type4(GlobalVariablesKPExtension.CUSP10);
		assertEquals(10, cusp10);

		int cusp11 = cNeeraj.getKB_CIL_Type4(GlobalVariablesKPExtension.CUSP11);
		assertEquals(10, cusp11);

		int cusp12 = cNeeraj.getKB_CIL_Type4(GlobalVariablesKPExtension.CUSP12);
		assertEquals(7, cusp12);
	}

	public void testgetKB_CIL_Type1_Hukum() {
		int[] cusp1 = cHukum.getKB_CIL_Type1(GlobalVariablesKPExtension.CUSP1);
		for (int i = 0; i < cusp1.length; i++)
			assertTrue(cusp1[i] == 2 || cusp1[i] == 4 || cusp1[i] == 8);

		int[] cusp2 = cHukum.getKB_CIL_Type1(GlobalVariablesKPExtension.CUSP2);
		assertNull(cusp2);

		int[] cusp3 = cHukum.getKB_CIL_Type1(GlobalVariablesKPExtension.CUSP3);
		for (int i = 0; i < cusp3.length; i++)
			assertTrue(cusp3[i] == 1 || cusp3[i] == 5 || cusp3[i] == 7
					|| cusp3[i] == 11 || cusp3[i] == 12);

		int[] cusp4 = cHukum.getKB_CIL_Type1(GlobalVariablesKPExtension.CUSP4);
		for (int i = 0; i < cusp4.length; i++)
			assertTrue(cusp4[i] == 3 || cusp4[i] == 6 || cusp4[i] == 9);

		int[] cusp5 = cHukum.getKB_CIL_Type1(GlobalVariablesKPExtension.CUSP5);
		for (int i = 0; i < cusp5.length; i++)
			assertTrue(cusp5[i] == 2 || cusp5[i] == 4 || cusp5[i] == 8);

		int[] cusp6 = cHukum.getKB_CIL_Type1(GlobalVariablesKPExtension.CUSP6);
		for (int i = 0; i < cusp6.length; i++)
			assertTrue(cusp6[i] == 1 || cusp6[i] == 5 || cusp6[i] == 7
					|| cusp6[i] == 11 || cusp6[i] == 12);

		int[] cusp7 = cHukum.getKB_CIL_Type1(GlobalVariablesKPExtension.CUSP7);
		for (int i = 0; i < cusp7.length; i++)
			assertTrue(cusp7[i] == 2 || cusp7[i] == 4 || cusp7[i] == 8);

		int[] cusp8 = cHukum.getKB_CIL_Type1(GlobalVariablesKPExtension.CUSP8);
		assertNull(cusp8);

		int[] cusp9 = cHukum.getKB_CIL_Type1(GlobalVariablesKPExtension.CUSP9);
		for (int i = 0; i < cusp9.length; i++)
			assertTrue(cusp9[i] == 1 || cusp9[i] == 5 || cusp9[i] == 7
					|| cusp9[i] == 11 || cusp9[i] == 12);

		int[] cusp10 = cHukum
				.getKB_CIL_Type1(GlobalVariablesKPExtension.CUSP10);

		for (int i = 0; i < cusp10.length; i++)
			assertTrue(cusp10[i] == 1 || cusp10[i] == 5 || cusp10[i] == 7
					|| cusp10[i] == 11 || cusp10[i] == 4);

		int[] cusp11 = cHukum
				.getKB_CIL_Type1(GlobalVariablesKPExtension.CUSP11);
		for (int i = 0; i < cusp11.length; i++)
			assertTrue(cusp11[i] == 2 || cusp11[i] == 4 || cusp11[i] == 8);

		int[] cusp12 = cHukum
				.getKB_CIL_Type1(GlobalVariablesKPExtension.CUSP12);

		for (int i = 0; i < cusp12.length; i++)
			assertTrue(cusp12[i] == 2 || cusp12[i] == 8 || cusp12[i] == 12);

	}

	public void testgetKB_CIL_Type2_Hukum() {
		int[] cusp1 = cHukum.getKB_CIL_Type2(GlobalVariablesKPExtension.CUSP1);
		if (cusp1 != null)
			for (int i = 0; i < cusp1.length; i++)
				assertTrue(cusp1[i] == 2 || cusp1[i] == 8);

		int[] cusp2 = cHukum.getKB_CIL_Type2(GlobalVariablesKPExtension.CUSP2);
		assertNull(cusp2);

		int[] cusp3 = cHukum.getKB_CIL_Type2(GlobalVariablesKPExtension.CUSP3);
		if (cusp3 != null)
			for (int i = 0; i < cusp3.length; i++)
				assertTrue(cusp3[i] == 1 || cusp3[i] == 5 || cusp3[i] == 7
						|| cusp3[i] == 11);

		int[] cusp4 = cHukum.getKB_CIL_Type2(GlobalVariablesKPExtension.CUSP4);
		assertNull(cusp4);

		int[] cusp5 = cHukum.getKB_CIL_Type2(GlobalVariablesKPExtension.CUSP5);
		if (cusp5 != null)
			for (int i = 0; i < cusp5.length; i++)
				assertTrue(cusp5[i] == 2 || cusp5[i] == 8);

		int[] cusp6 = cHukum.getKB_CIL_Type2(GlobalVariablesKPExtension.CUSP6);
		if (cusp6 != null)
			for (int i = 0; i < cusp6.length; i++)
				assertTrue(cusp6[i] == 1 || cusp6[i] == 5 || cusp6[i] == 7
						|| cusp6[i] == 11);

		int[] cusp7 = cHukum.getKB_CIL_Type2(GlobalVariablesKPExtension.CUSP7);
		if (cusp7 != null)
			for (int i = 0; i < cusp7.length; i++)
				assertTrue(cusp7[i] == 2 || cusp7[i] == 8);

		int[] cusp8 = cHukum.getKB_CIL_Type2(GlobalVariablesKPExtension.CUSP8);
		assertNull(cusp8);

		int[] cusp9 = cHukum.getKB_CIL_Type2(GlobalVariablesKPExtension.CUSP9);
		if (cusp9 != null)
			for (int i = 0; i < cusp9.length; i++)
				assertTrue(cusp9[i] == 1 || cusp9[i] == 5 || cusp9[i] == 7
						|| cusp9[i] == 11);

		int[] cusp10 = cHukum
				.getKB_CIL_Type2(GlobalVariablesKPExtension.CUSP10);
		if (cusp10 != null)
			for (int i = 0; i < cusp10.length; i++)
				assertTrue(cusp10[i] == 1 || cusp10[i] == 5 || cusp10[i] == 7
						|| cusp10[i] == 11);

		int[] cusp11 = cHukum
				.getKB_CIL_Type2(GlobalVariablesKPExtension.CUSP11);
		if (cusp11 != null)
			for (int i = 0; i < cusp11.length; i++)
				assertTrue(cusp11[i] == 2 || cusp11[i] == 8);

		int[] cusp12 = cHukum
				.getKB_CIL_Type2(GlobalVariablesKPExtension.CUSP12);
		if (cusp12 != null)
			for (int i = 0; i < cusp12.length; i++)
				assertTrue(cusp12[i] == 2 || cusp12[i] == 8);

	}

	public void testgetKB_CIL_Type3_Hukum() {
		int[] cusp1 = cHukum.getKB_CIL_Type3(GlobalVariablesKPExtension.CUSP1);
		if (cusp1 != null)
			for (int i = 0; i < cusp1.length; i++)
				assertTrue(cusp1[i] == 3 || cusp1[i] == 6 || cusp1[i] == 9
						|| cusp1[i] == 10);

		int[] cusp2 = cHukum.getKB_CIL_Type3(GlobalVariablesKPExtension.CUSP2);
		if (cusp2 != null)
			for (int i = 0; i < cusp2.length; i++)
				assertTrue(cusp2[i] == 1 || cusp2[i] == 5 || cusp2[i] == 7
						|| cusp2[i] == 11 || cusp2[i] == 12);

		int[] cusp3 = cHukum.getKB_CIL_Type3(GlobalVariablesKPExtension.CUSP3);
		assertNull(cusp3);

		int[] cusp4 = cHukum.getKB_CIL_Type3(GlobalVariablesKPExtension.CUSP4);
		assertNull(cusp4);

		int[] cusp5 = cHukum.getKB_CIL_Type3(GlobalVariablesKPExtension.CUSP5);
		if (cusp5 != null)
			for (int i = 0; i < cusp5.length; i++)
				assertTrue(cusp5[i] == 3 || cusp5[i] == 6 || cusp5[i] == 9
						|| cusp5[i] == 10);

		int[] cusp6 = cHukum.getKB_CIL_Type3(GlobalVariablesKPExtension.CUSP6);
		assertNull(cusp6);

		int[] cusp7 = cHukum.getKB_CIL_Type3(GlobalVariablesKPExtension.CUSP7);
		if (cusp7 != null)
			for (int i = 0; i < cusp7.length; i++)
				assertTrue(cusp7[i] == 3 || cusp7[i] == 6 || cusp7[i] == 9
						|| cusp7[i] == 10);

		int[] cusp8 = cHukum.getKB_CIL_Type3(GlobalVariablesKPExtension.CUSP8);
		if (cusp8 != null)
			for (int i = 0; i < cusp2.length; i++)
				assertTrue(cusp8[i] == 1 || cusp8[i] == 5 || cusp8[i] == 7
						|| cusp8[i] == 11 || cusp8[i] == 12);

		int[] cusp9 = cHukum.getKB_CIL_Type3(GlobalVariablesKPExtension.CUSP9);
		assertNull(cusp9);

		int[] cusp10 = cHukum
				.getKB_CIL_Type3(GlobalVariablesKPExtension.CUSP10);
		assertNull(cusp10);

		int[] cusp11 = cHukum
				.getKB_CIL_Type3(GlobalVariablesKPExtension.CUSP11);
		if (cusp11 != null)
			for (int i = 0; i < cusp11.length; i++)
				assertTrue(cusp11[i] == 3 || cusp11[i] == 6 || cusp11[i] == 9
						|| cusp11[i] == 10);

		int[] cusp12 = cHukum
				.getKB_CIL_Type3(GlobalVariablesKPExtension.CUSP12);
		assertNull(cusp12);

	}

	public void testgetKB_CIL_Type4_Hukum() {
		int cusp1 = cHukum.getKB_CIL_Type4(GlobalVariablesKPExtension.CUSP1);
		assertEquals(11, cusp1);

		int cusp2 = cHukum.getKB_CIL_Type4(GlobalVariablesKPExtension.CUSP2);
		assertEquals(9, cusp2);

		int cusp3 = cHukum.getKB_CIL_Type4(GlobalVariablesKPExtension.CUSP3);
		assertEquals(4, cusp3);

		int cusp4 = cHukum.getKB_CIL_Type4(GlobalVariablesKPExtension.CUSP4);
		assertEquals(9, cusp4);

		int cusp5 = cHukum.getKB_CIL_Type4(GlobalVariablesKPExtension.CUSP5);
		assertEquals(11, cusp5);

		int cusp6 = cHukum.getKB_CIL_Type4(GlobalVariablesKPExtension.CUSP6);
		assertEquals(4, cusp6);

		int cusp7 = cHukum.getKB_CIL_Type4(GlobalVariablesKPExtension.CUSP7);
		assertEquals(11, cusp7);

		int cusp8 = cHukum.getKB_CIL_Type4(GlobalVariablesKPExtension.CUSP8);
		assertEquals(9, cusp8);

		int cusp9 = cHukum.getKB_CIL_Type4(GlobalVariablesKPExtension.CUSP9);
		assertEquals(4, cusp9);

		int cusp10 = cHukum.getKB_CIL_Type4(GlobalVariablesKPExtension.CUSP10);
		assertEquals(4, cusp10);

		int cusp11 = cHukum.getKB_CIL_Type4(GlobalVariablesKPExtension.CUSP11);
		assertEquals(11, cusp11);

		int cusp12 = cHukum.getKB_CIL_Type4(GlobalVariablesKPExtension.CUSP12);
		assertEquals(11, cusp12);
	}
}
