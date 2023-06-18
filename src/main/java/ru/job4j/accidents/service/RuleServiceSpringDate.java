package ru.job4j.accidents.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Rule;
import ru.job4j.accidents.repository.RuleRepositorySpringData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ThreadSafe
@Service
@AllArgsConstructor
public class RuleServiceSpringDate implements RuleService {
    private final RuleRepositorySpringData repository;

    /**
     * Ищет все типы
     * @return список нарушений
     */
    @Override
    public List<Rule> findAll() {
        List<Rule> rules = new ArrayList<>();
        repository.findAll().forEach(rules::add);
        return rules;
    }

    /**
     * Добавляет новый тип в БД
     * @param rule объект нарушения
     * @return true если успешно добавлен, false если не добавлен.
     */
    @Override
    public boolean create(Rule rule) {
        return repository.save(rule).getId() != 0;
    }

    /**
     * Ищет тип по id
     * @param id идентификатор
     * @return объект типа обернутый в Optional, или Optional.empty если тип не найден
     */
    @Override
    public Optional<Rule> findById(int id) {
        return repository.findById(id);
    }

    /**
     * Обновляет объект типа
     * @param type объект типа
     * @return true если успешно обновилось, false если не обновилось
     */
    @Override
    public boolean update(Rule type) {
        repository.save(type);
        return true;
    }

}
