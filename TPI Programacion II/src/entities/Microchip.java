package entities;

import java.time.LocalDate;

public class Microchip extends Base {

    private String codigo;
    private LocalDate fechaImplantacion;
    private String veterinaria;
    private String observaciones;

    public Microchip() {
        super();
    }

    public Microchip(Long id,
                     String codigo,
                     LocalDate fechaImplantacion,
                     String veterinaria,
                     String observaciones,
                     boolean eliminado) {

        super(id, eliminado);
        this.codigo = codigo;
        this.fechaImplantacion = fechaImplantacion;
        this.veterinaria = veterinaria;
        this.observaciones = observaciones;
    }

    // Override opcional para conservar tu validación de id
    @Override
    public void setId(Long id) {
        if (id != null) {
            super.setId(id);
        }
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código del microchip no puede estar vacío");
        }
        this.codigo = codigo.trim();
    }

    public LocalDate getFechaImplantacion() {
        return fechaImplantacion;
    }

    public void setFechaImplantacion(LocalDate fechaImplantacion) {
        if (fechaImplantacion != null && fechaImplantacion.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Fecha inválida");
        }
        this.fechaImplantacion = fechaImplantacion;
    }

    public String getVeterinaria() {
        return veterinaria;
    }

    public void setVeterinaria(String veterinaria) {
        if (veterinaria == null || veterinaria.isBlank()) {
            throw new IllegalArgumentException("La veterinaria no puede estar vacía");
        }
        this.veterinaria = veterinaria.trim();
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public String toString() {
        return "Microchip{" +
                "Id = " + getId() +
                ", Eliminado = " + isEliminado() +
                ", Codigo = " + codigo +
                ", Fecha de Implantacion = " + fechaImplantacion +
                ", Veterinaria = " + veterinaria +
                ", Observaciones = " + observaciones +
                '}';
    }
}
