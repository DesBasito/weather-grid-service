package kg.manurov.weathergridservice.services.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
@Slf4j
public class OpenMeteoCounter {
    private static final long DAILY_LIMIT = 10_000;
    private final AtomicLong counter = new AtomicLong(0);

    public boolean tryConsume() {
        long current = counter.incrementAndGet();
        return current <= DAILY_LIMIT;
    }

    public long getRemaining() {
        return DAILY_LIMIT - counter.get();
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void resetDailyLimit() {
        counter.set(0);
        log.info("[OpenMeteo] Daily counter reset");
    }
}
