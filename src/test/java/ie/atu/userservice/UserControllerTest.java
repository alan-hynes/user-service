package ie.atu.userservice;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"}) // Simulate authenticated user
    void testGetAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(Arrays.asList(
                new User(1L, "John Doe", "john@example.com", "password123"),
                new User(2L, "Jane Smith", "jane@example.com", "password456")
        ));

        mockMvc.perform(get("/api/users")) // GET request
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].email").value("jane@example.com"));
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void testCreateUser() throws Exception {
        User mockUser = new User(1L, "John Doe", "john@example.com", "password123");
        when(userService.createUser(any(User.class))).thenReturn(mockUser);

        mockMvc.perform(post("/api/users")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\", \"email\":\"john@example.com\", \"password\":\"password123\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
        verify(userService, times(1)).createUser(any(User.class));
    }
}
