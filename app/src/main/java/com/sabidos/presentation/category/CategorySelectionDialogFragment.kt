package com.sabidos.presentation.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.sabidos.R
import com.sabidos.domain.Category
import com.sabidos.infrastructure.Resource
import com.sabidos.infrastructure.ResourceState.Loading
import com.sabidos.infrastructure.ResourceState.Success
import com.sabidos.infrastructure.extensions.dpToPx
import com.sabidos.infrastructure.extensions.goTo
import com.sabidos.infrastructure.extensions.hide
import com.sabidos.infrastructure.extensions.show
import com.sabidos.infrastructure.logging.Logger
import com.sabidos.presentation.quiz.SabidosQuizActivity
import com.sabidos.presentation.quiz.SabidosQuizActivity.Companion.CATEGORY_ID_BUNDLE_KEY
import kotlinx.android.synthetic.main.dialog_fragment_category_selection.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class CategorySelectionDialogFragment : DialogFragment() {

    private val viewModel: CategoryViewModel by viewModel()

    private val categoryAdapter = CategoryAdapter {
        finishButton.enable()
        viewModel.selectedCategory = it
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_fragment_category_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingView.setup(true)
        finishButton.disabled()
        setupCategoryRecyclerView()

        closeView.setOnClickListener { dismiss() }
        genericErrorView.onReloadListener { viewModel.loadCategories() }
        finishButton.onClickListener {
            viewModel.selectedCategory?.let {
                val bundle = Bundle()
                bundle.putInt(CATEGORY_ID_BUNDLE_KEY, it.id)
                activity?.goTo(SabidosQuizActivity::class.java, false, bundle = bundle)
            } ?: activity?.goTo(SabidosQuizActivity::class.java, false)
        }

        viewModel.loadCategories()
        viewModel.categoriesResource.observe(viewLifecycleOwner, Observer { bindState(it) })
    }

    override fun onResume() {
        super.onResume()

        runCatching {
            setupDialogDimensions()
        }.onFailure {
            Logger.withTag(CategorySelectionDialogFragment::class.java.simpleName).withCause(it)
            dismiss()
        }

    }

    private fun bindState(resource: Resource<List<Category>>?) {
        resource?.let {
            if (it.state != Loading) {
                loading(false)
            }
            when (it.state) {
                Success -> handleSuccess(it.data)
                Loading -> loading(true)
                else -> genericErrorView.show()
            }
        }
    }

    private fun setupDialogDimensions() {
        val display = this.resources.displayMetrics
        val width = display.widthPixels
        val height = display.heightPixels.minus(requireContext().dpToPx(32f))

        dialog?.window?.setLayout(width, height)
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
            GlobalScope.launch { loadingView.startAnimation() }
        } else {
            scrollView.show()
            loadingView.hide()
            loadingView.stopAnimation()
        }
    }

}