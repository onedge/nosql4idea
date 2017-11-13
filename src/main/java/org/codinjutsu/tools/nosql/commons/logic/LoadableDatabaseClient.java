package org.codinjutsu.tools.nosql.commons.logic;

import org.codinjutsu.tools.nosql.ServerConfiguration;
import org.codinjutsu.tools.nosql.commons.model.Database;
import org.codinjutsu.tools.nosql.commons.model.Query;
import org.codinjutsu.tools.nosql.commons.model.SearchResult;

public interface LoadableDatabaseClient<RESULT extends SearchResult, QUERY extends Query> extends DatabaseClient {
    RESULT loadRecords(ServerConfiguration configuration, Database database, QUERY query);
}