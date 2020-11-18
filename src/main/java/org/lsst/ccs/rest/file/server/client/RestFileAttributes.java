package org.lsst.ccs.rest.file.server.client;

import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

/**
 *
 * @author tonyj
 */
public class RestFileAttributes implements BasicFileAttributes {

    private final RestFileInfo info;
    
    RestFileAttributes(RestFileInfo info) {
        this.info = info;
    }
    
    @Override
    public FileTime lastModifiedTime() {
        return FileTime.fromMillis(info.getLastModified());
    }

    @Override
    public FileTime lastAccessTime() {
        return FileTime.fromMillis(info.getLastAccessTime());
    }

    @Override
    public FileTime creationTime() {
        return FileTime.fromMillis(info.getCreationTime());
    }

    @Override
    public boolean isRegularFile() {
        return info.isRegularFile();
    }

    @Override
    public boolean isDirectory() {
        return info.isDirectory();
    }

    @Override
    public boolean isSymbolicLink() {
        return info.isSymbolicLink();
    }

    @Override
    public boolean isOther() {
        return info.isOther();
    }

    @Override
    public long size() {
        return info.getSize();
    }

    @Override
    public Object fileKey() {
        return info.getFileKey();
    }
    
}
