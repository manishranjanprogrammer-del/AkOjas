package com.ojassoft.kpextension;

import java.util.ArrayList;
import java.util.List;

import com.ojassoft.kpextension.CKpRefactorExtension;
import com.ojassoft.kpextension.GlobalVariablesKPExtension;

public class CKp4StepCalculation implements IKp4StepCalculation{
	private BeanKp4StepPlanet sunStep1=null,moonStep1=null,marsStep1=null,merStep1=null,jupStep1=null,venStep1=null,
			satStep1=null,rahuStep1=null,ketuStep1=null;
	private BeanKp4StepPlanet sunStep2=null,moonStep2=null,marsStep2=null,merStep2=null,jupStep2=null,venStep2=null,
			satStep2=null,rahuStep2=null,ketuStep2=null;
	private BeanKP4Step []kp4Step=new BeanKP4Step[9];
	private double []plntDegree;
	double []cuspDegree;
	private CKpRefactorExtension kpr;
	
	public CKp4StepCalculation(double []plntDeg,double []cuspDeg)
	{
		plntDegree=plntDeg;
		cuspDegree=cuspDeg;
		kpr=new CKpRefactorExtension(plntDegree, cuspDegree);
	}
	public void calculate()
	{
		for(int i=0;i<kp4Step.length;i++)
			kp4Step[i]=new BeanKP4Step();
		calculatePlanetStep1();
		calculatePlanetStep2();
		
		//SUN 
		kp4Step[0].setKp4Step1(getCalculatePlanetForStep1Or3(GlobalVariablesKPExtension.SUN));		
		kp4Step[0].setKp4Step2(getCalculatePlanetForStep2Or4(kpr.getStarLord(plntDegree[GlobalVariablesKPExtension.SUN])));
		kp4Step[0].setKp4Step3(getCalculatePlanetForStep1Or3(kpr.getSubLord(plntDegree[GlobalVariablesKPExtension.SUN])));
		kp4Step[0].setKp4Step4(getCalculatePlanetForStep2Or4(kpr.getStarLord(plntDegree[kpr.getSubLord(plntDegree[GlobalVariablesKPExtension.SUN])])));
		
		//MOON 
		kp4Step[1].setKp4Step1(getCalculatePlanetForStep1Or3(GlobalVariablesKPExtension.MOON));
		kp4Step[1].setKp4Step2(getCalculatePlanetForStep2Or4(kpr.getStarLord(plntDegree[GlobalVariablesKPExtension.MOON])));
		kp4Step[1].setKp4Step3(getCalculatePlanetForStep1Or3(kpr.getSubLord(plntDegree[GlobalVariablesKPExtension.MOON])));
		kp4Step[1].setKp4Step4(getCalculatePlanetForStep2Or4(kpr.getStarLord(plntDegree[kpr.getSubLord(plntDegree[GlobalVariablesKPExtension.MOON])])));
		
		//MARS
		kp4Step[2].setKp4Step1(getCalculatePlanetForStep1Or3(GlobalVariablesKPExtension.MARS));
		kp4Step[2].setKp4Step2(getCalculatePlanetForStep2Or4(kpr.getStarLord(plntDegree[GlobalVariablesKPExtension.MARS])));
		kp4Step[2].setKp4Step3(getCalculatePlanetForStep1Or3(kpr.getSubLord(plntDegree[GlobalVariablesKPExtension.MARS])));
		kp4Step[2].setKp4Step4(getCalculatePlanetForStep2Or4(kpr.getStarLord(plntDegree[kpr.getSubLord(plntDegree[GlobalVariablesKPExtension.MARS])])));
		
		//MER
		kp4Step[3].setKp4Step1(getCalculatePlanetForStep1Or3(GlobalVariablesKPExtension.MERCURY));
		kp4Step[3].setKp4Step2(getCalculatePlanetForStep2Or4(kpr.getStarLord(plntDegree[GlobalVariablesKPExtension.MERCURY])));
		kp4Step[3].setKp4Step3(getCalculatePlanetForStep1Or3(kpr.getSubLord(plntDegree[GlobalVariablesKPExtension.MERCURY])));
		kp4Step[3].setKp4Step4(getCalculatePlanetForStep2Or4(kpr.getStarLord(plntDegree[kpr.getSubLord(plntDegree[GlobalVariablesKPExtension.MERCURY])])));
		
		//JUP 
		kp4Step[4].setKp4Step1(getCalculatePlanetForStep1Or3(GlobalVariablesKPExtension.JUPITER));
		kp4Step[4].setKp4Step2(getCalculatePlanetForStep2Or4(kpr.getStarLord(plntDegree[GlobalVariablesKPExtension.JUPITER])));
		kp4Step[4].setKp4Step3(getCalculatePlanetForStep1Or3(kpr.getSubLord(plntDegree[GlobalVariablesKPExtension.JUPITER])));
		kp4Step[4].setKp4Step4(getCalculatePlanetForStep2Or4(kpr.getStarLord(plntDegree[kpr.getSubLord(plntDegree[GlobalVariablesKPExtension.JUPITER])])));
		//VEN 
		kp4Step[5].setKp4Step1(getCalculatePlanetForStep1Or3(GlobalVariablesKPExtension.VENUS));
		kp4Step[5].setKp4Step2(getCalculatePlanetForStep2Or4(kpr.getStarLord(plntDegree[GlobalVariablesKPExtension.VENUS])));
		kp4Step[5].setKp4Step3(getCalculatePlanetForStep1Or3(kpr.getSubLord(plntDegree[GlobalVariablesKPExtension.VENUS])));
		kp4Step[5].setKp4Step4(getCalculatePlanetForStep2Or4(kpr.getStarLord(plntDegree[kpr.getSubLord(plntDegree[GlobalVariablesKPExtension.VENUS])])));
		
		
		//SAT 
		kp4Step[6].setKp4Step1(getCalculatePlanetForStep1Or3(GlobalVariablesKPExtension.SAT));
		kp4Step[6].setKp4Step2(getCalculatePlanetForStep2Or4(kpr.getStarLord(plntDegree[GlobalVariablesKPExtension.SAT])));
		kp4Step[6].setKp4Step3(getCalculatePlanetForStep1Or3(kpr.getSubLord(plntDegree[GlobalVariablesKPExtension.SAT])));
		kp4Step[6].setKp4Step4(getCalculatePlanetForStep2Or4(kpr.getStarLord(plntDegree[kpr.getSubLord(plntDegree[GlobalVariablesKPExtension.SAT])])));
				
		
		//RAHU
		kp4Step[7].setKp4Step1(getCalculatePlanetForStep1Or3(GlobalVariablesKPExtension.RAHU));
		kp4Step[7].setKp4Step2(getCalculatePlanetForStep2Or4(kpr.getStarLord(plntDegree[GlobalVariablesKPExtension.RAHU])));
		kp4Step[7].setKp4Step3(getCalculatePlanetForStep1Or3(kpr.getSubLord(plntDegree[GlobalVariablesKPExtension.RAHU])));
		kp4Step[7].setKp4Step4(getCalculatePlanetForStep2Or4(kpr.getStarLord(plntDegree[kpr.getSubLord(plntDegree[GlobalVariablesKPExtension.RAHU])])));
			

		//KETU
		kp4Step[8].setKp4Step1(getCalculatePlanetForStep1Or3(GlobalVariablesKPExtension.KETU));
		kp4Step[8].setKp4Step2(getCalculatePlanetForStep2Or4(kpr.getStarLord(plntDegree[GlobalVariablesKPExtension.KETU])));
		kp4Step[8].setKp4Step3(getCalculatePlanetForStep1Or3(kpr.getSubLord(plntDegree[GlobalVariablesKPExtension.KETU])));
		kp4Step[8].setKp4Step4(getCalculatePlanetForStep2Or4(kpr.getStarLord(plntDegree[kpr.getSubLord(plntDegree[GlobalVariablesKPExtension.KETU])])));
				
		
	}
	
	private BeanKp4StepPlanet getCalculatePlanetForStep1Or3(int plntNumber)
	{
		 BeanKp4StepPlanet plnt=null;
		 switch(plntNumber)
		 {
			 case GlobalVariablesKPExtension.SUN:
				 //System.out.println("SUN");
				   plnt= sunStep1;
			 	break;
			 case GlobalVariablesKPExtension.MOON:
				   plnt= moonStep1;
			 	break;
			 case GlobalVariablesKPExtension.MARS:
				   plnt= marsStep1;
			 	break;
			 case GlobalVariablesKPExtension.MERCURY:
				   plnt= merStep1;
			 	break;
			 case GlobalVariablesKPExtension.JUPITER:
				   plnt= jupStep1;
			 	break;
			 case GlobalVariablesKPExtension.VENUS:
				   plnt= venStep1;
			 	break;
			 case GlobalVariablesKPExtension.SAT:
				   plnt= satStep1;				   
			 	break;
			 case GlobalVariablesKPExtension.RAHU:
				   plnt= rahuStep1;				   
			 	break;
			 case GlobalVariablesKPExtension.KETU:
				   plnt= ketuStep1;				   
			 	break;
			 
		 }
		 
		
		  return plnt;
	}

	
	private BeanKp4StepPlanet getCalculatePlanetForStep2Or4(int plntNumber)
	{
		 BeanKp4StepPlanet plnt=null;
		 switch(plntNumber)
		 {
			 case GlobalVariablesKPExtension.SUN:
				   plnt= sunStep2;
			 	break;
			 case GlobalVariablesKPExtension.MOON:
				   plnt= moonStep2;
			 	break;
			 case GlobalVariablesKPExtension.MARS:
				   plnt= marsStep2;
			 	break;
			 case GlobalVariablesKPExtension.MERCURY:
				   plnt= merStep2;
			 	break;
			 case GlobalVariablesKPExtension.JUPITER:
				   plnt= jupStep2;
			 	break;
			 case GlobalVariablesKPExtension.VENUS:
				   plnt= venStep2;
			 	break;
			 case GlobalVariablesKPExtension.SAT:
				   plnt= satStep2;
			 	break;
			 case GlobalVariablesKPExtension.RAHU:
				   plnt= rahuStep2;				   
			 	break;
			 case GlobalVariablesKPExtension.KETU:
				   plnt= ketuStep2;				   
			 	break;
			 
		 }
		 
		
		  return plnt;
	}
	private void calculatePlanetStep1()
	{

		sunStep1=getPlanetWithStep1(GlobalVariablesKPExtension.SUN);
		
		
		//MOON STEP-1
		moonStep1=getPlanetWithStep1(GlobalVariablesKPExtension.MOON);
		
		//MARS STEP-1
		marsStep1=getPlanetWithStep1(GlobalVariablesKPExtension.MARS);
		
		//MER STEP-1
		merStep1=getPlanetWithStep1(GlobalVariablesKPExtension.MERCURY);
		
		//JUP STEP-1
		jupStep1=getPlanetWithStep1(GlobalVariablesKPExtension.JUPITER);
		
		//VEN STEP-1
		venStep1=getPlanetWithStep1(GlobalVariablesKPExtension.VENUS);
		//SAT STEP-1
		satStep1=getPlanetWithStep1(GlobalVariablesKPExtension.SAT);
		//RAHU STEP-1
		rahuStep1=getPlanetWithStep1(GlobalVariablesKPExtension.RAHU);
		//KETU STEP-1
		ketuStep1=getPlanetWithStep1(GlobalVariablesKPExtension.KETU);
	}
	
	private void calculatePlanetStep2()
	{

		sunStep2=getPlanetWithStep2(GlobalVariablesKPExtension.SUN);
		
		
		//MOON STEP-1
		moonStep2=getPlanetWithStep2(GlobalVariablesKPExtension.MOON);
		
		//MARS STEP-1
		marsStep2=getPlanetWithStep2(GlobalVariablesKPExtension.MARS);
		
		//MER STEP-1
		merStep2=getPlanetWithStep2(GlobalVariablesKPExtension.MERCURY);
		
		//JUP STEP-1
		jupStep2=getPlanetWithStep2(GlobalVariablesKPExtension.JUPITER);
		
		//VEN STEP-1
		venStep2=getPlanetWithStep2(GlobalVariablesKPExtension.VENUS);
		//SAT STEP-1
		satStep2=getPlanetWithStep2(GlobalVariablesKPExtension.SAT);
		//RAHU STEP-1
		rahuStep2=getPlanetWithStep2(GlobalVariablesKPExtension.RAHU);
		//KETU STEP-1
		ketuStep2=getPlanetWithStep2(GlobalVariablesKPExtension.KETU);
	}
	private BeanKp4StepPlanet getPlanetWithStep1(int plntNumber)
	{
		BeanKp4StepPlanet step1=new BeanKp4StepPlanet();
		
		step1.setPlanet(plntNumber);
		step1.setPlanetStrong(kpr.isPlanetStrongSignificatorIn4Step(plntNumber));
		if(step1.isPlanetStrong())
		{
			step1.setPlanetInItsOwnStar(kpr.isPlanetInItsOwnStar(plntNumber));
			step1.setPlanetStarLordOfOtherPlanet(kpr.checkPlanetIsStarLordOfOtherPlanet(plntNumber));
			
		}
		
		
		if((plntNumber==GlobalVariablesKPExtension.RAHU)||(plntNumber==GlobalVariablesKPExtension.KETU))
		{
			
			int rashiLord=kpr.getRashiLordOfHouseWherePlanetPlaced(plntNumber);
			 KeyValue4Step obj=new KeyValue4Step();
			 obj.setKey(rashiLord);
			 obj.setValues(kpr.get4StepTypeN2(rashiLord));
			 step1.setRahuKetPlacedInPlanetRashi(obj);
			
			 
		}
		
		//step1.setPlanetStrong(kpr.isPlanetStrongSignificatorIn4Step(plntNumber));
		step1.setPlanetStepValues(kpr.get4StepTypeN1(plntNumber));
		step1.setPlanetCuspConjunction(kpr.getPlanetConjunctionWithCuspIn4Step(plntNumber));
		
		//FOR CONJUNCTION
		int []plntConSun=kpr.getPlanetConjunctionIn4Step(plntNumber);
		
		
		 if(plntConSun!=null)
		 {
			 List<KeyValue4Step> sunConjWithPlanets = new ArrayList<KeyValue4Step>();
			
			 for(int pcs=0;pcs<plntConSun.length;pcs++)
			 {
				
				 KeyValue4Step obj=new KeyValue4Step();
				 obj.setKey(plntConSun[pcs]);
				 if(kpr.isPlanetStrongSignificatorIn4Step(obj.getKey()))
					 obj.setValues(kpr.get4StepTypeN1(obj.getKey()));
				 else
					 obj.setValues(kpr.get4StepTypeN2(obj.getKey()));
				 
				 
				 if((obj.getKey()==GlobalVariablesKPExtension.RAHU)||(obj.getKey()==GlobalVariablesKPExtension.KETU))
				 {
					 int rashiLord=kpr.getRashiLordOfHouseWherePlanetPlaced(obj.getKey());
					 obj.setPlanetInWhichRahuKetuPlaced(rashiLord);							
					 obj.setPlanetValuesInWhichRahuKetuPlaced(kpr.get4StepTypeN2(rashiLord));
				 }
				 
				 sunConjWithPlanets.add(obj);						 
				 
			 }
			 step1.setPlanetConjunction(sunConjWithPlanets);
			
		 }
		
		 
		 if((plntNumber!=GlobalVariablesKPExtension.RAHU)&&(plntNumber!=GlobalVariablesKPExtension.KETU))
		 {
			//PLANET ASPECT
			 int []plntAspectSun=kpr.getPlanetsAspectToPlanetIn4Step(plntNumber);
			
			 if(plntAspectSun!=null)
			 {
				 List<KeyValue4Step> planetAspectOnSun = new ArrayList<KeyValue4Step>();
				 for(int pas=0;pas<plntAspectSun.length;pas++)
				 {
					 KeyValue4Step obj=new KeyValue4Step();
					 obj.setKey(plntAspectSun[pas]);
					 if(kpr.isPlanetStrongSignificatorIn4Step(obj.getKey()))
						 obj.setValues(kpr.get4StepTypeN1(obj.getKey()));
					 else
						 obj.setValues(kpr.get4StepTypeN2(obj.getKey()));
					 
					 if((obj.getKey()==GlobalVariablesKPExtension.RAHU)||(obj.getKey()==GlobalVariablesKPExtension.KETU))
					 {
						 int rashiLord=kpr.getRashiLordOfHouseWherePlanetPlaced(obj.getKey());
						 obj.setPlanetInWhichRahuKetuPlaced(rashiLord);							
						 obj.setPlanetValuesInWhichRahuKetuPlaced(kpr.get4StepTypeN2(rashiLord));
					 }
					 
					 planetAspectOnSun.add(obj);						 
					 
				 }
				 step1.setPlanetAspect(planetAspectOnSun);
			 }
		 }
		
		//CUSP ASPECT
		//int []cuspAspectSun=kpr.getAspectOfPlanetOnCuspIn4Step(plntNumber);
		// System.out.println("10 ");
		step1.setCuspAspect(kpr.getAspectOfPlanetOnCuspIn4Step(plntNumber));
		// System.out.println("11");
		
		return step1;
	}
	
	private BeanKp4StepPlanet getPlanetWithStep2(int plntNumber)
	{
		BeanKp4StepPlanet step2=new BeanKp4StepPlanet();
		
		step2.setPlanet(plntNumber);
		step2.setPlanetStepValues(kpr.get4StepTypeN2(plntNumber));
		step2.setPlanetCuspConjunction(kpr.getPlanetConjunctionWithCuspIn4Step(plntNumber));
		if((plntNumber==GlobalVariablesKPExtension.RAHU)||(plntNumber==GlobalVariablesKPExtension.KETU))
		{
			int rashiLord=kpr.getRashiLordOfHouseWherePlanetPlaced(plntNumber);
			 KeyValue4Step obj=new KeyValue4Step();
			 obj.setKey(rashiLord);
			 obj.setValues(kpr.get4StepTypeN2(rashiLord));
			 step2.setRahuKetPlacedInPlanetRashi(obj);
			 
		}
		
		//FOR CONJUNCTION
				int []plntConSun=kpr.getPlanetConjunctionIn4Step(plntNumber);
				
				 if(plntConSun!=null)
				 {
					 List<KeyValue4Step> sunConjWithPlanets = new ArrayList<KeyValue4Step>();
					 for(int pcs=0;pcs<plntConSun.length;pcs++)
					 {
						 KeyValue4Step obj=new KeyValue4Step();
						 obj.setKey(plntConSun[pcs]);
						 if(kpr.isPlanetStrongSignificatorIn4Step(obj.getKey()))
							 obj.setValues(kpr.get4StepTypeN1(obj.getKey()));
						 else
							 obj.setValues(kpr.get4StepTypeN2(obj.getKey()));						 
						

					
						 if((obj.getKey()==GlobalVariablesKPExtension.RAHU)||(obj.getKey()==GlobalVariablesKPExtension.KETU))
						 {
							 int rashiLord=kpr.getRashiLordOfHouseWherePlanetPlaced(obj.getKey());
							 obj.setPlanetInWhichRahuKetuPlaced(rashiLord);							
							 obj.setPlanetValuesInWhichRahuKetuPlaced(kpr.get4StepTypeN2(rashiLord));
						 }
					
						 sunConjWithPlanets.add(obj);
						 
					 }
					 step2.setPlanetConjunction(sunConjWithPlanets);
				 }
				
				 if((plntNumber!=GlobalVariablesKPExtension.RAHU)&&(plntNumber!=GlobalVariablesKPExtension.KETU))
				 {
					//PLANET ASPECT
					 int []plntAspectSun=kpr.getPlanetsAspectToPlanetIn4Step(plntNumber);
					
					 if(plntAspectSun!=null)
					 {
						 List<KeyValue4Step> planetAspectOnSun = new ArrayList<KeyValue4Step>();
						 for(int pas=0;pas<plntAspectSun.length;pas++)
						 {
							 KeyValue4Step obj=new KeyValue4Step();
							 obj.setKey(plntAspectSun[pas]);
							 if(kpr.isPlanetStrongSignificatorIn4Step(obj.getKey()))
								 obj.setValues(kpr.get4StepTypeN1(obj.getKey()));
							 else
								 obj.setValues(kpr.get4StepTypeN2(obj.getKey()));
							 
							
							 
							 if((obj.getKey()==GlobalVariablesKPExtension.RAHU)||(obj.getKey()==GlobalVariablesKPExtension.KETU))
							 {
								 int rashiLord=kpr.getRashiLordOfHouseWherePlanetPlaced(obj.getKey());
								 obj.setPlanetInWhichRahuKetuPlaced(rashiLord);							
								 obj.setPlanetValuesInWhichRahuKetuPlaced(kpr.get4StepTypeN2(rashiLord));
							 }
							 planetAspectOnSun.add(obj);
							 
						 }
						 step2.setPlanetAspect(planetAspectOnSun);
					 }
				 }
				
				//CUSP ASPECT
				//int []cuspAspectSun=kpr.getAspectOfPlanetOnCuspIn4Step(plntNumber);
				
				step2.setCuspAspect(kpr.getAspectOfPlanetOnCuspIn4Step(plntNumber));
		
		return step2;
	}
	
	
	public BeanKP4Step getKp4StepForPlanet(int plntNumber)
	{
		return kp4Step[plntNumber];
	}
	

}
