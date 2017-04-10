export function getZIPData(response) {
      var result = document.createElement('a');
      var contentDisposition = response.headers.get('Content-Disposition') || '';
      var filename = contentDisposition.split('filename=')[1];
      filename = filename.replace(/"/g,"")
      return response.blob()
        .then(function(data) {
          result.href = window.URL.createObjectURL(data);
          result.target = '_self';
          result.download = filename;
          document.body.appendChild(result);

          return result;
        })
}
