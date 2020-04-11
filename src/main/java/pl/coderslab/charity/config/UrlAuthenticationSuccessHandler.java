package pl.coderslab.charity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import pl.coderslab.charity.Repository.UserRepository;
import pl.coderslab.charity.entity.User;
import pl.coderslab.charity.userStore.ActiveUserStore;
import pl.coderslab.charity.userStore.LoggedUser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

public class UrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ActiveUserStore activeUserStore;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws IOException {

        handle(request, response, authentication);
        addUserIdToSession(request, authentication); //dodaje userId do sesji
        clearAuthenticationAttributes(request);
    }
    protected void handle(HttpServletRequest request,
                          HttpServletResponse response, Authentication authentication)
            throws IOException {

        String targetUrl = determineTargetUrl(authentication);

//        if (response.isCommitted()) {
//            logger.debug(
//                    "Response has already been committed. Unable to redirect to "
//                            + targetUrl);
//            return;
//        }
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }
    protected String determineTargetUrl(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities
                = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
//            System.out.println(grantedAuthority.getAuthority());
            if (grantedAuthority.getAuthority().equals("ROLE_USER")) {
//            if (grantedAuthority.getAuthority().equals("READ_PRIVILEGE")) {
//                return "user/panel";
                return "donation/form";
//            } else if (grantedAuthority.getAuthority().equals("CHILD")) {
//                return "child/panel";
            } else if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
                return "admin/panel";
            }
        }
        throw new IllegalStateException();
    }

    protected  void addUserIdToSession(HttpServletRequest request, Authentication authentication) throws IOException{
        HttpSession session = request.getSession(false);
        if(session != null) {
            Optional<User> currentUser = userRepository.findByEmail(authentication.getName());
            LoggedUser user =new LoggedUser(authentication.getName(),currentUser.get().getId(),activeUserStore);
            session.setAttribute("loggedUser", user);

//            session.setAttribute("currentId",currentUser.get().getId());
        }
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }


}
