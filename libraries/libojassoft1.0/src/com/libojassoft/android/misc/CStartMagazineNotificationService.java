package com.libojassoft.android.misc;

import java.util.Locale;

import com.libojassoft.android.utils.LibCGlobalVariables;
import com.libojassoft.android.utils.LibCUtils;


import android.content.Context;

public class CStartMagazineNotificationService {
	Context _context;
	int timeToDelay=0;
	String applicationAdvId;
	int languageCode = 0;
	private final int LIB_LANG_CODE_ENGLISH = 0;
	private final int LIB_LANG_CODE_HINDI = 1;

	public CStartMagazineNotificationService(Context context,int timeDelay,String appAdvId,int _languageCode){
		_context=context;
		timeToDelay=timeDelay;
		applicationAdvId=appAdvId;
		languageCode = _languageCode;
	}

	public void startMyService(){
		if( LibCUtils.getUserChoiceFromSDCard(_context)!=LibCGlobalVariables.NEVER)
		{

			if(languageCode == LIB_LANG_CODE_ENGLISH) {
				new CMagazineNotificationServiceInEnglish(
						_context,
						timeToDelay,
						applicationAdvId).startMyService();
			}else if(languageCode == LIB_LANG_CODE_HINDI) {


				boolean hindiAvailble = LibCUtils.isSupportUnicodeHindi();// ADDED BY BIJENDRA ON 24 -JULY-13
				if (hindiAvailble) {
					new CMagazineNotificationServiceInHindi(
							_context,
							timeToDelay,
							applicationAdvId).startMyService();

				}else{
					new CMagazineNotificationServiceInEnglish(
							_context,
							timeToDelay,
							applicationAdvId).startMyService();

				}

			}else{
				new CMagazineNotificationServiceInEnglish(
						_context,
						timeToDelay,
						applicationAdvId).startMyService();
			}
		}
	}

}
