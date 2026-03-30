package com.ojassoft.astrosage.ui.fragments.matching;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.matching.OutputMatchingMasterActivity;
import com.ojassoft.astrosage.utils.CGlobalMatching;
import com.ojassoft.astrosage.utils.CUtils;

/**
 * Created by ojas on १५/१/१९.
 */

public class MatchingOutputBoyAndGirlDetailFragment extends Fragment implements View.OnClickListener {
    TextView boyDetailHeading, girlDetailHeading;
    TextView boyNameTitleTV, girlNameTitleTV;
    TextView boyNameValTV, girlNameValTV;
    TextView boyPlaceTitleTV, girlPlaceTitleTV;
    TextView boyPlaceValTV, girlPlaceValTV;
    TextView boyDateTitleTV, girlDateTitleTV;
    TextView boyDateValTV, girlDateValTV;
    TextView boyTimeTitleTV, girlTimeTitleTV;
    TextView boyTimeValTV, girlTimeValTV;
    Button boyViewKundliButton, girlViewKundliButton;
    BeanHoroPersonalInfo boyBeanHoroPersonalInfo;
    BeanHoroPersonalInfo girlBeanHoroPersonalInfo;

    public static MatchingOutputBoyAndGirlDetailFragment newInstance() {
        MatchingOutputBoyAndGirlDetailFragment matchingOutputBoyAndGirlDetailFragment = new MatchingOutputBoyAndGirlDetailFragment();
        Bundle args = new Bundle();
        matchingOutputBoyAndGirlDetailFragment.setArguments(args);
        return matchingOutputBoyAndGirlDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boyBeanHoroPersonalInfo = CGlobalMatching.getCGlobalMatching().getBoyPersonalDetail();
        girlBeanHoroPersonalInfo = CGlobalMatching.getCGlobalMatching().getGirlPersonalDetail();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.boy_girl_detail_layout, container, false);
        boyDetailHeading = view.findViewById(R.id.boy_detail_heading);
        boyNameTitleTV = view.findViewById(R.id.boy_name_tv);
        boyNameValTV = view.findViewById(R.id.boy_name_val_tv);
        boyPlaceTitleTV = view.findViewById(R.id.boy_birth_place_tv);
        boyPlaceValTV = view.findViewById(R.id.boy_birth_place_val_tv);
        boyDateTitleTV = view.findViewById(R.id.boy_birth_date_tv);
        boyDateValTV = view.findViewById(R.id.boy_birth_date_val_tv);
        boyTimeTitleTV = view.findViewById(R.id.boy_birth_time_tv);
        boyTimeValTV = view.findViewById(R.id.boy_birth_time_val_tv);
        boyViewKundliButton = view.findViewById(R.id.boy_detail_btn);

        girlDetailHeading = view.findViewById(R.id.girl_detail_heading);
        girlNameTitleTV = view.findViewById(R.id.girl_name_tv);
        girlNameValTV = view.findViewById(R.id.girl_name_val_tv);
        girlPlaceTitleTV = view.findViewById(R.id.girl_birth_place_tv);
        girlPlaceValTV = view.findViewById(R.id.girl_birth_place_val_tv);
        girlDateTitleTV = view.findViewById(R.id.girl_birth_date_tv);
        girlDateValTV = view.findViewById(R.id.girl_birth_date_val_tv);
        girlTimeTitleTV = view.findViewById(R.id.girl_birth_time_tv);
        girlTimeValTV = view.findViewById(R.id.girl_birth_time_val_tv);
        girlViewKundliButton = view.findViewById(R.id.girl_detail_btn);
        setDataOnViews();
        setTypefaceOfViews();
        return view;
    }

    private void setDataOnViews() {
        String[] dtPlaceBoy = CUtils.formatDateTimeOfKundliInputToShowOnMenu(boyBeanHoroPersonalInfo);
        String[] dateAndTimeBoy = dtPlaceBoy[0].trim().split(" ");
        boyNameValTV.setText(boyBeanHoroPersonalInfo.getName());
        boyPlaceValTV.setText(dtPlaceBoy[1]);
        boyDateValTV.setText(dateAndTimeBoy[0]);
        boyTimeValTV.setText(dateAndTimeBoy[3]);
        String[] dtPlaceGirl = CUtils.formatDateTimeOfKundliInputToShowOnMenu(girlBeanHoroPersonalInfo);
        String[] dateAndTimegirl = dtPlaceGirl[0].trim().split(" ");
        girlNameValTV.setText(girlBeanHoroPersonalInfo.getName());
        girlPlaceValTV.setText(dtPlaceGirl[1]);
        girlDateValTV.setText(dateAndTimegirl[0]);
        girlTimeValTV.setText(dateAndTimegirl[3]);
        girlViewKundliButton.setOnClickListener(MatchingOutputBoyAndGirlDetailFragment.this);
        boyViewKundliButton.setOnClickListener(MatchingOutputBoyAndGirlDetailFragment.this);
    }

    private void setTypefaceOfViews() {
        boyDetailHeading.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        boyNameTitleTV.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        boyNameValTV.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        boyPlaceTitleTV.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        boyPlaceValTV.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        boyDateTitleTV.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        boyDateValTV.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        boyTimeTitleTV.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        boyTimeValTV.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        boyViewKundliButton.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);

        girlDetailHeading.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        girlNameTitleTV.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        girlNameValTV.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        girlPlaceTitleTV.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        girlPlaceValTV.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        girlDateTitleTV.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        girlDateValTV.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        girlTimeTitleTV.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        girlTimeValTV.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        girlViewKundliButton.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.girl_detail_btn:
                ((OutputMatchingMasterActivity) getActivity()).openKundli(BaseInputActivity.TAG_VIEW_GIRL_KUNDLI);
                break;
            case R.id.boy_detail_btn:
                ((OutputMatchingMasterActivity) getActivity()).openKundli(BaseInputActivity.TAG_VIEW_BOYS_KUNDLI);
                break;
        }
    }
}
