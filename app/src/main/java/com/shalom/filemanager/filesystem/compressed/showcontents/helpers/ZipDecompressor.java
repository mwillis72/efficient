
package com.shalom.filemanager.filesystem.compressed.showcontents.helpers;

import android.content.Context;

import com.shalom.filemanager.adapters.data.CompressedObjectParcelable;
import com.shalom.filemanager.asynchronous.asynctasks.AsyncTaskResult;
import com.shalom.filemanager.asynchronous.asynctasks.compress.ZipHelperTask;
import java.util.ArrayList;

import com.shalom.filemanager.filesystem.compressed.showcontents.Decompressor;
import com.shalom.filemanager.utils.OnAsyncTaskFinished;

public class ZipDecompressor extends Decompressor {

    public ZipDecompressor(Context context) {
        super(context);
    }

    @Override
    public ZipHelperTask changePath(String path, boolean addGoBackItem,
                           OnAsyncTaskFinished<AsyncTaskResult<ArrayList<CompressedObjectParcelable>>> onFinish) {
        return new ZipHelperTask(context, filePath, path, addGoBackItem, onFinish);
    }

}
