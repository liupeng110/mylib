package com.andlib.lp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.andlib.lp.util.L;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.xutils.DbManager;
import org.xutils.x;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/** 717219917@qq.com  2017/6/6 14:32 */
public class App extends Application {

    private  static List<Activity> activityList = new LinkedList<Activity>();
    public static DbManager.DaoConfig daoConfig;// xutils数据库配置
    public static DbManager db;
    public boolean isCash = true;//是否开启崩溃日志捕捉. true开启 . false关闭

    @Override  public void onCreate() {
        super.onCreate();
        L.i("进入应用切入点....");
        x.Ext.init(this);     // 初始化xutils3
        x.Ext.setDebug(true); // 输出debug日志

        if (isCash) {// 进行捕捉崩溃日志
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(getApplicationContext());
        }
        initDatabase();        // 进行配置数据库
        initImageLoader(this); // imageloader初始化
    }
    public static void addActivity(Activity activity) {
        activityList.add(activity);
    }
    public static void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }   //退出
    public void initDatabase() {
        daoConfig = new DbManager.DaoConfig()
                .setDbName("andlp.db")
                // 不设置dbDir时, 默认存储在app的私有目录.
//                 .setDbDir(new File("/sdcard"))
                // .setDbDir(newFile(Environment.getExternalStorageDirectory().getPath()))
                .setDbVersion(1)
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) { // 开启WAL, 对写入加速提升巨大
                        db.getDatabase().enableWriteAheadLogging();
                    }
                }).setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion,
                                          int newVersion) {
                        // db.addColumn(...);//更新更新添加字段
                        // db.dropTable(...);
                        // db.dropDb();
                    }
                });
        db=x.getDb(daoConfig);
    }  //初始化数据库
    @SuppressWarnings({ "deprecation", "static-access" }) //初始化imageloader
    public static ImageLoader initImageLoader(Context context) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options=new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.welcome)//设置图片下载期间显示的图片
//                .showImageForEmptyUri(R.drawable.video_kuang)//设置图片uri为空或是错误的时候显示的图片
//                .showImageOnFail(R.drawable.video_kuang)//设置图片加载或解码过程中发生错误显示的图片
                .resetViewBeforeLoading(false)//设置图片在加载前是否重置、复位
//               .delayBeforeLoading(1000)//下载前的延迟时间
                .cacheInMemory(false)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(false)  //设置下载的图片是否缓存在sd卡中
                .considerExifParams(true) //可交换的参数
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)//设置图片的显示比例
                .bitmapConfig(Bitmap.Config.RGB_565)    //设置图片的解码类型
                .displayer(new RoundedBitmapDisplayer(0))//设置图片的圆角半径
                .displayer(new FadeInBitmapDisplayer(0)) //设置图片显示的透明度过程的时间

                .build();

        File cacheDir = StorageUtils.getOwnCacheDirectory(context,"andlp/Cache");// 缓存地址

        ImageLoaderConfiguration config = (new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .threadPoolSize(4).denyCacheImageMultipleSizesInMemory()
                .memoryCache(new WeakMemoryCache())
                .memoryCacheSize(1024 * 1024)//1M    1024 * 1024
                .diskCacheSize(50 * 1024 * 1024)

                .memoryCacheExtraOptions(480, 800)//内存缓存文件的最大长宽
                .discCacheExtraOptions(480, 800, null)//本地缓存的详细信息
                .taskExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
                .taskExecutorForCachedImages(AsyncTask.THREAD_POOL_EXECUTOR)

                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .discCache(new UnlimitedDiskCache(cacheDir))
                .memoryCacheSizePercentage(40)           //内存缓存百分比
                .imageDecoder(new BaseImageDecoder(true))//图片解码
                .denyCacheImageMultipleSizesInMemory()   //防止内存中图片重复

                .tasksProcessingOrder(QueueProcessingType.LIFO))
                .defaultDisplayImageOptions(options).build();
        imageLoader.getInstance().init(config);
        return imageLoader;

    }


}