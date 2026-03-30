package com.astrosagekundli.junit;

import com.ojassoft.kpextension.BeanKP4Step;
import com.ojassoft.kpextension.BeanKp4StepPlanet;
import com.ojassoft.kpextension.CKp4StepCalculation;
import com.ojassoft.kpextension.GlobalVariablesKPExtension;
import com.ojassoft.kpextension.KeyValue4Step;

public class NewTestWithValues {
	public static void main(String[] args) {
		
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("----------------NEERAJ----------------------");
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
		
		CKp4StepCalculation cNeeraj=new CKp4StepCalculation(GlobalVariablesKPExtensionTest.PLANST_VALUES_NEERAJ, GlobalVariablesKPExtensionTest.KP_CUSP_VALUES_NEERAJ);
		cNeeraj.calculate();
		
		showKP4PlanetStepBijendra(cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.SUN));
		showKP4PlanetStepBijendra(cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.MOON));
		showKP4PlanetStepBijendra(cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.MARS));
		showKP4PlanetStepBijendra(cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.MERCURY));
		showKP4PlanetStepBijendra(cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.JUPITER));
		showKP4PlanetStepBijendra(cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.VENUS));
		showKP4PlanetStepBijendra(cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.SAT));
		showKP4PlanetStepBijendra(cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.RAHU));
		showKP4PlanetStepBijendra(cNeeraj.getKp4StepForPlanet(GlobalVariablesKPExtension.KETU));
		System.out.println("");
		System.out.println("");
		
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("-------------------PUNIT---------------------");
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
		CKp4StepCalculation cPunit=new CKp4StepCalculation(GlobalVariablesKPExtensionTest.PLANST_VALUES_PUNIT, GlobalVariablesKPExtensionTest.KP_CUSP_VALUES_PUNIT);
		cPunit.calculate();
		
		showKP4PlanetStepBijendra(cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.SUN));
		showKP4PlanetStepBijendra(cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.MOON));
		showKP4PlanetStepBijendra(cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.MARS));
		showKP4PlanetStepBijendra(cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.MERCURY));
		showKP4PlanetStepBijendra(cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.JUPITER));
		showKP4PlanetStepBijendra(cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.VENUS));
		showKP4PlanetStepBijendra(cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.SAT));
		showKP4PlanetStepBijendra(cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.RAHU));
		showKP4PlanetStepBijendra(cPunit.getKp4StepForPlanet(GlobalVariablesKPExtension.KETU));
		System.out.println("");
		System.out.println("");
		
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("-------------------HUKUM---------------------");
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
		
		CKp4StepCalculation cHukum=new CKp4StepCalculation(GlobalVariablesKPExtensionTest.PLANST_VALUES_HUKUM, GlobalVariablesKPExtensionTest.KP_CUSP_VALUES_HUKUM);
		cHukum.calculate();
		
		showKP4PlanetStepBijendra(cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.SUN));
		showKP4PlanetStepBijendra(cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.MOON));
		showKP4PlanetStepBijendra(cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.MARS));
		showKP4PlanetStepBijendra(cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.MERCURY));
		showKP4PlanetStepBijendra(cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.JUPITER));
		showKP4PlanetStepBijendra(cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.VENUS));
		showKP4PlanetStepBijendra(cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.SAT));
		showKP4PlanetStepBijendra(cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.RAHU));
		showKP4PlanetStepBijendra(cHukum.getKp4StepForPlanet(GlobalVariablesKPExtension.KETU));
		System.out.println("");
	}

	private static void showKP4PlanetStepBijendra(BeanKP4Step kp4StepcBeanKP4Steps)
	{
		System.out.println("");
		System.out.println("********************************************************************* ");
		System.out.println(GlobalVariablesKPExtensionTest.PLANET_NAME[kp4StepcBeanKP4Steps.getKp4Step1().getPlanet()]);
		//STEP-1
		printStep1(kp4StepcBeanKP4Steps.getKp4Step1());
		printCuspalConjunctionStep1Or3(kp4StepcBeanKP4Steps.getKp4Step1());
		printPlanetConjunctionStep1(kp4StepcBeanKP4Steps.getKp4Step1());
		printPlanetCuspalAspectStep1(kp4StepcBeanKP4Steps.getKp4Step1());
		printPlanetAspectStep1(kp4StepcBeanKP4Steps.getKp4Step1());
		
		//STEP-2
		printStep2(kp4StepcBeanKP4Steps.getKp4Step2());
		printCuspalConjunctionStep2Or4(kp4StepcBeanKP4Steps.getKp4Step2());
		printPlanetConjunctionStep2Or4(kp4StepcBeanKP4Steps.getKp4Step2());
		printPlanetCuspalAspectStep2Or4(kp4StepcBeanKP4Steps.getKp4Step2());
		printPlanetAspectStep2Or4(kp4StepcBeanKP4Steps.getKp4Step2());
		
		//STEP-3
		System.out.println("");
		printStep3(kp4StepcBeanKP4Steps.getKp4Step3());
		printCuspalConjunctionStep3(kp4StepcBeanKP4Steps.getKp4Step3());
		printPlanetConjunctionStep3(kp4StepcBeanKP4Steps.getKp4Step3());
		printPlanetCuspalStep3(kp4StepcBeanKP4Steps.getKp4Step3());
		printPlanetAspectStep3(kp4StepcBeanKP4Steps.getKp4Step3());
		
		//STEP-4
		printStep4(kp4StepcBeanKP4Steps.getKp4Step4());
		printCuspalConjunctionStep2Or4(kp4StepcBeanKP4Steps.getKp4Step4());
		printPlanetConjunctionStep2Or4(kp4StepcBeanKP4Steps.getKp4Step4());
		printPlanetCuspalAspectStep2Or4(kp4StepcBeanKP4Steps.getKp4Step4());
		printPlanetAspectStep2Or4(kp4StepcBeanKP4Steps.getKp4Step4());
	}
	
	//FUNCTIONS -STEP-1/STEP-3
	public static void printStep3(BeanKp4StepPlanet beanKP4planet)
	{
		
		System.out.print(" STEP-3 : "+GlobalVariablesKPExtensionTest.PLANET_NAME[beanKP4planet.getPlanet()]);
		if(beanKP4planet.getPlanetStepValues()!=null)
		{
			int lSuSt1=beanKP4planet.getPlanetStepValues().length;
			for(int i=0;i<lSuSt1;i++)
				System.out.print(" "+beanKP4planet.getPlanetStepValues()[i]);
		
		
			if((beanKP4planet.getPlanet()==GlobalVariablesKPExtension.RAHU)||(beanKP4planet.getPlanet()==GlobalVariablesKPExtension.KETU))
			{
				KeyValue4Step obj=beanKP4planet.getRahuKetPlacedInPlanetRashi();
				if(obj.getValues()!=null)
				{
					System.out.print(" ("+" "+GlobalVariablesKPExtensionTest.PLANET_NAME[obj.getKey()]);
					for(int i=0;i<obj.getValues().length;i++)
						System.out.print(" "+obj.getValues()[i]);
					System.out.print(")");
				}
			}
		}
		
		
	}
	
	public static void printStep1(BeanKp4StepPlanet beanKP4planet)
	{
		System.out.print(" STEP-1 : ");
		if(beanKP4planet.getPlanetStepValues()!=null)
		{
			int lSuSt1=beanKP4planet.getPlanetStepValues().length;
			for(int i=0;i<lSuSt1;i++)
				System.out.print(" "+beanKP4planet.getPlanetStepValues()[i]);
		
		
			if((beanKP4planet.getPlanet()==GlobalVariablesKPExtension.RAHU)||(beanKP4planet.getPlanet()==GlobalVariablesKPExtension.KETU))
			{
				KeyValue4Step obj=beanKP4planet.getRahuKetPlacedInPlanetRashi();
				if(obj.getValues()!=null)
				{
					System.out.print(" ("+" "+GlobalVariablesKPExtensionTest.PLANET_NAME[obj.getKey()]);
					for(int i=0;i<obj.getValues().length;i++)
						System.out.print(" "+obj.getValues()[i]);
					System.out.print(")");
				}
			}
		}
		
		
	}
	
	public static void printCuspalConjunctionStep1Or3(BeanKp4StepPlanet beanKP4planet)
	{
		
		/*if(beanKP4planet.isPlanetStrong())		{
			if(beanKP4planet.getPlanetCuspConjunction()>0)
			System.out.print("  "+beanKP4planet.getPlanetCuspConjunction()+"(Cuspal)");
			
			
		}*/
		if(beanKP4planet.getPlanetCuspConjunction()>0)
			System.out.print("  "+beanKP4planet.getPlanetCuspConjunction()+"(Cuspal)");
		
		
	}
	public static void printCuspalConjunctionStep3(BeanKp4StepPlanet beanKP4planet)
	{
		
			if(beanKP4planet.getPlanetCuspConjunction()>0)
				System.out.print("  "+beanKP4planet.getPlanetCuspConjunction()+"(Cuspal)");
			
	}
	
	public static void printPlanetConjunctionStep1(BeanKp4StepPlanet beanKP4planet)
	{
		/*if(beanKP4planet.isPlanetStrong())
		{
			if(beanKP4planet.getPlanetConjunction().size()>0)
			{
				for(KeyValue4Step obj:beanKP4planet.getPlanetConjunction())
				{
					if(obj.getValues()!=null)
					{
						System.out.print("  CONJ-"+" "+GlobalVariablesKPExtensionTest.PLANET_NAME[obj.getKey()]);
						for(int i=0;i<obj.getValues().length;i++)
							System.out.print("  "+obj.getValues()[i]);
					}
				}
			}
		
		}*/
		
			if(beanKP4planet.getPlanetConjunction().size()>0)
			{
				for(KeyValue4Step obj:beanKP4planet.getPlanetConjunction())
				{
					if(obj.getValues()!=null)
					{
						System.out.print("  (CONJ-"+" "+GlobalVariablesKPExtensionTest.PLANET_NAME[obj.getKey()]);
						for(int i=0;i<obj.getValues().length;i++)
							System.out.print("  "+obj.getValues()[i]);
						System.out.print(")");
					}
				}
			}
		
		
		
	}
	public static void printPlanetConjunctionStep3(BeanKp4StepPlanet beanKP4planet)
	{
		
			if(beanKP4planet.getPlanetConjunction().size()>0)
			{
				for(KeyValue4Step obj:beanKP4planet.getPlanetConjunction())
				{
					if(obj.getValues()!=null)
					{
						System.out.print("  (CONJ-"+" "+GlobalVariablesKPExtensionTest.PLANET_NAME[obj.getKey()]);
						for(int i=0;i<obj.getValues().length;i++)
							System.out.print("  "+obj.getValues()[i]);
						
						if((obj.getKey()==GlobalVariablesKPExtension.RAHU)||(obj.getKey()==GlobalVariablesKPExtension.KETU))
						{
							if(obj.gePlanetValuesInWhichRahuKetuPlaced()!=null)
							{
								System.out.print(" ("+" "+GlobalVariablesKPExtensionTest.PLANET_NAME[obj.getPlanetInWhichRahuKetuPlaced()]);
								for(int i=0;i<obj.gePlanetValuesInWhichRahuKetuPlaced().length;i++)
									System.out.print(" "+obj.gePlanetValuesInWhichRahuKetuPlaced()[i]);
								System.out.print(")");
							}
						}
						System.out.print(")");
					}
				}
			}
		
		
		
	}
	
	public static void printPlanetCuspalAspectStep1(BeanKp4StepPlanet beanKP4planet)
	{
		if(beanKP4planet.isPlanetStrong())
		{
			if(beanKP4planet.getCuspAspect()!=null)
			{
			
					System.out.print(" ( Cusp-Asp  ");
					for(int i=0;i<beanKP4planet.getCuspAspect().length;i++)
						System.out.print(" "+beanKP4planet.getCuspAspect()[i]);
					
					System.out.print(" ) ");
			}
			
		}
		
	}
	public static void printPlanetCuspalStep3(BeanKp4StepPlanet beanKP4planet)
	{
		
			if(beanKP4planet.getCuspAspect()!=null)
			{
			
					System.out.print(" ( Cusp-Asp  ");
					for(int i=0;i<beanKP4planet.getCuspAspect().length;i++)
						System.out.print(" "+beanKP4planet.getCuspAspect()[i]);
					
					System.out.print(" ) ");
			}
			
		
		
	}
	
	public static void printPlanetAspectStep1(BeanKp4StepPlanet beanKP4planet)
	{
		/*if(beanKP4planet.isPlanetStrong())
		{
			if(beanKP4planet.getPlanetAspect().size()>0)
			{
				for(KeyValue4Step obj:beanKP4planet.getPlanetAspect())
				{
					if(obj.getValues()!=null)
					{
						System.out.print("  Asp-"+" "+GlobalVariablesKPExtensionTest.PLANET_NAME[obj.getKey()]);
						for(int i=0;i<obj.getValues().length;i++)
							System.out.print("  "+obj.getValues()[i]);
					}
				}
			}
		
		}*/
		
			if(beanKP4planet.getPlanetAspect().size()>0)
			{
				for(KeyValue4Step obj:beanKP4planet.getPlanetAspect())
				{
					if(obj.getValues()!=null)
					{
						System.out.print(" (Asp-"+" "+GlobalVariablesKPExtensionTest.PLANET_NAME[obj.getKey()]);
						for(int i=0;i<obj.getValues().length;i++)
							System.out.print("  "+obj.getValues()[i]);
						
						if((obj.getKey()==GlobalVariablesKPExtension.RAHU)||(obj.getKey()==GlobalVariablesKPExtension.KETU))
						{
							if(obj.gePlanetValuesInWhichRahuKetuPlaced()!=null)
							{
								System.out.print(" ("+" "+GlobalVariablesKPExtensionTest.PLANET_NAME[obj.getPlanetInWhichRahuKetuPlaced()]);
								for(int i=0;i<obj.gePlanetValuesInWhichRahuKetuPlaced().length;i++)
									System.out.print(" "+obj.gePlanetValuesInWhichRahuKetuPlaced()[i]);
								System.out.print(")");
							}
						}
						System.out.print(")");
					}
				}
			}
		
		
		
	}
	public static void printPlanetAspectStep3(BeanKp4StepPlanet beanKP4planet)
	{
		
			if(beanKP4planet.getPlanetAspect().size()>0)
			{
				for(KeyValue4Step obj:beanKP4planet.getPlanetAspect())
				{
					if(obj.getValues()!=null)
					{
						System.out.print("  (Asp-"+" "+GlobalVariablesKPExtensionTest.PLANET_NAME[obj.getKey()]);
						for(int i=0;i<obj.getValues().length;i++)
							System.out.print("  "+obj.getValues()[i]);
						if((obj.getKey()==GlobalVariablesKPExtension.RAHU)||(obj.getKey()==GlobalVariablesKPExtension.KETU))
						{
							if(obj.gePlanetValuesInWhichRahuKetuPlaced()!=null)
							{
								System.out.print(" ("+" "+GlobalVariablesKPExtensionTest.PLANET_NAME[obj.getPlanetInWhichRahuKetuPlaced()]);
								for(int i=0;i<obj.gePlanetValuesInWhichRahuKetuPlaced().length;i++)
									System.out.print(" "+obj.gePlanetValuesInWhichRahuKetuPlaced()[i]);
								System.out.print(")");
							}
						}
						System.out.print(")");
					}
				}
			}
		
		
		
	}
	
	//END-FUNCTIONS -STEP-1/STEP3
	
	//FUNCTIONS -STEP-2/STEP-4
	public static void printPlanetCuspalAspectStep2Or4(BeanKp4StepPlanet beanKP4planet)
	{
		
			if(beanKP4planet.getCuspAspect()!=null)
			{
			
					System.out.print(" ( Cusp-Asp  ");
					for(int i=0;i<beanKP4planet.getCuspAspect().length;i++)
						System.out.print(" "+beanKP4planet.getCuspAspect()[i]);
					
					System.out.print(" ) ");
			}
			
		
		
	}
	
	
	public static void printStep4(BeanKp4StepPlanet beanKP4planet)	{
		System.out.println("");
		System.out.print(" STEP-4 : "+GlobalVariablesKPExtensionTest.PLANET_NAME[beanKP4planet.getPlanet()]);
	
		if(beanKP4planet.getPlanetStepValues()!=null)
		{
			int lSuSt1=beanKP4planet.getPlanetStepValues().length;
			for(int i=0;i<lSuSt1;i++)
				System.out.print(" "+beanKP4planet.getPlanetStepValues()[i]);
		
		
			if((beanKP4planet.getPlanet()==GlobalVariablesKPExtension.RAHU)||(beanKP4planet.getPlanet()==GlobalVariablesKPExtension.KETU))
			{
				KeyValue4Step obj=beanKP4planet.getRahuKetPlacedInPlanetRashi();
				if(obj.getValues()!=null)
				{
					System.out.print(" ("+" "+GlobalVariablesKPExtensionTest.PLANET_NAME[obj.getKey()]);
					for(int i=0;i<obj.getValues().length;i++)
						System.out.print(" "+obj.getValues()[i]);
					System.out.print(")");
				}
			}
		}
		
		
	}
	
	public static void printStep2(BeanKp4StepPlanet beanKP4planet)	{
		System.out.println("");
		System.out.print(" STEP-2 : "+GlobalVariablesKPExtensionTest.PLANET_NAME[beanKP4planet.getPlanet()]);
	
		if(beanKP4planet.getPlanetStepValues()!=null)
		{
			int lSuSt1=beanKP4planet.getPlanetStepValues().length;
			for(int i=0;i<lSuSt1;i++)
				System.out.print(" "+beanKP4planet.getPlanetStepValues()[i]);
		
		
			if((beanKP4planet.getPlanet()==GlobalVariablesKPExtension.RAHU)||(beanKP4planet.getPlanet()==GlobalVariablesKPExtension.KETU))
			{
				KeyValue4Step obj=beanKP4planet.getRahuKetPlacedInPlanetRashi();
				if(obj.getValues()!=null)
				{
					System.out.print(" ("+" "+GlobalVariablesKPExtensionTest.PLANET_NAME[obj.getKey()]);
					for(int i=0;i<obj.getValues().length;i++)
						System.out.print(" "+obj.getValues()[i]);
					System.out.print(")");
				}
			}
		}
		
		
	}
	

	public static void printCuspalConjunctionStep2Or4(BeanKp4StepPlanet beanKP4planet)
	{	
		
			if(beanKP4planet.getPlanetCuspConjunction()>0)
				System.out.print("  "+beanKP4planet.getPlanetCuspConjunction()+"(Cuspal)");
		
	}
	
	public static void printPlanetConjunctionStep2Or4(BeanKp4StepPlanet beanKP4planet)
	{
		
			if(beanKP4planet.getPlanetConjunction().size()>0)
			{
				for(KeyValue4Step obj:beanKP4planet.getPlanetConjunction())
				{
					if(obj.getValues()!=null)
					{
					
						
						System.out.print("  (CONJ-"+" "+GlobalVariablesKPExtensionTest.PLANET_NAME[obj.getKey()]);
						for(int i=0;i<obj.getValues().length;i++)
							System.out.print("  "+obj.getValues()[i]);

						if((obj.getKey()==GlobalVariablesKPExtension.RAHU)||(obj.getKey()==GlobalVariablesKPExtension.KETU))
						{
							if(obj.gePlanetValuesInWhichRahuKetuPlaced()!=null)
							{
								System.out.print(" ("+" "+GlobalVariablesKPExtensionTest.PLANET_NAME[obj.getPlanetInWhichRahuKetuPlaced()]);
								for(int i=0;i<obj.gePlanetValuesInWhichRahuKetuPlaced().length;i++)
									System.out.print(" "+obj.gePlanetValuesInWhichRahuKetuPlaced()[i]);
								System.out.print(")");
							}
						}
						System.out.print(")");
					}
				}
			}
		
		
		
	}
	
	public static void printPlanetAspectStep2Or4(BeanKp4StepPlanet beanKP4planet)
	{
		if(beanKP4planet.getPlanetAspect().size()>0)
		{
			for(KeyValue4Step obj:beanKP4planet.getPlanetAspect())
			{
				if(obj.getValues()!=null)
				{
				
					
					//System.out.print("  CONJ-"+" "+GlobalVariablesKPExtensionTest.PLANET_NAME[obj.getKey()]);
					System.out.print(" ( Asp-"+" "+GlobalVariablesKPExtensionTest.PLANET_NAME[obj.getKey()]);
					for(int i=0;i<obj.getValues().length;i++)
						System.out.print("  "+obj.getValues()[i]);

					if((obj.getKey()==GlobalVariablesKPExtension.RAHU)||(obj.getKey()==GlobalVariablesKPExtension.KETU))
					{
						if(obj.gePlanetValuesInWhichRahuKetuPlaced()!=null)
						{
							System.out.print(" ("+" "+GlobalVariablesKPExtensionTest.PLANET_NAME[obj.getPlanetInWhichRahuKetuPlaced()]);
							for(int i=0;i<obj.gePlanetValuesInWhichRahuKetuPlaced().length;i++)
								System.out.print(" "+obj.gePlanetValuesInWhichRahuKetuPlaced()[i]);
							System.out.print(")");
						}
					}
					System.out.print(")");
				}
			}
		}
	}
	
}
