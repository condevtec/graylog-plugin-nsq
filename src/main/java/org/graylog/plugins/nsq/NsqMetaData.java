package org.graylog.plugins.nsq;

import org.graylog2.plugin.PluginMetaData;
import org.graylog2.plugin.ServerStatus;
import org.graylog2.plugin.Version;

import java.net.URI;
import java.util.Collections;
import java.util.Set;

public class NsqMetaData implements PluginMetaData {
    private static final String PLUGIN_PROPERTIES = "org.graylog.plugins.graylog-plugin-nsq/graylog-plugin.properties";

    @Override
    public String getUniqueId() {
        return NsqPlugin.class.getCanonicalName();
    }

    @Override
    public String getName() {
        return "Nsq Plugin";
    }

    @Override
    public String getAuthor() {
        return "Reimund Klain - ConDevTec";
    }

    @Override
    public URI getURL() {
        return URI.create("https://github.com/condevtec/graylog-plugin-nsq");
    }

    @Override
    public Version getVersion() {
        return Version.fromPluginProperties(getClass(), PLUGIN_PROPERTIES, "version", Version.from(1, 0, 0));
    }

    @Override
    public String getDescription() {
        return "Graylog NSQ input/output plugin";
    }

    @Override
    public Version getRequiredVersion() {
        return Version.fromPluginProperties(getClass(), PLUGIN_PROPERTIES, "graylog.version", Version.from(2, 2, 0));
    }

    @Override
    public Set<ServerStatus.Capability> getRequiredCapabilities() {
        return Collections.emptySet();
    }
}
