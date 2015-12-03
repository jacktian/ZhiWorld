package com.lb.zhiworld;

import java.io.File;

import org.androidannotations.annotations.EApplication;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.lb.zhiworld.db.SQLHelper;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

@EApplication
public class App extends Application {
	private static App application;
	private static SQLHelper mSqlHelper;

	@Override
	public void onCreate() {
		super.onCreate();
		application = this;
		initImageLoader(application);
	}

	/* 获取Application */
	public static Application getInstance() {
		return application;
	}

	/* 获取数据库Helper */
	public SQLHelper getSQLHelper() {
		if (mSqlHelper == null) {
			mSqlHelper = new SQLHelper(application);
		}
		return mSqlHelper;
	}

	@SuppressWarnings("deprecation")
	private void initImageLoader(Context context) {
		String filePath = Environment.getExternalStorageDirectory()
				+ "/Android/data/" + context.getPackageName() + "/cache/";
		// 获取到缓存的目录地址
		File file = StorageUtils.getOwnCacheDirectory(context, filePath);
		// 创建配置ImageLoader(所有的选项都是可选的)，这个可以设定在Application里面，设置为全局的配置参数
		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(
				context)
				// 线程池内加载的数量
				.threadPoolSize(3)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new WeakMemoryCache())
				.memoryCacheSize(2 * 1024 * 1024)
				// 将保存的时候的URI名称用MD5加密
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// 自定义缓存路径
				.discCache(new UnlimitedDiskCache(file))
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.imageDownloader(
						new BaseImageDownloader(context, 5 * 1000, 30 * 1000))
				.writeDebugLogs().build();
		ImageLoader.getInstance().init(configuration);
	}
}
