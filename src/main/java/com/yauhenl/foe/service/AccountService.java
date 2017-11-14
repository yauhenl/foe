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

    public Document getData(String sid, String userKey) {
        JSONArray payload = getPayload("getData", new String[]{}, "StartupService");
        List<Document> data = requestService.postRequest(payload, sid, userKey);
        Document result = new Document();
        result.put("user_data", requestService.findByRequestMethod(data, "getData").get("user_data"));
        result.put("resources", requestService.findByRequestMethod(data, "getPlayerResources").get("resources"));
        result.put("taverns", requestService.findListByRequestMethod(data, "getOtherTavernStates"));
        return result;
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
