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


    private int id;
    private long lastStuTime;//上次学习时间
    private int addedTimes = 0;//累计学习次数
    private int correctTimes = 0;
    private int wrongTimes = 0;
    private int proDiffDeg;//专家难度等级
    private double userDiffDeg;//用户难度等级（＝忘记次数/（忘记次数＋记得次数））
    private boolean isStudied = false;//该单词是否已经出现过
    private double fitness;//单词的适应度(=c1*ProDiffDeg+c2*UserDiffDeg+?????)
    private double b = Constants.B_DEFAULT;//该单词对该用户来说的衰减系数。初始值为0.155
    private int sumofb;//用来更新b的一个中间量
    private int selfValue;//用户自己评价的记忆量
    private int memory=0;//计算的出的记忆量
    private int connection;//用来表示该单词与每次入选的同组的单词之间的联系程度，是一个临时量，随着组的不同会变化


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

    public double getFitness() {

        double fitness;

//        fitness = morpheme.length();
        //return 0;


        if (isStudied) {
            int temp = (int) ((System.currentTimeMillis() / 1000 - lastStuTime) / b);

            if (temp < 5000)
                memory = 1000;
            else if (temp < 50000)
                memory = 300;
            else
                memory = 100;
        }else{
            memory = 0;
        }
        fitness = -Constants.C1*userDiffDeg + Constants.C2*memory - Constants.C3*proDiffDeg;

        return  fitness;
    }

    public void updateInfo(boolean isCorrect){
        lastStuTime = System.currentTimeMillis()/1000;
        isStudied = true;

        addedTimes++;
        if (isCorrect)
            correctTimes++;
        else
            wrongTimes++;

        userDiffDeg = wrongTimes/addedTimes;
    }

    @Override
    public String toString() {
        return morpheme;
    }
}
