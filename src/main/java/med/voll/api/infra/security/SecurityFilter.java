package med.voll.api.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import med.voll.api.domain.usuarios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

// CLASE QUE INTERCEPTA REQUESTS ANTES DE QUE LLEGUEN AL CONTROLLER
@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     *  MÉTODO QUE FILTRA LAS SOLICITUDES HTTP ENTRANTES
     *
     *  FilterChain Clase utilizada para establecer un filtro a las solicitudes
     * .getHeader("Authorization") obtiene el valor del Header especificado en los ()
     * .replace() reemplaza el String indicado por otro en el header entrante
     * .tokenService().getSubject(token) extrae el nombre de Usuario del token
     *  UsernamePasswordAuthenticationToken Clase utilizada para encapsular la información de Autenticación de los usuarios
     *  SecurityContextHolder Clase que contiene la información de Autenticación y Autorización de la APP
     * .getContext() utilizado para obtener el contexto de seguridad actual
     * .setAuthentication(authentication) establece la información de Authenticación proporcionada por el Contexto
     * y le pasamos el objeto "authentication" que tiene al usuario, las credenciales y los permisos.
     * filterChain.doFilter(request, response) Permite pasar la solicitud y la respuesta al siguiente filtro en la cadena
     *
     * */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        System.out.println("El filtro está siendo llamado...");

        var authHeader = request.getHeader("Authorization");
        System.out.println("Extraemos el header " + authHeader);

        if (authHeader != null) { // si el token no es nulo se ejecuta el filtro
            System.out.println("Comprobamos si el header es nulo");

            var token = authHeader.replace("Bearer ", "");
            System.out.println("reemplazamos el Bearer");

            var nombreUsuario = tokenService.getSubject(token);
            System.out.println("Extraemos el username " + nombreUsuario);

            if (nombreUsuario != null) {
                System.out.println("Comprobamos que no sea nulo el username");

                var usuario = usuarioRepository.findByLogin(nombreUsuario);
                System.out.println("usuario buscado en la base de datos");

                var authentication = new UsernamePasswordAuthenticationToken(
                        usuario,
                        null,
                        usuario.getAuthorities());
                System.out.println("Forzamos el inicio de sesion");

                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("Damos un login valido");

            }
        }
        System.out.println("El header vino vacío. Pasamos al siguiente filtro");
        filterChain.doFilter(request, response);
    }

}

/**
@WebFilter(urlPatterns = "/api/**")
public class LogFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Requisição recebida em: " + LocalDateTime.now());
        filterChain.doFilter(servletRequest, servletResponse);
    }
}*/