package com.jojartbence.archeologicalfieldwork


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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


    override fun onCreate(savedInstanceState: Bundle?) {

        val loginResultObserver = Observer<Boolean?> {
            hideProgressBar()
            when(it) {
                true -> {
                    navController.navigate(R.id.action_loginFragment_to_siteListFragment)
                }
                false -> {
                    Toast.makeText(activity, "Log in failed: ${viewModel.errorMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val signupResultObserver = Observer<Boolean?> {
            hideProgressBar()
            when(it) {
                true -> {
                    navController.navigate(R.id.action_loginFragment_to_siteListFragment)
                }
                false -> {
                    Toast.makeText(activity, "Sign up failed: ${viewModel.errorMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.loginResult.observe(this, loginResultObserver)
        viewModel.signupResult.observe(this, signupResultObserver)

        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        viewModel.skipIfAlreadyLoggedIn(activity!!.applicationContext)

        logIn.setOnClickListener {
            logIn()
        }

        signUp.setOnClickListener {
            signUp()
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                return viewModel.closeApp(activity)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun logIn() {
        val email = email.text.toString()
        val password = password.text.toString()
        if (email == "" || password == "") {
            Toast.makeText(activity, "Please provide email + password", Toast.LENGTH_SHORT).show()
        } else {
            hideKeyboard()
            showProgressBar()
            viewModel.doLogin(email, password, activity!!.applicationContext)
        }
    }


    private fun signUp() {
        val email = email.text.toString()
        val password = password.text.toString()
        if (email == "" || password == "") {
            Toast.makeText(activity, "Please provide email + password", Toast.LENGTH_SHORT).show()
        }
        else {
            hideKeyboard()
            showProgressBar()
            viewModel.doSignUp(email, password, activity!!.applicationContext)
        }
    }


    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }


    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }


    fun hideKeyboard() {
        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }
}
