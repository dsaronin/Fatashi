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
