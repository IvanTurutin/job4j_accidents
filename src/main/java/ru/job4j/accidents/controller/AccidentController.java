package ru.job4j.accidents.controller;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.service.AccidentService;

import java.util.Optional;

@ThreadSafe
@Controller
@AllArgsConstructor
@RequestMapping("/accidents")
public class AccidentController {
    private final AccidentService accidentService;
    private static final Logger LOG = LoggerFactory.getLogger(AccidentController.class.getName());


    @GetMapping("/createAccident")
    public String viewCreateAccident(Model model) {
        model.addAttribute("user", "Ivan");
        model.addAttribute("accident", new Accident());
        return "accident/createAccident";
    }

    @PostMapping("/saveAccident")
    public String save(Model model, @ModelAttribute Accident accident) {
        LOG.debug("accident at save() before add/update = " + accident);
        if (accident.getId() == 0) {
            if (!accidentService.create(accident)) {
                model.addAttribute("message", "Не удалось добавить новое нарушение.");
                return "message/fail";
            }
        } else {
            if (!accidentService.update(accident)) {
                model.addAttribute("message", "Не удалось обновить нарушение.");
                return "message/fail";
            }
        }
        LOG.debug("accident at save() after add/update = " + accident);
        return "redirect:/index";
    }

    @GetMapping("/formUpdateAccident")
    public String update(@RequestParam("id") int id, Model model) {
        Optional<Accident> accident = accidentService.findById(id);
        if (accident.isEmpty()) {
            model.addAttribute("message", "Не удалось найти нарушение.");
            return "message/fail";
        }
        model.addAttribute("accident", accident.get());
        return "accident/createAccident";
    }
}
