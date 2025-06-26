package crip.oferta.com.pe.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "oferta")
public class Oferta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    @Column(length = 1000)
    private String descripcion;

    @Column(nullable = false)
    private String ubicacion;
    @Column(length = 1000)
    private String requerimientos;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoOferta estado;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModalidadOferta modalidad;

    @ManyToOne
    @JoinColumn(name = "id_empresa")
    private Empresa empresa;


    @PrePersist
    public void setFechaInicioAuto() {
        if (this.fechaInicio == null) {
            this.fechaInicio = LocalDate.now();
        }
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getRequerimientos() {
        return requerimientos;
    }

    public void setRequerimientos(String requerimientos) {
        this.requerimientos = requerimientos;
    }

    public EstadoOferta getEstado() {
        return estado;
    }

    public void setEstado(EstadoOferta estado) {
        this.estado = estado;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public ModalidadOferta getModalidad() {
        return modalidad;
    }

    public void setModalidad(ModalidadOferta modalidad) {
        this.modalidad = modalidad;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    @Override
    public String toString() {
        return "Oferta{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                ", requerimientos='" + requerimientos + '\'' +
                ", estado=" + estado +
                ", fechaFin=" + fechaFin +
                ", fechaInicio=" + fechaInicio +
                ", modalidad=" + modalidad +
                ", empresa=" + empresa +
                '}';
    }
}
