package com.waracle.thecakelist.usecase

import com.waracle.thecakelist.api.CakeListService
import com.waracle.thecakelist.model.toCake
import kotlinx.coroutines.flow.flow
import retrofit2.await
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
        emit(cakeListService.getCakeList().await()
            .distinct()
            .map { it.toCake() }
            .sortedBy { it.title })
    }
}