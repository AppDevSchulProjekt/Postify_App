package com.appdev.postify.datastorage;

import android.os.Environment;
import android.util.Log;

import com.appdev.postify.BaseApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Soere on 26.04.2016.
 */
public class SDCardManager {
    public static boolean trySaveFile(String filename, String content){
        //prüfen ob SD Karte eingelegt und bool zurückgeben
        boolean result = false;
        FileOutputStream fileOutputStream;
        File root;

        //if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
        root = BaseApplication.getInstance().getExternalFilesDir(null);
        Log.d("SDTest1", "True:");
        Log.d("SDTest1", "BaseApplication.getInstance().getExternalFilesDir(null): " + root.getPath());
        Log.d("SDTest1", "Environment.getExternalStorageDirectory().getAbsolutePath(): " + Environment.getExternalStorageDirectory().getAbsolutePath() );
        Log.d("SDTest1", "System.getenv(SECONDARY_STORAGE): " + System.getenv("SECONDARY_STORAGE"));
        Log.d("SDTest1", "System.getenv(EXTERNAL_STORAGE): " + System.getenv("EXTERNAL_STORAGE"));
        Log.d("SDTest1", "System.getenv(EXTERNAL_SDCARD_STORAGE);: " + System.getenv("EXTERNAL_SDCARD_STORAGE"));
        root = new File(System.getenv("SECONDARY_STORAGE"));
        //}else {
        //    root = BaseApplication.getInstance().getFilesDir();
        //    Log.d("SDTest1", "False:");
        //    Log.d("SDTest1", "File2" + root.getPath());
        //}
        result = true;

        File file = new File(root, filename);
        Log.d("SDTest1", "MyFile" + file.getPath());
        if (result) {
            byte[] contentBytes = content.getBytes();

            try {
                if(!file.exists()) {
                    file.createNewFile();
                    Log.d("SDTest1", "Datei existiert nicht");
                }

                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(contentBytes);
                fileOutputStream.flush();
                fileOutputStream.close();

            } catch (FileNotFoundException e) {
                Log.d("SDTest","FileNotFound");
            } catch (IOException e) {
                // handle exception
            }
        }

        return result;
    }
}
