package examentwo.com.report_exm.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class Empresa {

    private Long id;
    private String nombre;
    private String direccion;
    private String distrito;
    private String provincia;
    private String pais;
    private String ruc;
    private String telefono;
    private EstadoEmpresa estado;
    private String representanteLegal;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaCreacion;
    private Long idPersona;

    public Empresa() {
    }

    public Empresa(Long id, String nombre, String direccion, String distrito, String provincia, String pais, String ruc, String telefono, EstadoEmpresa estado, String representanteLegal, LocalDate fechaCreacion, Long idPersona) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.distrito = distrito;
        this.provincia = provincia;
        this.pais = pais;
        this.ruc = ruc;
        this.telefono = telefono;
        this.estado = estado;
        this.representanteLegal = representanteLegal;
        this.fechaCreacion = fechaCreacion;
        this.idPersona = idPersona;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public EstadoEmpresa getEstado() {
        return estado;
    }

    public void setEstado(EstadoEmpresa estado) {
        this.estado = estado;
    }

    public String getRepresentanteLegal() {
        return representanteLegal;
    }

    public void setRepresentanteLegal(String representanteLegal) {
        this.representanteLegal = representanteLegal;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Long getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Long idPersona) {
        this.idPersona = idPersona;
    }

    // Builder interno
    public static class Builder {
        private Long id;
        private String nombre;
        private String direccion;
        private String distrito;
        private String provincia;
        private String pais;
        private String ruc;
        private String telefono;
        private EstadoEmpresa estado;
        private String representanteLegal;
        private LocalDate fechaCreacion;
        private Long idPersona;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder direccion(String direccion) {
            this.direccion = direccion;
            return this;
        }

        public Builder distrito(String distrito) {
            this.distrito = distrito;
            return this;
        }

        public Builder provincia(String provincia) {
            this.provincia = provincia;
            return this;
        }

        public Builder pais(String pais) {
            this.pais = pais;
            return this;
        }

        public Builder ruc(String ruc) {
            this.ruc = ruc;
            return this;
        }

        public Builder telefono(String telefono) {
            this.telefono = telefono;
            return this;
        }

        public Builder estado(EstadoEmpresa estado) {
            this.estado = estado;
            return this;
        }

        public Builder representanteLegal(String representanteLegal) {
            this.representanteLegal = representanteLegal;
            return this;
        }

        public Builder fechaCreacion(LocalDate fechaCreacion) {
            this.fechaCreacion = fechaCreacion;
            return this;
        }

        public Builder idPersona(Long idPersona) {
            this.idPersona = idPersona;
            return this;
        }

        public Empresa build() {
            return new Empresa(id, nombre, direccion, distrito, provincia, pais, ruc, telefono,
                    estado, representanteLegal, fechaCreacion, idPersona);
        }
    }

    // Método estático para acceder al builder
    public static Builder builder() {
        return new Builder();
    }
}
