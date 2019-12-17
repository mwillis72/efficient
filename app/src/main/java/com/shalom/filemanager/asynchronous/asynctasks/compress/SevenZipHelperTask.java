

package com.shalom.filemanager.asynchronous.asynctasks.compress;

import androidx.annotation.NonNull;
import android.widget.EditText;

import com.shalom.filemanager.R;
import com.shalom.filemanager.activities.MainActivity;
import com.shalom.filemanager.adapters.data.CompressedObjectParcelable;
import com.shalom.filemanager.asynchronous.asynctasks.AsyncTaskResult;
import com.shalom.filemanager.filesystem.compressed.ArchivePasswordCache;
import com.shalom.filemanager.filesystem.compressed.sevenz.SevenZArchiveEntry;
import com.shalom.filemanager.filesystem.compressed.sevenz.SevenZFile;
import com.shalom.filemanager.ui.dialogs.GeneralDialogCreation;
import com.shalom.filemanager.utils.OnAsyncTaskFinished;
import com.shalom.filemanager.utils.application.AppConfig;

import org.apache.commons.compress.PasswordRequiredException;
import org.apache.commons.compress.archivers.ArchiveException;
import org.tukaani.xz.CorruptedInputException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.shalom.filemanager.filesystem.compressed.CompressedHelper.SEPARATOR;

public class SevenZipHelperTask extends CompressedHelperTask {

    private String filePath, relativePath;

    private boolean paused = false;

    public SevenZipHelperTask(String filePath, String relativePath, boolean goBack,
                         OnAsyncTaskFinished<AsyncTaskResult<ArrayList<CompressedObjectParcelable>>> l) {
        super(goBack, l);
        this.filePath = filePath;
        this.relativePath = relativePath;
    }

    @Override
    void addElements(@NonNull ArrayList<CompressedObjectParcelable> elements) throws ArchiveException {
        while(true) {
            if (paused) continue;

            try {
                SevenZFile sevenzFile = (ArchivePasswordCache.getInstance().containsKey(filePath)) ?
                        new SevenZFile(new File(filePath), ArchivePasswordCache.getInstance().get(filePath).toCharArray()) :
                        new SevenZFile(new File(filePath));

                for (SevenZArchiveEntry entry : sevenzFile.getEntries()) {
                    String name = entry.getName();
                    boolean isInBaseDir = relativePath.equals("") && !name.contains(SEPARATOR);
                    boolean isInRelativeDir = name.contains(SEPARATOR)
                            && name.substring(0, name.lastIndexOf(SEPARATOR)).equals(relativePath);

                    if (isInBaseDir || isInRelativeDir) {
                        elements.add(new CompressedObjectParcelable(entry.getName(),
                                entry.getLastModifiedDate().getTime(), entry.getSize(), entry.isDirectory()));
                    }
                }
                paused = false;
                break;
            } catch (PasswordRequiredException e) {
                paused = true;
                publishProgress(e);
            } catch (IOException e) {
                throw new ArchiveException(String.format("7zip archive %s is corrupt", filePath));
            }
        }
    }

    @Override
    protected void onProgressUpdate(IOException... values) {
        super.onProgressUpdate(values);
        if (values.length < 1) return;

        IOException result = values[0];
        //We only handle PasswordRequiredException here.
        if(result instanceof PasswordRequiredException || result instanceof CorruptedInputException)
        {
            ArchivePasswordCache.getInstance().remove(filePath);
            GeneralDialogCreation.showPasswordDialog(AppConfig.getInstance().getMainActivityContext(),
                (MainActivity)AppConfig.getInstance().getMainActivityContext(),
                AppConfig.getInstance().getUtilsProvider().getAppTheme(),
                R.string.archive_password_prompt, R.string.authenticate_password,
                ((dialog, which) -> {
                    EditText editText = dialog.getView().findViewById(R.id.singleedittext_input);
                    String password = editText.getText().toString();
                    ArchivePasswordCache.getInstance().put(filePath, password);
                    paused = false;
                    dialog.dismiss();
                }), null);
        }
    }

}
