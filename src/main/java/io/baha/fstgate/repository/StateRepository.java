package io.baha.fstgate.repository;

import io.baha.fstgate.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {

    @Query(value = "SELECT state_id from user_state where user_id =?1", nativeQuery = true)
    Optional<Long> gettate(long userid);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user_state set state_id=1 where user_id =?1", nativeQuery = true)
    void ActivateState(long userid);

}
