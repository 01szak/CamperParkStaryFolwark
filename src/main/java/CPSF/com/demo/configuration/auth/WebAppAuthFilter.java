package CPSF.com.demo.configuration.auth;

import CPSF.com.demo.exception.AuthenticationException;
import CPSF.com.demo.model.constant.Operation;
import CPSF.com.demo.model.entity.User;
import CPSF.com.demo.service.core.OrganisationService;
import CPSF.com.demo.service.core.SearchCriteria;
import CPSF.com.demo.service.core.UserService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static CPSF.com.demo.model.constant.UserRole.WEB_APP;
import static java.util.concurrent.TimeUnit.MINUTES;

@Log4j2
@RequiredArgsConstructor
public class WebAppAuthFilter extends OncePerRequestFilter {

    private static final String ORGANISATION_ID_HEADER = "X-org-id";
    private static final String API_KEY_HEADER = "X-api-key";

    private final PasswordEncoder passwordEncoder;
    private final OrganisationService organisationService;
    private final UserService userService;
    private final Cache<String, User> authCache = Caffeine.newBuilder()
            .expireAfterWrite(5, MINUTES)
            .maximumSize(1)
            .build();


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        var orgId = request.getHeader(ORGANISATION_ID_HEADER);
        var apiKey = request.getHeader(API_KEY_HEADER);
        var headersPresent = orgId != null && apiKey != null;

        if (headersPresent) {
            var appUser = authCache.getIfPresent(getCacheKey(orgId, apiKey));

            Optional.ofNullable(appUser).ifPresentOrElse(
                    this::authoriseUser,
                    () -> authenticateUser(orgId, apiKey)
            );
        }

        filterChain.doFilter(request, response);
    }

    private void authenticateUser(String orgId, String apiKey) {
        logger.info("Web app auth headers found, starting Authentication for orgId: " + orgId);

        var organisation = organisationService.findById(Integer.parseInt(orgId));

        var appUser = userService.findBy(
                        new SearchCriteria("organisation", "id", Operation.EQUALS, orgId),
                        new SearchCriteria("userRole", Operation.EQUALS, "WEB_APP")
                )
                .stream()
                .findFirst()
                .orElseThrow(() -> new AuthenticationException("No web app associated for the given organisation"));

        if (passwordEncoder.matches(apiKey, organisation.getWebAppApiKey())) {
            authoriseUser(appUser);
            logger.info("Web app authentication succeeded for orgId: " + orgId);
        } else {
            throw new AuthenticationException("Api key not matched");
        }
        authCache.put(getCacheKey(orgId, apiKey), appUser);

    }

    private void authoriseUser(User appUser) {
        var auth = new UsernamePasswordAuthenticationToken(
                appUser.getLogin(),
                null,
                List.of(new SimpleGrantedAuthority(WEB_APP.getAuthority()))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private String getCacheKey(String orgId, String apiKey) {
        return orgId + ":" + apiKey;
    }

}
