package net.wangyl.test.ui.adapter

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import net.wangyl.test.R
import java.io.Serializable
import java.lang.NullPointerException


val defaultItem = R.layout.item_text_view

class BaseMultiAdapter<Data : BaseModel>(
    layouts: List<Int>,
    val itemBinder: IBindItem<Data, MyBaseViewHolder>,
    data: MutableList<Data>? = null
) : BaseDelegateMultiAdapter<Data, MyBaseViewHolder>(data), LoadMoreModule {

    init {
        if (layouts.isEmpty()) throw NullPointerException("fun getItemLayouts() : List<Int> must return more than one items")
        setMultiTypeDelegate(MultiTypeDelegate(layouts))
        setDiffCallback(object : DiffUtil.ItemCallback<Data>() {
            override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
                return oldItem.getItemId() == newItem.getItemId()
            }

            override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
                return (oldItem.getItemContent() == newItem.getItemContent()
                        && oldItem.equals(newItem))
            }
        })
    }

    override fun convert(holder: MyBaseViewHolder, item: Data) {
        itemBinder.bindItem(holder, item)
    }

    override fun convert(holder: MyBaseViewHolder, item: Data, payloads: List<Any>) {
        itemBinder.bindItem(holder, item, payloads)
    }

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): MyBaseViewHolder {
        val delgate = getMultiTypeDelegate()
        val itemId = delgate?.getLayoutId(viewType)
            ?: throw NullPointerException("未找到 viewType=$viewType 的布局")
        val itemBinding: ViewDataBinding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), itemId, parent, false)
        return MyBaseViewHolder(itemBinding.root).apply { binding = itemBinding }
    }
}

interface BaseModel : Parcelable, Serializable {
    //    val success: Boolean
    fun getItemId(): String = ""
    fun getItemContent(): String = ""
}

//recyclerview 多布局时需要使用到
interface BaseItem : BaseModel {
    fun getItemType(): Int = defaultItem
}

class MyBaseViewHolder(view: View) : BaseViewHolder(view) {
    var binding: ViewBinding? = null
}

class MultiTypeDelegate<Data>(layouts: List<Int>) : BaseMultiTypeDelegate<Data>() {
    init {
        layouts.map { addItemType(it, it) }
    }

    override fun getItemType(data: List<Data>, position: Int): Int {
        val item = data[position % data.size]
        //根据position 获取到item的布局layoutid
        return if (item is BaseItem) item.getItemType() else defaultItem
    }
}

interface IBindItem<Data : BaseModel, VH : BaseViewHolder> {
    fun bindItem(holder: VH, item: Data, payloads: List<Any>? = null)
}
