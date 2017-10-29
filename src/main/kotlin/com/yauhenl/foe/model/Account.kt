package com.yauhenl.foe.model

import org.json.JSONArray
import org.json.JSONObject

class Account: Model() {
    var data: JSONObject = JSONObject()

    fun getData(requestId: Int): JSONArray = getPayload(requestId, "getData", emptyArray(), "StartupService")
}