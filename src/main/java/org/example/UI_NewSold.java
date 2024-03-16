package org.example;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import static org.example.Main.totalVenta;

public class UI_NewSold {

    private static JTextArea productosArea;
    private static JLabel totalLabel;
    private static Map<String, Integer> productosEnVenta;
    private static JTextField billeteField;
    private static double entregar;
    private static double billete;
    static void mostrarPanelVender(JTextField codigoBarrasDelete) {

        productosEnVenta = new HashMap<>();
        totalVenta = 0.0;
        JFrame frame;

        frame = new JFrame("Realizar Venta");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 400);

        JPanel panel = new JPanel(new BorderLayout());

        JPanel codigoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel codigoLabel = new JLabel("Código de Barras:");
        JTextField codigoBarrasField = new JTextField(15);

        codigoBarrasField.addActionListener(e -> buscarProducto(codigoBarrasField));
        codigoPanel.add(codigoLabel);
        codigoPanel.add(codigoBarrasField);


        JPanel productosPanel = new JPanel();
        productosPanel.setLayout(new BoxLayout(productosPanel, BoxLayout.Y_AXIS));
        productosArea = new JTextArea(10, 20);
        productosArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(productosArea);
        productosPanel.add(scrollPane);

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        totalLabel = new JLabel("Total: $0.0");
        JLabel billeteLabel = new JLabel("Billete:");
        billeteField = new JTextField(10);
        JButton calcularCambioButton = new JButton("Calcular Cambio");
        JButton eliminarProducto = new JButton("Eliminar item");
        calcularCambioButton.addActionListener(e -> calcularCambio());
        eliminarProducto.addActionListener(actionEvent -> deleteItem(codigoBarrasDelete));
        totalPanel.add(totalLabel);
        totalPanel.add(billeteLabel);
        totalPanel.add(billeteField);
        totalPanel.add(calcularCambioButton);
        totalPanel.add(eliminarProducto);

        panel.add(codigoPanel, BorderLayout.NORTH);
        panel.add(productosPanel, BorderLayout.CENTER);
        panel.add(totalPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);
    }


     static void calcularCambio() {
        JFrame frame = new JFrame();


        try {
            billete = Double.parseDouble(billeteField.getText());
            entregar = billete - totalVenta;
            if (entregar >= 0) {
                JOptionPane.showMessageDialog(frame, "Cambio: $" + entregar, "Cambio Calculado", JOptionPane.INFORMATION_MESSAGE);
                generarRecibo();

            } else {
                JOptionPane.showMessageDialog(frame, "El valor del billete es insuficiente.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Ingrese un valor válido para el billete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        billeteField.setText("");




    }

     static void reiniciarVenta() {
        productosEnVenta.clear();
        productosArea.setText("");
        totalVenta = 0.0;
        totalLabel.setText("Total: $0.0");



    }


     static void generarRecibo() {
        try {

            String rutaDirectorio = "/home/mathew/Vídeos/FACTURAS/";
            LocalDateTime ahora = LocalDateTime.now();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            String nombreArchivo = "recibo_" + ahora.format(formatter) + ".pdf";


            String rutaArchivo = rutaDirectorio + nombreArchivo;
            File directorio = new File(rutaDirectorio);
            if (!directorio.exists()) {
                directorio.mkdirs(); // Crear el directorio si no existe
            }


            PdfWriter writer = new PdfWriter(rutaArchivo);

            PdfDocument pdf = new PdfDocument(writer);

            Document document = new Document(pdf, PageSize.A4);

            PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);

            Paragraph paragraph = new Paragraph();

            document.add(new Paragraph("Recibo de Venta").setFont(font).setFontSize(18).setTextAlignment(TextAlignment.CENTER));
            Text label = new Text("Cliente: ").setFont(font).setFontSize(16);
            Text NameClient = new Text("Mateo Rodriguez"+".").setFontSize(13);
            paragraph.add(label);
            paragraph.add(NameClient);
            document.add(paragraph);
            document.add(new Paragraph("Productos:").setFont(font).setFontSize(16));
            document.add(new Paragraph(productosArea.getText()).setFontSize(14));
            document.add(new Paragraph("Total: $" + totalVenta).setFontSize(15).setTextAlignment(TextAlignment.RIGHT));
            document.add(new Paragraph("Pago: $" + billete).setFontSize(15).setTextAlignment(TextAlignment.RIGHT));
            document.add(new Paragraph("Cambio: $" + entregar).setFontSize(15).setTextAlignment(TextAlignment.RIGHT));
            document.add(new Paragraph("GRACIAS POR SU COMPRA").setFont(font).setFontSize(18).setTextAlignment(TextAlignment.CENTER));

            document.close();

            System.out.println("Recibo generado exitosamente en: " + rutaArchivo);
            reiniciarVenta();
        } catch (IOException e) {
            System.err.println("Error al generar el recibo: " + e.getMessage());

        }
    }


     static void buscarProducto(JTextField codigoBarrasField) {



        String codigoBarras = codigoBarrasField.getText().trim();
        Producto producto = ProductoDAO.obtenerProductoPorId(codigoBarras.trim());

        if (producto!=null){
            if (productosEnVenta.containsKey(codigoBarras)) {
                int cantidadActual = productosEnVenta.get(codigoBarras);
                productosEnVenta.put(codigoBarras, cantidadActual + 1);
            } else {
                // El producto no está en la lista, agregarlo
                productosEnVenta.put(codigoBarras, 1);
            }
            totalVenta += producto.getPrecio();
            actualizarProductosArea();
            totalLabel.setText("Total: $" + totalVenta);
        }else{
            System.out.println( "No se encontró un producto con el código de barras especificado.");

        }
        codigoBarrasField.setText("");
    }

     static void actualizarProductosArea() {
        productosArea.setText("");
        for (Map.Entry<String, Integer> entry : productosEnVenta.entrySet()) {
            String codigoBarras = entry.getKey();
            int cantidad = entry.getValue();
            Producto producto = ProductoDAO.obtenerProductoPorId(codigoBarras);
            if (producto != null) {
                productosArea.append(producto.getNombre() + " - $" + producto.getPrecio() + " x " + cantidad + "\n");
            }
        }
    }

     static void deleteItem(JTextField codigoBarrasDelete ) {

        JFrame frame = new JFrame("Eliminar Producto de la Venta");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 150);
        JTextField cantidadField;

        JPanel panel = new JPanel(new GridLayout(3, 2));

        JLabel codigoBarrasLabel = new JLabel("Código de Barras:");
        JLabel cantidadLabel = new JLabel("Cantidad a Eliminar:");
        cantidadField = new JTextField();

        panel.add(codigoBarrasLabel);
        panel.add(codigoBarrasDelete);
        panel.add(cantidadLabel);
        panel.add(cantidadField);

        JButton eliminarButton = new JButton("Eliminar");
        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String codigoBarras = codigoBarrasDelete.getText();
                try {
                    int cantidad = Integer.parseInt(cantidadField.getText());
                    eliminarProducto(codigoBarras, cantidad);
                    frame.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Por favor, ingrese una cantidad válida.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(eliminarButton);

        frame.add(panel);

        frame.setVisible(true);


    }

     static void eliminarProducto(String codigoBarras, int cantidad) {
        if (productosEnVenta.containsKey(codigoBarras)) {
            int cantidadActual = productosEnVenta.get(codigoBarras);
            if (cantidad >= cantidadActual) {
                // Si la cantidad especificada es mayor o igual que la cantidad en la venta, eliminar el producto completamente
                totalVenta -= ProductoDAO.obtenerProductoPorId(codigoBarras).getPrecio() * cantidadActual;
                productosEnVenta.remove(codigoBarras);
            } else {
                // Si la cantidad especificada es menor que la cantidad en la venta, reducir la cantidad en la venta
                totalVenta -= ProductoDAO.obtenerProductoPorId(codigoBarras).getPrecio() * cantidad;
                productosEnVenta.put(codigoBarras, cantidadActual - cantidad);
            }
            actualizarProductosArea();
            totalLabel.setText("Total: $" + totalVenta);
        } else {
            System.out.println("El producto no está a la venta");
        }
    }



}
