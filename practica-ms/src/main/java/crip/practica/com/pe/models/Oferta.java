package crip.practica.com.pe.models;


public class Oferta {
    private String titulo;
    private ModalidadOferta modalidad;
    private Empresa empresa;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
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
}