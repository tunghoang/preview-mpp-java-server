package dev.webServer.service;

import dev.webServer.model.Project;
import net.sf.mpxj.*;
import net.sf.mpxj.mpp.MPPReader;
import net.sf.mpxj.mpx.MPXReader;
import net.sf.mpxj.reader.ProjectReader;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;


@Service
public class ConvertService {

    private final static Path root = Paths.get("uploads");

    public Project toJsonProject(String filename) {
        String MPP_TYPE = "mpp";
        String MPX_TYPE = "mpx";
        try {
            ProjectReader reader = null;
            if (getExtensionFile(filename).equalsIgnoreCase(MPP_TYPE)) {
                reader = new MPPReader();
            }
            if (getExtensionFile(filename).equalsIgnoreCase(MPX_TYPE)) {
                reader = new MPXReader();
            }
            if (reader == null) {
                return null;
            }
            ProjectFile project = reader.read(root.resolve(filename).toString());
            return new Project(project);

        } catch (MPXJException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getExtensionFile(String filename) {
        int idx = filename.lastIndexOf(".");
        String extension = (idx  == -1 ? "" : filename.substring(idx + 1));

        return extension;
    }
}
