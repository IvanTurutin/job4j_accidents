package ru.job4j.accidents.service;

import ru.job4j.accidents.model.Rule;

import java.util.List;
import java.util.Optional;

/**
 * Сервис статей нарушений
 */
public interface RuleService {
    /**
     * Ищет все статьи
     * @return список статей
     */
    List<Rule> findAll();

    /**
     * Добавляет новую статью в БД
     * @param rule объект статьи
     * @return true если успешно добавлен, false если не добавлен.
     */
    boolean create(Rule rule);

    /**
     * Ищет статью по id
     * @param id идентификатор
     * @return объект статьи обернутый в Optional, или Optional.empty если тип не найден
     */
    Optional<Rule> findById(int id);

    /**
     * Обновляет объект статьи
     * @param rule объект статьи
     * @return true если успешно обновилось, false если не обновилось
     */
    boolean update(Rule rule);

}
