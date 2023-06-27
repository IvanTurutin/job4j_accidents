package ru.job4j.accidents.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Authority;
import ru.job4j.accidents.repository.AuthorityRepositorySpringData;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Сервис ролей пользователей
 */
@ThreadSafe
@Service
@AllArgsConstructor
public class AuthorityService {
    private final AuthorityRepositorySpringData repository;

    /**
     * Ищет все роли
     * @return список ролей
     */
    public List<Authority> findAll() {
        return repository.findAll();
    }

    /**
     * Добавляет новую роль в БД
     * @param authority объект роли
     * @return true если успешно добавлен, false если не добавлен.
     */
    public boolean create(Authority authority) {
        return repository.save(authority).getId() != 0;
    }

    /**
     * Ищет роль по id
     * @param id идентификатор
     * @return объект типа обернутый в Optional, или Optional.empty если тип не найден
     */
    public Optional<Authority> findById(int id) {
        return repository.findById(id);
    }

    /**
     * Ищет роль по имени
     * @param name имя роли
     * @return объект обернутый в Optional, или Optional.empty если не найден
     */
    public Authority findByName(String name) {
        return repository.findByAuthority(name).orElseThrow(() -> new NoSuchElementException("Такой роли нет в БД."));
    }


    /**
     * Обновляет объект роли
     * @param authority объект роли
     * @return true если успешно обновилось, false если не обновилось
     */
    public boolean update(Authority authority) {
        repository.save(authority);
        return true;
    }

}
