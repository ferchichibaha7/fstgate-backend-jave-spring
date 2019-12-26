package io.baha.fstgate.controllers;
import io.baha.fstgate.FstgateApplication;
import io.baha.fstgate.exception.AppException;

import io.baha.fstgate.message.ApiResponse;
import io.baha.fstgate.message.JwtAuthenticationResponse;
import io.baha.fstgate.message.LoginRequest;
import io.baha.fstgate.message.SignUpRequest;
import io.baha.fstgate.models.*;
import io.baha.fstgate.repository.*;
import io.baha.fstgate.security.JwtTokenProvider;
import io.baha.fstgate.services.EmailSenderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger LOGGER = LogManager.getLogger(FstgateApplication.class);


    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    StateRepository stateRepository;

    @Autowired
    TypeRepository typeRepository;

    @Autowired
    GroupRepository groupRepository;


    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private EmailSenderService emailSenderService;


    @GetMapping("/groups")
    public Collection<Group> getAllGroups (){
        return  groupRepository.findAllByOrderByNameDesc();
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        LOGGER.info(loginRequest.getUsername()+userDetails.getAuthorities()+"--SIGNIN");

        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }
        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), signUpRequest.getPassword());

        Type type=null;
        Group group=null;
        if (signUpRequest.getRole()==2){
            group = groupRepository.findByName("SRT")
                    .orElseThrow(() -> new AppException("Group with id 2 not exist"));
            ;
        }
        else{
            group=groupRepository.findById(signUpRequest.getGroup())
                    .orElseThrow(() -> new AppException("Please select a group!"));
        }

        Role userRole =roleRepository.findById(signUpRequest.getRole())
                .orElseThrow(() -> new AppException("User Role not set."));
        user.setRoles(Collections.singleton(userRole));
if (userRole.getName()==RoleName.ROLE_PROF ||userRole.getName()==RoleName.ROLE_ADMIN){
    type=typeRepository.findByName(TypeName.TYPE_RESP)
            .orElseThrow(() -> new AppException("User Type not set."));
}
else {
   type=typeRepository.findByName(TypeName.TYPE_NORM)
            .orElseThrow(() -> new AppException("User Type not set."));
}
Prev userPrev=new Prev(user,group,type);
user.addPrevs(userPrev);
        user.setEnabled(false);
user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (userRole.getName()==RoleName.ROLE_PROF){
            State userState =stateRepository.findById((long)2)
                    .orElseThrow(() -> new AppException("User State not set."));
            user.setStates(Collections.singleton(userState));

        }
        else{
            State userState = stateRepository.findById((long) 1).orElseThrow(() -> new AppException("User State not set."));
           user.setStates(Collections.singleton(userState));
       }





        User result = userRepository.save(user);
        ConfirmationToken confirmationToken = new ConfirmationToken(user);

        confirmationTokenRepository.save(confirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom("chand312902@gmail.com");
        mailMessage.setText("To confirm your account, please click here : "
                + "http://localhost:4200/#/confirm-account/" + confirmationToken.getConfirmationToken());

        emailSenderService.sendEmail(mailMessage);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();
        LOGGER.info(result.getUsername()+"["+userRole.getName()+"]"+"--SIGNUP");
        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }


    @RequestMapping(value = "/confirm-account/{token}", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> confirmUserAccount(@PathVariable String token) {

        ConfirmationToken tokenn = confirmationTokenRepository.findByConfirmationToken(token)
                .orElseThrow(() -> new AppException("token doesnt exist."));
        ;

        if (tokenn != null) {
            User user = userRepository.findByEmail(tokenn.getUser().getEmail())
                    .orElseThrow(() -> new AppException("wrong email"));
            ;
            userRepository.EnableUser(user.getId());
            return new ResponseEntity<>(new ApiResponse(true, "account verified!"),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse(false, "Link invalid or broken!"),
                    HttpStatus.BAD_REQUEST);
        }


    }
}
