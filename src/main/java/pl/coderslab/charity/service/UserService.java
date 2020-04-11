package pl.coderslab.charity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import pl.coderslab.charity.Repository.RoleRepository;
import pl.coderslab.charity.Repository.UserRepository;
import pl.coderslab.charity.entity.User;
import pl.coderslab.charity.userStore.LoggedUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private HttpServletRequest request;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, HttpServletRequest request) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.request = request;
    }

    public Optional<User> userByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public String currentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public User getCurrentUser(){
        HttpSession session = request.getSession(false);
//        Long currentId = (Long) session.getAttribute("currentId");
        LoggedUser aktywny = (LoggedUser) session.getAttribute("currentUser");
//        System.out.println("CURRENT IDz pomocna " + currentId);
        return userById(aktywny.getUserId()).get();
    }

//    public User getCurrentUser() {
////        HttpServletRequest request
////        HttpSession session = request.getSession();
////        Object currentId = session.getAttribute("currentId");
////        return userById((Long) currentId).get();
//return pomocna(request);
//        return userByEmail(currentUserEmail()).get();
//    }

    public Optional<User> userByFirstName(String name) {
        return userRepository.findByFirstName(name);
    }

    public Optional<User> userById(Long id) {
        return userRepository.findUserById(id);
    }

//    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.roleRepository = roleRepository;
//        this.passwordEncoder = passwordEncoder;
//    }

    public User saveUser(User user) {
        Optional<String> formPass = Optional.ofNullable(user.getPassword()).filter(s -> !s.isEmpty());  /** if formPass empty don't change password **/
        formPass.ifPresentOrElse(
                password -> user.setPassword(passwordEncoder.encode(password)),
                () -> user.setPassword(getCurrentUser().getPassword()));

        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));
        user.setEnabled(true);  /** must be enabled to login */
        return userRepository.save(user);
    }

//    public void saveAvatar(MultipartFile file) {
//        String UPLOADED_FOLDER = "/opt/files/";
//        try {
//            // Get the file and save it somewhere
//            byte[] bytes = file.getBytes();
//            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
//            Files.write(path, bytes);
////            redirectAttributes.addFlashAttribute("message",
////                    "You successfully uploaded '" + file.getOriginalFilename() + "'");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public void existenceValidator(@Valid User user, BindingResult result) {
        Optional<Long> userId = Optional.ofNullable(user.getId());
        userId.ifPresentOrElse(var -> userNotEmpty(user, result), () -> userEmpty(user, result));
    }

    public void userEmpty(User user, BindingResult result) {
        userByEmail(user.getEmail()).ifPresent(r -> result.rejectValue("email", "error.user", "Istnieje już osoba o podanym emailu"));
        userByFirstName(user.getFirstName()).ifPresent(r -> result.rejectValue("firstName", "error.user", "Istnieje już osoba o podanym imieniu"));

    }

    public void userNotEmpty(User user, BindingResult result) {
        Optional<User> userById = userById(user.getId());
        if (!userById.get().getEmail().equals(user.getEmail())) {
            userByEmail(user.getEmail()).ifPresent(r -> result.rejectValue("email", "error.user", "Istnieje już osoba o podanym emailu"));
        }
        if (!userById.get().getFirstName().equals(user.getFirstName())) {
            userByFirstName(user.getFirstName()).ifPresent(r -> result.rejectValue("firstName", "error.user", "Istnieje już osoba o podanym imieniu"));
        }
    }
}
