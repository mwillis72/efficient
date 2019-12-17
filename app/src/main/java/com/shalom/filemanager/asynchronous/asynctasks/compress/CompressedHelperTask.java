

package com.shalom.filemanager.asynchronous.asynctasks.compress;

import android.os.AsyncTask;
import androidx.annotation.NonNull;

import com.shalom.filemanager.adapters.data.CompressedObjectParcelable;
import com.shalom.filemanager.asynchronous.asynctasks.AsyncTaskResult;
import com.shalom.filemanager.utils.OnAsyncTaskFinished;

import org.apache.commons.compress.archivers.ArchiveException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public abstract class CompressedHelperTask extends AsyncTask<Void, IOException, AsyncTaskResult<ArrayList<CompressedObjectParcelable>>> {

    private boolean createBackItem;
    private OnAsyncTaskFinished<AsyncTaskResult<ArrayList<CompressedObjectParcelable>>> onFinish;

    CompressedHelperTask(boolean goBack, OnAsyncTaskFinished<AsyncTaskResult<ArrayList<CompressedObjectParcelable>>> l) {
        createBackItem = goBack;
        onFinish = l;
    }

    @Override
    protected final AsyncTaskResult<ArrayList<CompressedObjectParcelable>> doInBackground(Void... voids) {
        AsyncTaskResult<ArrayList<CompressedObjectParcelable>> result = null;
        ArrayList<CompressedObjectParcelable> elements = new ArrayList<>();
        if (createBackItem) elements.add(0, new CompressedObjectParcelable());

        try {
            addElements(elements);
            Collections.sort(elements, new CompressedObjectParcelable.Sorter());

            return new AsyncTaskResult<>(elements);
        } catch (ArchiveException ifArchiveIsCorruptOrInvalid) {
            return new AsyncTaskResult<>(ifArchiveIsCorruptOrInvalid);
        }
    }

    @Override
    protected final void onPostExecute(AsyncTaskResult<ArrayList<CompressedObjectParcelable>> zipEntries) {
        super.onPostExecute(zipEntries);
        onFinish.onAsyncTaskFinished(zipEntries);
    }

    abstract void addElements(@NonNull ArrayList<CompressedObjectParcelable> elements) throws ArchiveException;

}
