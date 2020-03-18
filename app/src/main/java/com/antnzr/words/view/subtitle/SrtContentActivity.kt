package com.antnzr.words.view.subtitle

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.antnzr.words.R
import com.antnzr.words.utils.SUBTITLE_FILE_NAME

class SrtContentActivity : AppCompatActivity(),
    SrtContentFragment.OnSrtContentFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_srt_content)

        val subtitleFileName = intent.getStringExtra(SUBTITLE_FILE_NAME)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment, SrtContentFragment.newInstance(subtitleFileName))
        fragmentTransaction.commit()
    }

    /*override fun onStart() {
        super.onStart()

        currentFocus?.let { hideSoftKeyboard(it) }
    }

    private fun hideSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

            // here is one more tricky issue
            // imm.showSoftInputMethod doesn't work well
            // and imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0) doesn't work well for all cases too
            imm?.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
        }
    }*/

    override fun onFragmentInteraction() {}
}
