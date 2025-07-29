/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PDF;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javax.swing.*;
import java.awt.Desktop;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GenerarPDF {

    public static String generarFactura(javax.swing.JTable tabla, String totalPagar, String metodoPago) {
        Document doc = new Document();
        String nombreArchivo = "";
        try {
            int numeroFactura = obtenerNumeroFactura();

            // Fecha y hora actual
            LocalDateTime ahora = LocalDateTime.now();
            DateTimeFormatter formatoVisible = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            DateTimeFormatter formatoArchivo = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
            String fechaHoraVenta = ahora.format(formatoVisible);
            String fechaHoraArchivo = ahora.format(formatoArchivo);

            // Nombre del archivo generado automáticamente
            nombreArchivo = "Factura_" + fechaHoraArchivo + ".pdf";
            String ruta = "src/pdf/" + nombreArchivo;

            FileOutputStream archivo = new FileOutputStream(ruta);
            PdfWriter.getInstance(doc, archivo);
            doc.open();

            // Estilos
            Font negrita = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font normal = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
            BaseColor colorEncabezado = new BaseColor(230, 230, 250);
            BaseColor colorTablaHeader = new BaseColor(200, 221, 242);

            // LOGO (opcional)
            try {
                Image logo = Image.getInstance("/imagen/Joystickland_logo_128x128.png");
                logo.scaleToFit(80, 80);
                logo.setAlignment(Image.ALIGN_CENTER);
                doc.add(logo);
            } catch (Exception e) {
                // Logo no obligatorio
            }

            // Encabezado de empresa
            PdfPTable header = new PdfPTable(1);
            PdfPCell celdaHeader = new PdfPCell();
            celdaHeader.setBackgroundColor(colorEncabezado);
            celdaHeader.setPadding(10);
            celdaHeader.setPhrase(new Phrase("Joystickland\nTeléfono: 951-125-8882\nDirección: Oaxaca Centro\nRazón Social: Viva la vida", negrita));
            celdaHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            celdaHeader.setBorder(Rectangle.NO_BORDER);
            header.addCell(celdaHeader);
            header.setWidthPercentage(100);
            doc.add(header);

            doc.add(new Paragraph("\n"));

            // Info factura
            Paragraph facturaInfo = new Paragraph("Factura N°: " + numeroFactura + "\nFecha y hora: " + fechaHoraVenta + "\n\n", negrita);
            facturaInfo.setAlignment(Element.ALIGN_RIGHT);
            doc.add(facturaInfo);

            // Título
            Paragraph titulo = new Paragraph("FACTURA DE VENTA\n\n", negrita);
            titulo.setAlignment(Element.ALIGN_CENTER);
            doc.add(titulo);

            // Tabla productos
            PdfPTable tablaPDF = new PdfPTable(4);
            tablaPDF.setWidthPercentage(100);
            tablaPDF.setSpacingBefore(10f);
            tablaPDF.setSpacingAfter(10f);
            tablaPDF.setWidths(new float[]{20f, 40f, 20f, 20f});

            String[] encabezados = {"Cantidad", "Descripción", "Precio Unitario", "Subtotal"};
            for (String enc : encabezados) {
                PdfPCell celda = new PdfPCell(new Phrase(enc, negrita));
                celda.setBackgroundColor(colorTablaHeader);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setPadding(5);
                tablaPDF.addCell(celda);
            }

            for (int i = 0; i < tabla.getRowCount(); i++) {
                tablaPDF.addCell(new Phrase(tabla.getValueAt(i, 2).toString(), normal)); // Cantidad
                tablaPDF.addCell(new Phrase(tabla.getValueAt(i, 0).toString(), normal)); // Descripción
                tablaPDF.addCell(new Phrase(tabla.getValueAt(i, 1).toString(), normal)); // Precio
                tablaPDF.addCell(new Phrase(tabla.getValueAt(i, 3).toString(), normal)); // Subtotal
            }

            doc.add(tablaPDF);

            // Método de pago
            Paragraph pago = new Paragraph("Método de pago: " + metodoPago + "\n", negrita);
            pago.setAlignment(Element.ALIGN_LEFT);
            doc.add(pago);

            // Total
            Paragraph total = new Paragraph("Total a pagar: " + totalPagar + "\n", negrita);
            total.setAlignment(Element.ALIGN_RIGHT);
            doc.add(total);

            // Agradecimiento
            Paragraph gracias = new Paragraph("\n¡Gracias por su compra!", negrita);
            gracias.setAlignment(Element.ALIGN_CENTER);
            doc.add(gracias);

            doc.close();
            archivo.close();

            // Abrir PDF
            Desktop.getDesktop().open(new File(ruta));

            return ruta;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "❌ Error al generar el PDF: " + e.getMessage());
            return null;
        }
    }

    private static int obtenerNumeroFactura() {
        int numeroFactura = 1;
        File archivoFactura = new File("src/pdf/numeroFactura.txt");

        try {
            if (archivoFactura.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(archivoFactura));
                String linea = br.readLine();
                if (linea != null) {
                    numeroFactura = Integer.parseInt(linea.trim()) + 1;
                }
                br.close();
            }

            BufferedWriter bw = new BufferedWriter(new FileWriter(archivoFactura));
            bw.write(String.valueOf(numeroFactura));
            bw.close();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al leer/escribir el número de factura: " + e.getMessage());
        }

        return numeroFactura;
    }
}
