package pl.coderslab.charity.service;

import org.springframework.stereotype.Service;
import pl.coderslab.charity.Repository.UserRepository;

@Service
public class AdminService {

    UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

//    public List<User> findAllAdmins (String role){
//        return userRepository.findAllByRolesNotNull(role);
//    }
}
