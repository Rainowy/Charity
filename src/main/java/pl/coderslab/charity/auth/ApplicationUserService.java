//package pl.coderslab.charity.auth;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import pl.coderslab.charity.Repository.UserRepository;
//
//@Service
//public class ApplicationUserService implements UserDetailsService {
//
//    private final ApplicationUserDao applicationUserDao;
//
//    private UserRepository userRepository;
//
//
//
////    @Autowired
////    public ApplicationUserService(@Qualifier("fake") ApplicationUserDao applicationUserDao) {
////        this.applicationUserDao = applicationUserDao;
////    }
//
//
//    public ApplicationUserService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return applicationUserDao
//                .selectApplicationUserByUserName(username)
//                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", username)));
//    }
//}
