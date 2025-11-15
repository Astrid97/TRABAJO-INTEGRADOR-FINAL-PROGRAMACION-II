package service;

import java.util.List;
import java.util.Optional;
/**
 *
 * @author Astrid
 */
public interface GenericService<T> {
    
    T insertar(T entidad) throws Exception;
    
    void actualizar(T entidad) throws Exception;
    
    void eliminar(long id) throws Exception;
    
    Optional<T> getById(long id) throws Exception;
    
    List<T> getAll() throws Exception;
}