package ca.uvic.ecg;

import ca.uvic.ecg.token.JwtAuthenticationFilter;
import ca.uvic.ecg.token.JwtAuthorizationFilter;
import ca.uvic.ecg.authentication.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()

                .authorizeRequests()
                .antMatchers("/v1/{clinic}/nurses", "/v1/public/**", "/v1/{clinic}/patient/ecg-test/ecg-raw-data",
                        "/v1/{clinic}/phones", "/v1/{clinic}/phones/*").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtAuthorizationFilter(authenticationManager()))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //               .authorizeRequests()
                //             .antMatchers(HttpMethod.POST,"/rest/ecg/login").permitAll()
                //           .anyRequest().authenticated()
                //         .and()
                .formLogin()
                .loginPage("/v1/public/unauthorized")
                .failureUrl("/v1/public/unauthorized")
                .permitAll()
                .and()
                .logout()
                .permitAll();

    }

    //@Override
    //public void configure(WebSecurity webSecurity){
    //    webSecurity.ignoring().antMatchers(HttpMethod.POST, "/rest/ecg/login/{name}/{password}");
    //}

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(bCryptPasswordEncoder());
        //auth.inMemoryAuthentication()
        //      .withUser("user1")
        //    .password(bCryptPasswordEncoder().encode("password"))
        //  .authorities("ROLE_USER");
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());

        return source;
    }

}