package com.wizinc.renteasyjm.dialog

import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.wizinc.renteasyjm.R

fun Fragment.setupBottomSheetDialog(
    onSendClick: (String) -> Unit

)
{
    val dialog = BottomSheetDialog(requireContext(),R.style.DialogStyle)
    val view = layoutInflater.inflate(R.layout.reset_password_dialog, null)
    dialog.setContentView(view)
    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    dialog.show()


    val edEmail = view.findViewById<EditText>(R.id.edResetPassword)
    val btnSend = view.findViewById<Button>(R.id.sendButton)
    val btnCancel = view.findViewById<Button>(R.id.cancelButton)

    btnSend.setOnClickListener{
        val email = edEmail.text.toString().trim()
        onSendClick(email)
        dialog.dismiss()

    }

    btnCancel.setOnClickListener{
        dialog.dismiss()
    }

}