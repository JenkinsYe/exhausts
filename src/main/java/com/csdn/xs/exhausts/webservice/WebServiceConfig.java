package com.csdn.xs.exhausts.webservice;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;
/**
 * @author YJJ
 * @Date: Created in 12:11 2019-12-08
 */
@Configuration
@Deprecated
public class WebServiceConfig {

    @Bean
    public ServletRegistrationBean dispatcherServletConfig(){
        return new ServletRegistrationBean(new CXFServlet(), "/ws-api/*");
    }

    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        return new SpringBus();
    }

    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(springBus(), new RemoteSenseUploadService());
        endpoint.publish("/uploadRemoteSense");
        return endpoint;
    }
}
