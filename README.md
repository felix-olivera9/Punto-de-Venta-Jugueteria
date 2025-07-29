# 🎮 Sistema de Punto de Venta - Juguetería Joystickland

### 🧑‍💻 Número de equipo:
**Equipo 10**

### 👥 Integrantes:
- **Olivera Jimenez Felix Eliel** – Desarrollador principal y diseñador
- **Jose Jose Osvaldo** – Desarrollador secundaria y diseñador
---

### 🧾 ¿Qué hace el sistema?

Sistema de punto de venta para una juguetería, que permite gestionar productos (juguetes), registrar ventas, generar facturas digitales en PDF, y enviar comprobantes por correo electrónico. Cuenta con autenticación de usuarios mediante CAPTCHA para mayor seguridad.

---

### 🖥️ Tipo de sistema

**Aplicación de Escritorio (Desktop App)** desarrollada en **Java** utilizando **Swing**, ejecutada en **Apache NetBeans IDE 26**, con integración a base de datos **MySQL 8.0.42** mediante conexión JDBC. Se utiliza **MySQL Workbench Community** para la gestión de la base de datos.

---

### 📚 Librerías externas implementadas

- Usamos la librería del **Equipo 2** para enviar correos electrónicos con archivos adjuntos (PDF e imágenes):  
  🔗 [https://github.com/olmomomo/Libreria_correoElectronico](https://github.com/olmomomo/Libreria_correoElectronico)

---

### 🧩 Componente visual integrado

- Integramos el componente visual CAPTCHA del **Equipo 2** para validar el inicio de sesión de los usuarios:  
  🔗 [https://github.com/FanyBr07/ComponenteVisual](https://github.com/FanyBr07/ComponenteVisual)

---
## ⚙️ Funcionalidades Clave

### 🔐 Integración de CAPTCHA

Al iniciar sesión, el sistema muestra un CAPTCHA visual generado con el componente del **Equipo 2**  
🔗 [https://github.com/FanyBr07/ComponenteVisual](https://github.com/FanyBr07/ComponenteVisual)

El botón de inicio de sesión **solo se activa cuando el CAPTCHA es validado correctamente**, lo que mejora la seguridad de acceso al sistema.  
📸 ![CAPTCHA y login](<img width="518" height="655" alt="Vista Sesion" src="https://github.com/user-attachments/assets/f5a31996-cd64-4125-a5e5-eecfbfe84c76" />
)

---

### 👥 Roles de usuario: Gerente y Cajero

El sistema define dos tipos de usuarios con accesos diferenciados:

#### 👔 Gerente (Administrador)
- Tiene acceso completo al sistema.
- Puede **añadir y editar usuarios**.
- Gestiona el inventario: productos, precios, stock y estado.
- Asigna cuentas de inicio de sesión a nuevos empleados.
- Accede a herramientas administrativas y base de datos desde interfaz.
- Puede consultar y modificar los datos de ventas.

📸 ![Vista del gerente](img/mysql-productos.png)

#### 💼 Cajero
- Solo puede acceder al **módulo de ventas**.
- Registra productos vendidos.
- Selecciona método de pago (efectivo o tarjeta).
- Genera automáticamente un **ticket o factura de venta en PDF**.
- No puede editar ni consultar datos de otros módulos.

📸 ![Vista del cajero - ventas](img/interfaz-ventas.png)

---

### 🧸 CRUD de Productos (Juguetes)

Desde el panel del **Gerente**, se pueden administrar productos:

- Añadir nuevos artículos al catálogo.
- Modificar precio, estado y stock disponible.
- Eliminar productos descontinuados o en error.

Los productos están clasificados por nombre, estado, precio y stock.

📸 ![Gestión de productos](img/mysql-productos.png)

---

### 💰 Proceso principal: Venta de juguetes

La interfaz de ventas, usada por el **Cajero**, permite:

- Buscar productos disponibles.
- Seleccionar cantidad y ver precio total.
- Elegir método de pago (tarjeta o efectivo).
- Calcular subtotal, cambio y total.
- Generar el ticket de venta.

📸 ![Interfaz de ventas](img/interfaz-ventas.png)

---

### 📧 Generación de ticket en formato PDF

Al finalizar una venta, el sistema genera automáticamente un **ticket en formato PDF** con los datos de la transacción, como nombre del producto, cantidad, total, fecha y hora.

El archivo PDF se nombra dinámicamente con la fecha y hora del momento en que se registra la venta, por ejemplo:  
`Factura_28-07-2025_13-07-09.pdf`

Para generar el PDF se utiliza la librería **iText 5.5.12** (`itextpdf-5.5.12.jar`), sin depender de componentes externos.

> Esta factura actúa como comprobante que puede ser impreso por el cliente. En esta implementación no se envía por correo electrónico, únicamente se genera localmente en el equipo del cajero.

📸 ![Ticket de venta generado](img/ticket-generado.png)

---

### 🌟 Funcionalidades adicionales

- Interfaz intuitiva con íconos temáticos alusivos a juguetería.
- Facturación numerada con fecha y hora.
- Autenticación diferenciada según rol.
- Vista adaptada para cajero y gerente.
- Diseño personalizado para facturas (con logo y colores).
