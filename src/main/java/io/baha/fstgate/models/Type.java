package io.baha.fstgate.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.NaturalId;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "types")
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    private TypeName name;
    @JsonIgnore
    @OneToMany(mappedBy = "type")
    public List<Prev> prevs = new ArrayList<Prev>();

    public Type() {
    }

    public Type(TypeName name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeName getName() {
        return name;
    }

    public void setName(TypeName name) {
        this.name = name;
    }
}
