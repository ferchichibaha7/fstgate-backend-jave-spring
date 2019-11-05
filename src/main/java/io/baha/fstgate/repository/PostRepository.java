package io.baha.fstgate.repository;
import io.baha.fstgate.models.Post;
import io.baha.fstgate.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    Collection<Post> findByCreatedBy(Long id);

    @Override
    Page<Post> findAll(Pageable pageable);

    Page<Post> findByDescription(String desc);
}