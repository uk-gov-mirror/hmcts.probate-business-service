package uk.gov.hmcts.probate.services.invitation.unit;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.probate.services.businessvalidation.util.TestUtils;
import uk.gov.hmcts.probate.services.invitation.InvitationService;
import uk.gov.hmcts.reform.probate.model.multiapplicant.Invitation;

import java.io.UnsupportedEncodingException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InvitationServiceTest {

    public static final String ENCODED_INVITATION = "invitation/success.json";
    public static final String EXPECTED_DECODING = "invitation/expectedDecoding.json";
    public ObjectMapper objectMapper;
    @Autowired
    private TestUtils utils;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
    }

    @Test
    public void testInvitationDecoding() throws Exception, UnsupportedEncodingException {
        InvitationService invitationService = new InvitationService();

        Invitation encodedInvitation =
            objectMapper.readValue(utils.getJsonFromFile(ENCODED_INVITATION), Invitation.class);
        Invitation expectedDecoding =
            objectMapper.readValue(utils.getJsonFromFile(EXPECTED_DECODING), Invitation.class);

        Invitation decodedInvitation = invitationService.decodeURL(encodedInvitation);
        assertThat(decodedInvitation.getFirstName(), equalTo(expectedDecoding.getFirstName()));
        assertThat(decodedInvitation.getLastName(), equalTo(expectedDecoding.getLastName()));
        assertThat(decodedInvitation.getExecutorName(), equalTo(expectedDecoding.getExecutorName()));
        assertThat(decodedInvitation.getLeadExecutorName(), equalTo(expectedDecoding.getLeadExecutorName()));
    }
}
