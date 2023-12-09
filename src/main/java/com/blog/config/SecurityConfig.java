package com.blog.config;

import com.blog.security.CustomUserDetailService;
import com.blog.security.JwtAuthenticationEntryPoint;
import com.blog.security.JwtAuthenticationFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig{

	@Autowired
	private  CustomUserDetailService customUserDetailService; 

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

    @SuppressWarnings("removal")
	@Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception  {
//http.csrf().disable().authorizeHttpRequests().anyRequest().authenticated().and().httpBasic();
     http.addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(handling ->{
					try {
						handling
						                                                         .authenticationEntryPoint(this.jwtAuthenticationEntryPoint).and().sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).
						                                                         csrf(csrf -> csrf.disable())
						                                                         .authorizeHttpRequests(auth -> auth.requestMatchers("/api/v1/auth/login").permitAll()					                                                                 .anyRequest().authenticated());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
			//http.authenticationProvider(daoAuthenticatorProvider());
    	   DefaultSecurityFilterChain build = http.build();
         return build;
}
		protected void configure(AuthenticationManagerBuilder auth) throws Exception{
	
	auth.userDetailsService(this.customUserDetailService).passwordEncoder(new BCryptPasswordEncoder());
	}

//    @Bean
//    DaoAuthenticationProvider daoAuthenticatorProvider() {
//	
//	DaoAuthenticationProvider provider =new DaoAuthenticationProvider();
//	provider.setUserDetailsService(this.customUserDetailService);
//    provider.setPasswordEncoder(passwordEncoder());
//    return provider;
//}

    @Bean
    PasswordEncoder passwordEncoder() {
	
	return new BCryptPasswordEncoder();
}

    @Bean
    AuthenticationManager authenticationManagerBean(AuthenticationConfiguration conf) throws Exception {
	
	return conf.getAuthenticationManager();
}
}

