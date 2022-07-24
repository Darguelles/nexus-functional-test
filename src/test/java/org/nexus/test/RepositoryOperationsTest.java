package org.nexus.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nexus.test.model.*;
import org.nexus.test.service.NexusClientService;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class RepositoryOperationsTest {

    private static Logger log = Logger.getLogger( ServiceStatusTest.class.getSimpleName() );
    WebResource service = NexusClientService.getService();

    private static final String REPOSITORY_NAME = "internal";

    @Before
    public void createRepository() throws JsonProcessingException {
        log.info("Creating repository");
        Repository newRepo = Repository.builder()
                .name(REPOSITORY_NAME)
                .online(true)
                .storage(Storage.builder().blobStoreName("default")
                        .strictContentTypeValidation(false)
                        .writePolicy("allow_once").build())
                .cleanup(Cleanup.builder().policyNames(List.of("string")).build())
                .component(Component.builder().proprietaryComponents(true).build())
                .maven(Maven.builder()
                        .versionPolicy("MIXED")
                        .layoutPolicy("STRICT")
                        .contentDisposition("ATTACHMENT").build())
                .build();
        ObjectMapper mapper = new ObjectMapper();
        String repoJson = mapper.writeValueAsString(newRepo);
        log.info(repoJson);
        service.path("/service/rest/v1/repositories/maven/hosted").accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(repoJson);
        log.info("Repository created");
    }

    @After
    public void deleteRepository() {
        log.info("Deleting repository");
        service.path("/service/rest/v1/repositories").path(REPOSITORY_NAME).accept(MediaType.APPLICATION_JSON).delete();
        log.info("Repository deleted");
    }

    @Test
    public void uploadComponent() throws IOException {
        log.info("Uploading component");
        ClientResponse response = new NexusClientService().uploadMavenComponentService("example-1.0-jre.jar", REPOSITORY_NAME);
        log.info(String.valueOf(response));
        assertThat(response.getStatus(), is(204));
    }

    @Test
    public void shouldRetrieveMetadataFile() throws IOException{
        log.info("Uploading component");
        new NexusClientService().uploadMavenComponentService("example-1.0-jre.jar", REPOSITORY_NAME);
        log.info("Getting metadata from uploaded component");
        ClientResponse response = NexusClientService.getService()
                .path("repository/internal/com/test/example/maven-metadata.xml").get(ClientResponse.class);
        log.info(String.valueOf(response));
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void shouldReturn404forEmptyRepository() throws IOException{
        log.info("Get metadata from non-existing component");
        ClientResponse response = NexusClientService.getService()
                .path("repository/internal/com/test/example/maven-metadata.xml").get(ClientResponse.class);
        log.info(String.valueOf(response));
        assertThat(response.getStatus(), is(404));
    }






}
