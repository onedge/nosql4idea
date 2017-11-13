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

package org.codinjutsu.tools.nosql.commons.view.action.edition;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.codinjutsu.tools.nosql.commons.view.AbstractEditionPanel;
import org.codinjutsu.tools.nosql.commons.view.AddKeyDialog;

import java.awt.event.KeyEvent;

public class AddKeyAction extends AnAction {

    private final AbstractEditionPanel editionPanel;

    public AddKeyAction(AbstractEditionPanel editionPanel) {
        super("Add a key", "Add a key", AllIcons.General.Add);
        registerCustomShortcutSet(KeyEvent.VK_INSERT, KeyEvent.ALT_MASK, editionPanel);
        this.editionPanel = editionPanel;
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        AddKeyDialog dialog = AddKeyDialog.createDialog(editionPanel);
        dialog.show();

        if (!dialog.isOK()) {
            return;
        }

        editionPanel.addKey(dialog.getKey(), dialog.getValue());
    }

    @Override
    public void update(AnActionEvent event) {
//        event.getPresentation().setVisible(editionPanel.canAddKey());
    }
}