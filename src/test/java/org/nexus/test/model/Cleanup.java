package org.nexus.test.model;

import lombok.Builder;

import java.util.List;

@Builder
public class Cleanup {
    public List<String> policyNames;
}
