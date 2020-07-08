//  transitioningView.transitionName
    // Scroll recyclerview to show clicked item from list; Important when navigating from the grid.

    private fun scrollToPosition() {

        recyclerView?.addOnLayoutChangeListener(object : OnLayoutChangeListener {

            override fun onLayoutChange(
                v: View, left: Int, top: Int, right: Int, bottom: Int,
                oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int
            ) {
                recyclerView?.removeOnLayoutChangeListener(this)

                val layoutManager = recyclerView?.layoutManager
                val viewAtPosition = layoutManager?.findViewByPosition(MainActivity.currentPosition)

                // Scroll to position if the view for the current position is null (not currently part of
                // layout manager children), or it's not completely visible.
                if (viewAtPosition == null ||
                    layoutManager.isViewPartiallyVisible(
                        viewAtPosition, false, true
                    )) {
                    recyclerView?.post { layoutManager?.scrollToPosition(MainActivity.currentPosition) }
                }
            }
        })
    } // fun


        // A similar mapping is set at the ImagePagerFragment with a setEnterSharedElementCallback.
        setExitSharedElementCallback(
            object : SharedElementCallback() {
                override fun onMapSharedElements(names: List<String>, sharedElements: MutableMap<String, View>) {
                    // Locate the KIViewHolder for the clicked position.
                    val selectedViewHolder =
                        recyclerView?.findViewHolderForAdapterPosition(MainActivity.currentPosition)
                            ?: return

                    // Map the first shared element name to the child ImageView.
                    sharedElements[names[0]] = selectedViewHolder.itemView.findViewById(R.id.itemEntry)
                }
            })


        lateinit var onLoadCompleted: (Fragment, Int) -> Unit
                    // setup a callback to check item load completion after clicked
        myHolder.onLoadCompleted = {fragment, position ->
            if (MainActivity.currentPosition == position  &&
                !enterTransitionStarted.getAndSet(true)) {
                fragment.startPostponedEnterTransition()
            }
        }
 

        fun bind(resultItem: ResultItem, myListener: (ResultItem, TextView) -> Unit) =
            with(itemView) {
                itemEntryView.text = resultItem.entry
                itemEntryView.transitionName = "transition$resultItem.position"
                itemDefinitionView.text = resultItem.definition
                itemUsageView.text = resultItem.usage
                setOnClickListener {
                        // register the onLoadCompleted callback first
                    VPShellFragment.setNextOnLoadCompleted(fragment, resultItem.position, onLoadCompleted)
                    myListener(resultItem, itemEntryView)  // then handle the click
                }
            }
    private var detailPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
         // CALLBACK: onPageSelected, entered eachtime a page changes
        override fun onPageSelected(position: Int) {
            MainActivity.currentPosition = position  // update currentPosition
            if (nextOnLoadCompleted != null) nextOnLoadCompleted(lastFragment, targetPosition)
        }
    }

    /**
     * Prepares the shared element transition from and back to the grid fragment.
     */
    private fun prepareSharedElementTransition() {
        val transition = TransitionInflater.from(context)
            .inflateTransition(R.transition.list_shared_element_transition)
        sharedElementEnterTransition = transition

        // A similar mapping is set at the GridFragment with a setExitSharedElementCallback.
        setEnterSharedElementCallback(
            object : SharedElementCallback() {
                override fun onMapSharedElements(
                    names: List<String>, sharedElements: MutableMap<String, View>
                ) {
                    // Locate the image view at the primary fragment (the ImageFragment that is currently
                    // visible). To locate the fragment, call instantiateItem with the selection position.
                    // At this stage, the method will simply return the fragment at the position and will
                    // not create a new one.
                    val currentFragment = (viewPager?.adapter as PagerAdapter).instantiateItem(
                        viewPager!!, MainActivity.currentPosition) as Fragment
                    val view = currentFragment.view ?: return

                    // Map the first shared element name to the child ImageView.
                    sharedElements[names[0]] = view.findViewById(R.id.result_item_content_entry)
                }
            })
    }

        prepareSharedElementTransition()

        // Avoid a postponeEnterTransition on orientation change, and postpone only of first creation.
        if (savedInstanceState == null) postponeEnterTransition()

    // CLASS_LEVEL: to control callback for Fragment transitioning from LIST to DETAIL views
    companion object {

        lateinit var nextOnLoadCompleted : (Fragment, Int) -> Unit
        lateinit var lastFragment : Fragment
        var targetPosition : Int = 0

        fun setNextOnLoadCompleted( prevFragment: Fragment, nextPosition: Int, anOnLoadCompletedFunction: (Fragment, Int) -> Unit ) {
            nextOnLoadCompleted = anOnLoadCompletedFunction
            lastFragment = prevFragment
            targetPosition = nextPosition
        }
    }  // companion object
  
