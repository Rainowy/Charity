package pl.coderslab.charity.service;

import org.hibernate.event.service.spi.EventActionWithParameter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import pl.coderslab.charity.Repository.UserRepository;
import pl.coderslab.charity.entity.User;

import javax.validation.Valid;
import javax.xml.transform.Result;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class HomeService {

    private UserRepository userRepository;

    public HomeService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> userByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> userByFirstName(String name) {
        return userRepository.findByFirstName(name);
    }

    public User userById(Long id) {
        return userRepository.findUserById(id);
    }






    public void existenceValidator(@Valid User user, BindingResult result) {

        Optional<Long> id = Optional.ofNullable(user.getId());

        id.ifPresentOrElse(var -> userNotEmpty(user, result), () -> userEmpty(user, result));

//        result.rejectValue("email", "error.user", "Istnieje już osoba o podanym emailu");
//        EventActionWithParameter<BindingResult, String, String> bindingResultStringStringEventActionWithParameter = BindingResult::rejectValue;


        /** */
//        if  (id.isEmpty()) { //LONG więc null a nie 0 jak w int
//            userByEmail(user.getEmail()).ifPresent(r -> result.rejectValue("email", "error.user", "Istnieje już osoba o podanym emailu"));
//
//            userByFirstName(user.getFirstName()).ifPresent(r -> result.rejectValue("firstName", "error.user", "Istnieje już osoba o podanym imieniu"));

//            if ((userByEmail(user.getEmail()).isPresent())) {
//                result.rejectValue("email", "error.user", "Istnieje już osoba o podanym emailu");
//            }
//            if ((userByFirstName(user.getFirstName()).isPresent())) {
//                result.rejectValue("firstName", "error.user", "Istnieje już osoba o podanym imieniu");
//            }
    }
//        if (id.isPresent()) {
//            User userById = userById(user.getId());
//            System.out.println(userById.getFirstName());
//            System.out.println(userById.getId());
//            if (!userById.getEmail().equals(user.getEmail())) {
//                userByEmail(user.getEmail()).ifPresent(r -> result.rejectValue("email", "error.user", "Istnieje już osoba o podanym emailu"));
//
////                result.rejectValue("email", "error.user", "Istnieje już osoba o podanym emailu");
////                if (findChildrenByEmail(user.getEmail()) != null) {
////
////                }
//            }
//            if (!userById.getFirstName().equals(user.getFirstName())) {
//                userByFirstName(user.getFirstName()).ifPresent(r -> result.rejectValue("firstName", "error.user", "Istnieje już osoba o podanym imieniu"));
//
////                result.rejectValue("name", "error.user", "Istnieje już osoba o podanym imieniu");
////                if (findChildrenByName(user.getName()) != null) {
//
//            }
//        }

    public void userEmpty(User user, BindingResult result) {
        userByEmail(user.getEmail()).ifPresent(r -> result.rejectValue("email", "error.user", "Istnieje już osoba o podanym emailu"));
        userByFirstName(user.getFirstName()).ifPresent(r -> result.rejectValue("firstName", "error.user", "Istnieje już osoba o podanym imieniu"));
    }

    public void userNotEmpty(User user, BindingResult result) {


    }

}
