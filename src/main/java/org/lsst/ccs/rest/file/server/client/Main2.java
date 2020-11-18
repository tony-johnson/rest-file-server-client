package org.lsst.ccs.rest.file.server.client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.util.Map;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author tonyj
 */
public class Main2 {
    public static void main(String[] args) throws IOException {
//        String restUri = "http://localhost:8080/rest-file-server/rest/list/testVersions/test.file/4";
//        Client client = ClientBuilder.newClient();
//        RestFileInfo r = client.target(restUri).request(MediaType.APPLICATION_JSON).get(RestFileInfo.class);
//        System.out.println(r);
//        System.out.println(r.getMetadata());
//        System.out.println(r.getEntity());
        Path path = Path.of("/home/tonyj/Untitled9.ipynb");
        Map<String, Object> readAttributes = Files.readAttributes(path, "*");
        System.out.println(readAttributes);
    }
 
}
