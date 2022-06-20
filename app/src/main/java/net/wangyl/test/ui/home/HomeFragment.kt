package net.wangyl.test.ui.home

import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.graphics.PointF
import android.os.*
import android.os.Build.VERSION_CODES.S
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Keep
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.parcelize.Parcelize
import net.wangyl.test.R
import net.wangyl.test.databinding.FragmentHomeBinding
import net.wangyl.test.databinding.ItemTextViewBinding
import net.wangyl.test.dp
import net.wangyl.test.perm.PermissionManager
import net.wangyl.test.perm.Perms.BLUETOOTH_PERMS
import net.wangyl.test.toast
import net.wangyl.test.ui.BaseListFragment
import net.wangyl.test.ui.MaxHeightRecyclerView
import net.wangyl.test.ui.adapter.BaseModel
import net.wangyl.test.ui.adapter.BaseMultiAdapter
import net.wangyl.test.ui.adapter.MyBaseViewHolder
import net.wangyl.test.ui.adapter.defaultItem


@Parcelize
data class BlDevice(val device: BluetoothDevice): BaseModel {
    override fun getItemId(): String {
        return device.address
    }

    @SuppressLint("MissingPermission")
    override fun getItemContent(): String {
        return device.name + "-" + device.type
    }
}

@Keep
class HomeFragment : BaseListFragment<BlDevice>() {
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var blManager: BluetoothManager? = null
    private var mBluetoothAdapter: BluetoothAdapter? = null
    val mBondedDevices: MutableList<BlDevice> = arrayListOf()

    private lateinit var permissionManager: PermissionManager

    // https://juejin.cn/post/6987575150283587592
    private lateinit var mActivityLauncher : ActivityResultLauncher<Intent>

    private val mThreadHandler = HandlerThread("bluetooth")

    @SuppressLint("MissingPermission", "NotifyDataSetChanged")
    private val mHandler = Handler(Looper.getMainLooper()) {
        mBondedDevices.clear()
        // 注意，如果没有开启蓝牙，则获取不到绑定过的蓝牙设备
        when {
            mBluetoothAdapter == null ->  toast("该设备不支持蓝牙")
            mBluetoothAdapter?.isEnabled != true -> { //蓝牙没开启，是否提示开启？
                // 开启蓝牙
                mActivityLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
            }
            else -> { // 后台线程获取设备信息？
                mBluetoothAdapter?.bondedDevices?.map {
                    mBondedDevices.add(BlDevice(it))
                }
            }
        }
        println("mHandler on callback mBondedDevices= $mBondedDevices")
        binding.bleBondedRv.adapter?.notifyDataSetChanged()
        true
    }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            // 开启蓝牙返回后的操作
//            toast("从上一个界面返回，返回结果：${it.resultCode}")
//            if (it.resultCode == Activity.RESULT_OK) {
//
//            }
            loadBLEDevices()
        }
        blManager = context?.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
//        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter() // 方法已废弃
        mBluetoothAdapter = blManager?.adapter
        permissionManager = PermissionManager.from(this)

        loadBLEDevices()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel by viewModels<HomeViewModel>()

        requirePermission(0)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val adapter = BaseMultiAdapter(getItemLayouts(), this, mBondedDevices)
        initRecyclerView(binding.bleBondedRv)
        binding.bleBondedRv.adapter = adapter

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
            PointF(200.dp.toFloat(), 300.dp.toFloat())
        )
        animator.startDelay = 1000
        animator.duration = 2000
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.start()
        return root
    }

    fun getItemLayouts(): List<Int> {
        return ArrayList<Int>().apply { add(defaultItem) }
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

    override fun bindItem(holder: MyBaseViewHolder, item: BlDevice, payloads: List<Any>?) {
        val binding = holder.binding ?: return
        when (binding) {
            is ItemTextViewBinding -> {
                binding.tv.text = item.getItemContent()
                binding.tv1.text = item.getItemId()
            }
        }
        mThreadHandler.quit()
    }

    private fun loadBLEDevices() {
        mHandler.sendMessage(Message.obtain())
    }

    @TargetApi(S)
    private fun requirePermission(type: Int) {
        permissionManager.request(BLUETOOTH_PERMS)
            .rationale(getString(R.string.dialog_permission_default_message))
            .checkPermission { success ->
//                if (!success) {
//                    ()
//                } else {
//                    if (type == 0) {
//                        goCamera()
//                    } else {
//                        goSelect()
//                    }
//                }
                null
            }
    }

    fun initRecyclerView(recyclerView: RecyclerView) {
//        val layoutManager = LinearLayoutManager(context)
//        recyclerView.layoutManager = layoutManager
        val isMaxHeight = recyclerView is MaxHeightRecyclerView
        recyclerView.isNestedScrollingEnabled = isMaxHeight
        recyclerView.setHasFixedSize(isMaxHeight)
        recyclerView.itemAnimator = DefaultItemAnimator()
    }
}