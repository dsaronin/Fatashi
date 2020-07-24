// onCreate
// Floating Action Button Usage

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

// responding to an action by initiating new activity
// for example in 
        const val TAFUTA_MAULIZO = "org.umoja3life.fatashi.TAFUTA_MAULIZO"

    fun searchRequest(view: View) {
        // todo: read contents of search field and deliver to another activity
        //
        val editText = findViewById<EditText>(R.id.editTextTextPersonName)
        val maulizo = editText.text.toString()

        val intent = Intent(this, ScrollingActivity::class.java).apply {
            putExtra(TAFUTA_MAULIZO, maulizo)
        }
        startActivity(intent)
      }

// to lookup an existing fragment
val myfragment: KamusiItemFragment?
              = supportFragmentManager.findFragmentById(R.id.list_fragment) as KamusiItemFragment?
       
// LOGGER usage
val LOG_TAG = "RecyclerViewAdapter";

        Log.d(LOG_TAG, "onCreateViewHolder (" + ++counterOnCreateViewHolder + ")");

        Log.d(LOG_TAG, "onBindViewHolder (" + ++counterOnBindViewHolder + ")");

// from ItemFragment.kt :: onCreateView
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = KamusiItemRecyclerViewAdapter(ResultsContent.RESULT_ITEMS)
            }
        }
    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            KamusiItemFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }

=============================
how to replace a fragment

        supportFragmentManager.beginTransaction()
            .setTransition( FragmentTransaction.TRANSIT_NONE )
            .replace(R.id.list_fragment, KamusiItemFragment() )
            .addToBackStack( maulizo )
            .commit()
============================
        // Floating Action Button Usage
        // findViewById<FloatingActionButton>(R.id.fab_read)
        //    .setOnClickListener { view -> doSomeInput(view) }


// DEBUG USAGE ONLY: doSomeInput was for testing purposes
    private fun doSomeInput(view: View) {
        TODO("DEBUGGING vestige only in doSomeInput()")
        if (DEBUG) Log.d(LOG_TAG, ">>> doSomeInput <<< ")
        val perm = FileServices.hasReadPermission(this)
        // Snackbar.make(view, "Permission state: $perm", Snackbar.LENGTH_LONG).show()

        // (getActivity() as MainActivity).
        if (perm) {
            myViewModel.getKamusiFormatJson(
                "config_properties.json",
                onSuccess = {kf ->
                    Toast.makeText(this, kf.filename, Toast.LENGTH_LONG).show()
                    kf
                },
                onFail = {err -> Toast.makeText(this, err, Toast.LENGTH_LONG).show()}
            )
        }  // fi perm
    }


    /*
    A lambda expression or anonymous function
    (as well as a local function and an object expression)
    can access its closure, i.e. the variables declared in the outer scope.
    The variables captured in the closure can be modified in the lambda
    */
    // displayLambda  -- will be used within AndroidPlatform.listOut() to process the results
    // list and display it view RecyclerViewAdapter.
    private val displayLambda : (List<String>) -> Unit = { listResults ->
        var myfragment  = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (DEBUG) Log.d(LOG_TAG, ">>> displayLambda <<< $this, $myfragment")

        if ( myfragment is VPShellFragment ) {  // oops, still on the detail view fragment
            supportFragmentManager.popBackStackImmediate()  // pop back to KamusiItemFragment
            // now get the fragment supporting that view
            myfragment  = supportFragmentManager.findFragmentById(R.id.fragment_container)
        }
        (myfragment as KamusiItemFragment).updateFragmentResults( listResults )
    }


