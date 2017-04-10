<template>
  <div class="uploader">
    <div v-if="isLoading">
       <loading :message="'Uploading... ' + filename "></loading>
    </div>


   <div class="uploader text-center" v-if="!isLoading">
     <h2>Upload <span>ZIP</span> file</h2>
     <p class="lead"></p>

     <form  @submit.prevent="sendZipfile" role="form"  enctype="multipart/form-data" class="uploader--form">

      <label for="exampleFileUpload">

        <div>
         <i class="fa fa-upload fa-3x uploader--form--icon" aria-hidden="true"></i>
        </div>

        <span class="button">Select a file...</span>
      </label>
      <input type="file" id="exampleFileUpload" class="show-for-sr" @change="onFileChange" accept=".zip">

      <div v-if="error">
        Please select and <strong>ZIP</strong> file...
      </div>

      <div v-if="filename">
        <p class="uploader--form--selection">Selection: <strong>{{ filename }}</strong></p>
      </div>

      <div>
        <button v-if="filename" :disabled="!filename" role="submit" class="button success">Upload File</button>
      </div>
    </form>
   </div>

  </div>


</template>

<script>
import Loading from './Loading'

export default {
  name: 'uploader',
  components: {
    Loading
  },
  data () {
    return {
      filename: '',
      isLoading: false,
      error: false
    }
  },
  methods: {
    onFileChange (e) {
      var files = e.target.files || e.dataTransfer.files;
      if (files[0]) {
        this.filename = files[0].name
        var ext = this.getFileExtension(this.filename);

        if (ext[0] != 'zip') {
          this.filename = ''
          this.error = true
        } else {
          this.error = false
        }
      }
    },

    getFileExtension (filename) {
      return (/[.]/.exec(filename)) ? /[^.]+$/.exec(filename) : undefined;
    },

    sendZipfile (e) {
      var data = new FormData();
      var files = e.target[0].files || e.dataTransfer[0].files;
      this.isLoading = true;

      data.append('file', files[0]);

      var opts = {
        headers: {
          'Content-Type': 'multipart/form-data',
        }
      }

      this.$http.post('/upload', data, opts)
        .then( (response) => {
          this.isLoading = false;
          this.$router.push({name: "properties", params:  {properties: response.data.data }});
        })
        .catch((err) => {
          this.isLoading = false;
          throw err;
        })
    }
  }
}
</script>

<style>
.uploader {
  position: absolute;
  width: 100%;
  top: 50%;
  transform: translateY(50%);
  height: 100%;
}

.uploader h2 {
  color: #5f6982;
}

.uploader--form {
  max-width: 500px;
  margin: 0 auto;
}

.uploader--form--icon {
  margin-bottom: .4em;
  color: #5f6982;
  cursor: pointer;
}

.uploader--form--selection {
  color: #9a9a9a;
}

</style>
