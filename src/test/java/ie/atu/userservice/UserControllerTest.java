package ie.atu.userservice;

import ie.atu.userservice.User;
import ie.atu.userservice.UserRepository;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void testRegisterUser() throws Exception {
        // Create a new user
        User user = new User();
        user.setName("testuser");
        user.setPassword("password");
        user.setEmail("testuser@example.com");

        // Convert user object to JSON
        String userJson = objectMapper.writeValueAsString(user);

        // Perform the POST request and verify the response
        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("testuser")))
                .andExpect(jsonPath("$.email", is("testuser@example.com")));
    }

    @Test
    public void testFindUserByEmail() throws Exception {
        // Create and save a new user
        User user = new User();
        user.setName("testuser");
        user.setPassword("password");
        user.setEmail("testuser@example.com");
        userRepository.save(user);

        // Perform the GET request and verify the response
        mockMvc.perform(get("/users/testuser@example.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("testuser")))
                .andExpect(jsonPath("$.email", is("testuser@example.com")));
    }
}