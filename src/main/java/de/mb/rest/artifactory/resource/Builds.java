package de.mb.rest.artifactory.resource;

import java.util.Arrays;

public class Builds {

	public static class Build {
		
		private String uri;
		private String lastStarted;

		public String getUri() {
			return uri;
		}
		public void setUri(String uri) {
			this.uri = uri;
		}
		public String getLastStarted() {
			return lastStarted;
		}
		public void setLastStarted(String lastStarted) {
			this.lastStarted = lastStarted;
		}
		@Override
		public String toString() {
			return "Build [uri=" + uri + ", lastStarted=" + lastStarted + "]";
		}
	}

	private String uri;
	private Build[] builds;

	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public Build[] getBuilds() {
		return builds;
	}
	public void setBuilds(Build[] builds) {
		this.builds = builds;
	}
	@Override
	public String toString() {
		return "Builds [uri=" + uri + ", builds=" + Arrays.toString(builds)
				+ "]";
	}
}
