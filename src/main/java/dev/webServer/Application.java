package dev.webServer;
import dev.webServer.service.FilesStorageService;
import dev.webServer.service.GanttChartService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import javax.annotation.Resource;

@SpringBootApplication
public class Application implements CommandLineRunner {
    @Resource
    FilesStorageService storageService;
    @Resource
    GanttChartService ganttChartService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... arg) throws Exception {
        storageService.init();
        ganttChartService.init();
    }


}
