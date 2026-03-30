package com.libojassoft.android.ui;

import com.libojassoft.android.R;
import com.libojassoft.android.custom.controls.JustifiedTextView;
import com.libojassoft.android.utils.LibCGlobalVariables;
import com.libojassoft.android.utils.LibCUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;


/**
 * 
 *  @author  Bijendra : 17-april-2013
 *  Description:This activity is used to show Disclaimer. 
 *  
 *  Modified by Hukum on 22-April-2013. Added justified textview for disclaimer text.
 */
public class ActDisclaimer extends Activity {

	TextView disclaimerText;
	//ADDED BY BIJENDRA ON 25-MAY-2013 FOR PHONE AND TABLET FONT SIZE
	private int TXT_SIZE_PHONE=17;
	private int TXT_SIZE_TABLET=32;
	//END
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.libojassoft_lay_disclaimer);		
		
		//Added by Hukum for Justified Text
		 JustifiedTextView J = (JustifiedTextView)findViewById(R.id.textDisclaimerText);
         J.setText(getResources().getString(R.string.disclaimer_text));
         
         J.setTextColor(getResources().getColor(R.color.Disclaimer_Text_Color));
         //J.setTextSize(17); THIS CODE IS DISABLED BY BIJENDRA ON 25-MAY-2013 
         //THIS CODE IS ADDED BY BIJENDRA ON 25-MAY-2013 
         if(LibCUtils.isTablet(getApplicationContext()))
        	 J.setTextSize(TXT_SIZE_TABLET);
         else
        	 J.setTextSize(TXT_SIZE_PHONE);
         //END
        	 
         J.reload();//THIS IS ADDED BY BIJENDRA ON 27-APRIL-2013 
        
       //End
	}

	public void onClickDisclaimerCondition(View v) {
		int result = LibCGlobalVariables.CONST_DISCLAIMER_DISAGREE;
		try {
			result = Integer.valueOf(v.getTag().toString().trim());

		} catch (Exception e) {

		}

		if (result == LibCGlobalVariables.CONST_DISCLAIMER_AGREE)
			setResult(RESULT_OK, null);
		else
			setResult(RESULT_CANCELED, null);

		LibCUtils.setDisclaimerAgreementInPreference(getApplicationContext(),result);

		ActDisclaimer.this.finish();
	}

}
