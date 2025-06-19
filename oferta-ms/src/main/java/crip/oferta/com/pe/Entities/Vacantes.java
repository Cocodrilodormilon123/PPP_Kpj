package crip.oferta.com.pe.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "vacantes")
public class Vacantes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int total;
    private int ocupados;
    private int disponibles;

    @OneToOne
    @JoinColumn(name = "id_oferta", unique = true)
    private Oferta oferta;

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

    @Override
    public String toString() {
        return "Vacantes{" +
                "id=" + id +
                ", total=" + total +
                ", ocupados=" + ocupados +
                ", disponibles=" + disponibles +
                ", oferta=" + oferta +
                '}';
    }
}
