package ru.practicum.android.diploma.search.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.practicum.android.diploma.search.data.dto.VacancyDto

typealias VacancyPageLoader = suspend (pageIndex: Int, pageSize: Int) -> List<VacancyDto>

class VacancyPagingSource(
    private val loader: VacancyPageLoader,
) : PagingSource<Int, VacancyDto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, VacancyDto> {
        // get the index of page to be loaded (it may be NULL, in this case let's load the first page with index = 0)
        val pageIndex = params.key ?: 0

        return try {
            // loading the desired page of data
            val vacancies = loader.invoke(pageIndex, params.loadSize)
            // success! now we can return LoadResult.Page
            return LoadResult.Page(
                data = vacancies,
                // index of the previous page if exists
                prevKey = if (pageIndex == 0) null else pageIndex - 1,
                // index of the next page if exists
                nextKey = if (vacancies.size == params.loadSize) pageIndex + 1 else null
            )
        } catch (e: Exception) {
            // failed to load data -> need to return LoadResult.Error
            LoadResult.Error(
                throwable = e
            )
        }
    }

    override fun getRefreshKey(state: PagingState<Int, VacancyDto>): Int? {
        // get the most recently accessed index in the data list:
        val anchorPosition = state.anchorPosition ?: return null
        // convert item index to page index:
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        // page doesn't have 'currentKey' property, so need to calculate it manually:
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

}
