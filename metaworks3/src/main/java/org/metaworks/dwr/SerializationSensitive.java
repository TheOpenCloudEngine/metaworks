package org.metaworks.dwr;

public interface SerializationSensitive {
    public void afterDeserialization();
    public void beforeSerialization();
}
