package io.baha.fstgate.repository;
import io.baha.fstgate.models.Group;
import io.baha.fstgate.models.User;
import io.baha.fstgate.models.Role;
import io.baha.fstgate.models.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group,Long>{
    Optional<Group> findByName(String GroupName);

    @Override
    Optional<Group> findById(Long Long);

    Collection<Group>findAllByOrderByNameDesc();

}
