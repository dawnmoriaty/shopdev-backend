package com.example.shopdev.security.principle;

import com.example.shopdev.model.User;
import com.example.shopdev.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final IUserRepository  userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);;
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return  UserPrincipal.createUser(user);
    }
}
