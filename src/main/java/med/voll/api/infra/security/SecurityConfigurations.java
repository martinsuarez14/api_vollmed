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

/**
 * CLASE PARA CONFIGURAR LA SEGURIDAD DE LA APP
 * @Configuration // Clase de configuracion.
 * @EnableWebSecurity // Habilitamos el modulo Web Security para esta configuración, así sobreescribimos la autenticación.
 * */
@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter;

    // HACEMOS A LA SESSION STATELESS para APIS, no guardamos los datos de session
    /**
     * .csrf().diable() Desabilitamos la protección CSRF de Spring Security para imponer la nuestra.
     * .sessionManagement() Permite gestionar y configurar las sesiones de la aplicación
     * .sessionCreationPolicy(SessionCreationPolicy.STATELESS) Indicamos a Spring que el tipo de Sesión será STATELES
     * significa que no se mantendrá iniciada una sesión en la APP. La autenticación se basa en JWT.
     * .and() se utiliza para encadenar multiples configuraciones a la APP
     * .authorizeHttpRequests() Permite definir que solicitudes o roles serán permitidos
     * .requestMatchers(...).permiteAll() Damos todos los permisos a los métodos POST de /login y /login/registrar
     * .anyRequest().authenticated() Definimos que cualquier otra solicitud requiere de autenticación.
     * .addFilterBefore() Se utiliza para agregar un filtro personal antes del filtro de Spring Security.
     *
     * */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                    .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authorizeHttpRequests()
                        .requestMatchers(HttpMethod.POST, "/login/**", "/docs/**", "/v3/api-docs/**")
                            .permitAll()
                        .requestMatchers(HttpMethod.GET, "/docs/**", "/v3/api-docs/**")
                            .permitAll()
                        .anyRequest()
                            .permitAll()
                .and()
                    .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    /**
     * CREAMOS UN AuthenticationManager para poder recuperarlo en otras partes de la APP
     * .getAuthenticationManager() se usa para recuperar una instacia del método de autenticación de la APP
     * en otras partes de la APP y trabajar con ello.
     * */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Método para cifrar contraseñas a la hora de guardarlas en la BD
     * */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     *  APLICAR CUANDO TENGAMOS EL MODELO DE ROLES USER Y ADMIN
     *  SOLO LOS ADMINS PUEDEN BORRAR MÉDICOS Y PACIENTES
     *      @Bean
     *      public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
     *      return http.csrf().disable()
     *      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
     *      .and().authorizeRequests()
     *      .antMatchers(HttpMethod.POST, "/login").permitAll()
     *      .antMatchers(HttpMethod.DELETE, "/medicos").hasRole("ADMIN")
     *      .antMatchers(HttpMethod.DELETE, "/pacientes").hasRole("ADMIN")
     *      .anyRequest().authenticated()
     *      .and().addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
     *      .build();
     *      }
     */
}
