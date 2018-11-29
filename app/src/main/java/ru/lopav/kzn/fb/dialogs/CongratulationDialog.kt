package ru.lopav.kzn.fb.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.dialog_layout.*
import ru.lopav.kzn.fb.R

class CongratulationDialog: DialogFragment() {

    var listener: DialogListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.dialog_layout, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val state = arguments?.getBoolean(IS_WIN, false) ?: false
        if (!state) {
            setSadState()
        }
        again.setOnClickListener {
            listener?.onButtonClick()
            dismissAllowingStateLoss()
        }
    }

    private fun setSadState() {
        icon.setImageResource(R.drawable.ic_sad)
        title.text = getString(R.string.lose)
        description.text = getString(R.string.lose_text)
    }

    companion object {

        private const val IS_WIN = "key"

        fun getInstance(state: Boolean): CongratulationDialog =
                CongratulationDialog().apply {
                    arguments = Bundle().apply {
                        putBoolean(IS_WIN, state)
                    }
                }
    }
}

interface DialogListener {
    fun onButtonClick()
}
