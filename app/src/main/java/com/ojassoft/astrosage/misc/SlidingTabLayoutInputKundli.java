package com.ojassoft.astrosage.misc;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.jinterface.IHoroscopeDetailSelect;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import android.app.Activity;
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
import android.widget.LinearLayout;
import android.widget.TextView;


public class SlidingTabLayoutInputKundli extends HorizontalScrollView {


    /**
     * Allows complete control over the colors drawn in the tab layout. Set with
     * {@link #setCustomTabColorizer(TabColorizer)}.
     */
    public interface TabColorizer {

        int getIndicatorColor(int position);

    }

    private static final int TITLE_OFFSET_DIPS = 24;
    private static final int TAB_VIEW_PADDING_DIPS = 10;//16;
    private static final int TAB_VIEW_TEXT_SIZE_SP = 12;

    private int mTitleOffset;

    private int mTabViewLayoutId;
    private int mTabViewTextViewId;
    private boolean mDistributeEvenly;

    private ViewPager mViewPager;
    private SparseArray<String> mContentDescriptions = new SparseArray<String>();
    private ViewPager.OnPageChangeListener mViewPagerPageChangeListener;

    private final SlidingTabStripInputKundli mTabStrip;


    private Integer[] mIconResourceArray;

    private Typeface typeface = Typeface.DEFAULT;

    private IHoroscopeDetailSelect iHoroscopeDetailSelect;

    public SlidingTabLayoutInputKundli(Context context) {
        this(context, null);
    }

    public SlidingTabLayoutInputKundli(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public SlidingTabLayoutInputKundli(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // Disable the Scroll Bar
        setHorizontalScrollBarEnabled(false);
        // Make sure that the Tab Strips fills this View
        setFillViewport(true);

        mTitleOffset = (int) (TITLE_OFFSET_DIPS * getResources().getDisplayMetrics().density);

        mTabStrip = new SlidingTabStripInputKundli(context);
        addView(mTabStrip, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    public void setActivity(Activity activity) {
        iHoroscopeDetailSelect = (IHoroscopeDetailSelect) activity;
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

    public void setIconResourceArray(Integer[] mIconResourceArray) {
        this.mIconResourceArray = mIconResourceArray;
    }

    /**
     * Set the custom {@link TabColorizer} to be used.
     * <p/>
     * If you only require simple custmisation then you can use
     * {@link #setSelectedIndicatorColors(int...)} to achieve
     * similar effects.
     */
    public void setCustomTabColorizer(SlidingTabLayoutInputKundli.TabColorizer tabColorizer) {
        mTabStrip.setCustomTabColorizer(tabColorizer);
    }

    public void setDistributeEvenly(boolean distributeEvenly) {
        mDistributeEvenly = distributeEvenly;
    }

    /**
     * Sets the colors to be used for indicating the selected tab. These colors are treated as a
     * circular array. Providing one color will mean that all tabs are indicated with the same color.
     */
    public void setSelectedIndicatorColors(int... colors) {
        mTabStrip.setSelectedIndicatorColors(colors);
    }

    /**
     * Set the {@link ViewPager.OnPageChangeListener}. When using {@link SlidingTabLayoutInputKundli} you are
     * required to set any {@link ViewPager.OnPageChangeListener} through this method. This is so
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
     * Sets the associated view pager. Note that the assumption here is that the pager content
     * (number of tabs and tab titles) does not change after this call has been made.
     */
    public void setViewPager(ViewPager viewPager) {
        mTabStrip.removeAllViews();

        mViewPager = viewPager;
        if (viewPager != null) {
            viewPager.setOnPageChangeListener(new InternalViewPagerListener());
            populateTabStrip();
        }
    }

    /**
     * Create a default view to be used for tabs. This is called if a custom tab view is not set via
     * {@link #setCustomTabView(int, int)}.
     */
    protected TextView createDefaultTabView(Context context) {
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, TAB_VIEW_TEXT_SIZE_SP);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground,
                outValue, true);
        textView.setBackgroundResource(outValue.resourceId);
        //textView.setAllCaps(true);//DISABLED BY BIJENDRA ON 12-08-15

        int padding = (int) (TAB_VIEW_PADDING_DIPS * getResources().getDisplayMetrics().density);
        textView.setPadding(padding, padding, padding, padding);

        textView.setTypeface(typeface, Typeface.BOLD);//ADDED BY BIJENDRA ON 14-08-15


        return textView;
    }

    /*private void populateTabStrip() {
           final PagerAdapter adapter = mViewPager.getAdapter();
           final View.OnClickListener tabClickListener = new TabClickListener();

           for (int i = 0; i < adapter.getCount(); i++) {
               View tabView = null;
               TextView tabTitleView = null;

               if (mTabViewLayoutId != 0) {
                   // If there is a custom tab view layout id set, try and inflate it
                   tabView = LayoutInflater.from(getContext()).inflate(mTabViewLayoutId, mTabStrip,
                           false);
                   tabTitleView = (TextView) tabView.findViewById(mTabViewTextViewId);
               }

               if (tabView == null) {
                   tabView = createDefaultTabView(getContext());
               }

               if (tabTitleView == null && TextView.class.isInstance(tabView)) {
                   tabTitleView = (TextView) tabView;
               }

               if (mDistributeEvenly) {
                   LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                   lp.width = 0;
                   lp.weight = 1;
               }

               tabTitleView.setText(adapter.getPageTitle(i));
               tabView.setOnClickListener(tabClickListener);
               String desc = mContentDescriptions.get(i, null);
               if (desc != null) {
                   tabView.setContentDescription(desc);
               }

               tabView.setTag(i);
               mTabStrip.addView(tabView);
               if (i == mViewPager.getCurrentItem()) {
                   tabView.setSelected(true);
               }

               tabTitleView.setTextColor(getResources().getColorStateList(R.color.selector));
               tabTitleView.setTextSize(14);

           }
       }
   */
    public void populateTabStrip() {
        int Language_Code = CUtils.getLanguageCodeFromPreference(this.getContext());
        if (Language_Code == CGlobalVariables.TAMIL || Language_Code == CGlobalVariables.MARATHI)
            drawStripForTamil();
        else
            drawStripForEnglishOHindi();

    }

    private void drawStripForTamil() {
        final PagerAdapter adapter = mViewPager.getAdapter();
        final View.OnClickListener tabClickListener = new TabClickListener();

        for (int i = 0; i < adapter.getCount(); i++) {
            View tabView = null;

            tabView = LayoutInflater.from(getContext()).inflate(
                    R.layout.lay_input_kundli_tab_title, mTabStrip, false);


            TextView tabtext = (TextView) tabView.findViewById(R.id.tabtext);

            // SET TEXT VIEW TYPE FACE
            tabtext.setTypeface(getTextTypeface(), Typeface.BOLD);

            tabtext.setText(adapter.getPageTitle(i));
            tabView.setOnClickListener(tabClickListener);


            mTabStrip.addView(tabView);
        }
    }

    private void drawStripForEnglishOHindi() {
        final PagerAdapter adapter = mViewPager.getAdapter();
        final View.OnClickListener tabClickListener = new TabClickListener();

        for (int i = 0; i < adapter.getCount(); i++) {
            View tabView = null;
            TextView tabTitleView = null;

            if (mTabViewLayoutId != 0) {
                // If there is a custom tab view layout id set, try and inflate it
                tabView = LayoutInflater.from(getContext()).inflate(mTabViewLayoutId, mTabStrip,
                        false);
                tabTitleView = (TextView) tabView.findViewById(mTabViewTextViewId);

            }

            if (tabView == null) {
                tabView = createDefaultTabView(getContext());
            }

            if (tabTitleView == null && TextView.class.isInstance(tabView)) {
                tabTitleView = (TextView) tabView;
            }

            if (mDistributeEvenly) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                lp.width = 0;
                lp.weight = 1;
            }

            // SET TEXT VIEW TYPE FACE
            tabTitleView.setTypeface(getTextTypeface(), Typeface.BOLD);
            tabTitleView.setText(adapter.getPageTitle(i));
            tabView.setOnClickListener(tabClickListener);
            String desc = mContentDescriptions.get(i, null);
            if (desc != null) {
                tabView.setContentDescription(desc);
            }

            tabView.setTag(i);

            mTabStrip.addView(tabView);
            if (i == mViewPager.getCurrentItem()) {
                tabView.setSelected(true);
            }

            tabTitleView.setTextColor(getResources().getColorStateList(R.color.selector));
            tabTitleView.setTextSize(14);

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
        if (tabStripChildCount == 0 || tabIndex < 0 || tabIndex >= tabStripChildCount) {
            return;
        }

        View selectedChild = mTabStrip.getChildAt(tabIndex);
        if (selectedChild != null) {
            int targetScrollX = selectedChild.getLeft() + positionOffset;

            if (tabIndex > 0 || positionOffset > 0) {
                // If we're not at the first child and are mid-scroll, make sure we obey the offset
                targetScrollX -= mTitleOffset;
            }

            // iHoroscopeDetailSelect.horoscopeDetailDelected(targetScrollX);
            scrollTo(targetScrollX, 0);

        }
    }

    private class InternalViewPagerListener implements ViewPager.OnPageChangeListener {
        private int mScrollState;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int tabStripChildCount = mTabStrip.getChildCount();
            if ((tabStripChildCount == 0) || (position < 0) || (position >= tabStripChildCount)) {
                return;
            }

            mTabStrip.onViewPagerPageChanged(position, positionOffset);

            View selectedTitle = mTabStrip.getChildAt(position);
            int extraOffset = (selectedTitle != null)
                    ? (int) (positionOffset * selectedTitle.getWidth())
                    : 0;
            scrollToTab(position, extraOffset);

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrolled(position, positionOffset,
                        positionOffsetPixels);
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
            try {
                iHoroscopeDetailSelect.horoscopeDetailDelected(position);
            } catch (Exception e) {
                //	//Log.e("nullpointer exception Print", e.getMessage());
            }


        }

    }

    private class TabClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                if (v == mTabStrip.getChildAt(i)) {
                    try {
                        mViewPager.setCurrentItem(i);
                        iHoroscopeDetailSelect.horoscopeDetailDelected(i);
                    } catch (Exception e) {
                        //	//Log.e("nullpointer exception Print", e.getMessage());

                    }


                    return;
                }
            }
        }
    }


}