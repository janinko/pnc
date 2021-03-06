/**
 * JBoss, Home of Professional Open Source.
 * Copyright 2014-2019 Red Hat, Inc., and individual contributors
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
package org.jboss.pnc.integration_new;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.pnc.client.GroupConfigurationClient;
import org.jboss.pnc.client.RemoteResourceException;
import org.jboss.pnc.dto.GroupConfiguration;
import org.jboss.pnc.integration_new.setup.Deployments;
import org.jboss.pnc.integration_new.setup.RestClientConfiguration;
import org.jboss.pnc.test.category.ContainerTest;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import org.jboss.pnc.client.ClientException;
import org.jboss.pnc.dto.ProductVersionRef;
import static org.junit.Assert.assertEquals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:matejonnet@gmail.com">Matej Lazar</a>
 */
@RunAsClient
@RunWith(Arquillian.class)
@Category(ContainerTest.class)
public class GroupConfigEndpointTest {

    private static final Logger logger = LoggerFactory.getLogger(GroupConfigEndpointTest.class);

    @Deployment
    public static EnterpriseArchive deploy() {
        return Deployments.testEar();
    }

    @Test
    public void testCreateNewGroupConfig() throws RemoteResourceException, ClientException {
        GroupConfigurationClient client = new GroupConfigurationClient(RestClientConfiguration.getConfiguration(RestClientConfiguration.AuthenticateAs.USER));
        
        final String name = "Testing 101";
        
        GroupConfiguration gc = GroupConfiguration.builder()
                .productVersion(ProductVersionRef.refBuilder().id(101).build())
                .name(name)
                .build();
        
        GroupConfiguration created = client.createNew(gc);
        assertEquals(name, created.getName());
        GroupConfiguration specific = client.getSpecific(created.getId());
        assertEquals(created, specific);
    }

    @Test
    public void testUpdateBuildConfigurationSet() throws RemoteResourceException, ClientException {     
        GroupConfigurationClient client = new GroupConfigurationClient(RestClientConfiguration.getConfiguration(RestClientConfiguration.AuthenticateAs.USER));

        final String name = "Testing 100 Updated";
        
        GroupConfiguration specific = client.getSpecific(100);
        GroupConfiguration updating = specific.toBuilder().name(name).build();
        client.update(100, updating);
        GroupConfiguration updated = client.getSpecific(100);
        assertEquals(name, updated.getName());
    }

}
