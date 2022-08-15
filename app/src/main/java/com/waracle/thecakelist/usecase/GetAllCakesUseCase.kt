package com.waracle.thecakelist.usecase

import com.waracle.thecakelist.api.CakeListService
import com.waracle.thecakelist.api.ServiceResponse
import com.waracle.thecakelist.model.Cake
import com.waracle.thecakelist.model.toCake
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.awaitResponse
import javax.inject.Inject

/**
 * retrieves a list of cakes from the API and applies the following rules:
 * a. Remove duplicate entries
 * b. Order entries by name
 *
 * TODO: In a more sophisticated codebase, the retrieval and parsing of entries may be split into
 * two use cases, but this should suffice for this example.
 *
 * TODO: we may want to make this an interface + implementation, for easier mocking etc
 */
class GetAllCakesUseCase @Inject constructor(private val cakeListService: CakeListService) {
    operator fun invoke() = flow {
        val response = cakeListService.getCakeList().awaitResponse()
        if (response.isSuccessful) {
            emit(ServiceResponse.Success(
                response.body()
                    ?.distinct()
                    ?.map { it.toCake() }
                    ?.sortedBy { it.title }
                    ?: emptyList<Cake>()))
        } else {
            emit(
                ServiceResponse.Failure(
                    response.errorBody()?.toString() ?: "Unknown Error"
                )
            ) // TODO: not nice to dump server errors to user, preferably have some nicer messages
        }
    }.catch { e ->
        emit(
            ServiceResponse.Failure(
                e.message ?: "Unknown Error"
            )
        ) // TODO: not nice to dump server errors to user, preferably have some nicer messages
    }
}