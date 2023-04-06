package tacos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.context.annotation.RequestScope;
import tacos.IngredientService;
import tacos.RestIngredientService;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(
                        authorizeHttpRequests -> authorizeHttpRequests.anyRequest().authenticated()
                )
                .oauth2Login(oauth2Login -> oauth2Login.loginPage("/oauth2/authorization/taco-admin-client"))
                .oauth2Client(Customizer.withDefaults())
                .build();
    }

    @Bean
    @RequestScope
    public IngredientService ingredientService(OAuth2AuthorizedClientService clientService) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String accessToken = null;
        if (authentication.getClass().isAssignableFrom(OAuth2AuthenticationToken.class)) {
            OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken) authentication;
            String clientRegistrationId = oAuth2Token.getAuthorizedClientRegistrationId();
            if (clientRegistrationId.equals("taco-admin-client")) {
                OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(clientRegistrationId, oAuth2Token.getName());
                accessToken = client.getAccessToken().getTokenValue();
            }
        }
        return new RestIngredientService(accessToken);
    }
}
