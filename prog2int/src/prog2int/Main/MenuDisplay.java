package prog2int.Main;

/**
 * Muestra el menú de opciones para Pedido/Envio.
 */
public class MenuDisplay {

    public static void mostrarMenuPrincipal() {
        System.out.println("\n========= MENU PEDIDOS / ENVIOS =========");
        System.out.println("1. Crear pedido");
        System.out.println("2. Listar pedidos");
        System.out.println("3. Actualizar pedido");
        System.out.println("4. Eliminar pedido");
        System.out.println("5. Buscar pedido por número");

        System.out.println("6. Crear envío independiente");
        System.out.println("7. Listar envíos");
        System.out.println("8. Actualizar envío por ID");
        System.out.println("9. Eliminar envío por ID");

        System.out.println("0. Salir");
        System.out.print("Ingrese una opción: ");
    }
}
