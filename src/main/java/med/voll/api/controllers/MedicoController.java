package med.voll.api.controllers;

import com.electronwill.nightconfig.core.conversion.Path;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

//NO SE PUEDEN ACTUALIZAR EMAIL Y DOCUMENTO
// LOS MEDICOS NO PUEDEN SER ELIMINADOS SINO DADOS DE BAJA
// EL LISTADO NO DEBE MOSTRAR A LOS MÉDICOS DESACTIVADOS

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository medicoRepository;


    @PostMapping("/registrar")
    public void registrarMedico(@RequestBody @Valid DatosRegistroMedico datosRegistroMedico) {
        medicoRepository.save(new Medico(datosRegistroMedico));
    }

    // CUANDO TRABAJAMOS CON PAGINACIÓN DEBE LLEGAR DEL FRONTEND UN PARÁMETRO Pageable
    // CON LA ANOTACION @PageableDefault modificamos los valores por defecto de Spring
    @GetMapping
    public Page<DatosListadoMedico> listadoMedicos(@PageableDefault(size = 2) Pageable paginacion) {
        // Los valores que envíe el cliente sobreescriben @PageableDefault
        // return medicoRepository.findAll(paginacion).map(DatosListadoMedico::new);
        return medicoRepository.findByActivoTrue(paginacion).map(DatosListadoMedico::new);

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
    public void actualizarMedico(@RequestBody @Valid DatosActualizarMedico datosActualizarMedico) {
        // getReferenceById trae un objeto por iD
        Medico medico = medicoRepository.getReferenceById(datosActualizarMedico.id());
        medico.actualizarDatos(datosActualizarMedico);
    }


    // NO ELIMINAMOS EL MEDICO HACEMOS UN DELETE LÓGICO DANDOLE UN ESTADO ACTIVO
    @DeleteMapping("/{id}")
    @Transactional
    public void eliminarMedico(@PathVariable Long id) {
        Medico medico = medicoRepository.getReferenceById(id);
        medico.desactivarMedico();
    }





    //    BORRADO DE MÉDICO
    //    public void eliminarMedico(@PathVariable Long id) {
    //    Medico medico = medicoRepository.getReferenceById(id);
    //    medicoRepository.delete(medico);
    //}

}
