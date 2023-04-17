package med.voll.api.domain.medico;

import med.voll.api.domain.direccion.DatosDireccion;

// RECORD CREADO COMO MOLDE PARA LA RESPUESTA ENVIADA AL CLIENTE DE UNA ACTUALIZACIÓN
public record DatosRespuestaMedico(
        Long id,
        String nombre,
        String email,
        String telefono,
        String documento,
        DatosDireccion direccion
) {
}
