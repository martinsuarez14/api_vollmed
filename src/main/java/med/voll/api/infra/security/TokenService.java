package med.voll.api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import med.voll.api.domain.usuarios.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * CLASE ENCARGADA DE GENERAR TOKENS Y DE RECUPERAR EL SUBJECT DE TOKENS
 * */
@Service
public class TokenService {

    /**
     * Inyectamos la llave secreta guardada en application.propieties
     * */
    @Value("${api.security.secret}")
    private String apiSrecret;

    /**
     * MÉTODO QUE RETORNA UN TOKEN USANDO COMO PARÁMETRO A UN OBJETO USUARIO
     * Algorithm.HMAC256(apiSrecret) instanciamos un Algoritmo HMAC256 y como parámetro le pasamos la clave secreta
     * JWT.create() Creamos un objeto JWT y configuramos sus atributos
     * .withIssure("voll med") se establece el Emisor del Token
     * .withSubject(usuario.getLogin()) establecemos el Subject del Token. En este caso el Login del Usuario
     * .withClaim("id", usuario.getId()) se agrega un Claim personalizado al token. Aquí es el Id
     * .withExpiresAt(generarFechaExpiracion()) se establece la fecha de Expiración llamando al método correspondiente
     * .sign(algorithm) se usa para firmar el JWT utilizando el algoritmo y la clave especificados.
     * */
    public String generarToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSrecret);
            return JWT.create()
                    .withIssuer("voll med")
                    .withSubject(usuario.getLogin())
                    .withClaim("id", usuario.getId())
                    .withExpiresAt(generarFechaExpiracion())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException();
        }
    }

    /**
     * MÉTODO PARA OBTENER EL NOMBRE DE USUARIO Y SABER SI INICIÓ SESIÓN O NO
     * USANDO COMO PARÁMETRO UN TOKEN
     * DecodedJWT verifier = null; se crea una instancia de la clase DecodedJWT para almacenar
     * el resultado de la verificación del Token.
     * Algorithm.HMAC256(apiSrecret) instanciamos un Algoritmo HMAC256 y como parámetro le pasamos la clave secreta
     * JWT.require(algorithm) creamos un objeto que pueda verificar la firma y validar el token
     *  Se configuran las propiedades de verificación del token:
     * .withUssure("voll med") se especifica el emisor esperado en el Token
     * .build() se usa para finalizar la configuración y obtener el Verificador.
     * .verify(token) verificamos el Token
     * verifier.getSubject() obtener el Subject del Token verificado
     * si el Subject es null lanzamos excepción
     * De lo contrario retornamos el Subject
     * */
    public String getSubject(String token) {
        if (token == null) {
            throw new RuntimeException();
        }
        DecodedJWT verifier = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSrecret);
            verifier = JWT.require(algorithm)
                    .withIssuer("voll med")
                    .build()
                    .verify(token);
           // verifier.getSubject();
        } catch (JWTVerificationException exception) {
            System.out.println(exception.getMessage());
        }
        if (verifier.getSubject() == null) {
            throw new RuntimeException("Verifier inválido");
        }
        return verifier.getSubject();
    }

    /**
     * GENEREMOS UN OBJETO INSTANT QUE DEVUELVA UN TIEMPO
     * LocalDateTime.now() obtenemos la fecha y hora actual de la zona horaria por defecto
     * .plusHours(2) agregamos 2 horas a la fecha y hora actual
     * .toInstant(ZoneOffset.of("-03:00")) Convertimos de LocalDateTime a objeto Instant
     * ZoneOffset.of("-03:00") Se utiliza para especificar el desplazamiento horaio de la zona deseada
     * */
    private Instant generarFechaExpiracion() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

}
