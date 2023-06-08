package ru.job4j.accidents.model;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Модель данных для нарушений
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Accident {
    @EqualsAndHashCode.Include
    private int id;
    private String name;
    private String text;
    private String address;
    private AccidentType type;
    private Set<Rule> rules = new HashSet<>();

    public void addRule(Rule rule) {
        rules.add(rule);
    }
}
