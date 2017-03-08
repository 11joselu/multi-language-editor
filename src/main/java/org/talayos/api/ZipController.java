package org.talayos.api;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
public class ZipController {
    private static String UPLOADED_FOLDER = System.getProperty("java.io.tmpdir");
    private String filePath = "";
    @Autowired
    private FileUtils fileUtils;

    @PostMapping("/upload")
    public List<Translate> singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        List<Translate> translates = null;

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");

            return translates;
        }

        try {
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            filePath = UPLOADED_FOLDER + "/" + file.getOriginalFilename();
            Path path = Paths.get(filePath);
            Files.write(path, bytes);
            translates = fileUtils.generateTranslatesByZip(filePath);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return translates;
    }

    @PostMapping("/generate")
    public void test(@RequestBody ArrayList<Translate> translates, @RequestParam("languages") String languages, HttpServletResponse response) throws IOException {
        String[] langs = StringEscapeUtils.escapeJava(languages).split(",");
        Collections.sort(translates);
        ByteArrayOutputStream baos = fileUtils.getTranslateUtils().writeTranslatesIntoZip(langs, translates);

        Date date = new Date() ;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm") ;
        String filename = "generated-" + dateFormat.format(date) +".zip";

        // the response variable is just a standard HttpServletResponse
        response.setHeader("Content-Disposition","attachment; filename=\"" + filename + "\"");
        response.setContentType("application/zip");

        try{
            response.getOutputStream().write(baos.toByteArray());
            response.flushBuffer();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally{
            baos.close();
        }
    }


}
