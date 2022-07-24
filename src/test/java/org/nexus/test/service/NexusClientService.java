package org.nexus.test.service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.MultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;
import com.sun.jersey.multipart.impl.MultiPartWriter;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.io.IOException;
import java.net.URI;

public class NexusClientService {

    private static String user = "admin";
    private static String password = "admin123";
    private static String url = "http://localhost:8081/";

    public static WebResource getService() {
        ClientConfig config = new DefaultClientConfig();
        config.getClasses().add(MultiPartWriter.class);
        Client client = Client.create(config);
        client.addFilter(new HTTPBasicAuthFilter(user, password));
        return client.resource(getBaseURI());
    }


    private static URI getBaseURI() {
        return UriBuilder.fromUri( url ).build();
    }

    public ClientResponse uploadMavenComponentService(String componentPath, String repositoryName) throws IOException {
        WebResource resource = getService();
        ClassLoader classLoader = getClass().getClassLoader();
        File fileToUpload = new File(classLoader.getResource(componentPath).getFile());
        FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("maven2.asset1",
                fileToUpload,
                MediaType.APPLICATION_OCTET_STREAM_TYPE);
        System.out.println(fileToUpload.getName());
        fileDataBodyPart.setContentDisposition(
                FormDataContentDisposition.name("maven2.asset1")
                        .fileName(fileToUpload.getName()).build());

        final MultiPart multiPart = new FormDataMultiPart()
                .field("maven2.asset1.extension", "jar", MediaType.TEXT_PLAIN_TYPE)
                .field("maven2.groupId", "com.test", MediaType.TEXT_PLAIN_TYPE)
                .field("maven2.artifactId", "example", MediaType.TEXT_PLAIN_TYPE)
                .field("maven2.version", "1.0.0", MediaType.TEXT_PLAIN_TYPE)
                .field("version", "1.0.0", MediaType.TEXT_PLAIN_TYPE)
                .bodyPart(fileDataBodyPart);
        multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);

        return resource.path("service/rest/v1/components").queryParam("repository", repositoryName)
                .type("multipart/form-data").post(ClientResponse.class, multiPart);

    }

}
