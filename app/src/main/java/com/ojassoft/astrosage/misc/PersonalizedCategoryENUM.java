package com.ojassoft.astrosage.misc;

import static android.provider.Settings.System.getString;

import android.content.Context;
import android.content.res.Resources;

import com.ojassoft.astrosage.R;

public enum PersonalizedCategoryENUM {
    LOVE(R.string.love,R.drawable.ic_weekly_love,175),
    CAREER(R.string.career,R.drawable.ic_career,176),
    MARRIAGE(R.string.marriage_title,R.drawable.ic_marriage_knot,177),
    HEALTH(R.string.health,R.drawable.ic_health,178),
    EDUCATION(R.string.education,R.drawable.ic_book_stack,179),
    BUSINESS(R.string.business,R.drawable.ic_business,180),
    STOCK_MARKET(R.string.stock_market,R.drawable.stock_market,181),
    LEGAL(R.string.legal,R.drawable.ic_legal,182),
    WEIGHT_LOSS(R.string.weight_loss,R.drawable.ic_weightloss,183),
    MENTAL_COACH(R.string.mental_health,R.drawable.ic_mental_coach,184),
    COUNSELLOR(R.string.counsellor,R.drawable.iv_counsellor,185),
    MOTHERHOOD(R.string.motherhood,R.drawable.ic_motherhood,186),
    TODAY(R.string.today,R.drawable.ic_today_calender,187),
//    POLITICS(R.string.politics,R.drawable.ic_politics,188),
//    IPL(R.string.ipl,R.drawable.ic_ipl,189),
    MUHURAT(R.string.muhurat,R.drawable.ic_muhurat,190);

    private final int title;
    final int categoryIconResID;
    final int screenID;

    PersonalizedCategoryENUM(int title,int iconResId,int screenID){
        this.title = title;
        this.categoryIconResID = iconResId;
        this.screenID = screenID;
    }

    public String getTitle(Context context, Resources resources){
        if (resources == null) {
            return context.getString(title);
        } else {
            return resources.getString(title);
        }
    }
    public int getScreenID(){
        return screenID;
    }

    public int getIconResID(){
        return categoryIconResID;
    }

    /**
     * Returns the PersonalizedCategoryENUM corresponding to the given screenId
     * @param screenId The screen ID to look up
     * @return The matching PersonalizedCategoryENUM, or null if no match is found
     */
    public static PersonalizedCategoryENUM getByScreenId(int screenId) {
        for (PersonalizedCategoryENUM category : values()) {
            if (category.screenID == screenId) {
                return category;
            }
        }
        return null;
    }
}
