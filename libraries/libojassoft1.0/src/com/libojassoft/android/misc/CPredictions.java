package com.libojassoft.android.misc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.libojassoft.android.R;
import com.libojassoft.android.utils.LibCGlobalVariables;
import android.content.Context;



public class CPredictions {
	
 static final String [] RASHI_NAME_TITLE={"aries","taurus","gemini","cancer","leo","virgo","libra","scorpio","saggittarius","capricorn","aquarius","pisces"};
	
	
	  static final int ARIES = 0;
	  static final int TAURUS = 1;
	  static final int GEMINI = 2;
	  static final int CANCER = 3;
	  static final int LEO = 4;
	  static final int VIRGO = 5;
	  static final int LIBRA = 6;
	  static final int SCORPIO = 7;

	  static final int SAGGITTARIUS = 8;
	  static final int CAPRICORN = 9;
	  static final int AQUARIUS = 10;
	  static final int PISCES = 11;
	
	
	  static final int GENERAL = 0;
	  static final int FAMILY = 1;
	  static final int HEALTH = 2;
	 static final int LOVE = 3;
	 static final int CAREER = 4;
	 static final int MONEY = 5;
	 static final int EDUCTION = 6;
	 static final int REMEDIES = 7;

	 public static String getYearlyPredictionDetail(Context context,LibCGlobalVariables.LANGUAGE_TYPE langType,LibCGlobalVariables.RASHI_TYPE rashiType) throws Exception {
			 StringBuilder sb=new StringBuilder();
			 int resourceId=-1;
			 String expression="";
		      //GET RESOURCE ID
			 	resourceId=getResourceIdPredictionYearly( langType);
		        // create an InputSource object from /res/raw
		        InputSource inputSrc = new InputSource(context.getResources().openRawResource(resourceId));
		        // query XPath instance, this is the parser
		        XPath xpath = XPathFactory.newInstance().newXPath();
		        // specify the xpath expression
		       
		        expression=getxPathExpressionYearly(rashiType);
		        // list of nodes queried
		        NodeList nodes = (NodeList)xpath.evaluate(expression, inputSrc, XPathConstants.NODESET);
		        // if node found
		        if(nodes != null && nodes.getLength() > 0) {
		           
		            int len = nodes.getLength();
		            for(int i = 0; i < len; ++i) {
		                // query value
		                Node node = nodes.item(i);              
		              
		                sb.append(node.getTextContent());
		            }
		        }
		        return sb.toString();
		    }
	
	 static int getResourceIdPredictionYearly(LibCGlobalVariables.LANGUAGE_TYPE langType)
		{
			int resourceId=-1;
			switch(langType)
			{
				case  HINDI:
					resourceId=R.raw.hind_rashi_pre_yealy;
					break;
				case  ENGLISH:
					resourceId=R.raw.eng_rashi_pre_yealy;
					break;
				case  TAMIL:
					resourceId=R.raw.tamil_rashi_pre_yealy;
					break;
			}
			
			return resourceId;
		}
		
		 static String readRawTextFile(Context ctx,int resourceId ) throws Exception
		 {
			
		     InputStream inputStream = ctx.getResources().openRawResource(resourceId);

		     InputStreamReader inputreader = new InputStreamReader(inputStream);
		     BufferedReader buffreader = new BufferedReader(inputreader);
		     String line;
		     StringBuilder text = new StringBuilder();
		     
		         while (( line = buffreader.readLine()) != null) 
		             text.append(line);	            
		        
		    
		     return text.toString();
		 }
		
		 static int getRashiIndex(LibCGlobalVariables.RASHI_TYPE rashiType)
		{
			int rashiIndex=-1;
			switch(rashiType)
			{
			  case ARIES:
				  rashiIndex=ARIES;
				  break;
			  case TAURUS:
				  rashiIndex=TAURUS;
				  break;
			  case GEMINI:
				  rashiIndex=GEMINI;
				  break;
			  case CANCER:
				  rashiIndex=CANCER;
				  break;
			  case LEO:
				  rashiIndex=LEO;
				  break;
			  case VIRGO:
				  rashiIndex=VIRGO;
				  break;
			  case LIBRA:
				  rashiIndex=LIBRA;
				  break;
			  case SCORPIO:
				  rashiIndex=SCORPIO;
				  break;
			  case SAGGITTARIUS:
				  rashiIndex=SAGGITTARIUS;
				  break;
			  case CAPRICORN:
				  rashiIndex=CAPRICORN;
				  break;
			  case AQUARIUS:
				  rashiIndex=AQUARIUS;
				  break;
			  case PISCES:
				  rashiIndex=PISCES;
				  break;
			 
			}
			
			return rashiIndex;
			
		}
		
		
		  static String getxPathExpressionYearly(LibCGlobalVariables.RASHI_TYPE rashiType)
		  {
			  StringBuilder sbExpression=new StringBuilder();
			  sbExpression.append("/ojasyearlypredictions");
			  sbExpression.append("/");
			  sbExpression.append(RASHI_NAME_TITLE[getRashiIndex(rashiType)]);
			  sbExpression.append("/");
			  //sbExpression.append(SUB_HEADING_RASHI_PREDICTION_TITLE[getPredictionSubHeadingIndex(subHeading)]);
			  sbExpression.append("general");
			
			  return sbExpression.toString();
			 
		  }

}
