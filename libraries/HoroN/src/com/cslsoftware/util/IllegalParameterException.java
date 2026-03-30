package com.cslsoftware.util;

/**
 * Insert the type's description here.
 * Creation date: (6/28/02 5:47:49 PM)
 * @author: Administrator
 * @com.register ( clsid=1A12CD6D-F6BD-11D6-8ECE-0002442CEF43, typelib=1A12CD61-F6BD-11D6-8ECE-0002442CEF43 )
 */


public class IllegalParameterException extends HoroException {
/**
 * IllegalParameterException constructor comment.
 */
public IllegalParameterException() {
	super();
}
/**
 * IllegalParameterException constructor comment.
 */
public IllegalParameterException(String message) {
	super(message);
}
/**
 * IllegalParameterException constructor comment.
 */
public IllegalParameterException(String message1, String message2) {
	super(message1 + message2);
}
}
