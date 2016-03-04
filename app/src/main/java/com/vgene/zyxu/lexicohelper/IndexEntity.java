package com.vgene.zyxu.lexicohelper;

import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;

/**
 * Created by zyxu on 3/1/16.
 */
public class IndexEntity {

    private boolean isValid=false;
    private String name;
    private int start;
    private int end;
    private boolean isSelected=false;

    public IndexEntity(){

    }

    public IndexEntity(String name, int start, int end){
        this.name = name;
        this.start = start;
        this.end = end;
    }

    public IndexEntity(JsonReader reader) throws IOException {

        isValid=false;
        name=null;
        start=-1;
        end=-1;

        reader.beginObject();
        while (reader.hasNext()) {
            String name2 = reader.nextName();
            if (name2.equals("name")) {
                name = reader.nextString();
            } else if (name2.equals("start")) {
                start = reader.nextInt();
            }else if (name2.equals("end")){
                end = reader.nextInt();
            }else{
                reader.skipValue();
            }
        }

        if (name!=null && !name.isEmpty() && start>0 && end>0){
            isValid=true;
        }

        reader.endObject();
    }

    public String getName() {
        return name;
    }

    public int getEnd() {
        return end;
    }

    public int getStart() {
        return start;
    }

    public boolean isValid() {
        return isValid;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public void switchSelected(){
        isSelected = !isSelected;
        Log.d(name,isSelected+"");
    }
}
