package net.wangyl.test.ui.gallery

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import net.wangyl.test.databinding.FragmentGalleryBinding
import java.util.*
import java.util.concurrent.Executors

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val queue: LinkedList<String> = LinkedList()
    var count = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                println("handleMessage name=${msg.obj}")
                queue.add(msg.obj.toString())
            }
        }
        val executors = Executors.newFixedThreadPool(10)
        executors.execute {
            for (i in 1..10) {
                Thread.sleep(1000)
                handler.sendMessage(Message.obtain().apply {
                    obj = "用户${count++} 来登记"
                })
            }
        }

        Thread {
            while (true) {
                register()
            }
        }.start()
        val galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textGallery
        galleryViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        executors.shutdown()
        return root
    }

    fun register() {
        if (!queue.isEmpty()) {
            println("register: ${queue.removeFirst()}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}