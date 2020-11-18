package org.lsst.ccs.rest.file.server.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.AccessMode;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author tonyj
 */
public class RestPath implements Path {

    private final RestFileSystem fileSystem;
    private final LinkedList<String> path;
    private final boolean isReadOnly;
    private final boolean isAbsolute;

    RestPath(RestFileSystem fileSystem, String path, boolean isReadOnly) {
        this.fileSystem = fileSystem;
        this.path = new LinkedList(Arrays.asList(path.split("/")));
        this.isReadOnly = isReadOnly;
        this.isAbsolute = path.startsWith("/");
    }

    RestPath(RestFileSystem fileSystem, List<String> path, boolean isReadOnly, boolean isAbsolute) {
        this.fileSystem = fileSystem;
        this.path = new LinkedList(path);
        this.isReadOnly = isReadOnly;
        this.isAbsolute = isAbsolute;
    }

    @Override
    public FileSystem getFileSystem() {
        return fileSystem;
    }

    @Override
    public boolean isAbsolute() {
        return isAbsolute;
    }

    @Override
    public Path getRoot() {
        return fileSystem.getRootDirectories().iterator().next();
    }

    @Override
    public Path getFileName() {
        return new RestPath(fileSystem, path.getLast(), isReadOnly);
    }

    @Override
    public Path getParent() {
        return path.isEmpty() ? null : new RestPath(fileSystem, path.subList(0, path.size() - 1), isReadOnly, isAbsolute);
    }

    @Override
    public int getNameCount() {
        return path.size();
    }

    @Override
    public Path getName(int index) {
        return new RestPath(fileSystem, path.get(index), isReadOnly);
    }

    @Override
    public Path subpath(int beginIndex, int endIndex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean startsWith(Path other) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean endsWith(Path other) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Path normalize() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Path resolve(Path other) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Path relativize(Path other) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public URI toUri() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Path toAbsolutePath() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Path toRealPath(LinkOption... options) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public WatchKey register(WatchService watcher, WatchEvent.Kind<?>[] events, WatchEvent.Modifier... modifiers) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int compareTo(Path other) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toString() {
        return (isAbsolute ? ":" : "") + String.join("/", path);
    }

    InputStream newInputStream(OpenOption[] options) throws IOException {
        URI uri = fileSystem.getURI();
        uri = uri.resolve("rest/download/");
        uri = uri.resolve(String.join("/", path));
        URL realURL = new URL("http", uri.getHost(), uri.getPort(), uri.getPath());
        return realURL.openStream();
    }

    BasicFileAttributes getAttributes() throws IOException {
        URI uri = fileSystem.getURI();
        uri = uri.resolve("rest/info/");
        uri = uri.resolve(String.join("/", path));
        Client client = ClientBuilder.newClient();
        try {
            URI realURI = new URI("http", null, uri.getHost(), uri.getPort(), uri.getPath(), uri.getQuery(), uri.getFragment());
            Response response = client.target(realURI).request(MediaType.APPLICATION_JSON).get();
            RestFileInfo info = response.readEntity(RestFileInfo.class);
            return new RestFileAttributes(info);
        } catch (URISyntaxException x) {
            throw new IOException("invalid URL", x);
        }
    }
    
    BasicFileAttributeView getFileAttributeView() {
        return new BasicFileAttributeView() {
            @Override
            public String name() {
                return "basic";
            }

            @Override
            public BasicFileAttributes readAttributes() throws IOException {
                return getAttributes();
            }

            @Override
            public void setTimes(FileTime lastModifiedTime, FileTime lastAccessTime, FileTime createTime) throws IOException {
                throw new UnsupportedOperationException("Not supported yet."); 
            }
        };
    }

    void checkAccess(AccessMode... modes) throws IOException {
        URI uri = fileSystem.getURI();
        uri = uri.resolve("rest/list/");
        uri = uri.resolve(String.join("/", path));
        Client client = ClientBuilder.newClient();
        try {
            URI realURI = new URI("http", null, uri.getHost(), uri.getPort(), uri.getPath(), uri.getQuery(), uri.getFragment());
            Response response = client.target(realURI).request(MediaType.APPLICATION_JSON).get();
            if (response.getStatus() != 200) {
                throw new NoSuchFileException("No such file " + response.getStatus());
            } else {
                response.readEntity(RestFileInfo.class);
            }
        } catch (URISyntaxException x) {
            throw new IOException("invalid URL", x);
        }
    }

    DirectoryStream<Path> newDirectoryStream(DirectoryStream.Filter<? super Path> filter) throws IOException {
        URI uri = fileSystem.getURI();
        uri = uri.resolve("rest/list/");
        uri = uri.resolve(String.join("/", path));
        Client client = ClientBuilder.newClient();
        try {
            URI realURI = new URI("http", null, uri.getHost(), uri.getPort(), uri.getPath(), uri.getQuery(), uri.getFragment());
            Response response = client.target(realURI).request(MediaType.APPLICATION_JSON).get();
            if (response.getStatus() != 200) {
                throw new NoSuchFileException("No such file " + response.getStatus());
            } else {
                List<RestFileInfo> dirList = response.readEntity(new GenericType<List<RestFileInfo>>() {});
                List<Path> paths = dirList.stream().map(fileInfo -> {
                    List<String> newPath = new ArrayList<>(path);
                    newPath.add(fileInfo.getName());
                    return new RestPath(fileSystem, newPath, isReadOnly, true);
                }).collect(Collectors.toList());
                return new DirectoryStream<Path>() {
                    @Override
                    public Iterator<Path> iterator() {
                        return paths.iterator();
                    }

                    @Override
                    public void close() throws IOException {
                    }
                };
            }
        } catch (URISyntaxException x) {
            throw new IOException("invalid URL", x);
        }
    }

    void createDirectory(FileAttribute<?>[] attrs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
