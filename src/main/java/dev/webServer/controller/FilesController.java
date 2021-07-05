package dev.webServer.controller;


import dev.webServer.message.ResponseMessage;
import dev.webServer.model.FileInfo;
import dev.webServer.model.Project;
import dev.webServer.service.ConvertService;
import dev.webServer.service.FilesStorageService;
import dev.webServer.service.GanttChartService;

import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
public class FilesController {
    @Autowired
    FilesStorageService storageService;
    @Autowired
    ConvertService convertService;
    @Autowired
    GanttChartService ganttChartService;
    String key = "MY_API_KEY";

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> fileUpload(@RequestParam("file") MultipartFile file,
                                                      @RequestParam("api-key") String apiKey) {
        String message;
        try {
            if (key.equals(apiKey)) {
                storageService.save(file);
                message = file.getOriginalFilename();

                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            } else {
                message = "You need api key to upload file!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }

        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("/files")
    public ResponseEntity<List<FileInfo>> getListFiles() {
        List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(FilesController.class, "getFile", filename).build().toString();
            String ganttImage = url + "/gantt-chart-image";
            String json = url + "/json";

            return new FileInfo(filename, url, ganttImage, json);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<FileInfo> getFile(@PathVariable String filename) throws IOException {
            Resource file = storageService.load(filename);

            String url = MvcUriComponentsBuilder
                    .fromMethodName(FilesController.class, "getFile", file.getFilename()).build().toString();
            String ganttImage = url + "/gantt-chart-image";
            String json = url + "/json";

            FileInfo fileInfo = new FileInfo(filename, url, ganttImage, json);
            return ResponseEntity.status(HttpStatus.OK).body(fileInfo);
    }

    @DeleteMapping("files/{filename:.+}/delete")
    public ResponseEntity<ResponseMessage> deleteFile(@PathVariable String filename) {
        String message;
        if (storageService.delete(filename)) {
            message = filename + " deleted! ";
        } else {
            message = "Could not delete this file !";
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
    }


    @GetMapping("/files/{filename:.+}/json")
    public ResponseEntity<Project> getJsonFile(@PathVariable String filename) {
        Resource file = storageService.load(filename);
        Project jsonProject = convertService.toJsonProject(file.getFilename());

        if (jsonProject != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(jsonProject);
        }

        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                .body(new Project("Could not read file: " + file.getFilename()));

    }

    @GetMapping(
            value = "/files/{filename:.+}/gantt-chart-image",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody byte[] getGanttImage(@PathVariable String filename) throws IOException, InterruptedException {
        Resource file = storageService.load(filename);
        Project jsonProject = convertService.toJsonProject(file.getFilename());

        if (jsonProject != null) {
            ganttChartService.convertToImage(jsonProject, "gantt.mmd");

            Resource image = ganttChartService.load("gantt.png");
            InputStream in = image.getInputStream();
            return IOUtils.toByteArray(in);
        }

        return null;
    }


}

