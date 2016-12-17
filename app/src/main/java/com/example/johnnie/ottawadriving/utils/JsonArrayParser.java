package com.example.johnnie.ottawadriving.utils;

import android.util.Log;

import com.example.johnnie.ottawadriving.model.PersonModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Jiangqi on 2016-12-15.
 */

public class JsonArrayParser {




        // change getJSONFromUrl to return ArrayList<PersonModel>
        public static ArrayList<PersonModel> parsePersonModelFromJson(JSONArray jsonArray) {
            ArrayList<PersonModel> mModels = new ArrayList<PersonModel>();

            for (int i=0; i < jsonArray.length(); i++) {

                JSONObject json_data = null;
                try {
                    json_data = jsonArray.getJSONObject(i);
                    PersonModel resultRow = new PersonModel();
                    resultRow.setId(json_data.getInt("id"));
                    resultRow.setName(json_data.getString("name"));
                    resultRow.setAddress("Ottawa,Test Address,K2G 4L2");
                    resultRow.setEmail(json_data.getString("email"));
                    resultRow.setPhoneNumber(json_data.getString("phoneNumber"));
                    resultRow.setInformation(json_data.getString("title"));
                    resultRow.setImageUri("http://cdn-user.dealerrater.com/uimages/33553/main-33553-d0c01a7b23be.jpg");
                    mModels.add(resultRow);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                }

            return mModels;
        }
}
