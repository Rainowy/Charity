package pl.coderslab.charity.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.SneakyThrows;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeToken;
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
import pl.coderslab.charity.userStore.LoggedUser;
import pl.coderslab.charity.utils.DtoUtils;
import pl.coderslab.charity.utils.OnRegistrationCompleteEvent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

@Service
public class UserService implements Confirmable {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private HttpServletRequest request;
    private ApplicationEventPublisher eventPublisher;
    private VerificationTokenRepository tokenRepository;
    private AmazonS3 s3client;
    private String awsS3Bucket;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, HttpServletRequest request, ApplicationEventPublisher eventPublisher, VerificationTokenRepository tokenRepository, AmazonS3Service amazonS3Service) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.request = request;
        this.eventPublisher = eventPublisher;
        this.tokenRepository = tokenRepository;
        this.s3client = amazonS3Service.getS3client();
        this.awsS3Bucket = amazonS3Service.getAwsS3Bucket();
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
        return (UserDto) new DtoUtils().convertToDto(user, new UserDto());
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

        User user = (User) new DtoUtils().convertToEntity(new User(), userDto);

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

        User user = (User) new DtoUtils().convertToEntity(new User(), userDto);

        Optional<String> formPass = Optional.ofNullable(user.getPassword()).filter(s -> !s.isEmpty());  /** if formPass empty don't change password **/
        formPass.ifPresentOrElse(
                password -> user.setPassword(passwordEncoder.encode(password)),
                () -> user.setPassword(getCurrentUser().getPassword()));

        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));
        activateUser(user);
    }

    public void activateUser(User user) {
        user.setEnabled(true);
        user.setNotExpired(true);
        userRepository.save(user);
    }

    @SneakyThrows
    public void saveAvatar(MultipartFile file) {

        ObjectMetadata data = new ObjectMetadata();
        data.setContentType(file.getContentType());
        data.setContentLength(file.getSize());

        s3client.putObject(new PutObjectRequest(awsS3Bucket,
                file.getOriginalFilename(),
                file.getInputStream(),
                data)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    public String avatarUrl(String userAvatar) {
        return s3client.getUrl(awsS3Bucket, userAvatar).toString();
    }

    public void existenceValidator(@Valid UserDto userDto, BindingResult result) {

        User user = (User) new DtoUtils().convertToEntity(new User(), userDto);

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

    public List<UserDto> findAllAdmins() {
        List<User> admins = userRepository.findAllByRoles(roleRepository.findByName("ROLE_ADMIN"));
        return new DtoUtils().convertToDtoList(admins, new TypeToken<List<UserDto>>() {
        }.getType(), skipPasswordAndAvatar());
    }

    private PropertyMap<User, UserDto> skipPasswordAndAvatar() {
        return new PropertyMap<>() {
            @Override
            protected void configure() {
                skip(destination.getAvatar());
                skip(destination.getPassword());
            }
        };
    }

    public void deleteByUser() {
        userRepository.deleteById(1L);
    }

    @Override
    public User getUser(String verificationToken) {
        return tokenRepository.findByToken(verificationToken).getUser();
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