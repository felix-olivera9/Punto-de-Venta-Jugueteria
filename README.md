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

<p align="center">
  <img src="https://github.com/user-attachments/assets/2135638d-cb7a-44bc-99cf-d73b572fca4c" width="400" />
</p>

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
  
<p align="center">
  <img src="https://github.com/user-attachments/assets/ca1ba1fe-840a-43a3-bd3a-4779134db122" width="400" style="margin-right: 20px;" />
  <img src="https://github.com/user-attachments/assets/872a234f-e757-4c5c-a07e-82998331ab3a" width="400" />
</p>

<p align="center">
  <img src="https://github.com/user-attachments/assets/b76e7c9a-9ff6-4b47-9dfa-b9fdb811c8b6" width="400" />
</p>

#### 💼 Cajero
- Solo puede acceder al **módulo de ventas**.
- Registra productos vendidos.
- Selecciona método de pago (efectivo o tarjeta).
- Genera automáticamente un **ticket o factura de venta en PDF**.
- No puede editar ni consultar datos de otros módulos.

<p align="center">
  <img src="https://github.com/user-attachments/assets/b64e9f64-2379-45a9-8c0e-c09e7c8f28be" width="400" />
</p>

---

### 🧸 CRUD de Productos (Juguetes)

Desde el panel del **Gerente**, se pueden administrar productos:

- Añadir nuevos artículos al catálogo.
- Modificar precio, estado y stock disponible.
- Eliminar productos descontinuados o en error.

Los productos están clasificados por nombre, estado, precio y stock.

<p align="center">
  <img src="https://github.com/user-attachments/assets/3b4a4a57-159b-4b8d-821e-f8e54bb75c53" width="500" />
</p>

---

### 💰 Proceso principal: Venta de juguetes

La interfaz de ventas, usada por el **Cajero**, permite:

- Buscar productos disponibles.
- Seleccionar cantidad y ver precio total.
- Elegir método de pago (tarjeta o efectivo).
- Calcular subtotal, cambio y total.
- Generar el ticket de venta.

<p align="center">
  <img src="https://github.com/user-attachments/assets/6ef3447a-0eb6-4c96-8f28-616355fc010e" width="500" />
</p>

---

### 📧 Generación de ticket en formato PDF

Al finalizar una venta, el sistema genera automáticamente un **ticket en formato PDF** con los datos de la transacción, como nombre del producto, cantidad, total, fecha y hora.

El archivo PDF se nombra dinámicamente con la fecha y hora del momento en que se registra la venta, por ejemplo:  
`Factura_28-07-2025_13-07-09.pdf`

Para generar el PDF se utiliza la librería **iText 5.5.12** (`itextpdf-5.5.12.jar`), sin depender de componentes externos.

> Esta factura actúa como comprobante que puede ser impreso por el cliente. En esta implementación no se envía por correo electrónico, únicamente se genera localmente en el equipo del cajero.

<p align="center">
  <img src="https://github.com/user-attachments/assets/e387a873-1449-46a1-a5a0-62645f564cd9" width="500" />
</p>

---

### 🌟 Funcionalidades adicionales

- Interfaz intuitiva con íconos temáticos alusivos a juguetería.
- Facturación numerada con fecha y hora.
- Autenticación diferenciada según rol.
- Vista adaptada para cajero y gerente.
- Diseño personalizado para facturas (con logo y colores).
