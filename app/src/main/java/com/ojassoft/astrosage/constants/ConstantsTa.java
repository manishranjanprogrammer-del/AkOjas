package com.ojassoft.astrosage.constants;

import com.ojassoft.astrosage.jinterface.IConstants;

public class ConstantsTa implements IConstants
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
		
		 public final String[] masas = { "சித்திரை", "வைகாசி", "ஆனி", "ஆடி", "ஆவணி", "புரட்டாசி", "ஐப்பசி", "கார்த்திகை", "மார்கழி", "தை", "மாசி", "பங்குனி" };

	        // base 0
	        public final String[] varas = { "ஞாயிற்றுக்கிழமை", "திங்கட்கிழமை", "செவ்வாய்க்கிழமை", "புதன்கிழமை", "வியாழக்கிழமை", "வெள்ளிக்கிழமை", "சனிக்கிழமை" };
	        
	        public final String[] vaars_swami = { "NA", "சூரியன்", "சந்திரன்", "செவ்வாய்", "புதன்", "வியாழன்/குரு", "வெள்ளி", "சனி" };

	        // base 1
	        public final String[] samvats = {"Na","பிரபவ", "விபவ", "சுக்ல", "பிரமோதூத", "பிரசோற்பத்தி", "ஆங்கீரச", "ஸ்ரீமுக", "பவ", 
				"யுவ", "தாது","ஈஸ்வர", "வெகுதானிய", "பிரமாதி", "விக்கிரம", "விஷு", "சித்திரபானு", 
				"சுபானு", "தாரண", "பார்த்திப", "விய", "சர்வசித்து", "சர்வதாரி", "விரோதி", "விக்ருதி",
				"கர","நந்தன","விஜய", "ஜய", "மன்மத", "துன்முகி", "ஹேவிளம்பி", "விளம்பி",
				"விகாரி", "சார்வரி", "பிலவ", "சுபகிருது", "சோபகிருது", "குரோதி", "விசுவாசுவ", "பரபாவ",
				"பிலவங்க","கீலக","சௌமிய","சாதாரண","விரோதகிருது","பரிதாபி","பிரமாதீச","ஆனந்த","ராட்சச","நள",
				"பிங்கள","காளயுக்தி","சித்தார்த்தி","ரௌத்திரி","துன்மதி","துந்துபி","ருத்ரோத்காரி","ரக்தாட்சி","குரோதன","அட்சய"
	      };

	        //0 base
	        public final String[] ritus = { "இளவேனில்", "முதுவேனில்", "கார்", "குளிர்", "முன்பனி", "பின்பனி" };

	        public final String[] karanas = { "NA", "கிஂஸ்துக்ந", "பவ", "பாலவ", "கௌலவ", "தைதில", "கர", "வணிஜ", "விஷ்டி", "பவ", "பாலவ", "கௌலவ", "தைதில", "கர", "வணிஜ", "விஷ்டி", "பவ", "பாலவ", "கௌலவ", "தைதில", "கர", "வணிஜ", "விஷ்டி", "பவ", "பாலவ", "கௌலவ", "தைதில", "கர", "வணிஜ", "விஷ்டி", "பவ", "பாலவ", "கௌலவ", "தைதில", "கர", "வணிஜ", "விஷ்டி", "பவ", "பாலவ", "கௌலவ", "தைதில", "கர", "வணிஜ", "விஷ்டி", "பவ", "பாலவ", "கௌலவ", "தைதில", "கர", "வணிஜ", "விஷ்டி", "பவ", "பாலவ", "கௌலவ", "தைதில", "கர", "வணிஜ", "விஷ்டி", "ஶகுநி", "சதுஷ்பாத", "நாகவ" };

	        public final String[] pakshas = { "வளர்பிறை", "தேய்பிறை" };

	        public final String[] ayanas = { "உத்தராயணம்", "கதிர்த்திருப்பம்" };

	        public final String[] dishas = { "கிழக்கு ", "மேற்கு ", "வடக்கு ", "தெற்கு " };

	        public final String[] nakshatra = { "NA", "அஸ்வினி", "பரணி", "கார்த்திகை", "ரோஹிணி", "ம்ருகஷீர்ஷம்", "திருவாதிரை", "புனர்பூசம்", "பூசம்", "ஆயில்யம்", "மகம்", "பூரம்", "உத்திரம்", "ஹஸ்தம்", "சித்திரை", "சுவாதி", "விசாகம்", "அனுஷம்", "கேட்டை", "மூலம்", "பூராடம்", "உத்திராடம்", "திருவோணம்", "அவிட்டம்", "சதயம்", "பூரட்டாதி", "உத்திரட்டாதி", "ரேவதி" };

	        public final String[] yoga = {"NA","விஷ்கும்ப","ப்ரீதி","ஆயுஷ்மாந்","ஸௌபாக்ய","ஶோபந","அதிகண்ட","ஸுகர்மா","த்ருதி","ஶூலம்","கண்ட","வ்ருத்தி","த்ருவ",
	                                               "வ்யாகாத","ஹர்ஷண","வஜ்ர","ஸித்தி","வ்யதீபாத","வாரீயந","பரிக","ஶிவ","ஸித்த","ஸாத்ய","ஶுப","ஶுக்ல","ப்ராஹ்ம","ஐந்த்ர","வைத்ருதி"};

	        public final String[] tithi = {"NA", "ப்ரதமா", "த்விதீயா", "த்ருதிய", "சதுர்தீ", "பஞ்சமீ", "ஷஷ்டீ", "ஸப்தமீ","அஷ்டமீ","நவமீ","தஶமீ","ஏகாதஶீ","த்வாதஶீ","த்ரயோதஶீ","சதுர்தஶீ",
	                                                "பூர்ணிமா","ப்ரதமா", "த்விதீயா", "த்ருதிய", "சதுர்தீ", "பஞ்சமீ", "ஷஷ்டீ", "ஸப்தமீ","அஷ்டமீ","நவமீ","தஶமீ","ஏகாதஶீ","த்வாதஶீ","த்ரயோதஶீ","சதுர்தஶீ","அமாவஸ்யா" };

	        public final String[] rashi = { "NA", "மேஷம்	", "வ்ருஷப", "மிதுந", "கடகம்", "ஸிம்ஹ", "கன்னி", "துலா", "வ்ருச்சிகம்", "தனுசு", "மகர", "கும்ப", "மீனம்" };
	        public final String[] rashi2 = { "NA", "மேஷம்	", "வ்ருஷப", "மிதுந", "கடகம்", "ஸிம்ஹ", "கன்னி", "துலா", "வ்ருச்சிகம்", "தனுசு", "மகர", "கும்ப", "மீனம்" };
	        public final String[] rashiNature = { "NA","மோவாபில்", "பிஸ்டு", "டுவள்", "மோவாபில்", "பிஸ்டு", "டுவள்", "மோவாபில்", "பிஸ்டு", "டுவள்", "மோவாபில்", "பிஸ்டு", "டுவள்" };
	        public final String[] monthname = {"NA","ஜனவரி","பிப்ரவரி","மார்ச்","ஏப்ரல்","மே","ஜூன்","ஜூலை","ஆகஸ்ட்","செப்டம்பர்","அக்டோபர்","நவம்பர்","டிசம்பர்"};
	        public final String[] monthnameSort = {"NA","Jan", "Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
	        public final String[] dayname = {"NA","ஞாயிற்று கிழமை", "திங்கட் கிழமை", "செவ்வாய்க் கிழமை", "புதன் கிழமை", "வியாழக் கிழமை", "வெள்ளிக் கிழமை", "சனிக் கிழமை" };
	        
	        public final String[] choghadiaDayName = {"Udveg", "Chal", "Laabh", "Amrut", "Kaal", "Shubh", "Rog"};

			public final String[] choghadiaNightName = {"Shubh", "Amrut", "Chal", "Rog", "Kaal", "Laabh", "Udveg"};
	        
	        public final String[] exstring = { "யாருமிலர்" , "சந்திரௌதயம் இல்லை" ,"சந்திராஸ்தமனம் இல்லை","(அதிக)" };
    }




