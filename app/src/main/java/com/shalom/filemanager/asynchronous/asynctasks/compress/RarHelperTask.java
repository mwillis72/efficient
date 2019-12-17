
package com.shalom.filemanager.asynchronous.asynctasks.compress;

import androidx.annotation.NonNull;

import com.shalom.filemanager.adapters.data.CompressedObjectParcelable;
import com.shalom.filemanager.asynchronous.asynctasks.AsyncTaskResult;
import com.shalom.filemanager.filesystem.compressed.CompressedHelper;
import com.shalom.filemanager.filesystem.compressed.showcontents.helpers.RarDecompressor;
import com.shalom.filemanager.utils.OnAsyncTaskFinished;
import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;

import org.apache.commons.compress.archivers.ArchiveException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class RarHelperTask extends CompressedHelperTask {

    private String fileLocation;
    private String relativeDirectory;

    /**
     * AsyncTask to load RAR file items.
     * @param realFileDirectory the location of the zip file
     * @param dir relativeDirectory to access inside the zip file
     */
    public RarHelperTask(String realFileDirectory, String dir, boolean goBack,
                         OnAsyncTaskFinished<AsyncTaskResult<ArrayList<CompressedObjectParcelable>>> l) {
        super(goBack, l);
        fileLocation = realFileDirectory;
        relativeDirectory = dir;
    }

    @Override
    void addElements(@NonNull ArrayList<CompressedObjectParcelable> elements) throws ArchiveException {
        try {
            Archive zipfile = new Archive(new File(fileLocation));
            String relativeDirDiffSeparator = relativeDirectory.replace(CompressedHelper.SEPARATOR, "\\");

            for (FileHeader rarArchive : zipfile.getFileHeaders()) {
                String name = rarArchive.getFileNameString();//This uses \ as separator, not /
                if (!CompressedHelper.isEntryPathValid(name)) {
                    continue;
                }
                boolean isInBaseDir = (relativeDirDiffSeparator == null || relativeDirDiffSeparator.equals("")) && !name.contains("\\");
                boolean isInRelativeDir = relativeDirDiffSeparator != null && name.contains("\\")
                        && name.substring(0, name.lastIndexOf("\\")).equals(relativeDirDiffSeparator);

                if (isInBaseDir || isInRelativeDir) {
                    elements.add(new CompressedObjectParcelable(RarDecompressor.convertName(rarArchive), 0, rarArchive.getDataSize(), rarArchive.isDirectory()));
                }
            }
        } catch (RarException | IOException e) {
            throw new ArchiveException(String.format("RAR archive %s is corrupt", fileLocation));
        }
    }

}

