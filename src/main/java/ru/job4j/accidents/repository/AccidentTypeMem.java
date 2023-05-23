package ru.job4j.accidents.repository;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.AccidentType;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Репозиторий типов нарушений
 * @see ru.job4j.accidents.model.AccidentType
 */
@ThreadSafe
@Repository
public class AccidentTypeMem {
    @GuardedBy("this")
    private final Map<Integer, AccidentType> store = new ConcurrentHashMap<>();
    @GuardedBy("this")
    private final AtomicInteger count = new AtomicInteger(1);

    public AccidentTypeMem() {
        store.put(count.getAndIncrement(), new AccidentType(count.get(), "Две машины"));
        store.put(count.getAndIncrement(), new AccidentType(count.get(), "Машина и человек"));
        store.put(count.getAndIncrement(), new AccidentType(count.get(), "Машина и велосипед"));
    }

    /**
     * Ищет все типы
     * @return список типов
     */
    public List<AccidentType> findAll() {
        return store.values().stream().toList();
    }

    /**
     * Добавляет тип в базу данных и присваивает идентификатор
     * @param type объект типа
     * @return объект типа в Optional если успешно сохранено, Optional.empty если не сохранено.
     */
    public Optional<AccidentType> add(AccidentType type) {
        if (store.get(type.getId()) != null) {
            return Optional.empty();
        }
        type.setId(count.getAndIncrement());
        store.put(type.getId(), type);
        return Optional.of(type);
    }

    /**
     * Ищет тип по идентификатору
     * @param id идентификатор
     * @return объект типа обернутый в Optional, или Optional.empty если тип не найден
     */
    public Optional<AccidentType> findById(int id) {
        return Optional.ofNullable(store.get(id));
    }

    /**
     * Обновляет тип
     * @param type объект типа
     * @return true если успешно обновилось, false если не обновилось
     */
    public boolean update(AccidentType type) {
        return store.computeIfPresent(type.getId(), (k, v) -> type) != null;
    }

}
