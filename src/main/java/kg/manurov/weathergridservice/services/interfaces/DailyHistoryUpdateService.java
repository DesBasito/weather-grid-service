package kg.manurov.weathergridservice.services.interfaces;

import java.time.LocalDate;

public interface DailyHistoryUpdateService {
    void updateRange(LocalDate start, LocalDate end);
}
