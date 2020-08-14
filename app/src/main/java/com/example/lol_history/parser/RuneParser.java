package com.example.lol_history.parser;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

public class RuneParser {
    Context context;

    public RuneParser(Context context) {
        this.context = context;
    }

    public String getRuneIcon(int runeId) {
        String icon = "";
        try {
            InputStream is = context.getAssets().open("runes.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject mainObject = jsonArray.getJSONObject(i);
                if (mainObject.get("id").equals(runeId)) {
                    icon = mainObject.getString("icon");
                    break;
                } else {
                    String slots = mainObject.getString("slots");
                    JSONArray slotsArray = new JSONArray(slots);
                    JSONObject slotObject = slotsArray.getJSONObject(0);

                    String runes = slotObject.getString("runes");
                    JSONArray runesArray = new JSONArray(runes);
                    for (int j = 0; j < runesArray.length(); j++) {
                        JSONObject runeObject = runesArray.getJSONObject(j);
                        if (runeObject.get("id").equals(runeId)) {
                            icon = runeObject.getString("icon");
                            break;
                        }
                    }
                }
            }

        } catch (Exception e) {
            Log.d("TESTLOG", "[getRuneIcon] exception: " + e);
            e.printStackTrace();
        }

        return icon;
    }
}
