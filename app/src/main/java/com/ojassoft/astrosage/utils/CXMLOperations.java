package com.ojassoft.astrosage.utils;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * This class is used to parse XML document.
 * All this functions in this class are class level
 * functions.
 *
 * @author Bijendra
 * @version 1.0.0
 * @date 11 may 2012
 * @modify -
 */
public class CXMLOperations {


    /**
     * This function is used to convert string  value into
     * XML Document that is passed as a parameter.
     *
     * @param xml
     * @return Document
     */
    public final static Document XMLfromString(String xml) {

        Document doc = null;

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder db = dbf.newDocumentBuilder();

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);

        } catch (ParserConfigurationException e) {
            System.out.println("XML parse error: " + e.getMessage());
            return null;
        } catch (SAXException e) {
            System.out.println("Wrong XML file structure: " + e.getMessage());
            return null;
        } catch (IOException e) {
            System.out.println("I/O exeption: " + e.getMessage());
            return null;
        }

        return doc;

    }

    /**
     * This function return value  according to  passed node.
     *
     * @param elem
     * @return String
     */
    public final static String getElementValue(Node elem) {
        Node kid;
        if (elem != null) {
            if (elem.hasChildNodes()) {
                for (kid = elem.getFirstChild(); kid != null; kid = kid.getNextSibling()) {
                    if (kid.getNodeType() == Node.TEXT_NODE) {
                        return kid.getNodeValue();
                    }
                }
            }
        }
        return "";
    }

    /**
     * This function return number of records
     * in the Document according to padded  document.
     *
     * @param doc
     * @return int(number of records)
     */
    public static int numResults(Document doc) {
        Node results = doc.getDocumentElement();
        int res = -1;

        try {
            res = Integer.valueOf(results.getAttributes().getNamedItem("count").getNodeValue());
        } catch (Exception e) {
            res = -1;
        }

        return res;
    }

    /**
     * This function return value of element based on passed
     * tag name.
     *
     * @param item
     * @param str
     * @return String
     */
    public static String getValue(Element item, String str) {

        //String tempValue=item.getNodeValue().toString();
//	Log.d("getNodeValue",tempValue);
        NodeList n = item.getElementsByTagName(str);
        return getElementValue(n.item(0));
        //return getElementValue(n.item(0));
    /*String tempValue=item.getNodeValue().toString();
	
	Log.d("getNodeValue",tempValue);
	int index1=tempValue.indexOf("<"+str+">")+str.length();
	int index2=tempValue.indexOf("</"+str+">");
	String iValue=tempValue.substring(index1,index2 );
	Log.d("index1", String.valueOf(index1));
	Log.d("index2", String.valueOf(index2));
	Log.d("LoginValue",iValue);
	
return iValue;*/


    }

}
