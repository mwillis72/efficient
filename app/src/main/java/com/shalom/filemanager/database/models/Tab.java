
package com.shalom.filemanager.database.models;

import android.content.SharedPreferences;

import com.shalom.filemanager.utils.files.FileUtils;

/**
 * Created by Vishal on 9/17/2014
 */
public class Tab {
    public final int tabNumber;
    public final String path;
    public final String home;

    public Tab(int tabNo, String path, String home) {
        this.tabNumber = tabNo;
        this.path = path;
        this.home = home;
    }

    public String getOriginalPath(boolean savePaths, SharedPreferences sharedPreferences){
        if(savePaths && FileUtils.isPathAccessible(path, sharedPreferences)) {
            return path;
        } else {
            return home;
        }
    }

}
