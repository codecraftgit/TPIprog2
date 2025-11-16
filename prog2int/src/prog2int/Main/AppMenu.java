package prog2int.Main;

import java.util.Scanner;

import prog2int.Dao.EnvioDAO;
import prog2int.Dao.PedidoDAO;
import prog2int.Service.EnvioServiceImpl;
import prog2int.Service.PedidoServiceImpl;

/**
 * Orquestador principal del menú de la aplicación (Pedidos/Envios).
 * Ensambla dependencias y ejecuta el loop del menú.
 */
public class AppMenu {

    private final Scanner scanner;
    private final MenuHandler menuHandler;
    private boolean running;

    public AppMenu() {
        this.scanner = new Scanner(System.in);
        // Crear cadena de dependencias
        EnvioDAO envioDAO = new EnvioDAO(); // Instancia de EnvioDAO
        PedidoDAO pedidoDAO = new PedidoDAO(); // Instancia de PedidoDAO
        EnvioServiceImpl envioService = new EnvioServiceImpl(envioDAO); // Instancia de EnvioServiceImpl con EnvioDAO
        PedidoServiceImpl pedidoService = new PedidoServiceImpl(pedidoDAO, envioService); // Instancia de PedidoServiceImpl con PedidoDAO y EnvioService

        this.menuHandler = new MenuHandler(scanner, pedidoService, envioService);
        this.running = true;
    }

    public static void main(String[] args) {
        AppMenu app = new AppMenu();
        app.run();
    }

    public void run() {
        while (running) {
            try {
                MenuDisplay.mostrarMenuPrincipal();
                int opcion = Integer.parseInt(scanner.nextLine());
                processOption(opcion);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Ingrese un número.");
            }
        }
        scanner.close();
    }

    private void processOption(int opcion) {
        switch (opcion) {
            // PEDIDOS
            case 1 -> menuHandler.crearPedido();
            case 2 -> menuHandler.listarPedidos();
            case 3 -> menuHandler.actualizarPedido();
            case 4 -> menuHandler.eliminarPedido();
            case 5 -> menuHandler.buscarPedidoPorNumero();

            // ENVIOS
            case 6  -> menuHandler.crearEnvioIndependiente();
            case 7  -> menuHandler.listarEnvios();
            case 8  -> menuHandler.actualizarEnvioPorId();
            case 9 -> menuHandler.eliminarEnvioPorId();

            case 0 -> {
                System.out.println("Saliendo...");
                running = false;
            }
            default -> System.out.println("Opción no válida.");
        }
    }
}
