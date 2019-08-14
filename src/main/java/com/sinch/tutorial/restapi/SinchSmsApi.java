package com.sinch.tutorial.restapi;

import com.clxcommunications.xms.ApiConnection;
import com.clxcommunications.xms.ApiException;
import com.clxcommunications.xms.ClxApi;
import com.clxcommunications.xms.api.MtBatchSmsResult;
import com.clxcommunications.xms.api.ReportType;
import com.sinch.tutorial.ngrok.Ngrok;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;

@Component
public class SinchSmsApi {
    private static final Logger LOG = Logger.getLogger(SinchSmsApi.class.getName());

    private final URI publicCallbackUri;
    private ApiConnection connection;

    public SinchSmsApi(@Value("#{ngrok.publicUri}") URI publicCallbackUri,
                       @Value("${sinch.api.token}") String token,
                       @Value("${sinch.api.servicePlanId}") String servicePlanId) {
        this.publicCallbackUri = publicCallbackUri;

        this.connection = ApiConnection.builder()
                .token(token)
                .servicePlanId(servicePlanId)
                .build();

        LOG.info(String.format("Using service plan [%s] to communicate with the Sinch REST API.", servicePlanId));
    }

    public MtBatchSmsResult sendSms(String message, String... recipients) throws ApiException, InterruptedException {
        return this.connection.createBatch(ClxApi.batchTextSms()
                .sender("ignored")
                .body(message)
                .addRecipient(recipients)
                .deliveryReport(ReportType.SUMMARY)
                .callbackUrl(publicCallbackUri.resolve("/sms/deliveryReport"))
                .build());
    }

    @PostConstruct
    public void connect() {
        connection.start();
    }

    @PreDestroy
    public void disconnect() throws IOException {
        connection.close();
    }
}
