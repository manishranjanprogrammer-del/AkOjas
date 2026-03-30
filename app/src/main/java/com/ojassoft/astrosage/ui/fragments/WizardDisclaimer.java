package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.ojassoft.astrosage.R;

public class WizardDisclaimer extends Fragment {
    CheckBox checkBoxI_Accept;
    Button nextButton;
    IWizardDisclaimerFragmentInterface iWizardDisclaimerFragmentInterface;

    public WizardDisclaimer() {
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.lay_wizard_disclaimer, container, false);
        checkBoxI_Accept = (CheckBox) view.findViewById(R.id.checkBox_I_Accept);
        nextButton = (Button) view.findViewById(R.id.buttonNext);
        /*checkBoxI_Accept.setOnCheckedChangeListener( new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					nextButton.setEnabled(isChecked);
					nextButton.setTextColor(Color.BLACK);
				}else {
					nextButton.setEnabled(isChecked);
					nextButton.setTextColor(Color.GRAY);
				}
			}
		});*/
        nextButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                iWizardDisclaimerFragmentInterface.clickedDisclaimerNextButton();
            }
        });


        return view;

    }


    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onResume()
     */
    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        iWizardDisclaimerFragmentInterface = (IWizardDisclaimerFragmentInterface) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        iWizardDisclaimerFragmentInterface = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public interface IWizardDisclaimerFragmentInterface {
        public void clickedDisclaimerNextButton();
    }
}