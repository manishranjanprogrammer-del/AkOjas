package com.ojassoft.astrosage.constants;

import com.ojassoft.astrosage.jinterface.IConstants;

public class ConstantsTe implements IConstants
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
		
		 public final String[] masas = { "చైత్రం", "వైశాఖం", "జ్యేష్ఠం", "ఆషాఢం", "శ్రావణం", "భాద్రపదం", "ఆశ్వయుజం", "కార్తీకం", "మార్గశిరం", "పుష్యం", "మాఘం", "ఫల్గుణం" };

	        // base 0
	      public final String[] varas = { "ఆదివారము", "సోమవారము", "మంగళవారము", "బుధవారము", "గురువారము", "శుక్రవారము", "శనివారము" };
	      
	      public final String[] vaars_swami = { "NA", "సూర్యుడు", "చంద్రుడు", "కుజుడు", "బుధుడు", "గురువు", "శుక్రుడు", "శని" };

	        // base 1
	        public final String[] samvats = {"Na","ప్రభవ", "విభవ", "శుక్ల", "ప్రమోద", "ప్రజాపతి", "ఆఙ్గిరస", "శ్రీముఖ", "భావ", 
				"యువ", "ధాత్రీ","ఈశ్వర", "బహుధాన్య", "ప్రమాధి", "విక్రమ", "వృష", "చిత్రభాను", 
				"స్వభాను", "తారణ", "పార్థివ", "వ్యయ", "సర్వజిత", "సర్వధారీ", "విరోధీ", "వికృతి",
				"ఖర","నన్దన","విజయ", "జయ", "మన్మథ", "దుర్ముఖీ", "హేవిలమ్బీ", "విలమ్బీ",
				"వికారీ", "శార్వరీ", "ప్లవ", "శుభకృత్", "శోభకృత్", "క్రోధీ", "విశ్వావసు", "పరాభవ",
				"ప్లవఙ్గ","కీలక","సౌమ్య","సాధారణ","విరోధికృతి","పరిధావీ","ప్రమాదీచ","ఆనన్ద","రాక్షస","నల",
				"పిఙ్గల","కలాయుక్తి","సిద్ధార్థీ","రౌద్ర","దుర్మతి","దున్దుభి","రుధిరోద్గారీ","రక్తాక్షీ","క్రోధన","అక్షయ"
	      };

	        //0 base
	       public final String[] ritus = { "వసంత", "గ్రీష్మ", "వర్ష", "శరద్", "హేమంత", "శిశిర" };

	       public final String[] karanas = { "NA", "కింస్తుఘ్న", "బవ", "బాలవ", "కౌలవ", "తైతిల", "గర", "వణిజ", "విష్టి", "బవ", "బాలవ", "కౌలవ", "తైతిల", "గర", "వణిజ", "విష్టి", "బవ", "బాలవ", "కౌలవ", "తైతిల", "గర", "వణిజ", "విష్టి", "బవ", "బాలవ", "కౌలవ", "తైతిల", "గర", "వణిజ", "విష్టి", "బవ", "బాలవ", "కౌలవ", "తైతిల", "గర", "వణిజ", "విష్టి", "బవ", "బాలవ", "కౌలవ", "తైతిల", "గర", "వణిజ", "విష్టి", "బవ", "బాలవ", "కౌలవ", "తైతిల", "గర", "వణిజ", "విష్టి", "బవ", "బాలవ", "కౌలవ", "తైతిల", "గర", "వణిజ", "విష్టి", "శకుని", "చతుష్పాద", "నాగవ" };      

	         public final String[] pakshas = { "శుక్ల", "కృష్ణ" };

	         public final String[] ayanas = { "ఉత్తరాయణం", "దక్షిణాయనం" };

	         public final String[] dishas = { "తూర్పు", "పడమర", "ఉత్తరం", "దక్షిణం" };

	         public final String[] nakshatra = { "NA", "అశ్వని", "భరణి", "కృతిక", "రోహిణి", "మృగశిర", "ఆరుద్ర", "పునర్వసు", "పుష్యమి", "ఆశ్లేష", "మాఘ", "పూర్వఫల్గుణి", "ఉత్తరఫల్గుణి", "హస్త", "చిత్ర", "స్వాతి", "విశాఖ", "అనూరాధ", "జ్యేష్ఠ", "మూల", "పూర్వాషాఢ", "ఉత్తరాషాఢ", "శ్రావణ", "ధనిశ్ఠ", "శతభిష", "పూర్వభాద్ర", "ఉత్తరాభాద్ర", "రేవతి" };

	         public final String[] yoga = {"NA","విష్కుమ్భ","ప్రీతి","ఆయుష్మాన్","సౌభాగ్య","శోభన","అతిగణ్డ","సుకర్మా","ధృతి","శూల","గణ్డ","వృద్ధి","ధ్రువ",
	                                               "వ్యాఘాత","హర్షణ","వజ్ర","సిద్ధి","వ్యతీపాత","వారీయన","పరిఘ","శివ","సిద్ధ","సాధ్య","శుభ","శుక్ల","బ్రహ్మ","ఐన్ద్ర","వైధృతి"};

	         public final String[] tithi = { "NA","ప్రథమ", "ద్వితీయ", "తృతీయ", "చతుర్ధి", "పంచమి", "షష్టి", "సప్తమి","అష్టమి","నవమి","దశమి","ఏకాదశి","ద్వాదశి","త్రయోదశి","చతుర్దశి",
	                                                "పూర్ణిమ","ప్రథమ", "ద్వితీయ", "తృతీయ", "చతుర్ధి", "పంచమి", "షష్టి", "సప్తమి","అష్టమి","నవమి","దశమి","ఏకాదశి","ద్వాదశి","త్రయోదశి","చతుర్దశి","అమావస్య" };

	         public final String[] rashi = { "NA", "మేష", "వృషభ", "మిథున", "కర్కటక", "సింహ", "కన్య", "తుల", "వృశ్చిక", "ధనుః", "మకర", "కుమ్భ", "మీన" };
	         public final String[] rashi2 = { "NA", "మేష", "వృషభ", "మిథున", "కర్కటక", "సింహ", "కన్య", "తుల", "వృశ్చిక", "ధనుః", "మకర", "కుమ్భ", "మీన" };
	         public final String[] rashiNature = { "NA","చర్", "స్థిర్", "ద్విస్వభావం", "చర్", "స్థిర్", "ద్విస్వభావం", "చర్", "స్థిర్", "ద్విస్వభావం", "చర్", "స్థిర్", "ద్విస్వభావం" };
	         public final String[] monthname = {"NA","జనవరి","ఫిబ్రవరి","మార్చి","ఏప్రిల్","మే","జూన్","జూ","ఆగస్టు","సెప్టెంబర్","అక్టోబర్","నవంబర్","డిసెంబర్"};
	         public final String[] monthnameSort = {"NA","Jan", "Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
	         public final String[] dayname = {"NA","ఆదివారం", "సోమవారం", "మంగళవారం", "బుధవారం", "గురువారం", "శుక్రవారం", "శనివారం" };
	         
	         public final String[] choghadiaDayName = {"Udveg", "Chal", "Laabh", "Amrut", "Kaal", "Shubh", "Rog"};

	 		public final String[] choghadiaNightName = {"Shubh", "Amrut", "Chal", "Rog", "Kaal", "Laabh", "Udveg"};
	         
	         public final String[] exstring = { "ఏదీకాదు", "కాదు చంద్రోదయం","కాదు చంద్రాస్తమయం", "(అధిక)" };
    }