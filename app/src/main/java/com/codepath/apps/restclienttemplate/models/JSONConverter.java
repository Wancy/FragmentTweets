package com.codepath.apps.restclienttemplate.models;

import com.raizlabs.android.dbflow.converter.TypeConverter;

import org.json.JSONException;
import org.json.JSONObject;


@com.raizlabs.android.dbflow.annotation.TypeConverter
public class JSONConverter extends TypeConverter<String, JSONObject> {

    @Override
    public String getDBValue(JSONObject model) {
        return model == null ? null : model.toString();
    }

    @Override
    public JSONObject getModelValue(String data) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}