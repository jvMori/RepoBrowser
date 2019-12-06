package com.example.jvmori.repobrowser.ui.repos


import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import com.example.jvmori.repobrowser.R
import com.example.jvmori.repobrowser.data.base.local.RepoEntity
import com.example.jvmori.repobrowser.data.base.network.Resource
import com.example.jvmori.repobrowser.databinding.FragmentRepositoriesBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class RepositoriesFragment :
    DaggerFragment(),
    SearchView.OnQueryTextListener,
    SearchView.OnCloseListener {

    private lateinit var reposAdapter: ReposAdapter
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var viewModel: RepositoriesViewModel
    private lateinit var binding: FragmentRepositoriesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = ViewModelProviders.of(this, factory)[RepositoriesViewModel::class.java]
        viewModel.apply {
            observeNetworkStatus()
            observeRepositoriesSource()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        setupSearchViewInAppBar(inflater, menu)
    }

    private fun setupSearchViewInAppBar(inflater: MenuInflater, menu: Menu) {
        inflater.inflate(R.menu.menu, menu)
        val searchView = menu.findItem(R.id.action_search)?.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        searchView.setOnCloseListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindView(inflater, container)
        return binding.root
    }

    private fun bindView(inflater: LayoutInflater, container: ViewGroup?) {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_repositories, container, false)
        binding.lifecycleOwner = this
        binding.networkStatus = viewModel.networkState
    }

    override fun onStart() {
        super.onStart()
        initRecyclerView()
        viewModel.requestQuery("tetris")
        displayNetworkState()
        displayRepositories()
    }

    private fun displayRepositories() {
        viewModel.repositories.observe(viewLifecycleOwner, Observer {
            reposAdapter.submitList(it)
        })
    }

    private fun displayNetworkState() {
        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            if (it.errorMessage.isNotEmpty())
                Toast.makeText(this.requireContext(), it.errorMessage, Toast.LENGTH_SHORT).show()
        })
    }

    private fun initRecyclerView() {
        reposAdapter = ReposAdapter()
        binding.reposRv.apply {
            adapter = reposAdapter
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        viewModel.requestQuery(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        val text = newText ?: ""
        viewModel.requestQuery(text)
        return true
    }

    override fun onClose(): Boolean {
        viewModel.requestQuery("tetris")
        return false
    }
}
