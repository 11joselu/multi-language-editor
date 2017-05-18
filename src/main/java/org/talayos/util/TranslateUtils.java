package org.talayos.util;

import lombok.Data;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.talayos.bean.Translate;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
@Data
public class TranslateUtils {

    @Value("${multi.language.editor.prefix}")
    public String prefix;

    /**
     * Convert international string into unicode string
     * @param text
     * @return Scaped string
     */
    public String stringToUnicode(String text) {
        return StringEscapeUtils.escapeJava(text);
    }

    /**
     * Search for a translate in list by property key
     * @param translates
     * @param key
     * @return Translate object
     */
    public Translate getTranslateByPropertyKey(List<Translate> translates, String key) {
        Translate result = null;
        for (Translate translate : translates) {
            if (translate.getNameProperty().equals(key)) {
                result = translate;
                break;
            }
        }

        return result;
    }

    public ByteArrayOutputStream writeTranslatesIntoZip(String[] languages, List<Translate> translates) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String separator = System.lineSeparator();

        try (ZipOutputStream zos = new ZipOutputStream(baos, FileUtils.UTF8)) {
        for (String lang : languages) {
            String filename;

            if (lang.toLowerCase().contains(prefix)) {
                filename = lang + FileUtils.PROPERTY_FILE;
            } else {
                filename = prefix + "_" + lang.toLowerCase() + FileUtils.PROPERTY_FILE;
            }

            ZipEntry entry = new ZipEntry(filename);
            zos.putNextEntry(entry);

            for (Translate translate : translates) {
                String outputText = stringToUnicode(translate.getNameProperty()) + FileUtils.PROPERTY_DIVISOR + stringToUnicode(translate.getLanguages().get(lang).trim()) + "\n";
                zos.write(outputText.replace("\\n", separator).getBytes());
            }

            zos.closeEntry();
        }
        } catch (IOException ioe) {
            throw ioe;
        }

        return baos;
    }

    /**
     * Add property into translate list
     * @param translates
     * @param key
     * @param value
     * @param lang
     */
    public void addValueIntoTranslate(List<Translate> translates, String key, String value, String lang) {
        Translate translate = getTranslateByPropertyKey(translates, key);
        if (translate != null) {
            translate.getLanguages().put(lang, value);
        }
    }


    /**
     * Create new translate by property key and value
     * @param key
     * @param value
     * @param lang
     * @param arrLanguages
     * @return
     */
    public Translate createTranslateByProperty(String key, String value, String lang, ArrayList arrLanguages) {
        Map<String,String> languages = new HashMap<>();

        for (Object language : arrLanguages) {
            languages.put(language.toString(), "");
        }

        // Replace and empty value for a new value
        languages.put(lang, value);

        Translate translate = new Translate();
        translate.setNameProperty(key);
        translate.setLanguages(languages);

        return translate;
    }
}
