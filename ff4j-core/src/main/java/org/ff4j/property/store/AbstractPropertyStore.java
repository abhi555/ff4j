package org.ff4j.property.store;

import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import org.ff4j.conf.XmlConfiguration;
import org.ff4j.conf.XmlParser;
import org.ff4j.property.AbstractProperty;

/*
 * #%L
 * ff4j-core
 * %%
 * Copyright (C) 2013 - 2015 Ff4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

/**
 * Superclass for any property store.
 *
 * @author <a href="mailto:cedrick.lunven@gmail.com">Cedrick LUNVEN</a>
 */
public abstract class AbstractPropertyStore implements PropertyStore {
    
    /**
     * Initialize store from XML Configuration File.
     *
     * @param xmlConfFile
     *      xml configuration file
     */
    public  Map<String, AbstractProperty<?>> importPropertiesFromXmlFile(String xmlConfFile) {
        // Argument validation
        if (xmlConfFile == null || xmlConfFile.isEmpty()) {
            throw new IllegalArgumentException("Configuration filename cannot be null nor empty");
        }
        // Load as Inputstream
        InputStream xmlIS = getClass().getClassLoader().getResourceAsStream(xmlConfFile);
        if (xmlIS == null) {
            throw new IllegalArgumentException("File " + xmlConfFile + " could not be read, please check path and rights");
        }
        // Use the Feature Parser
        XmlConfiguration conf = new XmlParser().parseConfigurationFile(xmlIS);
        Map<String, AbstractProperty<?>> properties = conf.getProperties();

        // Override existing configuration within database
        for (String featureName : properties.keySet()) {
            if (exist(featureName)) {
                delete(featureName);
            }
            create(properties.get(featureName));
        }
        return properties;
    }
    
    /** {@inheritDoc} */
    public String toJson() {
        StringBuilder sb = new StringBuilder("{");
        sb.append("\"type\":\"" + this.getClass().getCanonicalName() + "\"");
        Set<String> myProperties = readAllProperties().keySet();
        sb.append(",\"numberOfProperties\":" + myProperties.size());
        sb.append(",\"properties\":[");
        boolean first = true;
        for (String myProperty : myProperties) {
            if (!first) {
                sb.append(",");
            }
            first = false;
           sb.append("\"" + myProperty + "\"");
        }
        sb.append("]}");
        return sb.toString();
    }
    
}
