
package com.shalom.filemanager.asynchronous.asynctasks.compress;

import android.content.Context;
import androidx.annotation.NonNull;

import com.shalom.filemanager.R;
import com.shalom.filemanager.adapters.data.CompressedObjectParcelable;
import com.shalom.filemanager.asynchronous.asynctasks.AsyncTaskResult;
import com.shalom.filemanager.filesystem.compressed.CompressedHelper;
import com.shalom.filemanager.utils.OnAsyncTaskFinished;
import com.shalom.filemanager.utils.application.AppConfig;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.shalom.filemanager.filesystem.compressed.CompressedHelper.SEPARATOR;

public class GzipHelperTask extends CompressedHelperTask {

    private WeakReference<Context> context;
    private String filePath, relativePath;

    public GzipHelperTask(Context context, String filePath, String relativePath, boolean goBack,
                         OnAsyncTaskFinished<AsyncTaskResult<ArrayList<CompressedObjectParcelable>>> l) {
        super(goBack, l);
        this.context = new WeakReference<>(context);
        this.filePath = filePath;
        this.relativePath = relativePath;
    }

    @Override
    void addElements(@NonNull ArrayList<CompressedObjectParcelable> elements) throws ArchiveException {
        TarArchiveInputStream tarInputStream = null;
        try {
            tarInputStream = new TarArchiveInputStream(
                    new GzipCompressorInputStream(new FileInputStream(filePath)));

            TarArchiveEntry entry;
            while ((entry = tarInputStream.getNextTarEntry()) != null) {
                String name = entry.getName();
                if (!CompressedHelper.isEntryPathValid(name)) {
                    AppConfig.toast(context.get(), context.get().getString(R.string.multiple_invalid_archive_entries));
                    continue;
                }
                if (name.endsWith(SEPARATOR)) name = name.substring(0, name.length() - 1);

                boolean isInBaseDir = relativePath.equals("") && !name.contains(SEPARATOR);
                boolean isInRelativeDir = name.contains(SEPARATOR)
                        && name.substring(0, name.lastIndexOf(SEPARATOR)).equals(relativePath);

                if (isInBaseDir || isInRelativeDir) {
                    elements.add(new CompressedObjectParcelable(entry.getName(),
                            entry.getLastModifiedDate().getTime(), entry.getSize(), entry.isDirectory()));
                }
            }
        } catch (IOException e) {
            throw new ArchiveException(String.format("Tarball archive %s is corrupt", filePath), e);
        }

    }

}
