package examentwo.com.report_exm.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;


public class Oferta {

    private Long id;
    private String titulo;
    private String descripcion;
    private EstadoOferta estado;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaInicio;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaFin;
    private ModalidadOferta modalidad;
    private Empresa empresa;

    public Oferta() {
    }

    public Oferta(Long id, String titulo, String descripcion, EstadoOferta estado, LocalDate fechaInicio, LocalDate fechaFin, ModalidadOferta modalidad, Empresa empresa) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.modalidad = modalidad;
        this.empresa = empresa;
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

    public EstadoOferta getEstado() {
        return estado;
    }

    public void setEstado(EstadoOferta estado) {
        this.estado = estado;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
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

    // Builder interno
    public static class Builder {
        private Long id;
        private String titulo;
        private String descripcion;
        private EstadoOferta estado;
        private LocalDate fechaInicio;
        private LocalDate fechaFin;
        private ModalidadOferta modalidad;
        private Empresa empresa;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder titulo(String titulo) {
            this.titulo = titulo;
            return this;
        }

        public Builder descripcion(String descripcion) {
            this.descripcion = descripcion;
            return this;
        }

        public Builder estado(EstadoOferta estado) {
            this.estado = estado;
            return this;
        }

        public Builder fechaInicio(LocalDate fechaInicio) {
            this.fechaInicio = fechaInicio;
            return this;
        }

        public Builder fechaFin(LocalDate fechaFin) {
            this.fechaFin = fechaFin;
            return this;
        }

        public Builder modalidad(ModalidadOferta modalidad) {
            this.modalidad = modalidad;
            return this;
        }

        public Builder empresa(Empresa empresa) {
            this.empresa = empresa;
            return this;
        }

        public Oferta build() {
            return new Oferta(id, titulo, descripcion, estado, fechaInicio, fechaFin, modalidad, empresa);
        }
    }

    // Método estático para acceder al builder
    public static Builder builder() {
        return new Builder();
    }
}