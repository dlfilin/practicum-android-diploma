package ru.practicum.android.diploma.search.data.repository

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import ru.practicum.android.diploma.common.data.network.NetworkClient
import ru.practicum.android.diploma.common.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.common.util.ErrorType
import ru.practicum.android.diploma.common.util.Result
import ru.practicum.android.diploma.filter.domain.models.FilterParameters
import ru.practicum.android.diploma.search.data.dto.VacancySearchRequest
import ru.practicum.android.diploma.search.data.dto.VacancySearchResponse
import ru.practicum.android.diploma.search.domain.model.VacancyItem

typealias VacancyPageLoader = suspend (pageIndex: Int, pageSize: Int) -> List<VacancyItem>

class VacancyPagingSource(
    private val networkClient: NetworkClient,
    private val text: String,
    private val filter: FilterParameters
) : PagingSource<Int, VacancyItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, VacancyItem> {
        // get the index of page to be loaded (it may be NULL, in this case let's load the first page with index = 0)
        val pageIndex = params.key ?: 0

        return try {
            // loading the desired page of items
            var items = emptyList<VacancyItem>()

            val result = networkClient.doRequest(
                VacancySearchRequest(prepareSearchQueryMap(text, pageIndex, params.loadSize, filter))
            )
            when (result) {
                is Result.Success -> {
                    items = (result.data as VacancySearchResponse).items.map { it.toVacancyItem() }
                }
                is Result.Error -> {
                    when (result.errorType) {
                        ErrorType.NO_INTERNET -> throw Exception("NO_INTERNET")
                        ErrorType.NON_200_RESPONSE -> throw Exception("NON_200_RESPONSE")
                        ErrorType.SERVER_THROWABLE -> throw Exception("SERVER_THROWABLE")
                        null -> Unit
                    }
                }
            }

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
            e.message?.let { Log.d("XXX", it) }
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

    private fun prepareSearchQueryMap(
        text: String,
        pageIndex: Int = 0,
        pageSize: Int = 10,
        filter: FilterParameters
    ): Map<String, String> {
        val map: HashMap<String, String> = HashMap()
        map["text"] = text
        map["page"] = pageIndex.toString()
        map["per_page"] = pageSize.toString()

        if (filter.area != null) {
            map["area"] = filter.area.id
        }
        if (filter.industry != null) {
            map["industry"] = filter.industry.id
        }
        if (filter.salary != null) {
            map["salary"] = filter.salary.toString()
        }
        if (filter.onlyWithSalary) {
            map["only_with_salary"] = "true"
        }
        return map
    }
}
