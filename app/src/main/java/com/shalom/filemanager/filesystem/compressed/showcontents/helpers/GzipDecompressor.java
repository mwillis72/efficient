

package com.shalom.filemanager.filesystem.compressed.showcontents.helpers;

import android.content.Context;

import com.shalom.filemanager.adapters.data.CompressedObjectParcelable;
import com.shalom.filemanager.asynchronous.asynctasks.AsyncTaskResult;
import com.shalom.filemanager.asynchronous.asynctasks.compress.CompressedHelperTask;
import com.shalom.filemanager.asynchronous.asynctasks.compress.GzipHelperTask;
import com.shalom.filemanager.filesystem.compressed.showcontents.Decompressor;
import com.shalom.filemanager.utils.OnAsyncTaskFinished;

import java.util.ArrayList;

public class GzipDecompressor extends Decompressor {

    public GzipDecompressor(Context context) {
        super(context);
    }

    @Override
    public CompressedHelperTask changePath(String path, boolean addGoBackItem,
                                           OnAsyncTaskFinished<AsyncTaskResult<ArrayList<CompressedObjectParcelable>>> onFinish) {
        return new GzipHelperTask(context, filePath, path, addGoBackItem, onFinish);
    }

}
