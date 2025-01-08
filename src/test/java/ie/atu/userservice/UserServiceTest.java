package ie.atu.userservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsers() {
        List<User> mockUsers = Arrays.asList(
                new User(1L, "John Doe", "john.doe@example.com", "password123"),
                new User(2L, "Jane Smith", "jane.smith@example.com", "password456")
        );

        when(userRepository.findAll()).thenReturn(mockUsers);

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
        assertEquals("John Doe", users.get(0).getName());
        assertEquals("Jane Smith", users.get(1).getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void createUser() {
        User mockUser = new User(null, "John Doe", "john.doe@example.com", "password123");
        User savedUser = new User(1L, "John Doe", "john.doe@example.com", "password123");

        when(userRepository.save(mockUser)).thenReturn(savedUser);

        User user = userService.createUser(mockUser);

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("John Doe", user.getName());
        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    void getUserById() {
        User mockUser = new User(1L, "John Doe", "john.doe@example.com", "password123");

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        User user = userService.getUserById(1L);

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("John Doe", user.getName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(1L);
        });

        assertEquals("User not found with id: 1", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void updateUser() {
        User existingUser = new User(1L, "John Doe", "john.doe@example.com", "password123");
        User updatedDetails = new User(null, "John Updated", "john.updated@example.com", "newpassword123");
        User updatedUser = new User(1L, "John Updated", "john.updated@example.com", "newpassword123");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(updatedUser);

        User user = userService.updateUser(1L, updatedDetails);

        assertNotNull(user);
        assertEquals("John Updated", user.getName());
        assertEquals("john.updated@example.com", user.getEmail());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void updateUser_NotFound() {
        User updatedDetails = new User(null, "John Updated", "john.updated@example.com", "newpassword123");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.updateUser(1L, updatedDetails);
        });

        assertEquals("User not found with id: 1", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void deleteUser() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository).findById(1L);
        verify(userRepository).deleteById(1L);
    }


    @Test
    void deleteUser_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.deleteUser(1L);
        });

        assertEquals("User not found with id: 1", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
    }
}