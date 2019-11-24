package io.baha.fstgate.security;

import io.baha.fstgate.FstgateApplication;
import io.baha.fstgate.exception.AppException;
import io.baha.fstgate.models.State;
import io.baha.fstgate.models.User;
import io.baha.fstgate.repository.StateRepository;
import io.baha.fstgate.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private static final Logger LOGGER = LogManager.getLogger(FstgateApplication.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    StateRepository stateRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail)
            throws UsernameNotFoundException {
        // Let people login with either username or email
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email : " + usernameOrEmail)
                );

        Long s = stateRepository.gettate(user.getId()).orElseThrow(() -> new AppException("state wrong"));
        ;

        if (s == 2) throw new AppException("Account not approved yet");


        return UserPrincipal.create(user);
    }


    // This method is used by JWTAuthenticationFilter
    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id : " + id)
        );

        return UserPrincipal.create(user);
    }

}
