package org.nexus.test.model;

import lombok.Builder;

@Builder
public class Storage {
    public String blobStoreName;
    public boolean strictContentTypeValidation;
    public String writePolicy;
}
