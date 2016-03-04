package com.vgene.zyxu.lexicohelper;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyxu on 2/29/16.
 */
public class MorphemeEntity {
    int INITIAL_COUNT_DOWN=3;

    String morpheme;
    String exp_eng;
    String exp_ch;
    boolean hasExamples=false;
    List<Pair<String,String>> examples;
    boolean isSelected=false;
    int countDown;
    public MorphemeEntity(){

    }


    public MorphemeEntity(JsonReader reader)throws IOException{

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("name")) {
                morpheme = reader.nextString();
            } else if (name.equals("explanation")) {
                exp_eng = reader.nextString();
            } else if (name.equals("examples")){

                //读取例子

                hasExamples = true;
                reader.beginArray();

                examples = new ArrayList<>();
                String word=null;
                String explanation=null;

                while (reader.hasNext()) {
                    reader.beginObject();

                    while (reader.hasNext()) {
                        String name2 = reader.nextName();
                        if (name2.equals("word")) {
                            word = reader.nextString();
                        } else if (name2.equals("explanation")) {
                            explanation = reader.nextString();
                        }
                    }
                    if (word != null && explanation != null && !word.isEmpty() && !explanation.isEmpty())
                        examples.add(new Pair<String, String>(word, explanation));
                    reader.endObject();
                }

                reader.endArray();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
    }

    public String getMorpheme() {
        return morpheme;
    }

    public String getExp_eng() {
        return exp_eng;
    }

    public List<Pair<String, String>> getExamples() {
        return examples;
    }

    public boolean isHasExamples(){
        return  hasExamples;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
        this.countDown=INITIAL_COUNT_DOWN;
    }

    public boolean doCountDown(int minus){
        if (countDown>0)
            countDown-=minus;
        if (countDown<=0) {
            isSelected = false;
            MorphemeSelector.selectedNum--;
            return true;
        }
        return false;
    }
}
