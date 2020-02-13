package pl.coderslab.charity.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import pl.coderslab.charity.Repository.UserRepository;
import pl.coderslab.charity.entity.User;

import javax.validation.Valid;
import java.util.Optional;

@Service
public class HomeService {

    private UserRepository userRepository;

    public HomeService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional <User> userByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public Optional <User> userByFirstName(String name){
        return userRepository.findByFirstName(name);
    }

    public Optional <User> userById(Long id){
        return userRepository.findById(id);
    }

    public void existenceValidator(@Valid User user, BindingResult result) {
        if (user.getId() == null) { //LONG więc null a nie 0 jak w int
            if ((userByEmail(user.getEmail()).isPresent())) {
                result.rejectValue("email", "error.user", "Istnieje już osoba o podanym emailu");
            }
            if ((userByFirstName(user.getFirstName()).isPresent())) {
                result.rejectValue("firstName", "error.user", "Istnieje już osoba o podanym imieniu");
            }
        }
//        if (user.getId() != 0) {
//            User userById = userById(user.getId()).get();
//            if (!userById.getEmail().equals(user.getEmail()) && (userByEmail(user.getEmail()).isPresent())) {
//                result.rejectValue("email", "error.user", "Istnieje już osoba o podanym emailu");
////                if (findChildrenByEmail(user.getEmail()) != null) {
////
////                }
//            }
//            if (!userById.getFirstName().equals(user.getFirstName()) && (userByFirstName(user.getEmail()).isPresent())) {
//                result.rejectValue("name", "error.user", "Istnieje już osoba o podanym imieniu");
////                if (findChildrenByName(user.getName()) != null) {
////
////                }
//            }
//        }
    }

}
