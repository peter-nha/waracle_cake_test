package com.waracle.thecakelist

import com.waracle.thecakelist.api.CakeDTO
import com.waracle.thecakelist.api.CakeListService
import com.waracle.thecakelist.api.ServiceResponse
import com.waracle.thecakelist.model.Cake
import com.waracle.thecakelist.usecase.GetAllCakesUseCase
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Timeout
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class GetAllCakesUseCaseTest {

    @Test
    fun `cakes loaded from the API should be de-duped`() = runTest {

        // TODO: preferable to use basic factories to create models for unit tests
        val listFromAPI = listOf(
            CakeDTO(
                title = "Title 1",
                image = "Image 1",
                desc = "Desc 1",
            ),
            CakeDTO(
                title = "Title 2",
                image = "Image 2",
                desc = "Desc 2",
            ),
            CakeDTO(
                title = "Title 1",
                image = "Image 1",
                desc = "Desc 1",
            ),
        )

        val parsedList = GetAllCakesUseCase(TestCakeListService(listFromAPI)).invoke()
            .first() as ServiceResponse.Success
        assertEquals(2, parsedList.data.count())
    }

    @Test
    fun `cakes loaded from the API should be sorted by name`() = runTest {

        // TODO: preferable to use basic factories to create models for unit tests
        val listFromAPI = listOf(
            CakeDTO(
                title = "Title C",
                image = "",
                desc = "",
            ),
            CakeDTO(
                title = "Title A",
                image = "",
                desc = "",
            ),
            CakeDTO(
                title = "Title B",
                image = "",
                desc = "",
            ),
        )

        val parsedList = GetAllCakesUseCase(TestCakeListService(listFromAPI)).invoke()
            .first() as ServiceResponse.Success
        assertEquals(
            Cake(
                title = "Title A",
                image = "",
                desc = "",
            ),
            parsedList.data[0]
        )
        assertEquals(
            Cake(
                title = "Title B",
                image = "",
                desc = "",
            ),
            parsedList.data[1]
        )
        assertEquals(
            Cake(
                title = "Title C",
                image = "",
                desc = "",
            ),
            parsedList.data[2]
        )
    }

    @Test
    fun `confirm API error causes error condition in usecase response`() = runTest {

        val listFromAPI = listOf(
            CakeDTO(
                title = "Title A",
                image = "",
                desc = "",
            ),
        )

        assertThat(
            GetAllCakesUseCase(TestCakeListService(listFromAPI, true)).invoke()
                .first(), instanceOf(ServiceResponse.Failure::class.java)
        )
    }
}

// TODO: many more sophisticated ways of doing this, for example:
// * we could create a more general-purpose mock implementation of Call
// * we could utilise a MockWebServer instead, especially if we are trying to create an instrumented test
// * we could do something clever with Hilt
class TestCakeResponseCall(
    private val requiredResponse: List<CakeDTO>,
    private val shouldError: Boolean
) : Call<List<CakeDTO>> {
    override fun clone(): Call<List<CakeDTO>> {
        return this
    }

    override fun execute(): Response<List<CakeDTO>> {
        return if (shouldError) Response.error(
            500,
            "test error".toResponseBody("text/plain".toMediaTypeOrNull())
        ) else Response.success(requiredResponse)
    }

    override fun enqueue(callback: Callback<List<CakeDTO>>) {
        callback.onResponse(this, execute())
    }

    override fun isExecuted(): Boolean {
        return false
    }

    override fun cancel() {
    }

    override fun isCanceled(): Boolean {
        return false
    }

    override fun request(): Request {
        return Request.Builder().build()
    }

    override fun timeout(): Timeout {
        return Timeout()
    }
}

class TestCakeListService(
    private val requiredResponse: List<CakeDTO>,
    private val shouldError: Boolean = false
) : CakeListService {
    override fun getCakeList(): Call<List<CakeDTO>> {
        return TestCakeResponseCall(requiredResponse, shouldError)
    }
}