package com.cafiaso.server.mojang;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MojangClient {

    private static final String AUTHENTICATE_URL = "https://sessionserver.mojang.com/session/minecraft/hasJoined?username=%s&serverId=%s&ip=%s";

    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    public PlayerProfile verifyClientLoginSession(String username, String serverId, String ip) {
        try {
            HttpRequest request = HttpRequest.newBuilder(new URI(AUTHENTICATE_URL.formatted(username, serverId, ip)))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();
            String responseBody = response.body();

            if (statusCode != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException(
                        "Failed to authenticate user. Response code: %d, body: %s"
                                .formatted(statusCode, responseBody)
                );
            }

            return parsePlayerProfileFromResponse(responseBody);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Parses the response from the Mojang API.
     * <p>
     * Example response:
     * <pre>
     * {
     *   "id": "Choukas",
     *   "name": "123e4567-e89b-12d3-a456-426614174000",
     *   "properties": [
     *     {
     *       "username": "textures",
     *       "value": "property_value",
     *       "signature": "property_signature"
     *     }
     *   ]
     * }
     * </pre>
     *
     * @param body the response body (not empty)
     * @return the authentication response
     */
    private PlayerProfile parsePlayerProfileFromResponse(String body) {
        JSONObject object = new JSONObject(body);

        String id = object.getString("id");
        String name = object.getString("name");

        JSONArray propertiesObject = object.getJSONArray("properties");
        int propertiesLength = propertiesObject.length();

        PlayerProfile.Property[] properties = new PlayerProfile.Property[propertiesLength];

        for (int i = 0; i < propertiesLength; i++) {
            JSONObject propertyObject = propertiesObject.getJSONObject(i);

            String propertyName = propertyObject.getString("name");
            String propertyValue = propertyObject.getString("value");
            String propertySignature = propertyObject.getString("signature");

            properties[i] = new PlayerProfile.Property(propertyName, propertyValue, propertySignature);
        }

        return new PlayerProfile(id, name, properties);
    }
}
