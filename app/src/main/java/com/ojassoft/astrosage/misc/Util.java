package com.ojassoft.astrosage.misc;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*import java.time.LocalDate;
import java.time.Month;*/

/*
import javax.servlet.http.HttpServletRequest;

import com.cslsoftware.desktophoro.DesktopHoro;
import com.google.appengine.api.memcache.MemcacheService;
import com.ojassoft.astrosagenow.AllYog;
import com.ojassoft.astrosagenow.localization.MyProperties;
import com.ojassoft.astrosagenow.localization.MyResourceBundle;
*/

public class Util {
	
	static String[]  shortSignArray = {"ari","tau","gem","can","leo","vig","lib","sco","sag","cap","aqua","pis","sun","moon","mar","mer","jup","ven","sat","sun","mon","tue","wed","thu","fri","saturday","merc","mars","aqu","vir"};
	static String[] longSignArray = {"Aries","Taurus","Gemini","Cancer","Leo","Virgo","Libra","Scorpio","Sagittarius","Capricorn","Aquarius","Pisces","Sun","Moon","Mars","Mercury","Jupiter","Venus","Saturn","Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Mercury","Mars","Aquarius","Virgo"};
	/* This arrays(shortPlanetArray,longPlanetArray) is being used in getActualSenteceEnPlanets method */
	static String[]  shortPlanetArray = {"sun","moon","mars","merc","jup","ven","sat","rah","ket"};
	static String[]  longPlanetArray = {"Sun","Moon","Mars","Mercury","Jupiter","Venus","Saturn","Rahu","Ketu"};
	public static final int LAGNA = 0;
	public static final int SUN = 1;
	public static final int MOON = 2;
	public static final int MARS = 3;
	public static final int MERCURY = 4;
	public static final int JUPITER = 5;
	public static final int VENUS = 6;
	public static final int SATURN = 7;
	public static final int RAHU = 8;
	public static final int KETU = 9;
	
	
	public static final int ARIES = 1;
	public static final int TAURUS = 2;
	public static final int GEMINI = 3;
	public static final int CANCER = 4;
	public static final int LEO = 5;
	public static final int VIRGO = 6;
	public static final int LIBRA = 7;
	public static final int SCORPIO = 8;
	public static final int SAGITTARIUS = 9;
	public static final int CAPRICORN = 10;
	public static final int AQURIUS = 11;
	public static final int PISCES = 12;
	
	public static final int SELF = 4;
	public static final int FRIEND = 3;
	public static final int NEUTRAL = 2;
	public static final int ENEMY = 1;
	
	int signs;
	int varga[][];

	public void initialize(int signArray) {
		signs = signArray; // Sun at 1, Lagna at 0

	}

	/*public static int getRashiNo(String rashiName, int langCode)
	{
		int no = -1;
		for (int i = 0; i <Common.engRashiNames.length; i++)
		{
			if(Common.engRashiNames[i].equalsIgnoreCase(rashiName))
			{
				no = i;
			}
		}
		return no;
	}*/
	
	public int[] getPositionForShodasvarg(int position) {
		int shodaspoint[] = new int[13];
		for (int i = 0; i < 13; i++)
			shodaspoint[i] = this.varga[i][position];
		return shodaspoint;
	}
	public void setVarga(int varga[][]) {
		this.varga = varga;
	}
	public static int arePlanetsFriend(int planet1, int planet2) {
		if (planet1 == planet2)
			return SELF;
		switch (planet1) {
		case SUN:
			if (planet2==MOON || planet2==MARS || planet2==JUPITER)
				return FRIEND;
			if (planet2==SATURN || planet2==VENUS)
				return ENEMY;
			if (planet2==MERCURY)
				return NEUTRAL;
			break;

		case MOON:
			if (planet2==SUN || planet2==MERCURY)
				return FRIEND;
			else 
				return NEUTRAL;
		case MARS:
			if (planet2==MOON || planet2==SUN || planet2==JUPITER)
				return FRIEND;
			if (planet2==MERCURY)
				return ENEMY;
			return NEUTRAL;
		case MERCURY:
			if (planet2==SUN || planet2==VENUS)
				return FRIEND;
			if (planet2==MOON || planet2==MARS || planet2==JUPITER)
				return ENEMY;
			if (planet2==VENUS || planet2==SATURN)
				return NEUTRAL;
			break;

		case JUPITER:
			if (planet2==MOON || planet2==MARS || planet2==SUN)
				return FRIEND;
			if (planet2==MERCURY || planet2==VENUS)
				return ENEMY;
			if (planet2==SATURN)
				return NEUTRAL;
			break;

		case VENUS:
			if (planet2==SATURN || planet2==MERCURY)
				return FRIEND;
			if (planet2==JUPITER || planet2 == MARS)
				return NEUTRAL;
			return ENEMY;
		case SATURN:
			if (planet2==MERCURY || planet2==VENUS)
				return FRIEND;
			if (planet2==JUPITER)
				return NEUTRAL;
			return ENEMY;
		case RAHU:
		case KETU:
			if (planet2==VENUS || planet2==SATURN)
				return FRIEND;
			if (planet2==SUN || planet2==MOON || planet2 == MARS)
				return ENEMY;
			if (planet2==JUPITER || planet2==MERCURY)
				return ENEMY;
			
		}
		
		
		
		return NEUTRAL;
	}
	
	
	
	public static boolean isMalefic(int planet){
		if (planet == SUN || planet == MARS || planet == SATURN || planet == RAHU || planet == KETU)
			return true;
		else 
			return false;
	}

	public static boolean isBenefic(int planet){
		if (planet == MOON || planet == MERCURY || planet == VENUS || planet == JUPITER)
			return true;
		else 
			return false;
	}
	
	/*public static boolean isMobileDevice(HttpServletRequest request) {
		String ua = request.getHeader("User-Agent");
		if (ua != null) {
			// Comment by ambuj on 21/07/2016
			// ua=ua.toLowerCase();
			// if ((ua.indexOf("mobile") != -1) ||
			// (ua.matches("(?i).*((android|bb\\d+|meego).+mobile|avantgo|bada\\/|blackberry|playbook|blazer|compal|elaine|ipad|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge
			// |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm(
			// os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\\.(browser|link)|vodafone|wap|windows
			// ce|xda|xiino).*"))
			// || (ua.length() >=4 && ua.substring(0, 4)
			// .matches(
			// "(?i)1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a
			// wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r
			// |s
			// )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1
			// u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp(
			// i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac(
			// |\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt(
			// |\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg(
			// g|\\/(k|l|u)|50|54|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-|
			// |o|v)|zz)|mt(50|p1|v
			// )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v
			// )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-|
			// )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\\-|your|zeto|zte\\-")))
			if (ua.matches(
					"(?i).*(up.browser|up.link|mmp|symbian|smartphone|midp|wap|phone|windows ce|pda|mobile|mini|palm|ipad|Android|BlackBerry|PlayBook|iPhone|iPod|Palm|Symbian).*")) {
				return true;
			} else
				return false;
		} else
			return false;
	}*/

	public static String safestr(String theval) {
		String returnVal = "";
		if (theval != null) {
			if (theval.indexOf("cast(") > 0 || theval.indexOf("char(") > 0) {
				returnVal = "gotoastrocamp";
			} else {
				returnVal = theval.replace("'", "''");
			}
		}
		return returnVal;
	}

	/*public static String getValuesFromDictionary(String key, int langCode) {
		String returnVal = key;

		if (langCode != 0) {
			if (key.indexOf(",") > 0) {
				String[] splitedvalues = key.split(",");
				for (int i = 0; i <= splitedvalues.length; i++) {
					returnVal = returnVal + splitedvalues[i] + ",";
				}
				returnVal = returnVal.replaceAll(",$", "");
			} else {
				HashMap<String, String> hm = new HashMap<String, String>();
				String[] dictionaryConstants = DictionaryConstants.langConstants[langCode];
				for (int i = 0; i <= 67; i++) {
					String[] keys = DictionaryConstants.EngConstantValues[i].split("ÿ");
					String[] values = dictionaryConstants[i].split("ÿ");
					for (int j = 0; j < keys.length; j++) {
						hm.put(keys[j].trim(), values[j].trim());
					}
				}
				returnVal = hm.get(key);
			}
		}
		return returnVal;
	}*/

	public static String getDate(String dateVal, String prefixYOB, int languageCode) {
		String Spcrmv = dateVal.replace("/ ", "/"); // To replace the space from date from page mahadasha-phala.jsp
		String[] dateEval = Spcrmv.split("/");
		// return
		// fullMonthName(Integer.parseInt(dateEval[1]),languageCode,rbConstants)+"
		// "+dateEval[0]+", "+prefixYOB+dateEval[2];
		return dateEval[0] + "/" + dateEval[1] + "/" + prefixYOB + dateEval[2];
	}
	
	/**
	 * This method return name of Tithis in English and Hindi 
	 * @param recLangCode
	 * @return ArrayList of Tithi name
	 */
	public static ArrayList<String> getNameOfTithi(int [] recMyGhatakTithi, int recLangCode) {
		final String[] tithiNameEng = {"Prathama","Dwitiya","Tritiya","Chaturthi","Panchami","Shashthi","Saptami","Ashtami","Navami","Dasami","Ekadasi","Dvadasi","Trayodasi","Chaturdashi","Amavasya"};
		final String[] tithiNameHi = {"प्रतिपदा","द्वितीया","तृतीया","चतुर्थी","पंचमी","षष्टी","सप्तमी","अष्टमी","नवमी","दशमी","एकादशी","द्वादशी","त्रयोदशी","चतुर्दशी","अमावस्या"};
		ArrayList<String> returnTithiName= new ArrayList<String>();
		for(int i=0;i<recMyGhatakTithi.length;i++){
			if(recLangCode==1){
				returnTithiName.add(tithiNameHi[recMyGhatakTithi[i]-1]);
			}
			else{
				returnTithiName.add(tithiNameEng[recMyGhatakTithi[i]-1]);
		}
		}
		return returnTithiName;
	}

	public static int planetNumber(String planetName) {
		int planetNumber = 0;
		if (planetName.equals("SUN")) {
			planetNumber = 1;
		} else if (planetName.equals("lw;")) {
			planetNumber = 1;
		} else if (planetName.equals("MON")) {
			planetNumber = 2;
		} else if (planetName.equals("pan")) {
			planetNumber = 2;
		} else if (planetName.equals("MAR")) {
			planetNumber = 3;
		} else if (planetName.equals("eax")) {
			planetNumber = 3;
		} else if (planetName.equals("MER")) {
			planetNumber = 4;
		} else if (planetName.equals("cq/")) {
			planetNumber = 4;
		} else if (planetName.equals("JUP")) {
			planetNumber = 5;
		} else if (planetName.equals("xq:")) {
			planetNumber = 5;
		} else if (planetName.equals("VEN")) {
			planetNumber = 6;
		} else if (planetName.equals("'kq")) {
			planetNumber = 6;
		} else if (planetName.equals("SAT")) {
			planetNumber = 7;
		} else if (planetName.equals("'kf")) {
			planetNumber = 7;
		} else if (planetName.equals("RAH")) {
			planetNumber = 8;
		} else if (planetName.equals("jkg")) {
			planetNumber = 8;
		} else if (planetName.equals("KET")) {
			planetNumber = 9;
		} else if (planetName.equals("dsr")) {
			planetNumber = 9;
		}

		return planetNumber;
	}

	/**
	 * will check the particular word is in sentence or not to identify the question
	 * 
	 * @author swatantra
	 * @param stringList
	 * @param word
	 * @return true/false
	 */
	public static boolean checkForWords(List<String> stringList, String word) {
		boolean boolVal = false;
		for (int i = 0; i < stringList.size(); i++) {
			if (stringList.get(i).contains(word)) {
				boolVal = true;
				break;
			}
		}
		return boolVal;
	}

	/**
	 * will check the array of words in sentence or not to identify the question
	 * @param questions
	 * @param wordList
	 * @return
	 */
	public static boolean isQuestionExist(List<String> questions, String[] wordList) {
		boolean boolVal = true;
		List<String> arrayList = Arrays.asList(wordList);
		for (int i = 0; i < questions.size(); i++) {
			String[] words = questions.get(i).split(" ");
			for (int j = 0; j < words.length; j++) {
				if (!arrayList.contains(words[j].trim())) {
					boolVal = false;
					break;
				}
			}
			if (boolVal) {
				break;
			}
		}
		return boolVal;
	}

	/*
	 * public String fullMonthName(int MonthN , int languageCode,MyProperties
	 * rbConstants){ String monthName = ""; if(languageCode == 1){
	 * 
	 * switch(MonthN){ case 1: monthName = "जनवरी"; break; case 2: monthName =
	 * "फरवरी"; break; case 3: monthName = "मार्च"; break; case 4: monthName =
	 * "अप्रैल"; break; case 5: monthName = "मई"; break; case 6: monthName = "जून";
	 * break; case 7: monthName = "जुलाई"; break; case 8: monthName = "अगस्त";
	 * break; case 9: monthName = "सितम्बर"; break; case 10: monthName = "अक्टूबर";
	 * break; case 11: monthName = "नवम्बर"; break; case 12: monthName = "दिसम्बर" ;
	 * } return monthName ;
	 * 
	 * }else{
	 * 
	 * switch(MonthN){ case 1: monthName = rbConstants.getString("JANUARY"); break;
	 * case 2: monthName = rbConstants.getString("FEBRUARY"); break; case 3:
	 * monthName = rbConstants.getString("MARCH"); break; case 4: monthName =
	 * rbConstants.getString("APRIL"); break; case 5: monthName =
	 * rbConstants.getString("MAY"); break; case 6: monthName =
	 * rbConstants.getString("JUNE"); break; case 7: monthName =
	 * rbConstants.getString("JULY"); break; case 8: monthName =
	 * rbConstants.getString("AUGUST"); break; case 9: monthName =
	 * rbConstants.getString("SEPTEMBER"); break; case 10: monthName =
	 * rbConstants.getString("OCTOBER"); break; case 11: monthName =
	 * rbConstants.getString("NOVEMBER"); break; case 12: monthName =
	 * rbConstants.getString("DECEMBER"); } return monthName; } }
	 */
	/**
	 * This method converts the input String to UTF-8
	 * 
	 * @param inputUTF
	 * @return String in UTF-8
	 */
	public static String decodeToUTF(String inputUTF) {
		String outISO = null;
		try {
			outISO = new String(inputUTF.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return outISO;
	}

	public static int getNormalSeq(int nakLordinVimSeq) {
		int[] chng = { 9, 6, 1, 2, 3, 8, 5, 7, 4 };
		return chng[nakLordinVimSeq];
	}

	/**
	 * This method checks if the passed in string is null or blank, then it returns 0, else 
	 * it returns the integer value.
	 * @param theval
	 * @return
	 */
	public static int safeInt(String theval) {
		int returnVal = 0;
		if(theval != null && !theval.trim().equals(""))
		{
			returnVal = Integer.parseInt(theval);
		}
		return returnVal;
	}
	
	/**
	 * This method is used to get data including intent from dialogflow.
	 * @return return json data as string
	 */
	/*public static String getDataFromDialogflow(String question,String androidId, int langCode, String authKey) {
		String data ="";
		URL url;
		try {
			//data format
			String dataFormat = "{\n" + 
            		"	\"lang\":\"hi\",\n" + 
            		"	\"query\":\"###\",\n" + 
            		"	\"sessionId\":\"@@@\"\n" + 
            		"}";
			
			dataFormat = dataFormat.replace("@@@", androidId).replace("###",question);
			
		    url = new URL(ConstantValues.dialogflowUrl);
		    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		    // Set Headers
		    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
		    conn.setRequestProperty("Authorization", "Bearer "+authKey);
		    
		    conn.setRequestMethod("POST");
		    conn.setDoOutput(true);
            conn.setDoInput(true);
            
            OutputStream os = conn.getOutputStream();
            os.write(dataFormat.getBytes("UTF-8"));
            os.close();

		    // Read response
		    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		    StringBuffer jsonString = new StringBuffer();
		    String line;
		    while ((line = br.readLine()) != null) {
		        jsonString.append(line);
		    }
		    data = jsonString.toString();
		    br.close();
		    conn.disconnect();
		} catch (IOException e) {
		    e.printStackTrace();
		    data = "{\r\n" + 
		    		"	\"id\": \"\",\r\n" + 
		    		"	\"timestamp\": \"\",\r\n" + 
		    		"	\"lang\": \"hi\",\r\n" + 
		    		"	\"result\": {\r\n" + 
		    		"		\"source\": \"agent\",\r\n" + 
		    		"		\"resolvedQuery\": \""+question+"\",\r\n" + 
		    		"		\"action\": \"input.unknown\",\r\n" + 
		    		"		\"actionIncomplete\": false,\r\n" + 
		    		"		\"parameters\": {},\r\n" + 
		    		"		\"contexts\": [],\r\n" + 
		    		"		\"metadata\": {\r\n" + 
		    		"			\"intentId\": \"f54b1167-ad8e-4496-aa2c-49c59a95fe04\",\r\n" + 
		    		"			\"webhookUsed\": \"false\",\r\n" + 
		    		"			\"webhookForSlotFillingUsed\": \"false\",\r\n" + 
		    		"			\"isFallbackIntent\": \"true\",\r\n" + 
		    		"			\"intentName\": \"Default Fallback Intent\"\r\n" + 
		    		"		},\r\n" + 
		    		"		\"fulfillment\": {\r\n" + 
		    		"			\"speech\": \"आपने क्या कहा, मुझे समझ नहीं आया. फिर से कहें?\",\r\n" + 
		    		"			\"messages\": [{\r\n" + 
		    		"				\"type\": 0,\r\n" + 
		    		"				\"speech\": \"माफ़ करें, मुझे समझ नहीं आया|\"\r\n" + 
		    		"			}]\r\n" + 
		    		"		},\r\n" + 
		    		"		\"score\": 1.0\r\n" + 
		    		"	},\r\n" + 
		    		"	\"status\": {\r\n" + 
		    		"		\"code\": 400,\r\n" + 
		    		"		\"errorType\": \"HardCodedJson\"\r\n" + 
		    		"	},\r\n" + 
		    		"	\"sessionId\": \"12345\"\r\n" + 
		    		"}";
		}
		
		return data;
	}
	*/
	
	public String newConvertNoSuffixHI(int Number){
	String newNumber_HI ="";
    switch(Number){
        case 1:
        	newNumber_HI = "पहले";
        	break;
        case 2:
        newNumber_HI = "दूसरे";
    	break;
        case 3:
        newNumber_HI = "तीसरे";
    	break;
        case 4:
        newNumber_HI = "चौथे";
    	break;
        case 5:
        newNumber_HI = "पांचवें";
    	break;
        case 6:
        newNumber_HI = "छठें";
    	break;
        case 7:
        newNumber_HI = "सातवें";
    	break;
        case 8:
        newNumber_HI = "आठवें";
    	break;
        case 9:
        newNumber_HI = "नौवें";
    	break;
        case 10:
        newNumber_HI = "दसवें";
    	break;
        case 11:
        newNumber_HI = "ग्यारहवें";
    	break;
        case 12:
        newNumber_HI = "बारहवें";
    	break;
    }
     return newNumber_HI;
	}
	
	/**
	 * This method returns the planet name in hindi.
	 * @param planetname
	 * @return String - Planet Name
	 */
	public String convertPlanetNameinHI(String planetname){
		String planetname_HI ="";
		if(planetname.equals("Sun")){
			planetname_HI = "सूर्य";
	    }
		else if(planetname.equals("Moon")){
			planetname_HI = "चन्द्र";
	    }
		else if(planetname.equals("Mars")){
			planetname_HI = "मंगल";
	    }
		else if(planetname.equals("Mercury")){
			planetname_HI = "बुध";
	    }
		else if(planetname.equals("Jupiter")){
			planetname_HI = "गुरु";
	    }
		else if(planetname.equals("Venus")){
			planetname_HI = "शुक्र" ;
	    }
		else if(planetname.equals("Saturn")){
			planetname_HI = "शनि";
	    }
		else if(planetname.equals("Rahu")){
			planetname_HI = "राहु";
	    }
		else if(planetname.equals("Ketu")){
			planetname_HI = "केतु";
	    }
		else if(planetname.equals("Null")){
			planetname_HI = "Null";
	    }
		return planetname_HI;
	}
	
	/**
	 * This method returns the relationship with planet from the properties file.
	 * @return String - relationshipWithPlanet.
	 */
	/*public String relationshipWithPlanet(String relationship,MyResourceBundle rbConstants)
	{
		String relationshipValue ="";
		
		if(relationship.equals("Exalted"))
		{
			relationshipValue = rbConstants.getString("EXALTED_RELATION");
		}
		else if(relationship.equals("Debilitated")){
			relationshipValue = rbConstants.getString("DEBILITATED_RELATION");
		}
		else if(relationship.equals("Own")){
			relationshipValue = rbConstants.getString("OWN_RELATION");
		}
		else if(relationship.equals("Friendly")){
			relationshipValue = rbConstants.getString("FRIENDLY_RELATION");
		}
		else if(relationship.equals("Enemy")){
			relationshipValue = rbConstants.getString("ENEMY_RELATION");
		}
		else if(relationship.equals("Neutral")){
			relationshipValue = rbConstants.getString("NEUTRAL_RELATION");
		}
		else
		{
			relationshipValue = "-";
		}
		return relationshipValue;
	}*/
	

	public String ConvertSignNameInHindi(String rashiname){
		String signName_Hindi="";
		if(rashiname.equals("es\"k\"")){
			signName_Hindi = "मेष";
		}
		else if(rashiname.equals("o`kHk")){
			signName_Hindi = "वृषभ";
		}
		else if(rashiname.equals("feFkqu")){
			signName_Hindi = "मिथुन";
		}
		else if(rashiname.equals("ddZ")){
			signName_Hindi = "कर्क";
		}
		else if(rashiname.equals("flag")){
			signName_Hindi =  "सिंह"  ;
		}
		else if(rashiname.equals("dU;k")){
			signName_Hindi =  "कन्या"  ;
		}
		else if(rashiname.equals("rqyk")){
			signName_Hindi =  "तुला"  ;
		}
		else if(rashiname.equals("o`f'pd")){
			signName_Hindi =  "वृश्चिक"  ;
		}
		else if(rashiname.equals("/kuq")){
			signName_Hindi =  "धनु"  ;
		}
		else if(rashiname.equals("edj")){
			signName_Hindi =  "मकर" ;
		}
		else if(rashiname.equals("dqaHk")){
			signName_Hindi =  "कुंभ" ;
		}
		else if(rashiname.equals("ehu")){
			signName_Hindi =  "मीन";
		}
    
      return signName_Hindi;
	}
	
	
	/*public static ArrayList<Integer> howManyYogaInKundali(DesktopHoro ObjHoro)
	  {
	      double hour = Double.parseDouble(ObjHoro.getHourOfBirth()) ;
	      double min = Double.parseDouble(ObjHoro.getMinuteOfBirth())/60;
	      double sec = Double.parseDouble(ObjHoro.getSecondOfBirth())/3600;
	      double tob = (hour + min + sec);
	      int maleFemaleNumber;
	      int dayornightNumber;
	      String maleFemale=ObjHoro.getMaleFemale();
	      //for male
	      if (maleFemale.toLowerCase().equals("m"))
	      {
	          maleFemaleNumber = 0;
	      }
	          //for female
	      else
	      {
	          maleFemaleNumber = 1;
	      }


	      //for night
	      if (tob > ObjHoro.getSunSetTime() || tob < ObjHoro.getSunRiseTime())
	      {
	          dayornightNumber = 1;

	      }
	//for day
	      else
	      {
	          dayornightNumber = 0;
	      }

	      AllYog allYogObj = new AllYog();


	      allYogObj.setObjectHoro(ObjHoro);

	      ArrayList<Integer>  yogaNumber=new ArrayList<Integer>();
	      if (allYogObj.getRuchakaMahapurushYoga())
	      {
	          yogaNumber.add(1);
	      }
	      if (allYogObj.getMalavyaMahapurushYoga())
	      {
	          yogaNumber.add(2);
	      }
	      if (allYogObj.getBhadraMahapurushYoga())
	      {
	          yogaNumber.add(3);
	      }
	      if (allYogObj.getHamsaMahapurushYoga())
	      {
	          yogaNumber.add(4);
	      }
	      if (allYogObj.getShashMahapurushYoga())
	      {
	          yogaNumber.add(5);
	      }
	      if (allYogObj.getChandradhiYoga())
	      {
	          yogaNumber.add(6);
	      }
	      if (allYogObj.getLagnadhiYoga())
	      {
	          yogaNumber.add(7);
	      }
	      if (allYogObj.getAmlaYoga())
	      {
	          yogaNumber.add(8);
	      }
	      if (allYogObj.getGajKesriYoga())
	      {
	          yogaNumber.add(9);
	      }
	      if (allYogObj.getSunaphaYoga())
	      {
	          yogaNumber.add(10);
	      }
	      if (allYogObj.getAnaphaYoga())
	      {
	          yogaNumber.add(11);
	      }
	      if (allYogObj.getDurdharaYoga())
	      {
	          yogaNumber.add(12);
	      }
	      if (allYogObj.getVeshiYoga())
	      {
	          yogaNumber.add(13);
	      }
	      if (allYogObj.getVoshiYoga())
	      {
	          yogaNumber.add(14);
	      }
	      if (allYogObj.getUbhayachariYoga())
	      {
	          yogaNumber.add(15);
	      }
	      if (allYogObj.getBudhAdityaYoga())
	      {
	          yogaNumber.add(16);
	      }
	      if (allYogObj.getChandraMangalYoga())
	      {
	          yogaNumber.add(17);
	      }

	      if (allYogObj.getMahaBhagyaYoga(maleFemaleNumber, dayornightNumber))
	      {
	          yogaNumber.add(18);
	      }
	      if (allYogObj.getSrikYoga())
	      {
	          yogaNumber.add(19);
	      }
	      if (allYogObj.getSarpaYoga())
	      {
	          yogaNumber.add(20);
	      }
	      if (allYogObj.getShakataYoga())
	      {
	          yogaNumber.add(21);
	      }
	      if (allYogObj.getSaraswatiYoga())
	      {
	          yogaNumber.add(22);
	      }
	      if (allYogObj.getVasumathiYoga())
	      {
	          yogaNumber.add(23);
	      }
	    
	     if (allYogObj.isRajju())
	      {
	          yogaNumber.add(24);
	      }
	      if (allYogObj.isMusala())
	      {
	          yogaNumber.add(25);
	      }
	      if (allYogObj.isNala())
	      {
	          yogaNumber.add(26);
	      }

	      if (allYogObj.isMala())
	      {
	          yogaNumber.add(27);
	      }
	      if (allYogObj.isSarp())
	      {
	          yogaNumber.add(28);
	      }

	      if (allYogObj.isGada())
	      {
	          yogaNumber.add(29);
	      }

	      if (allYogObj.isShakat())
	      {
	          yogaNumber.add(30);
	      }

	      if (allYogObj.isVihaga())
	      {
	          yogaNumber.add(31);
	      }

	      if (allYogObj.isKamal())
	      {
	          yogaNumber.add(34);
	      }

	      if (allYogObj.isVapi())
	      {
	          yogaNumber.add(35);
	      }


	      if (allYogObj.isShringataka())
	      {
	          yogaNumber.add(36);
	      }

	      if (allYogObj.isHala())
	      {
	          yogaNumber.add(37);
	      }

	      if (allYogObj.isYoop())
	      {
	          yogaNumber.add(38);
	      }

	      if (allYogObj.isShakti())
	      {
	          yogaNumber.add(40);
	      }
	      if (allYogObj.isDanda())
	      {
	          yogaNumber.add(41);
	      }

	      if (allYogObj.isNau())
	      {
	          yogaNumber.add(42);
	      }

	      if (allYogObj.isKoota())
	      {
	          yogaNumber.add(43);
	      }

	      if (allYogObj.isChaap())
	      {
	          yogaNumber.add(44);
	      }

	      if (allYogObj.isArdhaChandra()== true)
	      {
	          yogaNumber.add(45);
	      }

	      if (allYogObj.isChakra())
	      {
	          yogaNumber.add(46);
	      }

	      if (allYogObj.isSamudra())
	      {
	          yogaNumber.add(47);
	      }


	      if (allYogObj.isChhatra()== true)
	      {
	          yogaNumber.add(48);
	      }

	      if (allYogObj.getParvata())
	      {
	          yogaNumber.add(49);
	      }
	      if (allYogObj.getKaahala())
	      {
	          yogaNumber.add(50);
	      }
	      if (allYogObj.getAnotherChaamara())
	      {
	          yogaNumber.add(51);
	      }

	      if (allYogObj.getPaapKartariYoga())
	      {
	          yogaNumber.add(52);
	      }

	      if (allYogObj.getShubhKartariYoga())
	      {
	          yogaNumber.add(53);
	      }
	      if (allYogObj.isParashariRajyoga())
	      {
	          yogaNumber.add(54);
	      }
	      if (allYogObj.isVipreetHarsh())
	      {
	          yogaNumber.add(55);
	      }


	      if (allYogObj.isVipreetSaral())
	      {
	          yogaNumber.add(56);
	      }
	      if (allYogObj.isVipreetVimal())
	      {
	          yogaNumber.add(57);
	      }
	     
	      return yogaNumber;
	
}*/
	
	
	

	public int getHouseLord(int houseNo,int rashi) {
		int owner[] = { 3, 6, 4, 2, 1, 4, 6, 3, 5, 7, 7, 5 };
		int rasiInHouse = houseNo + getPlanetsSign(rashi) - 1;
		if (rasiInHouse > 12)
			rasiInHouse -= 12;
		return owner[rasiInHouse - 1];
	}

	public int getPlanetsSign(int planet) {
		return planet;
		// return varga[planet][0];
	}

	public String getHouseLordName(int planetNuber) {
		String lordName = "";
		switch (planetNuber) {
		case 1:
			lordName = "SURYA";
			break;
		case 2:
			lordName = "MOON";
			break;
		case 3:
			lordName = "MARS";
			break;
		case 4:
			lordName = "MERCURY";
			break;
		case 5:
			lordName = "JUPITER";
			break;
		case 6:
			lordName = "VENUS";
			break;
		case 7:
			lordName = "SATURN";
			break;

		default:
			break;
		}

		return lordName;

	}
	
	/**
	 * This method returns the name of the person included in the List of English words.
	 * The assumption is that the sentence is expected to be like :
	 * 1) What is the rashi(moon sign) of Sunita? OR
	 * 2) Tell me the rashi of name Sunita.
	 * So, in this scenario, we give the word 'name' a priority. If this word is found,
	 * we capture the word after it. If not, then we capture the word after 'of'.
	 * So, the method iterates through the list 
	 * @param wordList
	 * @return String
	 */
	public static String getEnglishName(List<String> wordList)
	{
		String returnName = "";
		boolean found = false;
		String tempName = "";
		for(int i=0;i<wordList.size();i++)
		{
			if(wordList.get(i).equals("name"))
			{
				returnName = wordList.get(i+1);
				found = true;
				break;
			}
			if(wordList.get(i).equals("of"))
			{
				tempName = wordList.get(i+1);
			}
		}
		if(!found)
		{
			returnName = tempName;
		}
		
		return returnName;
	}
	
	/**
	 * This method returns the name of the person included in the List of Hindi words.
	 * The assumptions that are made in this method are:
	 * 1) It assumes that the name of the person is before the "नाम".
	 * 2) In case just before the word "नाम", the word is "की" or "के",
	 * then the word before it is returned.
	 * @param wordList
	 * @return String
	 */
	public static String getHindiName(List<String> wordList)
	{
		String returnName = "";
		for(int i=0;i<wordList.size();i++)
		{
			if(wordList.get(i).equals("नाम"))
			{
				returnName = wordList.get(i-1);
				if(returnName.equals("की") || returnName.equals("के"))
				{
					returnName = wordList.get(i-2);
				}
				break;
			}
				
		}
		return returnName;
	}
	
	/**
	 * This method returns the two names which are present before and after the word 'and'/'और'.
	 * The two names are separated by "##".
	 * 
	 * @param wordList
	 * @param langCode
	 * @return String
	 */
	public static String getTwoNames(List<String> wordList, int langCode)
	{
		String name1 = "";
		String name2 = "";
		String lookForWord = "and";
		if(langCode == 1)
		{
			lookForWord = "और";
		}
		for(int i=0;i<wordList.size();i++)
		{
			if(wordList.get(i).equals(lookForWord))
			{
				name1 = wordList.get(i-1);
				name2 = wordList.get(i+1);
				break;
			}
				
		}
		return name1+"##"+name2;
	}
	public static int getMahaDashaPlanetNumber(String planet)
    {
        int planetNumber=0;
        switch(planet.trim())
        {
           case "Sun":
           planetNumber = 1;
           break;

           case "Moon":
           planetNumber = 2;
           break;

           case "Mars":
           planetNumber = 3;
           break;

           case "Mercury":
           planetNumber =4;
           break;

           case "Jupiter":
           planetNumber = 5;
           break;

           case "Venus":
           planetNumber = 6;
           break;

           case "Saturn":
           planetNumber = 7;
           break;

           case "Rahu":
           planetNumber = 8;
           break;

           case "Ketu":
           planetNumber = 9;
           break;
        }
        return planetNumber;
    }
    public static int getMahaDashaPlanetNumberHindi(String planet)
    {
        int planetNumber = 0;
        switch (planet.trim())
        {
            case "सूर्य":
                planetNumber = 1;
                break;

            case "चंद्र":
                planetNumber = 2;
                break;

            case "मंगल":
                planetNumber = 3;
                break;

            case "बुध":
                planetNumber = 4;
                break;

            case "बृहस्पति":
                planetNumber = 5;
                break;

            case "शुक्र":
                planetNumber = 6;
                break;

            case "शनि":
                planetNumber = 7;
                break;

            case "राहु":
                planetNumber = 8;
                break;

            case "केतु":
                planetNumber = 9;
                break;
        }
        return planetNumber;
    }
    
    public static String properCase(String str)
    {
    	return str.substring(0,1).toUpperCase()+str.substring(1).toLowerCase();
    	
    }
    
    public static String getProperSentece(String str,int lang,String spl)
    {
    	String returnStr="";
    	String[] splStr = str.split(spl);
    	for(int i=0 ; i<splStr.length;i++)
    	if(lang == 0)
    	{
    		returnStr = returnStr+getActualSenteceEn(splStr[i].toLowerCase().trim());
    		if(i<splStr.length-1){returnStr = returnStr+", ";}
    	}
    	else
    	{
    		returnStr = returnStr+splStr[i].trim();
    		if(i<splStr.length-1){returnStr = returnStr+"] ";}
    	}
    	return returnStr;
    	
    }
    /* This method is to split the values of planets with provided split values and calls  
     * getActualSenteceEnPlanets method to return the full planet name of short planet  name*/
    public static String getProperSentecePlanets(String str,int lang,String spl)
    {
    	String returnStr="";
    	String[] splStr = str.split(spl);
    	for(int i=0 ; i<splStr.length;i++)
    	if(lang == 0)
    	{
    		returnStr = returnStr+getActualSenteceEnPlanets(splStr[i].toLowerCase().trim());
    		if(i<splStr.length-1){returnStr = returnStr+", ";}
    	}
    	else
    	{
    		returnStr = returnStr+splStr[i].trim();
    		if(i<splStr.length-1){returnStr = returnStr+"] ";}
    	}
    	return returnStr;
    	
    }
    
    public static String getActualSenteceEn(String str)
    {
    	String rst="";
    	
    	for(int i=0 ; i<shortSignArray.length ; i++){
    		if(str.equals(shortSignArray[i])){
    			rst = longSignArray[i];
    		}
    	}
    	
    	return rst;
    	
    }
    /* This method (getActualSenteceEnPlanets) has been created for giving full planet names for intents */
    public static String getActualSenteceEnPlanets(String str)
    {
    	String rst="";
    	
    	for(int i=0 ; i<shortPlanetArray.length ; i++){
    		if(str.equals(shortPlanetArray[i])){
    			rst = longPlanetArray[i];
    		}
    	}
    	return rst;
    }
    
  }
