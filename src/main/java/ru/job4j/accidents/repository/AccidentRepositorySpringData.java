package ru.job4j.accidents.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;

@Repository
public interface AccidentRepositorySpringData extends CrudRepository<Accident, Integer> {
}
