package com.ojassoft.kpextension;

/**
 * 
 * @author Hukum
 *
 */
public class CKBPlanetSignificator implements IKBPlanetSignificator{

	double[] plntDegree;
	double[] cuspDegree;
	CKpRefactorExtension kpr;
	/**
	 * This is constructor, it inits all planet degree and cusp degree.
	 * @param plntDeg
	 * @param cuspDeg
	 */
	public CKBPlanetSignificator(double []plntDeg,double []cuspDeg)
	{
		plntDegree=plntDeg;
		cuspDegree=cuspDeg;
		kpr=new CKpRefactorExtension(plntDegree, cuspDegree);
	}

	
	public int[] getKB_PlanetSignificator(int planetNumber) {
		
		int[] result = new int[12];
		int index = 0;
		
		for(int i = 0 ; i<cuspDegree.length ; i++) {
			if(planetNumber == kpr.getSubLord(cuspDegree[i])) {
				result[index++] = i+1;
			}
		}
		
		if(index == 0) {
			int starLord = kpr.getStarLord(plntDegree[planetNumber]);
			int subLord  = kpr.getSubLord(plntDegree[planetNumber]);
			
			for(int i = 0 ; i<cuspDegree.length ; i++) {
				if(starLord == kpr.getSubLord(cuspDegree[i])) {
					result[index++] = i+1;
				}
			}
			for(int i = 0 ; i<cuspDegree.length ; i++) {
				if(subLord == kpr.getSubLord(cuspDegree[i])) {
					result[index++] = i+1;
				}
			}
		}
		
		if(index == 0) {
			int subsubLord = kpr.getSubSubLord(plntDegree[planetNumber]);
			for(int i = 0 ; i<cuspDegree.length ; i++) {
				if(subsubLord == kpr.getSubLord(cuspDegree[i])) {
					result[index++] = i+1;
				}
			}
		}
		
		if(index == 0) {
			result[index++] = kpr.getHouseOccupied(plntDegree[planetNumber]);
		}
		if(index == 0) {
			return null;
		}
		
		return CUtilsKPExtension.removeValueFromIntArray(0, result);
	}
}
