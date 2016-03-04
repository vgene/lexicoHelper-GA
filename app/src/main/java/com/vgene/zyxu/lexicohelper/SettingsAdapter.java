package com.vgene.zyxu.lexicohelper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyxu on 3/1/16.
 */
public class SettingsAdapter extends BaseAdapter {

    private int listSize;
    private List<IndexEntity> indexEntities;
    private Context context;



    public SettingsAdapter(List<IndexEntity> indexEntities, Context context){
        this.indexEntities = indexEntities;
        this.context = context;
    }
    @Override
    public int getCount() {
        return indexEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return indexEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder=null;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listitem_settings,parent,false);
            holder = new ViewHolder(convertView);
            //set tag
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.setContent(position);

        return convertView;
    }

    public class ViewHolder{
        TextView tvSettings;
        Switch switchSettings;
        boolean checked=false;

        ViewHolder(View view){
            tvSettings= (TextView) view.findViewById(R.id.tv_settings);
            switchSettings = (Switch) view.findViewById(R.id.switch_settings);

            switchSettings.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (checked)
                        checked=false;
                    else
                        checked=true;
                }
            });
        }

        void setContent(final int position){

            String text = indexEntities.get(position).getName();
            int morphemeNumber=indexEntities.get(position).getEnd()-indexEntities.get(position).getStart();
            text=text+" ("+(morphemeNumber)+")";
            tvSettings.setText(text);
            switchSettings.setChecked(indexEntities.get(position).isSelected());

            switchSettings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    indexEntities.get(position).switchSelected();
                }
            });

        }
    }

    public void directChange(int start, int end){
        for (int i=0;i<indexEntities.size();i++)
            if (i>=start && i<end){
                indexEntities.get(i).setIsSelected(true);
            }

            else
                indexEntities.get(i).setIsSelected(false);
    }

}
