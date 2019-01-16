package oktenweb.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class Security extends WebSecurityConfigurerAdapter{

    @Qualifier("userServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

    // the method PasswordEncoder works when configure(AuthenticationManagerBuilder auth is commented
    //securityPart5
    @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

    // the method below is important!! It allows to simply handle the project with "user" and "{noop}pass"
    //securityPart1
//     @Override
//         protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//             auth
//                     .inMemoryAuthentication()
//                     .withUser("user")
//                     .password("{noop}pass")
//                     //.password(passwordEncoder().encode("pass"))
//                     .roles("ADMIN");
//         }

      //securityPart6
        @Bean
        public DaoAuthenticationProvider authenticationProvider() {
            DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
            provider.setUserDetailsService(userDetailsService);
            provider.setPasswordEncoder(passwordEncoder());
            return provider;
        }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/home", "/saveUser").permitAll() // everyone can access on url: "/", "/home"
                .anyRequest().authenticated() // other url need auth
                .antMatchers("/admin/**").hasRole("ADMIN")
                .and()
                .formLogin()
                .loginPage("/login")
                .successForwardUrl("/successURL")//handle with post mapping in controller
                .failureUrl("/login?error").permitAll()
                //.permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).
                logoutSuccessUrl("/login")
                .permitAll();
    }

    //securityPart7
     private InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> inMemoryConfigurer() {
             return new InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder>();
         }

         @Autowired
         public void configureGlobal(AuthenticationManagerBuilder auth,
                                     AuthenticationProvider provider) throws Exception {
             inMemoryConfigurer()
                     .withUser("admin")
                     .password("{noop}admin")
                     .authorities("ADMIN")
                     .and()
                     .configure(auth);
             auth.authenticationProvider(provider);

         }
}
