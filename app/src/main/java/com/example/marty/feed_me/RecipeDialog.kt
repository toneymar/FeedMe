package com.example.marty.feed_me

import android.content.Context
import android.support.v4.app.DialogFragment
import android.widget.EditText
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import com.example.marty.feed_me.data.Favorite
import kotlinx.android.synthetic.main.dialog_recipe.view.*

class RecipeDialog : DialogFragment() {

    interface RecipeHandler {
        fun recipeCreated(recipe: Favorite)
    }

    override fun onStart() {
        super.onStart()

        val window = dialog.window
        window.setBackgroundDrawableResource(R.color.MedTorquoise)
    }

    private lateinit var recipeHandler: RecipeHandler

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is RecipeHandler) {
            recipeHandler = context
        } else {
            throw RuntimeException(
                    getString(R.string.runtime_exception))
        }
    }

    private lateinit var etRecipeText: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.builder_title))

        val rootView = requireActivity().layoutInflater.inflate(
                R.layout.dialog_recipe, null)

        etRecipeText = rootView.etRecipeText
        builder.setView(rootView)

        builder.setPositiveButton(getString(R.string.ok)) {
            dialog, witch ->
        }
        return builder.create()
    }

    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (etRecipeText.text.isNotEmpty()) {
                handleRecipeCreate()
                dialog.dismiss()
            } else {
                etRecipeText.error = getString(R.string.field_not_empty)
            }
        }
    }
    private fun handleRecipeCreate() {
        (context as MainActivity).initRecyclerView(etRecipeText.text.toString())
    }
}