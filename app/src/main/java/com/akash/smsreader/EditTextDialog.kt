package com.akash.smsreader

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class EditTextDialog : DialogFragment() {
    companion object {
        private const val TAG = "EditTextDialog"

        private const val EXTRA_TITLE = "title"
        private const val EXTRA_INFO_HINT = "hint"

        fun newInstance(title: String? = null, infoHint: String? = null): EditTextDialog {
            val dialog = EditTextDialog()
            val args = Bundle().apply {
                putString(EXTRA_TITLE, title)
                putString(EXTRA_INFO_HINT, infoHint)
            }
            dialog.arguments = args
            return dialog
        }
    }

    lateinit var tags: EditText
    lateinit var info: TextView
    var onOk: (() -> Unit)? = null
    var onCancel: (() -> Unit)? = null
    var onDelete: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val title = arguments?.getString(EXTRA_TITLE)
        val tagsHint = "Separate tags by comma(,)"
        val infoHint = arguments?.getString(EXTRA_INFO_HINT)

        val view = activity!!.layoutInflater.inflate(R.layout.dialog_layout, null)

        this.tags = view.findViewById(R.id.tagsText)
        this.tags.hint = tagsHint
        this.info = view.findViewById(R.id.infoText)

        val builder = AlertDialog.Builder(context!!)
            .setTitle(title)
            .setView(view)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                onOk?.invoke()
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->
                onCancel?.invoke()
            }
            .setNeutralButton("Delete Message") { _, _ ->
                onDelete?.invoke()
            }
        val dialog = builder.create()

        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

        return dialog
    }
}