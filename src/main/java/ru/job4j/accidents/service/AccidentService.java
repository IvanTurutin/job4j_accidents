package ru.job4j.accidents.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.repository.AccidentMem;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Сервис нарушений
 * @see ru.job4j.accidents.model.Accident
 */

@ThreadSafe
@Service
@AllArgsConstructor
@Slf4j
public class AccidentService {
    private final AccidentMem repository;
    private final AccidentTypeService accidentTypeService;

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
        return repository.add(checkAccidentType(accident)).isPresent();
    }

    private Accident checkAccidentType(Accident accident) {
        Optional<AccidentType> accidentType = accidentTypeService.findById(accident.getType().getId());
        if (accidentType.isEmpty()) {
            throw new NoSuchElementException("Такой тип нарушения не найден");
        }
        accident.setType(accidentType.get());
        return accident;
    }

    /**
     * Ищет нарушение по id
     * @param id идентификатор
     * @return объект нарушения обернутый в Optional, или Optional.empty если нарушение не найдено
     */
    public Optional<Accident> findById(int id) {
        return repository.findById(id);
    }

    /**
     * Обновляет объект нарушения
     * @param accident объект нарушения
     * @return true если успешно обновилось, false если не обновилось
     */
    public boolean update(Accident accident) {
        return repository.update(checkAccidentType(accident));
    }
}
