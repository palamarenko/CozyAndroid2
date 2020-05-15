package ua.palamarenko.cozyandroid2.recycler.pagination

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import ua.palamarenko.cozyandroid2.CozyCell
import ua.palamarenko.cozyandroid2.CozyRecyclerView
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.CozyFragment


abstract class PaginationController(
    val fragment: CozyFragment<*>,
    val recycler: CozyRecyclerView,
    var firstPage: Int = 0,
    var pageSize: Int = 50
) {

    var needLoadNewPage: Boolean = true


    fun start() {
        loadNextPage()
        recycler.listenEndList {
            if (needLoadNewPage) {
                loadNextPage()
            }
        }
        val liveData = listenAll()
        liveData.observe(fragment, Observer {
            recycler.setCell(it)
            if (needLoadNewPage(it)) {
                recycler.addProgressCell()
            } else {
                needLoadNewPage = false
            }
        })
    }


    open fun needLoadNewPage(list: List<CozyCell>): Boolean {
        return list.size % pageSize == 0 && list.isNotEmpty()
    }


    fun loadNextPage() {
        loadPage(firstPage, pageSize)
        firstPage += 1

    }

    abstract fun listenAll(): MutableLiveData<List<CozyCell>>
    abstract fun loadPage(page: Int, pageSize: Int)


}