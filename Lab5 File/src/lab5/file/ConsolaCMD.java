package lab5.file;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.*;
import java.time.format.*;

public class ConsolaCMD extends JFrame {

    static String rutaActual = System.getProperty("user.home");

    private JTextArea pantalla;
    private JTextField campoEntrada;
    private JButton btnEjecutar;
    private JLabel etiquetaRuta;   
    static final Color BLANCO  = new Color(0, 255, 0);
    static final Color VERDE = new Color(255, 255, 255);
    static final Color NEGRO  = new Color(0, 0, 0);

    public ConsolaCMD() {
        setTitle("Administrator: Command Prompt");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        pantalla = new JTextArea();
        pantalla.setBackground(NEGRO);
        pantalla.setForeground(VERDE);
        pantalla.setFont(new Font("Consolas", Font.PLAIN, 14));
        pantalla.setEditable(false);
        pantalla.setLineWrap(true);
        pantalla.setWrapStyleWord(true);
        pantalla.setMargin(new Insets(8, 8, 8, 8));

        JScrollPane scroll = new JScrollPane(pantalla);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        JPanel panelInferior = new JPanel(new BorderLayout(5, 0));
        panelInferior.setBackground(NEGRO);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(5, 8, 8, 8));

        etiquetaRuta = new JLabel(rutaActual + ">");
        etiquetaRuta.setForeground(VERDE);
        etiquetaRuta.setFont(new Font("Consolas", Font.PLAIN, 14));

        campoEntrada = new JTextField();
        campoEntrada.setBackground(NEGRO);
        campoEntrada.setForeground(BLANCO);
        campoEntrada.setFont(new Font("Consolas", Font.PLAIN, 14));
        campoEntrada.setCaretColor(BLANCO);
        campoEntrada.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));

        btnEjecutar = new JButton("OK");
        btnEjecutar.setBackground(new Color(40, 40, 40));
        btnEjecutar.setForeground(VERDE);
        btnEjecutar.setFont(new Font("Consolas", Font.BOLD, 13));
        btnEjecutar.setFocusPainted(false);
        btnEjecutar.setBorder(BorderFactory.createLineBorder(VERDE));
        btnEjecutar.setPreferredSize(new Dimension(55, 30));

        panelInferior.add(etiquetaRuta, BorderLayout.WEST);
        panelInferior.add(campoEntrada, BorderLayout.CENTER);
        panelInferior.add(btnEjecutar, BorderLayout.EAST);

        add(scroll, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        btnEjecutar.addActionListener(e -> procesarComando());
        campoEntrada.addActionListener(e -> procesarComando());
        getRootPane().setDefaultButton(btnEjecutar);

        mostrarTexto("Microsoft Windows [Version 10.0.22621.521]");
        mostrarTexto("(c) Microsoft Corporation. All rights reserved.\n");
        mostrarRuta();

        setVisible(true);
        campoEntrada.requestFocus();
    }

    void mostrarTexto(String texto) {
        pantalla.append(texto + "\n");
        pantalla.setCaretPosition(pantalla.getDocument().getLength());
    }

    void mostrarError(String texto) {
        pantalla.append("[ERROR] " + texto + "\n");
        pantalla.setCaretPosition(pantalla.getDocument().getLength());
    }

    void mostrarRuta() {
        pantalla.append(rutaActual + "> ");
        pantalla.setCaretPosition(pantalla.getDocument().getLength());
    }

    void actualizarEtiquetaRuta() {
        etiquetaRuta.setText(rutaActual + ">");
    }
    void procesarComando() {
        String entrada = campoEntrada.getText().trim();
        campoEntrada.setText("");

        if (entrada.isEmpty()) return;

        mostrarTexto(entrada);

        if (entrada.equalsIgnoreCase("dir")) {
            ejecutarDir();

        } else if (entrada.equalsIgnoreCase("cd <..>")) {
            ejecutarRegresar();

        } else if (entrada.equals("<..>")) {
            ejecutarRegresar();

        } else if (entrada.toLowerCase().startsWith("cd ")) {
            ejecutarCd(entrada.substring(3).trim());

        } else if (entrada.equalsIgnoreCase("date")) {
            ejecutarDate();

        } else if (entrada.equalsIgnoreCase("time")) {
            ejecutarTime();

        } else if (entrada.toLowerCase().startsWith("mkdir ")) {
            ejecutarMkdir(entrada.substring(6).trim());

        } else if (entrada.toLowerCase().startsWith("mfile ")) {
            ejecutarMfile(entrada.substring(6).trim());

        } else if (entrada.toLowerCase().startsWith("rm ")) {
            ejecutarRm(entrada.substring(3).trim());

        } else if (entrada.toLowerCase().startsWith("wr ")) {
            ejecutarWr(entrada.substring(3).trim());

        } else if (entrada.toLowerCase().startsWith("rd ")) {
            ejecutarRd(entrada.substring(3).trim());

        } else {
            mostrarError("'" + entrada + "' no se reconoce como un comando.");
        }

        mostrarTexto("");
        mostrarRuta();
    }

    void ejecutarDir() {
        File carpeta = new File(rutaActual);
        File[] contenido = carpeta.listFiles();
        mostrarTexto("\n Directorio: " + rutaActual + "\n");
        if (contenido == null || contenido.length == 0) {
            mostrarTexto("  La carpeta está vacía.");
        } else {
            for (File f : contenido) {
                if (f.isDirectory()) mostrarTexto("  <DIR>     " + f.getName());
                else mostrarTexto("  <FILE>    " + f.getName());
            }
        }
    }

    void ejecutarCd(String nombreCarpeta) {
        File nueva = new File(rutaActual + File.separator + nombreCarpeta);
        if (nueva.exists() && nueva.isDirectory()) {
            rutaActual = nueva.getAbsolutePath();
            actualizarEtiquetaRuta();
            mostrarTexto("Carpeta actual: " + rutaActual);
        } else {
            mostrarError("No se encontró la carpeta: " + nombreCarpeta);
        }
    }

    void ejecutarRegresar() {
        File padre = new File(rutaActual).getParentFile();
        if (padre != null) {
            rutaActual = padre.getAbsolutePath();
            actualizarEtiquetaRuta();
            mostrarTexto("Regresaste a: " + rutaActual);
        } else {
            mostrarError("Ya estás en la raíz, no puedes regresar más.");
        }
    }

    void ejecutarDate() {
        LocalDate fecha = LocalDate.now();
        mostrarTexto("Fecha actual: " + fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    void ejecutarTime() {
        LocalTime hora = LocalTime.now();
        mostrarTexto("Hora actual: " + hora.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    void ejecutarMkdir(String nombre) {
        if (nombre.isEmpty()) { mostrarError("Debes ingresar un nombre para la carpeta."); return; }
        File nueva = new File(rutaActual + File.separator + nombre);
        if (nueva.exists()) {
            mostrarError("Ya existe una carpeta con el nombre: " + nombre);
        } else {
            if (nueva.mkdir()) mostrarTexto("Carpeta creada: " + nombre);
            else mostrarError("No se pudo crear la carpeta: " + nombre);
        }
    }

    void ejecutarMfile(String nombre) {
        if (nombre.isEmpty()) { mostrarError("Ejemplo: mfile datos.txt"); return; }
        if (!nombre.contains(".")) { mostrarError("El archivo debe tener extensión. Ejemplo: datos.txt"); return; }
        File nuevo = new File(rutaActual + File.separator + nombre);
        try {
            if (nuevo.exists()) mostrarError("Ya existe un archivo con el nombre: " + nombre);
            else {
                if (nuevo.createNewFile()) mostrarTexto("Archivo creado: " + nombre);
                else mostrarError("No se pudo crear el archivo: " + nombre);
            }
        } catch (IOException e) { mostrarError("Error: " + e.getMessage()); }
    }

    void ejecutarRm(String nombre) {
        if (nombre.isEmpty()) { mostrarError("Debes ingresar el nombre a eliminar."); return; }
        File elemento = new File(rutaActual + File.separator + nombre);
        if (!elemento.exists()) {
            mostrarError("No se encontró: " + nombre);
        } else if (elemento.isDirectory()) {
            if (elemento.list().length > 0) mostrarError("La carpeta no está vacía.");
            else {
                if (elemento.delete()) mostrarTexto("Carpeta eliminada: " + nombre);
                else mostrarError("No se pudo eliminar: " + nombre);
            }
        } else {
            if (elemento.delete()) mostrarTexto("Archivo eliminado: " + nombre);
            else mostrarError("No se pudo eliminar: " + nombre);
        }
    }


    void ejecutarWr(String nombreArchivo) {
        if (nombreArchivo.isEmpty()) { mostrarError("Ejemplo: Wr notas.txt"); return; }
        File archivo = new File(rutaActual + File.separator + nombreArchivo);
        if (!archivo.exists()) { mostrarError("El archivo no existe: " + nombreArchivo); return; }

        JTextArea areaEscritura = new JTextArea(10, 40);
        areaEscritura.setBackground(NEGRO);
        areaEscritura.setForeground(VERDE);
        areaEscritura.setFont(new Font("Consolas", Font.PLAIN, 13));
        areaEscritura.setCaretColor(BLANCO);

        JScrollPane scrollArea = new JScrollPane(areaEscritura);
        JLabel instruccion = new JLabel("Escribe el texto. La última línea debe ser EXIT");
        instruccion.setForeground(VERDE);
        instruccion.setFont(new Font("Consolas", Font.PLAIN, 12));

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(NEGRO);
        panel.add(instruccion, BorderLayout.NORTH);
        panel.add(scrollArea, BorderLayout.CENTER);

        int resultado = JOptionPane.showConfirmDialog(this, panel,
                "Escribir en: " + nombreArchivo, JOptionPane.OK_CANCEL_OPTION);

        if (resultado == JOptionPane.OK_OPTION) {
            String contenido = areaEscritura.getText();
            int indexExit = contenido.indexOf("EXIT");
           if(indexExit == -1){
               mostrarError("Debes escribir EXIT al final del texto para guardar");
               return; 
           }
           contenido = contenido.substring(0, indexExit); 
            try {
                FileWriter fw = new FileWriter(archivo, true);
                fw.write(contenido);
                fw.close();
                mostrarTexto("Escritura finalizada en: " + nombreArchivo);
            } catch (IOException e) { mostrarError("Error al escribir: " + e.getMessage()); }
        } else {
            mostrarTexto("Escritura cancelada.");
        }
    }

    void ejecutarRd(String nombreArchivo) {
        if (nombreArchivo.isEmpty()) { mostrarError("Ejemplo: Rd notas.txt"); return; }
        File archivo = new File(rutaActual + File.separator + nombreArchivo);
        if (!archivo.exists()) { mostrarError("El archivo no existe: " + nombreArchivo); return; }

        try {
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            StringBuilder contenido = new StringBuilder();
            String linea;
            int num = 1;
            while ((linea = br.readLine()) != null) {
                contenido.append(num).append(". ").append(linea).append("\n");
                num++;
            }
            br.close();

            mostrarTexto("\n── Contenido de " + nombreArchivo + " ──");
            if (contenido.length() == 0) mostrarTexto("  El archivo está vacío.");
            else mostrarTexto(contenido.toString());
            mostrarTexto("── Fin del archivo ──");

        } catch (IOException e) { mostrarError("Error al leer: " + e.getMessage()); }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ConsolaCMD());
    }
}
