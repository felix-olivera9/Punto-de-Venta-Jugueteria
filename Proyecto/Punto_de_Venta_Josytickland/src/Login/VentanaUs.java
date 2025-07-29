/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Login;

import Conexion.ConexionMySQL;
import static Conexion.ConexionMySQL.conectar;
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
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicLabelUI;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.table.*;
import javax.sound.sampled.*;

/**
 *
 * @author felix
 */
public class VentanaUs extends javax.swing.JFrame {
    ConexionMySQL con;
      Map<String, Object> datos;
       int activo;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VentanaUs.class.getName());

    /**
     * Creates new form VentanaUs
     */
    public VentanaUs() {
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
                
                    buscarUsuarios();    
            }

            public void removeUpdate(DocumentEvent e) {
               
                    buscarUsuarios();
                
            }

            public void changedUpdate(DocumentEvent e) {

            }
            
           
    
    
    public void buscarUsuarios() {
    String texto = txtBusc.getText().trim();
    if (texto.isEmpty()) {
        cargarUsuarios();
        return;
    }

    DefaultTableModel modelo = new DefaultTableModel();
    modelo.addColumn("Nombre");
    modelo.addColumn("Correo");
    modelo.addColumn("Rol");

    // Consulta con INNER JOIN para obtener el nombre del rol
    String sql = "SELECT nombre, correo, rol AS rol_nombre "
               + "FROM usuario "
               + "INNER JOIN rol ON usuario.id_Rol = rol.id_Rol "
               + "WHERE LOWER(usuario.correo) LIKE ? OR LOWER(usuario.nombre) LIKE ? OR LOWER(rol.rol) LIKE ?";

    try {
        Connection con = conectar();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, "%" + texto.toLowerCase() + "%");
        ps.setString(2, "%" + texto.toLowerCase() + "%");
        ps.setString(3, "%" + texto.toLowerCase() + "%");
        ResultSet rs = ps.executeQuery();

        boolean encontrado = false;

        while (rs.next()) {
            Object[] fila = new Object[]{
                rs.getString("nombre"),
                rs.getString("correo"),
                rs.getString("rol_nombre")  // ← Aquí usas el alias del nombre del rol
            };
            modelo.addRow(fila);
            encontrado = true;
        }

        TablaDatos.setModel(modelo);

        if (!encontrado) {
            JOptionPane.showMessageDialog(null, "No se encontró ningún usuario con ese correo.");
        }

        rs.close();
        ps.close();
        con.close();

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al buscar el usuario: " + e.getMessage());
    }
}
    
    
        });

         personalizarComponentes();
         personalizarBuscador();

         personalizarLabelRounded(Fondorosa);
         
         this.cargarUsuarios();
         scrTabla.setVisible(true);
         
    }
    
    
    
    
    
    
    
    public static void personalizarLabelRounded(JLabel label) {
    label.setOpaque(false);
    //label.setBackground(new Color(255, 182, 193));
    label.setBackground(Color.darkGray);
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
    
    // Personalización del botón Agregar
    
    
    // Personalización del botón Eliminar

    
    personalizarBoton(btnActualizar);
    personalizarBoton(btnAgregarus);
    personalizarBoton(btnEditar);
    personalizarBoton(btnAtras);
    
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
    
    

private void personalizarMenuItem(JMenuItem menuItem, Color colorFondo) {
    // Fuente
    menuItem.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
    
    // Colores
    menuItem.setForeground(new Color(70, 70, 70)); // Texto gris oscuro
    menuItem.setBackground(colorFondo);
    menuItem.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 10));
   
    
    // Efecto hover
    menuItem.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            menuItem.setBackground(menuItem.getBackground().brighter());
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            menuItem.setBackground(colorFondo);
        }
    });
    
    // Para que muestre el color de fondo
    menuItem.setOpaque(true);
}
    private void cargarUsuarios() {
   

    Connection con = conectar(); // Usa tu método de conexión
    String sql = "SELECT nombre, correo, rol AS rol_nombre "
           + "FROM usuario "
           + "INNER JOIN rol ON usuario.id_Rol = rol.id_Rol";

    try {
         DefaultTableModel modelo = new DefaultTableModel();
    modelo.addColumn("Nombre");
    modelo.addColumn("Correo");
    modelo.addColumn("Rol");
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs != null && rs.next()) {
            modelo.addRow(new Object[]{
                rs.getString("nombre"),
                rs.getString("correo"),
                rs.getString("rol_nombre") // <- Nombre del rol
            });
        }
        TablaDatos.setModel(modelo);
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
        txtBusc = new javax.swing.JTextField();
        lblBuscar = new javax.swing.JLabel();
        btnActualizar = new javax.swing.JButton();
        btnAgregarus = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnAtras = new javax.swing.JButton();
        lblSonido = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        Fondorosa = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1536, 1024));

        jScrollPane1.setPreferredSize(new java.awt.Dimension(1536, 1024));

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

        jPanel1.add(scrTabla, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 320, 870, 233));
        jPanel1.add(txtBusc, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 270, 260, 40));

        lblBuscar.setText("Buscador:");
        jPanel1.add(lblBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 270, 130, 30));

        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });
        jPanel1.add(btnActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 260, -1, -1));

        btnAgregarus.setText("Agregar usuario");
        btnAgregarus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarusActionPerformed(evt);
            }
        });
        jPanel1.add(btnAgregarus, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 580, -1, -1));

        btnEditar.setText("Editar usuario");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });
        jPanel1.add(btnEditar, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 580, -1, -1));

        btnAtras.setText("Atras");
        btnAtras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtrasActionPerformed(evt);
            }
        });
        jPanel1.add(btnAtras, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, -1, -1));

        lblSonido.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSonidoMouseClicked(evt);
            }
        });
        jPanel1.add(lblSonido, new org.netbeans.lib.awtextra.AbsoluteConstraints(1330, 610, 50, 40));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/PelucheFrdy.png"))); // NOI18N
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(1290, 510, 200, 280));

        Fondorosa.setBackground(new java.awt.Color(255, 204, 204));
        Fondorosa.setOpaque(true);
        jPanel1.add(Fondorosa, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 220, 900, 410));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/foxy1.png"))); // NOI18N
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 0, 210, -1));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Fredy1.png"))); // NOI18N
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 0, 210, 220));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Puppet1.png"))); // NOI18N
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 530, 210, 250));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Chica1.png"))); // NOI18N
        jLabel4.setText("jLabel4");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, 210, 280));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Bunny1.png"))); // NOI18N
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1290, 20, 210, 310));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/FondoVentana5.png"))); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-4, 0, 1540, -1));

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1535, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1034, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        // TODO add your handling code here:
        
            this.cargarUsuarios();
   
        txtBusc.setText("");
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnAgregarusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarusActionPerformed
        // TODO add your handling code here:
        Registro as =new Registro();
        as.setVisible(true);
    }//GEN-LAST:event_btnAgregarusActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        // TODO add your handling code here:
        int id = getIdSeleccionado();
        String correo = getCorreoSeleccionado();
        String contraseña = getContraseñaSeleccionada();
        String nombre= getNombreSeleccionado();

        if (id == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un usuario primero.");
            return;
        }

        // Abrir la ventana de edición y pasarle los datos
        EdiitarUs editar = new EdiitarUs ();
        editar.cargarDatos(id, correo, contraseña,nombre);
        editar.setVisible(true);
    }//GEN-LAST:event_btnEditarActionPerformed

    private void lblSonidoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSonidoMouseClicked
        // TODO add your handling code here:
         try {
        // Carga el archivo de sonido desde la carpeta de recursos
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(
            getClass().getResource("/sounds/sonido.wav") // Ajusta la ruta según tu estructura
        );
        
        // Obtiene un Clip (reproductor de sonido)
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn); // Abre el flujo de audio
        
        // Reproduce el sonido
        clip.start();
        
    } catch (UnsupportedAudioFileException | IOException e) {
        System.err.println("Formato de audio no soportado o archivo no encontrado: " + e.getMessage());
    } catch (LineUnavailableException e) {
        System.err.println("Línea de audio no disponible: " + e.getMessage());
    }
    }//GEN-LAST:event_lblSonidoMouseClicked

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
        java.awt.EventQueue.invokeLater(() -> new VentanaUs().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Fondorosa;
    private javax.swing.JTable TablaDatos;
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnAgregarus;
    private javax.swing.JButton btnAtras;
    private javax.swing.JButton btnEditar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBuscar;
    private javax.swing.JLabel lblSonido;
    private javax.swing.JScrollPane scrTabla;
    private javax.swing.JTextField txtBusc;
    // End of variables declaration//GEN-END:variables
}
