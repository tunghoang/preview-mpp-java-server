<template>
  <div> 
    <label class="btn btn-default">
      <input type="file" ref="file" @change="selectFile" />
    </label>

    <button class="btn btn-success" :disabled="!selectedFiles" @click="upload">
      Upload
    </button>

    <div>
      <mpp-previewer :fileSelected="currentFile"/>
    </div>
  </div>
</template>

<script>
import MppPreviewer from "./MppPreviewer.vue"

export default {
  name: "upload-files",
  components: {MppPreviewer},
  data() {
    return {
      selectedFiles: null,
      currentFile: null,
    };
  },
  methods: {
    selectFile() {
      this.selectedFiles = this.$refs.file.files;
    },
    upload() {
      this.currentFile = this.selectedFiles.item(0);
    },
  }
};
</script>