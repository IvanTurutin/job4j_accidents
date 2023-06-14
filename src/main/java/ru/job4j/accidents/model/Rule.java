package ru.job4j.accidents.model;

import lombok.*;

import javax.persistence.*;

/**
 * Модель данных для статей нарушений
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Table(name = "rules")
public class Rule {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
}
