package tacos.authorization;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import tacos.authorization.users.User;
import tacos.authorization.users.UserRepository;

@SpringBootApplication
public class AuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }

    @Bean
    public ApplicationRunner dataLoader(UserRepository userRepo, PasswordEncoder encoder) {
        return args -> {
            userRepo.save(new User("habibi", encoder.encode("password"), "ROLE_ADMIN"));
            userRepo.save(new User("john", encoder.encode("password"), "ROLE_ADMIN"));
        };
    }
}
