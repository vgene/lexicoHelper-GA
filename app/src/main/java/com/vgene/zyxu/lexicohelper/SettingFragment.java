package com.vgene.zyxu.lexicohelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

/**
 * Created by zyxu on 3/1/16.
 */
public class SettingFragment extends Fragment {

    private List<IndexEntity> indexEntities;
    ListView lvSettings;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_setting,container,false);

        lvSettings = (ListView) view.findViewById(R.id.lv_settings);

        indexEntities = MainActivity.getIndexEntities();
        final SettingsAdapter settingsAdapter = new SettingsAdapter(indexEntities,getContext());
        lvSettings.setAdapter(settingsAdapter);

        Button btnDone = (Button) view.findViewById(R.id.btn_done);
        Button btnA2N = (Button) view.findViewById(R.id.btn_quick_a2n);
        Button btnO2Z = (Button) view.findViewById(R.id.btn_quick_o2z);
        Button btnAll = (Button) view.findViewById(R.id.btn_quick_all);
        Button btnNone = (Button) view.findViewById(R.id.btn_quick_none);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.refreshMorpheme();
                getFragmentManager().popBackStack();
            }
        });

        btnA2N.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsAdapter.directChange(0, 13);
                settingsAdapter.notifyDataSetChanged();
            }
        });

        btnO2Z.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsAdapter.directChange(13,23);
                settingsAdapter.notifyDataSetChanged();
            }
        });

        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsAdapter.directChange(0,26);
                settingsAdapter.notifyDataSetChanged();
            }
        });

        btnNone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsAdapter.directChange(0,0);
                settingsAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

}
