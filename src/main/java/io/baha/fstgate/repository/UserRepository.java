package io.baha.fstgate.repository;

import io.baha.fstgate.models.Group;
import io.baha.fstgate.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameOrEmail(String email,String username);
    Optional<User> findByUsername(String username);
    List<User> findByIdIn (List<Long> userIds);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
   List<User> findByGroups(Group g);

}
