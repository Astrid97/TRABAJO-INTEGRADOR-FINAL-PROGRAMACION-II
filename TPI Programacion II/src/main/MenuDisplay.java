package main;

/**
 * Clase utilitaria para mostrar el menú de la aplicación.
 * Imprime el menu principal con todas las opciones disponibles,
 * no hace logica ni llama a DAOs ni Services
 * 
 * La lectura de opciones la maneja AppMenu y la ejecucion
 * la maneja MenuHandler
 * 
 * @author Astrid
 */
public class MenuDisplay {
    /** Menu principal con todas las opciones CRUD para mascota
     * Pedir datos para crear mascota
     * pedir datos para actualizar mascota
     * pedir ID
     * mostrar listas de mascotas
     */
    
    /** Microchip:
     * pedir datos del microchip
     * mostrar microchips si corresponde
     */
    public static void mostrarMenuPrincipal() {
        System.out.println("\n====== MENU VETERINARIA 🐶🐾🐱 ======");
        System.out.println("1. Crear Mascota");
        System.out.println("2. Listar Mascotas");
        System.out.println("3. Buscar Mascota por ID");
        System.out.println("4. Actualizar Mascota");
        System.out.println("5. Eliminar Mascota");
        System.out.println("6. Agregar Microchip a Mascota");
        System.out.println("7. Actualizar Microchip");
        System.out.println("8. Buscar Microchip por ID");
        System.out.println("9. Eliminar Microchip");
        System.out.println("0. Salir");
        System.out.print("Ingrese una opción: ");
    }
}