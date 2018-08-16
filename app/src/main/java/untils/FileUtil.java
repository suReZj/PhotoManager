package untils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.nanchen.compresshelper.CompressHelper;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bean.ArrangementAlbum;
import bean.Photo;
import bean.SortPhoto;
import event.RefreshData;

public class FileUtil {


    //将图片的InputStream转化为本地图片
    public static void catchStreamToFile(final List<String> list, final List<String> buildFile, final Context context) throws IOException {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
        for (int j = 0; j < buildFile.size(); j++) {
            for (int i = 0; i < list.size(); i++) {
                String url = list.get(i);
                final Bitmap bitmap = CompressHelper.getDefault(context).compressToBitmap(new File(url));
//                final Bitmap bitmap = BitmapFactory.decodeFile(url);
                String path = url;//原地址
                int index = url.lastIndexOf("/");
                url = url.substring(index + 1, url.length());
                String savePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/photoManager/" + "/" + buildFile + "/" + url;

                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/photoManager/" + "/" + buildFile + "/" + url);

                file.getParentFile().mkdirs();

//            Photo photo = LitePal.where("mLocalPath = ?", path).find(Photo.class).get(0);
//            Photo newPhoto=new Photo(savePath,photo.getmSize(),photo.getmDisplayName(),photo.getmAlbumName(),photo.getmDate(),photo.getmLongitude(),photo.getmLatitude());
                Photo newPhoto = new Photo();
                newPhoto.setmLocalPath(savePath);
                newPhoto.updateAll("mLocalPath = ?", path);


                List<ArrangementAlbum> mList = LitePal.where("name = ?", buildFile.get(j)).find(ArrangementAlbum.class);
                if (mList.size() == 0) {
                    ArrangementAlbum album = new ArrangementAlbum();
                    album.setName(buildFile.get(j));
                    album.setSum(1);
                    List<String> photoList = new ArrayList<>();
//                    Photo photo = LitePal.where("mLocalPath = ?", savePath).find(Photo.class).get(0);
                    photoList.add(savePath);
                    album.setmList(photoList);
                    album.save();
                } else {
                    ArrangementAlbum album = new ArrangementAlbum();
                    List<String> list1 = mList.get(0).getmList();
                    List<String> list2 = new ArrayList<>();
                    list2.add(savePath);
                    list2.addAll(list1);
//                        list1.add(savePath);
//                        album.setmList(list1);
                    album.setmList(list2);
                    album.setSum(mList.get(0).getSum() + 1);
                    album.updateAll("name = ?", buildFile.get(j));
                }


                EventBus.getDefault().post(new RefreshData());

                try {
                    final FileOutputStream fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    // 最后通知图库更新
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + savePath)));
                }
            }
        }

        for (int y = 0; y < list.size(); y++) {
            Uri tempUri;
            File mfile = new File(list.get(y));
            if (Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
                //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                tempUri = FileProvider.getUriForFile(context, "包名", mfile);
//添加这一句表示对目标应用临时授权该Uri所代表的文件
            } else {
                tempUri = Uri.fromFile(mfile);
            }
            deleteImage(list.get(y), tempUri, context);
        }

//            }
//        }).run();

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
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }

    /**
     * delete image
     *
     * @param fileName filename
     * @param fileUri  file uri
     */
    private static void deleteImage(String fileName, Uri fileUri, Context context) {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver mContentResolver = context.getContentResolver();
            String where = MediaStore.Images.Media.DATA + "='" + fileName + "'";
            // 删除操作
            mContentResolver.delete(uri, where, null);
            //发送广播
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(fileUri);
            context.sendBroadcast(intent);
        }
    }


    public static void privateImage(List<String> list, Context context) {
        for (int y = 0; y < list.size(); y++) {
            Uri tempUri;
            File mfile = new File(list.get(y));
            if (Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
                //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                tempUri = FileProvider.getUriForFile(context, "包名", mfile);
//添加这一句表示对目标应用临时授权该Uri所代表的文件
            } else {
                tempUri = Uri.fromFile(mfile);
            }
            deleteImage(list.get(y), tempUri, context);
        }
        EventBus.getDefault().post(new RefreshData("lock"));
    }


    public static void deletePhoto(List<String> list, List<String> albumName, Context context) {
        for (int i = 0; i < albumName.size(); i++) {
            for (int j = 0; j < list.size(); j++) {

                Photo photo = LitePal.where("mLocalPath = ?", list.get(j)).find(Photo.class).get(0);
                photo.setmIsSort(true);
                List<String> name = photo.getSortName();
                if (name == null) {
                    name = new ArrayList<>();
                }
                name.add(albumName.get(i));
                photo.setSortName(name);
                photo.updateAll("mLocalPath = ?", list.get(j));


                List<ArrangementAlbum> mList = LitePal.where("name = ?", albumName.get(i)).find(ArrangementAlbum.class);
                if (mList.size() == 0) {
                    ArrangementAlbum album = new ArrangementAlbum();
                    album.setName(albumName.get(i));
                    album.setSum(1);
                    List<String> photoList = new ArrayList<>();
                    photoList.add(list.get(j));
                    album.setmList(photoList);
                    album.save();
                } else {
                    ArrangementAlbum album = new ArrangementAlbum();
                    List<String> list3 = mList.get(0).getmList();
                    List<String> list4 = new ArrayList<>();
                    list4.add(list.get(j));
                    list4.addAll(list3);
                    album.setmList(list4);
                    album.setSum(mList.get(0).getSum() + 1);
                    album.updateAll("name = ?", albumName.get(i));
                }
            }
        }
        EventBus.getDefault().post(new RefreshData("delete"));
    }

    public static void sortPhoto(List<String> list, List<String> albumName, Context context) {
        for (int i = 0; i < albumName.size(); i++) {
            for (int j = 0; j < list.size(); j++) {

                Photo photo = LitePal.where("mLocalPath = ?", list.get(j)).find(Photo.class).get(0);
                photo.setmIsSort(true);
                List<String> name = photo.getSortName();
                if (name == null) {
                    name = new ArrayList<>();
                }
                name.add(albumName.get(i));
                photo.setSortName(name);
                photo.updateAll("mLocalPath = ?", list.get(j));


                List<ArrangementAlbum> mList = LitePal.where("name = ?", albumName.get(i)).find(ArrangementAlbum.class);
                if (mList.size() == 0) {
                    ArrangementAlbum album = new ArrangementAlbum();
                    album.setName(albumName.get(i));
                    album.setSum(1);
                    List<String> photoList = new ArrayList<>();
                    photoList.add(list.get(j));
                    album.setmList(photoList);
                    album.save();
                } else {
                    ArrangementAlbum album = new ArrangementAlbum();
                    List<String> list3 = mList.get(0).getmList();
                    List<String> list4 = new ArrayList<>();
                    list4.add(list.get(j));
                    list4.addAll(list3);
                    album.setmList(list4);
                    album.setSum(mList.get(0).getSum() + 1);
                    album.updateAll("name = ?", albumName.get(i));
                }
            }
        }
        EventBus.getDefault().post(new RefreshData("sort"));
    }

}
