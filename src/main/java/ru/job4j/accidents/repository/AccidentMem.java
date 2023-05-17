package ru.job4j.accidents.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Репозиторий нарушений
 * @see ru.job4j.accidents.model.Accident
 */
@ThreadSafe
@Repository
public class AccidentMem {
    private final Map<Integer, Accident> store = new ConcurrentHashMap<>();

    public AccidentMem() {
        int count = 1;
        store.put(count, new Accident(count, "Accident 1", "Text for accident 1", "Address for accident 1"));
        store.put(++count, new Accident(count, "Accident 2", "Text for accident 2", "Address for accident 2"));
        store.put(++count, new Accident(count, "Accident 3", "Text for accident 3", "Address for accident 3"));
    }

    /**
     * Ищет все нарушения
     * @return список нарушений
     */
    public List<Accident> findAll() {
        return store.values().stream().toList();
    }
}
