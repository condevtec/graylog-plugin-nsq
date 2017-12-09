package org.graylog.plugins.nsq;

import com.google.auto.service.AutoService;
import org.graylog2.plugin.Plugin;
import org.graylog2.plugin.PluginMetaData;
import org.graylog2.plugin.PluginModule;

import java.util.Collection;
import java.util.Collections;

@AutoService(Plugin.class)
public class NsqPlugin implements Plugin {
    @Override
    public PluginMetaData metadata() {
        return new NsqMetaData();
    }

    @Override
    public Collection<PluginModule> modules() {
        return Collections.singleton(new NsqModule());
    }
}
