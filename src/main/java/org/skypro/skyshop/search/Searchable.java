package org.skypro.skyshop.search;

import java.util.UUID;

public interface Searchable {
    String getSearchTerm();
    String getContentType();
    UUID getId();
}