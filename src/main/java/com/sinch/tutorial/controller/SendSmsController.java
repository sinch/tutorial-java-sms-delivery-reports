package com.sinch.tutorial.controller;

import com.clxcommunications.xms.ApiException;
import com.clxcommunications.xms.api.MtBatchSmsResult;
import com.sinch.tutorial.restapi.SinchSmsApi;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendSmsController {
    private final SinchSmsApi sinch;

    public SendSmsController(SinchSmsApi sinch) {
        this.sinch = sinch;
    }

    @PostMapping("/sms/send")
    public MtBatchSmsResult send(@RequestBody SmsDetails details) throws InterruptedException, ApiException {
        return sinch.sendSms(details.message, details.recipients);
    }

    public static class SmsDetails {
        public String message;
        public String[] recipients;
    }
}
