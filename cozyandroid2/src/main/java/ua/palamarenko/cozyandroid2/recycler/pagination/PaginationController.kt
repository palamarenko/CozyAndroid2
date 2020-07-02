package ua.palamarenko.cozyandroid2.recycler.pagination

import android.annotation.SuppressLint
import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import io.reactivex.Single
import kotlinx.android.synthetic.main.view_recycler.view.*
import ua.palamarenko.cozyandroid2.CozyCell
import ua.palamarenko.cozyandroid2.CozyRecyclerView
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.CozyFragment
import ua.palamarenko.cozyandroid2.tools.LOG_EVENT


abstract class PaginationController(
    val fragment: CozyFragment<*>,
    val recycler: CozyRecyclerView,
    var firstPage: Int = 0,
    var pageSize: Int = 50
) {

    var needLoadNewPage: Boolean = true
    var currentPage = firstPage
    var lastLoadSize = -1


    fun start() {
        loadNextPage()
        recycler.listenEndList {
            if (needLoadNewPage) {
                loadNextPage()
            }
        }

    }


    private fun loadNextPage() {
        if (lastLoadSize == recycler.getItemCount() || !needLoadNewPage) {
            return
        }

        lastLoadSize = recycler.baseRecycler.layoutManager?.itemCount ?: 0

        loadPage(currentPage, pageSize).observe(fragment, Observer {
            recycler.removeProgressCell()
            if (firstPage == currentPage) {
                recycler.setCell(it.second)
            } else {
                recycler.addCell(it.second)
            }

            needLoadNewPage = it.first
            if (needLoadNewPage) {
                recycler.addProgressCell()
            }
            currentPage += 1

            Handler().postDelayed({
                if (recycler.isVisibleLastItem()) {
                    loadNextPage()
                }
            }, 200)

        })
    }


    fun restart(currentPage: Int = 0) {
        needLoadNewPage = true
        this.currentPage = currentPage
        lastLoadSize = -1
    }


    abstract fun loadPage(page: Int, pageSize: Int): MutableLiveData<Pair<Boolean, List<CozyCell>>>

}