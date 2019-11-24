package io.baha.fstgate.repository;

import io.baha.fstgate.models.State;
import io.baha.fstgate.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {
      Optional<User> findById(Long l);
    Optional<User> findByUsernameOrEmail(String email,String username);
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    Optional<State> findFirstById(Long id);
}
