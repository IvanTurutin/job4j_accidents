package ru.job4j.accidents.repository;

import ru.job4j.accidents.model.Accident;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий нарушений
 * @see ru.job4j.accidents.model.Accident
 */
public interface AccidentRepository {
    /**
     * Ищет все нарушения
     * @return список нарушений
     */
    List<Accident> findAll();

    /**
     * Добавляет нарушение в базу данных и присваивает идентификатор
     *
     * @param accident объект нарушения
     * @return объект нарушения в Optional если успешно сохранено, Optional.empty если не сохранено.
     */
    Optional<Accident> add(Accident accident);

    /**
     * Ищет нарушение по идентификатору
     *
     * @param id идентификатор
     * @return объект нарушения обернутый в Optional, или Optional.empty если нарушение не найдено
     */
    Optional<Accident> findById(int id);

    /**
     * Обновляет нарушение
     *
     * @param accident объект нарушения
     * @return true если успешно обновилось, false если не обновилось
     */
    boolean update(Accident accident);
}
