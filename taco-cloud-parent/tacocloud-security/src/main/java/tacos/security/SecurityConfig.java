package tacos.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import tacos.User;
import tacos.data.UserRepository;

@Configuration
public class SecurityConfig {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
//        List<UserDetails> usersList = new ArrayList<>();
//        usersList.add(new User(
//                "buzz",
//                encoder.encode("password"),
//                List.of(new SimpleGrantedAuthority("ROLE_USER"))));
//        usersList.add(new User(
//                "woody",
//                encoder.encode("password"),
//                List.of(new SimpleGrantedAuthority("ROLE_USER"))));
//        return new InMemoryUserDetailsManager(usersList);
//    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepo) {
        return username -> {
            User user = userRepo.findByUsername(username);
            if (user != null) {
                return user;
            }
            throw new UsernameNotFoundException("User " + username + " not found");
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests()
                .requestMatchers("/design", "/orders").hasRole("USER")
                .requestMatchers("/", "/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/ingredients").hasAuthority("SCOPE_writeIngredients")
                .requestMatchers(HttpMethod.DELETE, "/api//ingredients").hasAuthority("SCOPE_deleteIngredients")
                .and()
                .formLogin().loginPage("/login")

                .and()
                .logout().logoutSuccessUrl("/")

                .and()
                .csrf().disable()
                .headers().frameOptions().disable()

                .and().oauth2ResourceServer(oAuth2 -> oAuth2.jwt())
                .build();
    }

//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }

}
