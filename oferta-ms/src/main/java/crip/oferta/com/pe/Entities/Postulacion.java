package crip.oferta.com.pe.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "postulacion")
public class Postulacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comentario;

    @Enumerated(EnumType.STRING)
    private EstadoPostulacion estado;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "fecha_postulacion")
    private LocalDate fechaPostulacion;

    @Column(name = "id_persona", nullable = false)
    private Long idPersona;

    @ManyToOne
    @JoinColumn(name = "id_oferta")
    private Oferta oferta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public EstadoPostulacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoPostulacion estado) {
        this.estado = estado;
    }

    public Long getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Long idPersona) {
        this.idPersona = idPersona;
    }

    public LocalDate getFechaPostulacion() {
        return fechaPostulacion;
    }

    public void setFechaPostulacion(LocalDate fechaPostulacion) {
        this.fechaPostulacion = fechaPostulacion;
    }


    public Oferta getOferta() {
        return oferta;
    }

    public void setOferta(Oferta oferta) {
        this.oferta = oferta;
    }

    @Override
    public String toString() {
        return "Postulacion{" +
                "id=" + id +
                ", comentario='" + comentario + '\'' +
                ", estado=" + estado +
                ", fechaPostulacion=" + fechaPostulacion +
                ", idPersona=" + idPersona +
                ", oferta=" + oferta +
                '}';
    }
}
