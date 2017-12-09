package org.graylog.plugins.nsq;

import org.graylog2.plugin.PluginConfigBean;
import org.graylog2.plugin.PluginModule;

import java.util.Collections;
import java.util.Set;

public class NsqModule extends PluginModule {
    @Override
    public Set<? extends PluginConfigBean> getConfigBeans() {
        return Collections.emptySet();
    }

    @Override
    protected void configure() {
        addTransport("nsq", NsqTransport.class);

        //addMessageInput(GELFNsqInput.class);
        addMessageInput(RawNsqInput.class);
        //addMessageInput(SyslogNsqInput.class);

        //addMessageOutput(GELFNsqOutput.class);
    }
}
