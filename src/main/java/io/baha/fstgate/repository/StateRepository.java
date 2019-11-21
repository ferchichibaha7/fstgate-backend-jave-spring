package io.baha.fstgate.repository;

import io.baha.fstgate.models.Role;
import io.baha.fstgate.models.RoleName;
import io.baha.fstgate.models.State;
import io.baha.fstgate.models.StateName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {


}
