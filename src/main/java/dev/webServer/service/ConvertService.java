package dev.webServer.service;

import dev.webServer.model.*;

import dev.webServer.model.Task;
import net.sf.mpxj.*;
import net.sf.mpxj.mpp.MPPReader;
import net.sf.mpxj.reader.ProjectReader;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;


@Service
public class ConvertService {

    private final Path uploadsDir = Paths.get("uploads");

    public Project toJsonProject(String filename) {
        try {
            ProjectReader reader = new MPPReader();
            ProjectFile project = reader.read(uploadsDir.resolve(filename).toString());

            return new Project(project);

        } catch (MPXJException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String ganttToImage(Project jsonProject) {
        IntervalCategoryDataset dataset = getGanttChartData(jsonProject);
        JFreeChart chart = ChartFactory.createGanttChart(
                jsonProject.getProjectTitle(),
                "Software Development Phases",
                "Timeline",
                dataset, true, true
                , true);
        try {
            OutputStream out = new FileOutputStream("data/gantt.png");
            ChartUtilities.writeChartAsPNG(out, chart, 1000, 1000);
            byte[] fileContent = Files.readAllBytes(new File("data/gantt.png").toPath());

            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private IntervalCategoryDataset getGanttChartData(Project jsonProject) {
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
}
