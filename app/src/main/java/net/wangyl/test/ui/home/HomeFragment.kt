package net.wangyl.test.ui.home

import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.graphics.PointF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.annotation.Keep
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import net.wangyl.test.databinding.FragmentHomeBinding
import net.wangyl.test.dp

@Keep
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel by viewModels<HomeViewModel>()

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        //属性动画1 ViewPropertyAnimator
//        binding.customView.animate().translationX(100.dp).alpha(0.5f).rotation(90f).setStartDelay(1000)
        //属性动画
//        val animator = ObjectAnimator.ofFloat(binding.customView, "radius", 150.dp)
//        animator.startDelay = 1000
//        animator.start()

        val animator = ObjectAnimator.ofObject(
            binding.customView,
            "point",
            PointEvaluator(),
            PointF(200.dp, 300.dp)
        )
        animator.startDelay = 1000
        animator.duration = 2000
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.start()
        return root
    }

    class PointEvaluator : TypeEvaluator<PointF> {
        override fun evaluate(fraction: Float, startValue: PointF, endValue: PointF): PointF {
            val x = startValue.x + fraction * (endValue.x - startValue.x)
            val y = startValue.y + fraction * (endValue.y - startValue.y)
            return PointF(x, y)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}