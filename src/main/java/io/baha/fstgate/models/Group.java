package io.baha.fstgate.models;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Table(name = "study_groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NaturalId
    @Column(length = 60)
    private String name;


    public Group() {
    }

    public Group(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
