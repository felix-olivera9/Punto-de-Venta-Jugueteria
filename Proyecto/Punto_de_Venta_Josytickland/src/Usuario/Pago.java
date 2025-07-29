/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Usuario;

import Conexion.ConexionMySQL;
import static Conexion.ConexionMySQL.conectar;
import PDF.GenerarPDF;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;


/*import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;*/



/**
 *
 * @author HP
 */
public class Pago extends javax.swing.JFrame {
    /*FondoPago fondoPag = new FondoPago();*/

    
    public Pago() {
        initComponents();
        this.setSize(new Dimension(800,620));
        this.setLocationRelativeTo(null);
        /*this.setContentPane(fondoPag);*/
        /*this.setTitle("Pago");*/
        /*this.setLocationRelativeTo(null);*/ // Centra la ventana
        
        
         btncerr.setVisible(false);
         ConexionMySQL con=new ConexionMySQL();
        ConexionMySQL.configurar("root", "Olivera1234", "localhost", "3306", "bd");
        ConexionMySQL.conectar();
    ajustarImagenDeFondo(lblImagenFondo, "/imagen/FPago.png");
    
    lblImagenFondo.addComponentListener(new java.awt.event.ComponentAdapter() {
    @Override
    public void componentResized(java.awt.event.ComponentEvent evt) {
        ajustarImagenDeFondo(lblImagenFondo, "/imagen/FPago.png");
    }
});

    // Oculta los paneles al iniciar
    PanelEfectivo.setVisible(false);
    PanelTarjeta.setVisible(false);

    // Listener para método Efectivo
    rdbEfectivo.addActionListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            PanelEfectivo.setVisible(true);
            PanelTarjeta.setVisible(false);
        }
    });

    // Listener para método Tarjeta
    rdbTarjeta.addActionListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            PanelEfectivo.setVisible(false);
            PanelTarjeta.setVisible(true);
        }
    });
    
    btnCalcularCambio.addActionListener(new java.awt.event.ActionListener() {
    @Override
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnCalcularCambioActionPerformed(evt);
    }
    });
    
    btnPagoTarjeta.addActionListener(new java.awt.event.ActionListener() {
    @Override
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnPagoTarjetaActionPerformed(evt);
    }
    });
    
            txtProducto.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {  
                
                  buscarProductoPorNombre();  
                
                
                
            }

            public void removeUpdate(DocumentEvent e) {
               
                  buscarProductoPorNombre();  
            }

            public void changedUpdate(DocumentEvent e) {

            }
            
           
    public void buscarProductoPorNombre() {
        String texto = txtProducto.getText().trim();
    if (texto.isEmpty()) {
        cargarProductos();
        return;
    }

    DefaultTableModel modelo = new DefaultTableModel();
    modelo.addColumn("Producto");
    modelo.addColumn("Estado");
    modelo.addColumn("Stock");


    // Usamos LOWER para ignorar mayúsculas/minúsculas
    String sql = "SELECT * FROM producto WHERE LOWER(nombre) LIKE ?";

    try {
        Connection con = conectar();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, "%" + texto.toLowerCase() + "%");

        ResultSet rs = ps.executeQuery();

        boolean encontrado = false;

        while (rs.next()) {
            Object[] fila = new Object[]{
               
                rs.getString("nombre"),
                rs.getString("estado"),
            
                rs.getInt("stock")
            };
            modelo.addRow(fila);
            encontrado = true;
        }

        tblBusqueda.setModel(modelo);

        if (!encontrado) {
            JOptionPane.showMessageDialog(null, "No se encontró ningún producto con ese nombre.");
        }

        rs.close();
        ps.close();
        con.close();

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al buscar producto: " + e.getMessage());
    }
     }
  
    
        });
    
    this.cargarProductos();
    
    }
 
    
    public void activarcerrar(){
        btncerr.setVisible(true);
    }
    

  public void insertarVentasDesdeTabla() {
    String numeroTarjeta = txtTarjeta.getText().trim();
    String correoAsociado = null;
    Connection con = conectar();

    try {
        // 1. Buscar si la tarjeta ya existe
        String consultaCorreo = "SELECT correo FROM venta WHERE numero_tarjeta = ? LIMIT 1";
        PreparedStatement psBuscar = con.prepareStatement(consultaCorreo);
        psBuscar.setString(1, numeroTarjeta);
        ResultSet rs = psBuscar.executeQuery();

        if (rs.next()) {
            correoAsociado = rs.getString("correo");
            JOptionPane.showMessageDialog(null, "Se envió un recibo a: " + correoAsociado);
        }

        // 2. Insertar cada producto de la tabla en la base de datos
        String insertSQL = "INSERT INTO venta (correo, numero_tarjeta, nombre_producto, cantidad, precio_unitario) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement psInsert = con.prepareStatement(insertSQL);

        int filas = tblProductos.getRowCount();
        for (int i = 0; i < filas; i++) {
            String nombreProducto = tblProductos.getValueAt(i, 0).toString(); // Producto
            double precioUnitario = Double.parseDouble(tblProductos.getValueAt(i, 1).toString()); // Precio Unitario
            int cantidad = Integer.parseInt(tblProductos.getValueAt(i, 2).toString()); // Cantidad

            String correo = (correoAsociado != null) ? correoAsociado : "correo@desconocido.com";

            psInsert.setString(1, correo);
            psInsert.setString(2, numeroTarjeta);
            psInsert.setString(3, nombreProducto);
            psInsert.setInt(4, cantidad);
            psInsert.setDouble(5, precioUnitario);

            psInsert.executeUpdate();
        }

        JOptionPane.showMessageDialog(null, "Ventas registradas exitosamente.");

    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error al insertar ventas: " + ex.getMessage());
    }
}

    
    
    public void restarCantidadDelRegistro() {
    // Obtener el modelo de la tabla
    DefaultTableModel model = (DefaultTableModel) tblProductos.getModel();
    
    // Obtener la fila seleccionada
    int filaSeleccionada = tblProductos.getSelectedRow();
    
    // Verificar si hay una fila seleccionada
    if (filaSeleccionada == -1) {
        JOptionPane.showMessageDialog(null, "Por favor, seleccione un registro", 
                                    "Advertencia", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // Obtener la cantidad actual (asumiendo que es la 3ra columna, índice 2)
    int columnaCantidad = 2; // Índice base 0 para la 3ra columna
    int cantidadActual = Integer.parseInt(model.getValueAt(filaSeleccionada, columnaCantidad).toString());
    
    // Pedir la cantidad a restar
    String input = JOptionPane.showInputDialog(null, 
            "Cantidad actual: " + cantidadActual + "\nIngrese la cantidad a restar:", 
            "Restar cantidad", 
            JOptionPane.QUESTION_MESSAGE);
    
    // Validar si se canceló el diálogo
    if (input == null || input.trim().isEmpty()) {
        return;
    }
    
    try {
        int cantidadARestar = Integer.parseInt(input);
        
        // Validar que la cantidad a restar sea positiva
        if (cantidadARestar <= 0) {
            JOptionPane.showMessageDialog(null, "Debe ingresar un valor positivo", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validar que no se quiera restar más de lo disponible
        if (cantidadARestar > cantidadActual) {
            JOptionPane.showMessageDialog(null, 
                    "No hay suficiente cantidad\nCantidad disponible: " + cantidadActual, 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Calcular nueva cantidad
        int nuevaCantidad = cantidadActual - cantidadARestar;
        
        // Confirmar la operación
        int confirmacion = JOptionPane.showConfirmDialog(null, 
                "¿Desea restar " + cantidadARestar + " unidades?\nNueva cantidad: " + nuevaCantidad, 
                "Confirmar operación", 
                JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            // Actualizar o eliminar según corresponda
            if (nuevaCantidad == 0) {
                // Eliminar el registro si la cantidad llega a cero
                model.removeRow(filaSeleccionada);
                //JOptionPane.showMessageDialog(null, "Registro eliminado (cantidad llegó a cero)");
            } else {
                // Actualizar la cantidad
                model.setValueAt(nuevaCantidad, filaSeleccionada, columnaCantidad);
                //JOptionPane.showMessageDialog(null, "Cantidad actualizada correctamente");
            }
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Debe ingresar un número válido", 
                                    "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    

    
    private void cargarProductos() {
    DefaultTableModel modelo = new DefaultTableModel();
   
    modelo.addColumn("Producto");
    modelo.addColumn("Estado");
    modelo.addColumn("Stock");

    ResultSet rs = ConexionMySQL.listarTodo("producto");

    try {
        while (rs != null && rs.next()) {
            modelo.addRow(new Object[]{
               
                rs.getString("nombre"),
                rs.getString("estado"),
              
                rs.getInt("stock")
                
            });
        }
        tblBusqueda.setModel(modelo); // Asegúrate de que TablaDatos sea tu JTable
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al cargar productos: " + e.getMessage());
    }
}
    
    private void cargarAgregados() {
        int fila = tblBusqueda.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Selecciona un producto primero.");
            return;
        }

        String cantidadStr = txtCantidad.getText().trim();
        if (cantidadStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingresa una cantidad válida.");
            return;
        }

        try {
            int cantidad = Integer.parseInt(cantidadStr);
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(null, "La cantidad debe ser mayor que cero.");
                return;
            }

            String nombreProducto = tblBusqueda.getValueAt(fila, 0).toString();
            int stock = Integer.parseInt(tblBusqueda.getValueAt(fila, 2).toString());

            if (cantidad > stock) {
                JOptionPane.showMessageDialog(null, "La cantidad supera el stock disponible.");
                return;
            }

            double precio = obtenerPrecioDesdeBD(nombreProducto);
            if (precio < 0) {
                JOptionPane.showMessageDialog(null, "No se encontró el precio del producto.");
                return;
            }

            double subtotal = precio * cantidad;

            DefaultTableModel modelo = (DefaultTableModel) tblProductos.getModel();

            if (modelo.getColumnCount() == 0) {
                modelo.addColumn("Producto");
                modelo.addColumn("Precio unitario");
                modelo.addColumn("Cantidad");
                modelo.addColumn("Subtotal");
            }

            boolean productoExiste = false;
            for (int i = 0; i < modelo.getRowCount(); i++) {
                String productoExistente = modelo.getValueAt(i, 0).toString();
                if (productoExistente.equalsIgnoreCase(nombreProducto)) {
                    int cantidadExistente = Integer.parseInt(modelo.getValueAt(i, 2).toString());
                    int nuevaCantidad = cantidadExistente + cantidad;
                    double nuevoSubtotal = nuevaCantidad * precio;
                    modelo.setValueAt(nuevaCantidad, i, 2);
                    modelo.setValueAt(nuevoSubtotal, i, 3);
                    productoExiste = true;
                    break;
                }
            }

            if (!productoExiste) {
                modelo.addRow(new Object[]{nombreProducto, precio, cantidad, subtotal});
            }

            tblProductos.setModel(modelo);
            txtCantidad.setText("");
            this.calcularTotales();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Cantidad no válida.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
}

        private void calcularTotales() {
        DefaultTableModel model = (DefaultTableModel) tblProductos.getModel();
        double subtotal = 0.0;
        for (int i = 0; i < model.getRowCount(); i++) {
            Object valor = model.getValueAt(i, 3);
            if (valor != null) {
                subtotal += Double.parseDouble(valor.toString());
            }
        }
        txtSubtotal.setText(String.format("$%,.2f", subtotal));
        txttotalPago.setText(String.format("$%,.2f", subtotal * 1.16));
    }


    
private double obtenerPrecioDesdeBD(String nombreProducto) {
    double precio = -1;
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        con = conectar();
        String sql = "SELECT precio FROM producto WHERE nombre = ?";
        ps = con.prepareStatement(sql);
        ps.setString(1, nombreProducto);
        rs = ps.executeQuery();

        if (rs.next()) {
            precio = rs.getDouble("precio");
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al consultar precio: " + e.getMessage());
    } finally {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            // Ignorar
        }
    }

    return precio;
}    


public void actualizarStockDesdeTabla() {
    try {
        int filas = tblProductos.getRowCount();

        for (int i = 0; i < filas; i++) {
            String nombreProducto = tblProductos.getValueAt(i, 0).toString(); // columna 0: nombre producto
            int cantidadVendida = Integer.parseInt(tblProductos.getValueAt(i, 2).toString()); // columna 2: cantidad
            Connection con = conectar();
            // 1. Obtener stock actual
            String selectSQL = "SELECT stock FROM producto WHERE nombre = ?";
            PreparedStatement psSelect = con.prepareStatement(selectSQL);
            psSelect.setString(1, nombreProducto);
            ResultSet rs = psSelect.executeQuery();

            if (rs.next()) {
                int stockActual = rs.getInt("stock");
                int nuevoStock = stockActual - cantidadVendida;

                // 2. Actualizar nuevo stock
                String updateSQL = "UPDATE producto SET stock = ? WHERE nombre = ?";
                PreparedStatement psUpdate = con.prepareStatement(updateSQL);
                psUpdate.setInt(1, nuevoStock);
                psUpdate.setString(2, nombreProducto);
                psUpdate.executeUpdate();
            }

            rs.close();
            psSelect.close();
        }

        JOptionPane.showMessageDialog(null, "Stock actualizado correctamente.");

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error al actualizar el stock: " + e.getMessage());
    }
}

  @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grupoMetodoPago = new javax.swing.ButtonGroup();
        lblTitulo = new javax.swing.JLabel();
        lblProducto = new javax.swing.JLabel();
        txtProducto = new javax.swing.JTextField();
        lblCantidad = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        btnAgregar = new javax.swing.JButton();
        PanelProductos = new javax.swing.JPanel();
        scrollProductos = new javax.swing.JScrollPane();
        tblProductos = new javax.swing.JTable();
        btnRegistrarVenta = new javax.swing.JButton();
        btnEliminarProducto = new javax.swing.JButton();
        btnRegistrar = new javax.swing.JButton();
        lblRegistrarVenta = new javax.swing.JLabel();
        lblEliminarProducto = new javax.swing.JLabel();
        lblSubTotal = new javax.swing.JLabel();
        txtSubtotal = new javax.swing.JLabel();
        lblTotalPago = new javax.swing.JLabel();
        txttotalPago = new javax.swing.JLabel();
        PanelEfectivo = new javax.swing.JPanel();
        lblEfectivoRecibido = new javax.swing.JLabel();
        txtEfectivo = new javax.swing.JTextField();
        btnCalcularCambio = new javax.swing.JButton();
        lblCambios = new javax.swing.JLabel();
        lblCambio = new javax.swing.JLabel();
        PanelTarjeta = new javax.swing.JPanel();
        lblTarjerta = new javax.swing.JLabel();
        txtTitular = new javax.swing.JTextField();
        lblTitular = new javax.swing.JLabel();
        lblExpiracion = new javax.swing.JLabel();
        txtTarjeta = new javax.swing.JTextField();
        txtAnio = new javax.swing.JTextField();
        txtMes = new javax.swing.JTextField();
        lblCVV = new javax.swing.JLabel();
        txtCVV = new javax.swing.JTextField();
        lblMes = new javax.swing.JLabel();
        lblAnio = new javax.swing.JLabel();
        btnPagoTarjeta = new javax.swing.JButton();
        lblCC = new javax.swing.JLabel();
        txtCC = new javax.swing.JLabel();
        lblNuevaVenta = new javax.swing.JLabel();
        panelBusqueda = new javax.swing.JPanel();
        scrollBusqueda = new javax.swing.JScrollPane();
        tblBusqueda = new javax.swing.JTable();
        lblTablaVerProductos = new javax.swing.JLabel();
        lblTablaBusquedaProductos = new javax.swing.JLabel();
        btnBuscar = new javax.swing.JButton();
        PanelMetodosPago = new javax.swing.JPanel();
        lblMetodoPagp = new javax.swing.JLabel();
        rdbTarjeta = new javax.swing.JRadioButton();
        rdbEfectivo = new javax.swing.JRadioButton();
        btncerr = new javax.swing.JButton();
        lblImagenFondo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(0, 0, 204));
        lblTitulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagen/notas24.png"))); // NOI18N
        lblTitulo.setText(" Sistema de Facturación ");
        getContentPane().add(lblTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(248, 16, -1, 38));

        lblProducto.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblProducto.setText("Producto:");
        getContentPane().add(lblProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(54, 66, -1, -1));
        getContentPane().add(txtProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(54, 92, 225, -1));

        lblCantidad.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblCantidad.setText("Cantidad:");
        getContentPane().add(lblCantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 120, -1, -1));
        getContentPane().add(txtCantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 120, 71, -1));

        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagen/agregar-producto.png"))); // NOI18N
        btnAgregar.setBorder(null);
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });
        getContentPane().add(btnAgregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 80, 57, 40));

        PanelProductos.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblProductos.setBackground(new java.awt.Color(255, 255, 204));
        tblProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Producto", "Precio Unitario", "Cantidad", "SubTotal"
            }
        ));
        scrollProductos.setViewportView(tblProductos);

        javax.swing.GroupLayout PanelProductosLayout = new javax.swing.GroupLayout(PanelProductos);
        PanelProductos.setLayout(PanelProductosLayout);
        PanelProductosLayout.setHorizontalGroup(
            PanelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollProductos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
        );
        PanelProductosLayout.setVerticalGroup(
            PanelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollProductos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
        );

        getContentPane().add(PanelProductos, new org.netbeans.lib.awtextra.AbsoluteConstraints(54, 362, -1, 140));

        btnRegistrarVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagen/cajero-automatico.png"))); // NOI18N
        btnRegistrarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarVentaActionPerformed(evt);
            }
        });
        getContentPane().add(btnRegistrarVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 520, -1, -1));

        btnEliminarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagen/eliminar.png"))); // NOI18N
        btnEliminarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarProductoActionPerformed(evt);
            }
        });
        getContentPane().add(btnEliminarProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 520, -1, -1));

        btnRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagen/casa.png"))); // NOI18N
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });
        getContentPane().add(btnRegistrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 520, -1, -1));

        lblRegistrarVenta.setText("Registrar Venta");
        getContentPane().add(lblRegistrarVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 560, -1, -1));

        lblEliminarProducto.setText("Eliminar Producto");
        getContentPane().add(lblEliminarProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 560, -1, -1));

        lblSubTotal.setFont(new java.awt.Font("Segoe UI Historic", 1, 12)); // NOI18N
        lblSubTotal.setText("SubTotal:");
        getContentPane().add(lblSubTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 150, -1, -1));

        txtSubtotal.setBackground(new java.awt.Color(255, 255, 255));
        txtSubtotal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtSubtotal.setOpaque(true);
        getContentPane().add(txtSubtotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 150, 110, 20));

        lblTotalPago.setFont(new java.awt.Font("Segoe UI Historic", 1, 12)); // NOI18N
        lblTotalPago.setText("Total a pagar:");
        getContentPane().add(lblTotalPago, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 180, -1, -1));

        txttotalPago.setBackground(new java.awt.Color(255, 255, 255));
        txttotalPago.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txttotalPago.setOpaque(true);
        getContentPane().add(txttotalPago, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 180, 110, 20));

        PanelEfectivo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblEfectivoRecibido.setFont(new java.awt.Font("Segoe UI Historic", 1, 12)); // NOI18N
        lblEfectivoRecibido.setText("Efectivo Recibido:");
        PanelEfectivo.add(lblEfectivoRecibido, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 9, -1, -1));

        txtEfectivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEfectivoActionPerformed(evt);
            }
        });
        PanelEfectivo.add(txtEfectivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(112, 6, 80, -1));

        btnCalcularCambio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagen/tipo-de-cambio.png"))); // NOI18N
        btnCalcularCambio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcularCambioActionPerformed(evt);
            }
        });
        PanelEfectivo.add(btnCalcularCambio, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 10, -1, -1));

        lblCambios.setFont(new java.awt.Font("Segoe UI Historic", 1, 12)); // NOI18N
        lblCambios.setText("Cambio:");
        PanelEfectivo.add(lblCambios, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        lblCambio.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        PanelEfectivo.add(lblCambio, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 40, 80, 20));

        getContentPane().add(PanelEfectivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 270, 310, 70));

        PanelTarjeta.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTarjerta.setFont(new java.awt.Font("Segoe UI Historic", 1, 12)); // NOI18N
        lblTarjerta.setText("Numero de tarjeta");
        PanelTarjeta.add(lblTarjerta, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 56, -1, -1));
        PanelTarjeta.add(txtTitular, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 28, 170, -1));

        lblTitular.setFont(new java.awt.Font("Segoe UI Historic", 1, 12)); // NOI18N
        lblTitular.setText("Nombre del titular");
        PanelTarjeta.add(lblTitular, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, -1, -1));

        lblExpiracion.setFont(new java.awt.Font("Segoe UI Historic", 1, 12)); // NOI18N
        lblExpiracion.setText("Fecha de expiracion");
        PanelTarjeta.add(lblExpiracion, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, -1, -1));
        PanelTarjeta.add(txtTarjeta, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 78, 169, -1));

        txtAnio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAnioActionPerformed(evt);
            }
        });
        PanelTarjeta.add(txtAnio, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 130, 48, -1));

        txtMes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMesActionPerformed(evt);
            }
        });
        PanelTarjeta.add(txtMes, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 130, 49, -1));

        lblCVV.setFont(new java.awt.Font("Segoe UI Historic", 1, 12)); // NOI18N
        lblCVV.setText("CVV");
        PanelTarjeta.add(lblCVV, new org.netbeans.lib.awtextra.AbsoluteConstraints(201, 31, -1, -1));
        PanelTarjeta.add(txtCVV, new org.netbeans.lib.awtextra.AbsoluteConstraints(236, 28, 35, -1));

        lblMes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagen/calendario.png"))); // NOI18N
        PanelTarjeta.add(lblMes, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, -1, -1));

        lblAnio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagen/enero.png"))); // NOI18N
        PanelTarjeta.add(lblAnio, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 130, -1, -1));

        btnPagoTarjeta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagen/tarjeta-de-debito.png"))); // NOI18N
        btnPagoTarjeta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPagoTarjetaActionPerformed(evt);
            }
        });
        PanelTarjeta.add(btnPagoTarjeta, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 110, -1, -1));

        lblCC.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblCC.setText("Tipo CC");
        PanelTarjeta.add(lblCC, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 60, -1, -1));

        txtCC.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        PanelTarjeta.add(txtCC, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 80, 100, 20));

        getContentPane().add(PanelTarjeta, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 270, 310, 170));

        lblNuevaVenta.setText("Nueva Venta");
        getContentPane().add(lblNuevaVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 560, -1, -1));

        panelBusqueda.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblBusqueda.setBackground(new java.awt.Color(204, 255, 204));
        tblBusqueda.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Producto", "Estado", "Stock"
            }
        ));
        scrollBusqueda.setViewportView(tblBusqueda);

        javax.swing.GroupLayout panelBusquedaLayout = new javax.swing.GroupLayout(panelBusqueda);
        panelBusqueda.setLayout(panelBusquedaLayout);
        panelBusquedaLayout.setHorizontalGroup(
            panelBusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollBusqueda, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
        );
        panelBusquedaLayout.setVerticalGroup(
            panelBusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollBusqueda, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
        );

        getContentPane().add(panelBusqueda, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 180, 400, 140));

        lblTablaVerProductos.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTablaVerProductos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagen/dinero.png"))); // NOI18N
        lblTablaVerProductos.setText("Tabla-Venta-Productos");
        getContentPane().add(lblTablaVerProductos, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 330, -1, -1));

        lblTablaBusquedaProductos.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTablaBusquedaProductos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagen/encontrar.png"))); // NOI18N
        lblTablaBusquedaProductos.setText("Tabla-Busqueda-Productos");
        getContentPane().add(lblTablaBusquedaProductos, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 150, -1, -1));

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagen/buscar.png"))); // NOI18N
        btnBuscar.setBorder(null);
        getContentPane().add(btnBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 80, 50, 40));

        lblMetodoPagp.setFont(new java.awt.Font("Segoe UI Historic", 1, 12)); // NOI18N
        lblMetodoPagp.setText("MetodoPago");

        grupoMetodoPago.add(rdbTarjeta);
        rdbTarjeta.setFont(new java.awt.Font("Segoe UI Historic", 1, 12)); // NOI18N
        rdbTarjeta.setText("Tarjeta");

        grupoMetodoPago.add(rdbEfectivo);
        rdbEfectivo.setFont(new java.awt.Font("Segoe UI Historic", 1, 12)); // NOI18N
        rdbEfectivo.setText("Efectivo");

        javax.swing.GroupLayout PanelMetodosPagoLayout = new javax.swing.GroupLayout(PanelMetodosPago);
        PanelMetodosPago.setLayout(PanelMetodosPagoLayout);
        PanelMetodosPagoLayout.setHorizontalGroup(
            PanelMetodosPagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelMetodosPagoLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblMetodoPagp)
                .addGap(30, 30, 30)
                .addComponent(rdbTarjeta)
                .addGap(20, 20, 20)
                .addComponent(rdbEfectivo)
                .addContainerGap(29, Short.MAX_VALUE))
        );
        PanelMetodosPagoLayout.setVerticalGroup(
            PanelMetodosPagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelMetodosPagoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelMetodosPagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelMetodosPagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rdbTarjeta)
                        .addComponent(lblMetodoPagp))
                    .addComponent(rdbEfectivo))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        getContentPane().add(PanelMetodosPago, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 220, 300, 40));

        btncerr.setText("Cerrar");
        btncerr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncerrActionPerformed(evt);
            }
        });
        getContentPane().add(btncerr, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 520, -1, -1));

        lblImagenFondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagen/FPago.png"))); // NOI18N
        lblImagenFondo.setText("jLabel1");
        getContentPane().add(lblImagenFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 620));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtMesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMesActionPerformed

    private void txtAnioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAnioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAnioActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        // TODO add your handling code here:
        int fila = tblBusqueda.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione su producto primero.");
            
        }else{

            
     this.cargarAgregados();
        
       
        }
        
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void txtEfectivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEfectivoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEfectivoActionPerformed

    private void btnCalcularCambioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcularCambioActionPerformed

    try {
        // 1. Obtener y validar el total a pagar
        if (txttotalPago.getText().isEmpty() || txttotalPago.getText().equals("$0.00")) {
            JOptionPane.showMessageDialog(null, "No hay cantidad a pagar", 
                                      "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        double totalPagar = Double.parseDouble(
            txttotalPago.getText().replaceAll("[^\\d.]", ""));
        
        // 2. Obtener y validar el efectivo
        if (txtEfectivo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese la cantidad recibida", 
                                      "Advertencia", JOptionPane.WARNING_MESSAGE);
            txtEfectivo.requestFocus();
            return;
        }
        
        double efectivo = Double.parseDouble(
            txtEfectivo.getText().replaceAll("[^\\d.]", ""));
        
        // 3. Validar que el efectivo sea positivo
        if (efectivo <= 0) {
            JOptionPane.showMessageDialog(null, "La cantidad recibida debe ser mayor a cero", 
                                      "Error", JOptionPane.ERROR_MESSAGE);
            txtEfectivo.requestFocus();
            return;
        }
        
        // 4. Comparar con el total
        if (efectivo < totalPagar) {
            double faltante = totalPagar - efectivo;
            JOptionPane.showMessageDialog(null, 
                "Fondos insuficientes\nFaltan: $" + String.format("%,.2f", faltante), 
                "Error", JOptionPane.ERROR_MESSAGE);
            //lblCambio.setText("$0.00");
            return;
        }
        
        // 5. Calcular y mostrar el cambio
        double cambio = efectivo - totalPagar;
        lblCambio.setText(String.format("$%,.2f", cambio));
        this.insertarVentasDesdeTabla();
        this.actualizarStockDesdeTabla();
        this.cargarProductos();
        
        
        
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(null, 
            "Ingrese valores numéricos válidos\nEjemplo: 1500.50", 
            "Error de formato", JOptionPane.ERROR_MESSAGE);
        //lblCambios.setText("$0.00");
        txtEfectivo.requestFocus();
    }
    }//GEN-LAST:event_btnCalcularCambioActionPerformed

    private void btnPagoTarjetaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPagoTarjetaActionPerformed
    String titular = txtTitular.getText().trim();
    String numeroTarjeta = txtTarjeta.getText().trim().replaceAll(" ", "");
    String mesStr = txtMes.getText().trim();
    String anioStr = txtAnio.getText().trim();
    String cvv = txtCVV.getText().trim();

    // Validaciones básicas
    if (titular.isEmpty() || numeroTarjeta.isEmpty() || mesStr.isEmpty() || anioStr.isEmpty() || cvv.isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos de tarjeta.", "Campos incompletos", javax.swing.JOptionPane.WARNING_MESSAGE);
        return;
    }

    if (!numeroTarjeta.matches("\\d{16}")) {
        javax.swing.JOptionPane.showMessageDialog(this, "El número de tarjeta debe tener exactamente 16 dígitos.", "Número inválido", javax.swing.JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Detectar tipo de tarjeta
    String tipoTarjeta = "Desconocida";
    if (numeroTarjeta.startsWith("4")) {
        tipoTarjeta = "Visa";
    } else if (numeroTarjeta.matches("^5[1-5].*")) {
        tipoTarjeta = "MasterCard";
    } else if (numeroTarjeta.startsWith("34") || numeroTarjeta.startsWith("37")) {
        tipoTarjeta = "American Express";
    } else if (numeroTarjeta.startsWith("6")) {
        tipoTarjeta = "Discover";
    }

    txtCC.setText(tipoTarjeta);

    // Validar CVV
    if (!cvv.matches("\\d{3,4}")) {
        javax.swing.JOptionPane.showMessageDialog(this, "El CVV debe tener 3 o 4 dígitos.", "CVV inválido", javax.swing.JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Validar fecha
    int mes, anio;
    try {
        mes = Integer.parseInt(mesStr);
        anio = Integer.parseInt(anioStr);
        if (mes < 1 || mes > 12) {
            throw new NumberFormatException("Mes fuera de rango");
        }

        java.util.Calendar cal = java.util.Calendar.getInstance();
        int currentYear = cal.get(java.util.Calendar.YEAR);
        int currentMonth = cal.get(java.util.Calendar.MONTH) + 1;

        if (anio < currentYear || (anio == currentYear && mes < currentMonth)) {
            javax.swing.JOptionPane.showMessageDialog(this, "La tarjeta ya expiró.", "Fecha inválida", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
    } catch (NumberFormatException e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Ingrese mes y año válidos.", "Fecha inválida", javax.swing.JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Si todo está correcto
    javax.swing.JOptionPane.showMessageDialog(this, "✅ Pago con tarjeta exitoso.\nTipo: " + tipoTarjeta, "Aprobado", javax.swing.JOptionPane.INFORMATION_MESSAGE);

    
    
    this.insertarVentasDesdeTabla();
    this.actualizarStockDesdeTabla();
    this.cargarProductos();
    }//GEN-LAST:event_btnPagoTarjetaActionPerformed

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
        // TODO add your handling code here:
        DefaultTableModel modelo = (DefaultTableModel) tblProductos.getModel();
        modelo.setRowCount(0);
        txtSubtotal.setText("");
        txttotalPago.setText("");
        
        txtTitular.setText("");
        txtTarjeta.setText("");
        txtMes.setText("");
        txtAnio.setText("");
        txtCVV.setText("");
        txtCC.setText("");
    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void btnEliminarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarProductoActionPerformed
        // TODO add your handling code here:
        this.restarCantidadDelRegistro();
    }//GEN-LAST:event_btnEliminarProductoActionPerformed

    private void btnRegistrarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarVentaActionPerformed
    // Validar que haya productos en la tabla
    if (tblProductos.getRowCount() == 0) {
        JOptionPane.showMessageDialog(this, "No hay productos en la venta para generar el PDF.");
        return;
    }

    // Obtener el total desde el label txttotalPago
    String total = txttotalPago.getText().replace("$", "").replace(",", "").trim();

    // Determinar método de pago seleccionado
    String metodoPago;
    if (rdbEfectivo.isSelected()) {
        metodoPago = "Efectivo";
    } else if (rdbTarjeta.isSelected()) {
        metodoPago = "Tarjeta de crédito (" + txtCC.getText().trim() + ")";
    } else {
        metodoPago = "Método no especificado";
    }

    // Llamar al método mejorado de GenerarPDF
    try {
        PDF.GenerarPDF.generarFactura(tblProductos, total, metodoPago);

        JOptionPane.showMessageDialog(this, "✅ Factura generada con éxito.");

        // (Opcional) Limpiar la tabla y campos después de generar la factura
        DefaultTableModel modelo = (DefaultTableModel) tblProductos.getModel();
        modelo.setRowCount(0);
        txtSubtotal.setText("");
        txttotalPago.setText("");
        txtEfectivo.setText("");
        lblCambio.setText("");
        txtTitular.setText("");
        txtTarjeta.setText("");
        txtMes.setText("");
        txtAnio.setText("");
        txtCVV.setText("");
        txtCC.setText("");

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "❌ Error al generar factura: " + e.getMessage());
    }
    }//GEN-LAST:event_btnRegistrarVentaActionPerformed

    private void btncerrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncerrActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btncerrActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Pago.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Pago.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Pago.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Pago.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Pago().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelEfectivo;
    private javax.swing.JPanel PanelMetodosPago;
    private javax.swing.JPanel PanelProductos;
    private javax.swing.JPanel PanelTarjeta;
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCalcularCambio;
    private javax.swing.JButton btnEliminarProducto;
    private javax.swing.JButton btnPagoTarjeta;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JButton btnRegistrarVenta;
    private javax.swing.JButton btncerr;
    private javax.swing.ButtonGroup grupoMetodoPago;
    private javax.swing.JLabel lblAnio;
    private javax.swing.JLabel lblCC;
    private javax.swing.JLabel lblCVV;
    private javax.swing.JLabel lblCambio;
    private javax.swing.JLabel lblCambios;
    private javax.swing.JLabel lblCantidad;
    private javax.swing.JLabel lblEfectivoRecibido;
    private javax.swing.JLabel lblEliminarProducto;
    private javax.swing.JLabel lblExpiracion;
    private javax.swing.JLabel lblImagenFondo;
    private javax.swing.JLabel lblMes;
    private javax.swing.JLabel lblMetodoPagp;
    private javax.swing.JLabel lblNuevaVenta;
    private javax.swing.JLabel lblProducto;
    private javax.swing.JLabel lblRegistrarVenta;
    private javax.swing.JLabel lblSubTotal;
    private javax.swing.JLabel lblTablaBusquedaProductos;
    private javax.swing.JLabel lblTablaVerProductos;
    private javax.swing.JLabel lblTarjerta;
    private javax.swing.JLabel lblTitular;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTotalPago;
    private javax.swing.JPanel panelBusqueda;
    private javax.swing.JRadioButton rdbEfectivo;
    private javax.swing.JRadioButton rdbTarjeta;
    private javax.swing.JScrollPane scrollBusqueda;
    private javax.swing.JScrollPane scrollProductos;
    private javax.swing.JTable tblBusqueda;
    private javax.swing.JTable tblProductos;
    private javax.swing.JTextField txtAnio;
    private javax.swing.JLabel txtCC;
    private javax.swing.JTextField txtCVV;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtEfectivo;
    private javax.swing.JTextField txtMes;
    private javax.swing.JTextField txtProducto;
    private javax.swing.JLabel txtSubtotal;
    private javax.swing.JTextField txtTarjeta;
    private javax.swing.JTextField txtTitular;
    private javax.swing.JLabel txttotalPago;
    // End of variables declaration//GEN-END:variables
private void ajustarImagenDeFondo(JLabel label, String ruta) {
    java.net.URL url = getClass().getResource(ruta);
    if (url != null) {
        ImageIcon originalIcon = new ImageIcon(url);
        Image imagenEscalada = originalIcon.getImage().getScaledInstance(
            label.getWidth(),
            label.getHeight(),
            Image.SCALE_SMOOTH
        );
        label.setIcon(new ImageIcon(imagenEscalada));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
    } else {
        System.err.println("⚠ Imagen no encontrada en: " + ruta);
    }
}

/*class FondoPago extends JPanel {
    private Image imagen;

    @Override
    public void paint(Graphics g) {
        java.net.URL url = getClass().getResource("/imagen/FPago.png");
        if (url != null) {
            imagen = new ImageIcon(url).getImage();
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
        }
        setOpaque(false);
        super.paint(g);
    }
}*/
}
