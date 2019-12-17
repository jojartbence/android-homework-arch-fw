package com.jojartbence.archeologicalfieldwork


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_login.*


/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    private val viewModel by lazy { ViewModelProviders.of(this)[LoginViewModel::class.java] }

    lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        logIn.setOnClickListener {
            logIn()
        }

        signUp.setOnClickListener {
            signUp()
        }
    }


    fun logIn() {
        val email = email.text.toString()
        val password = password.text.toString()
        if (email == "" || password == "") {
            Toast.makeText(activity, "Please provide email + password", Toast.LENGTH_SHORT).show()
        } else {
            showProgressBar()
            viewModel.doLogin(email,password,
                doOnSuccess = {
                    navController.navigate(R.id.action_loginFragment_to_siteListFragment)
                },
                doOnFailure = {},
                doOnComplete = {
                    hideProgressBar()
                })
        }
    }


    fun signUp() {
        val email = email.text.toString()
        val password = password.text.toString()
        if (email == "" || password == "") {
            Toast.makeText(activity, "Please provide email + password", Toast.LENGTH_SHORT).show()
        }
        else {
            showProgressBar()
            viewModel.doSignUp(email,password,
                doOnSuccess = {
                    navController.navigate(R.id.action_loginFragment_to_siteListFragment)
                },
                doOnFailure = {},
                doOnComplete = {
                    hideProgressBar()
                })
        }
    }


    fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }


    fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }
}
