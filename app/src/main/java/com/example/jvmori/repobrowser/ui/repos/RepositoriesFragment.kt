package com.example.jvmori.repobrowser.ui.repos


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

import com.example.jvmori.repobrowser.R
import com.example.jvmori.repobrowser.data.base.Resource
import com.example.jvmori.repobrowser.data.repos.ReposUI
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_repositories.*
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class RepositoriesFragment : DaggerFragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var viewModel: RepositoriesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_repositories, container, false)
    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProviders.of(this, factory)[RepositoriesViewModel::class.java]
        viewModel.fetchRepos("Tetris")
        viewModel.repos.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> loading()
                Resource.Status.SUCCESS -> displayData(it.data)
                Resource.Status.ERROR -> error(it.message)
            }
        })
    }

    private fun loading() {}
    private fun error(errorMessage : String?) {
        Log.i("REPO", errorMessage)
    }
    private fun displayData(repos: List<ReposUI>?) {
        Log.i("REPO", repos.toString())
        reposRv.apply {
            adapter = ReposAdapter(repos)
        }
    }

}
