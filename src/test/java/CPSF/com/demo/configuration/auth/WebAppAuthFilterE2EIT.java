package CPSF.com.demo.configuration.auth;

import CPSF.com.demo.BaseE2E;
import CPSF.com.demo.model.constant.TaskType;
import CPSF.com.demo.model.constant.UserRole;
import CPSF.com.demo.model.entity.Organisation;
import CPSF.com.demo.model.entity.User;
import CPSF.com.demo.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.UUID;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * End-to-end coverage of {@link CPSF.com.demo.service.auth.WebAppAuthFilter}: which header
 * combinations authenticate a web-app API-key client on {@code POST /web/reservation/**} and which
 * are rejected, plus the rule that a {@code WEB_APP} principal cannot reach staff endpoints.
 */
class WebAppAuthFilterE2EIT extends BaseE2E {

    @Autowired
    private TaskRepository taskRepository;

    private RestTestClient.ResponseSpec verifyRequest(Consumer<HttpHeaders> headers) {
        return client.post()
                .uri("/web/reservation/verify/{targetId}", UUID.randomUUID().toString())
                .headers(headers)
                .exchange();
    }

    private Consumer<HttpHeaders> credentials(String orgId, String apiKey) {
        return h -> {
            if (orgId != null) {
                h.add("X-org-id", orgId);
            }
            if (apiKey != null) {
                h.add("X-api-key", apiKey);
            }
        };
    }

    @Test
    void rejectsRequestWithoutAnyCredentialHeaders() {
        verifyRequest(credentials(null, null)).expectStatus().isUnauthorized();
        assertThat(taskRepository.findAll()).isEmpty();
    }

    @Test
    void rejectsRequestWithOnlyTheOrgIdHeader() {
        verifyRequest(credentials(orgId(), null)).expectStatus().isUnauthorized();
    }

    @Test
    void rejectsRequestWithOnlyTheApiKeyHeader() {
        verifyRequest(credentials(null, API_KEY)).expectStatus().isUnauthorized();
    }

    @Test
    void rejectsANonNumericOrgId() {
        verifyRequest(credentials("not-a-number", API_KEY)).expectStatus().isUnauthorized();
    }

    @Test
    void rejectsAnUnknownOrgId() {
        verifyRequest(credentials("987654321", API_KEY)).expectStatus().isUnauthorized();
    }

    @Test
    void rejectsAnOrganisationThatHasNoWebAppUser() {
        var otherOwner = userService.create(User.builder()
                .login("no-webapp-owner").username("no-webapp-owner").email("noweb@example.com")
                .password("x").userRole(UserRole.OWNER).build());
        var otherOrg = organisationService.create(Organisation.builder()
                .owner(otherOwner).organisationName("No WebApp Org").address("x")
                .webAppApiKey(passwordEncoder.encode("other-key"))
                .build());

        verifyRequest(credentials(String.valueOf(otherOrg.getId()), "other-key"))
                .expectStatus().isUnauthorized();
    }

    @Test
    void rejectsAWrongApiKeyForAValidOrganisation() {
        verifyRequest(credentials(orgId(), "totally-wrong-key")).expectStatus().isUnauthorized();
        assertThat(taskRepository.findAll()).isEmpty();
    }

    @Test
    void acceptsValidWebAppCredentialsAndReachesTheEndpoint() {
        verifyRequest(credentials(orgId(), API_KEY)).expectStatus().isOk();

        assertThat(taskRepository.findAll())
                .singleElement()
                .extracting(t -> t.getTaskType())
                .isEqualTo(TaskType.RESERVATION_STATUS_VERIFIER_TASK);
    }

    @Test
    void webAppCredentialsCannotReachStaffEndpoints() {
        client.post()
                .uri("/reservation")
                .headers(credentials(orgId(), API_KEY))
                .exchange()
                .expectStatus().isForbidden();
    }
}
