package com.ojassoft.astrosage.constants;

import com.ojassoft.astrosage.jinterface.IConstants;

public class ConstantsKa implements IConstants
    {
		@Override
		public String getMasas(int index) {
			// TODO Auto-generated method stub
			return masas[index];
		}

		@Override
		public String getVaras(int index) {
			// TODO Auto-generated method stub
			return varas[index];
		}

		@Override
		public String getVarasSwami(int index) {
			// TODO Auto-generated method stub
			return vaars_swami[index];
		}
		
		@Override
		public String getSamvats(int index) {
			// TODO Auto-generated method stub
			return samvats[index];
		}

		@Override
		public String getRitus(int index) {
			// TODO Auto-generated method stub
			return ritus[index];
		}

		@Override
		public String getKaranas(int index) {
			// TODO Auto-generated method stub
			return karanas[index];
		}

		@Override
		public String getPakshas(int index) {
			// TODO Auto-generated method stub
			return pakshas[index];
		}

		@Override
		public String getAyanas(int index) {
			// TODO Auto-generated method stub
			return ayanas[index];
		}

		@Override
		public String getDishas(int index) {
			// TODO Auto-generated method stub
			return dishas[index];
		}

		@Override
		public String getNakshatra(int index) {
			// TODO Auto-generated method stub
			return nakshatra[index];
		}

		@Override
		public String getYoga(int index) {
			// TODO Auto-generated method stub
			return yoga[index];
		}

		@Override
		public String getMoonSign(int index) {
			// TODO Auto-generated method stub
			return rashi[index];
		}

		@Override
		public String getTithi(int index) {
			// TODO Auto-generated method stub
			return tithi[index];
		}

		@Override
		public String getChandraBala(int index) {
			// TODO Auto-generated method stub
			return rashi[index];
		}

		@Override
		public String getTaraBala(int index) {
			// TODO Auto-generated method stub
			return nakshatra[index];
		}

		@Override
		public String[] getChandraBala() {
			// TODO Auto-generated method stub
			return rashi;
		}

		@Override
		public String[] getTaraBala() {
			// TODO Auto-generated method stub
			return nakshatra;
		}
		
		@Override
		public String getMonthName(int index) {
			// TODO Auto-generated method stub
			return monthname[index];
		}
		
		@Override
		public String getMonthNameSort(int index) {
			// TODO Auto-generated method stub
			return monthnameSort[index];
		}
		
		@Override
		public String getDayName(int index) {
			// TODO Auto-generated method stub
			return dayname[index];
		}
		
		@Override
		public String getChoghadiaDayName(int index) {
			return choghadiaDayName[index];
		}

		@Override
		public String getChoghadiaNightName(int index) {
			return choghadiaNightName[index];
		}
		
		@Override
		public String getExString(int index) {
			// TODO Auto-generated method stub
			return exstring[index];
		}
	  
		@Override
		public String getLagna(int index) {
			return rashi2[index];
		}
		
		@Override
		public String getLagnaNature(int index) {
			return rashiNature[index];
		}
		
		@Override
		public String getRashi(int index) {
			// TODO Auto-generated method stub
			return rashi2[index];
		}
		
		public final String[] masas = { "ಚೈತ್ರ", "ವೈಶಾಖ", "ಜ್ಯೇಷ್ಠ", "ಆಷಾಡ", "ಶ್ರಾವಣ", "ಭಾದ್ರಪದ", "ಆಶ್ವೀಜ", "ಕಾರ್ತಿಕ", "ಮಾರ್ಗಶೀರ", "ಪುಷ್ಯ", "ಮಾಘ", "ಫಾಲ್ಗುಣ" };

	      // base 0
	      public final String[] varas = { "ಬಾನುವಾರ", "ಸೋಮವಾರ", "ಮಂಗಳವಾರ", "ಬುಧವಾರ", "ಗುರುವಾರ", "ಶುಕ್ರವಾರ", "ಶನಿವಾರ" };
	      
	      public final String[] vaars_swami = { "NA", "ಸೂರ್ಯ", "ಚಂದ್ರ", "ಮಂಗಳ", "ಬುಧ", "ಗುರು", "ಶುಕ್ರ", "ಶನಿ" };

	      // base 1
	       public final String[] samvats = {"Na","ಪ್ರಭವ", "ವಿಭವ", "ಶುಕ್ಲ", "ಪ್ರಮೋದ", "ಪ್ರಜಾಪತಿ", "ಆಂಗೀರಸ", "ಶ್ರೀಮುಖ", "ಭಾವ", 
				"ಯುವ", "ಧಾತ್ರಿ","ಈಶ್ವರ", "ಬಹುಧಾನ್ಯ", "ಪ್ರಮಾಥಿ", "ವಿಕ್ರಮ", "ವೃಷ", "ಚಿತ್ರಭಾನು", 
				"ಸ್ವಭಾನು", "ತಾರಣ", "ಪಾರ್ಥಿವ", "ವ್ಯಯ", "ಸರ್ವಜಿತ್", "ಸರ್ವಧಾರಿ", "ವಿರೋಧಿ", "ವಿಕೃತಿ",
				"ಖರ","ನಂದನ","ವಿಜಯ", "ಜಯ", "ಮನ್ಮಥ", "ದುರ್ಮುಖಿ", "ಹೇವಿಲಂಬಿ", "ವಿಲಂಬಿ",
				"ವಿಕಾರಿ", "ಶಾರ್ವರಿ", "ಪ್ಲವ", "ಶುಭಕೃತ್", "ಶೋಭಕೃತ್", "ಕ್ರೋಧಿ", "ವಿಶ್ವವಸು", "ಪರಾಭವ",
				"ಪ್ಲವಂಗ","ಕೀಲಕ","ಸೌಮ್ಯ","ಸಾಧಾರಣ","ವಿರೋಧಿಕೃತ್","ಪರಿಧಾವಿ","ಪ್ರಮಾಧೀಚ","ಆನಂದ","ರಾಕ್ಷಸ","ನಲ",
				"ಪಿಂಗಲ","ಕಾರ್ತೀಕ","ಸಿದ್ಧಾರ್ಥಿ","ರೌದ್ರಿ","ದುರ್ಮತಿ","ದುಂದುಭಿ","ರುಧಿರೋದ್ಗಾರೀ","ರಕ್ತಾಕ್ಷಿ","ಕ್ರೋಧನ","ಅಕ್ಷಯ"
	      };

	      //0 base
	       public final String[] ritus = { "ವಸಂತ", "ಗ್ರೀಷ್ಮ", "ವರ್ಷ", "ಶರದ್", "ಹೇಮಂತ", "ಶಿಶಿರ" };

	       public final String[] karanas = { "NA", "ಕಿಂಸ್ತುಘ್ನ", "ಭವ", "ಬಾಳವ", "ಕೌಳವ", "ತೈತುಲ", "ಗರಜ", "ವಾಣಿಜ", "ವಿಷ್ಟಿ (ಭದ್ರ)", "ಭವ", "ಬಾಳವ", "ಕೌಳವ", "ತೈತುಲ", "ಗರಜ", "ವಾಣಿಜ", "ವಿಷ್ಟಿ (ಭದ್ರ)", "ಭವ", "ಬಾಳವ", "ಕೌಳವ", "ತೈತುಲ", "ಗರಜ", "ವಾಣಿಜ", "ವಿಷ್ಟಿ (ಭದ್ರ)", "ಭವ", "ಬಾಳವ", "ಕೌಳವ", "ತೈತುಲ", "ಗರಜ", "ವಾಣಿಜ", "ವಿಷ್ಟಿ (ಭದ್ರ)", "ಭವ", "ಬಾಳವ", "ಕೌಳವ", "ತೈತುಲ", "ಗರಜ", "ವಾಣಿಜ", "ವಿಷ್ಟಿ (ಭದ್ರ)", "ಭವ", "ಬಾಳವ", "ಕೌಳವ", "ತೈತುಲ", "ಗರಜ", "ವಾಣಿಜ", "ವಿಷ್ಟಿ (ಭದ್ರ)", "ಭವ", "ಬಾಳವ", "ಕೌಳವ", "ತೈತುಲ", "ಗರಜ", "ವಾಣಿಜ", "ವಿಷ್ಟಿ (ಭದ್ರ)", "ಭವ", "ಬಾಳವ", "ಕೌಳವ", "ತೈತುಲ", "ಗರಜ", "ವಾಣಿಜ", "ವಿಷ್ಟಿ (ಭದ್ರ)", "ಶಕುನಿ", "ಚತುಷ್ಪಾದ", "ನಾಗವ" };
	       public final String[] pakshas = { "ಶುಕ್ಲ", "ಕೃಷ್ಣ" };

	      public final String[] ayanas = { "ಉತ್ತರಾಯಣ", "ದಕ್ಷಿಣಾಯಣ" };

	      public final String[] dishas = { "ಪೂರ್ವ ", "ಪಶ್ಚಿಮ ", "ಉತ್ತರ ", "ದಕ್ಷಿಣ " };

	      public final String[] nakshatra = { "NA", "ಅಶ್ವಿನಿ", "ಭರಣಿ", "ಕೃತ್ತಿಕಾ", "ರೋಹಿಣಿ", "ಮೃಗಶಿರ", "ಆರ್ದ್ರ", "ಪುನರ್ವಸು", "ಪುಷ್ಯ", "ಆಶ್ಲೇಷ", "ಮಾಘಾ", "ಪೂರ್ವ ಫಾಲ್ಗುಣಿ", "ಉತ್ತರ ಫಾಲ್ಗುಣಿ", "ಹಸ್ತ", "ಚಿತ್ತ", "ಸ್ವಾತಿ", "ವಿಶಾಖ", "ಅನುರಾಧ", "ಜ್ಯೇಷ್ಟ", "ಮೂಲ", "ಪೂರ್ವಾಷಾಡ", "ಉತ್ತರಾಷಾಡ", "ಶ್ರಾವಣ", "ಧನಿಷ್ಠ", "ಶತಭಿಷ", "ಪೂರ್ವಭಾದ್ರಪದ", "ಉತ್ತರಭಾದ್ರಪದ", "ರೇವತಿ" };

	      public final String[] yoga = {"NA","ವಿಷ್ಕುಂಭ","ಪ್ರೀತಿ","ಆಯುಷ್ಮಾನ್","ಸೌಭಾಗ್ಯ","ಶೋಭನ","ಅತಿಗಂಡ","ಸುಕರ್ಮ","ಧೃತಿ","ಶೂಲ","ಗಂಡ","ವೃದ್ಧಿ","ಧ್ರುವ",
	                                               "ವ್ಯಾಘಾತ","ಹರ್ಷಣ","ವಜ್ರ","ಸಿದ್ಧಿ","ವ್ಯತಾಪತಾ","ವರಿಯಾಣ","ಪರಿಘ","ಶಿವ","ಸಿದ್ದಿ","ಸಾಧ್ಯ","ಶುಭ","ಶುಕ್ಲ","ಬ್ರಹ್ಮ","ಇಂದ್ರ","ವೈಧೃತಿ"};

	      public final String[] tithi = {"NA", "ಪ್ರಥಮ", "ದ್ವಿತೀಯ", "ತೃತೀಯ", "ಚತುರ್ಥಿ", "ಪಂಚಮಿ", "ಷಷ್ಠಿ", "ಸಪ್ತಮಿ","ಅಷ್ಟಮಿ","ನವಮಿ","ದಶಮಿ","ಏಕಾದಶಿ","ದ್ವಾದಶಿ","ತ್ರಯೋದಶಿ","ಚತುರ್ದಶಿ",
	                                                "ಹುಣ್ಣುಮೆ","ಪ್ರಥಮ", "ದ್ವಿತೀಯ", "ತೃತೀಯ", "ಚತುರ್ಥಿ", "ಪಂಚಮಿ", "ಷಷ್ಠಿ", "ಸಪ್ತಮಿ","ಅಷ್ಟಮಿ","ನವಮಿ","ದಶಮಿ","ಏಕಾದಶಿ","ದ್ವಾದಶಿ","ತ್ರಯೋದಶಿ","ಚತುರ್ದಶಿ","ಅಮವಾಸ್ಯೆ" };


	      public final String[] rashi = { "NA", "ಮೇಷ", "ವೃಷಭ", "ಮಿಥುನ", "ಕರ್ಕಾಟಕ", "ಸಿಂಹ", "ಕನ್ಯಾ", "ತುಲಾ", "ವೃಷ್ಚಿಕ", "ಧನು", "ಮಕರ", "ಕುಂಭ", "ಮೀನ" };
	      public final String[] rashi2 = { "NA", "ಮೇಷ", "ವೃಷಭ", "ಮಿಥುನ", "ಕರ್ಕಾಟಕ", "ಸಿಂಹ", "ಕನ್ಯಾ", "ತುಲಾ", "ವೃಷ್ಚಿಕ", "ಧನು", "ಮಕರ", "ಕುಂಭ", "ಮೀನ" };
	      public final String[] rashiNature = { "NA","ಚಾರ್", "ಸ್ಥಿರ", "ದ್ವಿಸ್ವಭಾವ", "ಚಾರ್", "ಸ್ಥಿರ", "ದ್ವಿಸ್ವಭಾವ", "ಚಾರ್", "ಸ್ಥಿರ", "ದ್ವಿಸ್ವಭಾವ", "ಚಾರ್", "ಸ್ಥಿರ", "ದ್ವಿಸ್ವಭಾವ" };
	      public final String[] monthname = {"NA","ಜನವರಿ", "ಫೆಬ್ರವರಿ","ಮಾರ್ಚ್","ಏಪ್ರಿಲ್","ಮೇ","ಜೂನ್","ಜುಲೈ","ಆಗಸ್ಟ್","ಸೆಪ್ಟೆಂಬರ್","ಅಕ್ಟೋಬರ್","ನವೆಂಬರ್","ಡಿಸೆಂಬರ್"};
	      public final String[] monthnameSort = {"NA","Jan", "Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        public final String[] dayname = {"NA", "ಭಾನುವಾರ","ಸೋಮವಾರ", "ಮಂಗಳವಾರ", "ಬುಧವಾರ", "ಗುರುವಾರ", "ಶುಕ್ರವಾರ", "ಶನಿವಾರ" };
        
        public final String[] choghadiaDayName = {"Udveg", "Chal", "Laabh", "Amrut", "Kaal", "Shubh", "Rog"};

		public final String[] choghadiaNightName = {"Shubh", "Amrut", "Chal", "Rog", "Kaal", "Laabh", "Udveg"};
	      
	      public final String[] exstring = { "ಯಾವುದೂ ಇಲ್ಲ", "ಚಂದ್ರೋದಯ ಇಲ್ಲ","ಚಂದ್ರಾಸ್ಥ ಇಲ್ಲ", "(ಅದಿಕ)" };
    }




