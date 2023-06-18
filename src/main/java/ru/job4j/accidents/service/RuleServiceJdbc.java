package ru.job4j.accidents.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import ru.job4j.accidents.model.Rule;
import ru.job4j.accidents.repository.RuleRepository;

import java.util.List;
import java.util.Optional;

/**
 * Сервис статей нарушений
 */
@ThreadSafe
/*
@Service
*/
@AllArgsConstructor
public class RuleServiceJdbc implements RuleService {
    private final RuleRepository repository;

    /**
     * Ищет все статьи
     * @return список статей
     */
    @Override
    public List<Rule> findAll() {
        return repository.findAll();
    }

    /**
     * Добавляет новую статью в БД
     * @param rule объект статьи
     * @return true если успешно добавлен, false если не добавлен.
     */
    @Override
    public boolean create(Rule rule) {
        return repository.add(rule).isPresent();
    }

    /**
     * Ищет статью по id
     * @param id идентификатор
     * @return объект статьи обернутый в Optional, или Optional.empty если тип не найден
     */
    @Override
    public Optional<Rule> findById(int id) {
        return repository.findById(id);
    }

    /**
     * Обновляет объект статьи
     * @param rule объект статьи
     * @return true если успешно обновилось, false если не обновилось
     */
    @Override
    public boolean update(Rule rule) {
        return repository.update(rule);
    }

}
