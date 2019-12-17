
package com.shalom.filemanager.database.models;

/**
 * Created by Ning on 5/28/2018.
 */
public class Sort {
    public final String path;
    public final int type;

    public Sort(String path, int type) {
        this.path = path;
        this.type = type;
    }
}
