package com.control.empleados;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	
	@Bean
	public PasswordEncoder passEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	@Bean
	protected UserDetailsService userDetailsService() {
		UserDetails usuario1 = User.withUsername("admin")
				.password("$2a$10$HWyXxu1wKY64Q7GZK2UNeej7alROfrRwga4dh83dGnE6mWJA0J752")
				.roles("ADMIN").build();
		UserDetails usuario2 = User.withUsername("user")
				.password("$2a$10$HWyXxu1wKY64Q7GZK2UNeej7alROfrRwga4dh83dGnE6mWJA0J752")
				.roles("USER").build();
		return new InMemoryUserDetailsManager(usuario1, usuario2);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/").hasAnyRole("ADMIN","USER")
		.antMatchers("/form/*","/eliminar/*").hasRole("ADMIN")
		.anyRequest().authenticated()
		.and().formLogin().loginPage("/login").permitAll()
		.and().logout().permitAll();
	}
	
}
