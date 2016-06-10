package com.vgene.zyxu.lexicohelper;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
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

    public static ArrayList<MorphemeEntity> getRandomMorphemeList(int num){
        List<MorphemeEntity> morphemeEntities = MainActivity.getMorphemeEntities();
        ArrayList<MorphemeEntity> returnEntities = new ArrayList<>();
        HashSet<Integer> hashSet = new HashSet<>();
        int size = morphemeEntities.size();
        int[] availSeqs = new int[size];
        Object[] values;

        int pos=0;
        for (int i=0;i<size;i++){
            if (morphemeEntities.get(i).isSelected()){
                availSeqs[pos++]=i;
            }
        }

        if (pos>num) {
            while (hashSet.size() < num) {
                hashSet.add(random.nextInt(pos));
            }
            values = hashSet.toArray();

            for (int i = 0; i < num; i++)
                returnEntities.add(morphemeEntities.get(availSeqs[(int) values[i]]));
        }
        else{
            for (int i=0;i<pos;i++)
                returnEntities.add(morphemeEntities.get(availSeqs[i]));
        }

        return returnEntities;
    }

}
