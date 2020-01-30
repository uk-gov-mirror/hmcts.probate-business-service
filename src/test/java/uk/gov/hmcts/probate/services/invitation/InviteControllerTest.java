package uk.gov.hmcts.probate.services.invitation;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.probate.services.idgeneration.IdGeneratorService;
import uk.gov.hmcts.probate.services.invitation.controllers.InvitationController;
import uk.gov.hmcts.reform.probate.model.multiapplicant.Invitation;
import uk.gov.service.notify.NotificationClientException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InviteControllerTest {

    @InjectMocks
    private InvitationController invitationController;

    @Mock
    private IdGeneratorService idGeneratorService;

    @Mock
    private InvitationService invitationService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    BindingResult mockBindingResult;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldSendInvitationAndGenerateId() throws UnsupportedEncodingException, NotificationClientException {

        Invitation invitation = setUpInvitationMock();

        invitationController.invite(invitation, mockBindingResult, "");

        verify(invitationService).sendEmail("1233445", invitation, Boolean.FALSE);
    }

    @Test
    public void shouldReSendInvitation() throws UnsupportedEncodingException, NotificationClientException {

        Invitation invitation = Invitation.builder().firstName("firstName").lastName("lastName").build();

        invitationController.invite("1233445", invitation, mockBindingResult, "");

        verify(invitationService).sendEmail("1233445", invitation, Boolean.FALSE);
    }

    @Test
    public void shouldSendInvitation() throws UnsupportedEncodingException, NotificationClientException {

        Invitation invitation = setUpInvitationMock();

        invitationController.invite( invitation, mockBindingResult, "");

        verify(invitationService).sendEmail("1233445", invitation, Boolean.FALSE);
    }

    @NotNull
    private Invitation setUpInvitationMock() throws UnsupportedEncodingException {
        Invitation invitation = Invitation.builder().firstName("firstName").lastName("lastName").build();
        Map<String, String> data = new HashMap<>();
        data.put("firstName", invitation.getFirstName());
        data.put("lastName", invitation.getLastName());
        when(invitationService.decodeURL(invitation)).thenReturn(invitation);
        when(idGeneratorService.generate(data)).thenReturn("1233445");
        return invitation;
    }

    @Test
    public void shouldSendInvitationWithId() throws UnsupportedEncodingException, NotificationClientException {

        Invitation invitation = setUpInvitationMock();
        invitationController.invite( invitation, mockBindingResult, "");

        verify(invitationService).sendEmail("1233445", invitation, Boolean.FALSE);
    }

    @Test
    public void shouldSendBilingualInvitation() throws UnsupportedEncodingException, NotificationClientException {

        Invitation invitation = Invitation.builder().firstName("firstName").lastName("lastName").build();

        invitationController.inviteBilingual("1233445", invitation, mockBindingResult, "");

        verify(invitationService).sendEmail("1233445", invitation, Boolean.TRUE);
    }

    @Test
    public void shouldSendBilingualInvitationWithId() throws UnsupportedEncodingException, NotificationClientException {

        Invitation invitation = setUpInvitationMock();

        invitationController.inviteBilingual("1233445", invitation, mockBindingResult, "");

        verify(invitationService).sendEmail("1233445", invitation, Boolean.TRUE);
    }
}
