package crip.practica.com.pe.models;

import crip.practica.com.pe.Entities.EstadoPractica;
import java.time.LocalDate;

public class DetallePracticaDTO {
    private Long idPractica;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private EstadoPractica estado;
    private String tituloOferta;
    private String empresa;
    private ModalidadOferta modalidad;

    public Long getIdPractica() {
        return idPractica;
    }

    public void setIdPractica(Long idPractica) {
        this.idPractica = idPractica;
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

    public EstadoPractica getEstado() {
        return estado;
    }

    public void setEstado(EstadoPractica estado) {
        this.estado = estado;
    }

    public String getTituloOferta() {
        return tituloOferta;
    }

    public void setTituloOferta(String tituloOferta) {
        this.tituloOferta = tituloOferta;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public ModalidadOferta getModalidad() {
        return modalidad;
    }

    public void setModalidad(ModalidadOferta modalidad) {
        this.modalidad = modalidad;
    }
}