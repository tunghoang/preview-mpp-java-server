import api from "../api";
import path from 'path';

class ConvertFilesService {
  upload(file, apiKey, onUploadProgress) {
    let formData = new FormData();
    formData.append("file", file);
    formData.append("api-key", apiKey);
  
    return api.convertAPI.post("/upload", formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
      onUploadProgress
    });
  }

  getFiles(path) {
    return api.convertAPI.get(path);
  }

  getFile(fileName) {
    return api.convertAPI.get(path.join("/files", fileName));
  }

  getJsonData(fileName) {
    return api.convertAPI.get(path.join("/files", fileName, "json"));
  }

  getGanttImage(fileName) {
    return api.convertAPI.get(path.join("/files", fileName, "gantt-chart-image"));
  }

  deleteFile(fileName) {
    return api.convertAPI.delete(path.join("/files", fileName, "delete"));
  }

  getTableMpp(fileName) {
    return api.tableImageAPI.get(path.join("/mpp-table-image", fileName));
  }

}

export default new ConvertFilesService();