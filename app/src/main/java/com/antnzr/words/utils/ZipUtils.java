package com.antnzr.words.utils;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtils {

    private static String TAG = ZipUtils.class.getSimpleName();
    private static final int BUFFER_SIZE = 8192;//2048;

    public static Boolean unzip(File sourceFile, String destDir) {

        try (ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(sourceFile)))) {
            ZipEntry ze;
            int count;

            byte[] buffer = new byte[BUFFER_SIZE];
            while ((ze = zis.getNextEntry()) != null) {
                if (ze.isDirectory()) {
                    continue;
                }

                String fileName = ze.getName();
                fileName = fileName.substring(fileName.indexOf("/") + 1);
                File file = new File(destDir, fileName);
                File dir = ze.isDirectory() ? file : file.getParentFile();

                if (!dir.isDirectory() && !dir.mkdirs()) throw new FileNotFoundException("Invalid path: " + dir.getAbsolutePath());

                try (FileOutputStream out = new FileOutputStream(file)) {
                    while ((count = zis.read(buffer)) != -1)
                        out.write(buffer, 0, count);
                }
            }
        } catch (IOException exception) {
            Log.d(TAG, exception.getMessage());
            return false;
        } finally {
            sourceFile.delete();
        }

        return true;
    }
}
