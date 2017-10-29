package com.yauhenl.foe.model

import org.json.JSONArray
import org.json.JSONObject

abstract class Model {
    fun getPayload(requestId: Int, method: String, data: Array<String>, klass: String = "None"): JSONArray {
        val result = JSONArray()
        val payload = JSONObject()
        payload.put("requestId", requestId)
        payload.put("__class__", "ServerRequest")
        payload.put("requestClass", klass)
        payload.put("requestData", data)
        payload.put("requestMethod", method)
        payload.put("voClassName", "ServerRequest")
        result.put(payload)
        return result
    }
}