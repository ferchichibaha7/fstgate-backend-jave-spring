package io.baha.fstgate.repository;

import io.baha.fstgate.models.Group;
import io.baha.fstgate.models.Subgroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubGroupRepository extends JpaRepository<Subgroup,Long>{


    @Override
    Optional<Subgroup> findById(Long id);
}
