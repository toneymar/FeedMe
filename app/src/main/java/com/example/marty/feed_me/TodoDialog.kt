package com.example.marty.feed_me

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.EditText
import com.example.marty.feed_me.data.Todo
import java.lang.RuntimeException


class TodoDialog : DialogFragment() {

    interface TodoHandler {
        fun todoCreated(item: Todo)
    }


    override fun onStart() {
        super.onStart()

        val window = dialog.window
        window.setBackgroundDrawableResource(R.drawable.side_nav_bar)
    }


    private lateinit var todoHandler: TodoHandler

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is TodoHandler) {
            todoHandler = context
        } else {
            throw RuntimeException(getString(R.string.todo_runtime_exception))
        }
    }

    private lateinit var etItemName: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle(getString(R.string.new_city))

        val rootView = requireActivity().layoutInflater.inflate(
                R.layout.dialog_todo, null
        )

        etItemName = rootView.etItemName

        builder.setView(rootView)

        builder.setPositiveButton(getString(R.string.okay)) { dialog, witch ->
        }


        return builder.create()
    }

    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (etItemName.text.isNotEmpty()) {
                val arguments = this.arguments

                handleTodoCreate()

                dialog.dismiss()
            } else {
                etItemName.error = getString(R.string.empty_city_name)
            }
        }
    }


    private fun handleTodoCreate() {
        todoHandler.todoCreated(
                Todo(
                        null,
                        etItemName.text.toString()

                )
        )
    }
}
