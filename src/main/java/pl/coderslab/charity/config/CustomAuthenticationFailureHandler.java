package pl.coderslab.charity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

//    @Qualifier("messageSource")

    @Qualifier("messageSource")
    @Autowired
    private MessageSource messages;

    @Autowired
    private LocaleResolver localeResolver;

//    public CustomAuthenticationFailureHandler(@Qualifier("messageSource") MessageSource messages, LocaleResolver localeResolver) {
//        this.messages = messages;
//        this.localeResolver = localeResolver;
//    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {

        setDefaultFailureUrl("/login?error");

        super.onAuthenticationFailure(request, response, exception);

        Locale locale = localeResolver.resolveLocale(request);

        String errorMessage = messages.getMessage("message.badCredentials", null, Locale.getDefault());
        System.out.println(exception.getMessage() + "EXCEPTION");

        if (exception.getMessage().equalsIgnoreCase("User is disabled")) {
            errorMessage = messages.getMessage("auth.message.disabled", null, Locale.getDefault());
        } else if (exception.getMessage().equalsIgnoreCase("User account has expired")) {
            errorMessage = messages.getMessage("auth.message.account.expired", null, locale);

        }

        request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, errorMessage);
    }
//    @Override
//    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//        setDefaultFailureUrl("/login?error");
//        super.onAuthenticationFailure(request, response, exception);
//        String errorMessage = "Invalid username and/or password!";
//
//        if (exception.getMessage().equalsIgnoreCase("User is disabled")) {
//            errorMessage = "User account is disabled! Check user e-mail to activate the account.";
//        } else if (exception.getMessage().equalsIgnoreCase("User account has expired")) {
//            errorMessage = "User account has expired. Please contact our support team.";
//        } else if (exception.getMessage().equalsIgnoreCase("User account is locked")) {
//            errorMessage = "User account is banned. Please contact our support team.";
//        }
//        request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, errorMessage);
//    }

}