package main;

import java.util.Scanner;

import dao.MascotaDaoJdbc;
import dao.MicrochipDaoJdbc;

import service.MascotaService;
import service.MicrochipService;

/**
 * Orquestador principal del menu de la aplicacion.
 * Gestiona el ciclo de vida del menu y coordina las dependencias
 * @author Astrid
 */
public class AppMenu {
    // Scanner unico compartido por toda la aplicacion
    private final Scanner scanner;
    
    /** Handler que ejecuta las operaciones del menu
     * contiene la logica de interaccion con el usuario
     */
    
    private final MenuHandler menuHandler;
    
    /** Flag que controla el loop principal del menu
     * se setea a false cuando el usuario selecciona "0 - salir"
     */
    private boolean running;
    
    // Constructor que inicializa la app
    public AppMenu() {
        this.scanner = new Scanner(System.in);
        
        // Factory methods para crear los services
        MicrochipDaoJdbc microchipDao = new MicrochipDaoJdbc();
        MascotaService mascotaService = createMascotaService(microchipDao);
        MicrochipService microchipService = createMicrochipService(microchipDao);
        
        // Handler
        this.menuHandler = new MenuHandler(scanner, mascotaService, microchipService);
        
        this.running = true;
    }
    
    /** Punto de entrada de la aplicacion Java
     * Crea instancia de AppMenu y ejecuta el menu principal
     * @param args Argumentos de línea de comandos (no usados)
     */
    public static void main(String[] args) {
        AppMenu app = new AppMenu();
        app.run();
    }
    /** Loop principal del menu.
     * Flujo:
     * 1. Mientras running==true:
     *    a. Muestra menú con MenuDisplay.mostrarMenuPrincipal()
     *    b. Lee opción del usuario (scanner.nextLine())
     *    c. Convierte a int (puede lanzar NumberFormatException)
     *    d. Procesa opción con processOption()
     * 2. Si el usuario ingresa texto no numérico: Muestra mensaje de error y continúa
     * 3. Cuando running==false (opción 0): Sale del loop y cierra Scanner
     *
     * Manejo de errores:
     * - NumberFormatException: Captura entrada no numérica (ej: "abc")
     * - Muestra mensaje amigable y NO termina la aplicación
     * - El usuario puede volver a intentar
     *
     * IMPORTANTE: El Scanner se cierra al salir del loop.
     * Cerrar Scanner(System.in) cierra System.in para toda la aplicación.
     * 
     */
    public void run() {
        while (running) {
            try {
                MenuDisplay.mostrarMenuPrincipal();
                int opcion = Integer.parseInt(scanner.nextLine());
                processOption(opcion);
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Por favor, ingrese un numero.");
            }
        }
        scanner.close();
    }
    
    /**
     * Procesa la opción seleccionada por el usuario y delega a MenuHandler.
     *
     * Mapeo de opciones (corresponde a MenuDisplay):
     * 1  → Crear Mascota
     * 2  → Listar Mascotas
     * 3  → Buscar Mascota por ID
     * 4  → Actualizar Mascota
     * 5  → Eliminar Mascota
     * 6  → Agregar Microchip a Mascota
     * 7  → Actualizar Microchip
     * 8  → Buscar Microchip por ID
     * 9  → Eliminar Microchip
     * 0  → Salir
     */
    
    private void processOption(int opcion) {
        switch (opcion) {
            case 1 -> menuHandler.crearMascota();
            case 2 -> menuHandler.listarMascotas();
            case 3 -> menuHandler.buscarMascotaPorId();
            case 4 -> menuHandler.actualizarMascota();
            case 5 -> menuHandler.eliminarMascota();
            case 6 -> menuHandler.agregarMicrochipAMascota();
            case 7 -> menuHandler.actualizarMicrochip();
            case 8 -> menuHandler.buscarMicrochipPorId();
            case 9 -> menuHandler.eliminarMicrochip();
            case 0 -> {
                System.out.println("Saliendo...");
                running = false;
            }
            default -> System.out.println("Opción no válida.");
        }
    }
    
    /**
     * Factory method que crea la cadena de dependencias para MascotaService.
     * Implementa inyección de dependencias manual.
     *
     * Orden de creación (bottom-up desde la capa más baja):
     * 1. MascotaDaoJdbc: Sin dependencias, acceso directo a BD
     * 2. MicrochipDaoJdbc: Sin dependencias, acceso directo a BD
     * 3. MascotaService: Depende de MascotaDaoJdbc y MicrochipDaoJdbc
     *
     * Arquitectura resultante (4 capas):
     * Main (AppMenu, MenuHandler)
     *   ↓
     * Service (MascotaService, MicrochipService)
     *   ↓
     * DAO (MascotaDaoJdbc, MicrochipDaoJdbc)
     *   ↓
     * Entities (Mascota, Microchip)
     *
     * @param microchipDao Instancia compartida de MicrochipDaoJdbc
     * @return MascotaService completamente inicializado con sus dependencias
     */
    private MascotaService createMascotaService(MicrochipDaoJdbc microchipDao) {
        MascotaDaoJdbc mascotaDao = new MascotaDaoJdbc();
        return new MascotaService(mascotaDao, microchipDao);
    }

    /**
     * Factory method para crear MicrochipService.
     * MicrochipService depende únicamente de MicrochipDaoJdbc.
     *
     * @param microchipDao Instancia compartida de MicrochipDaoJdbc
     * @return MicrochipService inicializado
     */
    private MicrochipService createMicrochipService(MicrochipDaoJdbc microchipDao) {
        return new MicrochipService(microchipDao);
    }
}
