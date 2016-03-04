package com.vgene.zyxu.lexicohelper;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static List<MorphemeEntity> morphemeEntities;
    private static int listSize;

    private static List<IndexEntity> indexEntities;
    SettingFragment settingFragment = new SettingFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StartFragment startFragment = new StartFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.main_fragment,startFragment).commit();

        loadIndex();
        loadMorpheme();

    }


    static public MorphemeEntity getMorphemeEntityById(int id){
        if (id<listSize)
            return morphemeEntities.get(id);
        else
            return null;
    }

    static public List<MorphemeEntity> getMorphemeEntities(){
        return morphemeEntities;
    }
    static public int getListSize(){
        return listSize;
    }

    private void loadIndex() {
        indexEntities = new ArrayList<>();

        InputStream jsonIsIndex = getResources().openRawResource(R.raw.data_index);
        JsonReader reader = new JsonReader(new InputStreamReader(jsonIsIndex));

        try{
            reader.beginArray();
            while (reader.hasNext()){
                indexEntities.add(new IndexEntity(reader));
            }
            reader.endArray();
        }catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("index size", indexEntities.size()+"");

    }

    private void loadMorpheme(){
        morphemeEntities = new ArrayList<>();
        InputStream jsonIsWords = getResources().openRawResource(R.raw.data_words);

        JsonReader reader = new JsonReader(new InputStreamReader(jsonIsWords));

        try {
            morphemeEntities = new ArrayList<MorphemeEntity>();
            reader.beginArray();
            while (reader.hasNext()){
                morphemeEntities.add(new MorphemeEntity(reader));
            }
            reader.endArray();

        } catch (IOException e) {
            e.printStackTrace();
        }

        listSize = morphemeEntities.size();

        Log.d("listsize",listSize+"");
    }

    public static void refreshMorpheme(){

        int size = indexEntities.size();
        int selectedNum = 0;

        for (int i=0; i<size;i++){
            IndexEntity indexEntity = indexEntities.get(i);
            int start=indexEntity.getStart();
            int end = indexEntity.getEnd();
            boolean isSelected = indexEntity.isSelected();
            for (int j=start; j<end;j++){
                morphemeEntities.get(j).setIsSelected(isSelected);
                selectedNum += isSelected?1:0;
            }
        }

        MorphemeSelector.selectedNum=selectedNum;

    }

    public void showFragment(){
        LearningFragment learningFragment = new LearningFragment();
        getSupportFragmentManager().beginTransaction().addToBackStack("back").add(R.id.main_fragment, learningFragment).commit();
    }

    public void showSettings(){
        getSupportFragmentManager().beginTransaction().add(R.id.main_fragment, settingFragment).commit();
    }

    public void hideSettings(){
        getSupportFragmentManager().beginTransaction().hide(settingFragment).commit();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    public static List<IndexEntity> getIndexEntities() {
        return indexEntities;
    }
}
