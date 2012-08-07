package de.mb.rest.nexus.resource;

import org.sonatype.nexus.rest.model.RepositoryTargetResource;

public class RepositoryTargets {

	private RepositoryTargetResource[] data;

	public RepositoryTargetResource[] getData() {
		return data;
	}

	public void setData(RepositoryTargetResource[] data) {
		this.data = data;
	}
}
