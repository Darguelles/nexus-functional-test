package org.nexus.test.model;

import lombok.Builder;

@Builder
public class Maven {
    public String versionPolicy;
    public String layoutPolicy;
    public String contentDisposition;
}
