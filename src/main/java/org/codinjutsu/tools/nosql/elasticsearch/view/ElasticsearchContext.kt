package org.codinjutsu.tools.nosql.elasticsearch.view

import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.view.DatabaseContext
import org.codinjutsu.tools.nosql.commons.view.filedialogs.ImportPanelSettings
import org.codinjutsu.tools.nosql.elasticsearch.logic.ElasticsearchClient
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchDatabase
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchType

internal class ElasticsearchContext(client: ElasticsearchClient,
                                    serverConfiguration: ServerConfiguration,
                                    val type: ElasticsearchType?,
                                    val database: ElasticsearchDatabase) :
        DatabaseContext<ElasticsearchClient>(client, serverConfiguration) {

    override fun getImportPanelSettings(): ImportPanelSettings? {
        return object : ImportPanelSettings {
            override fun getExtensions() = arrayOf(".json")
        }
    }
}