package org.graylog.plugins.nsq;

import com.google.common.eventbus.EventBus;
import org.graylog2.plugin.ThrottleState;
import org.graylog2.plugin.configuration.Configuration;
import org.graylog2.plugin.inputs.transports.ThrottleableTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reimund Klain
 *
 * Overwrite determineIfThrottled to own values.
 */
public abstract class NsqThrottleableTransport extends ThrottleableTransport {
    private static final Logger log = LoggerFactory.getLogger(NsqThrottleableTransport.class);
    private long lastUncommitted;

    public NsqThrottleableTransport(EventBus eventBus, Configuration configuration) {
        super(eventBus, configuration);
    }

    @Override
    protected boolean determineIfThrottled(ThrottleState state) {
        long prevUncommitted = this.lastUncommitted;
        this.lastUncommitted = state.uncommittedJournalEntries;
        String transportName = this.getClass().getSimpleName();
        log.debug("Checking if transport {} should be throttled {}", transportName, state);
        if (state.uncommittedJournalEntries == 0L) {
            log.debug("[{}] [unthrottled] journal empty", transportName);
            return false;
        } else if (state.uncommittedJournalEntries > 100000L) {
            log.info("[{}] [throttled] number of unread journal entries is larger than 100.000 entries: {}", transportName, state.uncommittedJournalEntries);
            return true;
        } else if (state.uncommittedJournalEntries - prevUncommitted > 20000L) {
            log.info("[{}] [throttled] number of unread journal entries is growing by more than 20.000 entries: {}", transportName, state.uncommittedJournalEntries - prevUncommitted);
            return true;
        } else if (state.processBufferCapacity == 0L) {
            log.info("[{}] [throttled] no capacity in process buffer", transportName);
            return true;
        } else if (state.appendEventsPerSec == 0L && state.readEventsPerSec == 0L && state.processBufferCapacity > 0L) {
            log.debug("[{}] [unthrottled] no incoming messages and nothing read from journal even if we could", transportName);
            return false;
        } else if ((double)state.journalSize / (double)state.journalSizeLimit * 100.0D > 75.0D) {
            log.info("[{}] [throttled] journal more than 75% full", transportName);
            return true;
        } else if ((double)state.readEventsPerSec / (double)state.appendEventsPerSec * 100.0D < 50.0D) {
            log.info("[{}] [throttled] write rate is more than twice as high than read rate", transportName);
            return true;
        } else {
            log.debug("[{}] [unthrottled] fall through", transportName);
            return false;
        }
    }
}
