package ru.job4j.accidents.controller;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.service.AccidentService;

@ThreadSafe
@Controller
@AllArgsConstructor
@RequestMapping("/accidents")
public class AccidentController {
    private final AccidentService accidents;
    private static final Logger LOG = LoggerFactory.getLogger(AccidentController.class.getName());


    @GetMapping("/createAccident")
    public String viewCreateAccident(Model model) {
        model.addAttribute("user", "Ivan");
        model.addAttribute("accident", new Accident());
        return "accident/createAccident";
    }

    @PostMapping("/saveAccident")
    public String save(Model model, @ModelAttribute Accident accident) {
        LOG.debug("accident at save() before add = " + accident);
        if (!accidents.create(accident)) {
            model.addAttribute("message", "Не удалось добавить новое нарушение.");
            return "message/fail";
        }
        LOG.debug("accident at save() after add = " + accident);
        return "redirect:/index";
    }
}
