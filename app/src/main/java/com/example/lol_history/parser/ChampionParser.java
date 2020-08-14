package com.example.lol_history.parser;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.Iterator;

public class ChampionParser {
    Context context;
    public ChampionParser(Context context) {
        this.context = context;
    }

    public String getChampionName(int championId) {
        String championName = "";
        try {
            InputStream is = context.getAssets().open("champion.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            JSONObject jsonObject = new JSONObject(json);
            String dataValue = jsonObject.getString("data");

            JSONObject dataObject = new JSONObject(dataValue);
            Iterator iterator = dataObject.keys();

            while (iterator.hasNext()) {
                String name = iterator.next().toString();
                String championValue = dataObject.getString(name);
                JSONObject championObject = new JSONObject(championValue);
                if (String.valueOf(championId).equals(championObject.getString("key"))) {
                    championName = name;
                    break;
                }
            }

        } catch (Exception e) {
            Log.d("TESTLOG", "[getChampionName] exception: " + e);
            e.printStackTrace();
        }

        return championName;
    }
}
