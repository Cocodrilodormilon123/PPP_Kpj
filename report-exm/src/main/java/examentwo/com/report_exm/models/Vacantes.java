package examentwo.com.report_exm.models;

public class Vacantes {

    private Long id;
    private int total;
    private int ocupados;
    private int disponibles;
    private Oferta oferta;

    public Vacantes() {
    }

    public Vacantes(Long id, int total, int ocupados, int disponibles, Oferta oferta) {
        this.id = id;
        this.total = total;
        this.ocupados = ocupados;
        this.disponibles = disponibles;
        this.oferta = oferta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getOcupados() {
        return ocupados;
    }

    public void setOcupados(int ocupados) {
        this.ocupados = ocupados;
    }

    public int getDisponibles() {
        return disponibles;
    }

    public void setDisponibles(int disponibles) {
        this.disponibles = disponibles;
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
        private int total;
        private int ocupados;
        private int disponibles;
        private Oferta oferta;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder total(int total) {
            this.total = total;
            return this;
        }

        public Builder ocupados(int ocupados) {
            this.ocupados = ocupados;
            return this;
        }

        public Builder disponibles(int disponibles) {
            this.disponibles = disponibles;
            return this;
        }

        public Builder oferta(Oferta oferta) {
            this.oferta = oferta;
            return this;
        }

        public Vacantes build() {
            return new Vacantes(id, total, ocupados, disponibles, oferta);
        }
    }

    // Método estático para acceder al builder
    public static Builder builder() {
        return new Builder();
    }
}
