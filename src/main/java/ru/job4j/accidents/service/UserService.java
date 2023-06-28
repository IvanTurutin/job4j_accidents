package ru.job4j.accidents.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.User;
import ru.job4j.accidents.repository.AuthorityRepositorySpringData;
import ru.job4j.accidents.repository.UserRepositorySpringData;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Сервис пользователей
 */
@ThreadSafe
@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private final UserRepositorySpringData repository;
    private final AuthorityRepositorySpringData authorityRepository;

    /**
     * Ищет всех пользователей
     * @return список пользователей
     */
    public List<User> findAll() {
        return repository.findAll();
    }

    /**
     * Добавляет нового пользователя в БД
     * @param user объект пользователя
     * @return true если успешно добавлен, false если не добавлен.
     */
    public boolean create(User user) {
        try {
            return repository.save(checkForAuthority(user)).getId() != 0;
        } catch (Exception e) {
            log.error("Exception at UserService.create()", e);
            return false;
        }
    }

    private User checkForAuthority(User user) {
        user.setAuthority(
                authorityRepository
                        .findById(user.getAuthority().getId())
                        .orElseThrow(() -> new NoSuchElementException("Такой роли нет в БД."))
        );
        return user;
    }

    /**
     * Ищет пользователя по id
     * @param id идентификатор
     * @return объект пользователя обернутый в Optional, или Optional.empty если пользователь не найден
     */
    public Optional<User> findById(int id) {
        return repository.findById(id);
    }

    /**
     * Обновляет объект пользователя
     * @param user объект пользователя
     * @return true если успешно обновилось, false если не обновилось
     */
    public boolean update(User user) {
        try {
            repository.save(checkForAuthority(user));
            return true;
        } catch (Exception e) {
            log.error("Exception at UserService.create()", e);
            return false;
        }
    }

}
