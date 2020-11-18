package org.lsst.ccs.rest.file.server.client;

/**
 *
 * @author tonyj
 */
public class RestFileInfo {
    private long lastModified;
    private long creationTime;
    private long lastAccessTime;
    private long size;
    private String mimeType;
    private String name;       
    private String fileKey;
    private boolean isDirectory;
    private boolean isOther;
    private boolean isRegularFile;
    private boolean isSymbolicLink;

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setIsDirectory(boolean isDirectory) {
        this.isDirectory = isDirectory;
    }

    public boolean isOther() {
        return isOther;
    }

    public void setIsOther(boolean isOther) {
        this.isOther = isOther;
    }

    public boolean isRegularFile() {
        return isRegularFile;
    }

    public void setIsRegularFile(boolean isRegularFile) {
        this.isRegularFile = isRegularFile;
    }

    public boolean isSymbolicLink() {
        return isSymbolicLink;
    }

    public void setIsSymbolicLink(boolean isSymbolicLink) {
        this.isSymbolicLink = isSymbolicLink;
    }

    @Override
    public String toString() {
        return "RestFileInfo{" + "lastModified=" + lastModified + ", size=" + size + ", mimeType=" + mimeType + ", name=" + name + '}';
    }
    
}
