package com.pichs.common.utils.cache

import android.app.Application
import android.content.Context
import android.os.Parcelable
import com.tencent.mmkv.MMKV
import org.jetbrains.annotations.NotNull
import java.io.File

/**
 * MMKV
 */
class CacheHelper private constructor() {

    companion object {
        private lateinit var application: Application

        /**
         * 初始化MMKV，在Application的 onCreate 中初始化
         */
        fun init(application: Application) {
            CacheHelper.application = application
            MMKV.initialize("${application.filesDir.absolutePath}${File.separator}disk-cache")
        }

        /**
         * 获取应用的上下文
         * @return Application
         */
        fun getApplication(): Application {
            return application
        }

        // 单利模式
        @JvmStatic
        private val instance: CacheHelper by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            CacheHelper()
        }

        /**
         * 单利模式
         */
        @JvmStatic
        fun get(): CacheHelper {
            return instance
        }
    }

    private var kv: MMKV? = null

    /**
     * 创建MMKV对象
     */
    init {
        kv = MMKV.defaultMMKV()
    }


    fun setString(@NotNull key: String, value: String?) {
        kv?.encode(key, value)
    }

    @JvmOverloads
    fun getString(@NotNull key: String, defValue: String? = ""): String? {
        return kv?.decodeString(key, defValue) ?: defValue
    }

    fun setInt(@NotNull key: String, value: Int) {
        kv?.encode(key, value)
    }

    @JvmOverloads
    fun getInt(@NotNull key: String, defValue: Int = 0): Int {
        return kv?.decodeInt(key, defValue) ?: defValue
    }

    fun setLong(@NotNull key: String, value: Long) {
        kv?.encode(key, value)
    }

    @JvmOverloads
    fun getLong(@NotNull key: String, defValue: Long = 0): Long {
        return kv?.decodeLong(key, defValue) ?: defValue
    }

    fun setBool(@NotNull key: String, value: Boolean) {
        kv?.encode(key, value)
    }

    @JvmOverloads
    fun getBool(@NotNull key: String, defValue: Boolean = false): Boolean {
        return kv?.decodeBool(key, defValue) ?: defValue
    }


    fun setFloat(@NotNull key: String, value: Float) {
        kv?.encode(key, value)
    }

    @JvmOverloads
    fun getFloat(@NotNull key: String, defValue: Float = 0.0f): Float {
        return kv?.decodeFloat(key, defValue) ?: defValue
    }


    fun setDouble(@NotNull key: String, value: Double) {
        kv?.encode(key, value)
    }

    @JvmOverloads
    fun getDouble(@NotNull key: String, defValue: Double = 0.0): Double {
        return kv?.decodeDouble(key, defValue) ?: defValue
    }

    fun setParcelable(@NotNull key: String, value: Parcelable) {
        kv?.encode(key, value)
    }

    @JvmOverloads
    fun getParcelable(
            @NotNull key: String,
            clz: Class<Parcelable>,
            defValue: Parcelable? = null
    ): Parcelable? {
        return kv?.decodeParcelable(key, clz, defValue) ?: defValue
    }


    fun setBytes(@NotNull key: String, value: ByteArray) {
        kv?.encode(key, value)
    }

    @JvmOverloads
    fun getBytes(@NotNull key: String, defValue: ByteArray? = null): ByteArray? {
        return kv?.decodeBytes(key, defValue) ?: defValue
    }

    fun setStringSet(@NotNull key: String, value: Set<String>?) {
        kv?.encode(key, value)
    }

    @JvmOverloads
    fun getStringSet(@NotNull key: String, defValue: Set<String>? = null): Set<String>? {
        return kv?.decodeStringSet(key, defValue) ?: defValue
    }

}