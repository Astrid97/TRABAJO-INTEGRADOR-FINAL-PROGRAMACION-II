package entities;

/**
 * Clase base abstracta para todas las entidades del sistema.
 * Implementa el patrón de soft delete mediante el campo 'eliminado'.
 *
 * Propósito:
 * - Proporcionar campos comunes a todas las entidades (id, eliminado)
 * - Implementar el patrón de herencia para evitar duplicación de código
 * - Soportar eliminación lógica en lugar de eliminación física
 *
 * Patrón de diseño: Template (clase base abstracta)
 */
public abstract class Base {

    /**
     * Identificador único de la entidad.
     * Generado automáticamente por la base de datos (AUTO_INCREMENT).
     */
    private Long id;

    /**
     * Flag de eliminación lógica.
     * - true: La entidad está marcada como eliminada (no se mostrará en consultas)
     * - false: La entidad está activa
     *
     * Esto permite mantener integridad referencial y datos históricos.
     */
    private boolean eliminado;

    /**
     * Constructor completo con todos los campos.
     * Usado por los DAOs al reconstruir entidades desde la base de datos.
     *
     * @param id Identificador de la entidad
     * @param eliminado Estado de eliminación
     */
    protected Base(Long id, boolean eliminado) {
        this.id = id;
        this.eliminado = eliminado;
    }

    /**
     * Constructor por defecto.
     * Inicializa una entidad nueva sin ID (será asignado por la BD).
     * Por defecto, las entidades nuevas NO están eliminadas.
     */
    protected Base() {
        this.eliminado = false;
    }

    /** Obtiene el ID de la entidad. */
    public Long getId() {
        return id;
    }

    /** Establece el ID de la entidad (típicamente desde el DAO). */
    public void setId(Long id) {
        this.id = id;
    }

    /** ¿Está marcada como eliminada? (soft delete) */
    public boolean isEliminado() {
        return eliminado;
    }

    /** Marca o desmarca la entidad como eliminada. */
    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }
}