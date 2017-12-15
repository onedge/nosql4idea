/*
 * Copyright (c) 2015 David Boissier
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.codinjutsu.tools.nosql.couchbase.view;

import com.couchbase.client.java.document.json.JsonObject;
import com.intellij.openapi.command.impl.DummyProject;
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfigurationImpl;
import org.codinjutsu.tools.nosql.commons.view.TableCellReader;
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions;
import org.codinjutsu.tools.nosql.couchbase.logic.CouchbaseClient;
import org.codinjutsu.tools.nosql.couchbase.model.CouchbaseDatabase;
import org.codinjutsu.tools.nosql.couchbase.model.CouchbaseResult;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.Containers;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JTableFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

class CouchbasePanelTest {

    private FrameFixture frameFixture;

    private final CouchbaseClient couchbaseClientMock = Mockito.mock(CouchbaseClient.class);
    private CouchbasePanel couchbasePanelWrapper;

    @BeforeEach
    void setUp() {
        when(couchbaseClientMock.loadRecords(any(CouchbaseContext.class), any(QueryOptions.class))).thenReturn(new CouchbaseResult("dummy"));


        couchbasePanelWrapper = GuiActionRunner.execute(new GuiQuery<CouchbasePanel>() {
            protected CouchbasePanel executeInEDT() {
                return new CouchbasePanel(DummyProject.getInstance(),
                        new CouchbaseContext(couchbaseClientMock,
                                new ServerConfigurationImpl(),
                                new CouchbaseDatabase("default"))) {
                };
            }
        });

        frameFixture = Containers.showInFrame(couchbasePanelWrapper);
    }

    @AfterEach
    void tearDown() {
        frameFixture.cleanUp();
    }

    @Test
    void displayJsonObjects() {
        // TODO: 18.11.2017  couchbasePanelWrapper.updateResultTableTree(createResults());
        couchbasePanelWrapper.expandAll();

        JTableFixture tableFixture = frameFixture.table("resultTreeTable");
        tableFixture.replaceCellReader(new TableCellReader());
        tableFixture.requireColumnCount(2)
                    .requireContents(new String[][]{
                            {"amount", "123456764"},
                            {"mad", "true"},
                            {"address", "{\"City\":\"Paris\",\"ZIP Code\":75016,\"Street\":\"Av. Champs Elysées\"}"},
                            {"Street", "\"Av. Champs Elysées\""},
                            {"ZIP Code", "75016"},
                            {"City", "\"Paris\""},
                            {"interests", "[\"programming\",\"XP\",\"TDD\"]"},
                            {"[0]", "\"programming\""},
                            {"[1]", "\"XP\""},
                            {"[2]", "\"TDD\""},
                            {"movies", "[{\"title\":\"Fight Club\",\"critic\":8.2},{\"title\":\"Blade Runner\",\"critic\":9.3},{\"title\":\"Toys Story\",\"critic\":8.7}]"},
                            {"[0]", "{\"title\":\"Fight Club\",\"critic\":8.2}"},
                            {"title", "\"Fight Club\""},
                            {"critic", "8.2"},
                            {"[1]", "{\"title\":\"Blade Runner\",\"critic\":9.3}"},
                            {"title", "\"Blade Runner\""},
                            {"critic", "9.3"},
                            {"[2]", "{\"title\":\"Toys Story\",\"critic\":8.7}"},
                            {"title", "\"Toys Story\""},
                            {"critic", "8.7"},
                            {"age", "25"},
                            {"score", "12345.12121"},
                            {"firstname", "\"Jojo\""},
                    });
    }

    private CouchbaseResult createResults() {
        CouchbaseResult result = new CouchbaseResult("test");
        result.add(JsonObject.create().put("firstname", "Jojo")
                             .put("age", 25)
                             .put("mad", true)
                             .put("interests", Arrays.asList("programming", "XP", "TDD"))
                             .put("amount", 123456764L)
                             .put("score", 12345.12121d)
                             .put("address", JsonObject.create()
                                                       .put("Street", "Av. Champs Elysées")
                                                       .put("City", "Paris")
                                                       .put("ZIP Code", 75016))
                             .put("movies", Arrays.asList(
                                     JsonObject.create()
                                               .put("title", "Fight Club")
                                               .put("critic", 8.2),
                                     JsonObject.create()
                                               .put("title", "Blade Runner")
                                               .put("critic", 9.3),
                                     JsonObject.create()
                                               .put("title", "Toys Story")
                                               .put("critic", 8.7)
                             )));
        return result;
    }
}