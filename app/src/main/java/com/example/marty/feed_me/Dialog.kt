package com.example.marty.feed_me

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.EditText
import com.example.marty.feed_me.data.Recipe
import kotlinx.android.synthetic.main.dialog_add.view.*

class Dialog : DialogFragment() {

    interface ItemHandler {
        fun itemCreated(item: Recipe)
    }

    private lateinit var itemHandler: ItemHandler

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is ItemHandler) {
            itemHandler = context
        } else {
            throw RuntimeException("Something")
        }
    }


    private lateinit var cityName: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("Find Recipe")

        val rootView = requireActivity().layoutInflater.inflate(R.layout.dialog_add, null)
        cityName = rootView.etRecipe
        builder.setView(rootView)

        builder.setPositiveButton("Ok") {
            dialog, which -> //empty
        }

        return builder.create()
    }

    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (cityName.text.isNotEmpty()) {
                handleItemCreate()
                dialog.dismiss()
            }
            else {
                cityName.error = "Field Must Not Be Empty!"
            }
        }
    }

    private fun handleItemCreate() {
        itemHandler.itemCreated(
                Recipe(null, cityName.text.toString())
        )
    }
}