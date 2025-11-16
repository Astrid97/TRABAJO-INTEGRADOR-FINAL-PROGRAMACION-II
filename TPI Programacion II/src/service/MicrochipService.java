package service;

import java.util.List;
import java.util.Optional;

import dao.MicrochipDaoJdbc;
import entities.Microchip;


/**
 * Implementacion del servicio de negocio para la entidad Microchip
 * Capa intermedia entre la UI y el DAO que aplica validaciones de negocio
 * 
 * Responsabilidades: 
 * -
 * -
 * -
 * 
 * @author Astrid
 */
public class MicrochipService implements GenericService<Microchip> {

    private final MicrochipDaoJdbc microchipDao;

    public MicrochipService(MicrochipDaoJdbc microchipDao) {
        if (microchipDao == null) {
            throw new IllegalArgumentException("MicrochipDaoJdbc no puede ser null");
        }
        this.microchipDao = microchipDao;
    }

    @Override
    public Microchip insertar(Microchip mc) throws Exception {
        validarMicrochip(mc);
        // El DAO se encarga de insertar y setear el ID generado
        return microchipDao.crear(mc);
    }

    @Override
    public void actualizar(Microchip mc) throws Exception {
        validarMicrochip(mc);
        if (mc.getId() == null || mc.getId() <= 0) {
            throw new IllegalArgumentException("El ID del microchip debe ser mayor a 0 para actualizar");
        }
        microchipDao.actualizar(mc);
    }

    @Override
    public void eliminar(long id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0");
        }
        microchipDao.eliminar(id);
    }

    @Override
    public Optional<Microchip> getById(long id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0");
        }
        return microchipDao.leer(id);
    }

    @Override
    public List<Microchip> getAll() throws Exception {
        return microchipDao.leerTodos();
    }

 
    // Validaciones de negocio para Microchip
    
    private void validarMicrochip(Microchip mc) {
        if (mc == null) {
            throw new IllegalArgumentException("El microchip no puede ser null");
        }
        if (mc.getCodigo() == null || mc.getCodigo().trim().isEmpty()) {
            throw new IllegalArgumentException("El código del microchip es obligatorio");
        }
        if (mc.getFechaImplantacion() == null) {
            throw new IllegalArgumentException("La fecha de implantación es obligatoria");
        }
        if (mc.getVeterinaria() == null || mc.getVeterinaria().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la veterinaria es obligatorio");
        }
        // observaciones puede ser opcional, así que no lo validamos como obligatorio
    }
}