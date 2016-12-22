package com.zbd.disklrutest;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static android.view.View.HAPTIC_FEEDBACK_ENABLED;
import static android.view.View.SOUND_EFFECTS_ENABLED;

public class MainActivity extends AppCompatActivity {

    DiskLruCache mDiskLruCache ;
    ImageView imageView;
    //DiskLruCache
    private DiskLruCache mDiskCache;
    //指定磁盘缓存大小
    private static final long DISK_CACHE_SIZE = 1024 * 1024 * 50;//50MB

    String imageUrl = "http://img.my.csdn.net/uploads/201309/01/1378037235_7476.jpg";
    Retr0DiskLruCacheUtil retr0DiskLruCacheUtil;
    int a = SOUND_EFFECTS_ENABLED | HAPTIC_FEEDBACK_ENABLED;
    String TAG = "TEST";






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imgV);
        Retr0DiskLruCacheUtil r = new Retr0DiskLruCacheUtil();
        r.setImageLru(imageView,imageUrl,MainActivity.this);
        Log.e(TAG, "onCreate: "+a);






//        try {
//            File cacheDir = getDiskCacheDir(MainActivity.this, "bitmap");
//            if (!cacheDir.exists()) {
//                cacheDir.mkdirs();
//            }
//            mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(MainActivity.this), 1, 10 * 1024 * 1024);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    String imageUrl = "http://img.my.csdn.net/uploads/201309/01/1378037235_7476.jpg";
//                    String key = hashKeyForDisk(imageUrl);
//                    DiskLruCache.Editor editor = mDiskLruCache.edit(key);
//                    if (editor != null) {
//                        OutputStream outputStream = editor.newOutputStream(0);
//                        if (downloadUrlToStream(imageUrl, outputStream)) {
//                            editor.commit();
//                        } else {
//                            editor.abort();
//                        }
//                    }
//                    mDiskLruCache.flush();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//        try {
//            String imageUrl = "http://img.my.csdn.net/uploads/201309/01/1378037235_7476.jpg";
//            String key = hashKeyForDisk(imageUrl);
//            DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);
//            if (snapShot != null) {
//                InputStream is = snapShot.getInputStream(0);
//                Bitmap bitmap = BitmapFactory.decodeStream(is);
//                imageView.setImageBitmap(bitmap);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }






    }

    /**
     * 获取缓存地址
     * @param context
     * @param uniqueName
     * @return
     */
    public File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    /**\
     * 获取程序版本号
     * @param context
     * @return
     */
    public int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 将字符串进行MD5编码
     * @param key
     * @return
     */
    public String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }


    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(0xFF & aByte);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }




    private boolean downloadUrlToStream (String urlString, OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
            out = new BufferedOutputStream(outputStream, 8 * 1024);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
