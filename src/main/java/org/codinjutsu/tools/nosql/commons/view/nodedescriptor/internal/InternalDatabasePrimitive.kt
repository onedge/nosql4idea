package org.codinjutsu.tools.nosql.commons.view.nodedescriptor.internal

import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabasePrimitive

internal class InternalDatabasePrimitive(private val value: Any?) : InternalDatabaseElement(), DatabasePrimitive {

    override fun isBoolean() =
            if (value is DatabasePrimitive) {
                value.isBoolean()
            } else {
                value is Boolean
            }

    override fun isNumber() =
            if (value is DatabasePrimitive) {
                value.isNumber()
            } else {
                value is Number
            }

    override fun isString() =
            if (value is DatabasePrimitive) {
                value.isString()
            } else {
                value is String
            }

    override fun asBoolean() =
            if (value is DatabasePrimitive) {
                value.asBoolean()
            } else {
                value as Boolean
            }

    override fun asNumber() =
            if (value is DatabasePrimitive) {
                value.asNumber()
            } else {
                value as Number
            }

    override fun asString() =
            if (value is DatabasePrimitive) {
                value.asString()
            } else {
                value as String
            }

    override fun value() = value

    override fun toString() = value.toString()
}