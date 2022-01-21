package it.unibo.msenabler.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.inMemoryAuthentication().withUser("pi")
                .password(passwordEncoder().encode("unibo")).roles("USER");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
         
    }
    
/*
 *     
 */
    //a nice little DSL with which you can configure your FilterChain
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .anyRequest().authenticated()  // (1)	
                .and()
            .formLogin().and()   // (2) authentication methods are allowed
            .httpBasic();  // (3)
        
      //we have stopped the csrf to make post method work
        http.cors().and().csrf().disable();
    }
    
 
    
   /*
    protected void configure(HttpSecurity http) throws Exception {  // (2)
        http
          .authorizeRequests()
            .antMatchers("/", "/home").permitAll() // (3)
            .anyRequest().authenticated() // (4)
            .and()
         .formLogin() // (5)
           .loginPage("/login") // (5)
           .permitAll()
           .and()
        .logout() // (6)
          .permitAll()
          .and()
        .httpBasic(); // (7)
    }
    */
    
}


