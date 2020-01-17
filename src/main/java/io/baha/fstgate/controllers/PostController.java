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

    @GetMapping("posts/group/{groupid}")
    public Collection<Post> getPostsByGroup(@PathVariable Long groupid) {
        return postRepository.GetPostByGroup(groupid);
    }

    @GetMapping("posts/subgroup/{subid}")
    public Collection<Post> getPostsBySubGroup(@PathVariable Long subid) {
        return postRepository.GetPostBySub(subid);
    }

    @GetMapping("/subgroups/{subId}")
    public Optional<Subgroup> getSubgroupById(@PathVariable Long subId) {
        return subGroupRepository.findById(subId);
    }

    @GetMapping("/posts/{postId}")
    public Optional<Post> getPost(@PathVariable Long postId) {
        return postRepository.findById(postId);
    }

    @PostMapping("/posts/{subid}")
    public Post createPost(@Valid @RequestBody PostRequest postRequest, @PathVariable Long subid, @CurrentUser UserPrincipal currentUser) {
        Post p = new Post();
        if (postRequest.getTitle() == "" || postRequest.getDescription() == "")
            throw new AppException("invalid post request");

    p.setTitle(postRequest.getTitle());
        p.setDescription(postRequest.getDescription());
        p.setUsername(currentUser.getName());
Subgroup sb =subGroupRepository.findById(subid).orElseThrow(() -> new AppException("User Role not set."));
        Collection<Prev> pv = prevRepository.gettate(currentUser.getId(), sb.getGroup().getId());

        if (pv.isEmpty())
            throw new AppException("you are not allowd" + currentUser.getId() + "sb id:" + sb.getId() + "prev:");
        else {
            p.setSubgroup(sb);
            return postRepository.save(p);
        }

    }

    @PostMapping("/sub/{grpid}/{subname}")
    public Subgroup createSub(@PathVariable Long grpid, @PathVariable String subname) {
        Group gp = groupRepository.findById(grpid).orElseThrow(() -> new AppException("User Role not set."));
        Subgroup sb = new Subgroup(subname, gp);
        sb.setEnabled(true);
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
