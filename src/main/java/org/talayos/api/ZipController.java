package org.talayos.api;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.talayos.api.entities.ZipEntity;
import org.talayos.bean.Translate;
import org.talayos.util.FileUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "properties")
public class ZipController {
    private static String UPLOADED_FOLDER = System.getProperty("java.io.tmpdir");
    private String filePath = "";
    @Autowired
    private FileUtils fileUtils;

    @PostMapping("/upload")
    public ResponseEntity<ZipEntity> singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) throws IOException, FileUploadBase.InvalidContentTypeException, InvalidMediaTypeException {

        List<Translate> translates = null;

        if (file.isEmpty()) {
            throw new FileUploadBase.FileUploadIOException();
        }

        byte[] bytes = file.getBytes();
        filePath = UPLOADED_FOLDER + "/" + file.getOriginalFilename();
        Path path = Paths.get(filePath);
        Files.write(path, bytes);
        translates = fileUtils.generateTranslatesByZip(filePath);

        ZipEntity zipEntity = new ZipEntity();
        zipEntity.setMessage("");
        zipEntity.setData(translates);
        zipEntity.setStatusCode(HttpStatus.OK.value());

        ResponseEntity<ZipEntity> responseEntity = new ResponseEntity<>(zipEntity,
                HttpStatus.OK);


        return responseEntity;
    }

    @PostMapping("/generate")
    public void generateZipFile(@RequestBody ArrayList<Translate> translates, @RequestParam("languages") String languages, HttpServletResponse response) throws IOException {
        String[] langs = StringEscapeUtils.escapeJava(languages).split(",");
        Collections.sort(translates);
        ByteArrayOutputStream baos = fileUtils.getTranslateUtils().writeTranslatesIntoZip(langs, translates);

        Date date = new Date() ;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm") ;
        String filename = "generated-" + dateFormat.format(date) +".zip";

        // the response variable is just a standard HttpServletResponse
        response.setHeader("Content-Disposition","attachment; filename=\"" + filename + "\"");
        response.setContentType("application/zip");

        response.getOutputStream().write(baos.toByteArray());
        response.flushBuffer();
        baos.close();
    }


}
