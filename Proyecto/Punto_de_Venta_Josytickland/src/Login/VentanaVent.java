/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Login;
import Conexion.ConexionMySQL;
import static Conexion.ConexionMySQL.conectar;
import Usuario.Pago;
import java.awt.Color;
import java.awt.Font;
import java.sql.*;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicLabelUI;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.table.*;
/**
 *
 * @author felix
 */
public class VentanaVent extends javax.swing.JFrame {
      Map<String, Object> datos;
       int activo;
       
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VentanaVent.class.getName());

    /**
     * Creates new form Ventana2
     */
    public VentanaVent() {
        initComponents();
      this.setLocationRelativeTo(null);
        scrTabla.setVisible(false);
        ConexionMySQL con=new ConexionMySQL();
        ConexionMySQL.configurar("root", "Olivera1234", "localhost", "3306", "bd");
        ConexionMySQL.conectar();
        
       
        
        ///activo=0;
        datos = new HashMap<>();
        
        txtBusc.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {  
                
                  buscarProductoPorNombre();  
                
                
                
            }

            public void removeUpdate(DocumentEvent e) {
               
                  buscarProductoPorNombre();  
            }

            public void changedUpdate(DocumentEvent e) {

            }
            
           
    public void buscarProductoPorNombre() {
        String texto = txtBusc.getText().trim();
    if (texto.isEmpty()) {
        cargarVentas();
        return;
    }

    DefaultTableModel modelo = new DefaultTableModel();
   modelo.addColumn("ID");
    modelo.addColumn("Producto");
    modelo.addColumn("Cantidad");
    modelo.addColumn("Precio unitario");
    modelo.addColumn("SubTotal");

    // Usamos LOWER para ignorar mayúsculas/minúsculas
    String sql = "SELECT * FROM venta WHERE LOWER(nombre_producto) LIKE ?";

    try {
        Connection con = conectar();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, "%" + texto.toLowerCase() + "%");

        ResultSet rs = ps.executeQuery();

        boolean encontrado = false;

        while (rs.next()) {
            Object[] fila = new Object[]{
               rs.getInt("id"),
                rs.getString("nombre_producto"),
                rs.getString("cantidad"),
                rs.getString("precio_unitario"),
                rs.getString("subtotal")
            };
            modelo.addRow(fila);
            encontrado = true;
        }

        TablaDatos.setModel(modelo);

        if (!encontrado) {
            JOptionPane.showMessageDialog(null, "No se encontró ningún producto vendido con ese nombre.");
        }

        rs.close();
        ps.close();
        con.close();

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al buscar venta: " + e.getMessage());
    }
     }
    
    
    
    
        });

         personalizarComponentes();
         personalizarBuscador();
       
         personalizarLabelRounded(Fondorosa);
         this.cargarVentas();
         scrTabla.setVisible(true);
    }
    
    
    
    
    
    
    
    public static void personalizarLabelRounded(JLabel label) {
    label.setOpaque(false);
    //label.setBackground(new Color(255, 182, 193));
    label.setBackground(new Color(15, 22, 40));
    label.setUI(new BasicLabelUI() {
        @Override
        public void paint(Graphics g, JComponent c) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Fondo redondeado
            g2.setColor(c.getBackground());
            g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 20, 20);
            
            // Borde
            g2.setColor(c.getBackground().darker());
            g2.drawRoundRect(0, 0, c.getWidth()-1, c.getHeight()-1, 20, 20);
            
            super.paint(g, c);
        }
    });
}
    
    private void personalizarComponentes() {
    // Personalización de la tabla TablaDatos
    TablaDatos.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
    TablaDatos.setRowHeight(30);
    TablaDatos.setSelectionBackground(new Color(153, 204, 255));
    TablaDatos.setGridColor(new Color(240, 240, 240));
    
    // Personalización del encabezado de la tabla
    JTableHeader header = TablaDatos.getTableHeader();
    header.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
    header.setBackground(new Color(255, 204, 255));
    header.setForeground(Color.DARK_GRAY);
    
    // Renderer para las celdas de la tabla (filas alternas)
    DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected) {
                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(230, 245, 255));
            }
            return c;
        }
    };
    
    // Aplicar el renderer a todas las columnas
    for (int i = 0; i < TablaDatos.getColumnCount(); i++) {
        TablaDatos.getColumnModel().getColumn(i).setCellRenderer(renderer);
    }
    
    // Hacer que la tabla no sea editable
    TablaDatos.setDefaultEditor(Object.class, null);
 
    personalizarBoton(botonAgregar);
    personalizarBoton(btnAtras);
    personalizarBoton(btnActualizar);
   
    this.cargarVentas();
    scrTabla.setVisible(true);
    
    
}

// Método para personalizar los botones
private void personalizarBoton(JButton boton) {
    boton.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
    boton.setForeground(Color.WHITE);
    boton.setFocusPainted(false);
    boton.setContentAreaFilled(false);
    boton.setOpaque(false);

    // Crear borde redondeado azul pastel
    boton.setBorder(new Border() {
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(33, 150, 243)); // Azul pastel para el borde
            g2.drawRoundRect(x, y, width - 1, height - 1, 30, 30);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(4, 15, 4, 15);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    });

    boton.addPropertyChangeListener("model", evt -> {
        ButtonModel model = (ButtonModel) evt.getNewValue();
        if (model != null) {
            boton.setContentAreaFilled(false);
            boton.setOpaque(false);
        }
    });

    boton.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            boton.repaint();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            boton.repaint();
        }
    });

    boton.setUI(new BasicButtonUI() {
        @Override
        public void paint(Graphics g, JComponent c) {
            AbstractButton b = (AbstractButton) c;
            ButtonModel model = b.getModel();

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (model.isPressed()) {
                g2.setColor(new Color(25, 118, 210)); // Azul más fuerte al presionar
            } else if (model.isRollover()) {
                g2.setColor(new Color(100, 181, 246)); // Azul más claro al pasar el mouse
            } else {
                g2.setColor(new Color(33, 150, 243)); // Azul pastel base
            }

            g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 30, 30);
            g2.dispose();

            super.paint(g, c);
        }
    });

}
    
    
    private void personalizarBuscador() {
    lblBuscar.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
    //lblBuscar.setForeground(new Color(70, 70, 70));
    lblBuscar.setForeground(Color.white);
    txtBusc.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
    txtBusc.setBackground(new Color(250, 250, 250));
    txtBusc.setForeground(Color.white);
    txtBusc.setOpaque(false); // Importante para el fondo redondeado
    
    // Borde redondeado con mayor curvatura (radio de 15px)
    txtBusc.setBorder(new Border() {
        private final int radius = 25;
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(102, 204, 255));
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(x, y, width-1, height-1, radius, radius);
            g2.dispose();
        }
        
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(7, 12, 7, 12); // Margen interno
        }
        
        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    });
    
    // Fondo redondeado (requiere override de paintComponent)
    txtBusc.setUI(new BasicTextFieldUI() {
        @Override
        protected void paintBackground(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(txtBusc.getBackground());
            g2.fillRoundRect(0, 0, txtBusc.getWidth(), txtBusc.getHeight(), 15, 15);
            g2.dispose();
        }
    });
}
    
   
    

    private void cargarVentas() {
    DefaultTableModel modelo = new DefaultTableModel();
    modelo.addColumn("ID");
    modelo.addColumn("Producto");
    modelo.addColumn("Cantidad");
    modelo.addColumn("Precio unitario");
    modelo.addColumn("SubTotal");

    ResultSet rs = ConexionMySQL.listarTodo("venta");

    try {
        while (rs != null && rs.next()) {
            modelo.addRow(new Object[]{
                rs.getInt("id"),
                rs.getString("nombre_producto"),
                rs.getString("cantidad"),
                rs.getString("precio_unitario"),
                rs.getInt("subtotal")
                
            });
        }
        TablaDatos.setModel(modelo); // Asegúrate de que TablaDatos sea tu JTable
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al cargar usuarios: " + e.getMessage());
    }
}
    public int getIdSeleccionado() {
    String correo = getCorreoSeleccionado();
    int id = -1;

    try {
        Connection con = conectar();
        String sql = "SELECT id FROM usuario WHERE correo = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, correo);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            id = rs.getInt("id");
        }
        rs.close();
        ps.close();
        con.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al obtener ID: " + e.getMessage());
    }

    return id;
}
    
   public String getContraseñaSeleccionada() {
    String correo = getCorreoSeleccionado();
    String contraseña = "";

    try {
        Connection con = conectar();
        String sql = "SELECT contraseña FROM usuario WHERE correo = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, correo);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            contraseña = rs.getString("contraseña");
        }
        rs.close();
        ps.close();
        con.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al obtener la contraseña: " + e.getMessage());
    }

    return contraseña;
}


    public String getNombreSeleccionado() {
    int fila = TablaDatos.getSelectedRow();
    if (fila != -1) {
        return TablaDatos.getValueAt(fila, 0).toString(); // Nombre está en la columna 0
    }
    return "";
}
    public String getCorreoSeleccionado() {
    int fila = TablaDatos.getSelectedRow();
    if (fila != -1) {
        return TablaDatos.getValueAt(fila, 1).toString(); // Correo está en la columna 1
    }
    return "";
    }
    
    
    
    
    
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        scrTabla = new javax.swing.JScrollPane();
        TablaDatos = new javax.swing.JTable();
        btnAtras = new javax.swing.JButton();
        txtBusc = new javax.swing.JTextField();
        lblBuscar = new javax.swing.JLabel();
        botonAgregar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        Fondorosa = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(1536, 1024));

        jPanel1.setPreferredSize(new java.awt.Dimension(1536, 1024));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TablaDatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Correo", "Contraseña", "Rol"
            }
        ));
        scrTabla.setViewportView(TablaDatos);

        jPanel1.add(scrTabla, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 310, 960, 233));

        btnAtras.setText("Atras");
        btnAtras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtrasActionPerformed(evt);
            }
        });
        jPanel1.add(btnAtras, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, -1, -1));
        jPanel1.add(txtBusc, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 260, 260, 40));

        lblBuscar.setText("Buscador:");
        jPanel1.add(lblBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 260, 130, 30));

        botonAgregar.setText("Agregar venta");
        botonAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAgregarActionPerformed(evt);
            }
        });
        jPanel1.add(botonAgregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 560, -1, -1));

        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });
        jPanel1.add(btnActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 260, -1, -1));

        Fondorosa.setBackground(new java.awt.Color(255, 204, 204));
        Fondorosa.setOpaque(true);
        jPanel1.add(Fondorosa, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 250, 980, 360));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/FondoVenta2.png"))); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1540, 1020));

        jLabel7.setBackground(new java.awt.Color(0, 0, 0));
        jLabel7.setOpaque(true);
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(-2, -4, 1040, 1010));

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1552, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1018, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAgregarActionPerformed
        // TODO add your handling code here:
        Pago v=new Pago();
        v.activarcerrar();
        v.setVisible(true);
    }//GEN-LAST:event_botonAgregarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        // TODO add your handling code here:
        
       
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnAtrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtrasActionPerformed
        // TODO add your handling code here:
        VentanaInicio v=new VentanaInicio();
        v.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnAtrasActionPerformed

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new VentanaVent().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Fondorosa;
    private javax.swing.JTable TablaDatos;
    private javax.swing.JButton botonAgregar;
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnAtras;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBuscar;
    private javax.swing.JScrollPane scrTabla;
    private javax.swing.JTextField txtBusc;
    // End of variables declaration//GEN-END:variables
}
