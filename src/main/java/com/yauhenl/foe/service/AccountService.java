package com.yauhenl.foe.service;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    private RequestService requestService;

    public List<Document> getData(String sid, String userKey) {
        JSONArray payload = getPayload("getData", new String[]{}, "StartupService");
        List<Document> data = requestService.postRequest(payload, sid, userKey);
//        val result = requestService.method(data, "getData") as HashMap<String, Any>
//                result.put("taverns", requestService.method(data, "getOtherTavernStates"))
//        result.put("resources", requestService.method(data, "getPlayerResources"))
        return data;
    }

    private JSONArray getPayload(String method, String[] data, String klass) {
        JSONArray result = new JSONArray();
        JSONObject payload = new JSONObject();
        payload.put("__class__", "ServerRequest");
        payload.put("requestClass", klass == null ? "None" : klass);
        payload.put("requestData", data);
        payload.put("requestMethod", method);
        payload.put("voClassName", "ServerRequest");
        result.put(payload);
        return result;
    }
}
