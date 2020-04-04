package pl.coderslab.charity.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import pl.coderslab.charity.config.UrlAuthenticationSuccessHandler;
import pl.coderslab.charity.service.MyUserDetailsService;



@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
//@EnableConfigurationProperties(JwtConfig.class)
//mówi że ma korzystać z autoryzacji w metodach podpisanych anotacją @PreAuthorize zamiast .antMatchers
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private  final PasswordEncoder passwordEncoder;
//    private final ApplicationUserService applicationUserService;
//    private MyUserDetailsService myUserDetailsService;
//    private final SecretKey secretKey;
//    private final JwtConfig jwtConfig;
    private UserDetailsService userDetailsService;

//    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, ApplicationUserService applicationUserService, SecretKey secretKey, JwtConfig jwtConfig) {
//        this.passwordEncoder = passwordEncoder;
//        this.applicationUserService = applicationUserService;
//        this.secretKey = secretKey;
//        this.jwtConfig = jwtConfig;
//    }

    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, MyUserDetailsService myUserDetailsService, UserDetailsService userDetailsService) {
        this.passwordEncoder = passwordEncoder;
//        this.myUserDetailsService = myUserDetailsService;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler() {
        return new UrlAuthenticationSuccessHandler();
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /** WAŻNE KOLEJNOŚĆ ANT MATCHERS MA ZNACZENIE, JEŻELI WCZEŚNIEJ ZOSTANIE ZABLOKOWANY TO DALEJ NIE PÓJDZIE **/
        http
//                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) //Cookie nie będzie możliwe do modyfikacji przez javascript
//                .and()//chroni api , cross site request forgery
                .csrf().disable()

                /** JWT **/
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//teraz sesja nie będzie przechowywana w bazie
//                .and()
//                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(),jwtConfig,secretKey)) //pierwszy filtr JWT rejestrujemy
//                .addFilterAfter(new JwtTokenVerifier(secretKey,jwtConfig),JwtUsernameAndPasswordAuthenticationFilter.class)  //drugi filtr JWT rejestrujemy

//        "register"
//        "/", "index" ,"form" ,
//        "/home/*",
        /**   */

                .authorizeRequests()
                .antMatchers("/", "index",  "/donation/*",  "/register", "/static/**","/resources/**", "/css/**", "/js/**", "/images/**", "/webjars/**", "/scss/**", "/vendor/**", "/opt/**", "/files/**", "/home/**", "/tomek/**" , "/Desktop/**", "/pages/**").permitAll() //wszystkie wymienione będą dopuszczone
//                .antMatchers("/login").permitAll()
//                .antMatchers("/api/**").hasRole("USER") // wszystko z takim URL ** musi mieć rolę student = ROLE BASED AUTHENTICATION
//                .antMatchers("/courses").hasRole("USER")
//                .antMatchers("/courses").hasAuthority("USER")
                /** Działa */
//                .antMatchers("/user/**").hasAuthority("READ_PRIVILEGE")
//                .antMatchers("/admin/**").hasAuthority("WRITE_PRIVILEGE")
//                .antMatchers("/user/**").hasRole("USER")
//                .antMatchers("/admin/**").hasRole("ADMIN")
//                .antMatchers("/user/**").hasAuthority("ROLE_USER")
                /** */
//
//                .antMatchers(HttpMethod.DELETE,"/management/api/**").hasAuthority(COURSE_WRITE.getPermission()) //aby skasować musi mieć permission Course_WRITE czyli tylko ADMIN taką ma
////                .antMatchers(HttpMethod.DELETE,"/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.name())
//                .antMatchers(HttpMethod.POST,"/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
//                .antMatchers(HttpMethod.PUT,"/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
//                .antMatchers("/management/api/**").hasAnyRole(ADMIN.name(), ADMINTRAINEE.name()) //Do get musi być rola ADMIN albo ta druga
////                .antMatchers(HttpMethod.GET,"/management/api/**").hasAnyRole(ADMIN.name(), ADMINTRAINEE.name()) //Do get musi być rola ADMIN albo ta druga
                .anyRequest()//każdy request musi być authenticated
                .authenticated()

//                .httpBasic(); //sposób autoryzacji podstawowy, nie można się wylogować

//                .failureUrl("/login?error=true")
        //OD TEGO MOMENTU WPROWADZIŁ JWT
                .and()
                .formLogin() //form based authentication
                    .loginPage("/login").permitAll()//każdy może wejść na ten adres
//                    .defaultSuccessUrl("/courses", true)

                .successHandler(myAuthenticationSuccessHandler())
                //po wejściu zostanie przekierowany na /courses
                    .passwordParameter("password")// password i username = parametry w HTML  name="password" i  name="username"
                .usernameParameter("username");
//                .and()
//                .rememberMe().tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
////                    .userDetailsService(applicationUserService)  //u amigosa nie musi podawać tej linijki tak jakby UserDetailService się auto rejestrował, u mnie musiałem podać ręcznie implementację, ok naprawion, userdetailservice musi się też tak nazywać żeby nadpisać
//                .key("uniqueAndSecret")// defaults to 2 weeks(be    z parametrów) z parametrem tokenValidity rozszerzone do 21 dni oraz z kluczem do hashowania (username oraz expiration time)
//                .rememberMeParameter("remember-me") //parametr w HTMLgit
//                .and()
//                .logout()
//                .logoutUrl("/logout")
//                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")) //Gdy CSRF wyłączony możemy użyć GET, gdy włączony możemy ją skasowaś i defaultowo będzie używany POST
//                .clearAuthentication(true)
//                .invalidateHttpSession(true)
//                .deleteCookies("JSESSIONID", "remember-me")
//                .logoutSuccessUrl("/login");
    }

//    konfigurujemy podłączenie
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }
//    provider
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

//    }
    /** Konfig poniżej bez bazy danych */
    //Dodanie tego Beana nadpisuje standardową autoryzajcję, niezbędna nazwa, hasło, rola. Hasło już nie pokazuje się w Terminalu
//    @Override
//    @Bean
//    public UserDetailsService userDetailsService()  {
//
//        //Jak zbudujemy to poniżej to mamy użytkownika z takimi poświadczeniami
//        UserDetails annaSmithUser = User.builder()
//                .username("annasmith")
//                .password(passwordEncoder.encode("password"))
////                .roles(STUDENT.name()) // ROLE_STUDENT, tutaj zrobiliśmy static import
////                .roles(ApplicationUserRole.STUDENT.name()) to samo co wyżej tylko bez importu
//                .authorities(STUDENT.getGrantedAuthorities())
//                .build();
//
//        UserDetails lindaUser = User.builder()
//                .username("linda")
//                .password(passwordEncoder.encode("password123"))
////                .roles(ADMIN.name()) //tutaj zrobiliśmy static import
//                .authorities(ADMIN.getGrantedAuthorities())
//                .build();
//
//        UserDetails tomUser = User.builder()
//                .username("tom")
//                .password(passwordEncoder.encode("password123"))
////                .roles(ADMINTRAINEE.name()) //tutaj zrobiliśmy static import, ROLE_ADMINTRAINEE
//                .authorities(ADMINTRAINEE.getGrantedAuthorities())
//                .build();
//
//        return new InMemoryUserDetailsManager(annaSmithUser, lindaUser, tomUser);


}
