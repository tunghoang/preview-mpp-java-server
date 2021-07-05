<template>
  <div>
    <div v-if="fileSelected" class="progress" v-show="uploading">
      <div
        class="progress-bar progress-bar-info progress-bar-striped"
        role="progressbar"
        :aria-valuenow="progress"
        aria-valuemin="0"
        aria-valuemax="100"
        :style="{ width: progress + '%' }"
      >
        {{ progress }}%
      </div>
    </div>

    <div class="alert alert-light" role="alert">{{ message }}</div>

    <div class="card">
      <div class="card-header">List of Files</div>
      <ul class="list-group list-group-flush">
        <li
          class="list-group-item"
          v-for="(file, index) in fileInfos"
          :key="index"
        >
          <a :href="file.url">{{ file.name }} </a>

          <button class="btn btn-success" @click="previewFile(index)">
            Preview
          </button>
          <button class="btn btn-danger" @click="deleteFile(index)">
            Delete
          </button>
        </li>
      </ul>
    </div>
    <div class="renderMPP">
      <div v-if="tableHTML" v-html="tableHTML"></div>

      <div v-if="tableWaiting">
        <div id="loader"></div>
        <h3>Table is loading...</h3>
      </div>
      <div v-if="ganttURL">

        <img :src="ganttURL" alt="GanttChart image" />

      </div>
      <div v-if="ganttWaiting">
        <div id="loader"></div>
        <h3>GanttChart is loading...</h3>
      </div>
    </div>
  </div>
</template>

<script>
import ConvertService from "../services/ConvertFilesService";

export default {
  name: "mpp-previewer",
  components: {},
  props: ["fileSelected"],
  data: function () {
    return {
      fileInfos: [],
      message: "",
      uploading: false,
      progress: 0,
      ganttURL: null,
      tableHTML: null,
      tableWaiting: false,
      ganttWaiting: false,
    };
  },
  created() {
    this.getFileInfos("/files");
  },
  watch: {
    fileSelected: function (file) {
      if (file) {
        this.uploadFile(file, "MY_API_KEY");
      }
    },
  },
  methods: {
    previewFile(index) {
      let fileName = this.fileInfos[index].name;
      this.getTableHTML(fileName);
      this.getGanttURL(fileName);
    },
    deleteFile(index) {
      ConvertService.deleteFile(this.fileInfos[index].name)
        .then((res) => {
          this.setRenderNull();
          this.message = res.data.message;
          this.fileInfos.splice(index, 1);
        })
        .catch((err) => {
          console.log(err);
          this.message = "Delete fail!";
        });
    },
    uploadFile(file, apiKey) {
      ConvertService.upload(file, apiKey, (event) => {
        this.uploading = true;
        this.progress = Math.round((100 * event.loaded) / event.total);
      })
        .then((response) => {
          let fileName = response.data.message;
          this.message = "Upload successfully file: " + fileName;
          return ConvertService.getFile(fileName);
        })
        .then((file) => {
          this.uploading = false;
          this.fileInfos.push(file.data);
        })
        .catch((err) => {
          console.log(err);
          this.progress = 0;
          this.uploading = false;
          this.message = "Could not upload this file!";
        });
    },
    getFileInfos(path) {
      ConvertService.getFiles(path).then((files) => {
        this.fileInfos = files.data;
      }).catch(err => {
        console.log(err);
        this.message = "Server is not ready!"
      });
    },
    getGanttURL(fileName) {
      this.ganttWaiting = true;
      this.ganttURL = false;
      ConvertService.getGanttImage(fileName)
        .then((res) => {
          this.ganttWaiting = false;
          this.ganttURL =
            res.config.baseURL + res.config.url.slice(1, res.config.url.length);
        })
        .catch((err) => {
          console.log(err);
          this.ganttWaiting = false;
          this.message = "Could not preview gantt chart!";
          this.ganttURL = null;
        });
    },
    getTableHTML(fileName) {
      this.tableWaiting = true;
      this.tableHTML = false;
      ConvertService.getTableMpp(fileName)
        .then((res) => {
          this.tableHTML = res.data;
          this.tableWaiting = false;
        })
        .catch((err) => {
          console.log(err);
          this.tableWaiting = false;
          this.message = "Could not preview this file!";
          this.tableHTML = null;
        });
    },
    setRenderNull() {
      this.ganttURL = null;
      this.tableHTML = null;
      this.tableWaiting = false;
      this.ganttWaiting = false;
    }
  },
};
</script>

<style scoped>
.card {
  margin: 0 auto;
  width: 800px;
  justify-content: center;
}

li button {
  float: right;
  margin: 2px;
}

#loader {
  border: 10px solid #f3f3f3;
  border-top: 10px solid #3498db;
  border-radius: 50%;
  width: 60px;
  height: 60px;
  animation: spin 1s linear infinite;
  margin: 20px auto;
}

@keyframes spin {
  0% {transform: rotate(0deg);}
  100% {transform: rotate(360deg);}
}
</style>
