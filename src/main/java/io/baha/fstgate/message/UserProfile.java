package io.baha.fstgate.message;

import java.time.Instant;

public class UserProfile {
    private Long id;
    private String username;
    private String name;
    private Long ppic;

    public UserProfile(Long id, String username, String name, Long ppic) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.ppic = ppic;
    }

    public Long getPpic() {
        return ppic;
    }

    public void setPpic(Long ppic) {
        this.ppic = ppic;
    }

    public Long getId() {
        return id;
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


}
