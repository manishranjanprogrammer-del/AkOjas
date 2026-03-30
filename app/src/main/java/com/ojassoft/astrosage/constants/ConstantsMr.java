package com.ojassoft.astrosage.constants;

import com.ojassoft.astrosage.jinterface.IConstants;

public class ConstantsMr implements IConstants
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
		
		 public final String[] masas = { "चैत्र", "वैशाख", "ज्येष्ठ", "आषाढ", "श्रावण", "भाद्रपद", "आश्विन", "कार्तिक", "मार्गशीर्ष", "पौष", "माघ", "फाल्गुन" };

         // base 0
        public final String[] varas = { "रविवार", "सोमवार", "मंगळवार", "बुधवार", "गुरूवार", "शुक्रवार", "शनिवार" };
        
        public final String[] vaars_swami = { "NA", "सूर्य", "चन्द्र", "मंगल", "बुध", "बृहस्पति", "शुक्र", "शनि" };

        // base 1
        public final String[] samvats = {"Na","प्रभव", "विभव", "शुक्ल", "प्रमोद", "प्रजापति", "अंगिरा", "श्री मुख", "भाव", 
			"युवा", "धाता","ईश्वर", "बहुधान्य", "प्रमाथी", "विक्रम", "वृष", "चित्रभानु", 
			"सुभानु", "तारण", "पार्थिव", "अव्यय", "सर्वजीत", "सर्वधारी", "विरोधी", "विकृति",
			"खर","नंदन","विजय", "जय", "मन्मथ", "दुर्मुख", "हेम्लम्बी", "विलम्बी",
			"विकारी", "शार्वरी", "प्लव", "शुभकृत", "शोभकृत", "क्रोधी", "विश्वावसु", "पराभव",
			"प्ल्वंग","कीलक","सौम्य","साधारण","विरोधकृत","परिधावी","प्रमादी","आनंद","राक्षस","आनल",
			"पिंगल","कालयुक्त","सिद्धार्थी","रौद्र","दुर्मति","दुन्दुभी","रूधिरोद्गारी","रक्ताक्षी","क्रोधन","अक्षय"
      };

        //0 base
        public final String[] ritus = { "वसंत", "ग्रीष्म", "वर्षा", "शरद", "हेमंत", "शिशिर" };

        public final String[] karanas = { "NA", "किन्स्तुघ्ना", "भाव", "बालव", "कौलव", "तैतिल", "गर", "वणिज", "विष्टि", "भाव", "बालव", "कौलव", "तैतुल", "गर", "वणिज", "विष्टि", "भाव", "बालव", "कौलव", "तैतुल", "गर", "वणिज", "विष्टि", "भाव", "बालव", "कौलव", "तैेतिल", "गर", "वणिज", "विष्टि", "भाव", "बालव", "कौलव", "तैतुल", "गर", "वणिज", "विष्टि", "भाव", "बालव", "कौलव", "तैतुल", "गर", "वणिज", "विष्टि", "भाव", "बालव", "कौलव", "तैतुल", "गर", "वणिज", "विष्टि", "भाव", "बालव", "कौलव", "तैतुल", "गर", "वणिज", "विष्टि", "शकुन", "चतुष्पाद", "नागा" };

        public final String[] pakshas = { "शुक्ल", "कृष्ण" };

        public final String[] ayanas = { "उत्तरायण", "दक्षिणायण" };

        public final String[] dishas = { "पूर्व", "पश्चिम", "उत्तर", "दक्षिण" };

        public final String[] nakshatra = { "NA", "अश्विनी", "भरणी", "कृत्तिका", "रोहिणी", "मृगशिरा", "आर्द्रा", "पुनर्वसु", "पुष्य", "आश्लेषा", "माघ", "पूर्व फाल्गुनी", "उत्तरा फाल्गुनी", "हस्त", "चित्रा", "स्वाति", "विशाखा", "अनुराधा", "ज्येष्ठा", "मूळ", "पूर्वाषाढ़ा", "उत्तराषाढ़ा", "श्रवण", "धनिष्ठा", "शतभिष", "पूर्वाभाद्रपद", "उत्तराभाद्रपद", "रेवती" };

        public final String[] yoga = {"NA","विश्कुम्भ","प्रीति","आयुष्मान","सौभाग्य","शोभन","अतिगंड","सुकर्मा","धृति","शूल","गण्ड","वृद्वि","घ्रुव",
                                               "व्याघात","हर्शण","वज्र","सिद्वि","व्यतापता","वरियान","परिघ","शिव","सिद्ध","साघ्य","शुभ","शुक्ल","ब्रह्म","इंद्रा","वैधृति"};

        public final String[] tithi = {"NA", "प्रथम", "द्वितीया", "तृतीया", "चतुर्थी", "पंचमी", "षष्ठी", "सप्तमी","अष्टमी","नवमी","दशमी","एकादशी","द्वादशी","त्रयोदशी","चतुर्दशी",
                                                "पौर्णिमा","प्रथम", "द्वितीया", "तृतीया", "चतुर्थी", "पंचमी", "षष्ठी", "सप्तमी","अष्टमी","नवमी","दशमी","एकादशी","द्वादशी","त्रयोदशी","चतुर्दशी","अमावस्या" };

        public final String[] rashi = { "NA", "मेष", "वृषभ", "मिथुन", "कर्क", "सिंह", "कन्या", "तुळ", "वृश्चिक", "धनु", "मकर", "कुंभ", "मीन" };
        public final String[] rashi2 = { "NA", "मेष", "वृषभ", "मिथुन", "कर्क", "सिंह", "कन्या", "तुळ", "वृश्चिक", "धनु", "मकर", "कुंभ", "मीन" };
        public final String[] rashiNature = { "NA","चर", "स्थिर", "द्वि-स्वभाव", "चर", "स्थिर", "द्वि-स्वभाव", "चर", "स्थिर", "द्वि-स्वभाव", "चर", "स्थिर", "द्वि-स्वभाव" };
        public final String[] monthname = {"NA","जानेवारी", "फेब्रुवारी","मार्च","एप्रिल","मे","जून","जुलै","ऑगस्ट","सप्टेंबर","ऑक्टोबर","नोव्हेंबर","डिसेंबर"};
        public final String[] monthnameSort = {"NA","जानेवारी", "फेब्रुवारी","मार्च","एप्रिल","मे","जून","जुलै","ऑगस्ट","सप्टेंबर","ऑक्टोबर","नोव्हेंबर","डिसेंबर"};
        public final String[] dayname = {"NA","रविवार", "सोमवार", "मंगळवार", "बुधवार", "गुरुवार", "शुक्रवार", "शनिवार" };

        public final String[] choghadiaDayName = {"उद्वेग", "चल", "लाभ", "अमृत", "काळ", "शुभ", "रोग"};

		public final String[] choghadiaNightName = {"शुभ", "अमृत", "चल", "रोग", "काळ", "लाभ", "उद्वेग"};
        
        public final String[] exstring = { "नाही", "चंद्रोदय नाही","चंद्रोस्त नहीं", "(अधिक)" };
    }