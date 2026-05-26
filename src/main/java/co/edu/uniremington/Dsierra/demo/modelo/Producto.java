package co.edu.uniremington.Dsierra.demo.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name ="productos")
@Setter
@Getter
@NoArgsConstructor
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;

    @Column(name = "marca")
    private String marca;

    @Column(length = 500, name = "descripcion")
    private String descripcion;

    @Column(name = "precio")
    private float precio;

    @Column(name = "stock")
    private int stock;

    @Column(length = 500)
    private String foto;
}
