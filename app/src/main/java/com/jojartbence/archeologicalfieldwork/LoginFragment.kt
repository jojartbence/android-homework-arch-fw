package com.jojartbence.archeologicalfieldwork


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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


    // TODO: these lambda functions given to the viewModel should be replaced by a liveData message when viewModel authentication finishes.

    fun logIn() {
        val email = email.text.toString()
        val password = password.text.toString()
        if (email == "" || password == "") {
            Toast.makeText(activity, "Please provide email + password", Toast.LENGTH_SHORT).show()
        } else {
            showProgressBar()
            viewModel.doLogin(email,password,
                doOnSuccess = {
                    viewModel.createDatabase(activity!!.applicationContext)
                    navController.navigate(R.id.action_loginFragment_to_siteListFragment)
                },
                doOnFailure = {
                    Toast.makeText(activity, "Log in failed: ${it.message}", Toast.LENGTH_SHORT).show()
                },
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
                    viewModel.createDatabase(activity!!.applicationContext)
                    navController.navigate(R.id.action_loginFragment_to_siteListFragment)
                },
                doOnFailure = {
                    Toast.makeText(activity, "Sign up failed: ${it.message}", Toast.LENGTH_SHORT).show()
                },
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
