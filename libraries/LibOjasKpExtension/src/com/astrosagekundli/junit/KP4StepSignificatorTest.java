package com.astrosagekundli.junit;

import com.ojassoft.kpextension.BeanKP4Step;
import com.ojassoft.kpextension.CKp4StepCalculation;
import com.ojassoft.kpextension.GlobalVariablesKPExtension;
import com.ojassoft.kpextension.KeyValue4Step;

import junit.framework.TestCase;

public class KP4StepSignificatorTest extends TestCase{
	CKp4StepCalculation cPunit;
	CKp4StepCalculation cNeeraj;
	CKp4StepCalculation cHukum;
	
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		
		cPunit=new CKp4StepCalculation(GlobalVariablesKPExtensionTest.PLANST_VALUES_PUNIT, GlobalVariablesKPExtensionTest.KP_CUSP_VALUES_PUNIT);
		cPunit.calculate();
				
		cNeeraj=new CKp4StepCalculation(GlobalVariablesKPExtensionTest.PLANST_VALUES_NEERAJ, GlobalVariablesKPExtensionTest.KP_CUSP_VALUES_NEERAJ);
		cNeeraj.calculate();
		
		cHukum=new CKp4StepCalculation(GlobalVariablesKPExtensionTest.PLANST_VALUES_HUKUM, GlobalVariablesKPExtensionTest.KP_CUSP_VALUES_HUKUM);
		cHukum.calculate();
		
		super.setUp();
	}
	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		cPunit=null;
		cNeeraj=null;
		cHukum=null;
				
		super.tearDown();
	}
	public void testget4StepType1()
	{		
		
		get4StepType1Punit();
		get4StepType1Neeraj();
		get4StepType1Hukum();
		
	}
	
	public void testget4StepType2()
	{
		get4StepType2Punit();
		get4StepType2Neeraj();		
		get4StepType2Hukum();
				
		
	}
	public void testget4StepType3()
	{
		get4StepType3Punit();
		get4StepType3Neeraj();
		get4StepType3Hukum();
	}
	public void testget4StepType4()
	{
		get4StepType4Punit();
		get4StepType4Neeraj();		
		get4StepType4Hukum();
		
	}
	
	/******************TEST CASE FOR NEERAJ******************************/
	public void get4StepType1Neeraj()
	{
		//SUN
		BeanKP4Step cNeerajType1Sun=cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.SUN);		
		assertTrue(!cNeerajType1Sun.getKp4Step1().isPlanetStrong());
		assertNull(cNeerajType1Sun.getKp4Step1().getPlanetStepValues());
		assertEquals( -1,cNeerajType1Sun.getKp4Step1().getPlanetCuspConjunction());
		assertEquals(0,cNeerajType1Sun.getKp4Step1().getPlanetConjunction().size());
		assertNull(cNeerajType1Sun.getKp4Step1().getCuspAspect());	
		assertEquals(0,cNeerajType1Sun.getKp4Step1().getPlanetAspect().size());
		
		

		//MOON
		BeanKP4Step cNeerajType1Moon=cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.MOON);		
		assertTrue(!cNeerajType1Moon.getKp4Step1().isPlanetStrong());
		assertNull(cNeerajType1Moon.getKp4Step1().getPlanetStepValues());
		assertEquals( -1,cNeerajType1Moon.getKp4Step1().getPlanetCuspConjunction());
		assertEquals(0,cNeerajType1Moon.getKp4Step1().getPlanetConjunction().size());
		assertNull(cNeerajType1Moon.getKp4Step1().getCuspAspect());	
		assertEquals(0,cNeerajType1Moon.getKp4Step1().getPlanetAspect().size());
		
		//MARS
		BeanKP4Step cNeerajType1Mars=cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.MARS);		
		assertTrue(cNeerajType1Mars.getKp4Step1().isPlanetStrong());
		assertTrue(!cNeerajType1Mars.getKp4Step1().isPlanetInItsOwnStar());
		assertTrue(!cNeerajType1Mars.getKp4Step1().isPlanetStarLordOfOtherPlanet());
		
		for(int i=0;i<cNeerajType1Mars.getKp4Step1().getPlanetStepValues().length;i++)
			assertTrue(cNeerajType1Mars.getKp4Step1().getPlanetStepValues()[i]==1||
					cNeerajType1Mars.getKp4Step1().getPlanetStepValues()[i]==9);
		
		assertEquals( -1,cNeerajType1Mars.getKp4Step1().getPlanetCuspConjunction());	
		
		assertEquals(GlobalVariablesKPExtension.SAT,cNeerajType1Mars.getKp4Step1().getPlanetConjunction().get(0).getKey());
		for(int i=0;i<cNeerajType1Mars.getKp4Step1().getPlanetConjunction().get(0).getValues().length;i++)
			assertTrue(cNeerajType1Mars.getKp4Step1().getPlanetConjunction().get(0).getValues()[i]==3||
					cNeerajType1Mars.getKp4Step1().getPlanetConjunction().get(0).getValues()[i]==9);
		
		assertNull(cNeerajType1Mars.getKp4Step1().getCuspAspect());	
		assertEquals(0,cNeerajType1Mars.getKp4Step1().getPlanetAspect().size());
		
		
		//MERCURY
		BeanKP4Step cNeerajType1Mer=cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.MERCURY);		
		assertTrue(!cNeerajType1Mer.getKp4Step1().isPlanetStrong());
		assertNull(cNeerajType1Mer.getKp4Step1().getPlanetStepValues());
		/*NOTE: ACCORDING TO CALCULATION IT IS CORRECT BUT SOFTWARE  USING  FOR  TESTING ,IT SHOULD BE NULL*/	
		assertEquals( 7,cNeerajType1Mer.getKp4Step1().getPlanetCuspConjunction());
		//END 
		
		assertEquals(0,cNeerajType1Mer.getKp4Step1().getPlanetConjunction().size());		
		assertNull(cNeerajType1Mer.getKp4Step1().getCuspAspect());		
		assertEquals(GlobalVariablesKPExtension.SAT,cNeerajType1Mer.getKp4Step1().getPlanetAspect().get(0).getKey());
		for(int i=0;i<cNeerajType1Mer.getKp4Step1().getPlanetAspect().get(0).getValues().length;i++)
			assertTrue(cNeerajType1Mer.getKp4Step1().getPlanetAspect().get(0).getValues()[i]==3||
			cNeerajType1Mer.getKp4Step1().getPlanetAspect().get(0).getValues()[i]==9);
		
		
		//JUPITER
		BeanKP4Step cNeerajType1Jup=cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.JUPITER);		
		assertTrue(cNeerajType1Jup.getKp4Step1().isPlanetStrong());
		assertTrue(!cNeerajType1Jup.getKp4Step1().isPlanetInItsOwnStar());
		assertTrue(!cNeerajType1Jup.getKp4Step1().isPlanetStarLordOfOtherPlanet());
		
		for(int i=0;i<cNeerajType1Jup.getKp4Step1().getPlanetStepValues().length;i++)
			assertTrue(cNeerajType1Jup.getKp4Step1().getPlanetStepValues()[i]==2||
					cNeerajType1Jup.getKp4Step1().getPlanetStepValues()[i]==8);
		
		assertEquals( -1,cNeerajType1Jup.getKp4Step1().getPlanetCuspConjunction());
		assertEquals(GlobalVariablesKPExtension.VENUS,cNeerajType1Jup.getKp4Step1().getPlanetConjunction().get(0).getKey());
		for(int i=0;i<cNeerajType1Jup.getKp4Step1().getPlanetConjunction().get(0).getValues().length;i++)
			assertTrue(cNeerajType1Jup.getKp4Step1().getPlanetConjunction().get(0).getValues()[i]==8||
			cNeerajType1Jup.getKp4Step1().getPlanetConjunction().get(0).getValues()[i]==12);
	
			
		for(int i=0;i<cNeerajType1Jup.getKp4Step1().getCuspAspect().length;i++)
			assertTrue(cNeerajType1Jup.getKp4Step1().getCuspAspect()[i]==4||
			cNeerajType1Jup.getKp4Step1().getCuspAspect()[i]==12);
		assertEquals(0,cNeerajType1Jup.getKp4Step1().getPlanetAspect().size());
		
		//VENUS
		BeanKP4Step cNeerajType1Ven=cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.VENUS);		
		assertTrue(cNeerajType1Ven.getKp4Step1().isPlanetStrong());
		assertTrue(!cNeerajType1Ven.getKp4Step1().isPlanetInItsOwnStar());
		assertTrue(!cNeerajType1Ven.getKp4Step1().isPlanetStarLordOfOtherPlanet());
		
		for(int i=0;i<cNeerajType1Ven.getKp4Step1().getPlanetStepValues().length;i++)
			assertTrue(cNeerajType1Ven.getKp4Step1().getPlanetStepValues()[i]==8||
					cNeerajType1Ven.getKp4Step1().getPlanetStepValues()[i]==12);
		
		assertEquals( -1,cNeerajType1Ven.getKp4Step1().getPlanetCuspConjunction());	
		assertEquals(GlobalVariablesKPExtension.JUPITER,cNeerajType1Ven.getKp4Step1().getPlanetConjunction().get(0).getKey());
		for(int i=0;i<cNeerajType1Ven.getKp4Step1().getPlanetConjunction().get(0).getValues().length;i++)
			assertTrue(cNeerajType1Ven.getKp4Step1().getPlanetConjunction().get(0).getValues()[i]==2||
					cNeerajType1Ven.getKp4Step1().getPlanetConjunction().get(0).getValues()[i]==8);
		
		assertNull(cNeerajType1Ven.getKp4Step1().getCuspAspect());	
		assertEquals(0,cNeerajType1Ven.getKp4Step1().getPlanetAspect().size());
		
		
		//SAT
		BeanKP4Step cNeerajType1Sat=cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.SAT);		
		assertTrue(!cNeerajType1Sat.getKp4Step1().isPlanetStrong());
		assertNull(cNeerajType1Sat.getKp4Step1().getPlanetStepValues());
		assertEquals( -1,cNeerajType1Sat.getKp4Step1().getPlanetCuspConjunction());		
		assertEquals(GlobalVariablesKPExtension.MARS,cNeerajType1Sat.getKp4Step1().getPlanetConjunction().get(0).getKey());
		for(int i=0;i<cNeerajType1Sat.getKp4Step1().getPlanetConjunction().get(0).getValues().length;i++)
			assertTrue(cNeerajType1Sat.getKp4Step1().getPlanetConjunction().get(0).getValues()[i]==1||
					cNeerajType1Sat.getKp4Step1().getPlanetConjunction().get(0).getValues()[i]==9);
		
		
		assertNull(cNeerajType1Sat.getKp4Step1().getCuspAspect());	
		assertEquals(0,cNeerajType1Sat.getKp4Step1().getPlanetAspect().size());
		
		
		//RAHU
		BeanKP4Step cNeerajType1Rahu=cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.RAHU);		
		assertTrue(!cNeerajType1Rahu.getKp4Step1().isPlanetStrong());
		assertNull(cNeerajType1Rahu.getKp4Step1().getPlanetStepValues());
		assertEquals( -1,cNeerajType1Rahu.getKp4Step1().getPlanetCuspConjunction());
		assertEquals(0,cNeerajType1Rahu.getKp4Step1().getPlanetConjunction().size());			
		for(int i=0;i<cNeerajType1Rahu.getKp4Step1().getCuspAspect().length;i++)
			assertTrue(cNeerajType1Rahu.getKp4Step1().getCuspAspect()[i]==3||
					cNeerajType1Rahu.getKp4Step1().getCuspAspect()[i]==7);
		assertEquals(0,cNeerajType1Rahu.getKp4Step1().getPlanetAspect().size());
		
		
		//KETU
		BeanKP4Step cNeerajType1Ketu=cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.KETU);		
		assertTrue(!cNeerajType1Ketu.getKp4Step1().isPlanetStrong());
		assertNull(cNeerajType1Ketu.getKp4Step1().getPlanetStepValues());
		assertEquals( -1,cNeerajType1Ketu.getKp4Step1().getPlanetCuspConjunction());
		assertEquals(0,cNeerajType1Ketu.getKp4Step1().getPlanetConjunction().size());		
		for(int i=0;i<cNeerajType1Ketu.getKp4Step1().getCuspAspect().length;i++)
			assertTrue(cNeerajType1Ketu.getKp4Step1().getCuspAspect()[i]==1||
					cNeerajType1Ketu.getKp4Step1().getCuspAspect()[i]==9);
		assertEquals(0,cNeerajType1Ketu.getKp4Step1().getPlanetAspect().size());
				

		
	}
	
	public void get4StepType3Neeraj()
	{
		   //SUN
			BeanKP4Step cNeerajType3Sun=cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.SUN);		
			assertEquals(cNeerajType3Sun.getKp4Step3().getPlanet(),GlobalVariablesKPExtension.SAT);
			assertNull(cNeerajType3Sun.getKp4Step3().getPlanetStepValues());
			assertEquals( -1,cNeerajType3Sun.getKp4Step3().getPlanetCuspConjunction());
		   /* NOTE: ACCORDING TO CALCULATION IT IS CORRECT BUT SOFTWARE  USING  FOR  TESTING ,IT SHOULD BE NULL*/
			assertEquals(GlobalVariablesKPExtension.MARS,cNeerajType3Sun.getKp4Step3().getPlanetConjunction().get(0).getKey());
			for(int i=0;i<cNeerajType3Sun.getKp4Step3().getPlanetConjunction().get(0).getValues().length;i++)
				assertTrue(cNeerajType3Sun.getKp4Step3().getPlanetConjunction().get(0).getValues()[i]==1||
						cNeerajType3Sun.getKp4Step3().getPlanetConjunction().get(0).getValues()[i]==9);
	       //END
			assertNull(cNeerajType3Sun.getKp4Step3().getCuspAspect());	
			assertEquals(0,cNeerajType3Sun.getKp4Step3().getPlanetAspect().size());
	
			//MOON
			BeanKP4Step cNeerajType3Moon=cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.MOON);		
			assertEquals(cNeerajType3Moon.getKp4Step3().getPlanet(),GlobalVariablesKPExtension.SAT);
			assertNull(cNeerajType3Moon.getKp4Step3().getPlanetStepValues());
			assertEquals( -1,cNeerajType3Moon.getKp4Step3().getPlanetCuspConjunction());
			
			/* NOTE: ACCORDING TO CALCULATION IT IS CORRECT BUT SOFTWARE  USING  FOR  TESTING ,IT SHOULD BE NULL*/
			assertEquals(GlobalVariablesKPExtension.MARS,cNeerajType3Moon.getKp4Step3().getPlanetConjunction().get(0).getKey());
			for(int i=0;i<cNeerajType3Moon.getKp4Step3().getPlanetConjunction().get(0).getValues().length;i++)
				assertTrue(cNeerajType3Moon.getKp4Step3().getPlanetConjunction().get(0).getValues()[i]==1||
						cNeerajType3Moon.getKp4Step3().getPlanetConjunction().get(0).getValues()[i]==9);		
			  //END
			
			assertNull(cNeerajType3Moon.getKp4Step3().getCuspAspect());
		    assertEquals(0,cNeerajType3Moon.getKp4Step3().getPlanetAspect().size());
		    
		    

			 //MARS
			BeanKP4Step cNeerajType3Mars=cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.MARS);			
			assertEquals(cNeerajType3Mars.getKp4Step3().getPlanet(),GlobalVariablesKPExtension.SAT);
			assertNull(cNeerajType3Mars.getKp4Step3().getPlanetStepValues());
			assertEquals( -1,cNeerajType3Mars.getKp4Step3().getPlanetCuspConjunction());
			//assertEquals(0,cNeerajType3Mars.getKp4Step3().getPlanetConjunction().size());	
			
			/* NOTE: ACCORDING TO CALCULATION IT IS CORRECT BUT SOFTWARE  USING  FOR  TESTING ,IT SHOULD BE NULL*/
			assertEquals(GlobalVariablesKPExtension.MARS,cNeerajType3Mars.getKp4Step3().getPlanetConjunction().get(0).getKey());
			for(int i=0;i<cNeerajType3Mars.getKp4Step3().getPlanetConjunction().get(0).getValues().length;i++)
				assertTrue(cNeerajType3Mars.getKp4Step3().getPlanetConjunction().get(0).getValues()[i]==1||
						cNeerajType3Mars.getKp4Step3().getPlanetConjunction().get(0).getValues()[i]==9);		
			  //END
			
			assertNull(cNeerajType3Mars.getKp4Step3().getCuspAspect());
		    assertEquals(0,cNeerajType3Mars.getKp4Step3().getPlanetAspect().size());
		    
		    
		    

		 //MERCURY
			BeanKP4Step cNeerajType3Mer=cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.MERCURY);			
			assertEquals(cNeerajType3Mer.getKp4Step3().getPlanet(),GlobalVariablesKPExtension.JUPITER);
			assertNotNull(cNeerajType3Mer.getKp4Step3().getPlanetStepValues());
			for(int i=0;i<cNeerajType3Mer.getKp4Step3().getPlanetStepValues().length;i++)
				assertTrue(cNeerajType3Mer.getKp4Step3().getPlanetStepValues()[i]==2||
						cNeerajType3Mer.getKp4Step3().getPlanetStepValues()[i]==8);
			
			assertEquals( -1,cNeerajType3Mer.getKp4Step3().getPlanetCuspConjunction());
			/* NOTE: ACCORDING TO CALCULATION IT IS CORRECT BUT SOFTWARE  USING  FOR  TESTING ,IT SHOULD BE NULL*/
			assertEquals(GlobalVariablesKPExtension.VENUS,cNeerajType3Mer.getKp4Step3().getPlanetConjunction().get(0).getKey());
			for(int i=0;i<cNeerajType3Mer.getKp4Step3().getPlanetConjunction().get(0).getValues().length;i++)
				assertTrue(cNeerajType3Mer.getKp4Step3().getPlanetConjunction().get(0).getValues()[i]==8||
						cNeerajType3Mer.getKp4Step3().getPlanetConjunction().get(0).getValues()[i]==12);		
			  //END
		
			for(int i=0;i<cNeerajType3Mer.getKp4Step3().getCuspAspect().length;i++)
				assertTrue(cNeerajType3Mer.getKp4Step3().getCuspAspect()[i]==4||
						cNeerajType3Mer.getKp4Step3().getCuspAspect()[i]==12);
		    assertEquals(0,cNeerajType3Mer.getKp4Step3().getPlanetAspect().size());
		    
		  //JUPITER
			BeanKP4Step cNeerajType3Jup=cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.JUPITER);		
			assertEquals(cNeerajType3Jup.getKp4Step3().getPlanet(),GlobalVariablesKPExtension.VENUS);
			
			for(int i=0;i<cNeerajType3Jup.getKp4Step3().getPlanetStepValues().length;i++)
				assertTrue(cNeerajType3Jup.getKp4Step3().getPlanetStepValues()[i]==8||
						cNeerajType3Jup.getKp4Step3().getPlanetStepValues()[i]==12);
			
			assertEquals( -1,cNeerajType3Jup.getKp4Step3().getPlanetCuspConjunction());	
			assertEquals(GlobalVariablesKPExtension.JUPITER,cNeerajType3Jup.getKp4Step3().getPlanetConjunction().get(0).getKey());
			for(int i=0;i<cNeerajType3Jup.getKp4Step3().getPlanetConjunction().get(0).getValues().length;i++)
				assertTrue(cNeerajType3Jup.getKp4Step3().getPlanetConjunction().get(0).getValues()[i]==2||
						cNeerajType3Jup.getKp4Step3().getPlanetConjunction().get(0).getValues()[i]==8);
			
			assertNull(cNeerajType3Jup.getKp4Step3().getCuspAspect());	
			assertEquals(0,cNeerajType3Jup.getKp4Step3().getPlanetAspect().size());
		    
		   //VENUS
			BeanKP4Step cNeerajType3Ven=cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.VENUS);		
			assertEquals(cNeerajType3Ven.getKp4Step3().getPlanet(),GlobalVariablesKPExtension.MOON);
			assertNull(cNeerajType3Ven.getKp4Step3().getPlanetStepValues());
			assertEquals( -1,cNeerajType3Ven.getKp4Step3().getPlanetCuspConjunction());
			assertEquals(0,cNeerajType3Ven.getKp4Step3().getPlanetConjunction().size());
			assertNull(cNeerajType3Ven.getKp4Step3().getCuspAspect());	
			assertEquals(0,cNeerajType3Ven.getKp4Step3().getPlanetAspect().size());
			
			//SAT
			BeanKP4Step cNeerajType3Sat=cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.SAT);		
			assertEquals(cNeerajType3Sat.getKp4Step3().getPlanet(),GlobalVariablesKPExtension.VENUS);

			for(int i=0;i<cNeerajType3Sat.getKp4Step3().getPlanetStepValues().length;i++)
				assertTrue(cNeerajType3Sat.getKp4Step3().getPlanetStepValues()[i]==8||
						cNeerajType3Sat.getKp4Step3().getPlanetStepValues()[i]==12);
			
			assertEquals( -1,cNeerajType3Sat.getKp4Step3().getPlanetCuspConjunction());	
			assertEquals(GlobalVariablesKPExtension.JUPITER,cNeerajType3Sat.getKp4Step3().getPlanetConjunction().get(0).getKey());
			for(int i=0;i<cNeerajType3Sat.getKp4Step3().getPlanetConjunction().get(0).getValues().length;i++)
				assertTrue(cNeerajType3Sat.getKp4Step3().getPlanetConjunction().get(0).getValues()[i]==2||
						cNeerajType3Sat.getKp4Step3().getPlanetConjunction().get(0).getValues()[i]==8);
			
			assertNull(cNeerajType3Sat.getKp4Step3().getCuspAspect());	
			assertEquals(0,cNeerajType3Sat.getKp4Step3().getPlanetAspect().size());
			
			//RAHU
			BeanKP4Step cNeerajType3Rahu=cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.RAHU);		
			assertEquals(cNeerajType3Rahu.getKp4Step3().getPlanet(),GlobalVariablesKPExtension.VENUS);
			

			for(int i=0;i<cNeerajType3Rahu.getKp4Step3().getPlanetStepValues().length;i++)
				assertTrue(cNeerajType3Rahu.getKp4Step3().getPlanetStepValues()[i]==8||
						cNeerajType3Rahu.getKp4Step3().getPlanetStepValues()[i]==12);
			
			assertEquals( -1,cNeerajType3Rahu.getKp4Step3().getPlanetCuspConjunction());	
			assertEquals(GlobalVariablesKPExtension.JUPITER,cNeerajType3Rahu.getKp4Step3().getPlanetConjunction().get(0).getKey());
			for(int i=0;i<cNeerajType3Rahu.getKp4Step3().getPlanetConjunction().get(0).getValues().length;i++)
				assertTrue(cNeerajType3Rahu.getKp4Step3().getPlanetConjunction().get(0).getValues()[i]==2||
						cNeerajType3Rahu.getKp4Step3().getPlanetConjunction().get(0).getValues()[i]==8);
			
			assertNull(cNeerajType3Rahu.getKp4Step3().getCuspAspect());	
			assertEquals(0,cNeerajType3Rahu.getKp4Step3().getPlanetAspect().size());
			
			

			//KETU
			BeanKP4Step cNeerajType3Ketu=cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.KETU);		
			assertEquals(cNeerajType3Ketu.getKp4Step3().getPlanet(),GlobalVariablesKPExtension.VENUS);
			

			for(int i=0;i<cNeerajType3Ketu.getKp4Step3().getPlanetStepValues().length;i++)
				assertTrue(cNeerajType3Ketu.getKp4Step3().getPlanetStepValues()[i]==8||
						cNeerajType3Ketu.getKp4Step3().getPlanetStepValues()[i]==12);
			
			assertEquals( -1,cNeerajType3Ketu.getKp4Step3().getPlanetCuspConjunction());	
			assertEquals(GlobalVariablesKPExtension.JUPITER,cNeerajType3Ketu.getKp4Step3().getPlanetConjunction().get(0).getKey());
			for(int i=0;i<cNeerajType3Ketu.getKp4Step3().getPlanetConjunction().get(0).getValues().length;i++)
				assertTrue(cNeerajType3Ketu.getKp4Step3().getPlanetConjunction().get(0).getValues()[i]==2||
						cNeerajType3Ketu.getKp4Step3().getPlanetConjunction().get(0).getValues()[i]==8);
			
			assertNull(cNeerajType3Ketu.getKp4Step3().getCuspAspect());	
			assertEquals(0,cNeerajType3Ketu.getKp4Step3().getPlanetAspect().size());
		
	}
	public void get4StepType2Neeraj()
	{
		//SUN
		BeanKP4Step cNeerajType2Sun=cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.SUN);		
		assertEquals(cNeerajType2Sun.getKp4Step2().getPlanet(),GlobalVariablesKPExtension.MOON);
		for(int i=0;i<cNeerajType2Sun.getKp4Step2().getPlanetStepValues().length;i++)
			assertTrue(cNeerajType2Sun.getKp4Step2().getPlanetStepValues()[i]==5);
		assertEquals(-1,cNeerajType2Sun.getKp4Step2().getPlanetCuspConjunction());
		assertEquals(0,cNeerajType2Sun.getKp4Step2().getPlanetConjunction().size());
		assertNull(cNeerajType2Sun.getKp4Step2().getCuspAspect());		
		assertEquals(0,cNeerajType2Sun.getKp4Step2().getPlanetAspect().size());
		
		
		//MOON
		BeanKP4Step cNeerajType2Moon=cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.MOON);		
		assertEquals(cNeerajType2Moon.getKp4Step2().getPlanet(),GlobalVariablesKPExtension.MERCURY);
		for(int i=0;i<cNeerajType2Moon.getKp4Step2().getPlanetStepValues().length;i++)
			assertTrue(cNeerajType2Moon.getKp4Step2().getPlanetStepValues()[i]==6||
					cNeerajType2Moon.getKp4Step2().getPlanetStepValues()[i]==11);
		assertEquals(7,cNeerajType2Moon.getKp4Step2().getPlanetCuspConjunction());
		assertEquals(0,cNeerajType2Moon.getKp4Step2().getPlanetConjunction().size());
		assertNull(cNeerajType2Moon.getKp4Step2().getCuspAspect());		
		
		assertEquals(GlobalVariablesKPExtension.SAT,cNeerajType2Moon.getKp4Step2().getPlanetAspect().get(0).getKey());
		for(int i=0;i<cNeerajType2Moon.getKp4Step2().getPlanetAspect().get(0).getValues().length;i++)
			assertTrue(cNeerajType2Moon.getKp4Step2().getPlanetAspect().get(0).getValues()[i]==3||
					cNeerajType2Moon.getKp4Step2().getPlanetAspect().get(0).getValues()[i]==9);
		
		//MARS
		BeanKP4Step cNeerajType2Mars=cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.MARS);		
		assertEquals(cNeerajType2Mars.getKp4Step2().getPlanet(),GlobalVariablesKPExtension.MERCURY);
		for(int i=0;i<cNeerajType2Mars.getKp4Step2().getPlanetStepValues().length;i++)
			assertTrue(cNeerajType2Mars.getKp4Step2().getPlanetStepValues()[i]==6||
					cNeerajType2Mars.getKp4Step2().getPlanetStepValues()[i]==11);
		assertEquals(7,cNeerajType2Mars.getKp4Step2().getPlanetCuspConjunction());
		assertEquals(0,cNeerajType2Moon.getKp4Step2().getPlanetConjunction().size());
		assertNull(cNeerajType2Mars.getKp4Step2().getCuspAspect());		
		
		assertEquals(GlobalVariablesKPExtension.SAT,cNeerajType2Mars.getKp4Step2().getPlanetAspect().get(0).getKey());
		for(int i=0;i<cNeerajType2Mars.getKp4Step2().getPlanetAspect().get(0).getValues().length;i++)
			assertTrue(cNeerajType2Mars.getKp4Step2().getPlanetAspect().get(0).getValues()[i]==3||
					cNeerajType2Moon.getKp4Step2().getPlanetAspect().get(0).getValues()[i]==9);
		
		
		
		//MERCURY
		BeanKP4Step cNeerajType2Mer=cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.MERCURY);		
		assertEquals(cNeerajType2Mer.getKp4Step2().getPlanet(),GlobalVariablesKPExtension.SUN);
		for(int i=0;i<cNeerajType2Mer.getKp4Step2().getPlanetStepValues().length;i++)
			assertTrue(cNeerajType2Mer.getKp4Step2().getPlanetStepValues()[i]==7);
		assertEquals(-1,cNeerajType2Mer.getKp4Step2().getPlanetCuspConjunction());
		assertEquals(0,cNeerajType2Mer.getKp4Step2().getPlanetConjunction().size());
		assertNull(cNeerajType2Mer.getKp4Step2().getCuspAspect());		
		assertEquals(0,cNeerajType2Mer.getKp4Step2().getPlanetAspect().size());
		
		//JUPITER
		BeanKP4Step cNeerajType2Jup=cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.JUPITER);		
		assertEquals(cNeerajType2Jup.getKp4Step2().getPlanet(),GlobalVariablesKPExtension.RAHU);
		for(int i=0;i<cNeerajType2Jup.getKp4Step2().getPlanetStepValues().length;i++)
			assertTrue(cNeerajType2Jup.getKp4Step2().getPlanetStepValues()[i]==10);
		assertEquals(-1,cNeerajType2Jup.getKp4Step2().getPlanetCuspConjunction());
		assertEquals(0,cNeerajType2Jup.getKp4Step2().getPlanetConjunction().size());
		
		for(int i=0;i<cNeerajType2Jup.getKp4Step2().getCuspAspect().length;i++)
			assertTrue(cNeerajType2Jup.getKp4Step2().getCuspAspect()[i]==3);
	    assertEquals(0,cNeerajType2Jup.getKp4Step2().getPlanetAspect().size());
	    
	  //VENUS
  		BeanKP4Step cNeerajType2Ven=cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.VENUS);		
  		assertEquals(cNeerajType2Ven.getKp4Step2().getPlanet(),GlobalVariablesKPExtension.RAHU);
  		for(int i=0;i<cNeerajType2Ven.getKp4Step2().getPlanetStepValues().length;i++)
  			assertTrue(cNeerajType2Ven.getKp4Step2().getPlanetStepValues()[i]==10);
  		assertEquals(-1,cNeerajType2Ven.getKp4Step2().getPlanetCuspConjunction());
  		assertEquals(0,cNeerajType2Ven.getKp4Step2().getPlanetConjunction().size());
  		
  		for(int i=0;i<cNeerajType2Ven.getKp4Step2().getCuspAspect().length;i++)
  			assertTrue(cNeerajType2Ven.getKp4Step2().getCuspAspect()[i]==3);
  	    assertEquals(0,cNeerajType2Ven.getKp4Step2().getPlanetAspect().size());
  	    
     //SAT
  		BeanKP4Step cNeerajType2Sat=cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.SAT);		
  		assertEquals(cNeerajType2Sat.getKp4Step2().getPlanet(),GlobalVariablesKPExtension.KETU);
  		KeyValue4Step objNeerajSat=cNeerajType2Sat.getKp4Step2().getRahuKetPlacedInPlanetRashi();
		assertEquals(objNeerajSat.getKey(), GlobalVariablesKPExtension.JUPITER);
		for(int i=0;i<objNeerajSat.getValues().length;i++)
			assertTrue(objNeerajSat.getValues()[i]==2||
					objNeerajSat.getValues()[i]==8);
  		
  		for(int i=0;i<cNeerajType2Sat.getKp4Step2().getPlanetStepValues().length;i++)
  			assertTrue(cNeerajType2Sat.getKp4Step2().getPlanetStepValues()[i]==4);
  		assertEquals(-1,cNeerajType2Sat.getKp4Step2().getPlanetCuspConjunction());
  		assertEquals(0,cNeerajType2Sat.getKp4Step2().getPlanetConjunction().size());
  		
  		
  		for(int i=0;i<cNeerajType2Sat.getKp4Step2().getCuspAspect().length;i++)  			
  			assertTrue(cNeerajType2Sat.getKp4Step2().getCuspAspect()[i]==1||
  					cNeerajType2Sat.getKp4Step2().getCuspAspect()[i]==9);
  	    assertEquals(0,cNeerajType2Sat.getKp4Step2().getPlanetAspect().size());
		
  	    
  	  //RAHU
		BeanKP4Step cNeerajType2Rahu=cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.RAHU);		
		assertEquals(cNeerajType2Rahu.getKp4Step2().getPlanet(),GlobalVariablesKPExtension.SUN);
		for(int i=0;i<cNeerajType2Rahu.getKp4Step2().getPlanetStepValues().length;i++)
			assertTrue(cNeerajType2Rahu.getKp4Step2().getPlanetStepValues()[i]==7);
		assertEquals(-1,cNeerajType2Rahu.getKp4Step2().getPlanetCuspConjunction());
		assertEquals(0,cNeerajType2Rahu.getKp4Step2().getPlanetConjunction().size());
		assertNull(cNeerajType2Rahu.getKp4Step2().getCuspAspect());		
		assertEquals(0,cNeerajType2Rahu.getKp4Step2().getPlanetAspect().size());
		
	   //KETU
		BeanKP4Step cNeerajType2Ketu=cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.KETU);		
		assertEquals(cNeerajType2Ketu.getKp4Step2().getPlanet(),GlobalVariablesKPExtension.SAT);

		for(int i=0;i<cNeerajType2Ketu.getKp4Step2().getPlanetStepValues().length;i++)
			assertTrue(cNeerajType2Ketu.getKp4Step2().getPlanetStepValues()[i]==3||
					cNeerajType2Ketu.getKp4Step2().getPlanetStepValues()[i]==9);
		
		assertEquals( -1,cNeerajType2Ketu.getKp4Step2().getPlanetCuspConjunction());	
		assertEquals(GlobalVariablesKPExtension.MARS,cNeerajType2Ketu.getKp4Step2().getPlanetConjunction().get(0).getKey());
		for(int i=0;i<cNeerajType2Ketu.getKp4Step2().getPlanetConjunction().get(0).getValues().length;i++)
			assertTrue(cNeerajType2Ketu.getKp4Step2().getPlanetConjunction().get(0).getValues()[i]==1||
					cNeerajType2Ketu.getKp4Step2().getPlanetConjunction().get(0).getValues()[i]==9);
		
		assertNull(cNeerajType2Ketu.getKp4Step2().getCuspAspect());	
		assertEquals(0,cNeerajType2Ketu.getKp4Step2().getPlanetAspect().size());
		
	}
	public void get4StepType4Neeraj()
	{
		//SUN
		BeanKP4Step cNeerajType4Sun=cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.SUN);		
		assertEquals(cNeerajType4Sun.getKp4Step4().getPlanet(),GlobalVariablesKPExtension.KETU);
  		KeyValue4Step objNeerajSun=cNeerajType4Sun.getKp4Step4().getRahuKetPlacedInPlanetRashi();
		assertEquals(objNeerajSun.getKey(), GlobalVariablesKPExtension.JUPITER);
		for(int i=0;i<objNeerajSun.getValues().length;i++)
			assertTrue(objNeerajSun.getValues()[i]==2||
					objNeerajSun.getValues()[i]==8);
  		
  		for(int i=0;i<cNeerajType4Sun.getKp4Step4().getPlanetStepValues().length;i++)
  			assertTrue(cNeerajType4Sun.getKp4Step4().getPlanetStepValues()[i]==4);
  		assertEquals(-1,cNeerajType4Sun.getKp4Step4().getPlanetCuspConjunction());
  		assertEquals(0,cNeerajType4Sun.getKp4Step4().getPlanetConjunction().size());
  		
  		
  		for(int i=0;i<cNeerajType4Sun.getKp4Step4().getCuspAspect().length;i++)  			
  			assertTrue(cNeerajType4Sun.getKp4Step4().getCuspAspect()[i]==1||
  					cNeerajType4Sun.getKp4Step4().getCuspAspect()[i]==9);
  	    assertEquals(0,cNeerajType4Sun.getKp4Step4().getPlanetAspect().size());
  	    
  	   //MOON
		BeanKP4Step cNeerajType4Moon=cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.MOON);		
		assertEquals(cNeerajType4Moon.getKp4Step4().getPlanet(),GlobalVariablesKPExtension.KETU);
  		KeyValue4Step objNeerajMoon=cNeerajType4Sun.getKp4Step4().getRahuKetPlacedInPlanetRashi();
		assertEquals(objNeerajMoon.getKey(), GlobalVariablesKPExtension.JUPITER);
		for(int i=0;i<objNeerajMoon.getValues().length;i++)
			assertTrue(objNeerajMoon.getValues()[i]==2||
					objNeerajMoon.getValues()[i]==8);
  		
  		for(int i=0;i<cNeerajType4Moon.getKp4Step4().getPlanetStepValues().length;i++)
  			assertTrue(cNeerajType4Moon.getKp4Step4().getPlanetStepValues()[i]==4);
  		assertEquals(-1,cNeerajType4Moon.getKp4Step4().getPlanetCuspConjunction());
  		assertEquals(0,cNeerajType4Moon.getKp4Step4().getPlanetConjunction().size());
  		
  		
  		for(int i=0;i<cNeerajType4Moon.getKp4Step4().getCuspAspect().length;i++)  			
  			assertTrue(cNeerajType4Moon.getKp4Step4().getCuspAspect()[i]==1||
  					cNeerajType4Moon.getKp4Step4().getCuspAspect()[i]==9);
  	    assertEquals(0,cNeerajType4Moon.getKp4Step4().getPlanetAspect().size());
  	    
  	  //MARS
		BeanKP4Step cNeerajType4Mars=cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.MARS);		
		assertEquals(cNeerajType4Mars.getKp4Step4().getPlanet(),GlobalVariablesKPExtension.KETU);
  		KeyValue4Step objNeerajMars=cNeerajType4Sun.getKp4Step4().getRahuKetPlacedInPlanetRashi();
		assertEquals(objNeerajMars.getKey(), GlobalVariablesKPExtension.JUPITER);
		for(int i=0;i<objNeerajMars.getValues().length;i++)
			assertTrue(objNeerajMars.getValues()[i]==2||
					objNeerajMars.getValues()[i]==8);
  		
  		for(int i=0;i<cNeerajType4Mars.getKp4Step4().getPlanetStepValues().length;i++)
  			assertTrue(cNeerajType4Mars.getKp4Step4().getPlanetStepValues()[i]==4);
  		assertEquals(-1,cNeerajType4Mars.getKp4Step4().getPlanetCuspConjunction());
  		assertEquals(0,cNeerajType4Mars.getKp4Step4().getPlanetConjunction().size());
  		
  		
  		for(int i=0;i<cNeerajType4Mars.getKp4Step4().getCuspAspect().length;i++)  			
  			assertTrue(cNeerajType4Moon.getKp4Step4().getCuspAspect()[i]==1||
  					cNeerajType4Mars.getKp4Step4().getCuspAspect()[i]==9);
  	    assertEquals(0,cNeerajType4Moon.getKp4Step4().getPlanetAspect().size());
  	    
  	  //MERCURY
		BeanKP4Step cNeerajType4Mer=cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.MERCURY);		
		assertEquals(cNeerajType4Mer.getKp4Step4().getPlanet(),GlobalVariablesKPExtension.RAHU); 
		for(int i=0;i<cNeerajType4Mer.getKp4Step4().getPlanetStepValues().length;i++)
  			assertTrue(cNeerajType4Mer.getKp4Step4().getPlanetStepValues()[i]==10);
		
		KeyValue4Step objNeerajMer=cNeerajType4Mer.getKp4Step4().getRahuKetPlacedInPlanetRashi();
		assertEquals(objNeerajMer.getKey(), GlobalVariablesKPExtension.MERCURY);
		for(int i=0;i<objNeerajMer.getValues().length;i++)
			assertTrue(objNeerajMer.getValues()[i]==6||
					objNeerajMer.getValues()[i]==11);
		
		
  		assertEquals(-1,cNeerajType4Mer.getKp4Step4().getPlanetCuspConjunction());
  		assertEquals(0,cNeerajType4Mer.getKp4Step4().getPlanetConjunction().size());
  		
  		for(int i=0;i<cNeerajType4Mer.getKp4Step4().getCuspAspect().length;i++)
  			assertTrue(cNeerajType4Mer.getKp4Step4().getCuspAspect()[i]==3);
  	    assertEquals(0,cNeerajType4Mer.getKp4Step4().getPlanetAspect().size());
  	    
  	    //JUPITER
		BeanKP4Step cNeerajType4Jup=cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.JUPITER);		
		assertEquals(cNeerajType4Jup.getKp4Step4().getPlanet(),GlobalVariablesKPExtension.RAHU); 
		for(int i=0;i<cNeerajType4Jup.getKp4Step4().getPlanetStepValues().length;i++)
  			assertTrue(cNeerajType4Jup.getKp4Step4().getPlanetStepValues()[i]==10);
		
		KeyValue4Step objNeerajJup=cNeerajType4Jup.getKp4Step4().getRahuKetPlacedInPlanetRashi();
		assertEquals(objNeerajJup.getKey(), GlobalVariablesKPExtension.MERCURY);
		for(int i=0;i<objNeerajJup.getValues().length;i++)
			assertTrue(objNeerajJup.getValues()[i]==6||
					objNeerajJup.getValues()[i]==11);
		
		
  		assertEquals(-1,cNeerajType4Jup.getKp4Step4().getPlanetCuspConjunction());
  		assertEquals(0,cNeerajType4Jup.getKp4Step4().getPlanetConjunction().size());
  		
  		for(int i=0;i<cNeerajType4Jup.getKp4Step4().getCuspAspect().length;i++)
  			assertTrue(cNeerajType4Jup.getKp4Step4().getCuspAspect()[i]==3);
  	    assertEquals(0,cNeerajType4Jup.getKp4Step4().getPlanetAspect().size());
  	    
  	  //VENUS
		BeanKP4Step cNeerajType4Ven=cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.VENUS);		
		assertEquals(cNeerajType4Ven.getKp4Step4().getPlanet(),GlobalVariablesKPExtension.MERCURY);  
		for(int i=0;i<cNeerajType4Ven.getKp4Step4().getPlanetStepValues().length;i++)
			assertTrue(cNeerajType4Ven.getKp4Step4().getPlanetStepValues()[i]==6||
					cNeerajType4Ven.getKp4Step4().getPlanetStepValues()[i]==11);
		assertEquals(7,cNeerajType4Ven.getKp4Step4().getPlanetCuspConjunction());
		assertEquals(0,cNeerajType4Ven.getKp4Step4().getPlanetConjunction().size());
		assertNull(cNeerajType4Ven.getKp4Step4().getCuspAspect());		
		
		assertEquals(GlobalVariablesKPExtension.SAT,cNeerajType4Ven.getKp4Step4().getPlanetAspect().get(0).getKey());
		for(int i=0;i<cNeerajType4Ven.getKp4Step4().getPlanetAspect().get(0).getValues().length;i++)
			assertTrue(cNeerajType4Ven.getKp4Step4().getPlanetAspect().get(0).getValues()[i]==3||
					cNeerajType4Ven.getKp4Step4().getPlanetAspect().get(0).getValues()[i]==9);
		

  	    //RAHU
		BeanKP4Step cNeerajType4Rahu=cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.RAHU);		
		assertEquals(cNeerajType4Rahu.getKp4Step4().getPlanet(),GlobalVariablesKPExtension.RAHU); 
		for(int i=0;i<cNeerajType4Rahu.getKp4Step4().getPlanetStepValues().length;i++)
  			assertTrue(cNeerajType4Jup.getKp4Step4().getPlanetStepValues()[i]==10);
		
		KeyValue4Step objNeerajRahu=cNeerajType4Rahu.getKp4Step4().getRahuKetPlacedInPlanetRashi();
		assertEquals(objNeerajRahu.getKey(), GlobalVariablesKPExtension.MERCURY);
		for(int i=0;i<objNeerajRahu.getValues().length;i++)
			assertTrue(objNeerajRahu.getValues()[i]==6||
					objNeerajRahu.getValues()[i]==11);
		
		
  		assertEquals(-1,cNeerajType4Rahu.getKp4Step4().getPlanetCuspConjunction());
  		assertEquals(0,cNeerajType4Rahu.getKp4Step4().getPlanetConjunction().size());
  		
  		for(int i=0;i<cNeerajType4Rahu.getKp4Step4().getCuspAspect().length;i++)
  			assertTrue(cNeerajType4Rahu.getKp4Step4().getCuspAspect()[i]==3);
  	    assertEquals(0,cNeerajType4Rahu.getKp4Step4().getPlanetAspect().size());
  	    
  	  //KETU
		BeanKP4Step cNeerajType4Ket=cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.KETU);		
		assertEquals(cNeerajType4Ket.getKp4Step4().getPlanet(),GlobalVariablesKPExtension.RAHU); 
		for(int i=0;i<cNeerajType4Ket.getKp4Step4().getPlanetStepValues().length;i++)
  			assertTrue(cNeerajType4Ket.getKp4Step4().getPlanetStepValues()[i]==10);
		
		KeyValue4Step objNeerajKetu=cNeerajType4Ket.getKp4Step4().getRahuKetPlacedInPlanetRashi();
		assertEquals(objNeerajKetu.getKey(), GlobalVariablesKPExtension.MERCURY);
		for(int i=0;i<objNeerajKetu.getValues().length;i++)
			assertTrue(objNeerajKetu.getValues()[i]==6||
					objNeerajKetu.getValues()[i]==11);
		
		
  		assertEquals(-1,cNeerajType4Ket.getKp4Step4().getPlanetCuspConjunction());
  		assertEquals(0,cNeerajType4Ket.getKp4Step4().getPlanetConjunction().size());
  		
  		for(int i=0;i<cNeerajType4Ket.getKp4Step4().getCuspAspect().length;i++)
  			assertTrue(cNeerajType4Ket.getKp4Step4().getCuspAspect()[i]==3);
  	    assertEquals(0,cNeerajType4Ket.getKp4Step4().getPlanetAspect().size());
		
  	    
	}
	/******************TEST CASE FOR PUNIT******************************/
	public void get4StepType1Punit()
	{		
		
		//SUN
		BeanKP4Step cPunitType1Sun=cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.SUN);		
		assertTrue(cPunitType1Sun.getKp4Step1().isPlanetStrong());
		assertNotNull(cPunitType1Sun.getKp4Step1().getPlanetStepValues());
		for(int i=0;i<cPunitType1Sun.getKp4Step1().getPlanetStepValues().length;i++)
			assertTrue(cPunitType1Sun.getKp4Step1().getPlanetStepValues()[i]==7);
		
		assertEquals( 7,cPunitType1Sun.getKp4Step1().getPlanetCuspConjunction());
		assertEquals(0,cPunitType1Sun.getKp4Step1().getPlanetConjunction().size());
		assertEquals(1,cPunitType1Sun.getKp4Step1().getCuspAspect()[0]);//NOTE: ACCORDING TO CALCULATION IT IS CORRECT BUT SOFTWARE  USING  FOR  TESTING ,IT SHOULD BE NULL	
		assertEquals(0,cPunitType1Sun.getKp4Step1().getPlanetAspect().size());
		
		
		//MOON
		BeanKP4Step cPunitType1Moon=cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.MOON);		
		assertTrue(cPunitType1Moon.getKp4Step1().isPlanetStrong());
		assertNotNull(cPunitType1Moon.getKp4Step1().getPlanetStepValues());
		for(int i=0;i<cPunitType1Moon.getKp4Step1().getPlanetStepValues().length;i++)
			assertTrue(cPunitType1Moon.getKp4Step1().getPlanetStepValues()[i]==12);
		
		assertEquals( -1,cPunitType1Moon.getKp4Step1().getPlanetCuspConjunction());
		assertEquals(0,cPunitType1Moon.getKp4Step1().getPlanetConjunction().size());
		assertEquals(null,cPunitType1Moon.getKp4Step1().getCuspAspect());	
		assertEquals(0,cPunitType1Moon.getKp4Step1().getPlanetAspect().size());
		
		
		//MARS
		BeanKP4Step cPunitType1Mars=cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.MARS);		
		assertTrue(cPunitType1Mars.getKp4Step1().isPlanetStrong());
		assertNotNull(cPunitType1Mars.getKp4Step1().getPlanetStepValues());
		for(int i=0;i<cPunitType1Mars.getKp4Step1().getPlanetStepValues().length;i++)
			assertTrue(cPunitType1Mars.getKp4Step1().getPlanetStepValues()[i]==3||cPunitType1Mars.getKp4Step1().getPlanetStepValues()[i]==6||cPunitType1Mars.getKp4Step1().getPlanetStepValues()[i]==8);
		
		
		assertEquals( -1,cPunitType1Mars.getKp4Step1().getPlanetCuspConjunction());
		assertEquals(0,cPunitType1Mars.getKp4Step1().getPlanetConjunction().size());
		assertEquals(null,cPunitType1Mars.getKp4Step1().getCuspAspect());	
		assertEquals(0,cPunitType1Mars.getKp4Step1().getPlanetAspect().size());
		
		
		//MERCURY
		BeanKP4Step cPunitType1Mer=cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.MERCURY);		
		assertTrue(!cPunitType1Mer.getKp4Step1().isPlanetStrong());
		assertNull(cPunitType1Mer.getKp4Step1().getPlanetStepValues());
		

		assertEquals( -1,cPunitType1Mer.getKp4Step1().getPlanetCuspConjunction());
		assertEquals(0,cPunitType1Mer.getKp4Step1().getPlanetConjunction().size());
		assertEquals(null,cPunitType1Mer.getKp4Step1().getCuspAspect());		
		//NOTE: ACCORDING TO CALCULATION IT IS CORRECT BUT SOFTWARE  USING  FOR  TESTING ,IT SHOULD BE NULL	
		assertEquals(GlobalVariablesKPExtension.JUPITER,cPunitType1Mer.getKp4Step1().getPlanetAspect().get(0).getKey());
		for(int i=0;i<cPunitType1Mer.getKp4Step1().getPlanetAspect().get(0).getValues().length;i++)
			assertTrue(cPunitType1Mer.getKp4Step1().getPlanetAspect().get(0).getValues()[i]==4||cPunitType1Mer.getKp4Step1().getPlanetAspect().get(0).getValues()[i]==10);
		
		//END
		
		//JUPITER
		BeanKP4Step cPunitType1Jup=cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.JUPITER);		
		assertTrue(!cPunitType1Jup.getKp4Step1().isPlanetStrong());
		assertNull(cPunitType1Jup.getKp4Step1().getPlanetStepValues());
		assertEquals( -1,cPunitType1Jup.getKp4Step1().getPlanetCuspConjunction());
		assertEquals(0,cPunitType1Jup.getKp4Step1().getPlanetConjunction().size());
		assertNull(cPunitType1Jup.getKp4Step1().getCuspAspect());	
		assertEquals(0,cPunitType1Jup.getKp4Step1().getPlanetAspect().size());
		
		
		
		//VENUS
		BeanKP4Step cPunitType1Ven=cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.VENUS);		
		assertTrue(!cPunitType1Ven.getKp4Step1().isPlanetStrong());
		assertNull(cPunitType1Ven.getKp4Step1().getPlanetStepValues());
		
		assertEquals( -1,cPunitType1Ven.getKp4Step1().getPlanetCuspConjunction());
		assertEquals(GlobalVariablesKPExtension.KETU,cPunitType1Ven.getKp4Step1().getPlanetConjunction().get(0).getKey());
		assertEquals(GlobalVariablesKPExtension.SAT,cPunitType1Ven.getKp4Step1().getPlanetConjunction().get(0).getPlanetInWhichRahuKetuPlaced());
			
		for(int i=0;i<cPunitType1Ven.getKp4Step1().getPlanetConjunction().get(0).gePlanetValuesInWhichRahuKetuPlaced().length;i++)
			assertTrue(cPunitType1Ven.getKp4Step1().getPlanetConjunction().get(0).gePlanetValuesInWhichRahuKetuPlaced()[i]==11);
		
		assertNull(cPunitType1Ven.getKp4Step1().getCuspAspect());			
		assertEquals( GlobalVariablesKPExtension.RAHU,cPunitType1Ven.getKp4Step1().getPlanetAspect().get(0).getKey());
		
		for(int i=0;i<cPunitType1Ven.getKp4Step1().getPlanetAspect().get(0).getValues().length;i++)
			assertTrue(cPunitType1Ven.getKp4Step1().getPlanetAspect().get(0).getValues()[i]==11);		
	
		
		assertEquals( GlobalVariablesKPExtension.SUN,cPunitType1Ven.getKp4Step1().getPlanetAspect().get(0).getPlanetInWhichRahuKetuPlaced());
		for(int i=0;i<cPunitType1Ven.getKp4Step1().getPlanetAspect().get(0).gePlanetValuesInWhichRahuKetuPlaced().length;i++)
			assertTrue(cPunitType1Ven.getKp4Step1().getPlanetAspect().get(0).gePlanetValuesInWhichRahuKetuPlaced()[i]==7);
		
        //SAT
		BeanKP4Step cPunitType1Sat=cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.SAT);		
		assertTrue(!cPunitType1Sat.getKp4Step1().isPlanetStrong());
		assertNull(cPunitType1Sat.getKp4Step1().getPlanetStepValues());		

		assertEquals( -1,cPunitType1Sat.getKp4Step1().getPlanetCuspConjunction());
		assertEquals(0,cPunitType1Sat.getKp4Step1().getPlanetConjunction().size());		
		assertNull(cPunitType1Sat.getKp4Step1().getCuspAspect());
	    assertEquals(0,cPunitType1Sat.getKp4Step1().getPlanetAspect().size());
		
		//RAHU
		BeanKP4Step cPunitType1Rahu=cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.RAHU);		
		assertTrue(cPunitType1Rahu.getKp4Step1().isPlanetStrong());
		assertNotNull(cPunitType1Rahu.getKp4Step1().getPlanetStepValues());
		for(int i=0;i<cPunitType1Rahu.getKp4Step1().getPlanetStepValues().length;i++)
			assertTrue(cPunitType1Rahu.getKp4Step1().getPlanetStepValues()[i]==11);
		
		KeyValue4Step objPunitRahu=cPunitType1Rahu.getKp4Step1().getRahuKetPlacedInPlanetRashi();
		assertEquals(objPunitRahu.getKey(), GlobalVariablesKPExtension.SUN);
		for(int i=0;i<objPunitRahu.getValues().length;i++)
			assertTrue(objPunitRahu.getValues()[i]==7);
		
		assertNotNull(cPunitType1Rahu.getKp4Step1().getCuspAspect());
		for(int i=0;i<cPunitType1Rahu.getKp4Step1().getCuspAspect().length;i++)
			assertTrue(cPunitType1Rahu.getKp4Step1().getCuspAspect()[i]==4||cPunitType1Rahu.getKp4Step1().getCuspAspect()[i]==8);
		
		assertEquals( -1,cPunitType1Rahu.getKp4Step1().getPlanetCuspConjunction());
		assertEquals(0,cPunitType1Rahu.getKp4Step1().getPlanetConjunction().size());		
		//assertNotNull(cPunitType1Rahu.getKp4Step1().getCuspAspect());
		for(int i=0;i<cPunitType1Rahu.getKp4Step1().getCuspAspect().length;i++)
			assertTrue(cPunitType1Rahu.getKp4Step1().getCuspAspect()[i]==4||cPunitType1Rahu.getKp4Step1().getCuspAspect()[i]==8);
		
	    assertEquals(0,cPunitType1Rahu.getKp4Step1().getPlanetAspect().size());
		
		
		//KETU
		BeanKP4Step cPunitType1Ketu=cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.KETU);		
		assertTrue(cPunitType1Ketu.getKp4Step1().isPlanetStrong());
		assertNotNull(cPunitType1Ketu.getKp4Step1().getPlanetStepValues());
		for(int i=0;i<cPunitType1Ketu.getKp4Step1().getPlanetStepValues().length;i++)
			assertTrue(cPunitType1Ketu.getKp4Step1().getPlanetStepValues()[i]==5);
		

		KeyValue4Step objPunitKetu=cPunitType1Ketu.getKp4Step1().getRahuKetPlacedInPlanetRashi();
		assertEquals(objPunitKetu.getKey(), GlobalVariablesKPExtension.SAT);
		for(int i=0;i<objPunitKetu.getValues().length;i++)
			assertTrue(objPunitKetu.getValues()[i]==11);
		
		
		assertEquals( -1,cPunitType1Ketu.getKp4Step1().getPlanetCuspConjunction());
		assertEquals(1,cPunitType1Ketu.getKp4Step1().getPlanetConjunction().size());		
		for(int i=0;i<cPunitType1Ketu.getKp4Step1().getCuspAspect().length;i++)
			assertTrue(cPunitType1Ketu.getKp4Step1().getCuspAspect()[i]==2||cPunitType1Ketu.getKp4Step1().getCuspAspect()[i]==10);
		
	    assertEquals(0,cPunitType1Ketu.getKp4Step1().getPlanetAspect().size());
		
	}
	public void get4StepType2Punit()
	{
		//SUN
		BeanKP4Step cPunitType2Sun=cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.SUN);		
		assertEquals(cPunitType2Sun.getKp4Step2().getPlanet(),GlobalVariablesKPExtension.MERCURY);
		for(int i=0;i<cPunitType2Sun.getKp4Step2().getPlanetStepValues().length;i++)
			assertTrue(cPunitType2Sun.getKp4Step2().getPlanetStepValues()[i]==1||cPunitType2Sun.getKp4Step2().getPlanetStepValues()[i]==6);
		
		assertEquals(-1,cPunitType2Sun.getKp4Step2().getPlanetCuspConjunction());
		assertEquals(0,cPunitType2Sun.getKp4Step2().getPlanetConjunction().size());
		assertEquals(null,cPunitType2Sun.getKp4Step2().getCuspAspect());	
		assertEquals(GlobalVariablesKPExtension.JUPITER,cPunitType2Sun.getKp4Step2().getPlanetAspect().get(0).getKey());		
		for(int i=0;i<cPunitType2Sun.getKp4Step2().getPlanetAspect().get(0).getValues().length;i++)
			assertTrue(cPunitType2Sun.getKp4Step2().getPlanetAspect().get(0).getValues()[i]==4||cPunitType2Sun.getKp4Step2().getPlanetAspect().get(0).getValues()[i]==10);
		
		
		
		//MOON
		BeanKP4Step cPunitType2Moon=cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.MOON);		
		assertEquals(cPunitType2Moon.getKp4Step2().getPlanet(),GlobalVariablesKPExtension.MOON);
		for(int i=0;i<cPunitType2Moon.getKp4Step2().getPlanetStepValues().length;i++)
			assertTrue(cPunitType2Moon.getKp4Step2().getPlanetStepValues()[i]==12);
		
		assertEquals(-1,cPunitType2Moon.getKp4Step2().getPlanetCuspConjunction());
		assertEquals(0,cPunitType2Moon.getKp4Step2().getPlanetConjunction().size());
		assertNull(cPunitType2Moon.getKp4Step2().getCuspAspect());		
		assertEquals(0,cPunitType2Moon.getKp4Step2().getPlanetAspect().size());
		
		

		//MARS
		BeanKP4Step cPunitType2Mars=cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.MARS);		
		assertEquals(cPunitType2Mars.getKp4Step2().getPlanet(),GlobalVariablesKPExtension.SAT);
	
		for(int i=0;i<cPunitType2Mars.getKp4Step2().getPlanetStepValues().length;i++)
			assertTrue(cPunitType2Mars.getKp4Step2().getPlanetStepValues()[i]==11);
		
		assertEquals(-1,cPunitType2Mars.getKp4Step2().getPlanetCuspConjunction());
		assertEquals(0,cPunitType2Mars.getKp4Step2().getPlanetConjunction().size());
		assertNull(cPunitType2Mars.getKp4Step2().getCuspAspect());		
		assertEquals(0,cPunitType2Mars.getKp4Step2().getPlanetAspect().size());
		
		
		
		//MERCURY
		BeanKP4Step cPunitType2Mer=cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.MERCURY);		
		assertEquals(cPunitType2Mer.getKp4Step2().getPlanet(),GlobalVariablesKPExtension.JUPITER);
	
		for(int i=0;i<cPunitType2Mer.getKp4Step2().getPlanetStepValues().length;i++)
			assertTrue(cPunitType2Mer.getKp4Step2().getPlanetStepValues()[i]==4||cPunitType2Mer.getKp4Step2().getPlanetStepValues()[i]==10);
		
		assertEquals(-1,cPunitType2Mer.getKp4Step2().getPlanetCuspConjunction());
		assertEquals(0,cPunitType2Mer.getKp4Step2().getPlanetConjunction().size());
		assertNull(cPunitType2Mer.getKp4Step2().getCuspAspect());		
		assertEquals(0,cPunitType2Mer.getKp4Step2().getPlanetAspect().size());
		
		
		//JUPITER
		BeanKP4Step cPunitType2jup=cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.JUPITER);		
		assertEquals(cPunitType2jup.getKp4Step2().getPlanet(),GlobalVariablesKPExtension.SAT);
	
		for(int i=0;i<cPunitType2jup.getKp4Step2().getPlanetStepValues().length;i++)
			assertTrue(cPunitType2jup.getKp4Step2().getPlanetStepValues()[i]==11);
		
		assertEquals(-1,cPunitType2jup.getKp4Step2().getPlanetCuspConjunction());
		assertEquals(0,cPunitType2jup.getKp4Step2().getPlanetConjunction().size());
		assertNull(cPunitType2jup.getKp4Step2().getCuspAspect());		
		assertEquals(0,cPunitType2jup.getKp4Step2().getPlanetAspect().size());
		
		
		//VENUS
		BeanKP4Step cPunitType2Ven=cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.VENUS);		
		assertEquals(cPunitType2Ven.getKp4Step2().getPlanet(),GlobalVariablesKPExtension.JUPITER);
		
		for(int i=0;i<cPunitType2Ven.getKp4Step2().getPlanetStepValues().length;i++)
			assertTrue(cPunitType2Ven.getKp4Step2().getPlanetStepValues()[i]==4||cPunitType2Mer.getKp4Step2().getPlanetStepValues()[i]==10);
		
	
		assertEquals(-1,cPunitType2Ven.getKp4Step2().getPlanetCuspConjunction());
		assertEquals(0,cPunitType2Ven.getKp4Step2().getPlanetConjunction().size());
		assertNull(cPunitType2Ven.getKp4Step2().getCuspAspect());		
		assertEquals(0,cPunitType2Ven.getKp4Step2().getPlanetAspect().size());
		
		//SAT
		BeanKP4Step cPunitType2Sat=cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.SAT);		
		assertEquals(cPunitType2Sat.getKp4Step2().getPlanet(),GlobalVariablesKPExtension.VENUS);

		for(int i=0;i<cPunitType2Sat.getKp4Step2().getPlanetStepValues().length;i++)
			assertTrue(cPunitType2Sat.getKp4Step2().getPlanetStepValues()[i]==2||
					cPunitType2Sat.getKp4Step2().getPlanetStepValues()[i]==5||
					cPunitType2Sat.getKp4Step2().getPlanetStepValues()[i]==9);
		
		assertEquals(-1,cPunitType2Sat.getKp4Step2().getPlanetCuspConjunction());
		assertEquals(GlobalVariablesKPExtension.KETU,cPunitType2Sat.getKp4Step2().getPlanetConjunction().get(0).getKey());
		for(int i=0;i<cPunitType2Sat.getKp4Step2().getPlanetConjunction().get(0).getValues().length;i++)
			assertTrue(cPunitType2Sat.getKp4Step2().getPlanetConjunction().get(0).getValues()[i]==5);
		
		assertEquals(GlobalVariablesKPExtension.SAT,cPunitType2Sat.getKp4Step2().getPlanetConjunction().get(0).getPlanetInWhichRahuKetuPlaced());
		for(int i=0;i<cPunitType2Sat.getKp4Step2().getPlanetConjunction().get(0).gePlanetValuesInWhichRahuKetuPlaced().length;i++)
			assertTrue(cPunitType2Sat.getKp4Step2().getPlanetConjunction().get(0).gePlanetValuesInWhichRahuKetuPlaced()[i]==11);
		
		assertEquals(GlobalVariablesKPExtension.RAHU,cPunitType2Sat.getKp4Step2().getPlanetAspect().get(0).getKey());
		for(int i=0;i<cPunitType2Sat.getKp4Step2().getPlanetAspect().get(0).getValues().length;i++)
			assertTrue(cPunitType2Sat.getKp4Step2().getPlanetAspect().get(0).getValues()[i]==11);
		
		
		assertEquals(GlobalVariablesKPExtension.SUN,cPunitType2Sat.getKp4Step2().getPlanetAspect().get(0).getPlanetInWhichRahuKetuPlaced());
		for(int i=0;i<cPunitType2Sat.getKp4Step2().getPlanetAspect().get(0).gePlanetValuesInWhichRahuKetuPlaced().length;i++)
			assertTrue(cPunitType2Sat.getKp4Step2().getPlanetAspect().get(0).gePlanetValuesInWhichRahuKetuPlaced()[i]==7);
		
		
		
		//RAHU
		BeanKP4Step cPunitType2Rahu=cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.RAHU);		
		assertEquals(cPunitType2Rahu.getKp4Step2().getPlanet(),GlobalVariablesKPExtension.VENUS);
		for(int i=0;i<cPunitType2Rahu.getKp4Step2().getPlanetStepValues().length;i++)
			assertTrue(cPunitType2Rahu.getKp4Step2().getPlanetStepValues()[i]==2||
					cPunitType2Rahu.getKp4Step2().getPlanetStepValues()[i]==5||
							cPunitType2Rahu.getKp4Step2().getPlanetStepValues()[i]==9);
		
		assertEquals(-1,cPunitType2Rahu.getKp4Step2().getPlanetCuspConjunction());
		assertEquals(GlobalVariablesKPExtension.KETU,cPunitType2Rahu.getKp4Step2().getPlanetConjunction().get(0).getKey());
		for(int i=0;i<cPunitType2Rahu.getKp4Step2().getPlanetConjunction().get(0).getValues().length;i++)
			assertTrue(cPunitType2Rahu.getKp4Step2().getPlanetConjunction().get(0).getValues()[i]==5);
		
		assertEquals(GlobalVariablesKPExtension.SAT,cPunitType2Rahu.getKp4Step2().getPlanetConjunction().get(0).getPlanetInWhichRahuKetuPlaced());
		for(int i=0;i<cPunitType2Rahu.getKp4Step2().getPlanetConjunction().get(0).gePlanetValuesInWhichRahuKetuPlaced().length;i++)
			assertTrue(cPunitType2Rahu.getKp4Step2().getPlanetConjunction().get(0).gePlanetValuesInWhichRahuKetuPlaced()[i]==11);
		
		assertEquals(GlobalVariablesKPExtension.RAHU,cPunitType2Rahu.getKp4Step2().getPlanetAspect().get(0).getKey());
		for(int i=0;i<cPunitType2Rahu.getKp4Step2().getPlanetAspect().get(0).getValues().length;i++)
			assertTrue(cPunitType2Rahu.getKp4Step2().getPlanetAspect().get(0).getValues()[i]==11);
		
		
		assertEquals(GlobalVariablesKPExtension.SUN,cPunitType2Rahu.getKp4Step2().getPlanetAspect().get(0).getPlanetInWhichRahuKetuPlaced());
		for(int i=0;i<cPunitType2Rahu.getKp4Step2().getPlanetAspect().get(0).gePlanetValuesInWhichRahuKetuPlaced().length;i++)
			assertTrue(cPunitType2Rahu.getKp4Step2().getPlanetAspect().get(0).gePlanetValuesInWhichRahuKetuPlaced()[i]==7);
		
		
	
		//KETU
		BeanKP4Step cPunitType2Ketu=cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.KETU);		
		assertEquals(cPunitType2Ketu.getKp4Step2().getPlanet(),GlobalVariablesKPExtension.JUPITER);
	
		for(int i=0;i<cPunitType2Ketu.getKp4Step2().getPlanetStepValues().length;i++)
			assertTrue(cPunitType2Ketu.getKp4Step2().getPlanetStepValues()[i]==4||cPunitType2Ketu.getKp4Step2().getPlanetStepValues()[i]==10);
		
		assertEquals(-1,cPunitType2Ketu.getKp4Step2().getPlanetCuspConjunction());
		assertEquals(0,cPunitType2Ketu.getKp4Step2().getPlanetConjunction().size());
		assertNull(cPunitType2Ketu.getKp4Step2().getCuspAspect());		
		assertEquals(0,cPunitType2Ketu.getKp4Step2().getPlanetAspect().size());
				
		
	}
	public void get4StepType3Punit()
	{
		//SUN
		BeanKP4Step cPunitType3Sun=cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.SUN);		
		assertEquals(cPunitType3Sun.getKp4Step3().getPlanet(),GlobalVariablesKPExtension.JUPITER);
		assertNull(cPunitType3Sun.getKp4Step3().getPlanetStepValues());
		assertEquals( -1,cPunitType3Sun.getKp4Step3().getPlanetCuspConjunction());
		assertEquals(0,cPunitType3Sun.getKp4Step3().getPlanetConjunction().size());
		assertNull(cPunitType3Sun.getKp4Step3().getCuspAspect());	
		assertEquals(0,cPunitType3Sun.getKp4Step3().getPlanetAspect().size());
		
		//MOON
		BeanKP4Step cPunitType3Moon=cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.MOON);		
		assertEquals(cPunitType3Moon.getKp4Step3().getPlanet(),GlobalVariablesKPExtension.SAT);
		assertNull(cPunitType3Moon.getKp4Step3().getPlanetStepValues());
		assertEquals( -1,cPunitType3Moon.getKp4Step3().getPlanetCuspConjunction());
		assertEquals(0,cPunitType3Moon.getKp4Step3().getPlanetConjunction().size());		
		assertNull(cPunitType3Moon.getKp4Step3().getCuspAspect());
	    assertEquals(0,cPunitType3Moon.getKp4Step3().getPlanetAspect().size());
	    
	    

		//MARS
		BeanKP4Step cPunitType3Mars=cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.MARS);		
		assertEquals(cPunitType3Mars.getKp4Step3().getPlanet(),GlobalVariablesKPExtension.VENUS);
		assertNull(cPunitType3Mars.getKp4Step3().getPlanetStepValues());
		
		assertEquals( -1,cPunitType3Mars.getKp4Step3().getPlanetCuspConjunction());
		assertEquals(GlobalVariablesKPExtension.KETU,cPunitType3Mars.getKp4Step3().getPlanetConjunction().get(0).getKey());
		assertEquals(GlobalVariablesKPExtension.SAT,cPunitType3Mars.getKp4Step3().getPlanetConjunction().get(0).getPlanetInWhichRahuKetuPlaced());
			
		for(int i=0;i<cPunitType3Mars.getKp4Step3().getPlanetConjunction().get(0).gePlanetValuesInWhichRahuKetuPlaced().length;i++)
			assertTrue(cPunitType3Mars.getKp4Step3().getPlanetConjunction().get(0).gePlanetValuesInWhichRahuKetuPlaced()[i]==11);
		
		assertNull(cPunitType3Mars.getKp4Step3().getCuspAspect());			
		assertEquals( GlobalVariablesKPExtension.RAHU,cPunitType3Mars.getKp4Step3().getPlanetAspect().get(0).getKey());
		
		for(int i=0;i<cPunitType3Mars.getKp4Step3().getPlanetAspect().get(0).getValues().length;i++)
			assertTrue(cPunitType3Mars.getKp4Step3().getPlanetAspect().get(0).getValues()[i]==11);		
	
		
		assertEquals( GlobalVariablesKPExtension.SUN,cPunitType3Mars.getKp4Step3().getPlanetAspect().get(0).getPlanetInWhichRahuKetuPlaced());
		for(int i=0;i<cPunitType3Mars.getKp4Step3().getPlanetAspect().get(0).gePlanetValuesInWhichRahuKetuPlaced().length;i++)
			assertTrue(cPunitType3Mars.getKp4Step3().getPlanetAspect().get(0).gePlanetValuesInWhichRahuKetuPlaced()[i]==7);
		
		//MERCURY
		BeanKP4Step cPunitType3Mer=cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.MERCURY);		
		assertEquals(cPunitType3Mer.getKp4Step3().getPlanet(),GlobalVariablesKPExtension.RAHU);
		
		assertNotNull(cPunitType3Mer.getKp4Step3().getPlanetStepValues());
		for(int i=0;i<cPunitType3Mer.getKp4Step3().getPlanetStepValues().length;i++)
			assertTrue(cPunitType3Mer.getKp4Step3().getPlanetStepValues()[i]==11);
		
		KeyValue4Step objPunitRahu=cPunitType3Mer.getKp4Step3().getRahuKetPlacedInPlanetRashi();
		assertEquals(objPunitRahu.getKey(), GlobalVariablesKPExtension.SUN);
		for(int i=0;i<objPunitRahu.getValues().length;i++)
			assertTrue(objPunitRahu.getValues()[i]==7);
		
		assertNotNull(cPunitType3Mer.getKp4Step3().getCuspAspect());
		for(int i=0;i<cPunitType3Mer.getKp4Step3().getCuspAspect().length;i++)
			assertTrue(cPunitType3Mer.getKp4Step3().getCuspAspect()[i]==4||cPunitType3Mer.getKp4Step3().getCuspAspect()[i]==8);
		
		assertEquals( -1,cPunitType3Mer.getKp4Step3().getPlanetCuspConjunction());
		assertEquals(0,cPunitType3Mer.getKp4Step3().getPlanetConjunction().size());		
		assertNotNull(cPunitType3Mer.getKp4Step3().getCuspAspect());
		for(int i=0;i<cPunitType3Mer.getKp4Step3().getCuspAspect().length;i++)
			assertTrue(cPunitType3Mer.getKp4Step3().getCuspAspect()[i]==4||cPunitType3Mer.getKp4Step3().getCuspAspect()[i]==8);
		
	    assertEquals(0,cPunitType3Mer.getKp4Step3().getPlanetAspect().size());
	    
	    
	  //JUPITER
		BeanKP4Step cPunitType3Jup=cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.JUPITER);		
		assertEquals(cPunitType3Jup.getKp4Step3().getPlanet(),GlobalVariablesKPExtension.MERCURY);
		assertNull(cPunitType3Jup.getKp4Step3().getPlanetStepValues());
		
	
		assertEquals( -1,cPunitType3Jup.getKp4Step3().getPlanetCuspConjunction());
		assertEquals(0,cPunitType3Jup.getKp4Step3().getPlanetConjunction().size());
		assertEquals(null,cPunitType3Jup.getKp4Step3().getCuspAspect());		
		/*NOTE: ACCORDING TO CALCULATION IT IS CORRECT BUT SOFTWARE  USING  FOR  TESTING ,IT SHOULD BE NULL*/	
		assertEquals(GlobalVariablesKPExtension.JUPITER,cPunitType3Jup.getKp4Step3().getPlanetAspect().get(0).getKey());
		for(int i=0;i<cPunitType3Jup.getKp4Step3().getPlanetAspect().get(0).getValues().length;i++)
			assertTrue(cPunitType3Jup.getKp4Step3().getPlanetAspect().get(0).getValues()[i]==4||cPunitType3Jup.getKp4Step3().getPlanetAspect().get(0).getValues()[i]==10);
		
		//END
		
		//VENUS
		BeanKP4Step cPunitType3Ven=cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.VENUS);		
		assertEquals(cPunitType3Ven.getKp4Step3().getPlanet(),GlobalVariablesKPExtension.SAT);
		assertNull(cPunitType3Ven.getKp4Step3().getPlanetStepValues());		

		assertEquals( -1,cPunitType3Ven.getKp4Step3().getPlanetCuspConjunction());
		assertEquals(0,cPunitType3Ven.getKp4Step3().getPlanetConjunction().size());		
		assertNull(cPunitType3Ven.getKp4Step3().getCuspAspect());
	    assertEquals(0,cPunitType3Ven.getKp4Step3().getPlanetAspect().size());
	    
	    
	   //SAT
  		BeanKP4Step cPunitType3Sat=cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.SAT);		
  		assertEquals(cPunitType3Sat.getKp4Step3().getPlanet(),GlobalVariablesKPExtension.VENUS);
  		assertNull(cPunitType3Sat.getKp4Step3().getPlanetStepValues());
		
		assertEquals( -1,cPunitType3Sat.getKp4Step3().getPlanetCuspConjunction());
		assertEquals(GlobalVariablesKPExtension.KETU,cPunitType3Sat.getKp4Step3().getPlanetConjunction().get(0).getKey());
		assertEquals(GlobalVariablesKPExtension.SAT,cPunitType3Sat.getKp4Step3().getPlanetConjunction().get(0).getPlanetInWhichRahuKetuPlaced());
			
		for(int i=0;i<cPunitType3Sat.getKp4Step3().getPlanetConjunction().get(0).gePlanetValuesInWhichRahuKetuPlaced().length;i++)
			assertTrue(cPunitType3Sat.getKp4Step3().getPlanetConjunction().get(0).gePlanetValuesInWhichRahuKetuPlaced()[i]==11);
		
		assertNull(cPunitType3Sat.getKp4Step3().getCuspAspect());			
		assertEquals( GlobalVariablesKPExtension.RAHU,cPunitType3Sat.getKp4Step3().getPlanetAspect().get(0).getKey());
		
		for(int i=0;i<cPunitType3Sat.getKp4Step3().getPlanetAspect().get(0).getValues().length;i++)
			assertTrue(cPunitType3Sat.getKp4Step3().getPlanetAspect().get(0).getValues()[i]==11);		
	
		
		assertEquals( GlobalVariablesKPExtension.SUN,cPunitType3Sat.getKp4Step3().getPlanetAspect().get(0).getPlanetInWhichRahuKetuPlaced());
		for(int i=0;i<cPunitType3Sat.getKp4Step3().getPlanetAspect().get(0).gePlanetValuesInWhichRahuKetuPlaced().length;i++)
			assertTrue(cPunitType3Sat.getKp4Step3().getPlanetAspect().get(0).gePlanetValuesInWhichRahuKetuPlaced()[i]==7);
		
		
		
		 //RAHU
  		BeanKP4Step cPunitType3Rahu=cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.RAHU);		
  		assertEquals(cPunitType3Rahu.getKp4Step3().getPlanet(),GlobalVariablesKPExtension.SAT);
  		assertNull(cPunitType3Rahu.getKp4Step3().getPlanetStepValues());		

		assertEquals( -1,cPunitType3Rahu.getKp4Step3().getPlanetCuspConjunction());
		assertEquals(0,cPunitType3Rahu.getKp4Step3().getPlanetConjunction().size());		
		assertNull(cPunitType3Rahu.getKp4Step3().getCuspAspect());
	    assertEquals(0,cPunitType3Rahu.getKp4Step3().getPlanetAspect().size());
	    
	    //KETU
  		BeanKP4Step cPunitType3Ketu=cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.KETU);		
  		assertEquals(cPunitType3Ketu.getKp4Step3().getPlanet(),GlobalVariablesKPExtension.SAT);
  		assertNull(cPunitType3Ketu.getKp4Step3().getPlanetStepValues());		

		assertEquals( -1,cPunitType3Ketu.getKp4Step3().getPlanetCuspConjunction());
		assertEquals(0,cPunitType3Ketu.getKp4Step3().getPlanetConjunction().size());		
		assertNull(cPunitType3Ketu.getKp4Step3().getCuspAspect());
	    assertEquals(0,cPunitType3Ketu.getKp4Step3().getPlanetAspect().size());
	}
	public void get4StepType4Punit()
	{
		//SUN
		BeanKP4Step cPunitType4Sun=cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.SUN);		
		assertEquals(cPunitType4Sun.getKp4Step4().getPlanet(),GlobalVariablesKPExtension.SAT);

		for(int i=0;i<cPunitType4Sun.getKp4Step4().getPlanetStepValues().length;i++)
			assertTrue(cPunitType4Sun.getKp4Step4().getPlanetStepValues()[i]==11);
		
		assertEquals(-1,cPunitType4Sun.getKp4Step4().getPlanetCuspConjunction());
		assertEquals(0,cPunitType4Sun.getKp4Step4().getPlanetConjunction().size());
		assertNull(cPunitType4Sun.getKp4Step4().getCuspAspect());		
		assertEquals(0,cPunitType4Sun.getKp4Step4().getPlanetAspect().size());
		
		//MOON
		BeanKP4Step cPunitType4Moon=cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.MOON);		
		assertEquals(cPunitType4Moon.getKp4Step4().getPlanet(),GlobalVariablesKPExtension.VENUS);

		for(int i=0;i<cPunitType4Moon.getKp4Step4().getPlanetStepValues().length;i++)
			assertTrue(cPunitType4Moon.getKp4Step4().getPlanetStepValues()[i]==2||
					cPunitType4Moon.getKp4Step4().getPlanetStepValues()[i]==5||
							cPunitType4Moon.getKp4Step4().getPlanetStepValues()[i]==9);
		
		assertEquals(-1,cPunitType4Moon.getKp4Step4().getPlanetCuspConjunction());
		assertEquals(GlobalVariablesKPExtension.KETU,cPunitType4Moon.getKp4Step4().getPlanetConjunction().get(0).getKey());
		for(int i=0;i<cPunitType4Moon.getKp4Step4().getPlanetConjunction().get(0).getValues().length;i++)
			assertTrue(cPunitType4Moon.getKp4Step4().getPlanetConjunction().get(0).getValues()[i]==5);
		
		assertEquals(GlobalVariablesKPExtension.SAT,cPunitType4Moon.getKp4Step4().getPlanetConjunction().get(0).getPlanetInWhichRahuKetuPlaced());
		for(int i=0;i<cPunitType4Moon.getKp4Step4().getPlanetConjunction().get(0).gePlanetValuesInWhichRahuKetuPlaced().length;i++)
			assertTrue(cPunitType4Moon.getKp4Step4().getPlanetConjunction().get(0).gePlanetValuesInWhichRahuKetuPlaced()[i]==11);
		
		assertEquals(GlobalVariablesKPExtension.RAHU,cPunitType4Moon.getKp4Step4().getPlanetAspect().get(0).getKey());
		for(int i=0;i<cPunitType4Moon.getKp4Step4().getPlanetAspect().get(0).getValues().length;i++)
			assertTrue(cPunitType4Moon.getKp4Step4().getPlanetAspect().get(0).getValues()[i]==11);
		
		
		assertEquals(GlobalVariablesKPExtension.SUN,cPunitType4Moon.getKp4Step4().getPlanetAspect().get(0).getPlanetInWhichRahuKetuPlaced());
		for(int i=0;i<cPunitType4Moon.getKp4Step4().getPlanetAspect().get(0).gePlanetValuesInWhichRahuKetuPlaced().length;i++)
			assertTrue(cPunitType4Moon.getKp4Step4().getPlanetAspect().get(0).gePlanetValuesInWhichRahuKetuPlaced()[i]==7);
		
		
		
		//MARS
		BeanKP4Step cPunitType4Mars=cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.MARS);		
		assertEquals(cPunitType4Mars.getKp4Step4().getPlanet(),GlobalVariablesKPExtension.JUPITER);

		for(int i=0;i<cPunitType4Mars.getKp4Step4().getPlanetStepValues().length;i++)
			assertTrue(cPunitType4Mars.getKp4Step4().getPlanetStepValues()[i]==4||cPunitType4Mars.getKp4Step4().getPlanetStepValues()[i]==10);
		
		assertEquals(-1,cPunitType4Mars.getKp4Step4().getPlanetCuspConjunction());
		assertEquals(0,cPunitType4Mars.getKp4Step4().getPlanetConjunction().size());
		assertNull(cPunitType4Mars.getKp4Step4().getCuspAspect());		
		assertEquals(0,cPunitType4Mars.getKp4Step4().getPlanetAspect().size());
		
		//MERCURY
		BeanKP4Step cPunitType4Mer=cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.MERCURY);		
		assertEquals(cPunitType4Mer.getKp4Step4().getPlanet(),GlobalVariablesKPExtension.VENUS);
		
		for(int i=0;i<cPunitType4Mer.getKp4Step4().getPlanetStepValues().length;i++)
			assertTrue(cPunitType4Mer.getKp4Step4().getPlanetStepValues()[i]==2||
					cPunitType4Mer.getKp4Step4().getPlanetStepValues()[i]==5||
							cPunitType4Mer.getKp4Step4().getPlanetStepValues()[i]==9);
		
		assertEquals(-1,cPunitType4Mer.getKp4Step4().getPlanetCuspConjunction());
		assertEquals(GlobalVariablesKPExtension.KETU,cPunitType4Mer.getKp4Step4().getPlanetConjunction().get(0).getKey());
		for(int i=0;i<cPunitType4Mer.getKp4Step4().getPlanetConjunction().get(0).getValues().length;i++)
			assertTrue(cPunitType4Mer.getKp4Step4().getPlanetConjunction().get(0).getValues()[i]==5);
		
		assertEquals(GlobalVariablesKPExtension.SAT,cPunitType4Mer.getKp4Step4().getPlanetConjunction().get(0).getPlanetInWhichRahuKetuPlaced());
		for(int i=0;i<cPunitType4Mer.getKp4Step4().getPlanetConjunction().get(0).gePlanetValuesInWhichRahuKetuPlaced().length;i++)
			assertTrue(cPunitType4Mer.getKp4Step4().getPlanetConjunction().get(0).gePlanetValuesInWhichRahuKetuPlaced()[i]==11);
		
		assertEquals(GlobalVariablesKPExtension.RAHU,cPunitType4Mer.getKp4Step4().getPlanetAspect().get(0).getKey());
		for(int i=0;i<cPunitType4Mer.getKp4Step4().getPlanetAspect().get(0).getValues().length;i++)
			assertTrue(cPunitType4Mer.getKp4Step4().getPlanetAspect().get(0).getValues()[i]==11);
		
		
		assertEquals(GlobalVariablesKPExtension.SUN,cPunitType4Mer.getKp4Step4().getPlanetAspect().get(0).getPlanetInWhichRahuKetuPlaced());
		for(int i=0;i<cPunitType4Mer.getKp4Step4().getPlanetAspect().get(0).gePlanetValuesInWhichRahuKetuPlaced().length;i++)
			assertTrue(cPunitType4Mer.getKp4Step4().getPlanetAspect().get(0).gePlanetValuesInWhichRahuKetuPlaced()[i]==7);
		
		
		//JUPITER
		BeanKP4Step cPunitType4Jup=cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.JUPITER);		
		assertEquals(cPunitType4Jup.getKp4Step4().getPlanet(),GlobalVariablesKPExtension.JUPITER);
		for(int i=0;i<cPunitType4Jup.getKp4Step4().getPlanetStepValues().length;i++)
			assertTrue(cPunitType4Jup.getKp4Step4().getPlanetStepValues()[i]==4||cPunitType4Jup.getKp4Step4().getPlanetStepValues()[i]==10);
		
		assertEquals(-1,cPunitType4Jup.getKp4Step4().getPlanetCuspConjunction());
		assertEquals(0,cPunitType4Jup.getKp4Step4().getPlanetConjunction().size());
		assertNull(cPunitType4Jup.getKp4Step4().getCuspAspect());		
		assertEquals(0,cPunitType4Jup.getKp4Step4().getPlanetAspect().size());
		
		
		//VENUS
		BeanKP4Step cPunitType4Ven=cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.VENUS);		
		assertEquals(cPunitType4Ven.getKp4Step4().getPlanet(),GlobalVariablesKPExtension.VENUS);

		for(int i=0;i<cPunitType4Ven.getKp4Step4().getPlanetStepValues().length;i++)
			assertTrue(cPunitType4Ven.getKp4Step4().getPlanetStepValues()[i]==2||
					cPunitType4Ven.getKp4Step4().getPlanetStepValues()[i]==5||
							cPunitType4Ven.getKp4Step4().getPlanetStepValues()[i]==9);
		
		assertEquals(-1,cPunitType4Ven.getKp4Step4().getPlanetCuspConjunction());
		assertEquals(GlobalVariablesKPExtension.KETU,cPunitType4Ven.getKp4Step4().getPlanetConjunction().get(0).getKey());
		for(int i=0;i<cPunitType4Ven.getKp4Step4().getPlanetConjunction().get(0).getValues().length;i++)
			assertTrue(cPunitType4Ven.getKp4Step4().getPlanetConjunction().get(0).getValues()[i]==5);
		
		assertEquals(GlobalVariablesKPExtension.SAT,cPunitType4Ven.getKp4Step4().getPlanetConjunction().get(0).getPlanetInWhichRahuKetuPlaced());
		for(int i=0;i<cPunitType4Ven.getKp4Step4().getPlanetConjunction().get(0).gePlanetValuesInWhichRahuKetuPlaced().length;i++)
			assertTrue(cPunitType4Ven.getKp4Step4().getPlanetConjunction().get(0).gePlanetValuesInWhichRahuKetuPlaced()[i]==11);
		
		assertEquals(GlobalVariablesKPExtension.RAHU,cPunitType4Ven.getKp4Step4().getPlanetAspect().get(0).getKey());
		for(int i=0;i<cPunitType4Ven.getKp4Step4().getPlanetAspect().get(0).getValues().length;i++)
			assertTrue(cPunitType4Ven.getKp4Step4().getPlanetAspect().get(0).getValues()[i]==11);
		
		
		assertEquals(GlobalVariablesKPExtension.SUN,cPunitType4Ven.getKp4Step4().getPlanetAspect().get(0).getPlanetInWhichRahuKetuPlaced());
		for(int i=0;i<cPunitType4Ven.getKp4Step4().getPlanetAspect().get(0).gePlanetValuesInWhichRahuKetuPlaced().length;i++)
			assertTrue(cPunitType4Ven.getKp4Step4().getPlanetAspect().get(0).gePlanetValuesInWhichRahuKetuPlaced()[i]==7);
		
		
		
		//SAT
		BeanKP4Step cPunitType4Sat=cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.SAT);		
		assertEquals(cPunitType4Sat.getKp4Step4().getPlanet(),GlobalVariablesKPExtension.JUPITER);
		for(int i=0;i<cPunitType4Sat.getKp4Step4().getPlanetStepValues().length;i++)
			assertTrue(cPunitType4Sat.getKp4Step4().getPlanetStepValues()[i]==4||cPunitType4Sat.getKp4Step4().getPlanetStepValues()[i]==10);
		
		assertEquals(-1,cPunitType4Sat.getKp4Step4().getPlanetCuspConjunction());
		assertEquals(0,cPunitType4Sat.getKp4Step4().getPlanetConjunction().size());
		assertNull(cPunitType4Sat.getKp4Step4().getCuspAspect());		
		assertEquals(0,cPunitType4Sat.getKp4Step4().getPlanetAspect().size());
		
		
		//RAHU
		BeanKP4Step cPunitType4Rahu=cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.RAHU);		
		assertEquals(cPunitType4Rahu.getKp4Step4().getPlanet(),GlobalVariablesKPExtension.VENUS);

		for(int i=0;i<cPunitType4Rahu.getKp4Step4().getPlanetStepValues().length;i++)
			assertTrue(cPunitType4Rahu.getKp4Step4().getPlanetStepValues()[i]==2||
					cPunitType4Rahu.getKp4Step4().getPlanetStepValues()[i]==5||
							cPunitType4Rahu.getKp4Step4().getPlanetStepValues()[i]==9);
		
		assertEquals(-1,cPunitType4Rahu.getKp4Step4().getPlanetCuspConjunction());
		assertEquals(GlobalVariablesKPExtension.KETU,cPunitType4Rahu.getKp4Step4().getPlanetConjunction().get(0).getKey());
		for(int i=0;i<cPunitType4Rahu.getKp4Step4().getPlanetConjunction().get(0).getValues().length;i++)
			assertTrue(cPunitType4Rahu.getKp4Step4().getPlanetConjunction().get(0).getValues()[i]==5);
		
		assertEquals(GlobalVariablesKPExtension.SAT,cPunitType4Rahu.getKp4Step4().getPlanetConjunction().get(0).getPlanetInWhichRahuKetuPlaced());
		for(int i=0;i<cPunitType4Rahu.getKp4Step4().getPlanetConjunction().get(0).gePlanetValuesInWhichRahuKetuPlaced().length;i++)
			assertTrue(cPunitType4Rahu.getKp4Step4().getPlanetConjunction().get(0).gePlanetValuesInWhichRahuKetuPlaced()[i]==11);
		
		assertEquals(GlobalVariablesKPExtension.RAHU,cPunitType4Rahu.getKp4Step4().getPlanetAspect().get(0).getKey());
		for(int i=0;i<cPunitType4Rahu.getKp4Step4().getPlanetAspect().get(0).getValues().length;i++)
			assertTrue(cPunitType4Rahu.getKp4Step4().getPlanetAspect().get(0).getValues()[i]==11);
		
		
		assertEquals(GlobalVariablesKPExtension.SUN,cPunitType4Rahu.getKp4Step4().getPlanetAspect().get(0).getPlanetInWhichRahuKetuPlaced());
		for(int i=0;i<cPunitType4Rahu.getKp4Step4().getPlanetAspect().get(0).gePlanetValuesInWhichRahuKetuPlaced().length;i++)
			assertTrue(cPunitType4Rahu.getKp4Step4().getPlanetAspect().get(0).gePlanetValuesInWhichRahuKetuPlaced()[i]==7);
		
		
		//KETU
		BeanKP4Step cPunitType4Ketu=cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.KETU);		
		assertEquals(cPunitType4Ketu.getKp4Step4().getPlanet(),GlobalVariablesKPExtension.VENUS);

		for(int i=0;i<cPunitType4Ketu.getKp4Step4().getPlanetStepValues().length;i++)
			assertTrue(cPunitType4Ketu.getKp4Step4().getPlanetStepValues()[i]==2||
					cPunitType4Ketu.getKp4Step4().getPlanetStepValues()[i]==5||
							cPunitType4Ketu.getKp4Step4().getPlanetStepValues()[i]==9);
		
		assertEquals(-1,cPunitType4Ketu.getKp4Step4().getPlanetCuspConjunction());
		assertEquals(GlobalVariablesKPExtension.KETU,cPunitType4Ketu.getKp4Step4().getPlanetConjunction().get(0).getKey());
		for(int i=0;i<cPunitType4Ketu.getKp4Step4().getPlanetConjunction().get(0).getValues().length;i++)
			assertTrue(cPunitType4Ketu.getKp4Step4().getPlanetConjunction().get(0).getValues()[i]==5);
		
		assertEquals(GlobalVariablesKPExtension.SAT,cPunitType4Ketu.getKp4Step4().getPlanetConjunction().get(0).getPlanetInWhichRahuKetuPlaced());
		for(int i=0;i<cPunitType4Ketu.getKp4Step4().getPlanetConjunction().get(0).gePlanetValuesInWhichRahuKetuPlaced().length;i++)
			assertTrue(cPunitType4Ketu.getKp4Step4().getPlanetConjunction().get(0).gePlanetValuesInWhichRahuKetuPlaced()[i]==11);
		
		assertEquals(GlobalVariablesKPExtension.RAHU,cPunitType4Ketu.getKp4Step4().getPlanetAspect().get(0).getKey());
		for(int i=0;i<cPunitType4Ketu.getKp4Step4().getPlanetAspect().get(0).getValues().length;i++)
			assertTrue(cPunitType4Ketu.getKp4Step4().getPlanetAspect().get(0).getValues()[i]==11);
		
		
		assertEquals(GlobalVariablesKPExtension.SUN,cPunitType4Ketu.getKp4Step4().getPlanetAspect().get(0).getPlanetInWhichRahuKetuPlaced());
		for(int i=0;i<cPunitType4Ketu.getKp4Step4().getPlanetAspect().get(0).gePlanetValuesInWhichRahuKetuPlaced().length;i++)
			assertTrue(cPunitType4Ketu.getKp4Step4().getPlanetAspect().get(0).gePlanetValuesInWhichRahuKetuPlaced()[i]==7);
		
		
		
		
	}
	
	/*********************HUKUM****************/
	public void get4StepType1Hukum()
	{		
		
		//SUN
		BeanKP4Step cHukumType1Sun=cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.SUN);		
		assertTrue(cHukumType1Sun.getKp4Step1().isPlanetStrong());
		assertNotNull(cHukumType1Sun.getKp4Step1().getPlanetStepValues());
		for(int i=0;i<cHukumType1Sun.getKp4Step1().getPlanetStepValues().length;i++)
			assertTrue(cHukumType1Sun.getKp4Step1().getPlanetStepValues()[i]==12);
		
		assertEquals( -1,cHukumType1Sun.getKp4Step1().getPlanetCuspConjunction());
		assertEquals(0,cHukumType1Sun.getKp4Step1().getPlanetConjunction().size());
		assertEquals(null,cHukumType1Sun.getKp4Step1().getCuspAspect());	
		assertEquals(GlobalVariablesKPExtension.RAHU,cHukumType1Sun.getKp4Step1().getPlanetAspect().get(0).getKey());
		for(int i=0;i<cHukumType1Sun.getKp4Step1().getPlanetAspect().get(0).getValues().length;i++)
			assertTrue(cHukumType1Sun.getKp4Step1().getPlanetAspect().get(0).getValues()[i]==8);
		
		assertEquals(GlobalVariablesKPExtension.JUPITER,cHukumType1Sun.getKp4Step1().getPlanetAspect().get(0).getPlanetInWhichRahuKetuPlaced());
		
		for(int i=0;i<cHukumType1Sun.getKp4Step1().getPlanetAspect().get(0).gePlanetValuesInWhichRahuKetuPlaced().length;i++)
			assertTrue(cHukumType1Sun.getKp4Step1().getPlanetAspect().get(0).gePlanetValuesInWhichRahuKetuPlaced()[i]==6||
					cHukumType1Sun.getKp4Step1().getPlanetAspect().get(0).gePlanetValuesInWhichRahuKetuPlaced()[i]==9);
		
		
		
		//MOON
		BeanKP4Step cHukumType1Moon=cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.MOON);		
		assertTrue(!cHukumType1Moon.getKp4Step1().isPlanetStrong());
		assertNull(cHukumType1Moon.getKp4Step1().getPlanetStepValues());
				
		assertEquals( -1,cHukumType1Moon.getKp4Step1().getPlanetCuspConjunction());
		assertEquals(0,cHukumType1Moon.getKp4Step1().getPlanetConjunction().size());
		assertEquals(null,cHukumType1Moon.getKp4Step1().getCuspAspect());	
		assertEquals(0,cHukumType1Moon.getKp4Step1().getPlanetAspect().size());
		
		
		//MARS
		BeanKP4Step cHukumType1Mars=cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.MARS);		
		assertTrue(cHukumType1Mars.getKp4Step1().isPlanetStrong());
		assertNotNull(cHukumType1Mars.getKp4Step1().getPlanetStepValues());
		for(int i=0;i<cHukumType1Mars.getKp4Step1().getPlanetStepValues().length;i++)
			assertTrue(cHukumType1Mars.getKp4Step1().getPlanetStepValues()[i]==5||
			cHukumType1Mars.getKp4Step1().getPlanetStepValues()[i]==10||
			cHukumType1Mars.getKp4Step1().getPlanetStepValues()[i]==12);
		
		
		assertEquals( -1,cHukumType1Mars.getKp4Step1().getPlanetCuspConjunction());
		assertEquals(0,cHukumType1Mars.getKp4Step1().getPlanetConjunction().size());
		assertEquals(null,cHukumType1Mars.getKp4Step1().getCuspAspect());	
		assertEquals(0,cHukumType1Mars.getKp4Step1().getPlanetAspect().size());
		
		
		//MERCURY
		BeanKP4Step cHukumType1Mer=cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.MERCURY);		
		assertTrue(!cHukumType1Mer.getKp4Step1().isPlanetStrong());
		assertNull(cHukumType1Mer.getKp4Step1().getPlanetStepValues());
		

		assertEquals( -1,cHukumType1Mer.getKp4Step1().getPlanetCuspConjunction());
		assertEquals(0,cHukumType1Mer.getKp4Step1().getPlanetConjunction().size());
		assertNull(cHukumType1Mer.getKp4Step1().getCuspAspect());	
		assertEquals(0,cHukumType1Mer.getKp4Step1().getPlanetAspect().size());	
		
		
		//JUPITER
		BeanKP4Step cHukumType1Jup=cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.JUPITER);		
		assertTrue(!cHukumType1Jup.getKp4Step1().isPlanetStrong());
		assertNull(cHukumType1Jup.getKp4Step1().getPlanetStepValues());
		assertEquals( -1,cHukumType1Jup.getKp4Step1().getPlanetCuspConjunction());
		assertEquals(0,cHukumType1Jup.getKp4Step1().getPlanetConjunction().size());
		assertNull(cHukumType1Jup.getKp4Step1().getCuspAspect());	
		assertEquals(0,cHukumType1Jup.getKp4Step1().getPlanetAspect().size());
		
		
		
		//VENUS
		BeanKP4Step cHukumType1Ven=cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.VENUS);		
		assertTrue(cHukumType1Ven.getKp4Step1().isPlanetStrong());
		assertNotNull(cHukumType1Ven.getKp4Step1().getPlanetStepValues());
		for(int i=0;i<cHukumType1Ven.getKp4Step1().getPlanetStepValues().length;i++)
			assertTrue(cHukumType1Ven.getKp4Step1().getPlanetStepValues()[i]==12);
		
		assertEquals( 12,cHukumType1Ven.getKp4Step1().getPlanetCuspConjunction());
		assertEquals(0,cHukumType1Ven.getKp4Step1().getPlanetConjunction().size());
		assertNull(cHukumType1Ven.getKp4Step1().getCuspAspect());	
		assertEquals(0,cHukumType1Ven.getKp4Step1().getPlanetAspect().size());
		
		//SAT
		BeanKP4Step cHukumType1Sat=cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.SAT);		
		assertTrue(!cHukumType1Sat.getKp4Step1().isPlanetStrong());
		assertNull(cHukumType1Sat.getKp4Step1().getPlanetStepValues());		

		assertEquals( -1,cHukumType1Sat.getKp4Step1().getPlanetCuspConjunction());
		assertEquals(0,cHukumType1Sat.getKp4Step1().getPlanetConjunction().size());		
		assertNull(cHukumType1Sat.getKp4Step1().getCuspAspect());		
	    assertEquals(0,cHukumType1Sat.getKp4Step1().getPlanetAspect().size());
		
	   //RAHU
		BeanKP4Step cHukumType1Rahu=cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.RAHU);		
		assertTrue(cHukumType1Rahu.getKp4Step1().isPlanetStrong());
		assertNotNull(cHukumType1Rahu.getKp4Step1().getPlanetStepValues());
		for(int i=0;i<cHukumType1Rahu.getKp4Step1().getPlanetStepValues().length;i++)
			assertTrue(cHukumType1Rahu.getKp4Step1().getPlanetStepValues()[i]==8);
		
		KeyValue4Step objPunitRahu=cHukumType1Rahu.getKp4Step1().getRahuKetPlacedInPlanetRashi();
		assertEquals(objPunitRahu.getKey(), GlobalVariablesKPExtension.JUPITER);
		for(int i=0;i<objPunitRahu.getValues().length;i++)
			assertTrue(objPunitRahu.getValues()[i]==6||
					objPunitRahu.getValues()[i]==9);
		
		assertNull(cHukumType1Rahu.getKp4Step1().getCuspAspect());
		
		
		assertEquals( -1,cHukumType1Rahu.getKp4Step1().getPlanetCuspConjunction());
		assertEquals(0,cHukumType1Rahu.getKp4Step1().getPlanetConjunction().size());		
		assertNull(cHukumType1Rahu.getKp4Step1().getCuspAspect());
		
	    assertEquals(0,cHukumType1Rahu.getKp4Step1().getPlanetAspect().size());
		
		
	    //KETU
		BeanKP4Step cHukumType1Ketu=cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.KETU);		
		assertTrue(!cHukumType1Ketu.getKp4Step1().isPlanetStrong());
		assertNull(cHukumType1Ketu.getKp4Step1().getPlanetStepValues());
			

		
		assertEquals( -1,cHukumType1Ketu.getKp4Step1().getPlanetCuspConjunction());
		assertEquals(0,cHukumType1Ketu.getKp4Step1().getPlanetConjunction().size());		
		assertNull(cHukumType1Rahu.getKp4Step1().getCuspAspect());
	    assertEquals(0,cHukumType1Ketu.getKp4Step1().getPlanetAspect().size());
		
	}
	
	public void get4StepType3Hukum()
	{
		//SUN
		BeanKP4Step cHukumType3Sun=cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.SUN);		
		assertEquals(cHukumType3Sun.getKp4Step3().getPlanet(),GlobalVariablesKPExtension.VENUS);
		assertNotNull(cHukumType3Sun.getKp4Step3().getPlanetStepValues());
		for(int i=0;i<cHukumType3Sun.getKp4Step3().getPlanetStepValues().length;i++)
			assertTrue(cHukumType3Sun.getKp4Step3().getPlanetStepValues()[i]==12);
		
		assertEquals( 12,cHukumType3Sun.getKp4Step3().getPlanetCuspConjunction());
		assertEquals(0,cHukumType3Sun.getKp4Step3().getPlanetConjunction().size());
		assertNull(cHukumType3Sun.getKp4Step3().getCuspAspect());	
		assertEquals(0,cHukumType3Sun.getKp4Step3().getPlanetAspect().size());
		
		//MOON
		BeanKP4Step cHukumType3Moon=cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.MOON);		
		assertEquals(cHukumType3Moon.getKp4Step3().getPlanet(),GlobalVariablesKPExtension.MOON);
		assertNull(cHukumType3Moon.getKp4Step3().getPlanetStepValues());
		
		assertEquals( -1,cHukumType3Moon.getKp4Step3().getPlanetCuspConjunction());
		assertEquals(0,cHukumType3Moon.getKp4Step3().getPlanetConjunction().size());
		assertEquals(null,cHukumType3Moon.getKp4Step3().getCuspAspect());	
		assertEquals(0,cHukumType3Moon.getKp4Step3().getPlanetAspect().size());
	    
	    

	    //MARS
		BeanKP4Step cHukumType3Mars=cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.MARS);		
		assertEquals(cHukumType3Mars.getKp4Step3().getPlanet(),GlobalVariablesKPExtension.VENUS);
		assertNotNull(cHukumType3Mars.getKp4Step3().getPlanetStepValues());
		for(int i=0;i<cHukumType3Mars.getKp4Step3().getPlanetStepValues().length;i++)
			assertTrue(cHukumType3Mars.getKp4Step3().getPlanetStepValues()[i]==12);
		
		assertEquals( 12,cHukumType3Mars.getKp4Step3().getPlanetCuspConjunction());
		assertEquals(0,cHukumType3Mars.getKp4Step3().getPlanetConjunction().size());
		assertNull(cHukumType3Mars.getKp4Step3().getCuspAspect());	
		assertEquals(0,cHukumType3Mars.getKp4Step3().getPlanetAspect().size());
		
		//MERCURY
		BeanKP4Step cHukumType3Mer=cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.MERCURY);		
		assertEquals(cHukumType3Mer.getKp4Step3().getPlanet(),GlobalVariablesKPExtension.JUPITER);
		assertNull(cHukumType3Mer.getKp4Step3().getPlanetStepValues());
		assertEquals( -1,cHukumType3Mer.getKp4Step3().getPlanetCuspConjunction());
		assertEquals(0,cHukumType3Mer.getKp4Step3().getPlanetConjunction().size());
		assertNull(cHukumType3Mer.getKp4Step3().getCuspAspect());	
		assertEquals(0,cHukumType3Mer.getKp4Step3().getPlanetAspect().size());
		
		
		
	    
	    
	     //JUPITER
		BeanKP4Step cHukumType3Jup=cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.JUPITER);		
		assertEquals(cHukumType3Jup.getKp4Step3().getPlanet(),GlobalVariablesKPExtension.MARS);
		assertNotNull(cHukumType3Jup.getKp4Step3().getPlanetStepValues());
		for(int i=0;i<cHukumType3Jup.getKp4Step3().getPlanetStepValues().length;i++)
			assertTrue(cHukumType3Jup.getKp4Step3().getPlanetStepValues()[i]==5||
			cHukumType3Jup.getKp4Step3().getPlanetStepValues()[i]==10||
			cHukumType3Jup.getKp4Step3().getPlanetStepValues()[i]==12);
		
		
		assertEquals( -1,cHukumType3Jup.getKp4Step3().getPlanetCuspConjunction());
		assertEquals(0,cHukumType3Jup.getKp4Step3().getPlanetConjunction().size());
		assertEquals(null,cHukumType3Jup.getKp4Step3().getCuspAspect());	
		assertEquals(0,cHukumType3Jup.getKp4Step3().getPlanetAspect().size());
		
		//END
		
		//VENUS
		BeanKP4Step cHukumType3Ven=cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.VENUS);		
		assertEquals(cHukumType3Ven.getKp4Step3().getPlanet(),GlobalVariablesKPExtension.RAHU);
		
		assertNotNull(cHukumType3Ven.getKp4Step3().getPlanetStepValues());
		for(int i=0;i<cHukumType3Ven.getKp4Step3().getPlanetStepValues().length;i++)
			assertTrue(cHukumType3Ven.getKp4Step3().getPlanetStepValues()[i]==8);
		
		KeyValue4Step objHukumVen=cHukumType3Ven.getKp4Step3().getRahuKetPlacedInPlanetRashi();
		assertEquals(objHukumVen.getKey(), GlobalVariablesKPExtension.JUPITER);
		for(int i=0;i<objHukumVen.getValues().length;i++)
			assertTrue(objHukumVen.getValues()[i]==6||
					objHukumVen.getValues()[i]==9);
		
		assertNull(cHukumType3Ven.getKp4Step3().getCuspAspect());
		
		
		assertEquals( -1,cHukumType3Ven.getKp4Step3().getPlanetCuspConjunction());
		assertEquals(0,cHukumType3Ven.getKp4Step3().getPlanetConjunction().size());		
		assertNull(cHukumType3Ven.getKp4Step3().getCuspAspect());
		
	    assertEquals(0,cHukumType3Ven.getKp4Step3().getPlanetAspect().size());
		
	    
	    
	   //SAT
  		BeanKP4Step cHukumType3Sat=cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.SAT);		
  		assertEquals(cHukumType3Sat.getKp4Step3().getPlanet(),GlobalVariablesKPExtension.VENUS);
  		
  		assertNotNull(cHukumType3Sat.getKp4Step3().getPlanetStepValues());
		for(int i=0;i<cHukumType3Sat.getKp4Step3().getPlanetStepValues().length;i++)
			assertTrue(cHukumType3Sat.getKp4Step3().getPlanetStepValues()[i]==12);
		
		assertEquals( 12,cHukumType3Sat.getKp4Step3().getPlanetCuspConjunction());
		assertEquals(0,cHukumType3Sat.getKp4Step3().getPlanetConjunction().size());
		assertNull(cHukumType3Sat.getKp4Step3().getCuspAspect());	
		assertEquals(0,cHukumType3Sat.getKp4Step3().getPlanetAspect().size());
		
  		
  		
		
		
		 //RAHU
  		BeanKP4Step cHukumType3Rahu=cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.RAHU);		
  		assertEquals(cHukumType3Rahu.getKp4Step3().getPlanet(),GlobalVariablesKPExtension.MOON);
  		
  		assertNull(cHukumType3Rahu.getKp4Step3().getPlanetStepValues());
		
		assertEquals( -1,cHukumType3Rahu.getKp4Step3().getPlanetCuspConjunction());
		assertEquals(0,cHukumType3Rahu.getKp4Step3().getPlanetConjunction().size());
		assertEquals(null,cHukumType3Rahu.getKp4Step3().getCuspAspect());	
		assertEquals(0,cHukumType3Rahu.getKp4Step3().getPlanetAspect().size());
		
  			    
	    //KETU
  		BeanKP4Step cHukumType3Ketu=cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.KETU);		
  		assertEquals(cHukumType3Ketu.getKp4Step3().getPlanet(),GlobalVariablesKPExtension.RAHU);

		assertNotNull(cHukumType3Ven.getKp4Step3().getPlanetStepValues());
		for(int i=0;i<cHukumType3Ven.getKp4Step3().getPlanetStepValues().length;i++)
			assertTrue(cHukumType3Ven.getKp4Step3().getPlanetStepValues()[i]==8);
		
		KeyValue4Step objHukumKetu=cHukumType3Ketu.getKp4Step3().getRahuKetPlacedInPlanetRashi();
		assertEquals(objHukumKetu.getKey(), GlobalVariablesKPExtension.JUPITER);
		for(int i=0;i<objHukumKetu.getValues().length;i++)
			assertTrue(objHukumKetu.getValues()[i]==6||
					objHukumKetu.getValues()[i]==9);
		
		assertNull(cHukumType3Ketu.getKp4Step3().getCuspAspect());
		
		
		assertEquals( -1,cHukumType3Ketu.getKp4Step3().getPlanetCuspConjunction());
		assertEquals(0,cHukumType3Ketu.getKp4Step3().getPlanetConjunction().size());		
		assertNull(cHukumType3Ketu.getKp4Step3().getCuspAspect());
		
	    assertEquals(0,cHukumType3Ketu.getKp4Step3().getPlanetAspect().size());
	}
	public void get4StepType2Hukum()
	{
		//SUN
		BeanKP4Step cHukumType2Sun=cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.SUN);		
		assertEquals(cHukumType2Sun.getKp4Step2().getPlanet(),GlobalVariablesKPExtension.SAT);
		for(int i=0;i<cHukumType2Sun.getKp4Step2().getPlanetStepValues().length;i++)
			assertTrue(cHukumType2Sun.getKp4Step2().getPlanetStepValues()[i]==4||
					cHukumType2Sun.getKp4Step2().getPlanetStepValues()[i]==7);
		
		assertEquals(-1,cHukumType2Sun.getKp4Step2().getPlanetCuspConjunction());
		assertEquals(0,cHukumType2Sun.getKp4Step2().getPlanetConjunction().size());
		assertEquals(null,cHukumType2Sun.getKp4Step2().getCuspAspect());
		assertEquals(0,cHukumType2Sun.getKp4Step2().getPlanetAspect().size());
		
		
		//MOON
		BeanKP4Step cHukumType2Moon=cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.MOON);		
		assertEquals(cHukumType2Moon.getKp4Step2().getPlanet(),GlobalVariablesKPExtension.MERCURY);
		for(int i=0;i<cHukumType2Moon.getKp4Step2().getPlanetStepValues().length;i++)
			assertTrue(cHukumType2Moon.getKp4Step2().getPlanetStepValues()[i]==3||
					cHukumType2Moon.getKp4Step2().getPlanetStepValues()[i]==11);
		
		assertEquals(-1,cHukumType2Moon.getKp4Step2().getPlanetCuspConjunction());
		assertEquals(0,cHukumType2Moon.getKp4Step2().getPlanetConjunction().size());
		assertNull(cHukumType2Moon.getKp4Step2().getCuspAspect());		
		assertEquals(0,cHukumType2Moon.getKp4Step2().getPlanetAspect().size());
		
		

		//MARS
		BeanKP4Step cHukumType2Mars=cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.MARS);		
		assertEquals(cHukumType2Mars.getKp4Step2().getPlanet(),GlobalVariablesKPExtension.MERCURY);
	
		for(int i=0;i<cHukumType2Mars.getKp4Step2().getPlanetStepValues().length;i++)
			assertTrue(cHukumType2Mars.getKp4Step2().getPlanetStepValues()[i]==3||
					cHukumType2Mars.getKp4Step2().getPlanetStepValues()[i]==11);
		
		assertEquals(-1,cHukumType2Mars.getKp4Step2().getPlanetCuspConjunction());
		assertEquals(0,cHukumType2Mars.getKp4Step2().getPlanetConjunction().size());
		assertNull(cHukumType2Mars.getKp4Step2().getCuspAspect());		
		assertEquals(0,cHukumType2Mars.getKp4Step2().getPlanetAspect().size());
		
		
		//MERCURY
		BeanKP4Step cHukumType2Mer=cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.MERCURY);		
		assertEquals(cHukumType2Mer.getKp4Step2().getPlanet(),GlobalVariablesKPExtension.JUPITER);
	
		for(int i=0;i<cHukumType2Mer.getKp4Step2().getPlanetStepValues().length;i++)
			assertTrue(cHukumType2Mer.getKp4Step2().getPlanetStepValues()[i]==6||
			cHukumType2Mer.getKp4Step2().getPlanetStepValues()[i]==9);
		
		assertEquals(-1,cHukumType2Mer.getKp4Step2().getPlanetCuspConjunction());
		assertEquals(0,cHukumType2Mer.getKp4Step2().getPlanetConjunction().size());
		assertNull(cHukumType2Mer.getKp4Step2().getCuspAspect());		
		assertEquals(0,cHukumType2Mer.getKp4Step2().getPlanetAspect().size());
		
		
		//JUPITER
		BeanKP4Step cHukumType2jup=cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.JUPITER);		
		assertEquals(cHukumType2jup.getKp4Step2().getPlanet(),GlobalVariablesKPExtension.KETU);
	
		for(int i=0;i<cHukumType2jup.getKp4Step2().getPlanetStepValues().length;i++)
			assertTrue(cHukumType2jup.getKp4Step2().getPlanetStepValues()[i]==2);
		
		assertEquals(-1,cHukumType2jup.getKp4Step2().getPlanetCuspConjunction());
		assertEquals(0,cHukumType2jup.getKp4Step2().getPlanetConjunction().size());
		assertNull(cHukumType2jup.getKp4Step2().getCuspAspect());		
		assertEquals(0,cHukumType2jup.getKp4Step2().getPlanetAspect().size());
		
		KeyValue4Step objPunitRahu=cHukumType2jup.getKp4Step2().getRahuKetPlacedInPlanetRashi();
		assertEquals(objPunitRahu.getKey(), GlobalVariablesKPExtension.MERCURY);
		for(int i=0;i<objPunitRahu.getValues().length;i++)
			assertTrue(objPunitRahu.getValues()[i]==3||
					objPunitRahu.getValues()[i]==11);
		
	//VENUS
		BeanKP4Step cHukumType2Ven=cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.VENUS);		
		assertEquals(cHukumType2Ven.getKp4Step2().getPlanet(),GlobalVariablesKPExtension.JUPITER);
		
		for(int i=0;i<cHukumType2Ven.getKp4Step2().getPlanetStepValues().length;i++)
			assertTrue(cHukumType2Ven.getKp4Step2().getPlanetStepValues()[i]==6||
			cHukumType2Ven.getKp4Step2().getPlanetStepValues()[i]==9);
		
		assertEquals(-1,cHukumType2Ven.getKp4Step2().getPlanetCuspConjunction());
		assertEquals(0,cHukumType2Ven.getKp4Step2().getPlanetConjunction().size());
		assertNull(cHukumType2Ven.getKp4Step2().getCuspAspect());		
		assertEquals(0,cHukumType2Ven.getKp4Step2().getPlanetAspect().size());
		
		//SAT
		BeanKP4Step cHukumType2Sat=cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.SAT);		
		assertEquals(cHukumType2Sat.getKp4Step2().getPlanet(),GlobalVariablesKPExtension.MERCURY);
		
		for(int i=0;i<cHukumType2Sat.getKp4Step2().getPlanetStepValues().length;i++)
			assertTrue(cHukumType2Sat.getKp4Step2().getPlanetStepValues()[i]==3||
					cHukumType2Sat.getKp4Step2().getPlanetStepValues()[i]==11);
		
		assertEquals(-1,cHukumType2Sat.getKp4Step2().getPlanetCuspConjunction());
		assertEquals(0,cHukumType2Sat.getKp4Step2().getPlanetConjunction().size());
		assertNull(cHukumType2Sat.getKp4Step2().getCuspAspect());		
		assertEquals(0,cHukumType2Sat.getKp4Step2().getPlanetAspect().size());

		
		
		//RAHU
		BeanKP4Step cHukumType2Rahu=cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.RAHU);		
		assertEquals(cHukumType2Rahu.getKp4Step2().getPlanet(),GlobalVariablesKPExtension.SAT);
		
		for(int i=0;i<cHukumType2Rahu.getKp4Step2().getPlanetStepValues().length;i++)
			assertTrue(cHukumType2Rahu.getKp4Step2().getPlanetStepValues()[i]==4||
					cHukumType2Rahu.getKp4Step2().getPlanetStepValues()[i]==7);
		
		assertEquals(-1,cHukumType2Rahu.getKp4Step2().getPlanetCuspConjunction());
		assertEquals(0,cHukumType2Rahu.getKp4Step2().getPlanetConjunction().size());
		assertEquals(null,cHukumType2Rahu.getKp4Step2().getCuspAspect());
		assertEquals(0,cHukumType2Rahu.getKp4Step2().getPlanetAspect().size());
		
		
	
		//KETU
		BeanKP4Step cHukumType2Ketu=cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.KETU);		
		assertEquals(cHukumType2Ketu.getKp4Step2().getPlanet(),GlobalVariablesKPExtension.MOON);
	
		for(int i=0;i<cHukumType2Ketu.getKp4Step2().getPlanetStepValues().length;i++)
			assertTrue(cHukumType2Ketu.getKp4Step2().getPlanetStepValues()[i]==1||
			cHukumType2Ketu.getKp4Step2().getPlanetStepValues()[i]==12);
		
		assertEquals(-1,cHukumType2Ketu.getKp4Step2().getPlanetCuspConjunction());
		assertEquals(0,cHukumType2Ketu.getKp4Step2().getPlanetConjunction().size());
		assertNull(cHukumType2Ketu.getKp4Step2().getCuspAspect());		
		assertEquals(0,cHukumType2Ketu.getKp4Step2().getPlanetAspect().size());
				
		
	}
	public void get4StepType4Hukum()
	{
		//SUN
		BeanKP4Step cHukumType4Sun=cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.SUN);		
		assertEquals(cHukumType4Sun.getKp4Step4().getPlanet(),GlobalVariablesKPExtension.JUPITER);

		for(int i=0;i<cHukumType4Sun.getKp4Step4().getPlanetStepValues().length;i++)
			assertTrue(cHukumType4Sun.getKp4Step4().getPlanetStepValues()[i]==6||
			cHukumType4Sun.getKp4Step4().getPlanetStepValues()[i]==9);
		
		assertEquals(-1,cHukumType4Sun.getKp4Step2().getPlanetCuspConjunction());
		assertEquals(0,cHukumType4Sun.getKp4Step4().getPlanetConjunction().size());
		assertNull(cHukumType4Sun.getKp4Step4().getCuspAspect());		
		assertEquals(0,cHukumType4Sun.getKp4Step4().getPlanetAspect().size());
		
		//MOON
		BeanKP4Step cHukumType4Moon=cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.MOON);		
		assertEquals(cHukumType4Moon.getKp4Step4().getPlanet(),GlobalVariablesKPExtension.MERCURY);
		

		for(int i=0;i<cHukumType4Moon.getKp4Step2().getPlanetStepValues().length;i++)
			assertTrue(cHukumType4Moon.getKp4Step2().getPlanetStepValues()[i]==3||
					cHukumType4Moon.getKp4Step2().getPlanetStepValues()[i]==11);
		
		assertEquals(-1,cHukumType4Moon.getKp4Step2().getPlanetCuspConjunction());
		assertEquals(0,cHukumType4Moon.getKp4Step2().getPlanetConjunction().size());
		assertNull(cHukumType4Moon.getKp4Step2().getCuspAspect());		
		assertEquals(0,cHukumType4Moon.getKp4Step2().getPlanetAspect().size());


		//MARS
		BeanKP4Step cHukumType4Mars=cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.MARS);		
		assertEquals(cHukumType4Mars.getKp4Step4().getPlanet(),GlobalVariablesKPExtension.JUPITER);
		
		for(int i=0;i<cHukumType4Mars.getKp4Step4().getPlanetStepValues().length;i++)
			assertTrue(cHukumType4Mars.getKp4Step4().getPlanetStepValues()[i]==6||
			cHukumType4Mars.getKp4Step4().getPlanetStepValues()[i]==9);
		
		assertEquals(-1,cHukumType4Mars.getKp4Step4().getPlanetCuspConjunction());
		assertEquals(0,cHukumType4Mars.getKp4Step4().getPlanetConjunction().size());
		assertNull(cHukumType4Mars.getKp4Step4().getCuspAspect());		
		assertEquals(0,cHukumType4Mars.getKp4Step4().getPlanetAspect().size());

		
		//MERCURY
		BeanKP4Step cHukumType4Mer=cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.MERCURY);		
		assertEquals(cHukumType4Mer.getKp4Step4().getPlanet(),GlobalVariablesKPExtension.KETU);
		
		for(int i=0;i<cHukumType4Mer.getKp4Step4().getPlanetStepValues().length;i++)
			assertTrue(cHukumType4Mer.getKp4Step4().getPlanetStepValues()[i]==2);
		
		assertEquals(-1,cHukumType4Mer.getKp4Step4().getPlanetCuspConjunction());
		assertEquals(0,cHukumType4Mer.getKp4Step4().getPlanetConjunction().size());
		assertNull(cHukumType4Mer.getKp4Step2().getCuspAspect());		
		assertEquals(0,cHukumType4Mer.getKp4Step4().getPlanetAspect().size());
		
		KeyValue4Step objHukumMer=cHukumType4Mer.getKp4Step4().getRahuKetPlacedInPlanetRashi();
		assertEquals(objHukumMer.getKey(), GlobalVariablesKPExtension.MERCURY);
		for(int i=0;i<objHukumMer.getValues().length;i++)
			assertTrue(objHukumMer.getValues()[i]==3||
					objHukumMer.getValues()[i]==11);
		
		
		
		//JUPITER
		BeanKP4Step cHukumType4Jup=cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.JUPITER);		
		assertEquals(cHukumType4Jup.getKp4Step4().getPlanet(),GlobalVariablesKPExtension.MERCURY);
		

		for(int i=0;i<cHukumType4Jup.getKp4Step4().getPlanetStepValues().length;i++)
			assertTrue(cHukumType4Jup.getKp4Step4().getPlanetStepValues()[i]==3||
					cHukumType4Jup.getKp4Step4().getPlanetStepValues()[i]==11);
		
		assertEquals(-1,cHukumType4Jup.getKp4Step4().getPlanetCuspConjunction());
		assertEquals(0,cHukumType4Jup.getKp4Step4().getPlanetConjunction().size());
		assertNull(cHukumType4Jup.getKp4Step4().getCuspAspect());		
		assertEquals(0,cHukumType4Jup.getKp4Step4().getPlanetAspect().size());
		
		
		
		//VENUS
		BeanKP4Step cHukumType4Ven=cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.VENUS);		
		assertEquals(cHukumType4Ven.getKp4Step4().getPlanet(),GlobalVariablesKPExtension.SAT);

		for(int i=0;i<cHukumType4Ven.getKp4Step4().getPlanetStepValues().length;i++)
			assertTrue(cHukumType4Ven.getKp4Step4().getPlanetStepValues()[i]==4||
					cHukumType4Ven.getKp4Step4().getPlanetStepValues()[i]==7);
		
		assertEquals(-1,cHukumType4Ven.getKp4Step4().getPlanetCuspConjunction());
		assertEquals(0,cHukumType4Ven.getKp4Step4().getPlanetConjunction().size());
		assertEquals(null,cHukumType4Ven.getKp4Step4().getCuspAspect());
		assertEquals(0,cHukumType4Ven.getKp4Step4().getPlanetAspect().size());
		
		
		
		//SAT
		BeanKP4Step cHukumType4Sat=cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.SAT);		
		assertEquals(cHukumType4Sat.getKp4Step4().getPlanet(),GlobalVariablesKPExtension.JUPITER);
		for(int i=0;i<cHukumType4Sat.getKp4Step4().getPlanetStepValues().length;i++)
			assertTrue(cHukumType4Sat.getKp4Step4().getPlanetStepValues()[i]==6||
			cHukumType4Sat.getKp4Step4().getPlanetStepValues()[i]==9);
		
		assertEquals(-1,cHukumType4Sat.getKp4Step4().getPlanetCuspConjunction());
		assertEquals(0,cHukumType4Sat.getKp4Step4().getPlanetConjunction().size());
		assertNull(cHukumType4Sat.getKp4Step4().getCuspAspect());		
		assertEquals(0,cHukumType4Sat.getKp4Step4().getPlanetAspect().size());
		
		
		//RAHU
		BeanKP4Step cHukumType4Rahu=cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.RAHU);		
		assertEquals(cHukumType4Rahu.getKp4Step4().getPlanet(),GlobalVariablesKPExtension.MERCURY);


		for(int i=0;i<cHukumType4Rahu.getKp4Step4().getPlanetStepValues().length;i++)
			assertTrue(cHukumType4Rahu.getKp4Step4().getPlanetStepValues()[i]==3||
					cHukumType4Rahu.getKp4Step4().getPlanetStepValues()[i]==11);
		
		assertEquals(-1,cHukumType4Rahu.getKp4Step4().getPlanetCuspConjunction());
		assertEquals(0,cHukumType4Rahu.getKp4Step4().getPlanetConjunction().size());
		assertNull(cHukumType4Rahu.getKp4Step4().getCuspAspect());		
		assertEquals(0,cHukumType4Rahu.getKp4Step4().getPlanetAspect().size());
		
		
		
		//KETU
		BeanKP4Step cHukumType4Ketu=cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.KETU);		
		assertEquals(cHukumType4Ketu.getKp4Step4().getPlanet(),GlobalVariablesKPExtension.SAT);


		for(int i=0;i<cHukumType4Ketu.getKp4Step4().getPlanetStepValues().length;i++)
			assertTrue(cHukumType4Ketu.getKp4Step4().getPlanetStepValues()[i]==4||
					cHukumType4Ketu.getKp4Step4().getPlanetStepValues()[i]==7);
		
		assertEquals(-1,cHukumType4Ketu.getKp4Step4().getPlanetCuspConjunction());
		assertEquals(0,cHukumType4Ketu.getKp4Step4().getPlanetConjunction().size());
		assertEquals(null,cHukumType4Ketu.getKp4Step4().getCuspAspect());
		assertEquals(0,cHukumType4Ketu.getKp4Step4().getPlanetAspect().size());
		
		
	}

}
