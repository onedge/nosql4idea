package org.codinjutsu.tools.nosql.mongo.view.wrapper

import com.mongodb.BasicDBList
import com.mongodb.BasicDBObject
import com.mongodb.DBObject
import org.codinjutsu.tools.nosql.commons.view.wrapper.ObjectWrapper

internal class MongoObjectWrapper(private val dbObject: DBObject) : ObjectWrapper {

    override val names: Collection<String>
        get() = dbObject.keySet()

    override fun get(name: String): Any? = dbObject.get(name)

    override fun isArray(value: Any?) = value is BasicDBList

    override fun isObject(value: Any?) = value is BasicDBObject
}