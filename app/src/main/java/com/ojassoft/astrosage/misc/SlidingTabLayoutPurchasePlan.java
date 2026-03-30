package com.ojassoft.astrosage.misc;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

public class SlidingTabLayoutPurchasePlan extends HorizontalScrollView {

    private Typeface typeface = Typeface.DEFAULT;

    /**
     * Allows complete control over the colors drawn in the tab layout. Set with
     * {@link #setCustomTabColorizer(TabColorizer)}.
     */
    public interface TabColorizer {

        int getIndicatorColor(int position);

    }

    private static final int TITLE_OFFSET_DIPS = 24;
    // private static final int TAB_VIEW_PADDING_DIPS = 16;
    private static final int TAB_VIEW_PADDING_DIPS = 10;
    private static final int TAB_VIEW_TEXT_SIZE_SP = 12;

    private int mTitleOffset;

    private int mTabViewLayoutId;
    private int mTabViewTextViewId;
    private boolean mDistributeEvenly;

    private ViewPager mViewPager;
    private SparseArray<String> mContentDescriptions = new SparseArray<String>();
    private ViewPager.OnPageChangeListener mViewPagerPageChangeListener;

    private final SlidingTabStripPurchasePlan mTabStrip;

    public List<View> titleList = new ArrayList<View>();

    private Integer[] mIconResourceArray;
    private Integer[] mIconCheckResourceArray;
    Context contxt;
    private String screenId = "";
    public TextView tabtext;

    public SlidingTabLayoutPurchasePlan(Context context) {

        this(context, null);
        contxt = context;
    }

    public SlidingTabLayoutPurchasePlan(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingTabLayoutPurchasePlan(Context context, AttributeSet attrs,
                                        int defStyle) {
        super(context, attrs, defStyle);

        // Disable the Scroll Bar
        setHorizontalScrollBarEnabled(false);
        // Make sure that the Tab Strips fills this View
        setFillViewport(true);

        mTitleOffset = (int) (TITLE_OFFSET_DIPS * getResources()
                .getDisplayMetrics().density);

        mTabStrip = new SlidingTabStripPurchasePlan(context);
        addView(mTabStrip, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    public void initTextTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public Typeface getTextTypeface() {
        return this.typeface;
    }

    public Integer[] getIconResourceArray() {
        return mIconResourceArray;
    }

    private Integer getCheckedIconImageId(int index) {
        return this.mIconCheckResourceArray[index];
    }

    public void setIconResourceArray(Integer[] mIconResourceArray,
                                     Integer[] mIconCheckResourceArray) {
        this.mIconResourceArray = mIconResourceArray;
        this.mIconCheckResourceArray = mIconCheckResourceArray;
    }

    /**
     * Set the custom {@link TabColorizer} to be used.
     * <p/>
     * If you only require simple custmisation then you can use
     * {@link #setSelectedIndicatorColors(int...)} to achieve similar effects.
     */
    public void setCustomTabColorizer(TabColorizer tabColorizer) {
        mTabStrip.setCustomTabColorizer(tabColorizer);
    }

    public void setDistributeEvenly(boolean distributeEvenly) {
        mDistributeEvenly = distributeEvenly;
    }

    /**
     * Sets the colors to be used for indicating the selected tab. These colors
     * are treated as a circular array. Providing one color will mean that all
     * tabs are indicated with the same color.
     */
    public void setSelectedIndicatorColors(int... colors) {
        mTabStrip.setSelectedIndicatorColors(colors);
    }

    /**
     * Set the {@link ViewPager.OnPageChangeListener}. When using
     * {@link SlidingTabLayoutPurchasePlan} you are required to set any
     * {@link ViewPager.OnPageChangeListener} through this method. This is so
     * that the layout can update it's scroll position correctly.
     *
     * @see ViewPager#setOnPageChangeListener(ViewPager.OnPageChangeListener)
     */
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mViewPagerPageChangeListener = listener;
    }

    /**
     * Set the custom layout to be inflated for the tab views.
     *
     * @param layoutResId Layout id to be inflated
     * @param textViewId  id of the {@link TextView} in the inflated view
     */
    public void setCustomTabView(int layoutResId, int textViewId) {
        mTabViewLayoutId = layoutResId;
        mTabViewTextViewId = textViewId;
    }

    /**
     * Sets the associated view pager. Note that the assumption here is that the
     * pager content (number of tabs and tab titles) does not change after this
     * call has been made.
     */
    public void setViewPager(ViewPager viewPager, String screenId) {
        mTabStrip.removeAllViews();

        mViewPager = viewPager;
        if (viewPager != null) {
            viewPager.setOnPageChangeListener(new InternalViewPagerListener());
            this.screenId = screenId;
            populateTabStrip();
        }
    }

    /**
     * Create a default view to be used for tabs. This is called if a custom tab
     * view is not set via {@link #setCustomTabView(int, int)}.
     */
    protected TextView createDefaultTabView(Context context) {
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, TAB_VIEW_TEXT_SIZE_SP);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(
                android.R.attr.selectableItemBackground, outValue, true);
        textView.setBackgroundResource(outValue.resourceId);
        // textView.setAllCaps(true);//DISABLED BY BIJENDRA ON 12-08-15

        int padding = (int) (TAB_VIEW_PADDING_DIPS * getResources()
                .getDisplayMetrics().density);
        textView.setPadding(padding, padding, padding, padding);

        return textView;
    }


   /* private void populateTabStrip() {
        final PagerAdapter adapter =
                mViewPager.getAdapter();
        final View.OnClickListener tabClickListener =
                new TabClickListener();
        MY_VIEWS.clear();
        for (int i = 0; i < adapter.getCount(); i++) {
            View tabView = null;
            TextView tabTitleView =
                    null;

            if (mTabViewLayoutId != 0) { // If there is a custom tab view layout idset, try and inflate it
                tabView = LayoutInflater.from(getContext()).inflate(mTabViewLayoutId, mTabStrip,
                        false);
                tabTitleView = (TextView)
                        tabView.findViewById(mTabViewTextViewId);
            }

            if (tabView == null) {
                tabView = createDefaultTabView(getContext());
            }

            if (tabTitleView == null && TextView.class.isInstance(tabView)) {
                tabTitleView = (TextView) tabView;
            }

            if (mDistributeEvenly) {
                LinearLayout.LayoutParams lp =
                        (LinearLayout.LayoutParams) tabView.getLayoutParams();
                lp.width = 0;
                lp.weight = 1;
            }

            tabTitleView.setText(adapter.getPageTitle(i));
            tabView.setOnClickListener(tabClickListener);
            String desc =
                    mContentDescriptions.get(i, null);
            if (desc != null) {
                tabView.setContentDescription(desc);
            }

            tabView.setTag(i);
            tabView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) { // TODO Auto-generated methodstub
                    int index = Integer.valueOf(v.getTag().toString());
                    iMyTabListener.onSelectedTab(v, index);
                }
            });
            mTabStrip.addView(tabView);
            if (i == mViewPager.getCurrentItem()) {
                tabView.setSelected(true);
            }

            tabTitleView.setTextColor(getResources().getColorStateList(R.color.selector
            ));
            tabTitleView.setTextSize(14);
            MY_VIEWS.add(tabView);
        }
    }
     */

    public void populateTabStrip() {
        //String PlanPreferencename;
        titleList.clear();
        final PagerAdapter adapter = mViewPager.getAdapter();
        final View.OnClickListener tabClickListener = new TabClickListener();
        int planId = CUtils.getUserPurchasedPlanFromPreference(getContext());
        for (int i = 0; i < adapter.getCount(); i++) {
            View tabView = null;

            tabView = LayoutInflater.from(getContext()).inflate(
                    R.layout.lay_purchase_plan_tab_title, mTabStrip, false);

            ImageView iconImageView = (ImageView) tabView
                    .findViewById(R.id.tabimage);
            /*
            if (i == 0) {
				iconImageView.setImageDrawable(getContext().getResources()
						.getDrawable(getCheckedIconImageId(0)));
			} else {
				iconImageView.setImageDrawable(getContext().getResources()
						.getDrawable(getIconResourceArray()[i]));
			}
			 */

            //PlanPreferencename = CUtils.GetPlaninPreference(getContext());
            /*
             * 28 - Dec -2015
             * @description To set image on tabs
             */
            if (screenId.equals("")) {
                if ((i == 1 || i == 2)) {

                    if ((planId == 2 || planId == 4 || planId == 5) && i == 1) {
                        iconImageView.setImageDrawable(getContext().getResources()
                                .getDrawable(getCheckedIconImageId(1)));

                    } else if ((planId == 3 || planId == 6 || planId == 7) && i == 2) {
                        iconImageView.setImageDrawable(getContext().getResources()
                                .getDrawable(getCheckedIconImageId(2)));

                    } else if (planId == 1 && i == 1) {
                        iconImageView.setImageDrawable(getContext().getResources()
                                .getDrawable(getCheckedIconImageId(1)));
                    } else {
                        iconImageView.setImageDrawable(getContext().getResources()
                                .getDrawable(getIconResourceArray()[i]));
                    }
                } else {

                    iconImageView.setImageDrawable(getContext().getResources()
                            .getDrawable(getIconResourceArray()[i]));

                }
            } else {
                if (screenId.equals(CGlobalVariables.SILVER_PLAN_VALUE_YEAR) && i == 1) {
                    iconImageView.setImageDrawable(getContext().getResources()
                            .getDrawable(getCheckedIconImageId(1)));
                } else if (screenId.equals(CGlobalVariables.GOLD_PLAN_VALUE_YEAR) && i == 2) {
                    iconImageView.setImageDrawable(getContext().getResources()
                            .getDrawable(getCheckedIconImageId(2)));
                } else {
                    iconImageView.setImageDrawable(getContext().getResources()
                            .getDrawable(getIconResourceArray()[i]));
                }
            }
            if (mDistributeEvenly) {
                LinearLayout.LayoutParams lp =
                        (LinearLayout.LayoutParams) tabView.getLayoutParams();
                lp.width = 0;
                lp.weight = 1;
            }
            tabtext = (TextView) tabView.findViewById(R.id.tabtext);

            // SET TEXT VIEW TYPE FACE
            //tabtext.setTypeface(getTextTypeface(), Typeface.BOLD);

            tabtext.setText(adapter.getPageTitle(i));

            if (planId == 2 || planId == 4 || planId == 5) {
                if (i == 1) {
                    tabtext.setTextColor(getResources().getColor(R.color.subs_sky_blue));
                }
            } else if (planId == 3 || planId == 6 || planId == 7) {
                if (i == 2) {
                    tabtext.setTextColor(getResources().getColor(R.color.subs_orange));
                }
            }


            tabView.setOnClickListener(tabClickListener);
            View leftLine = tabView.findViewById(R.id.left_line_connector);
            View rightLine = tabView.findViewById(R.id.right_line_connector);
            if (i == 0) {
                leftLine.setVisibility(INVISIBLE);
            } else if (i == 2) {
                rightLine.setVisibility(INVISIBLE);
            }

            titleList.add(tabView);
            mTabStrip.addView(tabView);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (mViewPager != null) {
            scrollToTab(mViewPager.getCurrentItem(), 0);
        }
    }

    private void scrollToTab(int tabIndex, int positionOffset) {
        final int tabStripChildCount = mTabStrip.getChildCount();
        if (tabStripChildCount == 0 || tabIndex < 0
                || tabIndex >= tabStripChildCount) {
            return;
        }

        View selectedChild = mTabStrip.getChildAt(tabIndex);
        if (selectedChild != null) {
            int targetScrollX = selectedChild.getLeft() + positionOffset;

            if (tabIndex > 0 || positionOffset > 0) {
                // If we're not at the first child and are mid-scroll, make sure
                // we obey the offset
                targetScrollX -= mTitleOffset;
            }

            scrollTo(targetScrollX, 0);

        }
    }

    private class InternalViewPagerListener implements
            ViewPager.OnPageChangeListener {
        private int mScrollState;

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            int tabStripChildCount = mTabStrip.getChildCount();
            if ((tabStripChildCount == 0) || (position < 0)
                    || (position >= tabStripChildCount)) {
                return;
            }

            mTabStrip.onViewPagerPageChanged(position, positionOffset);

            View selectedTitle = mTabStrip.getChildAt(position);
            int extraOffset = (selectedTitle != null) ? (int) (positionOffset * selectedTitle
                    .getWidth()) : 0;
            scrollToTab(position, extraOffset);

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrolled(position,
                        positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mScrollState = state;

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                mTabStrip.onViewPagerPageChanged(position, 0f);
                scrollToTab(position, 0);
            }
            for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                mTabStrip.getChildAt(i).setSelected(position == i);
            }
            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageSelected(position);
            }
            // iMyTabListener.onSelectedTab( mTabStrip.getChildAt(position),
            // position);
            showSelectedTab(mTabStrip.getChildAt(position), position);
        }

    }

    private class TabClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                if (v == mTabStrip.getChildAt(i)) {
                    mViewPager.setCurrentItem(i);
                    // iMyTabListener.onSelectedTab(v, i);
                    showSelectedTab(v, i);
                    return;
                }
            }
        }
    }

    public void showSelectedTab(View view, int index) {
        TextView tv;
        ImageView iv;
        PagerAdapter adapter = mViewPager.getAdapter();
        String title = (String) mViewPager.getAdapter().getPageTitle(index);
        for (int i = 0; i < titleList.size(); i++) {
            if (i != index) {
                ((ImageView) ((View) (titleList.get(i)))
                        .findViewById(R.id.tabimage))
                        .setImageDrawable(getResources().getDrawable(
                                mIconResourceArray[i]));
                ((TextView) ((View) (titleList.get(i)))
                        .findViewById(R.id.tabtext)).setText(adapter
                        .getPageTitle(i).toString());
                ((TextView) ((View) (titleList.get(i)))
                        .findViewById(R.id.tabtext)).setTextColor(getResources().getColor(R.color.black));

            }

        }

        if (view != null) {
            ((ImageView) view.findViewById(R.id.tabimage))
                    .setImageDrawable(getResources().getDrawable(
                            getCheckedIconImageId(index)));

            ((TextView) view.findViewById(R.id.tabtext)).setText(title);
            if (index == 0) {
                ((TextView) view.findViewById(R.id.tabtext)).setTextColor(getResources().getColor(R.color.subs_violet));
            } else if (index == 1) {
                ((TextView) view.findViewById(R.id.tabtext)).setTextColor(getResources().getColor(R.color.subs_sky_blue));
            } else if (index == 2) {
                ((TextView) view.findViewById(R.id.tabtext)).setTextColor(getResources().getColor(R.color.subs_orange));
            }
        }

    }

    /*
     * private String GetPlaninPreference(Context context) { String planname =
     * ""; SharedPreferences sharedPreferences = context
     * .getSharedPreferences(CGlobalVariables.APP_PREFS_NAME,
     * Context.MODE_PRIVATE); planname =
     * sharedPreferences.getString(CGlobalVariables.APP_PREFS_Plan, "");
     * planname="gold";
     *
     * return planname;
     *
     * }
     */
}