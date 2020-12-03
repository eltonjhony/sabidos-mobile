package com.sabidos.presentation.category

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.sabidos.R
import com.sabidos.infrastructure.Resource
import com.sabidos.infrastructure.ResourceState
import com.sabidos.infrastructure.extensions.hide
import com.sabidos.infrastructure.extensions.show
import com.sabidos.presentation.BaseActivity
import com.sabidos.presentation.common.StartToPlayCommand
import kotlinx.android.synthetic.main.activity_category.*
import org.koin.android.viewmodel.ext.android.viewModel

class CategoryActivity : BaseActivity() {

    private val viewModel: CategoryViewModel by viewModel()

    private val topsAdapter = CategoryHorizontalAdapter {
        StartToPlayCommand.playWithCategory(this, it)
    }

    private val newestAdapter = CategoryHorizontalAdapter {
        StartToPlayCommand.playWithCategory(this, it)
    }

    private val browseAllAdapter = CategoryVerticalAdapter {
        StartToPlayCommand.playWithCategory(this, it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        setupCategoryRecyclerView()

        closeView.setOnClickListener {
            super.onBackPressed()
        }

        genericErrorView.onReloadListener { viewModel.loadCategories() }

        viewModel.loadCategories()
        viewModel.categoryWrapperResource.observe(this, Observer { bindState(it) })
    }

    private fun bindState(resource: Resource<CategoryWrapper>?) {
        resource?.let {
            if (it.state != ResourceState.Loading) {
                loading(false)
            }
            when (it.state) {
                ResourceState.Success -> handleSuccess(it.data)
                ResourceState.Loading -> loading(true)
                else -> {
                    genericErrorView.show()
                    scrollView.hide()
                }
            }
        }
    }

    private fun setupCategoryRecyclerView() {

        topsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = topsAdapter
            isNestedScrollingEnabled = false
        }

        newestRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = newestAdapter
            isNestedScrollingEnabled = false
        }

        browseAllRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = browseAllAdapter
            isNestedScrollingEnabled = false
        }

    }

    private fun handleSuccess(results: CategoryWrapper?) {
        scrollView.show()
        loadingView.hide()
        genericErrorView.hide()
        topsAdapter.addItems(results?.tops)
        newestAdapter.addItems(results?.newest)
        browseAllAdapter.addItems(results?.all)
    }

    private fun loading(isLoading: Boolean) {
        if (isLoading) {
            scrollView.hide()
            genericErrorView.hide()
            loadingView.show()
            loadingView.startAnimation()
        } else {
            scrollView.show()
            loadingView.hide()
            loadingView.stopAnimation()
        }
    }

}