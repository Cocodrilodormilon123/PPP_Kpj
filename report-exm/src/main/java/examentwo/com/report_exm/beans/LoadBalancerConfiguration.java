package examentwo.com.report_exm.beans;

import org.slf4j.*;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

public class LoadBalancerConfiguration {
    private final Logger logger = LoggerFactory.getLogger(LoadBalancerConfiguration.class);

    @Bean
    public ServiceInstanceListSupplier serviceInstanceListSupplier(ConfigurableApplicationContext context) {
    logger.info("Load balancer service instance list");
    return ServiceInstanceListSupplier.builder()
            .withBlockingDiscoveryClient()
            .build(context);
    }
}
