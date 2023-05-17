package ru.job4j.accidents.repository;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
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
@Repository
public class AccidentMem {
    @GuardedBy("this")
    private final Map<Integer, Accident> store = new ConcurrentHashMap<>();
    @GuardedBy("this")
    private final AtomicInteger count = new AtomicInteger(1);


    public AccidentMem() {
        store.put(count.getAndIncrement(), new Accident(count.get(), "Accident 1", "Text for accident 1", "Address for accident 1"));
        store.put(count.getAndIncrement(), new Accident(count.get(), "Accident 2", "Text for accident 2", "Address for accident 2"));
        store.put(count.getAndIncrement(), new Accident(count.get(), "Accident 3", "Text for accident 3", "Address for accident 3"));
    }

    /**
     * Ищет все нарушения
     * @return список нарушений
     */
    public List<Accident> findAll() {
        return store.values().stream().toList();
    }

    /**
     * Добавляет нарушение в базу данных и присваивает идентификатор
     * @param accident объект нарушения
     * @return объект нарушения в Optional если успешно сохранено, Optional.empty если не сохранено.
     */
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
    public Optional<Accident> findById(int id) {
        Accident accident = store.get(id);
        return accident == null ? Optional.empty() : Optional.of(accident);
    }

    /**
     * Обновляет нарушение
     * @param accident объект нарушения
     * @return true если успешно обновилось, false если не обновилось
     */
    public boolean update(Accident accident) {
        Optional<Accident> accidentOptional = findById(accident.getId());
        if (accidentOptional.isEmpty()) {
            return false;
        }
        return store.put(accident.getId(), accident) != null;
    }

}
