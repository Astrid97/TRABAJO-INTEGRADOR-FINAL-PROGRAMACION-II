package main;

/**
 * Punto de entrada de la aplicación.
 * Clase simple que delega inmediatamente a AppMenu.
 *
 * Responsabilidad:
 * - Proporcionar un punto de entrada main() estándar
 * - Delegar la ejecución al menú principal de la veterinaria
 *
 * Flujo:
 * 1. Crea instancia de AppMenu (inicializa DAOs, Services y MenuHandler)
 * 2. Llama a app.run() que ejecuta el loop del menú
 * 3. Cuando el usuario elige "0 - Salir", run() termina y la aplicación finaliza
 *
 * Dominio: Mascota → Microchip (relación 1→1 unidireccional)
 * @author Astrid
 */
public class Main {
    /**
     * Punto de entrada principal de la aplicación Java.
     *
     * @param args Argumentos de línea de comandos (no usados)
     */
    public static void main(String[] args) {
        AppMenu app = new AppMenu();
        app.run();
    }
}
