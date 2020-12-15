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

/*  FUTURE HOOK FOR FILE PICKER
        // Request code for selecting a PDF document.
        const val PICK_PDF_FILE = 2
    fun openFile(pickerInitialUri: URI) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"

            // Optionally, specify a URI for the file that should appear in the
            // system file picker when it loads.
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
        }

        startActivityForResult(intent, PICK_PDF_FILE)
    }
*/

/*

    override fun onStart() {
        super.onStart()
        if (DEBUG) Log.d(LOG_TAG, ">>> onStart <<< ********************")
    }

    override fun onPause() {
        super.onPause()
        if (DEBUG) Log.d(LOG_TAG, ">>> onPause <<< ********************")
    }

    override fun onStop() {
        super.onStop()
        if (DEBUG) Log.d(LOG_TAG, ">>> onStop <<< ********************")
    }

    override fun onRestart() {
        super.onRestart()
        if (DEBUG) Log.d(LOG_TAG, ">>> onRestart <<< ********************")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (DEBUG) Log.d(LOG_TAG, ">>> onDestroy <<< ********************")
    }
*/
        // try to determine external public storage path
        fun dynamicExternalDownloadPath() : String? {
            if ( System.getenv("EXTERNAL_STORAGE").isNullOrBlank() ) return null
            return System.getenv("EXTERNAL_STORAGE")!! + DOWNLOAD_DIR
        }

        // Checks if a volume containing external storage is available to at least read.
        fun isExternalStorageReadable(): Boolean {
            return Environment.getExternalStorageState() in
                    setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
        }

       /* fun listDir(myContext: Context) {
            // val dynaPaths = Context.getExternalFilesDirs(Environment.DIRECTORY_DOWNLOADS)

        }*/

        fun getMyFilePath(myContext: Context) : String = dynamicExternalDownloadPath() ?: DEFAULT_PATH


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (DEBUG) Log.d(LOG_TAG, ">>> onRequestPermissionsResult <<< ")

        if (requestCode == READ_PERMISSION_CODE) {
            // Request for read file storage permission.
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start to do something
                initializeFatashiBackend()  // asynchronously initialize backend
                // returns here before completion of backend setup
            }
            else {
                // Permission request was denied.
                Snackbar.make(myLayout, R.string.read_permission_denied, Snackbar.LENGTH_LONG).show()
                // initialize builtin kamusi for demo purposes
                initializeFallbackBackend()
            }
        }
    }
    // Snackbar.make(myLayout, R.string.forced_exit, Snackbar.LENGTH_SHORT).show()
    // finishAndRemoveTask()

