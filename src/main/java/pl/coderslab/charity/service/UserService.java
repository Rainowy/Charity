package pl.coderslab.charity.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import pl.coderslab.charity.Repository.RoleRepository;
import pl.coderslab.charity.Repository.UserRepository;
import pl.coderslab.charity.entity.User;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Optional;

@Service
public class UserService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    public Optional<User> userByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public Optional<User> userByFirstName(String name) {
        return userRepository.findByFirstName(name);
    }
    public User userById(Long id) {
        return userRepository.findUserById(id);
    }

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User saveUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));
        user.setEnabled(true); /** must be enabled to login */
        return userRepository.save(user);
    }

    public void existenceValidator(@Valid User user, BindingResult result) {

        Optional<Long> userId = Optional.ofNullable(user.getId());
        userId.ifPresentOrElse(var -> userNotEmpty(user, result), () -> userEmpty(user, result));
    }
    public void userEmpty(User user, BindingResult result) {
        userByEmail(user.getEmail()).ifPresent(r -> result.rejectValue("email", "error.user", "Istnieje już osoba o podanym emailu"));
        userByFirstName(user.getFirstName()).ifPresent(r -> result.rejectValue("firstName", "error.user", "Istnieje już osoba o podanym imieniu"));
    }
    public void userNotEmpty(User user, BindingResult result) {
    }
}
