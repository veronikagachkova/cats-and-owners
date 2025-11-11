package CatsAndOwners;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ShutdownLogger {
    @EventListener(ApplicationReadyEvent.class)
    public void logApplicationStartup() {
        System.out.println("Приложение успешно запущено!");
    }

    @EventListener(ContextClosedEvent.class)
    public void logApplicationShutdown() {
        System.out.println("Приложение завершило свою работу.");
    }
}