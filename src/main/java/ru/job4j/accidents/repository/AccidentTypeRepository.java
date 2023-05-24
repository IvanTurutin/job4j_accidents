package ru.job4j.accidents.repository;

import ru.job4j.accidents.model.AccidentType;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий типов нарушений
 * @see ru.job4j.accidents.model.AccidentType
 */
public interface AccidentTypeRepository {
    /**
     * Ищет все типы
     * @return список типов
     */
    List<AccidentType> findAll();

    /**
     * Добавляет тип в базу данных и присваивает идентификатор
     * @param type объект типа
     * @return объект типа в Optional если успешно сохранено, Optional.empty если не сохранено.
     */
    Optional<AccidentType> add(AccidentType type);

    /**
     * Ищет тип по идентификатору
     * @param id идентификатор
     * @return объект типа обернутый в Optional, или Optional.empty если тип не найден
     */
    Optional<AccidentType> findById(int id);

    /**
     * Обновляет тип
     * @param type объект типа
     * @return true если успешно обновилось, false если не обновилось
     */
    boolean update(AccidentType type);
}
