package examentwo.com.report_exm.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class Postulacion {

    private Long id;
    private String comentario;
    private EstadoPostulacion estado;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaPostulacion;
    private Long idPersona;
    private Oferta oferta;

    public Postulacion() {
    }

    public Postulacion(Long id, String comentario, EstadoPostulacion estado, LocalDate fechaPostulacion, Long idPersona, Oferta oferta) {
        this.id = id;
        this.comentario = comentario;
        this.estado = estado;
        this.fechaPostulacion = fechaPostulacion;
        this.idPersona = idPersona;
        this.oferta = oferta;
    }

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

    public LocalDate getFechaPostulacion() {
        return fechaPostulacion;
    }

    public void setFechaPostulacion(LocalDate fechaPostulacion) {
        this.fechaPostulacion = fechaPostulacion;
    }

    public Long getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Long idPersona) {
        this.idPersona = idPersona;
    }

    public Oferta getOferta() {
        return oferta;
    }

    public void setOferta(Oferta oferta) {
        this.oferta = oferta;
    }

    // Builder interno
    public static class Builder {
        private Long id;
        private String comentario;
        private EstadoPostulacion estado;
        private LocalDate fechaPostulacion;
        private Long idPersona;
        private Oferta oferta;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder comentario(String comentario) {
            this.comentario = comentario;
            return this;
        }

        public Builder estado(EstadoPostulacion estado) {
            this.estado = estado;
            return this;
        }

        public Builder fechaPostulacion(LocalDate fechaPostulacion) {
            this.fechaPostulacion = fechaPostulacion;
            return this;
        }

        public Builder idPersona(Long idPersona) {
            this.idPersona = idPersona;
            return this;
        }

        public Builder oferta(Oferta oferta) {
            this.oferta = oferta;
            return this;
        }

        public Postulacion build() {
            return new Postulacion(id, comentario, estado, fechaPostulacion, idPersona, oferta);
        }
    }

    // Método estático para acceder al builder
    public static Builder builder() {
        return new Builder();
    }
}
