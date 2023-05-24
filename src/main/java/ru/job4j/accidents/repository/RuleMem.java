package ru.job4j.accidents.repository;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Rule;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Репозиторий статей нарушений
 * @see ru.job4j.accidents.model.Rule
 */
@ThreadSafe
@Repository
public class RuleMem implements RuleRepository {
    @GuardedBy("this")
    private final Map<Integer, Rule> store = new ConcurrentHashMap<>();
    @GuardedBy("this")
    private final AtomicInteger count = new AtomicInteger(1);

    public RuleMem() {
        store.put(count.get(), new Rule(count.getAndIncrement(), "Статья. 1"));
        store.put(count.get(), new Rule(count.getAndIncrement(), "Статья. 2"));
        store.put(count.get(), new Rule(count.getAndIncrement(), "Статья. 3"));
    }

    /**
     * Ищет все статьи
     * @return список статей
     */
    @Override
    public List<Rule> findAll() {
        return store.values().stream().toList();
    }

    /**
     * Добавляет статью в базу данных и присваивает идентификатор
     * @param rule объект статьи
     * @return объект статьи в Optional если успешно сохранено, Optional.empty если не сохранено.
     */
    @Override
    public Optional<Rule> add(Rule rule) {
        if (store.get(rule.getId()) != null) {
            return Optional.empty();
        }
        rule.setId(count.getAndIncrement());
        store.put(rule.getId(), rule);
        return Optional.of(rule);
    }

    /**
     * Ищет статью по идентификатору
     * @param id идентификатор
     * @return объект статьи обернутый в Optional, или Optional.empty если тип не найден
     */
    @Override
    public Optional<Rule> findById(int id) {
        return Optional.ofNullable(store.get(id));
    }

    /**
     * Обновляет статью
     * @param rule объект статьи
     * @return true если успешно обновилось, false если не обновилось
     */
    @Override
    public boolean update(Rule rule) {
        return store.computeIfPresent(rule.getId(), (k, v) -> rule) != null;
    }

}
