package io.baha.fstgate.controllers;


import io.baha.fstgate.exception.AppException;
import io.baha.fstgate.exception.ResourceNotFoundException;
import io.baha.fstgate.message.ApiResponse;
import io.baha.fstgate.message.PostRequest;
import io.baha.fstgate.models.*;
import io.baha.fstgate.repository.*;
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
    @Autowired
    private PrevRepository prevRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private SubGroupRepository subGroupRepository;

//    @GetMapping("/posts")
//    public Page<Post> getAllPosts(Pageable pageable) {
//        return postRepository.findAll(pageable);
//    }
@GetMapping("/posts")
public Collection<Post> GetAllPosts (){
    return  postRepository.findAll();
}


    @GetMapping("profile/posts")
    public Collection<Post> GetAllUserPosts(@CurrentUser UserPrincipal currentUser) {
        Long userid=currentUser.getId();
        return postRepository.findByCreatedBy(userid);
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

    @PostMapping("/posts/{subid}")
    public Post createPost(@Valid @RequestBody PostRequest postRequest,@PathVariable Long subid) {
    Post p=new Post();
    p.setTitle(postRequest.getTitle());
p.setDescription(postRequest.getDescription());
Subgroup sb =subGroupRepository.findById(subid).orElseThrow(() -> new AppException("User Role not set."));
p.setSubgroup(sb);

    return postRepository.save(p);

  }
    @PostMapping("/sub/{grpid}")
    public Subgroup createSub(@PathVariable Long grpid) {
        Group gp=groupRepository.findById(grpid).orElseThrow(() -> new AppException("User Role not set."));
Subgroup sb=new Subgroup("java",gp);
 return subGroupRepository.save(sb);

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
