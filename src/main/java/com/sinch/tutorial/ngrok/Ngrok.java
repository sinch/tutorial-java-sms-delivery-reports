package com.sinch.tutorial.ngrok;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@Component("ngrok")
public class Ngrok {
    private static final Logger LOG = Logger.getLogger(Ngrok.class.getName());

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${ngrok.api.host}")
    private String host;
    @Value("${ngrok.api.port}")
    private String port;

    private URI publicUri;

    @PostConstruct
    public void prefetchPublicUri() {
        try {
            publicUri = Objects.requireNonNull(restTemplate.getForObject(tunnels(), Tunnels.class)).tunnels.stream()
                    .filter(tunnel -> "https".equals(tunnel.proto))
                    .map(tunnel -> tunnel.publicUrl)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Could not find suitable HTTPS tunnel from Ngrok"));

            LOG.info(String.format("Callbacks for delivery reports and inbound messages will come through Ngrok at URL [%s]", publicUri));
        } catch (ResourceAccessException e) {
            throw new RuntimeException("Could not obtain public URI from Ngrok. " +
                    "Please make sure Ngrok is running and its API is reachable at " + tunnels(), e.getCause());
        }
    }

    public URI getPublicUri() {
        return publicUri;
    }

    private URI tunnels() {
        return URI.create(String.format("http://%s:%s", host, port)).resolve("/api/tunnels");
    }

    public static class Tunnels {
        public List<Tunnel> tunnels = new ArrayList<>();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Tunnel {
        @JsonProperty("public_url")
        public URI publicUrl;
        @JsonProperty("proto")
        public String proto;
    }
}
