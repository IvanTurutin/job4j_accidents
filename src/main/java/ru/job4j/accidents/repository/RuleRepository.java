package ru.job4j.accidents.repository;

import ru.job4j.accidents.model.Rule;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий статей нарушений
 * @see ru.job4j.accidents.model.Rule
 */
public interface RuleRepository {
    /**
     * Ищет все статьи
     * @return список статей
     */
    List<Rule> findAll();

    /**
     * Добавляет статью в базу данных и присваивает идентификатор
     * @param rule объект статьи
     * @return объект статьи в Optional если успешно сохранено, Optional.empty если не сохранено.
     */
    Optional<Rule> add(Rule rule);

    /**
     * Ищет статью по идентификатору
     * @param id идентификатор
     * @return объект статьи обернутый в Optional, или Optional.empty если тип не найден
     */
    Optional<Rule> findById(int id);

    /**
     * Обновляет статью
     * @param rule объект статьи
     * @return true если успешно обновилось, false если не обновилось
     */
    boolean update(Rule rule);

}
