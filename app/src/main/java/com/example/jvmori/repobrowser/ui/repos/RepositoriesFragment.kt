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
import androidx.paging.PagedList
import com.example.jvmori.repobrowser.R
import com.example.jvmori.repobrowser.data.base.local.RepoEntity
import com.example.jvmori.repobrowser.data.base.network.Resource
import com.example.jvmori.repobrowser.databinding.FragmentRepositoriesBinding
import com.example.jvmori.repobrowser.databinding.LoadingBinding
import com.example.jvmori.repobrowser.utils.NetworkState
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
        viewModel.fetchRepos()
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
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_repositories, container, false)
        binding.lifecycleOwner = this
        binding.networkStatus = viewModel.networkState
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerView()
        viewModel.observeNetworkStatus()
        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            if (it.errorMessage.isNotEmpty())
                Toast.makeText(this.requireContext(), it.errorMessage, Toast.LENGTH_SHORT).show()
        })
        viewModel.configurePublishSubject()
        viewModel.results.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> success(it.data)
                Resource.Status.ERROR -> error(it.message)
            }
        })
    }

    private fun initRecyclerView() {
        reposAdapter = ReposAdapter()
        binding.reposRv.apply {
            adapter = reposAdapter
        }
    }

    private fun success(data: PagedList<RepoEntity>?) {
        reposAdapter.submitList(data)
    }

    private fun error(errorMessage: String?) {
        Toast.makeText(this.requireContext(), errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        viewModel.onQueryTextChange(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        val text = newText ?: ""
        viewModel.onQueryTextChange(text)
        return true
    }

    override fun onClose(): Boolean {
        viewModel.fetchRepos()
        return false
    }
}
