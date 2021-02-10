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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import pl.integrable.dusterserver.property.CredentialsProperties
import pl.integrable.dusterserver.service.JwtAuthenticationFilter
import pl.integrable.dusterserver.service.JwtTokenService
import java.lang.Exception

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true) // Used to secure endpoints by annotation
// Used to secure endpoints by annotation
class AuthenticationSecurityConfig  : WebSecurityConfigurerAdapter() {

    @Autowired
    val credentialsProperties = CredentialsProperties()

    @Autowired
    lateinit var jwtTokenService : JwtTokenService

    protected override fun configure(authenticationManagerBuilder: AuthenticationManagerBuilder) {

        authenticationManagerBuilder
            .inMemoryAuthentication()
            .withUser("admin")
            .password("{noop}" + credentialsProperties.adminPassword)
            .roles("ADMIN");
    }

    protected override fun configure(httpSecurity: HttpSecurity) {

        httpSecurity
            .csrf().disable()
            .httpBasic()
            .and()
            .authorizeRequests()
            .antMatchers("/api/v1/admin/**").hasRole("ADMIN")
            .antMatchers("/api/v1/content/**").hasRole("ADMIN")
            .antMatchers("/api/v1/**").hasRole("SENSOR")
            .antMatchers("/admin/**").hasRole("ADMIN")
            .antMatchers("/**").permitAll()
            .and()
            .addFilterBefore(
                JwtAuthenticationFilter(jwtTokenService), UsernamePasswordAuthenticationFilter::class.java)
            .formLogin()
            .loginPage("/login")
            .defaultSuccessUrl("/admin")
            .failureUrl("/error")
            .permitAll()
            .and()
            .logout()
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
            .logoutSuccessUrl("/")
            .permitAll();
    }

    @Bean
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager? {
        return super.authenticationManagerBean()
    }
}