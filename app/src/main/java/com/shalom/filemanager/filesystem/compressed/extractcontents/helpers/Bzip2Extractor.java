
package com.shalom.filemanager.filesystem.compressed.extractcontents.helpers;

import android.content.Context;
import androidx.annotation.NonNull;

import com.shalom.filemanager.filesystem.FileUtil;
import com.shalom.filemanager.filesystem.compressed.extractcontents.Extractor;
import com.shalom.filemanager.utils.ServiceWatcherUtil;
import com.shalom.filemanager.utils.files.GenericCopyUtil;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Bzip2Extractor extends Extractor {

    public Bzip2Extractor(@NonNull Context context, @NonNull String filePath, @NonNull String outputPath, @NonNull OnUpdate listener) {
        super(context, filePath, outputPath, listener);
    }

    @Override
    protected void extractWithFilter(@NonNull Filter filter) throws IOException {
        long totalBytes = 0;
        ArrayList<TarArchiveEntry> archiveEntries = new ArrayList<>();
        TarArchiveInputStream inputStream = new TarArchiveInputStream(
                new BZip2CompressorInputStream(new FileInputStream(filePath)));

        TarArchiveEntry tarArchiveEntry;

        while ((tarArchiveEntry = inputStream.getNextTarEntry()) != null) {
            if(filter.shouldExtract(tarArchiveEntry.getName(), tarArchiveEntry.isDirectory())) {
                archiveEntries.add(tarArchiveEntry);
                totalBytes += tarArchiveEntry.getSize();
            }
        }

        listener.onStart(totalBytes, archiveEntries.get(0).getName());

        inputStream.close();
        inputStream = new TarArchiveInputStream(new BZip2CompressorInputStream(new FileInputStream(filePath)));

        for (TarArchiveEntry entry : archiveEntries) {
            if (!listener.isCancelled()) {
                listener.onUpdate(entry.getName());
                //TAR is sequential, you need to walk all the way to the file you want
                while (entry.hashCode() != inputStream.getNextTarEntry().hashCode());
                extractEntry(context, inputStream, entry, outputPath);
            }
        }
        inputStream.close();

        listener.onFinish();
    }

    private void extractEntry(@NonNull final Context context, TarArchiveInputStream inputStream,
                              TarArchiveEntry entry, String outputDir) throws IOException {
        if (entry.isDirectory()) {
            FileUtil.mkdir(new File(outputDir, entry.getName()), context);
            return;
        }

        File outputFile = new File(outputDir, entry.getName());
        if (!outputFile.getParentFile().exists()) {
            FileUtil.mkdir(outputFile.getParentFile(), context);
        }

        BufferedOutputStream outputStream = new BufferedOutputStream(
                FileUtil.getOutputStream(outputFile, context));
        try {
            int len;
            byte buf[] = new byte[GenericCopyUtil.DEFAULT_BUFFER_SIZE];
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
                ServiceWatcherUtil.position += len;
            }
        } finally {
            outputStream.close();
        }
    }

}