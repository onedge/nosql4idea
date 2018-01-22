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

package org.codinjutsu.tools.nosql.commons.view.renderer

import com.intellij.ui.ColoredTableCellRenderer
import com.intellij.ui.treeStructure.treetable.TreeTable
import org.codinjutsu.tools.nosql.commons.view.NoSqlTreeNode
import javax.swing.JTable

class ValueCellRenderer : ColoredTableCellRenderer() {

    override fun customizeCellRenderer(table: JTable, value: Any?, selected: Boolean, hasFocus: Boolean, row: Int, column: Int) {
        val tree = (table as TreeTable).tree
        val pathForRow = tree.getPathForRow(row)
        val node = pathForRow.lastPathComponent as NoSqlTreeNode
        node.descriptor.renderValue(this, tree.isExpanded(pathForRow))
    }
}
