package pl.coderslab.charity.service;

import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import pl.coderslab.charity.Repository.RoleRepository;
import pl.coderslab.charity.Repository.UserRepository;
import pl.coderslab.charity.Repository.VerificationTokenRepository;
import pl.coderslab.charity.dto.UserDto;
import pl.coderslab.charity.entity.User;
import pl.coderslab.charity.entity.VerificationToken;
import pl.coderslab.charity.event.OnRegistrationCompleteEvent;
import pl.coderslab.charity.userStore.LoggedUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

@Service
public class UserService implements Confirmable {


    private String UPLOADED_FOLDER = "/opt/files/";
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private HttpServletRequest request;
    private ApplicationEventPublisher eventPublisher;
    private VerificationTokenRepository tokenRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, HttpServletRequest request, ApplicationEventPublisher eventPublisher, VerificationTokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.request = request;
        this.eventPublisher = eventPublisher;
        this.tokenRepository = tokenRepository;
    }

    public Optional<User> userByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public String currentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    private LoggedUser getLoggedUser() {
        HttpSession session = request.getSession(false);
        return (LoggedUser) session.getAttribute("loggedUser");
    }

    public User getCurrentUser() {
        return userById(getLoggedUser().getUserId()).get();
    }

    public UserDto getCurrentUserDto() {
        User user = userById(getLoggedUser().getUserId()).get();
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(user, UserDto.class);
        return userDto;
    }

    public String mailHash() {
        String mailToHash = getLoggedUser().getUsername().trim();
        return md5Hex(mailToHash).toLowerCase();
    }

    public Optional<User> userByFirstName(String name) {
        return userRepository.findByFirstName(name);
    }

    public Optional<User> userById(Long id) {
        return userRepository.findUserById(id);
    }

    public User saveUser(UserDto userDto) {

        User user = getEntity(userDto);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));

        try {
            userRepository.save(user);
            String appUrl = request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user,
                    request.getLocale(), appUrl));
        } catch (RuntimeException ex) {
            System.out.println(ex + "MAIL ERROR");
        }
        return user;
    }

    public void updateUser(UserDto userDto) {

        User user = getEntity(userDto);

        Optional<String> formPass = Optional.ofNullable(user.getPassword()).filter(s -> !s.isEmpty());  /** if formPass empty don't change password **/
        formPass.ifPresentOrElse(
                password -> user.setPassword(passwordEncoder.encode(password)),
                () -> user.setPassword(getCurrentUser().getPassword()));


        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));
        activateUser(user);
    }

    private User getEntity(UserDto userDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(userDto, User.class);
    }

    public void activateUser(User user) {
        user.setEnabled(true);
        user.setNotExpired(true);
        userRepository.save(user);
    }

    public void saveAvatar(MultipartFile file) {
        try {
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);
//            redirectAttributes.addFlashAttribute("message",
//                    "You successfully uploaded '" + file.getOriginalFilename() + "'");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void existenceValidator(@Valid UserDto userDto, BindingResult result) {

        User user = getEntity(userDto);

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

    public List<User> findAllAdmins() {
        return userRepository.findAllByRoles(roleRepository.findByName("ROLE_ADMIN"));
    }

    @Override
    public User getUser(String verificationToken) {
        User user = tokenRepository.findByToken(verificationToken).getUser();
        return user;
    }

    @Override
    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

    @Override
    public VerificationToken getVerificationToken(String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }
}
