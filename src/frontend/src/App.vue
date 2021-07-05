<template>
  <img alt="Vue logo" src="./assets/logo.png">
  <div id="app">
    <div>
      <div style="margin: 20px">
        <h4>Upload Files MPP</h4>
      </div>
      <upload-files />
    </div>
  </div>
</template>

<script>
import UploadFiles from './components/UploadFiles'

export default {
  name: 'App',
  components: {
    UploadFiles
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
