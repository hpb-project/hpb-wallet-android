package com.zhaoxi.Open_source_Android.libs.utils;

import android.os.Environment;
import android.util.Log;

import com.zhaoxi.Open_source_Android.DAppConstants;

import java.io.File;
import java.io.RandomAccessFile;

import static com.zhaoxi.Open_source_Android.DAppConstants.PATH_IMAGE_CACHE;

/**
 * des:
 * Created by ztt on 2018/7/30.
 */

public class FileManager {

    public static boolean hasSDCard() {
        String status = Environment.getExternalStorageState();
        if (!status.equals(Environment.MEDIA_MOUNTED)) {
            return false;
        }
        return true;
    }

    public static String getRootFilePath() {
        if (hasSDCard()) {
            return Environment.getExternalStorageDirectory().getPath();// filePath:/sdcard/
        } else {
            return Environment.getDataDirectory().getAbsolutePath() + "/data"; // filePath:																		// /data/data/
        }
    }

    /* 图片压缩 */
    public static String compressImagePath(String name) {
        String path;
        if (FileManager.hasSDCard()) {
            path = DAppConstants.SD_CARD_PATH + PATH_IMAGE_CACHE + name;
        } else {
            path = DAppConstants.DATA_DIRECTORY_PATH + PATH_IMAGE_CACHE + name;
        }
        return path;
    }

    public static String createPic(String Apath) {
        String path;
        if (FileManager.hasSDCard()) {
            path = DAppConstants.SD_CARD_PATH + Apath;
        } else {
            path = DAppConstants.DATA_DIRECTORY_PATH + Apath;
        }
        return path;
    }

    /**
     * 判断某文件是否存在
     *
     * @param fileName 文件名
     * @return 返回true，否则返回false
     */

    public static boolean checkFileExists(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * 删除文件，可以是文件或文件夹
     *
     * @param fileName 要删除的文件名
     * @return 删除成功返回true，否则返回false
     */

    public static boolean deleteComFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile())
                return deleteFile(fileName);
        }
        return false;
    }


    /**
     * 删除单个文件
     *
     * @param fileName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */

    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    // 将字符串写入到文本文件中
    public static boolean writeTxtToFile(String strcontent, String filePath, String fileName) {
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName);
        String strFilePath = filePath + fileName;
        // 每次写入时，都换行写
        String strContent = strcontent + "\r\n";
        try {
            File file = new File(strFilePath);
            if(file.exists()){
                deleteFile(strFilePath);
            }
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
            return true;
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
            return false;
        }
    }

    //生成文件
    public static File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    //生成文件夹
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.i("error:", e + "");
        }
    }
}
