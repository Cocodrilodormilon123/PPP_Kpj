package examentwo.com.report_exm.repositories;


import examentwo.com.report_exm.beans.LoadBalancerConfiguration;
import examentwo.com.report_exm.models.Oferta;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "oferta-ms")
@LoadBalancerClient(name = "oferta-ms",configuration = LoadBalancerConfiguration.class)
public interface ResultadosRepository {

    @GetMapping("/oferta-ms/ofertas")
    List<Oferta> getOferta();

    @GetMapping("/oferta-ms/ofertas/{id}")
    Optional<Oferta> getById(@PathVariable Long id);

}
