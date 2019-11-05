package io.baha.fstgate.controllers;
import io.baha.fstgate.exception.ResourceNotFoundException;
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
import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;


    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public UserPrincipal getme(@CurrentUser UserPrincipal currentUser) {
        return currentUser;
    }
}