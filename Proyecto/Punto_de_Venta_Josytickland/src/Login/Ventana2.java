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
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicLabelUI;
import javax.swing.plaf.basic.BasicMenuBarUI;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.table.*;
/**
 *
 * @author felix
 */
public class Ventana2 extends javax.swing.JFrame {
    ConexionMySQL con;
      Map<String, Object> datos;
       int activo;
       
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Ventana2.class.getName());

    /**
     * Creates new form Ventana2
     */
    public Ventana2() {
        initComponents();
      this.setLocationRelativeTo(null);
        scrTabla.setVisible(false);
        ConexionMySQL con=new ConexionMySQL();
        ConexionMySQL.configurar("root", "Olivera1234", "localhost", "3306", "bd");
        ConexionMySQL.conectar();
        Agrexist.setVisible(false);
        Agrnue.setVisible(false);
        Elimnr.setVisible(false);
       
        
        ///activo=0;
        datos = new HashMap<>();
        
        txtBusc.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {  
                if(activo==1){
                  buscarProductoPorNombre();  
                }else{
                    buscarUsuarios();
                }
                
                
            }

            public void removeUpdate(DocumentEvent e) {
                if(activo==1){
                  buscarProductoPorNombre();  
                }else{
                    buscarUsuarios();
                }
            }

            public void changedUpdate(DocumentEvent e) {

            }
            
           
    public void buscarProductoPorNombre() {
        String texto = txtBusc.getText().trim();
    if (texto.isEmpty()) {
        cargarProductos();
        return;
    }

    DefaultTableModel modelo = new DefaultTableModel();
    modelo.addColumn("ID");
    modelo.addColumn("Nombre del producto");
    modelo.addColumn("Estado");
    modelo.addColumn("Precio");
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
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("estado"),
                rs.getDouble("precio"),
                rs.getInt("stock")
            };
            modelo.addRow(fila);
            encontrado = true;
        }

        TablaDatos.setModel(modelo);

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
    
    public void buscarUsuarios() {
        String texto = txtBusc.getText().trim();
    if (texto.isEmpty()) {
        cargarUsuarios();
        return;
    }

    DefaultTableModel modelo = new DefaultTableModel();
    modelo.addColumn("ID");
    modelo.addColumn("Correo");
    modelo.addColumn("Contraseña");
    modelo.addColumn("Rol");

    // Usamos LOWER para ignorar mayúsculas/minúsculas
    String sql = "SELECT * FROM usuario WHERE LOWER(correo) LIKE ?";

    try {
        Connection con = conectar();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, "%" + texto.toLowerCase() + "%");

        ResultSet rs = ps.executeQuery();

        boolean encontrado = false;

        while (rs.next()) {
            Object[] fila = new Object[]{
                rs.getInt("id"),
                rs.getString("correo"),
                rs.getString("contraseña"),
                rs.getInt("id_Rol")
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
        JOptionPane.showMessageDialog(null, "Error al buscar el correo: " + e.getMessage());
    }
     }
    
    
        });

         personalizarComponentes();
         personalizarBuscador();
         personalizarMenu();
         personalizarLabelRounded(Fondorosa);
         
    }
    
    
    
    
    
    
    
    public static void personalizarLabelRounded(JLabel label) {
    label.setOpaque(false);
    label.setBackground(new Color(255, 182, 193));
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
    personalizarBoton(botonAgregar);
    
    // Personalización del botón Eliminar
    personalizarBoton(botonEliminar);
    
    personalizarBoton(btnActualizar);
    personalizarBoton(btnAgregarus);
    personalizarBoton(btnEditar);
    
    
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
    lblBuscar.setForeground(new Color(70, 70, 70));
    
    txtBusc.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
    txtBusc.setBackground(new Color(250, 250, 250));
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
    
    private void personalizarMenu() {
        
        // Configurar la UI personalizada
    Menubarr.setUI(new BasicMenuBarUI() {
        @Override
        public void paint(Graphics g, JComponent c) {
            g.setColor(new Color(173, 216, 230)); // Azul cielo
            g.fillRect(0, 0, c.getWidth(), c.getHeight());
        }
    });
    // Personalización de la JMenuBar
    Menubarr.setBackground(new Color(173, 216, 230)); // Azul cielo pastel
    Menubarr.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
    
    // Fuente para todos los elementos del menú
    Font menuFont = new Font("Comic Sans MS", Font.BOLD, 24);
    
    // Personalización del JMenu principal
    Menu.setFont(menuFont);
    Menu.setForeground(Color.BLACK); // Texto negro
    Menu.setBackground(new Color(144, 238, 144)); // Verde claro pastel (#90EE90)
    Menu.setOpaque(true); // Importante para que se vea el color de fondo
    Menu.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    
    // Personalización de los JMenuItem
    personalizarMenuItem(Menuuser, new Color(221, 160, 221)); // Lila pastel
    personalizarMenuItem(Menujugue, new Color(255, 255, 153)); // Amarillo suave
    
   
    // Efecto hover para el menú (verde más intenso al pasar el mouse)
    Menu.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            Menu.setBackground(new Color(102, 205, 170)); // Verde menta
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            Menu.setBackground(new Color(144, 238, 144)); // Vuelve al verde claro
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
    private void cargarProductos() {
    DefaultTableModel modelo = new DefaultTableModel();
    modelo.addColumn("ID");
    modelo.addColumn("Nombre");
    modelo.addColumn("Estado");
    modelo.addColumn("Precio");
    modelo.addColumn("Stock");

    ResultSet rs = ConexionMySQL.listarTodo("producto");

    try {
        while (rs != null && rs.next()) {
            modelo.addRow(new Object[]{
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("estado"),
                rs.getString("precio"),
                rs.getInt("stock")
                
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
        txtBusc = new javax.swing.JTextField();
        lblBuscar = new javax.swing.JLabel();
        botonAgregar = new javax.swing.JButton();
        Elimnr = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        txtElm = new javax.swing.JTextField();
        Agrnue = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        t1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        t2 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        t3 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        lblM = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        Agrexist = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtAgre = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        botonEliminar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnAgregarus = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        Fondorosa = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        Menubarr = new javax.swing.JMenuBar();
        Menu = new javax.swing.JMenu();
        Menuuser = new javax.swing.JMenuItem();
        Menujugue = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1536, 1024));
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

        jPanel1.add(scrTabla, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 370, 870, 233));
        jPanel1.add(txtBusc, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 290, 260, 40));

        lblBuscar.setText("Buscador:");
        jPanel1.add(lblBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 290, 130, 30));

        botonAgregar.setText("Agregar Producto");
        botonAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAgregarActionPerformed(evt);
            }
        });
        jPanel1.add(botonAgregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 630, -1, -1));

        Elimnr.setBackground(new java.awt.Color(204, 153, 255));

        jLabel6.setText("Cantidad:");

        jButton7.setText("Aceptar");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ElimnrLayout = new javax.swing.GroupLayout(Elimnr);
        Elimnr.setLayout(ElimnrLayout);
        ElimnrLayout.setHorizontalGroup(
            ElimnrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ElimnrLayout.createSequentialGroup()
                .addGroup(ElimnrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ElimnrLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
                        .addComponent(txtElm, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(ElimnrLayout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(jButton7)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ElimnrLayout.setVerticalGroup(
            ElimnrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ElimnrLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(ElimnrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtElm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addComponent(jButton7)
                .addGap(26, 26, 26))
        );

        jPanel1.add(Elimnr, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 680, -1, -1));

        Agrnue.setBackground(new java.awt.Color(153, 153, 255));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Nombre del producto:");

        t1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t1ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Precio ($  0.00)  :");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Cantidad:");

        jButton3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton3.setText("Agregar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton4.setText("Cerrar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout AgrnueLayout = new javax.swing.GroupLayout(Agrnue);
        Agrnue.setLayout(AgrnueLayout);
        AgrnueLayout.setHorizontalGroup(
            AgrnueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AgrnueLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(AgrnueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addGroup(AgrnueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(t1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(AgrnueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(t3, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(t2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(27, 27, 27))
            .addGroup(AgrnueLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AgrnueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblM, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(AgrnueLayout.createSequentialGroup()
                        .addGap(92, 92, 92)
                        .addComponent(jButton3)
                        .addGap(65, 65, 65)
                        .addComponent(jButton4)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        AgrnueLayout.setVerticalGroup(
            AgrnueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AgrnueLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AgrnueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(t1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(AgrnueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(t2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(AgrnueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(t3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43)
                .addGroup(AgrnueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addGap(18, 18, 18)
                .addComponent(lblM, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jPanel1.add(Agrnue, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 670, -1, -1));

        Agrexist.setBackground(new java.awt.Color(204, 153, 255));

        jLabel2.setText("Cantidad:");

        jButton2.setText("Aceptar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout AgrexistLayout = new javax.swing.GroupLayout(Agrexist);
        Agrexist.setLayout(AgrexistLayout);
        AgrexistLayout.setHorizontalGroup(
            AgrexistLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AgrexistLayout.createSequentialGroup()
                .addGroup(AgrexistLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(AgrexistLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
                        .addComponent(txtAgre, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(AgrexistLayout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(jButton2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        AgrexistLayout.setVerticalGroup(
            AgrexistLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AgrexistLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(AgrexistLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtAgre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(26, 26, 26))
        );

        jPanel1.add(Agrexist, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 680, -1, -1));

        botonEliminar.setText("Eliminar Producto");
        botonEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarActionPerformed(evt);
            }
        });
        jPanel1.add(botonEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 630, -1, -1));

        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });
        jPanel1.add(btnActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 310, -1, -1));

        btnAgregarus.setText("Agregar usuario");
        btnAgregarus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarusActionPerformed(evt);
            }
        });
        jPanel1.add(btnAgregarus, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 710, -1, -1));

        btnEditar.setText("Editar usuario");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });
        jPanel1.add(btnEditar, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 630, -1, -1));

        Fondorosa.setBackground(new java.awt.Color(255, 204, 204));
        Fondorosa.setOpaque(true);
        jPanel1.add(Fondorosa, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 270, 1020, 680));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/foxy1.png"))); // NOI18N
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 210, 290));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/FondoVentana4.png"))); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jScrollPane1.setViewportView(jPanel1);

        Menu.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Menu.setText("Menu");
        Menu.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        Menuuser.setText("Usuarios");
        Menuuser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuuserActionPerformed(evt);
            }
        });
        Menu.add(Menuuser);

        Menujugue.setText("Juguetes");
        Menujugue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenujugueActionPerformed(evt);
            }
        });
        Menu.add(Menujugue);

        Menubarr.add(Menu);

        setJMenuBar(Menubarr);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1530, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 988, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void MenuuserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuuserActionPerformed
        // TODO add your handling code here:
        txtBusc.setText("");
        activo=2;
        botonEliminar.setEnabled(false);
        botonAgregar.setEnabled(false);
        btnAgregarus.setEnabled(true);
        btnEditar.setEnabled(true);

        this.cargarUsuarios();
        scrTabla.setVisible(true);
        personalizarComponentes();
    }//GEN-LAST:event_MenuuserActionPerformed

    private void MenujugueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenujugueActionPerformed
        // TODO add your handling code here:
        txtBusc.setText("");
        activo=1;
        botonEliminar.setEnabled(true);
        botonAgregar.setEnabled(true);
        btnAgregarus.setEnabled(false);
        btnEditar.setEnabled(false);
        this.cargarProductos();
        scrTabla.setVisible(true);
        personalizarComponentes() ;
    }//GEN-LAST:event_MenujugueActionPerformed

    private void botonAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAgregarActionPerformed
        // TODO add your handling code here:
        int fila = TablaDatos.getSelectedRow();

        if (fila == -1) {
            if(Agrnue.isVisible()==true){
                Agrnue.setVisible(false);
            }else{

                Agrnue.setLocation(200, 250);
                Agrnue.setVisible(true);

            }
        }else{

            if(Agrexist.isVisible()==true){
                Agrexist.setVisible(false);
            }else{
                Agrexist.setLocation(250, 250);
                Agrexist.setVisible(true);
            }

        }

    }//GEN-LAST:event_botonAgregarActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        int fila = TablaDatos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Selecciona un producto primero.");
            return;
        }

        if (activo == 1) {
            try {
                int stock = (int) TablaDatos.getValueAt(fila, 4); // columna stock
                String cantidadStr = txtElm.getText().trim();

                if (cantidadStr.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Ingresa una cantidad válida.");
                    return;
                }

                int cantidadRestar = Integer.parseInt(cantidadStr);

                if (cantidadRestar <= 0) {
                    JOptionPane.showMessageDialog(null, "La cantidad debe ser mayor que cero.");
                    return;
                }

                int total = stock - cantidadRestar;

                if (total < 0) {
                    JOptionPane.showMessageDialog(null, "Cantidad inválida. No hay suficiente stock.");
                    return;
                }

                int idProducto = (int) TablaDatos.getValueAt(fila, 0);

                Connection con = conectar();
                String sql = "UPDATE producto SET stock = ? WHERE id = ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, total);
                ps.setInt(2, idProducto);
                ps.executeUpdate();
                ps.close();
                con.close();

                JOptionPane.showMessageDialog(null, "Stock actualizado.");
                txtElm.setText("");

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Ingresa un número válido.");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al actualizar el stock: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void t1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        if (t1.getText().equals("") || t2.getText().equals("") || t3.getText().equals("")) {
            lblM.setText("Rellene todos los datos primero.");
            lblM.setForeground(Color.red);
        } else {
            try {
                double precio = Double.parseDouble(t2.getText());
                int cant = Integer.parseInt(t3.getText());

                if (cant > 0) {
                    datos.put("nombre", t1.getText());
                    datos.put("precio", precio);
                    datos.put("estado", "Disponible");
                    datos.put("stock", cant);

                    ConexionMySQL.insertar("producto", datos);

                    lblM.setText("Se agregaron los productos correctamente.");
                    lblM.setForeground(Color.black);
                    t1.setText("");
                    t2.setText("");
                    t3.setText("");
                } else {
                    lblM.setText("Es necesario que la cantidad sea mayor a 0.");
                    lblM.setForeground(Color.red);
                }

            } catch (NumberFormatException e) {
                lblM.setText("Error: ingrese un número válido para el precio y cantidad.");
                lblM.setForeground(Color.red);
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        if(activo==1){
            int fila = TablaDatos.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(null, "Selecciona un producto primero.");
                return;
            }

            if (activo == 1) {
                try {
                    int stock = (int) TablaDatos.getValueAt(fila, 4); // columna stock
                    String cantidadStr = txtAgre.getText().trim();

                    if (cantidadStr.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Ingresa una cantidad válida.");
                        return;
                    }

                    int cantidadSumar = Integer.parseInt(cantidadStr);

                    if (cantidadSumar <= 0) {
                        JOptionPane.showMessageDialog(null, "La cantidad debe ser mayor que cero.");
                        return;
                    }

                    int total = stock + cantidadSumar;

                    int idProducto = (int) TablaDatos.getValueAt(fila, 0);

                    Connection con = conectar();
                    String sql = "UPDATE producto SET stock = ? WHERE id = ?";
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setInt(1, total);
                    ps.setInt(2, idProducto);
                    ps.executeUpdate();
                    ps.close();
                    con.close();

                    JOptionPane.showMessageDialog(null, "Stock actualizado.");
                    txtAgre.setText("");

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Ingresa un número válido.");
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error al actualizar el stock: " + e.getMessage());
                }
            }

        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void botonEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEliminarActionPerformed
        // TODO add your handling code here:
        if(Elimnr.isVisible()==true){
            Elimnr.setVisible(false);
        }else{

            Elimnr.setVisible(true);
        }

    }//GEN-LAST:event_botonEliminarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        // TODO add your handling code here:
        if(activo==1){
            Agrnue.setVisible(false);
            Agrexist.setVisible(false);
            Elimnr.setVisible(false);
            this.cargarProductos();
        }else{
            this.cargarUsuarios();
        }
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
        java.awt.EventQueue.invokeLater(() -> new Ventana2().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Agrexist;
    private javax.swing.JPanel Agrnue;
    private javax.swing.JPanel Elimnr;
    private javax.swing.JLabel Fondorosa;
    private javax.swing.JMenu Menu;
    private javax.swing.JMenuBar Menubarr;
    private javax.swing.JMenuItem Menujugue;
    private javax.swing.JMenuItem Menuuser;
    private javax.swing.JTable TablaDatos;
    private javax.swing.JButton botonAgregar;
    private javax.swing.JButton botonEliminar;
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnAgregarus;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton7;
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
    private javax.swing.JLabel lblM;
    private javax.swing.JScrollPane scrTabla;
    private javax.swing.JTextField t1;
    private javax.swing.JTextField t2;
    private javax.swing.JTextField t3;
    private javax.swing.JTextField txtAgre;
    private javax.swing.JTextField txtBusc;
    private javax.swing.JTextField txtElm;
    // End of variables declaration//GEN-END:variables
}
