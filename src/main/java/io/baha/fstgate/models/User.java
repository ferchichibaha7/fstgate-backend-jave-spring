package io.baha.fstgate.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.baha.fstgate.message.UserProfile;
import io.baha.fstgate.models.audit.DataAudit;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        })
})

public class User extends DataAudit {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String name;

    @NotBlank
    @Size(max = 15)
    private String username;

    @NaturalId
    @NotBlank
    @Size(max = 40)
    private String email;

    @NotBlank
    @Size(max = 100)
    private String password;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Role> roles = new HashSet<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_state",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "state_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<State> states = new HashSet<>();

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "user_id")
    public List<Prev> prevs = new ArrayList<>();

    private boolean isEnabled;
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "ppic_id")
    private Ppic Ppic;


    public User() {
        super();
        isEnabled = false;
    }

    public User(@NotBlank @Size(max = 40) String name, @NotBlank @Size(max = 15) String username, @NotBlank @Size(max = 40) @Email String email, @NotBlank @Size(max = 100) String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;

    }

    public Long getId() {
        return id;
    }

    public Ppic getPpic() {
        return this.Ppic;
    }

    public void setPpic(Ppic ppic) {

        this.Ppic = ppic;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<Prev> getPrevs() {
        return prevs;
    }

    public void setPrevs(List<Prev> prevs) {
        this.prevs = prevs;
    }
    public void addPrevs(Prev prev) {
        this.prevs.add(prev);
    }

    public Set<State> getStates() {
        return states;
    }

    public void setStates(Set<State> states) {
        this.states = states;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
