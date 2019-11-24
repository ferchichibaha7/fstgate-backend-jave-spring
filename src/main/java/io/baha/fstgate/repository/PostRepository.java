package io.baha.fstgate.repository;
import io.baha.fstgate.models.Post;
import io.baha.fstgate.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    Collection<Post> findByCreatedBy(Long id);
Collection<Post> findByDescription(String desc);

    @Query(value = "SELECT * from posts where posts.sub_group_id IN (select subgroup.id from subgroup where subgroup.group_id = ?1)", nativeQuery = true)
    Collection<Post> findByGroupOrderByCreatedAtDesc(long groupid);

    @Query(value = "SELECT * from posts where posts.sub_group_id = ?1", nativeQuery = true)
    Collection<Post> GetPostBySub(long subid);

}