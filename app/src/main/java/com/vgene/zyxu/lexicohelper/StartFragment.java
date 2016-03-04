package com.vgene.zyxu.lexicohelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by zyxu on 3/2/16.
 */
public class StartFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start,container,false);


        final Button btnSettings = (Button) view.findViewById(R.id.btn_settings);
        final Button btnStart = (Button) view.findViewById(R.id.btn_start);

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingFragment settingFragment = new SettingFragment();
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("back").add(R.id.main_fragment, settingFragment).commit();
                //showSettings();
            }
        });


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LearningFragment learningFragment = new LearningFragment();
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("back").replace(R.id.main_fragment, learningFragment).commit();
            }
        });
        return view;
    }
}
