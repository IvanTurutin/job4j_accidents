package ru.job4j.accidents.model;

import lombok.*;

/**
 * Модель данных для статей нарушений
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Rule {
    @EqualsAndHashCode.Include
    private int id;

    private String name;
}
