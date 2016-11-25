package com.example.ehsueh.appygolucky;

import android.app.Application;

import java.io.File;

/**
 * Created by Vinson on 2016-11-24.
 * Clear data programmatically
 * mostly modified from this reference
 * http://stackoverflow.com/questions/6134103/clear-applications-data-programmatically
 */

public class Datacleaner extends Application {
    private static Datacleaner instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Datacleaner getInstance() {
        return instance;
    }

    public void clearApplicationData() {
        File cacheDirectory = getCacheDir();
        File applicationDirectory = new File(cacheDirectory.getParent());
        if (applicationDirectory.exists()) {
            String[] fileNames = applicationDirectory.list();
            for (String fileName : fileNames) {
                if (!fileName.equals("lib")) {
                    deleteFile(new File(applicationDirectory, fileName));
                }
            }
        }
    }

    public static boolean deleteFile(File file) {
        boolean deletedAll = true;
        if (file != null) {
            if (file.isDirectory()) {
                String[] children = file.list();
                for (int i = 0; i < children.length; i++) {
                    deletedAll = deleteFile(new File(file, children[i])) && deletedAll;
                }
            } else {
                deletedAll = file.delete();
            }
        }

        return deletedAll;
    }
}

