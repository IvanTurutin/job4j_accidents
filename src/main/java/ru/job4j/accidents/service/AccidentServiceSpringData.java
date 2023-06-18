package ru.job4j.accidents.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.repository.AccidentRepositorySpringData;
import ru.job4j.accidents.repository.AccidentTypeRepository;
import ru.job4j.accidents.repository.RuleRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Сервис нарушений
 * @see Accident
 */

@ThreadSafe
@Service
@AllArgsConstructor
@Slf4j
public class AccidentServiceSpringData implements AccidentService {
    private final AccidentRepositorySpringData repository;
    private final AccidentTypeRepository accidentTypeRepository;
    private final RuleRepository ruleRepository;

    /**
     * Ищет все нарушения
     * @return список нарушений
     */
    @Override
    public List<Accident> findAll() {
        List<Accident> accidents = new ArrayList<>();
        repository.findAll().forEach(accidents::add);
        return accidents;
    }

    /**
     * Добавляет новое нарушение в БД
     * @param accident объект нарушения
     * @return true если успешно добавлен, false если не добавлен.
     */
    @Override
    public boolean create(Accident accident) {
        return repository.save(checkAccidentType(accident)).getId() != 0;
    }

    /**
     * Добавляет новое нарушение в БД
     * @param accident объект нарушения
     * @param ids строковый массив из индексов статей нарушений
     * @return true если успешно добавлен, false если не добавлен.
     */
    @Override
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
    @Override
    public Optional<Accident> findById(int id) {
        return repository.findById(id);
    }

    /**
     * Обновляет объект нарушения
     * @param accident объект нарушения
     * @return true если успешно обновилось, false если не обновилось
     */
    @Override
    public boolean update(Accident accident) {
        repository.save(checkAccidentType(accident));
        return true;
    }

    /**
     * Обновляет объект нарушения
     * @param accident объект нарушения
     * @param ids строковый массив из индексов статей нарушений
     * @return true если успешно добавлен, false если не добавлен.
     */
    @Override
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
        if (ids != null) {
            accident.setRules(
                    ruleRepository.findRules(
                            Arrays.stream(ids)
                                    .map(Integer::parseInt)
                                    .collect(Collectors.toList())
                    ));
        }
        log.debug("accident at setRulesToAccident() after add rules = " + accident);
        return accident;
    }
}
