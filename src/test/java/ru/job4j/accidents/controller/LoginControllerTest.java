package ru.job4j.accidents.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.job4j.accidents.Main;

@ActiveProfiles("test")
@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void whenLoginPage() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("user/login"));
    }

    @Test
    @WithMockUser
    void whenWrongLogin() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/login").param("error", "error"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("user/login"))
                .andExpect(MockMvcResultMatchers.model().attribute("errorMessage", "Username or Password is incorrect !!"));
    }

    @Test
    @WithMockUser
    void whenLogout() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/login").param("logout", "logout"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("user/login"))
                .andExpect(MockMvcResultMatchers.model()
                        .attribute("errorMessage", "You have been successfully logged out !!"));
    }

    @Test
    @WithMockUser
    void whenLogout2() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/logout"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login?logout=true"));
    }
}