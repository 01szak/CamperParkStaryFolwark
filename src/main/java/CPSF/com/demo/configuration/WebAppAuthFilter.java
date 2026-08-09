package CPSF.com.demo.configuration;

import CPSF.com.demo.exception.AuthorizationException;
import CPSF.com.demo.model.constant.Operation;
import CPSF.com.demo.service.core.OrganisationService;
import CPSF.com.demo.service.core.SearchCriteria;
import CPSF.com.demo.service.core.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static CPSF.com.demo.model.constant.UserRole.WEB_APP;

@RequiredArgsConstructor
public class WebAppAuthFilter extends OncePerRequestFilter {

    private static final String ORGANISATION_ID_HEADER = "X-org-id";
    private static final String API_KEY_HEADER = "X-api-key";

    private final OrganisationService organisationService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        var orgId = request.getHeader(ORGANISATION_ID_HEADER);
        var apiKey = request.getHeader(API_KEY_HEADER);

        Optional.ofNullable(apiKey).ifPresentOrElse(_ -> {
            var organisation = organisationService.findById(Integer.parseInt(orgId));
            var compareApiKey = organisation.getWebAppApiKey();

            var appUser = userService.findBy(
                    new SearchCriteria("organisation", "id", Operation.EQUALS, orgId),
                    new SearchCriteria("userRole", Operation.EQUALS, "WEB_APP")
            )
            .stream()
            .findFirst()
            .orElseThrow(() -> new AuthorizationException("No web app associated for the given organisation"));

            if (compareApiKey.equals(apiKey)) {
                var auth = new UsernamePasswordAuthenticationToken(
                        appUser.getLogin(),
                        null,
                        List.of(new SimpleGrantedAuthority(WEB_APP.getAuthority()))
                );

                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                throw new AuthorizationException("Api key not matched");
            }
        }, () -> {
            throw new AuthorizationException("Api key not found");
        });

        filterChain.doFilter(request, response);
    }

}
