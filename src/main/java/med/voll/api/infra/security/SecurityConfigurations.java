package med.voll.api.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// CLASE PARA CONFIGURAR LA SEGURIDAD DE LA APP
@Configuration // clase de configuracion
@EnableWebSecurity // habilitame el modulo web security para esta configuración. Así sobreescribimos la autenticación
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter;

    // HACEMOS A LA SESSION STATELESS para APIS, no guardamos los datos de session
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // indicamos a spring el tipo de session
                .and().authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/login").permitAll() // authorizamos todos los requests a /login
                .anyRequest() // los demás necesitan autenticarse
                .authenticated()
                .and()
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class) // valida que el usuario que inicia session existe
                .build();
    }
/**
 *  APLICAR CUANDO TENGAMOS EL MODELO DE ROLES USER Y ADMIN
 *  SOLO LOS ADMINS PUEDEN BORRAR MÉDICOS Y PACIENTES
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.DELETE, "/medicos").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/pacientes").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and().addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
 */

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
