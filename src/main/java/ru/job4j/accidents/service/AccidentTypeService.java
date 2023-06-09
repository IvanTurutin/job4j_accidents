package ru.job4j.accidents.service;

import ru.job4j.accidents.model.AccidentType;

import java.util.List;
import java.util.Optional;

public interface AccidentTypeService {

    /**
     * Ищет все типы
     * @return список нарушений
     */
    List<AccidentType> findAll();

    /**
     * Добавляет новый тип в БД
     * @param type объект нарушения
     * @return true если успешно добавлен, false если не добавлен.
     */
    boolean create(AccidentType type);

    /**
     * Ищет тип по id
     * @param id идентификатор
     * @return объект типа обернутый в Optional, или Optional.empty если тип не найден
     */
    Optional<AccidentType> findById(int id);

    /**
     * Обновляет объект типа
     * @param type объект типа
     * @return true если успешно обновилось, false если не обновилось
     */
    boolean update(AccidentType type);

}
