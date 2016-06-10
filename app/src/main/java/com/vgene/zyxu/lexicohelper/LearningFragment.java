package com.vgene.zyxu.lexicohelper;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class LearningFragment extends Fragment{

    int seq;
    MorphemeEntity morpheme;
    ArrayList<MorphemeEntity> morphemeEntities;
    int stage=0;

    private final static int WORD_NUM = Constants.WORD_GROUP_COUNT;

    int countDownNum;
    int learnedNum;
    int totalLearningProgress;
    int learningProgress;

    TextView tvMorpheme;
    TextView tvExplanation;
    ListView lvExamples;
    TextView tvWordList;
    ProgressBar pbStatus;

    AppCompatButton btnRemember;
    AppCompatButton btnForgot;
    AppCompatButton btnNext;

    ImageView ivLogo;


    public LearningFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_learning, container, false);

        findWidgets(view);
        showNextButton();

        tvMorpheme.setVisibility(View.GONE);
        tvExplanation.setVisibility(View.GONE);

        countDownNum=MorphemeSelector.selectedNum;

        learnedNum=0;
        learningProgress=0;

        pbStatus.setMax(countDownNum);
        pbStatus.setProgress(learnedNum);


        if (morpheme!=null) {
            tvMorpheme.setText(morpheme.getMorpheme());
            tvExplanation.setText(morpheme.getExp_eng());
        }

        btnRemember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Snackbar.make(view,"Nice!还剩"+1+"次背熟",Snackbar.LENGTH_SHORT).show();
                updateMorphemeInfo(true);
                learningProgress++;
                pbStatus.setSecondaryProgress(learningProgress/3);

                tvMorpheme.setVisibility(View.VISIBLE);
                tvExplanation.setVisibility(View.VISIBLE);
                showListView();
                showNextButton();
                if (morpheme.doCountDown(1)) {
                    pbStatus.setProgress(++learnedNum);
                }
            }
        });

        btnRemember.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Snackbar.make(view, "真的背熟了吗?", Snackbar.LENGTH_LONG).setAction("真的", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvExplanation.setVisibility(View.VISIBLE);
                        showListView();
                        showNextButton();
                        learningProgress+=3;
                        morpheme.doCountDown(3);
                        pbStatus.setProgress(++learnedNum);
                        pbStatus.setSecondaryProgress(learningProgress/3);
                        Snackbar.make(view, "已标记为背熟，不会再出现", Snackbar.LENGTH_SHORT).show();
                    }
                }).show();
                return true;
            }
        });

        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateMorphemeInfo(false);
                tvMorpheme.setTextColor(getColor(getContext(),R.color.colorAccent));
                stage--;
                if (tvExplanation.getVisibility() == View.GONE) {
                    tvExplanation.setVisibility(View.VISIBLE);
                }
                if (stage <=0){
                    showListView();
                    showNextButton();
                    return;
                }
                //Snackbar.make(view, "又忘了!还剩7次背熟", Snackbar.LENGTH_SHORT).show();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnNext.setText("下一个");

//                ivLogo.setVisibility(View.GONE);
                hideNextButton();
                tvMorpheme.setVisibility(View.VISIBLE);
                tvExplanation.setVisibility(View.GONE);
                hideListView();

//                newMorpheme();
                newMorphemeFromList();
            }
        });

        return view;
    }

    @SuppressLint("WrongViewCast")
    private void findWidgets(View view) {
        pbStatus = (ProgressBar) view.findViewById(R.id.pb_status);
        btnRemember= (AppCompatButton) view.findViewById(R.id.btn_remember);
        btnForgot = (AppCompatButton) view.findViewById(R.id.btn_forgot);
        btnNext = (AppCompatButton) view.findViewById(R.id.btn_next);
        ivLogo = (ImageView)view.findViewById(R.id.iv_logo);
        tvMorpheme = (TextView) view.findViewById(R.id.tv_morpheme);
        tvExplanation = (TextView) view.findViewById(R.id.tv_exp_eng);
        lvExamples = (ListView) view.findViewById(R.id.lv_examples);
        tvWordList = (TextView) view.findViewById(R.id.tv_word_list);

        ColorStateList cslPrimary = ColorStateList.valueOf(getColor(getContext(),R.color.colorPrimary));
        ColorStateList cslAccent = ColorStateList.valueOf(getColor(getContext(),R.color.colorAccent));
        btnRemember.setSupportBackgroundTintList(cslPrimary);
        btnForgot.setSupportBackgroundTintList(cslAccent);
        btnNext.setSupportBackgroundTintList(cslPrimary);

        (view.findViewById(R.id.ll_btns)).bringToFront();
    }

    private void showNextButton() {
        btnRemember.setVisibility(View.GONE);
        btnForgot.setVisibility(View.GONE);
        btnNext.setVisibility(View.VISIBLE);
    }

    private void hideNextButton(){
        btnRemember.setVisibility(View.VISIBLE);
        btnForgot.setVisibility(View.VISIBLE);
        btnNext.setVisibility(View.GONE);
    }


    //return 实际上获取到的单词数
    private int getNewMorphemes(int wordNum){
//        morphemeEntities = MorphemeSelector.getRandomMorphemeList(wordNum);
        if ((morphemeEntities = WordSelector.getWordList(wordNum))==null)
            morphemeEntities  = MorphemeSelector.getRandomMorphemeList(wordNum);

        return  morphemeEntities.size();
    }

    private void newMorphemeFromList(){

        if ( morphemeEntities==null || morphemeEntities.size()<=0) {
            if (getNewMorphemes(WORD_NUM)>0) {
                showMorphemeList();
                return;
            }
        }

        //重新获得也没得到东西的话 给null
        if (morphemeEntities.size()>0) {
            morpheme = morphemeEntities.get(0);
            morphemeEntities.remove(0);
        }
        else
            morpheme = null;

        if (morpheme!=null) {
            tvMorpheme.setText(morpheme.getMorpheme());
            tvExplanation.setText(morpheme.getExp_eng());
            tvMorpheme.setTextColor(getColor(getContext(), R.color.colorPrimary));

            stage = 1;
            if (morpheme.isHasExamples()){
                stage = 2;
                WordListAdapter wordListAdapter = new WordListAdapter(morpheme.getExamples(),getContext());
                lvExamples.setAdapter(wordListAdapter);

            }
        }
        else{
            if (learnedNum>0 && learnedNum==countDownNum)
                tvMorpheme.setText("已学完，返回重新选择再次学习");
            else
                tvMorpheme.setText("未选择单词，请返回上层菜单重新选择");
            btnRemember.setVisibility(View.GONE);
            btnForgot.setVisibility(View.GONE);
        }
    }

    private void newMorpheme(){

        morpheme = MorphemeSelector.getRandomMorpheme();

        if (morpheme!=null) {
            tvMorpheme.setText(morpheme.getMorpheme());
            tvExplanation.setText(morpheme.getExp_eng());
            tvMorpheme.setTextColor(getColor(getContext(), R.color.colorPrimary));

            stage = 1;
            if (morpheme.isHasExamples()){
                stage = 2;
                WordListAdapter wordListAdapter = new WordListAdapter(morpheme.getExamples(),getContext());
                lvExamples.setAdapter(wordListAdapter);

            }
        }
        else{
            if (learnedNum>0 && learnedNum==countDownNum)
                tvMorpheme.setText("已学完，返回重新选择再次学习");
            else
                tvMorpheme.setText("未选择单词，请返回上层菜单重新选择");
            btnRemember.setVisibility(View.GONE);
            btnForgot.setVisibility(View.GONE);
        }

    }

    private void showListView(){
        if (morpheme.isHasExamples()) {
            lvExamples.setVisibility(View.VISIBLE);
        }
    }

    private void hideListView(){
        lvExamples.setVisibility(View.GONE);
    }

    public static final int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    private void updateMorphemeInfo(boolean isCorrect){
        morpheme.updateInfo(isCorrect);
    }

    private void showMorphemeList(){
        Log.d("LearningFragment:","showlist");

        tvWordList.setVisibility(View.VISIBLE);

        String strMorphemeList = morphemeEntities.get(0).getMorpheme()+'\n';

        for (int i =1; i< morphemeEntities.size();i++)
        {

            strMorphemeList = strMorphemeList+(morphemeEntities.get(i).getMorpheme()+'\n');
        }

        tvWordList.setTextColor(Color.BLUE);
        tvWordList.setTextSize(20);
        tvWordList.setText(strMorphemeList);

        showNextButton();
    }

    private void hideMorphemeList(){
        tvWordList.setVisibility(View.GONE);
    }
}
