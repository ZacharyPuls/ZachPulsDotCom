package com.zachpuls.website.mail;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by zachpuls on 5/26/2016.
 */

public class SesEmailTest {

    private static final String DEFAULT_FROM_ADDRESS = "undisclosed@zachpuls.com";
    private static final String TEST_FROM_ADDRESS = "from@zachpuls.com";

    private static final String TEST_TO_ADDRESS = "to@zachpuls.com";
    private static final String TEST_CC_ADDRESS = "cc@zachpuls.com";
    private static final String TEST_BCC_ADDRESS = "bcc@zachpuls.com";

    private static final String DEFAULT_SUBJECT = "No Subject";
    private static final String TEST_SUBJECT = "test-subject";

    private static final String DEFAULT_BODY = "Empty Message";
    private static final String TEST_BODY = "test-body";

    @Test
    public void testDefaultFromAddressIsUnknown() {
        assertEquals(DEFAULT_FROM_ADDRESS, new SesEmail().fromAddress);
    }

    @Test
    public void testSetFromAddress() {
        assertTrue(getTestSesEmail().fromAddress.equals(TEST_FROM_ADDRESS));
    }

    @Test
    public void testAddToAddress() {
        assertTrue(getTestSesEmail().toAddresses.contains(TEST_TO_ADDRESS));
    }

    @Test
    public void testAddCcAddress() {
        assertTrue(getTestSesEmail().ccAddresses.contains(TEST_CC_ADDRESS));
    }

    @Test
    public void testAddBccAddress() {
        assertTrue(getTestSesEmail().bccAddresses.contains(TEST_BCC_ADDRESS));
    }

    @Test
    public void testDefaultSubjectIsNone() {
        assertEquals(DEFAULT_SUBJECT, new SesEmail().subject);
    }

    @Test
    public void testSetSubject() {
        assertTrue(getTestSesEmail().subject.equals(TEST_SUBJECT));
    }

    @Test
    public void testDefaultBodyIsEmpty() {
        assertEquals(DEFAULT_BODY, new SesEmail().body);
    }

    @Test
    public void testSetBody() {
        assertTrue(getTestSesEmail().body.equals(TEST_BODY));
    }

    @Test
    public void testSend() {
        SesEmail sesEmail = getTestSesEmail();
        sesEmail.sesClient = mock(AmazonSimpleEmailServiceClient.class);
        when(sesEmail.sesClient.sendEmail(anyObject())).thenReturn(null);
        sesEmail.send();
        verify(sesEmail.sesClient);
    }

    @Test
    public void testConstructMessageSubject() {
        assertEquals(TEST_SUBJECT, getTestMessage().getSubject().getData());
    }

    @Test
    public void testConstructMessageBody() {
        assertEquals(TEST_BODY, getTestMessage().getBody().getText().getData());
    }

    private Message getTestMessage() {
        return getTestSesEmail().constructMessage();
    }

    @Test
    public void testConstructSendEmailRequestFromAddress() {
        assertEquals(TEST_FROM_ADDRESS, getTestSendEmailRequest().getSource());
    }

    @Test
    public void testConstructSendEmailRequestToAddress() {
        assertTrue(getTestSendEmailRequest().getDestination().getToAddresses().contains
                (TEST_TO_ADDRESS));
    }

    @Test
    public void testConstructSendEmailRequestCcAddress() {
        assertTrue(getTestSendEmailRequest().getDestination().getCcAddresses().contains
                (TEST_CC_ADDRESS));
    }

    @Test
    public void testConstructSendEmailRequestBccAddress() {
        assertTrue(getTestSendEmailRequest().getDestination().getBccAddresses().contains
                (TEST_BCC_ADDRESS));
    }

    @Test
    public void testConstructSendEmailRequestSubject() {
        assertEquals(TEST_SUBJECT, getTestSendEmailRequest().getMessage().getSubject().getData());
    }

    @Test
    public void testConstructSendEmailRequestBody() {
        assertEquals(TEST_BODY, getTestSendEmailRequest().getMessage().getBody().getText()
                .getData());
    }

    private SendEmailRequest getTestSendEmailRequest() {
        return getTestSesEmail().constructSendEmailRequest(getTestMessage());
    }

    private SesEmail getTestSesEmail() {
        return new SesEmail().withFromAddress(TEST_FROM_ADDRESS).withToAddress(TEST_TO_ADDRESS)
                .withCcAddress(TEST_CC_ADDRESS).withBccAddress(TEST_BCC_ADDRESS)
                .withSubject(TEST_SUBJECT).withBody(TEST_BODY);
    }
}
