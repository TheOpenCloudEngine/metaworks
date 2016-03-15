package org.metaworks.dwr;

public interface SerializationSensitive {
    public void afterMWDeserialization();
    public void beforeMWSerialization();
}
