package io.baha.fstgate.repository;

import io.baha.fstgate.models.State;
import io.baha.fstgate.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {
      Optional<User> findById(Long l);
    Optional<User> findByUsernameOrEmail(String email,String username);
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
    @Query(value = "SELECT * FROM users where user_id in(select user_id from user_state where state_id = 2)", nativeQuery = true)
    Collection<User> findPending();

    @Modifying
    @Transactional
    @Query(value = "UPDATE users set is_enabled=true where user_id =?1", nativeQuery = true)
    void EnableUser(long userid);



}
