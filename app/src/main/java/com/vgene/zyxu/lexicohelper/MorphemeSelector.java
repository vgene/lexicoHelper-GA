package com.vgene.zyxu.lexicohelper;

import android.util.Log;

import java.util.List;
import java.util.Random;

/**
 * Created by zyxu on 3/2/16.
 */
public class MorphemeSelector {

    static Random random = new Random(System.nanoTime());

    public static int selectedNum = 0;
    public static MorphemeEntity getRandomMorpheme(){

        List<MorphemeEntity> morphemeEntities = MainActivity.getMorphemeEntities();


        if (selectedNum<=0)
            return null;
        int availWordSeq;

        if (selectedNum>1)
            availWordSeq= random.nextInt(selectedNum)+1;
        else
            availWordSeq = 1;

        int wordSeq=0;
        while(availWordSeq>0){
            if (morphemeEntities.get(wordSeq).isSelected()){
                availWordSeq--;
            }
            if (availWordSeq>0)
                wordSeq++;
        }

        return morphemeEntities.get(wordSeq);
    }

}
