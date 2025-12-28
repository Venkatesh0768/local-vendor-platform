package org.localvendor.authservice.security;

import lombok.RequiredArgsConstructor;
import org.localvendor.authservice.exception.UserNotFoundException;
import org.localvendor.authservice.helper.UserPrincipal;
import org.localvendor.authservice.model.User;
import org.localvendor.authservice.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("No User Found By this UserName"));

        return new UserPrincipal(user);
    }

}
