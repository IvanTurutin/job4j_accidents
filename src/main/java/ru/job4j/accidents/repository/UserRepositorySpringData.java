package ru.job4j.accidents.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.accidents.model.User;

import java.util.List;

public interface UserRepositorySpringData extends CrudRepository<User, Integer> {
    List<User> findAll();
}