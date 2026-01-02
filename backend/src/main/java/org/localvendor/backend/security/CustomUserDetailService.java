package org.localvendor.backend.security;

import lombok.RequiredArgsConstructor;

import org.localvendor.backend.auth.model.User;
import org.localvendor.backend.auth.repositories.UserRepository;
import org.localvendor.backend.exception.UserNotFoundException;
import org.localvendor.backend.helper.UserPrincipal;
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
