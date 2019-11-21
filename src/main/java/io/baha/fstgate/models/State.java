package io.baha.fstgate.models;
import org.hibernate.annotations.NaturalId;
import javax.persistence.*;

@Entity
@Table(name = "State")
public class State {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    private  StateName Name;

    public State() {
    }

    public State(StateName name) {
        this.Name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StateName getName() {
        return Name;
    }

    public void setName(StateName name) {
        this.Name = name;
    }
}
