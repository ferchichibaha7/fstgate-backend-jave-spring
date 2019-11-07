package io.baha.fstgate.repository;
import io.baha.fstgate.models.Type;
import io.baha.fstgate.models.TypeName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TypeRepository extends JpaRepository<Type,Long> {
    Optional<Type> findByName(TypeName name);
}
