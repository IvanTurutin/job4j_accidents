package ru.job4j.accidents.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.repository.AccidentTypeRepository;

import java.util.List;
import java.util.Optional;

@ThreadSafe
/*
@Service
*/
@AllArgsConstructor
public class AccidentTypeServiceJdbc implements AccidentTypeService {
    private final AccidentTypeRepository repository;

    /**
     * Ищет все типы
     * @return список нарушений
     */
    @Override
    public List<AccidentType> findAll() {
        return repository.findAll();
    }

    /**
     * Добавляет новый тип в БД
     * @param type объект нарушения
     * @return true если успешно добавлен, false если не добавлен.
     */
    @Override
    public boolean create(AccidentType type) {
        return repository.add(type).isPresent();
    }

    /**
     * Ищет тип по id
     * @param id идентификатор
     * @return объект типа обернутый в Optional, или Optional.empty если тип не найден
     */
    @Override
    public Optional<AccidentType> findById(int id) {
        return repository.findById(id);
    }

    /**
     * Обновляет объект типа
     * @param type объект типа
     * @return true если успешно обновилось, false если не обновилось
     */
    @Override
    public boolean update(AccidentType type) {
        return repository.update(type);
    }

}
