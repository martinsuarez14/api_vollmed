package med.voll.api.controllers;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.direccion.DatosDireccion;
import med.voll.api.domain.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

//NO SE PUEDEN ACTUALIZAR EMAIL Y DOCUMENTO
// LOS MEDICOS NO PUEDEN SER ELIMINADOS SINO DADOS DE BAJA
// EL LISTADO NO DEBE MOSTRAR A LOS MÉDICOS DESACTIVADOS

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository medicoRepository;


    // ESPECIFICAR EN EL ResponseEntity el tipo de objeto a retornar <T>
    @PostMapping("/registrar")
    public ResponseEntity<DatosRespuestaMedico> registrarMedico(@RequestBody @Valid DatosRegistroMedico datosRegistroMedico, UriComponentsBuilder uriComponentBuilder) {
        Medico medico = medicoRepository.save(new Medico(datosRegistroMedico));
        DatosRespuestaMedico datosRespuestaMedico = new DatosRespuestaMedico(
                medico.getId(), medico.getNombre(), medico.getEmail(), medico.getTelefono(),
                medico.getEspecialidad().toString(),
                new DatosDireccion(medico.getDireccion().getCalle(), medico.getDireccion().getDistrito(), medico.getDireccion().getCiudad(),
                        medico.getDireccion().getNumero(), medico.getDireccion().getComplemento()));

        // CON LA CLASE URI RETORNAMOS LA URL
        // EL RETORNO CREATED NOS PIDE LA URL
        URI url = uriComponentBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();

        return ResponseEntity.created(url).body(datosRespuestaMedico);
        // Debo retornar en el header lo siguiente
        // Return 201 Created
        // URL donde encontrar al médico
        // GET http://localhost:8080/medicos
    }

    // CUANDO TRABAJAMOS CON PAGINACIÓN DEBE LLEGAR DEL FRONTEND UN PARÁMETRO Pageable
    // CON LA ANOTACION @PageableDefault modificamos los valores por defecto de Spring
    @GetMapping
    public ResponseEntity<Page<DatosListadoMedico>> listadoMedicos(@PageableDefault(size = 2) Pageable paginacion) {
        // Los valores que envíe el cliente sobreescriben @PageableDefault
        // return medicoRepository.findAll(paginacion).map(DatosListadoMedico::new);
        return ResponseEntity.ok(medicoRepository.findByActivoTrue(paginacion).map(DatosListadoMedico::new));

    }

    // PARA SOBREESCRIBIR EL Pageable
    // en la petición de postman utilizar queryParams

    // EJ: http://localhost:8080/medicos?size=2
    // Le decimos que nos traiga de a dos elementos

    // http://localhost:8080/medicos?size=2&page=2
    // con page=2 le decimos que nos traiga dos elementos de la pagina 2


    // ORDENACIÓN EN LA PAGINACIÓN

    // http://localhost:8080/medicos?size=2&page=0&sort=nombre
    // con el parámetro sort LE DECIMOS QUE LO ORDENE POR EL ATRIBUTO NOMBRE
    // LE TENEMOS QUE PASAR EL ATRIBUTO DE ORDEN DE MANERA CORRECTA


    // Para editar debo recibir el id del Medico
    @PutMapping
    @Transactional
    public ResponseEntity actualizarMedico(@RequestBody @Valid DatosActualizarMedico datosActualizarMedico) {
        // getReferenceById trae un objeto por iD
        Medico medico = medicoRepository.getReferenceById(datosActualizarMedico.id());
        medico.actualizarDatos(datosActualizarMedico);
        return ResponseEntity.ok(new DatosRespuestaMedico(
                medico.getId(), medico.getNombre(), medico.getEmail(), medico.getTelefono(),
                medico.getEspecialidad().toString(),
                new DatosDireccion(medico.getDireccion().getCalle(), medico.getDireccion().getDistrito(), medico.getDireccion().getCiudad(),
                        medico.getDireccion().getNumero(), medico.getDireccion().getComplemento())
        ));
    }


    // NO ELIMINAMOS EL MEDICO HACEMOS UN DELETE LÓGICO DANDOLE UN ESTADO ACTIVO
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminarMedico(@PathVariable Long id) {
        Medico medico = medicoRepository.getReferenceById(id);
        medico.desactivarMedico();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaMedico> retornaDatosMedico(@PathVariable Long id) {
        Medico medico = medicoRepository.getReferenceById(id);
        var datosMedico = new DatosRespuestaMedico(
                medico.getId(), medico.getNombre(), medico.getEmail(), medico.getTelefono(),
                medico.getEspecialidad().toString(),
                new DatosDireccion(medico.getDireccion().getCalle(), medico.getDireccion().getDistrito(), medico.getDireccion().getCiudad(),
                        medico.getDireccion().getNumero(), medico.getDireccion().getComplemento()));
        return ResponseEntity.ok(datosMedico);
    }




    //    BORRADO DE MÉDICO
    //    public void eliminarMedico(@PathVariable Long id) {
    //    Medico medico = medicoRepository.getReferenceById(id);
    //    medicoRepository.delete(medico);
    //}

}
