
package com.shalom.filemanager.filesystem.compressed.showcontents.helpers;

import android.content.Context;
import androidx.annotation.NonNull;

import com.shalom.filemanager.adapters.data.CompressedObjectParcelable;
import com.shalom.filemanager.asynchronous.asynctasks.AsyncTaskResult;
import com.shalom.filemanager.asynchronous.asynctasks.compress.SevenZipHelperTask;
import com.shalom.filemanager.filesystem.compressed.showcontents.Decompressor;
import com.shalom.filemanager.utils.OnAsyncTaskFinished;

import java.util.ArrayList;

public class SevenZipDecompressor extends Decompressor {

    public SevenZipDecompressor(@NonNull Context context) {
        super(context);
    }

    @Override
    public SevenZipHelperTask changePath(String path, boolean addGoBackItem,
                                       OnAsyncTaskFinished<AsyncTaskResult<ArrayList<CompressedObjectParcelable>>> onFinish) {
        return new SevenZipHelperTask(filePath, path, addGoBackItem, onFinish);
    }
}
