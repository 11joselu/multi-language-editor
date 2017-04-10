export function getZIPData(response) {
      var result = document.createElement('a');
      var contentDisposition = response.headers.get('Content-Disposition') || '';
      var filename = contentDisposition.split('filename=')[1];
      filename = filename.replace(/"/g,"")
      return response.blob()
        .then(function(data) {
          var binaryData = [];
          binaryData.push(data);
          result.href = window.URL.createObjectURL(new Blob(binaryData, {type: "application/zip"}));
          result.target = '_self';
          result.download = filename;
          document.body.appendChild(result);

          return result;
        })
}
