package prog2int.Main;

/**
 * Clase que delega   AppMenu.
 */
public class Main {
    /**
     * 1. Crea instancia de AppMenu (inicializa toda la aplicación)
     * 2. Llama a app.run() que ejecuta el loop del menú
     * 3. Cuando el usuario sale (opción 0), run() termina y la aplicación finaliza
     */
    public static void main(String[] args) {
       //TestConexion.main(args);
        AppMenu app = new AppMenu();
        app.run();
    }
}