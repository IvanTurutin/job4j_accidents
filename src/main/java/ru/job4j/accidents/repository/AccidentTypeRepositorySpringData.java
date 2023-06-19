package ru.job4j.accidents.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.AccidentType;

import java.util.List;

@Repository
public interface AccidentTypeRepositorySpringData extends CrudRepository<AccidentType, Integer> {
    List<AccidentType> findAll();
}
