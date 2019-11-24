package io.baha.fstgate.controllers;

import io.baha.fstgate.exception.AppException;
import io.baha.fstgate.exception.ResourceNotFoundException;
import io.baha.fstgate.message.ApiResponse;
import io.baha.fstgate.message.UserIdentityAvailability;
import io.baha.fstgate.message.UserProfile;
import io.baha.fstgate.message.UserSummary;
import io.baha.fstgate.models.*;
import io.baha.fstgate.repository.*;
import io.baha.fstgate.security.CurrentUser;
import io.baha.fstgate.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.io.Console;
import java.util.*;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
private RoleRepository roleRepository;
    @Autowired
    private PrevRepository prevRepository;
    @Autowired
    private SubGroupRepository subGroupRepository;


    @GetMapping("/user/group")
    public Collection<Group> getprevs(@CurrentUser UserPrincipal currentUser) {
      User u =userRepository.findById(currentUser.getId()) .orElseThrow(() -> new ResourceNotFoundException("g"));
    List<Group> l=new ArrayList<>() ;
        prevRepository.findByUser(u).forEach(p-> {
            l.add(p.getGroup());
        });
        return l ;
    }
    @GetMapping("/user/me")
    public UserProfile getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        UserProfile userSummary = new UserProfile(currentUser.getId(), currentUser.getUsername(), currentUser.getName());
        return userSummary;
    }

//    @GetMapping("/users/{username}")
//    public UserProfile getUserProfile(@PathVariable(value = "username") String username) {
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
//        UserProfile userProfile = new UserProfile(user.getId(), user.getUsername(), user.getName(), user.getCreatedAt());
//        return userProfile;
//    }

    @GetMapping("/users/stud/prev")
    public Prev getStudPrev(@CurrentUser UserPrincipal currentUser) {
        Prev Userprev = prevRepository.findFirstByUserId(currentUser.getId()).orElseThrow(() -> new AppException("have no group"));
        return Userprev;
    }

    @GetMapping("/users/prof/prev")
    public Collection<Prev> getProfPrev(@CurrentUser UserPrincipal currentUser) {
        Collection<Prev> Profprevs = prevRepository.findByUserId(currentUser.getId());
        return Profprevs;
    }

    @GetMapping("/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }



    @GetMapping("/{userID}/{roleName}")
    @Secured( {"ROLE_ADMIN","ROLE_PROF","ROLE_RESP"} )
    public User getme(@PathVariable Long postId, @Valid @RequestBody User u) {
        return u;
    }


    @GetMapping("subgroup/group/{groupid}")
    public Collection<Subgroup> GetSubGroupsByGroupId(@PathVariable Long groupid) {
        return subGroupRepository.GetSubGroupsByGroupId(groupid);
    }



}
