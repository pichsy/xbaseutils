package com.pichs.common.utils.expands

import android.app.Activity
import android.view.View
import android.widget.Toast

public fun Activity.toast(string: String) {
    Toast.makeText(this.applicationContext, string, Toast.LENGTH_SHORT).show()
}

public fun Activity.toast(resId: Int) {
    Toast.makeText(this.applicationContext, resId, Toast.LENGTH_SHORT).show()
}

public fun androidx.fragment.app.Fragment.toast(string: String) {
    Toast.makeText(requireContext().applicationContext, string, Toast.LENGTH_SHORT).show()
}

public fun androidx.fragment.app.Fragment.toast(resId: Int) {
    Toast.makeText(requireContext().applicationContext, resId, Toast.LENGTH_SHORT).show()
}

public fun View.toast(string: String) {
    Toast.makeText(this.context.applicationContext, string, Toast.LENGTH_SHORT).show()
}

public fun View.toast(resId: Int) {
    Toast.makeText(this.context.applicationContext, resId, Toast.LENGTH_SHORT).show()
}


// 再也不用if了。 b.isTrue{}.isFalse{}
public inline fun <R> Boolean.isTrue(block: () -> R): Boolean {
    if (this) block()
    return this
}

public inline fun <R> (() -> Boolean).isTrue(block: () -> R): Boolean {
    if (this()) block()
    return this()
}

public inline fun <R> Boolean.isFalse(block: () -> R): Boolean {
    if (!this) block()
    return this
}

public inline fun <R> (() -> Boolean).isFalse(block: () -> R): Boolean {
    if (!this()) block()
    return this()
}

public fun <T> T.isNULL(block: () -> Unit): T? {
    if (this == null) {
        block()
    }
    return this
}

public fun <T> T.isNotNULL(block: () -> Unit): Unit {
    if (this != null) {
        block()
    }
}

// 自定义三元运算符
public fun <R> Boolean.judge(trueblock: () -> R, falseblock: () -> R): R {
    return if (this) trueblock() else falseblock()
}

public fun <R> Boolean.judge(trueObj: R, falseObj: R): R {
    return if (this) trueObj else falseObj
}

public fun <R> (() -> Boolean).judge(trueblock: () -> R, falseblock: () -> R): R {
    return if (this()) trueblock() else falseblock()
}