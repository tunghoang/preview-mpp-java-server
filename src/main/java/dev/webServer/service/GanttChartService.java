package dev.webServer.service;

import dev.webServer.model.Project;
import dev.webServer.model.Task;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.stream.Stream;

@Service
public class GanttChartService {
    private final Path root = Paths.get("gantt-chart");

    public void init() {
        try {
            Files.createDirectory(root);
        } catch (IOException e) {
//            throw new RuntimeException("Could not initialize folder for upload!");
            System.out.println("Warning: " + e.getMessage() + " folder has been initialized! ");
        }
    }

    public void convertToImage(Project jsonProject, String filename) {
        StringBuilder ganttData = new StringBuilder("gantt\n");
        ganttData.append("title ").append(jsonProject.getProjectTitle()).append("\n");
        ganttData.append("dateFormat YYYY-MM-DD\n");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        for (Task task : jsonProject.getTasks()) {
            if (task.getId() == 0) continue;
            ganttData.append(task.getName()).append(": ").append(task.getId()).append(", ")
                    .append(formatter.format(task.getStart())).append(", ")
                    .append((int) task.getDuration().getDuration()).append("d\n");
        }

        try {
            PrintWriter writer = new PrintWriter(this.root.resolve(filename).toString());
            ganttData = new StringBuilder(ganttData.toString().replaceAll("#", ""));
            writer.println(ganttData);
            writer.close();

            boolean isWindows = System.getProperty("os.name")
                    .toLowerCase().startsWith("windows");
            Process process;
            if (isWindows) {
                process = Runtime.getRuntime()
                        .exec(String.format("cmd /c E: cd %s && mmdc -i gantt.mmd -o gantt.png", this.root));

            } else {
                process = Runtime.getRuntime()
                        .exec(String.format("sh -c cd %s && mmdc -i gantt.mmd -o gantt.png", this.root));
            }
            process.waitFor();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }
}
