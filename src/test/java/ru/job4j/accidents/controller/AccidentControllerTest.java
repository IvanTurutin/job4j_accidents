package ru.job4j.accidents.controller;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
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
import static org.assertj.core.api.Assertions.assertThat;

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

    @Test
    @WithMockUser
    void whenCreateSuccessfully() throws Exception {
        Accident accident =
                new Accident(
                        0,
                        "Accident1",
                        "Accident text",
                        "Accident address",
                        new AccidentType(1, null),
                        new HashSet<>());
        String[] ids = {"1", "2"};
        when(accidentService.create(accident, ids)).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/accidents/saveAccident")
                        .param("id", String.valueOf(accident.getId()))
                        .param("name", accident.getName())
                        .param("text", accident.getText())
                        .param("address", accident.getAddress())
                        .param("type.id", String.valueOf(accident.getType().getId()))
                        .param("rIds", ids))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/index"));

        ArgumentCaptor<Accident> argument = ArgumentCaptor.forClass(Accident.class);
        ArgumentCaptor<String[]> argument2 = ArgumentCaptor.forClass(String[].class);
        Mockito.verify(accidentService).create(argument.capture(), argument2.capture());
        assertThat(argument.getValue().getId()).isEqualTo(accident.getId());
        assertThat(argument.getValue().getName()).isEqualTo(accident.getName());
        assertThat(argument.getValue().getText()).isEqualTo(accident.getText());
        assertThat(argument.getValue().getAddress()).isEqualTo(accident.getAddress());
        assertThat(argument.getValue().getType().getId()).isEqualTo(accident.getType().getId());
        assertThat(argument2.getValue()[0]).isEqualTo(ids[0]);
        assertThat(argument2.getValue()[1]).isEqualTo(ids[1]);
    }

    @Test
    @WithMockUser
    void whenNotCreate() throws Exception {
        Accident accident =
                new Accident(
                        0,
                        "Accident1",
                        "Accident text",
                        "Accident address",
                        new AccidentType(1, null),
                        new HashSet<>());
        String[] ids = {"1", "2"};
        when(accidentService.create(accident, ids)).thenReturn(false);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/accidents/saveAccident")
                        .param("id", String.valueOf(accident.getId()))
                        .param("name", accident.getName())
                        .param("text", accident.getText())
                        .param("address", accident.getAddress())
                        .param("type.id", String.valueOf(accident.getType().getId()))
                        .param("rIds", ids))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("message/fail"))
                .andExpect(MockMvcResultMatchers.model()
                        .attribute("message", "Не удалось добавить новое нарушение."));
    }

    @Test
    @WithMockUser
    void whenUpdateSuccessfully() throws Exception {
        Accident accident =
                new Accident(
                        1,
                        "Accident1",
                        "Accident text",
                        "Accident address",
                        new AccidentType(1, null),
                        new HashSet<>());
        String[] ids = {"1", "2"};
        when(accidentService.update(accident, ids)).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/accidents/updateAccident")
                        .param("id", String.valueOf(accident.getId()))
                        .param("name", accident.getName())
                        .param("text", accident.getText())
                        .param("address", accident.getAddress())
                        .param("type.id", String.valueOf(accident.getType().getId()))
                        .param("rIds", ids))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/index"));

        ArgumentCaptor<Accident> argument = ArgumentCaptor.forClass(Accident.class);
        ArgumentCaptor<String[]> argument2 = ArgumentCaptor.forClass(String[].class);
        Mockito.verify(accidentService).update(argument.capture(), argument2.capture());
        assertThat(argument.getValue().getId()).isEqualTo(accident.getId());
        assertThat(argument.getValue().getName()).isEqualTo(accident.getName());
        assertThat(argument.getValue().getText()).isEqualTo(accident.getText());
        assertThat(argument.getValue().getAddress()).isEqualTo(accident.getAddress());
        assertThat(argument.getValue().getType().getId()).isEqualTo(accident.getType().getId());
        assertThat(argument2.getValue()[0]).isEqualTo(ids[0]);
        assertThat(argument2.getValue()[1]).isEqualTo(ids[1]);
    }

    @Test
    @WithMockUser
    void whenNotUpdate() throws Exception {
        Accident accident =
                new Accident(
                        1,
                        "Accident1",
                        "Accident text",
                        "Accident address",
                        new AccidentType(1, null),
                        new HashSet<>());
        String[] ids = {"1", "2"};
        when(accidentService.update(accident, ids)).thenReturn(false);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/accidents/updateAccident")
                        .param("id", String.valueOf(accident.getId()))
                        .param("name", accident.getName())
                        .param("text", accident.getText())
                        .param("address", accident.getAddress())
                        .param("type.id", String.valueOf(accident.getType().getId()))
                        .param("rIds", ids))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("message/fail"))
                .andExpect(MockMvcResultMatchers.model()
                        .attribute("message", "Не удалось обновить нарушение."));
    }
}