package io.baha.fstgate.controllers;
import io.baha.fstgate.exception.ResourceNotFoundException;
import io.baha.fstgate.message.ApiResponse;
import io.baha.fstgate.message.UserIdentityAvailability;
import io.baha.fstgate.message.UserProfile;
import io.baha.fstgate.message.UserSummary;
import io.baha.fstgate.models.Post;
import io.baha.fstgate.models.Role;
import io.baha.fstgate.models.RoleName;
import io.baha.fstgate.models.User;
import io.baha.fstgate.repository.PostRepository;
import io.baha.fstgate.repository.RoleRepository;
import io.baha.fstgate.repository.UserRepository;
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
import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
private RoleRepository roleRepository;





    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName());
        return userSummary;
    }

    @GetMapping("/users/{username}")
    public UserProfile getUserProfile(@PathVariable(value = "username") String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        UserProfile userProfile = new UserProfile(user.getId(), user.getUsername(), user.getName(), user.getCreatedAt());
        return userProfile;
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

}
