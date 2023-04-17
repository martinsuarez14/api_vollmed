package med.voll.api.medico;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.direccion.DatosDireccion;

// LOS RECORDS CREAN UNA CLASE NORMAL PERO YA
// CUANDO COMPILAMOS EL CÓDIGO
public record DatosRegistroMedico(
        @NotBlank
        String nombre,
        @NotBlank
        @Email
        String email,
        @NotBlank
        String telefono,
        @NotBlank
        String documento,
        @NotNull
        //@Pattern(regexp = "\\d{8,12}")
        Especialidad especialidad,
        @NotNull
        @Valid
        DatosDireccion direccion
) {

}
