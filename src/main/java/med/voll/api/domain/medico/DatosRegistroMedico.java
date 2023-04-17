package med.voll.api.domain.medico;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import med.voll.api.domain.direccion.DatosDireccion;

// LOS RECORDS CREAN UNA CLASE NORMAL PERO YA
// CUANDO COMPILAMOS EL CÓDIGO
public record DatosRegistroMedico(
        @NotBlank(message = "Nombre es obligatorio")
        String nombre,
        @NotBlank(message = "Email es obligatorio")
        @Email(message = "Formato de Email inválido")
        String email,
        @NotBlank(message = "Teléfono es obligatorio")
        String telefono,
        @NotBlank(message = "El DNI es obligatorio")
        String documento,
        @NotNull(message = "Indique la especialidad")
        //@Pattern(regexp = "\\d{8,12}")
        Especialidad especialidad,
        @NotNull(message = "Datos de dirección son obligatorios")
        @Valid
        DatosDireccion direccion
) {

}
