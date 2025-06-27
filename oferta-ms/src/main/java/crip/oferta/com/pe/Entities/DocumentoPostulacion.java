package crip.oferta.com.pe.Entities;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "documento_postulacion")
public class DocumentoPostulacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_postulacion", nullable = false)
    private Long idPostulacion;

    @Column(name = "ruta_archivo", nullable = false)
    private String rutaArchivo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoDocumento estado;

    @Column(name = "fecha_subida")
    private LocalDate fechaSubida;

    @Column(name = "comentario")
    private String comentario;

    @PrePersist
    public void prePersist() {
        this.estado = EstadoDocumento.PENDIENTE;
        this.fechaSubida = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdPostulacion() {
        return idPostulacion;
    }

    public void setIdPostulacion(Long idPostulacion) {
        this.idPostulacion = idPostulacion;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public EstadoDocumento getEstado() {
        return estado;
    }

    public void setEstado(EstadoDocumento estado) {
        this.estado = estado;
    }

    public LocalDate getFechaSubida() {
        return fechaSubida;
    }

    public void setFechaSubida(LocalDate fechaSubida) {
        this.fechaSubida = fechaSubida;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    @Override
    public String toString() {
        return "DocumentoPostulacion{" +
                "id=" + id +
                ", idPostulacion=" + idPostulacion +
                ", rutaArchivo='" + rutaArchivo + '\'' +
                ", estado=" + estado +
                ", fechaSubida=" + fechaSubida +
                ", comentario='" + comentario + '\'' +
                '}';
    }
}
