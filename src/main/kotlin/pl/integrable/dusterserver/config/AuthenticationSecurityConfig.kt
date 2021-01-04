package pl.integrable.dusterserver.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import java.lang.Exception

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true) // Used to secure endpoints by annotation
// Used to secure endpoints by annotation
class AuthenticationSecurityConfig @Autowired constructor(
)  : WebSecurityConfigurerAdapter() {

    protected override fun configure(authenticationManagerBuilder: AuthenticationManagerBuilder) {

        authenticationManagerBuilder
            .inMemoryAuthentication()
            .withUser("user")
            .password("{noop}password")
            .roles("USER");
    }

    protected override fun configure(httpSecurity: HttpSecurity) {

        httpSecurity
            .csrf().disable()
            .httpBasic()
            .and()
            .authorizeRequests()
            .antMatchers("/**").permitAll()
            .and()
            //.addFilterBefore(new JwtAuthenticationFilter(jwtTokenService), UsernamePasswordAuthenticationFilter.class)
            .formLogin()
            .loginPage("/login")
            .defaultSuccessUrl("/")
            .failureUrl("/noaccount")
            .permitAll()
            .and()
            .logout()
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
            .logoutSuccessUrl("/")
            .permitAll();
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder? {
        return BCryptPasswordEncoder()
    }

    @Bean
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager? {
        return super.authenticationManagerBean()
    }
}