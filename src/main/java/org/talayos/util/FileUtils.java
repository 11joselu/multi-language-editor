package org.talayos.util;


import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.talayos.bean.Translate;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Component
@Data
public class FileUtils {
    public static final String PROPERTY_FILE = ".properties";
    public static final String PROPERTY_DIVISOR = "=";
    public static final Charset UTF8 = Charset.forName("UTF-8");

    public FileUtils() {}

    @Autowired
    private TranslateUtils translateUtils;

    /**
     * Validate type file by extension
     * @param fileName
     * @return boolean
     */
    public boolean isPropertyFile(String fileName) {
        return fileName.endsWith(PROPERTY_FILE);
    }

    /**
     * Validate inputs (search for invalid files)
     * @param zp
     * @return boolean
     */
    public boolean hasValidFiles(ZipFile zp) {
        boolean isValid = false;

        if (zp != null) {
            Predicate<String> propertyValidator = fileName -> isPropertyFile(fileName);

            isValid = zp.stream()
                    .map(ZipEntry::getName)
                    .allMatch(propertyValidator);
        }

        return isValid;
    }

    /**
     * Prevent repeated key into Translates list
     * @param translates
     * @param property
     * @return boolean
     */
    private boolean alreadyExistsProperty(List<Translate> translates, String property) {
        boolean exists = false;

        for (Translate translate : translates) {
            if (translate.getNameProperty().equals(property)) {
                exists = true;
                break;
            }
        }

        return exists;
    }

    /**
     * Split file into name and extension
     * @param fileName
     * @return File name
     */
    public String getLanguageFromPropertyFile(String fileName) {

        return fileName.replaceAll(PROPERTY_FILE, "");
    }

    /**
     * Create available languages by Files into a zip
     * @param zp
     * @return List of languages
     */
    public ArrayList<String> availableLanguagesFromZip(ZipFile zp) {
        Object[] names = zp.stream()
                .map(ZipEntry::getName)
                .toArray();

        ArrayList<String> languages = new ArrayList<>(names.length);

        for (Object name : names) {
            String lang = getLanguageFromPropertyFile(name.toString());
            languages.add(lang);
        }

        return languages;
    }

    /**
     * Read zip file
     * @param path
     * @return
     */
    public ZipFile readZip (String path) {
        ZipFile zf = null;
        try {

            zf = new ZipFile(path);

            if (!hasValidFiles(zf)) {
                zf = null;
            }

        } catch (IOException io) {
            io.printStackTrace();
        }

        return zf;
    }

    /**
     * Generate Translate list from Zip file
     * @param path
     * @return
     */
    public List<Translate> generateTranslatesByZip(String path) {
        ZipFile zf;
        List<Translate> result = new ArrayList<>();

        try {

            zf = readZip(path);

            Enumeration entries = zf.entries();
            ArrayList<String> availableLanguages = availableLanguagesFromZip(zf);

            while(entries.hasMoreElements()) {
                ZipEntry ze = (ZipEntry) entries.nextElement();
                BufferedReader br = new BufferedReader(new InputStreamReader(zf.getInputStream(ze)));

                Properties prop = new Properties();
                prop.load(new InputStreamReader(zf.getInputStream(ze), UTF8));

                String lang = getLanguageFromPropertyFile(ze.getName());

                Enumeration<?> e = prop.propertyNames();

                while (e.hasMoreElements()) {
                    String key = (String) e.nextElement();
                    String value = prop.getProperty(key);

                    if (!alreadyExistsProperty(result, key)) {
                        result.add(translateUtils.createTranslateByProperty(key, value, lang, availableLanguages));
                    } else {
                        translateUtils.addValueIntoTranslate(result, key, value, lang);
                    }
                }

                br.close();

            }



        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.sort(result);

        return result;
    }


    /**
     * Write a zip file into path
     * @param baos
     * @param path
     */
    public void writeZipFile(ByteArrayOutputStream baos, String path) {
        try {
            FileOutputStream fos = new FileOutputStream (new File(path));
            baos.writeTo(fos);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
