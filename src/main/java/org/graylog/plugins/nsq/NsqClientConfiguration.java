package org.graylog.plugins.nsq;

import com.google.common.collect.ImmutableList;
import org.graylog2.plugin.configuration.ConfigurationRequest;
import org.graylog2.plugin.configuration.fields.ConfigurationField;
import org.graylog2.plugin.configuration.fields.NumberField;
import org.graylog2.plugin.configuration.fields.TextField;

import static java.util.Objects.requireNonNull;

public class NsqClientConfiguration extends ConfigurationRequest {
    static final String LOOKUPD_HTTP_ADDRESS = "lookup_http_address";
    static final String TOPIC = "topic";
    static final String CHANNEL = "channel";
    static final String MAX_IN_FLIGHT = "max_in_flight";

    public NsqClientConfiguration(ConfigurationRequest configurationRequest) {
        this(requireNonNull(configurationRequest, "configurationRequest").getFields().values());
    }

    NsqClientConfiguration(Iterable<ConfigurationField> fields) {
        addFields(
                ImmutableList.<ConfigurationField>builder()
                        .addAll(fields)
                        .add(new TextField(
                                LOOKUPD_HTTP_ADDRESS,
                                "Lookupd HTTP addresses",
                                "http://localhost:4161",
                                "Lookupd HTTP addresses: http://host01:4161,http://host02:4161",
                                ConfigurationField.Optional.NOT_OPTIONAL))
                        .add(new TextField(TOPIC,
                                "Topic",
                                "graylog-nsq",
                                "topic with log lines",
                                ConfigurationField.Optional.NOT_OPTIONAL))
                        .add(new TextField(CHANNEL,
                                "CHANNEL",
                                "graylog-nsq#ephemeral",
                                "channel for consumption. Default is emphemeral channel",
                                ConfigurationField.Optional.NOT_OPTIONAL))
                        .add(new NumberField(MAX_IN_FLIGHT,
                                "max in flight",
                                200,
                                "Max number of messages to allow in flight. Default: 200",
                                ConfigurationField.Optional.OPTIONAL))
                        .build()
        );
    }
}
