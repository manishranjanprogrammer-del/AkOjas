package com.ojassoft.astrosage.constants;

import com.ojassoft.astrosage.jinterface.IConstants;

public class ConstantsMl implements IConstants
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
		
		 public final String[] masas = { "ചിത്തിര", "വിശാഖം", "തൃക്കേട്ട", "ആഷാടം", "ശ്രാവണം", "ഭദ്രപാദം", "അശ്വിനി", "കാർത്തിക", "മാർഘളി", "പുഷ്ണ", "മാഘം", "ഫാൽഘുനം" };

	        // base 0
	      public final String[] varas = { "ഞായറാഴ്ച", "തിങ്കളാഴ്ച", "ചൊവാഴ്ച", "ബുധനാഴ്ച", "വ്യാഴാഴ്ച", "വെളിയാഴ്ച", "ശനിയാഴ്ച" };
	      
	      public final String[] vaars_swami = { "NA", "സൂര്യൻ", "ചന്ദ്രൻ", "കുജൻ", "ബുധൻ", "വ്യാഴം", "ശുക്രൻ", "ശനി" };

	        // base 1
	      public final String[] samvats = {"Na","പ്രഭവ", "വിഭവ", "ശുക്ല", "പ്രമദോത്ത", "പ്രജ്യോത്പതി", "അംഗിര", "ശ്രീമുഖം", "ഭാവ", 
				"യുവ", "ദത്ത്","ഈശ്വര", "ബഹുധാന്യ", "പ്രമതി", "വിക്രമ", "വൃഷ", "ചിത്രഭാനു", 
				"സുഭാനു", "തരണ", "പാർദ്ധിവ", "വ്യായ", "സർവജിത്ത്", "സർവധാരി", "വിരോധി", "വികൃതി",
				"കാര","നന്ദന","വിജയ", "ജയ", "മന്മത", "ദുർമുഖി", "ഹെവിലംബി", "വിലമ്പി",
				"വികാരി", "സർവാണി", "പ്ലവ", "ശുഭാകൃത", "ശോഭാകൃത", "ക്രോധി", "വിശ്വവസു", "പ്രഭവ",
				"പ്ലവംഗം","കിലക","സൗമ്യ","സാധാരണ","വിരോധാകൃത","പ്രിഥ്വി","പ്രമാധി","ആനന്ദ","രാക്ഷസ","നള",
				"പിംഗള","കാലയുക്തി","സിധാർത്തി","രൌദ്രി","ദുർമതി","ദുന്ദുഭി","രുധിരോധാരി","രക്തയക്ഷി","ക്രോധന","അക്ഷയ"
	      };

	        //0 base
	      public final String[] ritus = { "വസന്തം", "ഗ്രീഷ്മം", "വര്‍ഷം", "ശരത്", "ഹേമന്തം", "ശിശിരം" };

	      public final String[] karanas = { "NA", "കിൻസ്റ്റുഘ", "ഭാവ", "ബാലവൻ", "കൗലവ", "റ്റൈറ്റുല", "ഗരാജ", "വാണിജ", "വിഷ്ടി (ഭദ്ര)", "ഭാവ", "ബാലവൻ", "കൗലവ", "റ്റൈറ്റുല", "ഗരാജ", "വാണിജ", "വിഷ്ടി (ഭദ്ര)", "ഭാവ", "ബാലവൻ", "കൗലവ", "റ്റൈറ്റുല", "ഗരാജ", "വാണിജ", "വിഷ്ടി (ഭദ്ര)", "ഭാവ", "ബാലവൻ", "കൗലവ", "റ്റൈറ്റുല", "ഗരാജ", "വാണിജ", "വിഷ്ടി (ഭദ്ര)", "ഭാവ", "ബാലവൻ", "കൗലവ", "റ്റൈറ്റുല", "ഗരാജ", "വാണിജ", "വിഷ്ടി (ഭദ്ര)", "ഭാവ", "ബാലവൻ", "കൗലവ", "റ്റൈറ്റുല", "ഗരാജ", "വാണിജ", "വിഷ്ടി (ഭദ്ര)", "ഭാവ", "ബാലവൻ", "കൗലവ", "റ്റൈറ്റുല", "ഗരാജ", "വാണിജ", "വിഷ്ടി (ഭദ്ര)", "ഭാവ", "ബാലവൻ", "കൗലവ", "റ്റൈറ്റുല", "ഗരാജ", "വാണിജ", "വിഷ്ടി (ഭദ്ര)", "ശകുനി", "ചടുസ്പത", "നാഗവ" };

	      public final String[] pakshas = { "ശുക്ല", "കൃഷ്ണ" };

	      public final String[] ayanas = { "ഉത്തരയനം", "ദക്ഷിണായനം" };

	      public final String[] dishas = { "കിഴക്ക് ", "പടിഞ്ഞാറ്", "വടക്ക് ", "തെക്ക്" };

	      public final String[] nakshatra = { "NA", "അശ്വതി", "ഭരണി", "കാർതിക", "രോഹിണി", "മഗയിരം", "തിരുവാതിര", "പുണർതം", "പൂയം", "ആയില്യം", "മഗം", "പൂരം", "ഉത്രം", "അത്തം", "ചിത്തിര", "ചോതി", "വിശാഖം", "അനിഴം", "തൃക്കേട്ട", "മൂലം", "പൂരം", "ഉത്രാടം", "തിരുവോണം", "അവിടം", "ചതയം", "പൂരോരുടതി", "ഉത്രട്ടാതി", "രേവതി" };

	      public final String[] yoga = {"NA","വിഷ്കംബം","പ്രിതി","ആയുഷ്മാൻ","സൌബാഘ്യ","ശോഭന","അതിഘണ്ട","സുകർമ്മ","ദ്രിതി","ശൂല","ഗന്ദ","വൃദ്ധ","ധ്രുവ",
	                                               "വ്യഗത","ഹർഷണ","വജ്ര","സിദ്ധി","വ്യടാപത","വരിയാൻ","പ്രീഘ","ശിവ","സിദ്ധ","സദ്യ","ശുഭ","ശുക്ല","ബ്രഹ്മ","ഇന്ദ്ര","വൈധ്രിതി"};

	      public final String[] tithi = { "NA","പ്രതിപതം", "ദ്വിതിയ", "ത്രിതിയ", "ചത്രുതി", "പഞ്ചമി", "ഷഷ്ടി", "സപ്തമി","അഷ്ടമി","നവമി","ദശമി","ഏകാദശി","ദ്വാദശി","ത്രയോദശി","ചതുർദശി",
	                                                "പൗർണമി","പ്രതിപതം", "ദ്വിതിയ", "ത്രിതിയ", "ചത്രുതി", "പഞ്ചമി", "ഷഷ്ടി", "സപ്തമി","അഷ്ടമി","നവമി","ദശമി","ഏകാദശി","ദ്വാദശി","ത്രയോദശി","ചതുർദശി","അമാവാസി" };

	       public final String[] rashi = { "NA", "മേടം", "ഇടവം", "മിഥുനം", "കർകിടകം", "ചിങ്ങം", "കന്നി", "തുലാം", "വൃശ്ചികം", "ധനു", "മകരം", "കുംഭം", "മീനം" };
	       public final String[] rashi2 = { "NA", "മേടം", "ഇടവം", "മിഥുനം", "കർകിടകം", "ചിങ്ങം", "കന്നി", "തുലാം", "വൃശ്ചികം", "ധനു", "മകരം", "കുംഭം", "മീനം" };
	       public final String[] rashiNature = { "NA","മൂവബിൾ", "ഫിക്സഡ്", "ഡ്യൂവൽ", "മൂവബിൾ", "ഫിക്സഡ്", "ഡ്യൂവൽ", "മൂവബിൾ", "ഫിക്സഡ്", "ഡ്യൂവൽ", "മൂവബിൾ", "ഫിക്സഡ്", "ഡ്യൂവൽ" };
	       public final String[] monthname = {"NA","ജനുവരി", "ഫബ്രുവരി","മാർച്ച്","ഏപ്രിൽ","മെയ്","ജൂൺ","ജൂലയി","ഓഗസ്റ്റ്","സെപ്റ്റംബർ","ഒക്റ്റോബർ","നവംബർ","ഡിസംബർ"};
	       public final String[] monthnameSort = {"NA","Jan", "Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
	        public final String[] dayname = {"NA", "ഞായറാഴ്ച","തിങ്കള്‍", "ചൊവ്വാഴ്ച", "ബുധന്‍", "വ്യാഴാഴ്ച", "വെള്ളിയാഴ്ച", "ശനിയാഴ്ച" };
	        
	        public final String[] choghadiaDayName = {"Udveg", "Chal", "Laabh", "Amrut", "Kaal", "Shubh", "Rog"};

			public final String[] choghadiaNightName = {"Shubh", "Amrut", "Chal", "Rog", "Kaal", "Laabh", "Udveg"};
	       
	       public final String[] exstring = { "ആരുമില്ല" , "ചന്ദ്രോദയം ഇല്ല","ചന്ദ്രാസ്ട ഇല്ല", "(Adhik)" };
    }




