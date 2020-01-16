package com.antnzr.words.view.wordDetails

import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import com.antnzr.words.R
import com.antnzr.words.data.WordPair
import com.antnzr.words.utils.WEB_RESOURCE
import com.antnzr.words.utils.WORD_NEED_DETAILS


class WordDetailsActivity : AppCompatActivity(),
    WordWebFragment.OnWordWebFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_details)

        val wordPair = intent.getParcelableExtra<Parcelable>(WORD_NEED_DETAILS) as WordPair
        val resource = intent.getStringExtra(WEB_RESOURCE)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment, WordWebFragment.newInstance(resource, wordPair))
        fragmentTransaction.commit()
    }

    override fun onFragmentInteraction() {}
}