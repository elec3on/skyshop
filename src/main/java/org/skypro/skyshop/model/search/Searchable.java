package org.skypro.skyshop.model.search;

import java.util.UUID;

public interface Searchable {
    String getSearchTerm();
    String getContentType();
    UUID getId();
    String getName();
}