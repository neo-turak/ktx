
package com.github.neoturak.db

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import java.lang.reflect.Type

/**
 * JSONHelper
 * 因为Json的初始化，反射浪费资源。
 */
class JSONHelper
private constructor() {
    private val gson: Gson
    private val gsonWithExpose: Gson

    init {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.excludeFieldsWithoutExposeAnnotation()
        gsonWithExpose = gsonBuilder.create()
        gson = Gson()
    }

    fun <MODEL> fromGson(json: String?, clazz: Class<MODEL>?): MODEL {
        return gson.fromJson(json, clazz)
    }

    fun <MODEL> fromGson(json: String?, type: Type?): MODEL {
        return gson.fromJson(json, type)
    }

    fun <MODEL> fromGsonWithExpose(json: String?, clazz: Class<MODEL>?): MODEL {
        return gsonWithExpose.fromJson(json, clazz)
    }

    fun <MODEL> fromGsonArray(json: String?, clazz: Class<MODEL>?): List<MODEL> {
        val resultList: MutableList<MODEL> = ArrayList()
        val jsonElement = JsonParser.parseString(json)
        var jsonArray: JsonArray? = null
        if (jsonElement.isJsonArray) {
            jsonArray = jsonElement.asJsonArray
        }
        if (jsonArray == null) {
            return resultList
        }
        for (js in jsonArray) {
            val model = gson.fromJson(js, clazz)
            resultList.add(model)
        }
        return resultList
    }

    fun toJSONString(obj: Any?): String {
        return gson.toJson(obj)
    }

    companion object {
        private val lockObj = Any()

        @Volatile
        private var ins: JSONHelper? = null
            get() {
                if (field == null) {
                    synchronized(lockObj) {
                        if (field == null) {
                            field = JSONHelper()
                        }
                    }
                }
                return field
            }
        val instance = ins!!
    }
}

/**
 * 实体类转Json
 */
fun Any.toJson(): String {
    return JSONHelper.instance.toJSONString(this)
}
