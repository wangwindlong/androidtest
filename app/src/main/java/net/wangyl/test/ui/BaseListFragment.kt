package net.wangyl.test.ui

import androidx.fragment.app.Fragment
import net.wangyl.test.ui.adapter.BaseModel
import net.wangyl.test.ui.adapter.IBindItem
import net.wangyl.test.ui.adapter.MyBaseViewHolder

abstract class BaseListFragment<Data : BaseModel> : Fragment(), IBindItem<Data, MyBaseViewHolder> {

}