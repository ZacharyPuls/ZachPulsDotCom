package com.zachpuls.website.mail;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by zachpuls on 5/26/2016.
 */

public class SesEmail {

    String fromAddress = "undisclosed@zachpuls.com";
    List<String> toAddresses = new ArrayList<>();
    List<String> ccAddresses = new ArrayList<>();
    List<String> bccAddresses = new ArrayList<>();

    String subject = "No Subject";
    String body = "Empty Message";

    AmazonSimpleEmailServiceClient sesClient;

    public SesEmail() {

        Properties properties = new Properties();

        try {
            properties.load(getClass().getResourceAsStream("/WEB-INF/ses-client-credentials" +
                    ".properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        sesClient = new AmazonSimpleEmailServiceClient(new BasicAWSCredentials(properties
                .getProperty("accessKey"), properties.getProperty("secretKey")))
                .withRegion(Region.getRegion(Regions.US_WEST_2));
    }

    public SesEmail withFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
        return this;
    }

    public SesEmail withToAddress(String toAddress) {
        toAddresses.add(toAddress);
        return this;
    }

    public SesEmail withCcAddress(String ccAddress) {
        ccAddresses.add(ccAddress);
        return this;
    }

    public SesEmail withBccAddress(String bccAddress) {
        bccAddresses.add(bccAddress);
        return this;
    }

    public SesEmail withSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public SesEmail withBody(String body) {
        this.body = body;
        return this;
    }

    public void send() {
        SendEmailRequest sendEmailRequest = constructSendEmailRequest(constructMessage());
        sesClient.sendEmail(sendEmailRequest);
    }

    Message constructMessage() {
        Content subjectContent = new Content().withData(subject);
        Content bodyContent = new Content().withData(body);
        Body bodyText = new Body().withText(bodyContent);

        return new Message().withSubject(subjectContent).withBody(bodyText);
    }

    SendEmailRequest constructSendEmailRequest(Message message) {
        Destination destination = new Destination().withToAddresses(toAddresses)
                .withCcAddresses(ccAddresses).withBccAddresses(bccAddresses);
        return new SendEmailRequest().withSource(fromAddress)
                .withDestination(destination).withMessage(message);
    }
}
