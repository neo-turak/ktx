# <p align="center"> KtKit </p>

<p align="center">
KtKit Anko库的延伸，数据库，Intent处理，dp转换工具，自定义ShapeableView等<br/>
</p>

<p align="center"><image src="http://img.hi-dhl.com/intent-act.png" width = 700px/></p>

## 关于 KtKit

KtKit 是用 Kotlin 语言编写的工具库，包含了项目中常用的一系列工具，是 Jetpack ktx 系列的补充，涉及到了很多从 Kotlin 源码、Jetpack ktx、anko 等等知名的开源项目中学习的技巧，包含了 Kotlin 委托属性、高阶函数、扩展函数、内联、注解的使用等等，再次感谢以下项目提供的思路。

* Kotlin 官方 API
* [anko](https://github.com/Kotlin/anko)
* [FlowBinding](https://github.com/ReactiveCircus/FlowBinding)
* Google Jetpack ktx

但是目前还不是很完善，正在陆续将一些常用的功能，结合着 Kotlin 的高级函数的特性，不仅让代码可读性更强，使用更加简单，而且还可以帮助我们解决项目中常见的问题。

## Download

**正式版本: 此版本包含稳定版本的 API** 

[![](https://jitpack.io/v/Neo-Turak/KtKit.svg)](https://jitpack.io/#Neo-Turak/KtKit)

```
// Project 级别的 `build.gradle`
allprojects {
    repositories {
       maven { url 'https://jitpack.io' }
    }
}

// 模块级 `build.gradle`
dependencies {
    implementation 'com.github.Neo-Turak:KtKit:Tag'
}
```

**如果这个仓库对你有帮助，请在仓库右上角帮我 star 一下，非常感谢你的支持，同时也欢迎你提交 PR**  ❤️❤️❤️


## 如何使用

**在 Activity 或者 Fragment 中获取传递过来的参数**

以下两种方式根据实际情况使用即可

```
// 方式一： 不带默认值
private val userPassword by intent<String>(KEY_USER_PASSWORD)

// 方式二：带默认值：如果获取失败，返回一个默认值
private val userName by intent<String>(KEY_USER_NAME) {
    "公众号：ByteCode"
}
```

**Activity 之间跳转 及传递参数**

以下两种方式根据实际情况使用即可

```
// 方式一
context.startActivity<ProfileActivity> {
    arrayOf(
            KEY_USER_NAME to "ByteCode",
            KEY_USER_PASSWORD to "1024",
            KEY_PEOPLE_PARCELIZE to "hi"
    )
}

// 方式二
context.startActivity<ProfileActivity>(
        KEY_USER_NAME to "ByteCode",
        KEY_USER_PASSWORD to "1024"
)
```

**Activity 之间跳转 及传递参数 和 回传结果**
以下两种方式根据实际情况使用即可

```
// 方式一
context.startActivityForResult<ProfileActivity>(KEY_REQUEST_CODE,
        KEY_USER_NAME to "ByteCode",
        KEY_USER_PASSWORD to "1024",
        KEY_PEOPLE_PARCELIZE to PeopleModel("hi")
)

// 方式二
context.startActivityForResult<ProfileActivity>(KEY_REQUEST_CODE) {
    arrayOf(
            KEY_USER_NAME to "ByteCode",
            KEY_USER_PASSWORD to "1024",
            KEY_PEOPLE_PARCELIZE to PeopleModel("hi")
    )
}
```

**回传结果**

```
// 方式一
setActivityResult(Activity.RESULT_OK) {
   arrayOf(
            KEY_RESULT to "success"
    )
}
                    
// 方式二
setActivityResult(
        Activity.RESULT_OK,
        KEY_RESULT to "success",
        KEY_USER_NAME to "ByteCode"
)
```

**Fragment 跳转 及 传递参数**

```
// 方式一
fun newInstance1(): Fragment {
    return LoginFragment().makeBundle(
            ProfileActivity.KEY_USER_NAME to "ByteCode",
            ProfileActivity.KEY_USER_PASSWORD to "1024",
            ProfileActivity.KEY_PEOPLE_PARCELIZE to PeopleModel("hi")
    )
}

// 方式二
fun newInstance2(): Fragment {
    return LoginFragment().makeBundle {
        arrayOf(
                KEY_USER_NAME to "ByteCode",
                KEY_USER_PASSWORD to "1024",
                KEY_PEOPLE_PARCELIZE to PeopleModel("hi")
        )
    }
}
```

**一行代码实现点击事件，避免内存泄露**

KtKit 提供了常用的三个 API：单击事件、延迟第一次点击事件、防止多次点击

**单击事件**

```
view.click(lifecycleScope) { showShortToast("公众号：ByteCode" }
```


**延迟第一次点击事件**

```
// 默认延迟时间是 500ms
view.clickDelayed(lifecycleScope){ showShortToast("公众号：ByteCode" }

// or
view.clickDelayed(lifecycleScope, 1000){ showShortToast("公众号：ByteCode") }
```


**防止多次点击**

```
// 默认间隔时间是 500ms
view.clickTrigger(lifecycleScope){ showShortToast("公众号：ByteCode") }

// or
view.clickTrigger(lifecycleScope, 1000){ showShortToast("公众号：ByteCode") }
```



