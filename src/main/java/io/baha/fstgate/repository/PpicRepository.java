package io.baha.fstgate.repository;

import io.baha.fstgate.models.Ppic;
import io.baha.fstgate.models.Role;
import io.baha.fstgate.models.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PpicRepository extends JpaRepository<Ppic, Long> {

    Optional<Ppic> findByName(String name);
}
