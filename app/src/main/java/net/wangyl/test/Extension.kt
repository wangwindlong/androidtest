package net.wangyl.test

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavOptions

//</editor-fold>
private val density = Resources.getSystem().displayMetrics.density
//
////封装koin 避免样本代码
//inline fun <reified T: Any> getK(qualifier: Qualifier? = null,
//                                 noinline parameters: ParametersDefinition? = null): T {
////    GlobalContext.get().get<T>(qualifier, parameters)
//    return KoinJavaComponent.get(T::class.java, qualifier)
//}
//
//inline fun <reified T : Any> inject(
//    qualifier: Qualifier? = null,
//    mode: LazyThreadSafetyMode = KoinPlatformTools.defaultLazyMode(),
//    noinline parameters: ParametersDefinition? = null
//): Lazy<T> {
//    return GlobalContext.get().inject(qualifier, mode, parameters)
//}
//
//fun NavOptions.Builder.setHorizontalSlide(): NavOptions.Builder {
//    return setEnterAnim(R.anim.base_h_slide_enter)
//        .setExitAnim(R.anim.base_h_slide_exit)
//        .setPopEnterAnim(R.anim.base_h_slide_popenter)
//        .setPopExitAnim(R.anim.base_h_slide_popexit)
//}
//
//val NON_INTENT: Intent.() -> Unit = {}
//fun<T> goIntent(c: Context, frag: Class<T>, init: (Intent.() -> Unit) = NON_INTENT): Intent {
//    return Intent(c, SimpleActivity::class.java).also(init).putExtra(TAG_FRAGNAME, frag.name)
//}
//
//
//fun Context.createFragment(fm: FragmentManager, data: FragmentData) : Fragment {
//    return fm.fragmentFactory.instantiate(classLoader, data.name).apply {
//        arguments = data.extra?.extras
//    }
//}

fun Context.isDebuggable(): Boolean = (0 != (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE))

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun Int.px2dp(): Int {
    return (this / density).toInt()
}

//使用值对应的dp
fun Int.toPx(): Float {
    return 0.5f + this * density
}

fun Float.toPx(): Float {
    return 0.5f + this * density
}

fun Int.dp2px(): Int {
    return this.toPx().toInt()
}


val Int.dp
    get() = this.toPx()
//    get() = TypedValue.applyDimension(
//        TypedValue.COMPLEX_UNIT_DIP,
//        this.toFloat(),
//        Resources.getSystem().displayMetrics
//    )

val Float.dp
//    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)
    get() = this.toPx().toInt()



fun checkContext(context: Context) {
    if (context is Application)
        throw IllegalStateException("please call this in activity or fragment or view")
}

//fun<T: Fragment>  Context.goSimpleActivity(frag: Class<T>, extra: Intent? = null) {
//    checkContext(this)
//    startActivity(goIntent(this, frag) {
//        extra?.let { this.putExtras(it) }
//    })
//}
//
//fun<T: Fragment> Fragment.goSimpleActivity(frag: Class<T>, extra: Intent? = null) {
//    startActivity(goIntent(requireContext(), frag) {
//        extra?.let { this.putExtras(it) }
//    })
//}

fun Fragment.toast(@StringRes message: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context?.applicationContext, message, duration).show()
}

fun Fragment.toast(message: String?, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context?.applicationContext, message, duration).show()
}

fun Activity.toast(@StringRes message: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(applicationContext, message, duration).show()
}

fun Activity.toast(message: String?, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(applicationContext, message, duration).show()
}

fun Context.toast(message: String?, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(applicationContext, message, duration).show()
}

inline fun <reified T> Fragment.startActivity() {
    startActivity(Intent(context, T::class.java))
}

inline fun <reified T> Context.startActivity() {
    startActivity(Intent(this, T::class.java))
}



