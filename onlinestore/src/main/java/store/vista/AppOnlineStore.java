package store.vista;

import store.controlador.Controlador;
import store.modelo.*;

import java.util.List;
import java.util.Scanner;

public class AppOnlineStore {

    private static final Scanner sc = new Scanner(System.in);
    private static final Controlador controlador = new Controlador();

    public static void main(String[] args) {
        boolean salir = false;

        while (!salir) {
            mostrarMenu();
            int opcion = leerInt("Elige una opción: ", 0, 3);

            switch (opcion) {
                case 1 -> gestionArticulos();
                case 2 -> gestionClientes();
                case 3 -> gestionPedidos();
                case 0 -> salir = true;
            }
        }

        System.out.println("¡Hasta luego!");
    }

    private static void mostrarMenu() {
        System.out.println("\n--- MENÚ ONLINE STORE ---");
        System.out.println("1. Gestión de Artículos");
        System.out.println("2. Gestión de Clientes");
        System.out.println("3. Gestión de Pedidos");
        System.out.println("0. Salir");
    }

    // ---------- ARTÍCULOS ----------
    private static void gestionArticulos() {
        System.out.println("\n--- Gestión de Artículos ---");
        System.out.println("1. Añadir Artículo");
        System.out.println("2. Mostrar Artículos");
        int opcion = leerInt("Elige una opción: ", 1, 2);

        switch (opcion) {
            case 1 -> añadirArticulo();
            case 2 -> mostrarArticulos();
        }
    }

    private static void añadirArticulo() {
        System.out.print("Código: ");
        String codigo = sc.nextLine();

        System.out.print("Descripción: ");
        String descripcion = sc.nextLine();

        double precio = leerDouble("Precio venta: ", 0, Double.MAX_VALUE);
        double envio = leerDouble("Gastos de envío: ", 0, Double.MAX_VALUE);
        int tiempo = leerInt("Tiempo preparación (minutos): ", 1, Integer.MAX_VALUE);

        controlador.anadirArticulo(codigo, descripcion, precio, envio, tiempo);
        System.out.println("Artículo añadido correctamente.");
    }

    private static void mostrarArticulos() {
        List<Articulo> articulos = controlador.getArticulos();

        if (articulos.isEmpty()) {
            System.out.println("No hay artículos registrados.");
        } else {
            articulos.forEach(System.out::println);
        }
    }

    // ---------- CLIENTES ----------
    private static void gestionClientes() {
        System.out.println("\n--- Gestión de Clientes ---");
        System.out.println("1. Añadir Cliente");
        System.out.println("2. Mostrar Todos los Clientes");
        System.out.println("3. Mostrar Clientes Estándar");
        System.out.println("4. Mostrar Clientes Premium");

        int opcion = leerInt("Elige una opción: ", 1, 4);
        switch (opcion) {
            case 1 -> anadirCliente();
            case 2 -> controlador.getClientes().values().forEach(System.out::println);
            case 3 -> controlador.getClientesEstandar().forEach(System.out::println);
            case 4 -> controlador.getClientesPremium().forEach(System.out::println);
        }
    }

    private static void anadirCliente() {
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();

        System.out.print("Domicilio: ");
        String domicilio = sc.nextLine();

        System.out.print("NIF: ");
        String nif = sc.nextLine();

        String email;
        do {
            System.out.print("Email: ");
            email = sc.nextLine();

            if (email.isEmpty() || controlador.existeCliente(email)) {
                System.out.println("Email vacío o ya registrado. Intenta otro.");
            }

        } while (email.isEmpty() || controlador.existeCliente(email));

        String tipo;
        do {
            System.out.print("¿Es cliente Premium? (s/n): ");
            tipo = sc.nextLine().toLowerCase();
        } while (!tipo.equals("s") && !tipo.equals("n"));

        controlador.anadirCliente(nombre, domicilio, nif, email, tipo.equals("s"));
        System.out.println("Cliente añadido correctamente.");
    }

    // ---------- PEDIDOS ----------
    private static void gestionPedidos() {
        System.out.println("\n--- Gestión de Pedidos ---");
        System.out.println("1. Añadir Pedido");
        System.out.println("2. Eliminar Pedido");
        System.out.println("3. Mostrar Pedidos Pendientes");
        System.out.println("4. Mostrar Pedidos Enviados");

        int opcion = leerInt("Elige una opción: ", 1, 4);

        switch (opcion) {
            case 1 -> añadirPedido();
            case 2 -> eliminarPedido();
            case 3 -> mostrarPedidosPendientes();
            case 4 -> mostrarPedidosEnviados();
        }
    }

    private static void añadirPedido() {
        System.out.print("Email del cliente: ");
        String email = sc.nextLine();

        System.out.print("Código del artículo: ");
        String codigo = sc.nextLine();

        int cantidad = leerInt("Cantidad: ", 1, Integer.MAX_VALUE);

        try {
            controlador.anadirPedido(email, codigo, cantidad);
            System.out.println("Pedido añadido correctamente.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void eliminarPedido() {
        int num = leerInt("Número de pedido a eliminar: ", 1, Integer.MAX_VALUE);

        try {
            controlador.eliminarPedido(num);
            System.out.println("Pedido eliminado correctamente.");
        } catch (PedidoNoCancelableException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void mostrarPedidosPendientes() {
        System.out.print("Filtrar por cliente (email, vacío para todos): ");
        String email = sc.nextLine();
        controlador.getPedidosPendientes(email).forEach(System.out::println);
    }

    private static void mostrarPedidosEnviados() {
        System.out.print("Filtrar por cliente (email, vacío para todos): ");
        String email = sc.nextLine();
        controlador.getPedidosEnviados(email).forEach(System.out::println);
    }

    // ---------- VALIDACIONES ----------
    private static int leerInt(String mensaje, int min, int max) {
        int valor = 0;
        while (true) {
            System.out.print(mensaje);
            try {
                valor = Integer.parseInt(sc.nextLine());
                if (valor >= min && valor <= max) return valor;
                System.out.println("Valor fuera de rango.");
            } catch (NumberFormatException e) {
                System.out.println("Introduce un número entero válido.");
            }
        }
    }

    private static double leerDouble(String mensaje, double min, double max) {
        double valor = 0;
        while (true) {
            System.out.print(mensaje);
            try {
                valor = Double.parseDouble(sc.nextLine());
                if (valor >= min && valor <= max) return valor;
                System.out.println("Valor fuera de rango.");
            } catch (NumberFormatException e) {
                System.out.println("Introduce un número válido.");
            }
        }
    }
}