package com.example.jwt.security.auth;

import com.example.jwt.security.config.CacheKey;
import com.example.jwt.domain.exception.UserNotFoundException;
import com.example.jwt.domain.model.User;
import com.example.jwt.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Cacheable(value = CacheKey.USER, key = "#username", cacheManager = "cacheManager")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("PrincipalDetailsService.loadUserByUsername()");
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        return new PrincipalDetails(user);
    }
}
