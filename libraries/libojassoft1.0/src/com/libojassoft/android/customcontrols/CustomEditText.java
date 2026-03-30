package com.libojassoft.android.customcontrols;



import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.LayoutInflater.Factory;
import android.widget.EditText;
import android.widget.TextView;

/**
 * This is custom EditText control 
 * @author Bijendra
 *@date 15-nov-13
 *@copyright ojassoft
 */
public class CustomEditText extends EditText {
	
	/* Local variables*/
	private Rect mRect;
	private Paint mPaint;
	private Typeface mTypeFace=Typeface.DEFAULT;
	private LayoutInflater mLayoutInflater;
	private VALIDATION_TYPE mValidationType=VALIDATION_TYPE.NO;
	private String mErrorMsg="";
	private int mTextColor=Color.BLACK;
	private int mErrorMsgColor=Color.RED;
	private boolean mLocalization=false;

	private boolean isShowingErroMsg=false;
	/* Public variables */
	public static enum VALIDATION_TYPE {
		NO,E_MAIL,PHONE
	}
	
	
	public CustomEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mRect = new Rect();
		mPaint = new Paint();
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mPaint.setColor(Color.BLACK);		
	}
	
	
	/**
	 * Local function to set validation type
	 */
	private void setValidateInputType()
	{
		if(mValidationType==VALIDATION_TYPE.PHONE)
			this.setInputType(InputType.TYPE_CLASS_PHONE);
		
	}
	
	/**
	 * This function issued to set localization type
	 * @param boolean
	 */
  public void setLocalization(boolean localization)
  {
	  mLocalization=localization;
  }
	
  /**
   * This function is used to set edit text box, text color
   * @param color
   */
  public void setCustomTextColor(int color) {
		mTextColor=color;
	}
	/**
	 * This function is used to set error message text color
	 * @param color
	 */
	public void setErrorMessageCustomTextColor(int color)
	{
		mErrorMsgColor=color;
	}
	
	/**
	 * This function is used to set error message
	 * @param errorMsg
	 */
	public void setErrorMessage(String errorMsg)
	{
		mErrorMsg=errorMsg;
	}

	/**
	 * This function is used to set validation type(email/phone etc.)
	 * @param vType
	 */
  public void setValidationType(VALIDATION_TYPE vType)
  {
	  mValidationType=vType;
  }
  /**
   * This function is used to set custom typeface(font) 
   * @param typeFace
   */
	public void setCustomTypeface(Typeface typeFace)
	{
		mTypeFace=typeFace;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		int height = getHeight();
		int lHeight = getLineHeight();
		// the number of line
		int count = height / lHeight;
		if (getLineCount() > count) {
			// for long text with scrolling
			count = getLineCount();
		}		
		
		this.setTextColor(mTextColor);
		setValidateInputType();
		if(mLocalization)
		 setTypeface(mTypeFace);
		super.onDraw(canvas);
		
	}
	
	/**
	 * This function is used to set Layout intent flater from the called control 
	 * @param layoutInflater
	 */
	public void setLayoutInflater(LayoutInflater layoutInflater)
	{		
		
			mLayoutInflater=layoutInflater;
			mLayoutInflater.setFactory(new Factory() {
				
				@Override
	            public View onCreateView(String name, Context context,
	                    AttributeSet attrs) {
					
						
	                if (name.equalsIgnoreCase("TextView")) {
	                    try {
	                    	LayoutInflater li = LayoutInflater.from(context);
	                        final View view = li.createView(name, null, attrs);
	                        new Handler().post(new Runnable() {
	                            public void run() {  
	                            	if(isShowingErroMsg)
	                            	{
	                            		isShowingErroMsg=false;
		                            	((TextView) view).setTypeface(mTypeFace);
		                                ((TextView) view).setTextColor(mErrorMsgColor);
	                            	}
	                            	else
	                            	{
	                            		((TextView) view).setTypeface(Typeface.DEFAULT);
	                            	}
	                            		                            	
	                            }
	                        });
	                        return view;
	                    } catch (InflateException e) {
	                    	
	                    } catch (ClassNotFoundException e) {
	                    	
	                    }
	                }
	                return null;
	            }
	
	        });
		}
	


/**
 * This function return that this control has valid input or not 
 * @return boolean
 */
public boolean isValid()
{
  boolean _isValid=false;
	
	if(mValidationType==VALIDATION_TYPE.NO)
		return true;
	if(this.getText()==null)
		_isValid= false;
	
	if(mValidationType==VALIDATION_TYPE.E_MAIL)
		_isValid=	android.util.Patterns.EMAIL_ADDRESS.matcher(this.getText().toString()).matches();
	
	if(mValidationType==VALIDATION_TYPE.PHONE)
		_isValid=	android.util.Patterns.PHONE.matcher(this.getText().toString()).matches();
	
	if(!_isValid)	
	{
     isShowingErroMsg=true;
     this.setError(mErrorMsg);
	}
	
	return _isValid;
	
}


} 

