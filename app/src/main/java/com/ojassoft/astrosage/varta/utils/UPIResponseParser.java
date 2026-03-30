package com.ojassoft.astrosage.varta.utils;

import android.content.Intent;
import android.os.Build;

import com.ojassoft.astrosage.varta.model.UPIResponse;

import java.util.HashMap;
import java.util.Map;

public class UPIResponseParser {

    public static UPIResponse parse(Intent data) {
        UPIResponse resp = new UPIResponse();

        if (data == null) {
            resp.status = "FAILURE";
            return resp; // user cancelled
        }

        String response = data.getStringExtra("response");
        if (response == null) {
            resp.status = "FAILURE";
            return resp;
        }

        Map<String, String> map = new HashMap<>();
        try {
            String[] pairs = response.split("&");
            for (String pair : pairs) {
                String[] parts = pair.split("=");
                if (parts.length >= 2) {
                    map.put(parts[0].trim().toLowerCase(), parts[1].trim());
                } else if (parts.length == 1) {
                    map.put(parts[0].trim().toLowerCase(), "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ✅ Use getOrDefault if available (API 24+), else fallback
        resp.status = getValue(map, "status").toUpperCase();
        resp.txnId = getValue(map, "txnId");
        resp.txnRef = getValue(map, "txnRef");
        resp.responseCode = getValue(map, "responseCode");

        return resp;
    }

    private static String getValue(Map<String, String> map, String key) {
        key = key.toLowerCase();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return map.getOrDefault(key, "");
        } else {
            return map.containsKey(key) ? map.get(key) : "";
        }
    }
}

