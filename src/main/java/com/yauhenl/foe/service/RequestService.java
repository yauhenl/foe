package com.yauhenl.foe.service;

import org.bson.Document;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Stream;

import static java.lang.String.format;

@Service
public class RequestService {
    private static final Logger logger = LoggerFactory.getLogger(RequestService.class);
    private static final Random rnd = new Random();

    @Value("${game.server}")
    private String server;

    @Value("${game.secret}")
    private String secret;

    @Value("${game.timestamp}")
    private String timestamp;

    @Value("${game.version}")
    private String version;

    private Integer requestId = rnd.nextInt(255);

    public Document findByRequestMethod(List<Document> data, String mathodName) {
        return data.stream().filter(it -> mathodName.equals(it.getString("requestMethod"))).findAny().map(it -> it.get("responseData", Document.class)).orElse(null);
    }

    public List<Document> findListByRequestMethod(List<Document> data, String mathodName) {
        return data.stream().filter(it -> mathodName.equals(it.getString("requestMethod"))).findAny().map(it -> it.get("responseData", List.class)).orElse(null);
    }

    public List<Document> postRequest(JSONArray payload, String sid, String userKey) {
        String url = format("https://%s.forgeofempires.com/game/json?h=%s", server, userKey);
        payload.getJSONObject(0).put("requestId", requestId++);
        String body = getBody(payload);
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "*/*");
        headers.put("Accept-Encoding", "gzip, deflate, br");
        headers.put("Accept-Language", "en,ru;q=0.8");
        headers.put("Client-Identification", format("version=%s; requiredVersion=%s; platform=bro; platformVersion=web", version, version));
        headers.put("Connection", "keep-alive");
        headers.put("Content-Type", "application/json");
        headers.put("Cookie", format("portal_ref_session=1; metricsUvId=ffcf4c04-e10a-42a8-bc83-dadf16cc2c70; sid=%s; req_page_info=game_v1; start_page_type=game; start_page_version=v1; ref=daa_ru_cis; ig_conv_last_site=https://%s.forgeofempires.com/game/index", sid, server));
        headers.put("Host", format("%s.forgeofempires.com", server));
        headers.put("Origin", "https://foeru.innogamescdn.com");
        headers.put("Referer", format("https://foeru.innogamescdn.com/swf/Preloader.swf?%s/[[DYNAMIC]]/1", timestamp));
        headers.put("Signature", getSignature(body, userKey));
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
        headers.put("X-Requested-With", "ShockwaveFlash/27.0.0.183");
//        JSONArray result = post(url, headers, new HashMap<>(), body).getJsonArray();
        StringBuilder data = new StringBuilder();
        try {
            Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("response.json")).toURI());
            Stream<String> lines = Files.lines(path);
            lines.forEach(line -> data.append(line).append("\n"));
            lines.close();
        } catch (URISyntaxException | IOException e) {
            logger.error(e.getMessage());
        }
        JSONArray result = new JSONArray(data.toString().trim());
        List<Document> documents = new ArrayList<>();
        for (int i = 0; i < result.length(); i++) {
            documents.add(Document.parse(result.getJSONObject(i).toString()));
        }
        return documents;
    }

    private String getSignature(String body, String userKey) {
        String data = userKey + secret + body;
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            logger.error("NoSuchAlgorithmException", e);
        }
        byte[] digest = Objects.requireNonNull(md).digest(data.getBytes());
        return DatatypeConverter.printHexBinary(digest).substring(0, 10).toLowerCase();
    }

    private String getBody(JSONArray payload) {
        return payload.toString().replace(" ", "");
    }
}
