package com.waracle.thecakelist.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import coil.compose.AsyncImage
import com.waracle.thecakelist.model.Cake
import com.waracle.thecakelist.viewmodel.CakeListViewModel

@Composable
fun CakeListView(
    cakeListViewModel: CakeListViewModel,
) {

    // composing with lifecycleOwner first, to make sure not collected when not started
    // TODO: there is a cool extension to trim this boilerplate here https://proandroiddev.com/how-to-collect-flows-lifecycle-aware-in-jetpack-compose-babd53582d0b
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleAwareCakes = remember(cakeListViewModel.getAllCakes, lifecycleOwner) {
        cakeListViewModel.getAllCakes.flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        )
    }

    val cakes: List<Cake> by lifecycleAwareCakes.collectAsState(initial = emptyList())

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(cakes) { cake ->
            CakeRow(cake)
        }
    }
}

@Composable
fun CakeRow(
    cake: Cake,
) {
    Row(modifier = Modifier.height(128.dp)) {
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