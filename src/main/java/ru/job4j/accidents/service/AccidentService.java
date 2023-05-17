package ru.job4j.accidents.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.repository.AccidentMem;

import java.util.List;

/**
 * Сервис нарушений
 * @see ru.job4j.accidents.model.Accident
 */

@ThreadSafe
@Service
@AllArgsConstructor
public class AccidentService {
    private final AccidentMem repository;

    /**
     * Ищет все нарушения
     * @return список нарушений
     */
    public List<Accident> findAll() {
        return repository.findAll();
    }

    /**
     * Добавляет новое нарушение в БД
     * @param accident объект нарушения
     * @return true если успешно добавлен, false если не добавлен.
     */
    public boolean create(Accident accident) {
        return repository.add(accident).isPresent();
    }
}
