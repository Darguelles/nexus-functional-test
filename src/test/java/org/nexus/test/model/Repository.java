package org.nexus.test.model;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.ArrayList;

@Builder
@AllArgsConstructor
public class Repository {
    public String name;
    public boolean online;
    public Storage storage;
    public Cleanup cleanup;
    public Component component;
    public Maven maven;
}

