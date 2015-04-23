package org.metaworks.dao;

public interface ReleaseResourceListener {
	void beforeReleaseResource(TransactionContext tx) throws Exception;
}
