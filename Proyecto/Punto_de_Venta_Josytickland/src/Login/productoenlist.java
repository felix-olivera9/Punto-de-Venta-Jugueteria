/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Login;

/**
 *
 * @author felix
 */
public class productoenlist {
    private int id;
    private String nombre;
    private int cantidad;
    private double precio;
    
     public productoenlist(int id, String nombre, int cantidad, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public Object[] toObjectArray() {
        return new Object[]{id, nombre, cantidad, precio, cantidad * precio};
    }
}
