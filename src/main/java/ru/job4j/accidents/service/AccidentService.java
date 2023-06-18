package ru.job4j.accidents.service;

import ru.job4j.accidents.model.Accident;

import java.util.List;
import java.util.Optional;

/**
 * Сервис нарушений
 * @see Accident
 */

public interface AccidentService {

    /**
     * Ищет все нарушения
     * @return список нарушений
     */
    List<Accident> findAll();

    /**
     * Добавляет новое нарушение в БД
     * @param accident объект нарушения
     * @return true если успешно добавлен, false если не добавлен.
     */
    boolean create(Accident accident);

    /**
     * Добавляет новое нарушение в БД
     * @param accident объект нарушения
     * @param ids строковый массив из индексов статей нарушений
     * @return true если успешно добавлен, false если не добавлен.
     */
    boolean create(Accident accident, String[] ids);

    /**
     * Ищет нарушение по id
     * @param id идентификатор
     * @return объект нарушения обернутый в Optional, или Optional.empty если нарушение не найдено
     */
    Optional<Accident> findById(int id);

    /**
     * Обновляет объект нарушения
     * @param accident объект нарушения
     * @return true если успешно обновилось, false если не обновилось
     */
    boolean update(Accident accident);

    /**
     * Обновляет объект нарушения
     * @param accident объект нарушения
     * @param ids строковый массив из индексов статей нарушений
     * @return true если успешно добавлен, false если не добавлен.
     */
    boolean update(Accident accident, String[] ids);
}
