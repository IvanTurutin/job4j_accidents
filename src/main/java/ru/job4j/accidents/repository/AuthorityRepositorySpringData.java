package ru.job4j.accidents.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.accidents.model.Authority;

import java.util.List;
import java.util.Optional;

public interface AuthorityRepositorySpringData extends CrudRepository<Authority, Integer> {

    Optional<Authority> findByAuthority(String authority);
    List<Authority> findAll();
}