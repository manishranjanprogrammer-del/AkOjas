package com.ojassoft.astrosage.constants;

import com.ojassoft.astrosage.jinterface.IConstants;

public class ConstantsGu implements IConstants
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
		public String getRashi(int index) {
			// TODO Auto-generated method stub
			return rashi2[index];
		}
		
		@Override
		public String getLagnaNature(int index) {
			return rashiNature[index];
		}
	  
		public final String[] masas = { "ચૈત્ર", "વૈશાખ", "જયેષ્ઠ (જેઠ)", "અષાઢ", "શ્રાવણ", "ભાદ્રપદ (ભાદરવો)", "આશ્વિન (આસો)", "કાર્તિક (કારતક)", "માર્ગશીર્ષ (માગશર)", "પોષ", "માઘ (મહા)", "ફાલ્ગુન (ફાગણ)" };

        // base 0
        public final String[] varas = { "રવિવાર", "સોમવાર", "મંગળવાર", "બુધવાર", "ગુરુવાર", "શુક્રવાર", "શનિવાર" };
        
        public final String[] vaars_swami = { "NA", "સૂર્ય", "ચંદ્ર", "મંગળ", "બુધ", "ગુરુ", "શુક્ર", "શનિ" };

        // base 1
        public final String[] samvats = {"Na","પ્રભવ", "વિભવ", "શુક્લ", "પ્રમોદ", "પ્રજાપતિ", "અંગિરા", "શ્રીમુખ", "ભાવ", 
			"યુવા", "ધાતા","ઈશ્વર", "બહુધાન્ય", "પ્રમાથી", "વિક્રમ", "વૃષભ", "ચિત્રભાનુ", 
			"સુભાનુ", "તારણ", "પાર્થિવ", "વ્યય", "સર્વજીત", "સર્વધારી", "વિરોધી", "વિકૃતિ",
			"ખર","નંદન","વિજય", "જય", "મન્મથ", "દુર્મુખ", "હેવિલંબી", "વિલંબી",
			"વિકારી", "શાર્વરી", "પ્લવ", "શુભકૃત", "શોભકૃત", "ક્રોધી", "વિશ્વાવસુ", "પરાભવ",
			"પ્લવંગ","કિલક","સૌમ્ય","સાધારણ","વિરોધીકૃત","પરિધાવી","પ્રમાદી","આનંદ","રાક્ષસ","નલ",
			"પિંગળ","કાળયુક્ત","સિદ્ધાર્થી","રૌદ્ર","દુર્મતિ","દુંદુભી","રુધિરોદ્ગારી","રક્તાક્ષી","ક્રોધન","અક્ષય"
      };

        //0 base
        public final String[] ritus = { "વસંત", "ગ્રીષ્મ", "વર્ષા", "શરદ", "હેમંત", "શિશિર" };

        public final String[] karanas = { "NA", "કિન્સ્તુઘ્ના", "ભાવ", "બાલવ", "કૌલવ", "તૈતુલ", "ગરજ", "વાણિજ", "વિષ્ટિ ભદ્ર", "ભાવ", "બાલવ", "કૌલવ", "તૈતુલ", "ગરજ", "વાણિજ", "વિષ્ટિ ભદ્ર", "ભાવ", "બાલવ", "કૌલવ", "તૈતુલ", "ગરજ", "વાણિજ", "વિષ્ટિ ભદ્ર", "ભાવ", "બાલવ", "કૌલવ", "તૈતુલ", "ગરજ", "વાણિજ", "વિષ્ટિ ભદ્ર", "ભાવ", "બાલવ", "કૌલવ", "તૈતુલ", "ગરજ", "વાણિજ", "વિષ્ટિ ભદ્ર", "ભાવ", "બાલવ", "કૌલવ", "તૈતુલ", "ગરજ", "વાણિજ", "વિષ્ટિ ભદ્ર", "ભાવ", "બાલવ", "કૌલવ", "તૈતુલ", "ગરજ", "વાણિજ", "વિષ્ટિ ભદ્ર", "ભાવ", "બાલવ", "કૌલવ", "તૈતુલ", "ગરજ", "વાણિજ", "વિષ્ટિ ભદ્ર", "શકુની", "ચતુષ્પદા", "નાગવ" };

        public final String[] pakshas = { "શુક્લ", "કૃષ્ણ" };

        public final String[] ayanas = { "ઉત્તરાયણ", "દક્ષિણાયન" };

        public final String[] dishas = { "પૂર્વ ", "પશ્ચિમ ", "ઉત્તર ", "દક્ષિણ " };

        public final String[] nakshatra = { "NA", "અશ્વિની", "ભરણી", "કૃતિકા", "રોહિણી", "મૃગશીર્ષા", "આર્દ્રા", "પુનર્વસુ", "પુષ્ય", "આશ્લેષા", "માઘ", "પૂર્વ ફાલ્ગુની", "ઉત્તર ફાલ્ગુની", "હસ્ત", "ચિત્રા", "સ્વાતિ", "વિશાખા", "અનુરાધા", "જ્યેષ્ઠા", "મૂળ", "પૂર્વાષાઢા", "ઉત્તરાષાઢા", "શ્રાવણ", "ધનિષ્ઠા", "શતભિષ", "પૂર્વભાદ્રપદ", "ઉત્તરભાદ્રપદ", "રેવતી" };

        public final String[] yoga = {"NA","વિશ્કુમ્ભ","પ્રીતિ","આયુષ્માન","સૌભાગ્ય","શોભન","અતિગંડ","સુકર્મા","ધૃતિ","શૂળ","ગંડ","વૃદ્ધિ","ધ્રુવ",
                                               "વ્યાઘાત","હર્ષણ","વજ્ર","સિદ્ધિ","વ્યતાપતા","વરિયાન","પરિઘ","શિવ","સિદ્ધ","સાધ્ય","શુભ","શુક્લ","બ્રહ્મ","ઇન્દ્ર","વૈધૃતિ"};

        public final String[] tithi = { "NA","પ્રથમા (એકમ)", "દ્વિતિયા (બીજ)", "તૃતીયા (ત્રીજ)", "ચતુર્થી (ચોથ)", "પંચમી (પાંચમ)", "ષષ્ટિ (છઠ્ઠ)", "સપ્તમી (સાતમ)","અષ્ટમી (આઠમ)","નવમી (નોમ)","દશમી (દશમ)","એકાદશી (અગિયારસ)","દ્વાદશી (બારસ)","ત્રયોદશી (તેરસ)","ચતુર્દશી (ચૌદસ)",
                                                "પૂર્ણિમા (પૂનમ)","પ્રથમા (એકમ)", "દ્વિતિયા (બીજ)", "તૃતીયા (ત્રીજ)", "ચતુર્થી (ચોથ)", "પંચમી (પાંચમ)", "ષષ્ટિ (છઠ્ઠ)", "સપ્તમી (સાતમ)","અષ્ટમી (આઠમ)","નવમી (નોમ)","દશમી (દશમ)","એકાદશી (અગિયારસ)","દ્વાદશી (બારસ)","ત્રયોદશી (તેરસ)","ચતુર્દશી (ચૌદસ)","અમાવાસ્યા (અમાસ)" };

        public final String[] rashi = { "NA", "મેશ", "વૃષભ", "મિથુન", "કર્ક", "સિંહ", "કન્યા", "તુલા", "વૃશ્ચિક", "ધનુ", "મકર", "કુંભ", "મીન" };
        public final String[] rashi2 = { "NA", "મેશ", "વૃષભ", "મિથુન", "કર્ક", "સિંહ", "કન્યા", "તુલા", "વૃશ્ચિક", "ધનુ", "મકર", "કુંભ", "મીન" };
        public final String[] rashiNature = { "NA","ચર", "સ્થિર", "દ્વિસ્વભાવ", "ચર", "સ્થિર", "દ્વિસ્વભાવ", "ચર", "સ્થિર", "દ્વિસ્વભાવ", "ચર", "સ્થિર", "દ્વિસ્વભાવ" };
        public final String[] monthname = {"NA","જાન્યુઆરી", "ફેબ્રુઆરી","માર્ચ","એપ્રિલ","મે","જૂન","જુલાઈ","ઑગસ્ટ","સપ્ટેમ્બર","ઑક્ટોબર","નવેમ્બર","ડિસેમ્બર"};
        public final String[] monthnameSort = {"NA","Jan", "Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        public final String[] dayname = {"NA", "રવિવાર","સોમવાર", "મંગળવાર", "બુધવાર", "ગુરૂવાર", "શુક્રવાર", "શનિવાર" };
        
        public final String[] choghadiaDayName = {"Udveg", "Chal", "Laabh", "Amrut", "Kaal", "Shubh", "Rog"};

		public final String[] choghadiaNightName = {"Shubh", "Amrut", "Chal", "Rog", "Kaal", "Laabh", "Udveg"};
        
        public final String[] exstring = { "કોઈ નહીં" , "ચંદ્રોદય નહીં","ચંદ્રાસ્ત નહીં","(અધિક)" };
    }




