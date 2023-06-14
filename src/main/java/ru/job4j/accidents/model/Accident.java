package ru.job4j.accidents.model;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Модель данных для нарушений
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Table(name = "accidents")
public class Accident {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String text;
    private String address;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "accident_type_id")
    private AccidentType type;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(
            name = "accidents_rules",
            joinColumns = { @JoinColumn(name = "accident_id") },
            inverseJoinColumns = { @JoinColumn(name = "rule_id") }
    )
    private Set<Rule> rules = new HashSet<>();
    public void addRule(Rule rule) {
        rules.add(rule);
    }
}
