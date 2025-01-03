package ie.atu.userservice;

import ie.atu.userservice.User;
import ie.atu.userservice.UserRepository;
import ie.atu.userservice.UserService;
import ie.atu.userservice.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser() {
        // Create a new user
        User user = new User();
        user.setName("testuser");
        user.setPassword("password");
        user.setEmail("testuser@example.com");

        // Mock the save method
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Call the registerUser method
        User registeredUser = userService.registerUser(user);

        // Verify the user was saved
        assertThat(registeredUser).isNotNull();
        assertThat(registeredUser.getName()).isEqualTo("testuser");
        assertThat(registeredUser.getEmail()).isEqualTo("testuser@example.com");
    }

    @Test
    public void testFindUserByEmail() {
        // Create a new user
        User user = new User();
        user.setName("testuser");
        user.setPassword("password");
        user.setEmail("testuser@example.com");

        // Mock the findByEmail method
        when(userRepository.findByEmail("testuser@example.com")).thenReturn(Optional.of(user));

        // Call the findUserByEmail method
        User foundUser = userService.findUserByEmail("testuser@example.com");

        // Verify the user was found
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getName()).isEqualTo("testuser");
        assertThat(foundUser.getEmail()).isEqualTo("testuser@example.com");
    }

    @Test
    public void testFindUserByEmailNotFound() {
        // Mock the findByEmail method to return an empty Optional
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // Call the findUserByEmail method and expect a ResourceNotFoundException
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.findUserByEmail("nonexistent@example.com");
        });

        // Verify the exception message
        assertThat(exception.getMessage()).isEqualTo("User not found with email: nonexistent@example.com");
    }
}