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

package org.codinjutsu.tools.nosql.mongo.view.console;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.mongodb.AuthenticationMechanism;
import org.codinjutsu.tools.nosql.DatabaseVendor;
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration;
import org.codinjutsu.tools.nosql.commons.model.AuthenticationSettings;
import org.codinjutsu.tools.nosql.commons.view.console.AbstractNoSQLConsoleRunner;
import org.codinjutsu.tools.nosql.mongo.MongoUtils;
import org.codinjutsu.tools.nosql.mongo.logic.MongoExtraSettings;
import org.codinjutsu.tools.nosql.mongo.model.MongoDatabase;
import org.jetbrains.annotations.NotNull;

import static org.codinjutsu.tools.nosql.DatabaseVendor.MONGO;


public class MongoConsoleRunner extends AbstractNoSQLConsoleRunner {

    private static final Key<Boolean> MONGO_SHELL_FILE = Key.create("MONGO_SHELL_FILE");
    private static final String CONSOLE_TYPE_ID = "Mongo Shell";
    private final MongoDatabase database;

    public MongoConsoleRunner(@NotNull Project project, ServerConfiguration serverConfiguration, MongoDatabase database) {
        super(project, CONSOLE_TYPE_ID, "/tmp", serverConfiguration); //NON-NLS
        this.database = database;
    }

    @NotNull
    @Override
    protected String getShellConsoleTitle() {
        return "Mongo Console";
    }

    @NotNull
    @Override
    protected Key<Boolean> getShellFile() {
        return MONGO_SHELL_FILE;
    }

    @Override
    @NotNull
    protected DatabaseVendor getDatabaseVendor() {
        return MONGO;
    }

    @Override
    @NotNull
    protected Process createProcess(@NotNull GeneralCommandLine commandLine, @NotNull ServerConfiguration serverConfiguration) throws ExecutionException {
        commandLine.addParameter(MongoUtils.buildMongoUrl(serverConfiguration, database));

        setWorkingDirectory(commandLine, serverConfiguration);

        AuthenticationSettings authenticationSettings = serverConfiguration.getAuthenticationSettings();
        addCommandlineParameter(commandLine, "--username", authenticationSettings.getUsername()); //NON-NLS
        addCommandlineParameter(commandLine, "--password", authenticationSettings.getPassword()); //NON-NLS

        MongoExtraSettings mongoExtraSettings = new MongoExtraSettings(authenticationSettings.getExtras());
        addCommandlineParameter(commandLine, "--authenticationDatabase", mongoExtraSettings.getAuthenticationDatabase()); //NON-NLS

        AuthenticationMechanism authenticationMecanism = mongoExtraSettings.getAuthenticationMechanism();
        if (authenticationMecanism != null) {
            commandLine.addParameter("--authenticationMecanism"); //NON-NLS
            commandLine.addParameter(authenticationMecanism.getMechanismName());
        }

        addShellArguments(commandLine, serverConfiguration);

        return commandLine.createProcess();
    }

    @NotNull
    @Override
    protected String getConsoleTypeId() {
        return CONSOLE_TYPE_ID;
    }
}
