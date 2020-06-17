package org.umoja4life.fatashi

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

const val TAFUTA_MAULIZO = "org.umoja4life.fatashi.TAFUTA_MAULIZO"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }
    /** Called when the user taps the Send button
     * Android requirements:
     * - public access
     * - implicit unit return value
     * - View as only parameter (the view object)
     * */

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
}