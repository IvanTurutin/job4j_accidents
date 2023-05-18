package ru.job4j.accidents.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.service.AccidentService;
import ru.job4j.accidents.service.AccidentTypeService;

import java.util.Optional;

/**
 * Контроллер нарушений
 */
@ThreadSafe
@Controller
@AllArgsConstructor
@RequestMapping("/accidents")
@Slf4j
public class AccidentController {
    private final AccidentService accidentService;
    private final AccidentTypeService accidentTypeService;

    /**
     * Подготавливает вид создания нового нарушения
     * @param model модель вида
     * @return адрес шаблона вида
     */
    @GetMapping("/createAccident")
    public String viewCreateAccident(Model model) {
        model.addAttribute("user", "Ivan");
        model.addAttribute("types", accidentTypeService.findAll());
        return "accident/createAccident";
    }

    /**
     * Обрабатывает запрос на добавление нового нарушения
     * @param model модель вида
     * @param accident формируемое нарушение
     * @return адрес шаблона начальной страницы
     */
    @PostMapping("/saveAccident")
    public String save(Model model, @ModelAttribute Accident accident) {
        log.debug("accident at save() before add = " + accident);
        if (!accidentService.create(accident)) {
            model.addAttribute("message", "Не удалось добавить новое нарушение.");
            return "message/fail";
        }
        log.debug("accident at save() after add = " + accident);
        return "redirect:/index";
    }

    /**
     * Подготавливает вид для редактирования нарушения
     * @param id идентификатор нарушения
     * @param model модель вида
     * @return адрес шаблона вида для редактирования нарушения
     */
    @GetMapping("/formUpdateAccident")
    public String viewUpdate(@RequestParam("id") int id, Model model) {
        Optional<Accident> accident = accidentService.findById(id);
        if (accident.isEmpty()) {
            model.addAttribute("message", "Не удалось найти нарушение.");
            return "message/fail";
        }
        model.addAttribute("user", "Ivan");
        model.addAttribute("types", accidentTypeService.findAll());
        model.addAttribute("accident", accident.get());
        return "accident/updateAccident";
    }

    /**
     * Обрабатывает запрос на обновление нарушения
     * @param model модель вида
     * @param accident формируемое нарушение
     * @return адрес шаблона начальной страницы
     */
    @PostMapping("/updateAccident")
    public String update(Model model, @ModelAttribute Accident accident) {
        log.debug("accident at update() before update = " + accident);
        if (!accidentService.update(accident)) {
            model.addAttribute("message", "Не удалось обновить нарушение.");
            return "message/fail";
        }
        log.debug("accident at update() after update = " + accident);
        return "redirect:/index";
    }

}
