package pl.coderslab.charity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.charity.Repository.UserRepository;
import pl.coderslab.charity.entity.Role;
import pl.coderslab.charity.entity.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service("userDetailsService")
@Transactional
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private IUserService service;
//
//    @Autowired
//    private MessageSource messages;

//    @Autowired
//    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String userInput)
            throws UsernameNotFoundException {

        Optional<User> userByEmail = userRepository.findByEmail(userInput);
        Optional<User> userByName = userRepository.findByFirstName(userInput);

        if (userByEmail.isPresent()) {
            return getUserDetails(userByEmail.get());
        } else if (userByName.isPresent()) {
            return getUserDetails(userByName.get());
        } else {
            throw new UsernameNotFoundException(String.format("Username %s not found", userInput));
        }
    }

    private UserDetails getUserDetails(User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                user.isNotExpired(),
                true,
                true,
                getAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(
            Collection<Role> roles) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            role.getPrivileges().stream()
                    .map(p -> new SimpleGrantedAuthority(p.getName()))
                    .forEach(authorities::add);
        }
        return authorities;
    }
}
