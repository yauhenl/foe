package com.yauhenl.foe.service

import com.yauhenl.foe.model.Account
import khttp.post
import org.json.JSONArray
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.MessageDigest
import javax.xml.bind.DatatypeConverter
import org.apache.tomcat.jni.User.username
import org.json.JSONObject


@Service
class RequestService(
        @Value("\${game.server}") val server: String,
        @Value("\${game.secret}") val secret: String,
        @Value("\${login.user_key}") val userKey: String,
        @Value("\${login.sid}") val sid: String,
        @Value("\${game.timestamp}") val timestamp: String,
        @Value("\${game.version}") val version: String) {

    private var requestId = 0

    fun initialize() {
        val account = Account()
        val data = postRequest(account.getData(requestId++))
        account.data = service(data, "StartupService")
        account.data.put("taverns", method(data, "getOtherTavernStates"))
        account.data.put("resources", method(data, "getPlayerResources"))
    }

    fun service(data: JSONArray, service: String): JSONObject {
        (0..(data.length() - 1))
                .map { data.getJSONObject(it) }
                .filter { it.getString("requestClass") == service }
                .first { return it.getJSONObject("responseData") }
        return JSONObject()
    }

    fun method(data: JSONArray, method: String): JSONArray {
        (0..(data.length() - 1))
                .map { data.getJSONObject(it) }
                .filter { it.getString("requestMethod") == method }
                .first { return it.getJSONArray("responseData") }
        return JSONArray()
    }

    fun postRequest(payload: JSONArray): JSONArray {
        val url = "https://${server}.forgeofempires.com/game/json?h=${userKey}"
        val body = getBody(payload)
        val headers = hashMapOf(
                "Accept" to "*/*",
                "Accept-Encoding" to "gzip, deflate, br",
                "Accept-Language" to "en,ru;q=0.8",
                "Client-Identification" to "version=${version}; requiredVersion=${version}; platform=bro; platformVersion=web",
                "Connection" to "keep-alive",
                "Content-Type" to "application/json",
                "Cookie" to "portal_ref_session=1; metricsUvId=ffcf4c04-e10a-42a8-bc83-dadf16cc2c70; sid=${sid}; req_page_info=game_v1; start_page_type=game; start_page_version=v1; ref=daa_ru_cis; ig_conv_last_site=https://${server}.forgeofempires.com/game/index",
                "Host" to "${server}.forgeofempires.com",
                "Origin" to "https://foeru.innogamescdn.com",
                "Referer" to "https://foeru.innogamescdn.com/swf/Preloader.swf?${timestamp}/[[DYNAMIC]]/1",
                "Signature" to getSignature(body),
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36",
                "X-Requested-With" to "ShockwaveFlash/27.0.0.183")
        return post(url, headers, data = body).jsonArray
    }

    fun getSignature(body: String): String {
        val data = userKey + secret + body
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(data.toByteArray())
        return DatatypeConverter.printHexBinary(digest).take(10).toLowerCase()
    }

    fun getBody(payload: JSONArray): String {
        return payload.toString().replace(" ", "")
    }
}
