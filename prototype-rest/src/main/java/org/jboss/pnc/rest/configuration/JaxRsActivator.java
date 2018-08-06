/**
 * JBoss, Home of Professional Open Source.
 * Copyright 2014 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.pnc.rest.configuration;

import io.swagger.jaxrs.config.BeanConfig;

import org.jboss.pnc.rest.configuration.metrics.GeneralRestMetricsFilter;
import org.jboss.pnc.rest.configuration.metrics.TimedMetric;
import org.jboss.pnc.rest.configuration.metrics.TimedMetricFilter;
import org.jboss.pnc.rest.endpoint.BuildConfigurationEndpointImpl;
import org.jboss.resteasy.plugins.interceptors.CorsFilter;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

@ApplicationPath("/rest")
public class JaxRsActivator extends Application {

    private Set<Object> singletons = new HashSet<Object>();

    public JaxRsActivator() throws IOException {
        configureSwagger();
        configureCors();
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        addSwaggerResources(resources);
        addProjectResources(resources);
        addMetricsResources(resources);
        return resources;
    }

    private void configureCors () {
        CorsFilter corsFilter = new CorsFilter();
        corsFilter.getAllowedOrigins().add("*");
        corsFilter.setAllowedMethods("OPTIONS, GET, POST, DELETE, PUT, PATCH");
        singletons.add(corsFilter);
    }

    private final void configureSwagger() throws IOException {
        BeanConfig swaggerConfig = new BeanConfig();
        swaggerConfig.setVersion("1.0.0");
        swaggerConfig.setBasePath(getRestBasePath());
        swaggerConfig.setPrettyPrint(true);
        swaggerConfig.setResourcePackage("org.jboss.pnc.rest");
        swaggerConfig.setScan(true);
    }

    private void addProjectResources(Set<Class<?>> resources) {
        addEndpoints(resources);
        addExceptionMappers(resources);
    }

    private void addEndpoints(Set<Class<?>> resources) {
        resources.add(BuildConfigurationEndpointImpl.class);
    }

    private void addExceptionMappers(Set<Class<?>> resources) {
        resources.add(ValidationExceptionExceptionMapper.class);
        resources.add(BuildConflictExceptionMapper.class);
        resources.add(AllOtherExceptionsMapper.class);
    }


    private void addSwaggerResources(Set<Class<?>> resources) {
        resources.add(io.swagger.jaxrs.listing.ApiListingResource.class);
        resources.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);
    }

    private void addMetricsResources(Set<Class<?>> resources) {
        resources.add(GeneralRestMetricsFilter.class);
        resources.add(TimedMetric.class);
        resources.add(TimedMetricFilter.class);
    }

    private String getRestBasePath() throws IOException {
        URL resource = Thread.currentThread().getContextClassLoader().getResource("/swagger.properties");
        Properties properties = new Properties();
        try (InputStream inStream = resource.openStream()) {
            properties.load(inStream);
        }
        return properties.getProperty("baseUrl");
    }

}
