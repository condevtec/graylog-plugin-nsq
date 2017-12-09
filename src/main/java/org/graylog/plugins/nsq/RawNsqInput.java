package org.graylog.plugins.nsq;

import com.codahale.metrics.MetricRegistry;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import org.graylog2.inputs.codecs.RawCodec;
import org.graylog2.plugin.LocalMetricRegistry;
import org.graylog2.plugin.ServerStatus;
import org.graylog2.plugin.configuration.Configuration;
import org.graylog2.plugin.inputs.MessageInput;
import org.graylog2.plugin.inputs.annotations.ConfigClass;
import org.graylog2.plugin.inputs.annotations.FactoryClass;

import javax.inject.Inject;

public class RawNsqInput extends MessageInput {

    private static final String NAME = "Raw/Plaintext NSQ";

    @AssistedInject
    public RawNsqInput(MetricRegistry metricRegistry,
                       @Assisted Configuration configuration,
                       NsqTransport.Factory transportFactory,
                       RawCodec.Factory codecFactory,
                       LocalMetricRegistry localRegistry,
                       Config config, Descriptor descriptor,
                       ServerStatus serverStatus) {
        super(metricRegistry, configuration, transportFactory.create(configuration), localRegistry,
                codecFactory.create(configuration), config, descriptor, serverStatus);
    }

    @FactoryClass
    public interface Factory extends MessageInput.Factory<RawNsqInput> {
        @Override
        RawNsqInput create(Configuration configuration);

        @Override
        Config getConfig();

        @Override
        Descriptor getDescriptor();
    }

    public static class Descriptor extends MessageInput.Descriptor {
        @Inject
        public Descriptor() {
            super(NAME, false, "");
        }
    }

    @ConfigClass
    public static class Config extends MessageInput.Config {
        @Inject
        public Config(NsqTransport.Factory transport, RawCodec.Factory codec) {
            super(transport.getConfig(), codec.getConfig());
        }
    }
}