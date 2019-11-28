package com.example.jvmori.repobrowser.ui.repos


import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.jvmori.repobrowser.R
import com.example.jvmori.repobrowser.data.base.network.Resource
import com.example.jvmori.repobrowser.databinding.FragmentRepositoriesBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class RepositoriesFragment :
    DaggerFragment(),
    SearchView.OnQueryTextListener,
    SearchView.OnCloseListener {

    private lateinit var reposAdapter: ReposAdapter

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        val text = newText ?: ""
        viewModel?.onQueryTextChange(text)
        if (text.isEmpty())
            initRecyclerView()
        return true
    }

    override fun onClose(): Boolean {
        viewModel.fetchTetrisRepos()
        return false
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var viewModel: RepositoriesViewModel
    private lateinit var binding: FragmentRepositoriesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
        val searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        searchView.setOnCloseListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_repositories, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProviders.of(this, factory)[RepositoriesViewModel::class.java]
        initRecyclerView()
        displayTetrisRepos()
    }

    private fun displayTetrisRepos() {
        viewModel.fetchReposLiveData("tetris").observe(this, Observer {
            reposAdapter.submitList(it)
        })
        //viewModel.configurePublishSubject()
        //viewModel.fetchTetrisRepos()
        //observeResults()
    }

    private fun observeResults() {
        viewModel.repos.observe(this, Observer
        {
            when (it.status) {
                Resource.Status.LOADING -> loading()
               // Resource.Status.SUCCESS -> initRecyclerView(it.data)
                Resource.Status.ERROR -> error(it.message)
            }
        })
    }


    private fun loading() {
        binding.loading.visibility = View.VISIBLE
    }

    private fun error(errorMessage: String?) {
        binding.loading.visibility = View.GONE
        Toast.makeText(this.requireContext(), errorMessage, Toast.LENGTH_LONG).show()
    }

    private fun initRecyclerView() {
        binding.loading.visibility = View.GONE
        reposAdapter = ReposAdapter()
        binding.reposRv.apply {
            adapter = reposAdapter
        }
    }
}
