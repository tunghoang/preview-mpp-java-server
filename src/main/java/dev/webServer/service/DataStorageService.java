package dev.webServer.service;

import dev.webServer.model.Project;
import dev.webServer.model.Task;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class GanttChartService {
    private final Path root = Paths.get("gantt-chart");

    public void init() {
        try {
            Files.createDirectory(root);
        } catch (IOException e) {
            System.out.println("Warning: " + e.getMessage() + " folder has been initialized! ");
        }
    }

    public void convertToImage(Project jsonProject) {
        IntervalCategoryDataset dataset = getCategoryDataset(jsonProject);
        JFreeChart chart = ChartFactory.createGanttChart(
                jsonProject.getProjectTitle(),
                "Software Development Phases",
                "Timeline",
                dataset, true, true
                , true);
        try {
            OutputStream out = new FileOutputStream("gantt-chart/gantt.png");
            ChartUtilities.writeChartAsPNG(out,
                    chart,
                    1000,
                    1000);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private IntervalCategoryDataset getCategoryDataset(Project jsonProject) {
        TaskSeries series_1 = new TaskSeries("Summary");
        TaskSeries series_2 = new TaskSeries("Task");
        TaskSeriesCollection dataset = new TaskSeriesCollection();
        for (Task task : jsonProject.getTasks()) {
            if (task.getId() == 0) {
                series_1.add(new org.jfree.data.gantt.Task(task.getName(), task.getStart(), task.getFinish()));
            } else {
                series_2.add(new org.jfree.data.gantt.Task(task.getName(), task.getStart(), task.getFinish()));
            }
        }
        dataset.add(series_1);
        dataset.add(series_2);

        return dataset;
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
