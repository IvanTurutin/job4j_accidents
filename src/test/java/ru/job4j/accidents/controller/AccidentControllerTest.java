package ru.job4j.accidents.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.job4j.accidents.Main;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.service.AccidentService;
import ru.job4j.accidents.service.AccidentTypeService;
import ru.job4j.accidents.service.RuleService;

import java.util.HashSet;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
class AccidentControllerTest {

    @MockBean
    private AccidentService accidentService;
    @MockBean
    private AccidentTypeService typeService;
    @MockBean
    private RuleService ruleService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void whenCreateAccident() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get("/accidents/createAccident"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("accident/createAccident"));
    }

    @Test
    @WithMockUser
    void whenFoundAccidentAtViewUpdate() throws Exception {
        int id = 1;
        Optional<Accident> accident = Optional.of(
                new Accident(
                        id,
                        "Accident1",
                        "Accident text",
                        "Accident address",
                        new AccidentType(1, "Accident Type"),
                        new HashSet<>()));
        when(accidentService.findById(1)).thenReturn(accident);

        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/accidents/formUpdateAccident")
                        .param("id", String.valueOf(id)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("accident/updateAccident"));
    }

    @Test
    @WithMockUser
    void whenNotFoundAccidentAtViewUpdate() throws Exception {

        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/accidents/formUpdateAccident")
                        .param("id", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("message/fail"))
                .andExpect(MockMvcResultMatchers.model().attribute("message", "Не удалось найти нарушение."));
    }
}