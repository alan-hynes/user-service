package ie.atu.userservice;

import ie.atu.userservice.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveAndFindByUsername() {
        // Create a new user
        User user = new User();
        user.setName("testuser");
        user.setPassword("password");
        user.setEmail("test@email.com");

        // Save the user
        userRepository.save(user);

        // Find the user by username
        Optional<User> foundUser = userRepository.findByEmail("test@email.com");

        // Verify the user was found
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("test@email.com");
    }

    @Test
    public void NotUserFound() {
        // Create a new user
        User user = new User();
        user.setName("testuser");
        user.setPassword("password");
        user.setEmail("test@email.com");

        // Save the user
        userRepository.save(user);

        // Find the user by username
        Optional<User> foundUser = userRepository.findByEmail("Alan");

        // Verify the user was not found
        assertThat(foundUser).isNotPresent();

    }
}