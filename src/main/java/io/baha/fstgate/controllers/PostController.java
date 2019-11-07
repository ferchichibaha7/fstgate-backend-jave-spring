package io.baha.fstgate.controllers;


import io.baha.fstgate.exception.ResourceNotFoundException;
import io.baha.fstgate.message.ApiResponse;
import io.baha.fstgate.models.Post;
import io.baha.fstgate.models.User;
import io.baha.fstgate.repository.PostRepository;
import io.baha.fstgate.repository.UserRepository;
import io.baha.fstgate.security.CurrentUser;
import io.baha.fstgate.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

@RestController
public class PostController {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/posts")
    public Page<Post> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }
    @GetMapping("profile/posts")
    public Collection<Post> getMyPosts(@CurrentUser UserPrincipal currentUser) {
        Long userid=currentUser.getId();
        return postRepository.findByCreatedBy(userid);
    }

    @GetMapping("profile/posts/{desc}")
    public Collection<Post> MyPosts(@PathVariable String desc) {

        return postRepository.findByDescription(desc);
    }
    @GetMapping("profile/{username}/posts")
    public Collection<Post> getPostsByUser(@PathVariable String username) {
      Optional <User> u= userRepository.findByUsername(username);
      Long id=u.get().getId();
        return postRepository.findByCreatedBy(id);
    }
    @GetMapping("/posts/{postId}")
    public Optional <Post>  getPost(@PathVariable Long postId) {
        return postRepository.findById(postId);
    }

    @PostMapping("/posts")
    public Post createPost(@Valid @RequestBody Post post) {
        return postRepository.save(post);
    }


    @PutMapping("/posts/{postId}")
    public Post updatePost(@PathVariable Long postId, @Valid @RequestBody Post postRequest) {
        return postRepository.findById(postId).map(post -> {
            post.setTitle(postRequest.getTitle());
            post.setDescription(postRequest.getDescription());
            return postRepository.save(post);
        }).orElseThrow(() -> new ResourceNotFoundException("PostId " + postId + " not found"));
    }


    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        return postRepository.findById(postId).map(post -> {
            postRepository.delete(post);
            return ResponseEntity.ok().body(new ApiResponse(true, "Post with id: " + postId + " deleted successfully"));
        }).orElseThrow(() -> new ResourceNotFoundException("PostId " + postId + " not found"));
    }
}
