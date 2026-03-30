package com.astrosagekundli.junit;

import com.ojassoft.kpextension.CKpRefactorExtension;
import com.ojassoft.kpextension.GlobalVariablesKPExtension;

import junit.framework.TestCase;

public class KCILTest extends TestCase{
	CKpRefactorExtension kpr=null;
	
	@Override
	protected void setUp() throws Exception {
		kpr=new CKpRefactorExtension(GlobalVariablesKPExtensionTest.PLANST_VALUES_KCLI_1, GlobalVariablesKPExtensionTest.KP_CUSP_VALUES_KCLI_1);
	}
	@Override
	protected void tearDown() throws Exception {
		kpr=null;
	}
	
	public void testgetKCILType1()
	{
		
		int []type1Sun=kpr.getKCILType1(GlobalVariablesKPExtension.SUN);
		for(int i=0;i<type1Sun.length;i++)			 
			assertTrue(type1Sun[i]==3||type1Sun[i]==11);
		
		
		int []type1Moon=kpr.getKCILType1(GlobalVariablesKPExtension.MOON);
		for(int i=0;i<type1Moon.length;i++)
			assertTrue(type1Moon[i]==10);
		
		
		int []type1Mars=kpr.getKCILType1(GlobalVariablesKPExtension.MARS);
		for(int i=0;i<type1Mars.length;i++)
			assertTrue(type1Mars[i]==1||
					type1Mars[i]==2||
					type1Mars[i]==3||
					type1Mars[i]==4||
					type1Mars[i]==7||
					type1Mars[i]==8||
					type1Mars[i]==9||
					type1Mars[i]==10||
					type1Mars[i]==12
					);
	
		int []type1Mer=kpr.getKCILType1(GlobalVariablesKPExtension.MERCURY);
		for(int i=0;i<type1Mer.length;i++)			
			assertTrue(type1Mer[i]==1||					
					type1Mer[i]==3||
					type1Mer[i]==6||
					type1Mer[i]==7||					
					type1Mer[i]==10||
					type1Mer[i]==12
					);
	
		
		int []type1jup=kpr.getKCILType1(GlobalVariablesKPExtension.JUPITER);
		for(int i=0;i<type1jup.length;i++)
			assertTrue(type1jup[i]==2||					
					type1jup[i]==4||
					type1jup[i]==5||
					type1jup[i]==6||					
					type1jup[i]==7||
					type1jup[i]==8||
					type1jup[i]==9
					);
		
		int []type1Ven=kpr.getKCILType1(GlobalVariablesKPExtension.VENUS);
		for(int i=0;i<type1Ven.length;i++)		
			assertTrue(type1Ven[i]==4||					
					type1Ven[i]==6||
					type1Ven[i]==7||
					type1Ven[i]==8
					);
		

		int []type1Sat=kpr.getKCILType1(GlobalVariablesKPExtension.SAT);
		for(int i=0;i<type1Sat.length;i++)
			assertTrue(type1Sat[i]==3||					
					type1Sat[i]==11
					
					);
		
	
		int []type1Rahu=kpr.getKCILType1(GlobalVariablesKPExtension.RAHU);
		for(int i=0;i<type1Rahu.length;i++)
			assertTrue(type1Rahu[i]==3||
					type1Rahu[i]==4||					
					type1Rahu[i]==8||
					type1Rahu[i]==9||
					type1Rahu[i]==10||
					type1Rahu[i]==11||
					type1Rahu[i]==12
					);
		
		
		int []type1Ketu=kpr.getKCILType1(GlobalVariablesKPExtension.KETU);
		for(int i=0;i<type1Ketu.length;i++)			
			assertTrue(type1Ketu[i]==2||
					type1Ketu[i]==4||					
					type1Ketu[i]==5||
					type1Ketu[i]==6||
					type1Ketu[i]==7||
					type1Ketu[i]==8||
					type1Ketu[i]==9
					);
		
	}

	
	public void testgetKCILType2()
	{
		
		int []type2Sun=kpr.getKCILType2(GlobalVariablesKPExtension.SUN);
		for(int i=0;i<type2Sun.length;i++)		
			assertTrue(type2Sun[i]==3||type2Sun[i]==11);
		
		
		int []type2Moon=kpr.getKCILType2(GlobalVariablesKPExtension.MOON);
		for(int i=0;i<type2Moon.length;i++)			
			assertTrue(type2Moon[i]==2||
					type2Moon[i]==5||
					type2Moon[i]==10||
					type2Moon[i]==11);
		
		
		int []type2Mars=kpr.getKCILType2(GlobalVariablesKPExtension.MARS);
		for(int i=0;i<type2Mars.length;i++)			
			assertTrue(type2Mars[i]==1||
					type2Mars[i]==4||
					type2Mars[i]==8||
					type2Mars[i]==9||
					type2Mars[i]==11
					);
		
		
		int []type2Mer=kpr.getKCILType2(GlobalVariablesKPExtension.MERCURY);
		for(int i=0;i<type2Mer.length;i++)			
			assertTrue(type2Mer[i]==2||					
					type2Mer[i]==5||									
					type2Mer[i]==10||
					type2Mer[i]==11
					);		
		
		
		int []type2jup=kpr.getKCILType2(GlobalVariablesKPExtension.JUPITER);
		for(int i=0;i<type2jup.length;i++)			
			assertTrue(type2jup[i]==2||
					type2jup[i]==5||									
					type2jup[i]==10||				
					type2jup[i]==11
					);
		
	
		int []type2Ven=kpr.getKCILType2(GlobalVariablesKPExtension.VENUS);
		for(int i=0;i<type2Ven.length;i++)			
			assertTrue(type2Ven[i]==1||					
					type2Ven[i]==4||
					type2Ven[i]==8||
					type2Ven[i]==9||
					type2Ven[i]==11
					);
		
		int []type2Sat=kpr.getKCILType2(GlobalVariablesKPExtension.SAT);
		for(int i=0;i<type2Sat.length;i++)			
			assertTrue(type2Sat[i]==1||					
			type2Sat[i]==4||
			type2Sat[i]==8||
			type2Sat[i]==9||
			type2Sat[i]==11
			);
		
		
		 int []type2Rahu=kpr.getKCILType2(GlobalVariablesKPExtension.RAHU);
		for(int i=0;i<type2Rahu.length;i++)			
			assertTrue(type2Rahu[i]==2||
					type2Rahu[i]==5||					
					type2Rahu[i]==10||
					type2Rahu[i]==11					
					);
	
		 int []type2Ketu=kpr.getKCILType2(GlobalVariablesKPExtension.KETU);
		for(int i=0;i<type2Ketu.length;i++)			
			assertTrue(type2Ketu[i]==1||
					type2Ketu[i]==3||					
					type2Ketu[i]==6||
					type2Ketu[i]==7||
					type2Ketu[i]==10||
					type2Ketu[i]==12
					);
		
	}

	
	public void testgetKCILType3()
	{
		
		int []type3Sun=kpr.getKCILType3(GlobalVariablesKPExtension.SUN);
		for(int i=0;i<type3Sun.length;i++)		
			assertTrue(type3Sun[i]==4||
			type3Sun[i]==6||
			type3Sun[i]==7||
			type3Sun[i]==8);
		
		
		int []type3Moon=kpr.getKCILType3(GlobalVariablesKPExtension.MOON);
		for(int i=0;i<type3Moon.length;i++)			
			assertTrue(type3Moon[i]==1||
					type3Moon[i]==4||
					type3Moon[i]==8||
					type3Moon[i]==9||
					type3Moon[i]==11);
		
	
		int []type3Mars=kpr.getKCILType3(GlobalVariablesKPExtension.MARS);
		for(int i=0;i<type3Mars.length;i++)			
			assertTrue(type3Mars[i]==1||
					type3Mars[i]==3||
					type3Mars[i]==6||	
					type3Mars[i]==7||	
					type3Mars[i]==10||	
					type3Mars[i]==12
					);
		
		
		int []type3Mer=kpr.getKCILType3(GlobalVariablesKPExtension.MERCURY);
		for(int i=0;i<type3Mer.length;i++)			
			assertTrue(type3Mer[i]==3||
					type3Mer[i]==11
					);
	
		
	 int []type3jup=kpr.getKCILType3(GlobalVariablesKPExtension.JUPITER);
		for(int i=0;i<type3jup.length;i++)			
			assertTrue(type3jup[i]==1||
					type3jup[i]==2||									
					type3jup[i]==7||				
					type3jup[i]==9
					);
		
	
			int []type3Ven=kpr.getKCILType3(GlobalVariablesKPExtension.VENUS);		
			for(int i=0;i<type3Ven.length;i++)	
				assertTrue(type3Ven[i]==1||
					type3Ven[i]==2||
					type3Ven[i]==3||
					type3Ven[i]==4||
					type3Ven[i]==7||
					type3Ven[i]==8||
					type3Ven[i]==9||
					type3Ven[i]==10||
					type3Ven[i]==12
					);
	
	 int []type3Sat=kpr.getKCILType3(GlobalVariablesKPExtension.SAT);
		for(int i=0;i<type3Sat.length;i++)			
			assertTrue(type3Sat[i]==3||
			type3Sat[i]==11
			);
		
	
		 int []type3Rahu=kpr.getKCILType3(GlobalVariablesKPExtension.RAHU);
		for(int i=0;i<type3Rahu.length;i++)			
			assertTrue(type3Rahu[i]==1||
					type3Rahu[i]==2||					
					type3Rahu[i]==7||
					type3Rahu[i]==9
					
					);
	
		int []type3Ketu=kpr.getKCILType3(GlobalVariablesKPExtension.KETU);
		for(int i=0;i<type3Ketu.length;i++)			
			assertTrue(type3Ketu[i]==3||
					type3Ketu[i]==4||					
					type3Ketu[i]==8||
					type3Ketu[i]==9||
					type3Ketu[i]==10||
					type3Ketu[i]==12
					);
		
	}
	
	public void testgetKCILType4()
	{
		int []type4Sun=kpr.getKCILType4(GlobalVariablesKPExtension.SUN);
		assertNull(type4Sun);
		
		int []type4Moon=kpr.getKCILType4(GlobalVariablesKPExtension.MOON);
		assertNull(type4Moon);
		
		int []type4Mars=kpr.getKCILType4(GlobalVariablesKPExtension.MARS);
		assertNull(type4Mars);		
		
		int []type4Mer=kpr.getKCILType4(GlobalVariablesKPExtension.MERCURY);
		for(int i=0;i<type4Mer.length;i++)			
			assertTrue(type4Mer[i]==2||					
					type4Mer[i]==5||
					type4Mer[i]==10||
					type4Mer[i]==11
					);
		
		
		int []type4jup=kpr.getKCILType4(GlobalVariablesKPExtension.JUPITER);
		for(int i=0;i<type4jup.length;i++)			
			assertTrue(type4jup[i]==1||					
					type4jup[i]==4||
					type4jup[i]==8||
					type4jup[i]==9||					
					type4jup[i]==11
					);
		
		int []type4Ven=kpr.getKCILType4(GlobalVariablesKPExtension.VENUS);
		assertNull(type4Ven);
		
		
		int []type4Sat=kpr.getKCILType4(GlobalVariablesKPExtension.SAT);
		for(int i=0;i<type4Sat.length;i++)			
			assertTrue(type4Sat[i]==1||					
					type4Sat[i]==2||
					type4Sat[i]==7||
					type4Sat[i]==9
					);
		
	
		int []type4Rahu=kpr.getKCILType4(GlobalVariablesKPExtension.RAHU);
		assertNull(type4Rahu);
		
		
		int []type4Ketu=kpr.getKCILType4(GlobalVariablesKPExtension.KETU);
		for(int i=0;i<type4Ketu.length;i++)			
			assertTrue(type4Ketu[i]==2||									
					type4Ketu[i]==5||
					type4Ketu[i]==9
					);
		
	}
}
