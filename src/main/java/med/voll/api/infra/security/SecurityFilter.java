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

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        System.out.println("El filtro está siendo llamado...");

        // Primero obtener el token del header
        var authHeader = request.getHeader("Authorization");

        // Comprobamos que no llegue vacío o nulo
        if (authHeader != null) { // si el token no es nulo se ejecuta el filtro

            // Reemplazamos el "Bearer " por vacío
            var token = authHeader.replace("Bearer ", "");
            // Debemos evaluar si el token es válido
            var nombreUsuario = tokenService.getSubject(token);// extraemos el username
            if (nombreUsuario != null) {
                // Token válido
                // Forzamos un inicio de session para spring
                var usuario = usuarioRepository.findByLogin(nombreUsuario);
                var authentication = new UsernamePasswordAuthenticationToken(
                        usuario,
                        null,
                        usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication); // este login es válido para mi
            }
        }
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