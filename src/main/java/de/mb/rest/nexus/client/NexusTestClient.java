package de.mb.rest.nexus.client;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.sonatype.nexus.rest.model.RepositoryTargetResource;
import org.sonatype.nexus.rest.model.RepositoryTargetResourceResponse;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.Base64;

import de.mb.rest.nexus.resource.RepositoryTargets;

/**
 * http://localhost:8081/nexus/nexus-core-documentation-plugin/core/docs/index.html
 * 
 * @author Marcel Birkner
 */
public class NexusTestClient {

	private static Logger log = Logger.getLogger( NexusTestClient.class.getSimpleName() );

	private static String user = "admin";
	private static String password = "admin123";
	private static String url = "http://localhost:8081/nexus/";
	
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		NexusTestClient client = new NexusTestClient();
		client.run();
	}

	private void run() throws JsonParseException, JsonMappingException, IOException {
		WebResource service = getService();
		
		log.info("Check that Nexus is running");
		String nexusStatus = service.path("service").path("local").path("status").accept(MediaType.APPLICATION_JSON).get(ClientResponse.class).toString();
		log.info(nexusStatus + "\n");

		log.info("GET Nexus Version");
		String nexusVersion = service.path("service").path("local").path("status").accept(MediaType.APPLICATION_JSON).get(String.class).toString();
		log.info(nexusVersion + "\n");
		if( ! nexusVersion.contains( "nexus:metadata" ) ) {
			log.warning("Please install the metadata plugin.");
			return;
		}
		if( ! nexusVersion.contains( "Sonatype Nexus Professional" ) )  {
			log.warning("Please install Sonatype Nexus Professional.");
			return;
		}
		log.info("Installation seems to be correct.\n");

		String targetName = "NewTarget";
		log.info("Create Repo Target with name: " +  targetName);
		RepositoryTargetResourceResponse request = new RepositoryTargetResourceResponse();
		RepositoryTargetResource data = new RepositoryTargetResource();
		data.setContentClass("maven2");
		data.setName(targetName);
		data.setPatterns(Arrays.asList(".*"));
		request.setData(data);
		service.path("service").path("local").path("repo_targets").post(request);
		
		log.info("Get all repo targets");
		String repoTargets = service.path("service").path("local").path("repo_targets")
				.accept(MediaType.APPLICATION_JSON).get(String.class);
		log.info( repoTargets );
		if( ! repoTargets.contains( targetName ) )  {
			log.warning("Repo Target was not created successfully");
			return;
		}
		
		ObjectMapper mapper = new ObjectMapper();
		RepositoryTargets targets = mapper.readValue(repoTargets, RepositoryTargets.class);
		RepositoryTargetResource[] list = targets.getData();
		for (RepositoryTargetResource res : list) {
			if( res.getName().equalsIgnoreCase( targetName ) ) {
				log.info("Delete Repo Target with ID " + res.getId() + " Name " + res.getName() );
				service.path("service").path("local").path("repo_targets").path(res.getId()).delete();
			}
		}
		
		log.info(service.path("service").path("local").path("search").path("m2").path("freeform")
				.queryParam("p", "commitStage").queryParam("t", "matches").queryParam("v", "success")
				.accept(MediaType.APPLICATION_JSON).get(String.class).toString());

		String artifact = "urn:maven/artifact#de.mb:rest-test:0.0.1::jar";
		log.info("Get metadata of artifact " + artifact + ". Only works with Nexus Pro & metadata plugin installed");
		String encodedString = new String( Base64.encode(artifact.getBytes()));
		String metaDataResult = service.path("service").path("local").path("index").path("custom_metadata").path("releases")
				.path(encodedString).accept(MediaType.APPLICATION_JSON).get(String.class).toString();
		log.info(metaDataResult);
	}

	private WebResource getService() {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		client.addFilter(new HTTPBasicAuthFilter(user, password)); 
		return client.resource(getBaseURI());
	}

	private URI getBaseURI() {
		return UriBuilder.fromUri( url ).build();
	}
}

