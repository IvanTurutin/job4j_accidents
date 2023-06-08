package ru.job4j.accidents.repository;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import ru.job4j.accidents.model.Accident;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Репозиторий нарушений
 * @see ru.job4j.accidents.model.Accident
 */
@ThreadSafe
/*@Repository*/
public class AccidentMem implements AccidentRepository {
    @GuardedBy("this")
    private final Map<Integer, Accident> store = new ConcurrentHashMap<>();
    @GuardedBy("this")
    private final AtomicInteger count = new AtomicInteger(1);

    /**
     * Ищет все нарушения
     * @return список нарушений
     */
    @Override
    public List<Accident> findAll() {
        return store.values().stream().toList();
    }

    /**
     * Добавляет нарушение в базу данных и присваивает идентификатор
     * @param accident объект нарушения
     * @return объект нарушения в Optional если успешно сохранено, Optional.empty если не сохранено.
     */
    @Override
    public Optional<Accident> add(Accident accident) {
        if (store.get(accident.getId()) != null) {
            return Optional.empty();
        }
        accident.setId(count.getAndIncrement());
        store.put(accident.getId(), accident);
        return Optional.of(accident);
    }

    /**
     * Ищет нарушение по идентификатору
     * @param id идентификатор
     * @return объект нарушения обернутый в Optional, или Optional.empty если нарушение не найдено
     */
    @Override
    public Optional<Accident> findById(int id) {
        return Optional.ofNullable(store.get(id));
    }

    /**
     * Обновляет нарушение
     * @param accident объект нарушения
     * @return true если успешно обновилось, false если не обновилось
     */
    @Override
    public boolean update(Accident accident) {
        return store.computeIfPresent(accident.getId(), (k, v) -> accident) != null;
    }
}
