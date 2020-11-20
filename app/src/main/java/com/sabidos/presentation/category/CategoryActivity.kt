package com.sabidos.presentation.category

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.sabidos.R
import com.sabidos.domain.Category
import com.sabidos.infrastructure.Resource
import com.sabidos.infrastructure.ResourceState
import com.sabidos.infrastructure.extensions.goTo
import com.sabidos.infrastructure.extensions.hide
import com.sabidos.infrastructure.extensions.show
import com.sabidos.presentation.BaseActivity
import com.sabidos.presentation.quiz.SabidosQuizActivity
import kotlinx.android.synthetic.main.activity_category.*
import org.koin.android.viewmodel.ext.android.viewModel

class CategoryActivity : BaseActivity() {

    private val viewModel: CategoryViewModel by viewModel()

    private val categoryAdapter = CategoryAdapter {
        finishButton.enable()
        viewModel.selectedCategory = it
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        loadingView.setup(true)
        finishButton.disabled()
        setupCategoryRecyclerView()

        closeView.setOnClickListener {
            super.onBackPressed()
        }

        genericErrorView.onReloadListener { viewModel.loadCategories() }
        finishButton.onClickListener {
            viewModel.selectedCategory?.let {
                val bundle = Bundle()
                bundle.putInt(SabidosQuizActivity.CATEGORY_ID_BUNDLE_KEY, it.id)
                goTo(SabidosQuizActivity::class.java, false, bundle = bundle)
            } ?: goTo(SabidosQuizActivity::class.java, false)
        }

        viewModel.loadCategories()
        viewModel.categoriesResource.observe(this, Observer { bindState(it) })
    }

    private fun bindState(resource: Resource<List<Category>>?) {
        resource?.let {
            if (it.state != ResourceState.Loading) {
                loading(false)
            }
            when (it.state) {
                ResourceState.Success -> handleSuccess(it.data)
                ResourceState.Loading -> loading(true)
                else -> genericErrorView.show()
            }
        }
    }

    private fun setupCategoryRecyclerView() {
        categoryRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = categoryAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun handleSuccess(results: List<Category>?) {
        scrollView.show()
        loadingView.hide()
        genericErrorView.hide()
        categoryAdapter.addItems(results)
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