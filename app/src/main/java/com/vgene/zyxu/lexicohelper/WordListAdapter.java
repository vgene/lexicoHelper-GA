package com.vgene.zyxu.lexicohelper;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zyxu on 2/29/16.
 */
public class WordListAdapter extends BaseAdapter {

    private List<Pair<String,String>> examples;
    private Context context;
    public WordListAdapter(List<Pair<String,String>> examples, Context context){
        this.examples = examples;
        this.context = context;
    }

    @Override
    public int getCount() {
        return examples.size();
    }

    @Override
    public Object getItem(int position) {
        return examples.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.listitem_word_example,parent,false);

        TextView tvWord = (TextView) convertView.findViewById(R.id.tv_example_word);
        TextView tvExp = (TextView) convertView.findViewById(R.id.tv_example_word_meaning);

        tvWord.setText(examples.get(position).first);
        tvExp.setText(examples.get(position).second);

        return convertView;
    }
}
