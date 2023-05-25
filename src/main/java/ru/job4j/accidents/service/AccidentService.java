package ru.job4j.accidents.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.repository.AccidentRepository;
import ru.job4j.accidents.repository.AccidentTypeRepository;
import ru.job4j.accidents.repository.RuleRepository;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сервис нарушений
 * @see ru.job4j.accidents.model.Accident
 */

@ThreadSafe
@Service
@AllArgsConstructor
@Slf4j
public class AccidentService {
    private final AccidentRepository repository;
    private final AccidentTypeRepository accidentTypeRepository;
    private final RuleRepository ruleRepository;

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

    /**
     * Добавляет новое нарушение в БД
     * @param accident объект нарушения
     * @param ids строковый массив из индексов статей нарушений
     * @return true если успешно добавлен, false если не добавлен.
     */
    public boolean create(Accident accident, String[] ids) {
        return create(setRulesToAccident(accident, ids));
    }

    /**
     * Осуществляет проверку наличия соответствующего типа нарушения и его внедрение в объект нарушения
     * @param accident объект нарушения
     * @return объект нарушения с внедренным типом нарушения
     */
    private Accident checkAccidentType(Accident accident) {
        Optional<AccidentType> accidentType = accidentTypeRepository.findById(accident.getType().getId());
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

    /**
     * Обновляет объект нарушения
     * @param accident объект нарушения
     * @param ids строковый массив из индексов статей нарушений
     * @return true если успешно добавлен, false если не добавлен.
     */
    public boolean update(Accident accident, String[] ids) {
        return update(setRulesToAccident(accident, ids));
    }

    /**
     * Проверяет наличие всех выбранных статей нарушений и присваивает их объекту нарушения
     * @param accident нарушение
     * @param ids массив идентификаторов статей нарушений
     * @return собранный объект нарушения
     */
    private Accident setRulesToAccident(Accident accident, String[] ids) {
        accident.setRules(
                ruleRepository.findRules(
                        Arrays.stream(ids)
                                .map(Integer::parseInt)
                                .collect(Collectors.toList())
                ));
        return accident;
    }
}
