package io.baha.fstgate.repository;
import io.baha.fstgate.models.Group;
import io.baha.fstgate.models.Prev;
import io.baha.fstgate.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
public interface PrevRepository extends  JpaRepository<Prev,Long> {
Collection<Prev> findByUser(User u);
boolean existsByGroupIdAndUserId(Long group,Long id);
}
