package org.nexus.test;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.junit.Test;
import org.nexus.test.service.NexusClientService;

import javax.ws.rs.core.MediaType;
import java.util.logging.Logger;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ServiceStatusTest {

    private static Logger log = Logger.getLogger( ServiceStatusTest.class.getSimpleName() );
    WebResource service = NexusClientService.getService();

    @Test
    public void checkNexusServiceStatusOK() {
        log.info("Check that Nexus is running");
        ClientResponse nexusStatus = service.path("service").path("rest").path("v1").path("status").path("check").accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        log.info(nexusStatus + "\n");
        assertThat(nexusStatus.getStatus(), is(200));
    }

    @Test
    public void checkNexusServiceIsReadableWritable() {
        log.info("Check that Nexus is running");
        ClientResponse nexusStatus = service.path("service").path("rest").path("v1").path("status").path("writable").accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        log.info(nexusStatus + "\n");
        assertThat(nexusStatus.getStatus(), is(200));
    }

}
