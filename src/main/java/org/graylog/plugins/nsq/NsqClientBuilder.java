package org.graylog.plugins.nsq;

import com.github.brainlag.nsq.NSQConfig;
import com.github.brainlag.nsq.NSQConsumer;
import com.github.brainlag.nsq.callbacks.NSQMessageCallback;
import com.github.brainlag.nsq.lookup.DefaultNSQLookup;
import com.github.brainlag.nsq.lookup.NSQLookup;
import com.google.common.base.Splitter;
import org.graylog2.plugin.configuration.Configuration;

import java.net.MalformedURLException;
import java.net.URL;

import static java.util.Objects.requireNonNull;

public class NsqClientBuilder {
    private final Configuration configuration;

    public NsqClientBuilder(Configuration configuration) {
        this.configuration = requireNonNull(configuration);
    }

    public NSQConsumer buildLookupd(NSQMessageCallback callback) {
        final String nsqLookupdHttpAddress = configuration.getString(NsqClientConfiguration.LOOKUPD_HTTP_ADDRESS, "http://127.0.0.1");
        final String nsqTopic = configuration.getString(NsqClientConfiguration.TOPIC, "logging");
        final String nsqChannel = configuration.getString(NsqClientConfiguration.CHANNEL, "loggingChannel");
        final Integer maxInFlight = configuration.getInt(NsqClientConfiguration.MAX_IN_FLIGHT, 200);

        NSQLookup lookup = new DefaultNSQLookup();
        NSQConfig nsqConfig = new NSQConfig();
        nsqConfig.setMaxInFlight(maxInFlight);

        for(String httpAddress: Splitter.on(",").trimResults().split(nsqLookupdHttpAddress)) {
            int port = 4161;
            String host = "http://127.0.0.1";
            try {
                URL url = new URL(httpAddress);
                port = url.getPort();
                host = url.getHost();
            } catch (MalformedURLException ignore) {

            }
            lookup.addLookupAddress(host, port);
        }
        return new NSQConsumer(lookup, nsqTopic, nsqChannel, callback, nsqConfig);
    }
}
