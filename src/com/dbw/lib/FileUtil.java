package com.dbw.lib;

import java.io.*;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.logging.Logger;

public class FileUtil {
    private static final Logger log = Logger.getLogger("FileUtil");
    private static final int BUFSIZE = 4096;

    static public void copyStringToFile(String buf, String destFile) throws IOException {
        prepareDest(destFile);
        InputStream in = new ByteArrayInputStream(buf.getBytes());
        OutputStream out = new FileOutputStream(destFile);
        copyStream(in, out);
    }

    static public void copyBytesToFile(byte[] buf, String destFile) throws IOException {
        prepareDest(destFile);
        InputStream in = new ByteArrayInputStream(buf);
        OutputStream out = new FileOutputStream(destFile);
        copyStream(in, out);
    }

    static public void copyStream(InputStream in, OutputStream out) throws IOException {
        int byteCount;
        byte[] buffer = new byte[BUFSIZE];
        try (BufferedInputStream from = new BufferedInputStream(in);
             BufferedOutputStream to = new BufferedOutputStream(out)) {
            while ((byteCount = from.read(buffer)) >= 0) {
                to.write(buffer, 0, byteCount);
            }
        }
    }

    static public void writeFile(String buf, java.io.File destFile) throws IOException {
        prepareDest(destFile);
        ByteArrayInputStream from = new ByteArrayInputStream(buf.getBytes());
        FileOutputStream to = new FileOutputStream(destFile);
        copyStream(from, to);
    }

    static private void prepareDest(String destPath) {
        java.io.File destFile = new java.io.File(destPath);
        prepareDest(destFile);
    }

    static private void prepareDest(java.io.File destFile) {
        if (destFile.exists()) {
            destFile.delete();
        } else {
            createDirectory(destFile.getParentFile());
        }
    }

    static public void createDirectory(java.io.File dir) {
        if (dir == null) return;
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static String readFileAsString(String filePath) {
        StringBuilder fileData = new StringBuilder(BUFSIZE);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            char[] buf = new char[BUFSIZE];
            int numRead;
            while ((numRead = reader.read(buf)) != -1) {
                fileData.append(buf, 0, numRead);
            }
        } catch (Exception e) {
            log.severe(e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    // do nothing.
                }
            }
        }
        return fileData.toString();
    }

    public static void toFile(List<Double> centers, String filename) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);
        StringBuilder builder = new StringBuilder();
        for(double center: centers) {
            builder.append(df.format(center));
            builder.append(",");
        }
        builder.deleteCharAt(builder.length()-1);
        try {
            FileUtil.writeFile(builder.toString(), new File(filename));
            System.out.println("Results written to: " + filename);
        } catch (Exception ex) {
            System.err.println("Unable to write output file: " + filename);
        }
    }
}
