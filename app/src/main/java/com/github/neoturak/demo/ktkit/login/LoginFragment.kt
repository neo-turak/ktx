package com.github.neoturak.demo.ktkit.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.github.neoturak.demo.ktkit.R
import com.github.neoturak.demo.ktkit.databinding.FragmentLoginBinding
import com.hi.dhl.binding.viewbind
import com.github.neoturak.demo.ktkit.model.PeopleModel
import com.github.neoturak.ktkit.ui.intent
import com.github.neoturak.ktkit.ui.makeBundle

/**
 * <pre>
 *     author: dhl
 *     date  : 2021/7/18
 *     desc  :
 * </pre>
 */
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val binding by viewbind<FragmentLoginBinding>()

    private val userName by intent<String>(KEY_USER_NAME) {
        "default"
    }
    private val userPassword by intent<String>(KEY_USER_PASSWORD)
    private val peopleModel by intent<PeopleModel>(KEY_PEOPLE_PARCELIZE)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            val content = "userNnam = $userName userPassword = $userPassword   people = $peopleModel"
            textResult.setText(content)
        }
    }

    companion object {
        const val KEY_USER_NAME = "userName"
        const val KEY_USER_PASSWORD = "userPassWord"
        const val KEY_PEOPLE_PARCELIZE = "peopleParcelize"

        fun newInstance1(): Fragment {
            return LoginFragment().makeBundle(
                KEY_USER_NAME to "ByteCode",
                KEY_USER_PASSWORD to "1024",
                KEY_PEOPLE_PARCELIZE to PeopleModel("hi-dhl")
            )
        }

        fun newInstance2(): Fragment {
            return LoginFragment().makeBundle {
                arrayOf(
                    KEY_USER_NAME to "ByteCode",
                    KEY_USER_PASSWORD to "1024",
                    KEY_PEOPLE_PARCELIZE to PeopleModel("hi-dhl")
                )
            }
        }
    }
}
