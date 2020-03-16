package com.antnzr.words.view.subtitle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.antnzr.words.R
import com.antnzr.words.utils.SUBTITLE_FILE_NAME

class SubtitleActivity : AppCompatActivity(),
    SubtitleFragment.OnSubtitleFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subtitle)

        val subtitleFileName = intent.getStringExtra(SUBTITLE_FILE_NAME)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment, SubtitleFragment.newInstance(subtitleFileName))
        fragmentTransaction.commit()
    }

    override fun onFragmentInteraction() {}
}
