package ru.practicum.android.diploma.search.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.practicum.android.diploma.search.domain.model.VacancyItem

typealias VacancyPageLoader = suspend (pageIndex: Int, pageSize: Int) -> List<VacancyItem>

class VacancyPagingSource(
    private val loader: VacancyPageLoader
) : PagingSource<Int, VacancyItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, VacancyItem> {
        // get the index of page to be loaded (it may be NULL, in this case let's load the first page with index = 0)
        val pageIndex = params.key ?: 0

        return try {
            // loading the desired page of items
            val items = loader.invoke(pageIndex, params.loadSize)
            // success! now we can return LoadResult.Page
            return LoadResult.Page(
                data = items,
                // index of the previous page if exists
                prevKey = if (pageIndex == 0) null else pageIndex - 1,
                // index of the next page if exists
                nextKey = if (items.size == params.loadSize) pageIndex + 1 else null
            )
        } catch (e: Exception) {
            // failed to load items -> need to return LoadResult.Error
            LoadResult.Error(
                throwable = e
            )
        }
    }

    override fun getRefreshKey(state: PagingState<Int, VacancyItem>): Int? {
        // get the most recently accessed index in the items list:
        val anchorPosition = state.anchorPosition ?: return null
        // convert item index to page index:
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        // page doesn't have 'currentKey' property, so need to calculate it manually:
        return page.nextKey?.minus(1) ?: page.prevKey?.plus(1)
    }
}
