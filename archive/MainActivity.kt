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


