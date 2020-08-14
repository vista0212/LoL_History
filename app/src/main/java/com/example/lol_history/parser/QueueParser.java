package com.example.lol_history.parser;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

public class QueueParser {
    Context context;

    public QueueParser(Context context) {
        this.context = context;
    }

    public String getQueueType(int queueId) {
        String queueDescription = "";
        try {
            InputStream is = context.getAssets().open("queues.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.get("queueId").equals(queueId)) {
                    queueDescription = jsonObject.getString("description");
                    break;
                }
            }
        } catch (Exception e) {
            Log.d("TESTLOG", "[getQueueType] exception: " + e);
            e.printStackTrace();
        }

        return queueDescription;
    }
}
