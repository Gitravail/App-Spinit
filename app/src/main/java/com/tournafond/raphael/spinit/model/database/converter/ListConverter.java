package com.tournafond.raphael.spinit.model.database.converter;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ListConverter {
    @TypeConverter
    public ArrayList<String> JSONStringToArrayList(String value) {
        return new Gson().fromJson(value, ArrayList.class);
    }

    @TypeConverter
    public String ArrayListToJSONString(ArrayList<String> liste) {
        return new Gson().toJson(liste);
    }
}
