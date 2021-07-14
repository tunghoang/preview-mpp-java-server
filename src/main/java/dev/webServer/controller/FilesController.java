package dev.webServer.controller;

import dev.webServer.api.TableApi;
import dev.webServer.message.ResponseMessage;
import dev.webServer.model.FileInfo;
import dev.webServer.model.Project;
import dev.webServer.service.ConvertService;
import dev.webServer.service.FilesStorageService;
import dev.webServer.service.DataStorageService;

import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
public class FilesController {
    @Autowired
    FilesStorageService storageService;
    @Autowired
    ConvertService convertService;
    @Autowired
    DataStorageService dataStorageService;
    @Autowired
    TableApi tableApi;

    String key = "MY_API_KEY";

    @PostMapping("/tasks/api/FileManager/UploadFile")
    public ResponseEntity<ResponseMessage> fileUpload(@RequestParam("file") MultipartFile file,
                                                      @RequestParam("api-key") String apiKey) {
        String message;
        try {
            if (key.equals(apiKey)) {
                String fileGuid = storageService.save(file);
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(fileGuid));
            } else {
                message = "You need api key to upload file!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @PostMapping("/tasks/api/TasksConversion/Convert")
    public ResponseEntity<ResponseMessage> convertFile(@RequestBody Map<String, String> payload) throws IOException {
        String message;
        String fileGuid = payload.get("fileGuid");
        String format = payload.get("format");
        Resource file = storageService.load(fileGuid);
        Project jsonProject = convertService.toJsonProject(file.getFilename());

        if (jsonProject != null) {
            if (format.equals("HTML")) {
                StringBuilder htmlFile = new StringBuilder(tableApi.getHtmlStringTable(fileGuid));
                htmlFile.append("<img src='data:image/png;base64,")
                        .append(convertService.ganttToImage(jsonProject))
                        .append("'/>");
                FileWriter writer = new FileWriter("data/" + fileGuid + ".html");
                writer.write(htmlFile.toString());
                writer.close();

                message = "Convert succesfully!";
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            }

            message = "Just convert to html!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }

        message = "Could not convert this file!";
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
    }

    @GetMapping("/tasks/api/FileManager/DownloadFile/{fileGuid}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileGuid) {
        Resource file = dataStorageService.load(fileGuid + ".html");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"index.html\"").body(file);
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

    @DeleteMapping("files/delete/{filename:.+}")
    public ResponseEntity<ResponseMessage> deleteFile(@PathVariable String filename) {
        String message;
        if (storageService.delete(filename)) {
            message = filename + " deleted! ";
        } else {
            message = "Could not delete this file !";
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
    }


    @GetMapping("/files/json/{filename:.+}")
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
            value = "/files/gantt-chart-image/{filename:.+}",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody byte[] getGanttImage(@PathVariable String filename) throws IOException {
        Resource file = storageService.load(filename);
        Project jsonProject = convertService.toJsonProject(file.getFilename());

        if (jsonProject != null) {
            convertService.ganttToImage(jsonProject);

            Resource image = dataStorageService.load("gantt.png");
            InputStream in = image.getInputStream();
            return IOUtils.toByteArray(in);
        }

        return null;
    }

}

