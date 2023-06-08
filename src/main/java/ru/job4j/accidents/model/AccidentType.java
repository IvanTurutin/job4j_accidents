package ru.job4j.accidents.model;

import lombok.*;

/**
 * Модель данных для типов нарушений
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class AccidentType {
    @EqualsAndHashCode.Include
    private int id;

    private String name;
}
