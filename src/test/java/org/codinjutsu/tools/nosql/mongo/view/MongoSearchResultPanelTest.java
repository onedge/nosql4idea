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

package org.codinjutsu.tools.nosql.mongo.view;

import com.intellij.openapi.command.impl.DummyProject;
import com.intellij.util.ui.tree.TreeUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.apache.commons.io.IOUtils;
import org.codinjutsu.tools.nosql.commons.view.NoSQLResultPanelDocumentOperations;
import org.codinjutsu.tools.nosql.commons.view.TableCellReader;
import org.codinjutsu.tools.nosql.mongo.model.MongoSearchResult;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.Containers;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JTableFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Disabled("The JsonTreeTableView was not found by it's name")
class MongoSearchResultPanelTest {

    private MongoResultPanel mongoResultPanel;

    private FrameFixture frameFixture;

    private NoSQLResultPanelDocumentOperations<DBObject> noSQLResultPanelDocumentOperations;

    @AfterEach
    void tearDown() {
        frameFixture.cleanUp();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(MongoSearchResultPanelTest.class);
        noSQLResultPanelDocumentOperations = mock(NoSQLResultPanelDocumentOperations.class);
        when(noSQLResultPanelDocumentOperations.getDocument(any())).thenReturn(new BasicDBObject());

        mongoResultPanel = GuiActionRunner.execute(new GuiQuery<MongoResultPanel>() {
            protected MongoResultPanel executeInEDT() {
                return new MongoResultPanel(DummyProject.getInstance(), noSQLResultPanelDocumentOperations) {
                    @Override
                    protected void buildPopupMenu() {
                    }
                };
            }
        });

        frameFixture = Containers.showInFrame(mongoResultPanel);
    }

    @Test
    void displayTreeWithASimpleArray() throws Exception {
        mongoResultPanel.updateResultTableTree(createCollectionResults("simpleArray.json", "mycollec"));

        JTableFixture tableFixture = frameFixture.table("resultTreeTable");
        tableFixture.replaceCellReader(new TableCellReader());
        tableFixture.requireColumnCount(2)
                .requireContents(new String[][]{
                        {"[0]", "\"toto\""},
                        {"[1]", "true"},
                        {"[2]", "10"},
                        {"[3]", "null"},
                });
    }

    @Test
    void testDisplayTreeWithASimpleDocument() throws Exception {
        mongoResultPanel.updateResultTableTree(createCollectionResults("simpleDocument.json", "mycollec"));

        JTableFixture tableFixture = frameFixture.table("resultTreeTable");
        tableFixture.replaceCellReader(new TableCellReader());
        tableFixture.requireColumnCount(2)
                .requireContents(new String[][]{
                        {"[0]", "{ \"_id\" : \"50b8d63414f85401b9268b99\" , \"label\" : \"toto\" , \"visible\" : false , \"image\" :  null }"},
                        {"_id", "\"50b8d63414f85401b9268b99\""},
                        {"label", "\"toto\""},
                        {"visible", "false"},
                        {"image", "null"}
                });
    }


    @Test
    void testDisplayTreeWithAStructuredDocument() throws Exception {
        mongoResultPanel.updateResultTableTree(createCollectionResults("structuredDocument.json", "mycollec"));
        TreeUtil.expandAll(mongoResultPanel.getResultTableView().getTree());
        JTableFixture tableFixture = frameFixture.table("resultTreeTable");
        tableFixture.replaceCellReader(new TableCellReader());
        tableFixture.requireColumnCount(2)
                .requireContents(new String[][]{
                        {"[0]", "{ \"id\" : 0 , \"label\" : \"toto\" , \"visible\" : false , \"doc\" : { \"title\" : \"hello\" , \"nbPages\" : 10 , \"keyWord\" : [ \"toto\" , true , 10]}}"},
                        {"id", "0"},
                        {"label", "\"toto\""},
                        {"visible", "false"},
                        {"doc", "{ \"title\" : \"hello\" , \"nbPages\" : 10 , \"keyWord\" : [ \"toto\" , true , 10]}"},
                        {"title", "\"hello\""},
                        {"nbPages", "10"},
                        {"keyWord", "[ \"toto\" , true , 10]"},
                        {"[0]", "\"toto\""},
                        {"[1]", "true"},
                        {"[2]", "10"},
                });
    }

    @Test
    void testDisplayTreeWithAnArrayOfStructuredDocument() throws Exception {
        mongoResultPanel.updateResultTableTree(createCollectionResults("arrayOfDocuments.json", "mycollec"));

        TreeUtil.expandAll(mongoResultPanel.getResultTableView().getTree());
        JTableFixture tableFixture = frameFixture.table("resultTreeTable");
        tableFixture.replaceCellReader(new TableCellReader());
        tableFixture.requireContents(new String[][]{
                {"[0]", "{ \"id\" : 0 , \"label\" : \"toto\" , \"visible\" : false , \"doc\" : { \"title\" : \"hello\" , \"nbPages\" : 10 , \"keyWord\" : [ \"toto\" , true , 10]}}"},
                {"id", "0"},
                {"label", "\"toto\""},
                {"visible", "false"},
                {"doc", "{ \"title\" : \"hello\" , \"nbPages\" : 10 , \"keyWord\" : [ \"toto\" , true , 10]}"},
                {"title", "\"hello\""},
                {"nbPages", "10"},
                {"keyWord", "[ \"toto\" , true , 10]"},
                {"[0]", "\"toto\""},
                {"[1]", "true"},
                {"[2]", "10"},
                {"[1]", "{ \"id\" : 1 , \"label\" : \"tata\" , \"visible\" : true , \"doc\" : { \"title\" : \"ola\" , \"nbPages\" : 1 , \"keyWord\" : [ \"tutu\" , false , 10]}}"},
                {"id", "1"},
                {"label", "\"tata\""},
                {"visible", "true"},
                {"doc", "{ \"title\" : \"ola\" , \"nbPages\" : 1 , \"keyWord\" : [ \"tutu\" , false , 10]}"},
                {"title", "\"ola\""},
                {"nbPages", "1"},
                {"keyWord", "[ \"tutu\" , false , 10]"},
                {"[0]", "\"tutu\""},
                {"[1]", "false"},
                {"[2]", "10"},
        });
    }

    @Test
    void testCopyMongoObjectNodeValue() throws Exception {
        mongoResultPanel.updateResultTableTree(createCollectionResults("structuredDocument.json", "mycollec"));
        TreeUtil.expandAll(mongoResultPanel.getResultTableView().getTree());

        mongoResultPanel.getResultTableView().setRowSelectionInterval(0, 0);
        assertEquals("{ \"id\" : 0 , \"label\" : \"toto\" , \"visible\" : false , \"doc\" : { \"title\" : \"hello\" , \"nbPages\" : 10 , \"keyWord\" : [ \"toto\" , true , 10]}}", mongoResultPanel.getSelectedNodeStringifiedValue());

        mongoResultPanel.getResultTableView().setRowSelectionInterval(2, 2);
        assertEquals("\"label\" : \"toto\"", mongoResultPanel.getSelectedNodeStringifiedValue());

        mongoResultPanel.getResultTableView().setRowSelectionInterval(4, 4);
        assertEquals("\"doc\" : { \"title\" : \"hello\" , \"nbPages\" : 10 , \"keyWord\" : [ \"toto\" , true , 10]}", mongoResultPanel.getSelectedNodeStringifiedValue());
    }

    @Test
    void copyMongoResults() throws Exception {
        mongoResultPanel.updateResultTableTree(createCollectionResults("arrayOfDocuments.json", "mycollec"));

        TreeUtil.expandAll(mongoResultPanel.getResultTableView().getTree());

        JTableFixture tableFixture = frameFixture.table("resultTreeTable");
        tableFixture.replaceCellReader(new TableCellReader());
        tableFixture.requireContents(new String[][]{
                {"[0]", "{ \"id\" : 0 , \"label\" : \"toto\" , \"visible\" : false , \"doc\" : { \"title\" : \"hello\" , \"nbPages\" : 10 , \"keyWord\" : [ \"toto\" , true , 10]}}"},
                {"id", "0"},
                {"label", "\"toto\""},
                {"visible", "false"},
                {"doc", "{ \"title\" : \"hello\" , \"nbPages\" : 10 , \"keyWord\" : [ \"toto\" , true , 10]}"},
                {"title", "\"hello\""},
                {"nbPages", "10"},
                {"keyWord", "[ \"toto\" , true , 10]"},
                {"[0]", "\"toto\""},
                {"[1]", "true"},
                {"[2]", "10"},
                {"[1]", "{ \"id\" : 1 , \"label\" : \"tata\" , \"visible\" : true , \"doc\" : { \"title\" : \"ola\" , \"nbPages\" : 1 , \"keyWord\" : [ \"tutu\" , false , 10]}}"},
                {"id", "1"},
                {"label", "\"tata\""},
                {"visible", "true"},
                {"doc", "{ \"title\" : \"ola\" , \"nbPages\" : 1 , \"keyWord\" : [ \"tutu\" , false , 10]}"},
                {"title", "\"ola\""},
                {"nbPages", "1"},
                {"keyWord", "[ \"tutu\" , false , 10]"},
                {"[0]", "\"tutu\""},
                {"[1]", "false"},
                {"[2]", "10"},
        });

        assertEquals("[ " +
                        "{ \"id\" : 0 , \"label\" : \"toto\" , \"visible\" : false , \"doc\" : { \"title\" : \"hello\" , \"nbPages\" : 10 , \"keyWord\" : [ \"toto\" , true , 10]}} , " +
                        "{ \"id\" : 1 , \"label\" : \"tata\" , \"visible\" : true , \"doc\" : { \"title\" : \"ola\" , \"nbPages\" : 1 , \"keyWord\" : [ \"tutu\" , false , 10]}}" +
                        " ]",
                mongoResultPanel.getSelectedNodeStringifiedValue());
    }

    private MongoSearchResult createCollectionResults(String data, String collectionName) throws IOException {
        DBObject jsonObject = (DBObject) JSON.parse(IOUtils.toString(getClass().getResourceAsStream(data), Charset.defaultCharset()));

        MongoSearchResult mongoSearchResult = new MongoSearchResult(collectionName);
        mongoSearchResult.add(jsonObject);

        return mongoSearchResult;
    }

}