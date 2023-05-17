package ru.job4j.accidents.controller;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.accidents.service.AccidentService;

import javax.servlet.http.HttpSession;

/**
 * Контроллер стартовой страницы
 */
@ThreadSafe
@Controller
@AllArgsConstructor
public class IndexController {

    private final AccidentService accidentService;

    /**
     * Принимает запрос на отображение стартовой страницы
     * @param model модель вида
     * @param session сессия подключения
     * @return название шаблона, которое требуется для формирования вида и показа пользователю
     */
    @GetMapping("/index")
    public String index(Model model, HttpSession session) {
        model.addAttribute("user", "Ivan");
        model.addAttribute("accidents", accidentService.findAll());
        return "index";
    }

}
