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
package org.jboss.pnc.web;

import org.jboss.pnc.common.Configuration;
import org.jboss.pnc.common.json.ConfigurationParseException;
import org.jboss.pnc.common.json.moduleconfig.UIModuleConfig;
import org.jboss.pnc.common.json.moduleprovider.PncConfigProvider;
import org.jboss.pnc.rest.utils.JsonOutputConverterMapper;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Dynamically serves a configuration file for the UI.
 *
 * @author Alex Creasy
 */
@WebServlet("/scripts/config.js")
public class UIConfigurationServlet extends HttpServlet {

    public static final int CACHE_EXPIRES_IN = 0; // Cache time in seconds.
    public static final String PNC_GLOBAL_MODULE = "pnc";
    public static final String CONFIG_PROPERTY = "config";

    private String uiConfig;

    public UIConfigurationServlet() {}

    @Inject
    public UIConfigurationServlet(Configuration configuration) throws ConfigurationParseException {
        this.uiConfig = generateJS(JsonOutputConverterMapper.apply(configuration.getModuleConfig(
                new PncConfigProvider<UIModuleConfig>(UIModuleConfig.class))));
    }

    private String generateJS(String configJson) {
        return String.format("var %s = %s || {}; var %s.%s = %s;", PNC_GLOBAL_MODULE, PNC_GLOBAL_MODULE,
                PNC_GLOBAL_MODULE, CONFIG_PROPERTY, configJson);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/javascript");

        // Override our default javascript cache time of 10 years as we're not using the standard cache
        // defeating mechanism here.
        resp.setHeader("Cache-Control", "max-age=" + CACHE_EXPIRES_IN);

        PrintWriter writer = resp.getWriter();
        writer.println(uiConfig);
        writer.close();
    }
}
