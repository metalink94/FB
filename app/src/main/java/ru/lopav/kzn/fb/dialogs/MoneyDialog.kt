package ru.lopav.kzn.fb.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.money_dialog.*
import ru.lopav.kzn.fb.R

class MoneyDialog: DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.money_dialog, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        description.text = Html.fromHtml("Не стоит огорчаться. У Вас закончилась игровая валюта. Но наш банк уже пополнил Ваш счёт чтобы Вы смогли продолжить играть!<br><br><b>Напоминаем ещё раз, данная валюта игровая и ничего больше!</b><br>Удачной игры!")
        start.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }
}
