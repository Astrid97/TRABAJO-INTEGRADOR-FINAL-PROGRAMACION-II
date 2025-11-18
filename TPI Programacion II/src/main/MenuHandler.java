package main;

import entities.Mascota;
import entities.Microchip;

import service.MascotaService;
import service.MicrochipService;

import java.util.List;
import java.util.Scanner;
import java.util.Optional;
import java.time.LocalDate;

import config.DatabaseConnection;
import config.TransactionManager;
import java.sql.Connection;
/**
 * Controlador de las operaciones del menú (Menu Handler).
 * Gestiona toda la lógica de interacción con el usuario para operaciones CRUD
 * de Mascota y Microchip.
 *
 * Responsabilidades:
 * - Capturar entrada del usuario desde consola (Scanner)
 * - Validar entrada básica (conversión de tipos, valores vacíos)
 * - Invocar servicios de negocio (MascotaService, MicrochipService)
 * - Mostrar resultados y mensajes de error al usuario
 * - Coordinar operaciones Mascota ↔ Microchip (relación 1→1)
 *
 * Patrón: Controller (MVC) - capa de presentación en arquitectura de 4 capas
 * Arquitectura: Main → Service → DAO → Entities
 *
 * IMPORTANTE: Este handler NO contiene lógica de negocio.
 * Todas las validaciones de negocio están en la capa Service.
 * @author Astrid
 */
public class MenuHandler {
    /**
     * Scanner compartido para leer entrada del usuario.
     * Inyectado desde AppMenu para evitar múltiples Scanners de System.in.
     */
    private final Scanner scanner;

    /**
     * Servicios de negocio para Mascota y Microchip.
     */
    private final MascotaService mascotaService;
    private final MicrochipService microchipService;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param scanner          Scanner compartido
     * @param mascotaService   servicio de mascotas
     * @param microchipService servicio de microchips
     */
    public MenuHandler(Scanner scanner, MascotaService mascotaService, MicrochipService microchipService) {
        if (scanner == null) {
            throw new IllegalArgumentException("Scanner no puede ser null");
        }
        if (mascotaService == null) {
            throw new IllegalArgumentException("MascotaService no puede ser null");
        }
        if (microchipService == null) {
            throw new IllegalArgumentException("MicrochipService no puede ser null");
        }
        this.scanner = scanner;
        this.mascotaService = mascotaService;
        this.microchipService = microchipService;
    }
    
    // Opción 1: Crear Mascota (con microchip opcional)- CON TRANSACCIÓN
   
    public void crearMascota() {
        try (Connection conn = DatabaseConnection.getConnection();
             TransactionManager tx = new TransactionManager(conn)) {

            tx.startTransaction();

            System.out.println("\n--- Crear Mascota ---");

            Mascota mascota = leerDatosMascotaBasicos();

            System.out.print("¿Desea agregar un microchip ahora? (s/n): ");
            String resp = scanner.nextLine().trim();
            if (resp.equalsIgnoreCase("s")) {
                Microchip mc = leerDatosMicrochip();
                mascota.setMicrochip(mc);
            }

            // Usamos la versión transaccional del service
            Mascota creada = mascotaService.insertar(mascota, conn);

            tx.commit();
            System.out.println("Mascota creada exitosamente con ID: " + creada.getId());
            System.out.println("(Transacción confirmada: COMMIT)");

        } catch (Exception e) {
            // TransactionManager hará rollback en close() si la transacción sigue activa
            System.err.println("Error al crear mascota, se revertirá la transacción (ROLLBACK): " + e.getMessage());
        }
    }

    // Opción 2: Listar Mascotas

    public void listarMascotas() {
        try {
            List<Mascota> mascotas = mascotaService.getAll();

            if (mascotas.isEmpty()) {
                System.out.println("No se encontraron mascotas.");
                return;
            }

            System.out.println("\n=== LISTADO DE MASCOTAS ===");
            for (Mascota m : mascotas) {
                /*System.out.print("ID: " + m.getId() +
                        ", Nombre: " + m.getNombre() +
                        ", Especie: " + m.getEspecie() +
                        ", Raza: " + m.getRaza() +
                        ", Dueño: " + m.getDuenio()+
                        ", Eliminado: " + m.isEliminado() 
                ); */
                System.out.println(m.toString());
                if (m.getMicrochip() != null) {
                    /*System.out.println(" Microchip ID: " + m.getMicrochip().getId() +
                            ", Código: " + m.getMicrochip().getCodigo() +
                            ", Fecha de Implatación: " + m.getMicrochip().getFechaImplantacion() +
                            ", Veterinaria: " + m.getMicrochip().getVeterinaria() +
                            ", Observaciones: " + m.getMicrochip().getObservaciones() +
                            ", Eliminado: " + m.getMicrochip().isEliminado() 
                    );*/
                    System.out.println(m.toString());
                } else {
                    System.out.println("Microchip: no asignado");
                }
                System.out.println();
            }
        } catch (Exception e) {
            System.err.println("Error al listar mascotas: " + e.getMessage());
        }
    }

    
    // Opción 3: Buscar Mascota por ID
 
    public void buscarMascotaPorId() {
        try {
            System.out.print("ID de la mascota a buscar: ");
            long id = Long.parseLong(scanner.nextLine());

            Optional<Mascota> op = mascotaService.getById(id);

            if (op.isEmpty()) {
                System.out.println("Mascota no encontrada.");
                return;
            }

            Mascota m = op.get();
            System.out.println("ID: " + m.getId());
            System.out.println("Nombre: " + m.getNombre());
            System.out.println("Especie: " + m.getEspecie());
            System.out.println("Raza: " + m.getRaza());
            System.out.println("Fecha nacimiento: " + m.getFechaNacimiento());
            System.out.println("Dueño: " + m.getDuenio());
            if (m.getMicrochip() != null) {
                System.out.println("Microchip ID: " + m.getMicrochip().getId() +
                        ", Código: " + m.getMicrochip().getCodigo());
            } else {
                System.out.println("Microchip: no asignado");
            }

        } catch (Exception e) {
            System.err.println("Error al buscar mascota: " + e.getMessage());
        }
    }

    
    // Opción 4: Actualizar Mascota
    
    public void actualizarMascota() {
        try {
            System.out.print("ID de la mascota a actualizar: ");
            long id = Long.parseLong(scanner.nextLine());

            Optional<Mascota> op = mascotaService.getById(id);
            if (op.isEmpty()) {
                System.out.println("Mascota no encontrada.");
                return;
            }

            Mascota m = op.get();
            System.out.println("Actualizando mascota (Enter para mantener el valor actual).");

            System.out.print("Nuevo nombre (" + m.getNombre() + "): ");
            String nombre = scanner.nextLine().trim();
            if (!nombre.isEmpty()) {
                m.setNombre(nombre);
            }

            System.out.print("Nueva especie (" + m.getEspecie() + "): ");
            String especie = scanner.nextLine().trim();
            if (!especie.isEmpty()) {
                m.setEspecie(especie);
            }

            System.out.print("Nueva raza (" + m.getRaza() + "): ");
            String raza = scanner.nextLine().trim();
            if (!raza.isEmpty()) {
                m.setRaza(raza);
            }

            System.out.print("Nueva fecha de nacimiento (" + m.getFechaNacimiento() + ") [YYYY-MM-DD]: ");
            String fechaStr = scanner.nextLine().trim();
            if (!fechaStr.isEmpty()) {
                m.setFechaNacimiento(LocalDate.parse(fechaStr));
            }

            System.out.print("Nuevo dueño (" + m.getDuenio() + "): ");
            String duenio = scanner.nextLine().trim();
            if (!duenio.isEmpty()) {
                m.setDuenio(duenio);
            }

            // Actualizar o agregar microchip
            manejarActualizacionDeMicrochip(m);

            mascotaService.actualizar(m);
            System.out.println("Mascota actualizada exitosamente.");

        } catch (Exception e) {
            System.err.println("Error al actualizar mascota: " + e.getMessage());
        }
    }

    
    // Opción 5: Eliminar Mascota
  
    public void eliminarMascota() {
        try {
            System.out.print("ID de la mascota a eliminar: ");
            long id = Long.parseLong(scanner.nextLine());

            mascotaService.eliminar(id);
            System.out.println("Mascota eliminada exitosamente.");

        } catch (Exception e) {
            System.err.println("Error al eliminar mascota: " + e.getMessage());
        }
    }

    
 
    // Opción 6: Agregar Microchip a Mascota - CON TRANSACCIÓN
 
    public void agregarMicrochipAMascota() {
        try {
            System.out.print("ID de la mascota: ");
            long mascotaId = Long.parseLong(scanner.nextLine());

            Optional<Mascota> op = mascotaService.getById(mascotaId);
            if (op.isEmpty()) {
                System.out.println("Mascota no encontrada.");
                return;
            }

            Mascota m = op.get();
            if (m.getMicrochip() != null) {
                System.out.println("La mascota ya tiene un microchip asignado (ID: "
                        + m.getMicrochip().getId() + ").");
                return;
            }

            System.out.println("Cargando datos del nuevo microchip...");
            Microchip mc = leerDatosMicrochip();
            m.setMicrochip(mc);

            // 🔹 Envolvemos la actualización en una transacción
            try (Connection conn = DatabaseConnection.getConnection();
                 TransactionManager tx = new TransactionManager(conn)) {

                tx.startTransaction();
                mascotaService.actualizar(m, conn);
                tx.commit();
                System.out.println("Microchip agregado correctamente a la mascota.");
                System.out.println("(Transacción confirmada: COMMIT)");
            }

        } catch (Exception e) {
            System.err.println("Error al agregar microchip a mascota, se puede haber hecho ROLLBACK: " + e.getMessage());
        }
    }

    // Opción 7: Actualizar Microchip
   
    public void actualizarMicrochip() {
        try {
            System.out.print("ID del microchip a actualizar: ");
            long id = Long.parseLong(scanner.nextLine());

            Optional<Microchip> op = microchipService.getById(id);
            if (op.isEmpty()) {
                System.out.println("Microchip no encontrado.");
                return;
            }

            Microchip mc = op.get();
            System.out.println("Actualizando microchip (Enter para mantener valor actual).");

            System.out.print("Nuevo código (" + mc.getCodigo() + "): ");
            String codigo = scanner.nextLine().trim();
            if (!codigo.isEmpty()) {
                mc.setCodigo(codigo);
            }

            System.out.print("Nueva fecha de implantación (" + mc.getFechaImplantacion() + ") [YYYY-MM-DD]: ");
            String fechaStr = scanner.nextLine().trim();
            if (!fechaStr.isEmpty()) {
                mc.setFechaImplantacion(LocalDate.parse(fechaStr));
            }

            System.out.print("Nueva veterinaria (" + mc.getVeterinaria() + "): ");
            String vet = scanner.nextLine().trim();
            if (!vet.isEmpty()) {
                mc.setVeterinaria(vet);
            }

            System.out.print("Nuevas observaciones (" + mc.getObservaciones() + "): ");
            String obs = scanner.nextLine().trim();
            if (!obs.isEmpty()) {
                mc.setObservaciones(obs);
            }

            microchipService.actualizar(mc);
            System.out.println("Microchip actualizado correctamente.");

        } catch (Exception e) {
            System.err.println("Error al actualizar microchip: " + e.getMessage());
        }
    }

    
    // Opción 8: Buscar Microchip por ID
   
    public void buscarMicrochipPorId() {
        try {
            System.out.print("ID del microchip: ");
            long id = Long.parseLong(scanner.nextLine());

            Optional<Microchip> op = microchipService.getById(id);
            if (op.isEmpty()) {
                System.out.println("Microchip no encontrado.");
                return;
            }

            Microchip mc = op.get();
            System.out.println("ID: " + mc.getId());
            System.out.println("Código: " + mc.getCodigo());
            System.out.println("Fecha de implantación: " + mc.getFechaImplantacion());
            System.out.println("Veterinaria: " + mc.getVeterinaria());
            System.out.println("Observaciones: " + mc.getObservaciones());

        } catch (Exception e) {
            System.err.println("Error al buscar microchip: " + e.getMessage());
        }
    }

   
    // Opción 9: Eliminar Microchip
  
    public void eliminarMicrochip() {
        try {
            System.out.print("ID del microchip a eliminar: ");
            long id = Long.parseLong(scanner.nextLine());

            microchipService.eliminar(id);
            System.out.println("Microchip eliminado exitosamente.");

        } catch (Exception e) {
            System.err.println("Error al eliminar microchip: " + e.getMessage());
        }
    }

    
    // Métodos auxiliares (creación / actualización de datos)
 

    /**
     * Captura los datos básicos de una mascota desde consola.
     * NO incluye el ID (lo maneja BD) ni el microchip (se agrega aparte).
     */
    private Mascota leerDatosMascotaBasicos() {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();

        System.out.print("Especie: ");
        String especie = scanner.nextLine().trim();

        System.out.print("Raza: ");
        String raza = scanner.nextLine().trim();

        System.out.print("Fecha de nacimiento (YYYY-MM-DD): ");
        LocalDate fechaNac = LocalDate.parse(scanner.nextLine().trim());

        System.out.print("Nombre del dueño: ");
        String duenio = scanner.nextLine().trim();

        Mascota m = new Mascota();
        m.setNombre(nombre);
        m.setEspecie(especie);
        m.setRaza(raza);
        m.setFechaNacimiento(fechaNac);
        m.setDuenio(duenio);

        return m;
    }

    /**
     * Captura los datos de un microchip desde consola.
     */
    private Microchip leerDatosMicrochip() {
        System.out.print("Código: ");
        String codigo = scanner.nextLine().trim();

        System.out.print("Fecha de implantación (YYYY-MM-DD): ");
        LocalDate fechaImpl = LocalDate.parse(scanner.nextLine().trim());

        System.out.print("Veterinaria: ");
        String vet = scanner.nextLine().trim();

        System.out.print("Observaciones: ");
        String obs = scanner.nextLine().trim();

        Microchip mc = new Microchip();
        mc.setCodigo(codigo);
        mc.setFechaImplantacion(fechaImpl);
        mc.setVeterinaria(vet);
        mc.setObservaciones(obs);

        return mc;
    }

    /**
     * Maneja la lógica de actualización/creación de microchip dentro de actualizarMascota().
     * Permite:
     * - Mantener el microchip actual
     * - Actualizarlo (pidiendo datos nuevos)
     * - Crear uno nuevo si no tenía
     */
    private void manejarActualizacionDeMicrochip(Mascota m) {
        if (m.getMicrochip() != null) {
            System.out.print("La mascota tiene microchip. ¿Desea actualizarlo? (s/n): ");
            String resp = scanner.nextLine().trim();
            if (resp.equalsIgnoreCase("s")) {
                Microchip mc = m.getMicrochip();

                System.out.print("Nuevo código (" + mc.getCodigo() + "): ");
                String codigo = scanner.nextLine().trim();
                if (!codigo.isEmpty()) {
                    mc.setCodigo(codigo);
                }

                System.out.print("Nueva fecha implantación (" + mc.getFechaImplantacion() + ") [YYYY-MM-DD]: ");
                String fechaStr = scanner.nextLine().trim();
                if (!fechaStr.isEmpty()) {
                    mc.setFechaImplantacion(LocalDate.parse(fechaStr));
                }

                System.out.print("Nueva veterinaria (" + mc.getVeterinaria() + "): ");
                String vet = scanner.nextLine().trim();
                if (!vet.isEmpty()) {
                    mc.setVeterinaria(vet);
                }

                System.out.print("Nuevas observaciones (" + mc.getObservaciones() + "): ");
                String obs = scanner.nextLine().trim();
                if (!obs.isEmpty()) {
                    mc.setObservaciones(obs);
                }
            }
        } else {
            System.out.print("La mascota no tiene microchip. ¿Desea agregar uno? (s/n): ");
            String resp = scanner.nextLine().trim();
            if (resp.equalsIgnoreCase("s")) {
                Microchip nuevo = leerDatosMicrochip();
                m.setMicrochip(nuevo);
            }
        }
    }

}
