package com.example.lol_history.parser;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.Iterator;

public class SpellParser {
    Context context;

    public SpellParser(Context context) {
        this.context = context;
    }

    public String getSpellName(int spellId) {
        String spellName = "";

        try {
            InputStream is = context.getAssets().open("spell.json");
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
                String spellValue = dataObject.getString(name);
                JSONObject spellObject = new JSONObject(spellValue);
                if (String.valueOf(spellId).equals(spellObject.getString("key"))) {
                    spellName = name;
                    break;
                }
            }
        } catch (Exception e) {
            Log.d("TESTLOG", "[getSpellName] exception: " + e);
            e.printStackTrace();
        }

        return spellName;
    }
}
