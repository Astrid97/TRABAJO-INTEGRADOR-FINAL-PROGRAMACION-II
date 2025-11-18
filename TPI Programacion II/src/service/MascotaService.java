package service;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import entities.Mascota;
import entities.Microchip;
import dao.MascotaDaoJdbc;
import dao.MicrochipDaoJdbc;

/**
 * Implementacion del servicio de negocio para la entidad Mascota.
 * Capa intermedia entre la UI y el DAO que aplica validaciones de negocio complejas.
 * 
 * Responsabilidades: 
 * - Validar datos de Mascota antes de persistir
 * - Coordinar la relacion 1->1 unidireccional Mascota -> Microchip
 * - Delegar el acceso a datos al MascotaDao
 * - Exponer operaciones CRUD a la capa de menu / UI. 
 * Patron: Service Layer con inyeccion de dependencias y coordinacion de servicios
 * @author Astrid
 */
public class MascotaService implements GenericService<Mascota> {
    
    private final MascotaDaoJdbc mascotaDao;
    private final MicrochipDaoJdbc microchipDao;
    
    public MascotaService(MascotaDaoJdbc mascotaDao, MicrochipDaoJdbc microchipDao){
        if (mascotaDao == null || microchipDao == null) {
            throw new IllegalArgumentException("Los DAO no pueden ser null");
        }
       
        this.mascotaDao = mascotaDao;
        this.microchipDao = microchipDao;
    }
    
    // CRUD
    @Override
    public Mascota insertar(Mascota m) throws Exception {
        validarMascota(m);

        // Si la mascota tiene microchip asociado, primero lo actualizamos
        Microchip chip = m.getMicrochip();
        if (chip != null) {
            if (chip.getId() == null || chip.getId() == 0L) {
                // chip nuevo
                chip = microchipDao.crear(chip);
                m.setMicrochip(chip);
            } else {
                // chip existente
                microchipDao.actualizar(chip);
            }
        }
        // Insertar mascota (el DAO se encarga de setear el id generado)
        return mascotaDao.crear(m);
    }

    @Override
    public void actualizar(Mascota m) throws Exception {
        validarMascota(m);
        if (m.getId() == null || m.getId() <= 0) {
            throw new IllegalArgumentException("El ID de la mascota debe ser mayor a 0 para actualizar");
        }

        // Actualizar microchip si existe
        Microchip chip = m.getMicrochip();
        if (chip != null) {
            if (chip.getId() == null || chip.getId() == 0L) {
                chip = microchipDao.crear(chip);
                m.setMicrochip(chip);
            } else {
                microchipDao.actualizar(chip);
            }
        }

        mascotaDao.actualizar(m);
    }

    @Override
    public void eliminar(long id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0");
        }
        // la FK está en microchip con ON DELETE CASCADE,
        // así que al eliminar la mascota se elimina el chip asociado.
        mascotaDao.eliminar(id);
    }

    @Override
    public Optional<Mascota> getById(long id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0");
        }
        return mascotaDao.leer(id);
    }

    @Override
    public List<Mascota> getAll() throws Exception {
        return mascotaDao.leerTodos();
    }

    // Validaciones de negocio básicas
   
    private void validarMascota(Mascota m) {
        if (m == null) {
            throw new IllegalArgumentException("La mascota no puede ser null");
        }
        if (m.getNombre() == null || m.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la mascota es obligatorio");
        }
        if (m.getEspecie() == null || m.getEspecie().trim().isEmpty()) {
            throw new IllegalArgumentException("La especie de la mascota es obligatoria");
        }
        if (m.getDuenio() == null || m.getDuenio().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del dueño es obligatorio");
        }
    }
    
    /**
     * Metodos transaccionales, usan una Conenection externa.
     * Se usan cuando MenuHandler maneja TransactionManager.
     */

    public Mascota insertar(Mascota m, Connection conn) throws Exception {
        validarMascota(m);

        Microchip chip = m.getMicrochip();
        if (chip != null) {
            if (chip.getId() == null || chip.getId() == 0L) {
                // Usamos la versión del DAO que recibe Connection
                chip = microchipDao.crear(chip, conn);
                m.setMicrochip(chip);
            } else {
            microchipDao.actualizar(chip, conn);
            }
        }

        return mascotaDao.crear(m, conn);
    }

    public void actualizar(Mascota m, Connection conn) throws Exception {
        validarMascota(m);
        if (m.getId() == null || m.getId() <= 0) {
            throw new IllegalArgumentException("El ID de la mascota debe ser mayor a 0 para actualizar");
        }

        Microchip chip = m.getMicrochip();
        if (chip != null) {
            if (chip.getId() == null || chip.getId() == 0L) {
                chip = microchipDao.crear(chip, conn);
                m.setMicrochip(chip);
            } else {
                microchipDao.actualizar(chip, conn);
            }
        }

        mascotaDao.actualizar(m, conn);
    }
}
    
  