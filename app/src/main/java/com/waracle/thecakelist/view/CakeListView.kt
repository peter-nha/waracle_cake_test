package com.waracle.thecakelist.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.waracle.thecakelist.R
import com.waracle.thecakelist.model.Cake
import com.waracle.thecakelist.viewmodel.CakeListViewModel

@Composable
fun CakeListView(
    cakeListViewModel: CakeListViewModel,
) {

    // composing with lifecycleOwner first, to make sure not collected when not started
    // TODO: there is a cool extension to trim this boilerplate here https://proandroiddev.com/how-to-collect-flows-lifecycle-aware-in-jetpack-compose-babd53582d0b
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleAwareCakes = remember(cakeListViewModel.allCakes, lifecycleOwner) {
        cakeListViewModel.allCakes.flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        )
    }

    val lifecycleAwareCakeDetails =
        remember(cakeListViewModel.displayedCakeDetails, lifecycleOwner) {
            cakeListViewModel.displayedCakeDetails.flowWithLifecycle(
                lifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            )
        }

    val lifecycleAwareErrorMessage =
        remember(cakeListViewModel.errorMessage, lifecycleOwner) {
            cakeListViewModel.errorMessage.flowWithLifecycle(
                lifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            )
        }

    val lifecycleAwareIsLoading =
        remember(cakeListViewModel.isLoading, lifecycleOwner) {
            cakeListViewModel.isLoading.flowWithLifecycle(
                lifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            )
        }

    val cakes: List<Cake> by lifecycleAwareCakes.collectAsState(initial = emptyList())
    val displayedCakeDetails: String? by lifecycleAwareCakeDetails.collectAsState(initial = null)
    val errorMessage: String? by lifecycleAwareErrorMessage.collectAsState(initial = null)
    val isLoading: Boolean by lifecycleAwareIsLoading.collectAsState(initial = false)

    displayedCakeDetails?.let { details ->
        AlertDialog(
            onDismissRequest = { cakeListViewModel.showCakeDetails(null) },
            title = {
                Text(stringResource(id = R.string.cake_details))
            },
            text = {
                Text(details)
            },
            confirmButton = {
                Button(
                    onClick = {
                        cakeListViewModel.showCakeDetails(null)
                    }) {
                    Text(stringResource(id = R.string.ok))
                }
            },
        )
    }

    errorMessage?.let { error ->
        AlertDialog(
            onDismissRequest = { cakeListViewModel.clearErrorMessage() },
            title = {
                Text(stringResource(id = R.string.error_message))
            },
            text = {
                Text(error)
            },
            dismissButton = {
                Button(
                    onClick = {
                        cakeListViewModel.clearErrorMessage()
                    }) {
                    Text(stringResource(id = R.string.ok))
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        cakeListViewModel.clearErrorMessage()
                        cakeListViewModel.refresh()
                    }) {
                    Text(stringResource(id = R.string.try_again))
                }
            },
        )
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isLoading),
        onRefresh = {
            cakeListViewModel.refresh()
        },
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(cakes) { cake ->
                CakeRow(cakeListViewModel, cake)
            }
        }
    }
}

@Composable
fun CakeRow(
    cakeListViewModel: CakeListViewModel,
    cake: Cake,
) {
    Row(modifier = Modifier
        .height(128.dp)
        .clickable {
            cakeListViewModel.showCakeDetails(cake.desc)
        }) {
        AsyncImage(
            model = cake.image,
            contentDescription = null,
            modifier = Modifier
                .fillMaxHeight()
                .width(128.dp)
                .padding(4.dp),
            contentScale = ContentScale.Inside,
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(
                text = cake.title,
                modifier = Modifier.padding(4.dp),
            )
        }
    }

    Divider(color = Color.LightGray)
}