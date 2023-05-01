package med.voll.api.infra.security;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


// CLASE DE CONFIGURACIÓN DE SWAGGER
/**
 * CLASE DE CONFIGURACIÓN PARA SWAGGER
 * @OpenAPIDefinition definimos la información de la API
 * @SecuritySchemes indicamos que vamos a definir uno o más esquemas de seguridad
 * @SecurityScheme define un esquema de seguridad específico
 * name = "BearerAuth" asignamos un nombre al esquema
 * type = SecuritySchemeType.HTTP indicamos que el esquema es de tipo HTTP
 * scheme = "bearer" indicamo el esquema de autenticación utilizado. "bearer" para Tokens
 * bearerFormat = "JWT" indicamos que el formato de acceso es JWT
 * @SecurityRequirement(name = "BearerAuth") indicamos que el esquema de seguridad llamado "BearerAuth"
 * es requisito de seguridad para los endpoints protegidos en Swagger
 * */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Voll Med API",
                version = "v1.0.0",
                description = "Aplicación para Clinica Médica")
)
@SecuritySchemes(
        @SecurityScheme(
                name = "BearerAuth",
                type = SecuritySchemeType.HTTP,
                scheme = "bearer",
                bearerFormat = "JWT"
        )
)
@SecurityRequirement(name = "BearerAuth")
public class SwaggerConfig {

        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI();
        }

}
