The challenge took just under 3 hours. I went with an MVVM architecture using Compose, Hilt, Retrofit, and two UI libs: Coil and Swipe Refresh. The following requirements are completed:

* When the app is started, load and display a list of cakes
* Remove duplicate entries [unit tested]
* Order entries by name [unit tested]
* Display image and title for each entry
* Display a divider between each entry
* Display the cake description in some kind of popup when a cake entry is clicked
* Provide some kind of refresh option that reloads the list
* Display an error message if the list cannot be loaded (e.g. no network) [unit tested]
* Handle orientation changes, ideally without reloading the list
* Provide an option to retry when an error is presented

I did not complete the following:

* Animate in list items (e.g. fade in or fall down animations)

I had a look at using some of the default Jetpack animation methods, but these did not have the desired effect. I would probably choose to implement this "manually" using one of the finer-grained Compose animation methods; this would probably require a couple of hours of experimentation and tweaking to get right.
