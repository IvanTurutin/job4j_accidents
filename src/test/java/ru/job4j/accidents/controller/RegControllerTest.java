package ru.job4j.accidents.controller;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.job4j.accidents.Main;
import ru.job4j.accidents.model.Authority;
import ru.job4j.accidents.model.User;
import ru.job4j.accidents.service.AuthorityService;
import ru.job4j.accidents.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
class RegControllerTest {

    @MockBean
    private UserService users;

    @MockBean
    private PasswordEncoder encoder;

    @MockBean
    private AuthorityService authorities;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void whenRegistrationPage() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/reg"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("user/reg"));
    }

    @Test
    @WithMockUser
    void whenCreateSuccessfully() throws Exception {
        User user = new User(
                0,
                "$2a$10$T0wIbKsejrnY9CLh3WQqlewbwMlFV7GyleMvXujKa1r8K6lXb5Aiq",
                "User",
                new Authority(1, "ROLE_USER"),
                true);
        String password = "123";
        when(users.create(user)).thenReturn(true);
        when(encoder.encode(password)).thenReturn(user.getPassword());
        when(authorities.findByName(user.getAuthority().getAuthority())).thenReturn(user.getAuthority());

        this.mockMvc.perform(MockMvcRequestBuilders.post("/registration")
                        .param("id", String.valueOf(user.getId()))
                        .param("username", user.getUsername())
                        .param("password", password))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login"));

        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        Mockito.verify(users).create(argument.capture());
        assertThat(argument.getValue().getId()).isEqualTo(user.getId());
        assertThat(argument.getValue().getUsername()).isEqualTo(user.getUsername());
        assertThat(argument.getValue().getPassword()).isEqualTo(user.getPassword());
        assertThat(argument.getValue().getAuthority()).isEqualTo(user.getAuthority());
        assertThat(argument.getValue().isEnabled()).isEqualTo(user.isEnabled());
    }

    @Test
    @WithMockUser
    void whenNotCreate() throws Exception {
        User user = new User(
                0,
                "$2a$10$T0wIbKsejrnY9CLh3WQqlewbwMlFV7GyleMvXujKa1r8K6lXb5Aiq",
                "User",
                new Authority(1, "ROLE_USER"),
                true);
        String password = "123";
        when(users.create(user)).thenReturn(false);
        when(encoder.encode(password)).thenReturn(user.getPassword());
        when(authorities.findByName(user.getAuthority().getAuthority())).thenReturn(user.getAuthority());

        this.mockMvc.perform(MockMvcRequestBuilders.post("/registration")
                        .param("id", String.valueOf(user.getId()))
                        .param("username", user.getUsername())
                        .param("password", password))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/message/fail"))
                .andExpect(MockMvcResultMatchers.model()
                        .attribute("message", "Не удалось создать пользователя с таким именем. "
                                + "Возможно имя уже занято."));
    }
}