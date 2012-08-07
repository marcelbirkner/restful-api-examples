package de.mb.rest.artifactory.client;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

import de.mb.rest.artifactory.resource.Builds;


/**
 * http://wiki.jfrog.org/confluence/display/RTF/Artifactory%27s+REST+API
 * 
 * @author Marcel Birkner
 */
public class ArtifactoryTestClient {

	private static Logger log = Logger.getLogger( ArtifactoryTestClient.class.getSimpleName() );

	private static String user = "marcel";
	private static String password = "c0d3c3ntr1c";
	private static String url = "http://artifactory:8081/artifactory/";

	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		ArtifactoryTestClient client = new ArtifactoryTestClient();
		client.run();
	}

	private void run() throws JsonParseException, JsonMappingException, IOException {
		WebResource service = getService();
		
		log.info("Simple PING to verify that Artifactory is running.");
		log.info( service.path("api").path("system").path("ping").accept(MediaType.TEXT_PLAIN).get(ClientResponse.class).toString() + "\n");

		log.info("System Information");
		log.info( service.path("api").path("system").accept(MediaType.TEXT_PLAIN).get(String.class).toString() + "\n");

		log.info("Build Information");
		String allBuilds = service.path("api").path("build").accept(MediaType.APPLICATION_JSON).get(String.class).toString();
		log.info(allBuilds);

		log.info("Unmarshalling JSON response using Codehause Jackson ObjectMapper");
		ObjectMapper mapper = new ObjectMapper();
		Builds builds = mapper.readValue(allBuilds, Builds.class);
		log.info( builds.getUri() );
		log.info( builds.toString() );

		log.info("Find artefact in repository by name=worblehat-web");
		log.info(service.path("api").path("search").path("artifact").queryParam("name", "worblehat-web").queryParam("repos", "libs-release-local").accept(MediaType.APPLICATION_JSON).get(ClientResponse.class).toString());
		log.info(service.path("api").path("search").path("artifact").queryParam("name", "worblehat-web").queryParam("repos", "libs-release-local").accept(MediaType.APPLICATION_JSON).get(String.class).toString());
	}

	private URI getBaseURI() {
		return UriBuilder.fromUri(url).build();
	}

	private WebResource getService() {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		client.addFilter(new HTTPBasicAuthFilter(user, password)); 
		return client.resource(getBaseURI());
	}
}

