package org.graylog.plugins.nsq;

import com.codahale.metrics.MetricSet;
import com.github.brainlag.nsq.NSQConsumer;
import com.github.brainlag.nsq.NSQMessage;
import com.github.brainlag.nsq.callbacks.NSQMessageCallback;
import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import org.graylog2.plugin.LocalMetricRegistry;
import org.graylog2.plugin.configuration.Configuration;
import org.graylog2.plugin.configuration.ConfigurationRequest;
import org.graylog2.plugin.inputs.MessageInput;
import org.graylog2.plugin.inputs.MisfireException;
import org.graylog2.plugin.inputs.annotations.ConfigClass;
import org.graylog2.plugin.inputs.annotations.FactoryClass;
import org.graylog2.plugin.inputs.codecs.CodecAggregator;
import org.graylog2.plugin.inputs.transports.ThrottleableTransport;
import org.graylog2.plugin.inputs.transports.Transport;
import org.graylog2.plugin.journal.RawMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class NsqTransport extends ThrottleableTransport {
    private static final Logger LOG = LoggerFactory.getLogger(NsqTransport.class);

    private final Configuration configuration;
    private final LocalMetricRegistry localRegistry;

    private NSQConsumer consumer;

    @Inject
    public NsqTransport(@Assisted final Configuration configuration,
                          final EventBus serverEventBus,
                          final LocalMetricRegistry localRegistry) {
        super(serverEventBus, configuration);
        this.configuration = configuration;
        this.localRegistry = localRegistry;
    }

    @Override
    protected void doLaunch(final MessageInput input) throws MisfireException {
        consumer = new NsqClientBuilder(configuration).buildLookupd(new NSQMessageCallback() {
            @Override
            public void message(NSQMessage nsqMessage) {
                final RawMessage rawMessage = new RawMessage(nsqMessage.getMessage());
                nsqMessage.finished();
                input.processRawMessage(rawMessage);

                // do throttle the code
                if (isThrottled()) {
                    blockUntilUnthrottled();
                }

            }
        });

        consumer.start();
    }

    @Override
    protected void doStop() {
        if (consumer != null) {
            consumer.shutdown();
        }
    }

    @Override
    public void setMessageAggregator(CodecAggregator aggregator) {
    }

    @Override
    public MetricSet getMetricSet() {
        return localRegistry;
    }

    @FactoryClass
    public interface Factory extends Transport.Factory<NsqTransport> {
        @Override
        NsqTransport create(Configuration configuration);

        @Override
        Config getConfig();
    }

    @ConfigClass
    public static class Config extends NsqThrottleableTransport.Config {
        @Override
        public ConfigurationRequest getRequestedConfiguration() {
            return new NsqClientConfiguration(super.getRequestedConfiguration());
        }
    }
}
