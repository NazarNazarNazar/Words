package com.antnzr.words.view.wordList

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.antnzr.words.R

class WordListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_list)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment, WordListFragment.newInstance())
        fragmentTransaction.commit()
    }
}
