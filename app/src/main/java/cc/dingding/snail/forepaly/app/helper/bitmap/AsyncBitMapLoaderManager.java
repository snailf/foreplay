package cc.dingding.snail.forepaly.app.helper.bitmap;

import android.util.Log;

import java.util.HashSet;
import java.util.Set;

import cc.dingding.snail.forepaly.app.helper.bitmap.listener.ImageDownloadListener;

/**
 * 异步加载图片频次控制器
 * 避免oom
 * Created by koudejian on 14-2-20.
 */
public class AsyncBitMapLoaderManager {
    private final String TAG = "AsyncBitMapLoaderManager";
    private static AsyncBitMapLoaderManager INSTANCE = null;
    /**
     * 记录所有正在下载或等待下载的任务。
     */
    private Set<DownloadImageTask> mTaskList = null;

    private ImageDownloadListener mDownloadListener = new ImageDownloadListener() {
        @Override
        public void OnSuccessed(DownloadImageTask finishedTask) {
            remove(finishedTask);
        }
    };

    private AsyncBitMapLoaderManager(){
        mTaskList = new HashSet<DownloadImageTask>();
    }

    /**
     * 移除任务
     * @param downloadImageTask
     */
    private void remove(DownloadImageTask downloadImageTask){
        mTaskList.remove(downloadImageTask);
        Log.i(TAG, "remove one task:" + downloadImageTask.toString());
    }

    public static AsyncBitMapLoaderManager getInstance(){
        if(INSTANCE == null){
            INSTANCE = new AsyncBitMapLoaderManager();
        }
        return INSTANCE;
    }

    /**
     * 添加任务并自动开始
     * @param downloadImageTask
     */
    public void add(DownloadImageTask downloadImageTask){
        mTaskList.add(downloadImageTask);
        downloadImageTask.setImageDownloadListener(mDownloadListener);
        downloadImageTask.execute();
        Log.i(TAG, "add one task:" + downloadImageTask.toString());
    }

    /**
     * 取消所有正在下载或等待下载的任务。
     */
    public void clear(){
        if (mTaskList != null) {
//            for(DownloadImageTask task : mTaskList){
//                task.cancel(false);
//            }
            mTaskList.clear();
        }
    }
}
