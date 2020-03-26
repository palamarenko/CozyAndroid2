package ua.palamarenko.cozyandroid

import android.content.Context
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import ua.palamarenko.cozyandroid2.tools.LOG_EVENT
import java.util.*
import kotlin.collections.ArrayList

var parentId: String? = null

fun convertJson(context: Context) : String {
    val file_name = "output.txt"
    val string = context.assets.open(file_name).bufferedReader().use {
        it.readText()
    }



    val list = ArrayList<SpecializationsDto>()

    string.split('@').forEach {
        it.split("\n").filter { it.isNotEmpty() }.forEachIndexed { index, s ->

            if (index == 0) {
                val id = UUID.randomUUID().toString()
                parentId = id
                list.add(SpecializationsDto(id, s, null))
            } else {
                val id = UUID.randomUUID().toString()
                SpecializationsDto(id, s, parentId)
                list.add(SpecializationsDto(id, s, parentId))
            }
        }
    }

    LOG_EVENT("HELLO!!", Gson().toJson(list))
    return Gson().toJson(list)
}


data class SpecializationsDto(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("parentId")
    val parentId: String?
)