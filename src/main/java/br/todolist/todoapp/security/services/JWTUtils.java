package br.todolist.todoapp.security.services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.todolist.todoapp.Env;
import br.todolist.todoapp.model.Client;

@Service
public class JWTUtils {
    @Autowired
    Env env;

    public String buildJwtToken(Client client) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(env.getJWTSECRET());

            return JWT
                    .create()
                    .withIssuer("auth-api")
                    .withPayload(Map.of("id", client.getId(), "email", client.getEmail()))
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException error) {
            return "";
        }
    }

    public String decodeToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(env.getJWTSECRET());
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getPayload();
        } catch (JWTVerificationException exception) {
            return "";
        }
    }

    public Map<String, String> decodePayloadToMap(String base64) throws JsonProcessingException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = new String(Base64.getDecoder().decode(base64));

        JsonNode jsonNode = mapper.readTree(json);

        return Map.of("email", jsonNode.get("email").asText(), "id", jsonNode.get("id").asText());
    }

    public Instant genExpirationDate() {
        return LocalDateTime.now().plusWeeks(1).toInstant(ZoneOffset.of("-03:00"));
    }
}
