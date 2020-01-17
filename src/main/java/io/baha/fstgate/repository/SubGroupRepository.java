package io.baha.fstgate.repository;

import io.baha.fstgate.models.Group;
import io.baha.fstgate.models.Subgroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface SubGroupRepository extends JpaRepository<Subgroup, Long> {


    @Override
    Optional<Subgroup> findById(Long id);

    @Query(value = "SELECT * from subgroup where group_id =?1 and is_enabled=true ", nativeQuery = true)
    Collection<Subgroup> GetSubGroupsByGroupId(long groupid);

    @Modifying
    @Transactional
    @Query(value = "UPDATE subgroup set is_enabled=false where id =?1", nativeQuery = true)
    void disablesub(long subid);
}
