package org.talayos.unit;

import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talayos.bean.Translate;
import org.talayos.util.FileUtils;
import org.talayos.util.TranslateUtils;

import java.io.*;
import java.util.*;
import java.util.zip.ZipFile;

public class FileUtilsTest {

    private FileUtils fileUtils;
    private String ZIP_FILE = "properties.zip";

    private String getPath(String file) {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        String path = classLoader.getResource(file).getPath();

        return path;
    }

    @Before
    public void before(){
        fileUtils = new FileUtils();
        fileUtils.setTranslateUtils(new TranslateUtils());
        fileUtils.getTranslateUtils().setPrefix("openpppmtest");
    }

    @Test
    public void testIsPropertyFile() {
        Assert.assertTrue(fileUtils.isPropertyFile("propertyTest.properties"));
        Assert.assertFalse(fileUtils.isPropertyFile("propertyTest.txt"));
        Assert.assertFalse(fileUtils.isPropertyFile(ZIP_FILE));
    }


    @Test(expected = IOException.class)
    public void testReadZipExceptionHandler() throws IOException, FileUploadBase.InvalidContentTypeException {
        fileUtils.readZip("error.txt");
    }


    @Test(expected = FileUploadBase.InvalidContentTypeException.class)
    public void testHasInvalidFiles() throws IOException, FileUploadBase.InvalidContentTypeException {
        ZipFile zipOne = fileUtils.readZip(getPath(ZIP_FILE));
        Assert.assertTrue(fileUtils.hasValidFiles(zipOne));
        ZipFile zipTwo = fileUtils.readZip(getPath("invalid_zip.zip"));
        Assert.assertFalse(fileUtils.hasValidFiles(zipTwo));
    }

    @Test
    public void testReadZip() throws IOException, FileUploadBase.InvalidContentTypeException {
        ZipFile result = fileUtils.readZip(getPath(ZIP_FILE));
        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() > 0);
        Assert.assertTrue(result.getName().endsWith(ZIP_FILE));
    }


    @Test
    public void testGetLanguageFromPropertyFile() {
        Assert.assertTrue(fileUtils.getLanguageFromPropertyFile("en.properties").equals("en"));
        Assert.assertTrue(fileUtils.getLanguageFromPropertyFile("es.properties").equals("es"));

        Assert.assertTrue(fileUtils.getLanguageFromPropertyFile("openppm-es.properties").equals("openppm-es"));
        Assert.assertTrue(fileUtils.getLanguageFromPropertyFile("test-pre-test.txt").equals("test-pre-test.txt"));
    }

    @Test
    public void testAvailableLanguagesFromZip() throws IOException, FileUploadBase.InvalidContentTypeException {
        ZipFile result = fileUtils.readZip(getPath(ZIP_FILE));
        ArrayList<String> languages = fileUtils.availableLanguagesFromZip(result);
        ArrayList<String> expected = new ArrayList<>();
        expected.add("en");
        expected.add("fr");
        expected.add("es");

        ArrayList<String> unexpected = new ArrayList<>();
        unexpected.add("en");
        unexpected.add("fr");
        unexpected.add("us");

        Assert.assertTrue(languages.equals(expected));
        Assert.assertFalse(languages.equals(unexpected));
    }

    @Test
    public void testReadPropertyFilesInZip() throws IOException, FileUploadBase.InvalidContentTypeException {
        List<Translate> translates = fileUtils.generateTranslatesByZip(getPath(ZIP_FILE));
    }


    @Test
    public void testWriteZip() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        fileUtils.writeZipFile(baos, "outputTest.zip");
    }

    @Test
    public void testWritePropertyFilesIntoZip() throws IOException, FileUploadBase.InvalidContentTypeException {
        List<Translate> translates = fileUtils.generateTranslatesByZip(getPath(ZIP_FILE));

        String[] languages = {"es", "en"};

        ByteArrayOutputStream baos = fileUtils.getTranslateUtils().writeTranslatesIntoZip(languages, translates);

        fileUtils.writeZipFile(baos, "outputTest.zip");
    }
}
