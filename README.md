# <p align="center"> KtKit </p>

<p align="center">
最全 Kotlin 工具箱（长期更新中）
</p>

<p align="center">
<a href="https://github.com/hi-dhl"><img src="https://img.shields.io/badge/GitHub-dhl-4BC51D.svg?style=flat"></a>  &nbsp; <img src="https://img.shields.io/badge/language-kotlin-orange.svg"/> &nbsp; <img src="https://img.shields.io/badge/platform-android-lightgrey.svg"/>
</p>

<p align="center"> 如果图片无法查看，请点击这里查看 <a href="http://img.hi-dhl.com/intent-act.png"> 图例</a>
<image src="http://img.hi-dhl.com/intent-act.png" width = 700px/>
</p>

## 关于 KtKit

KtKit 是用 Kotlin 语言编写的工具箱，包含了项目中常用的一系列工具，是 Jetpack ktx 系列的补充，同时也是 [anko](https://github.com/Kotlin/anko) 的延续，再次感谢以下项目提供的思路。

* Kotlin 官方 API
* [anko](https://github.com/Kotlin/anko)
* Google Jetpack ktx

## Download

**正式版本: 此版本包含稳定版本的 API**

```
// Project 级别的 `build.gradle`
allprojects {
    repositories {
        mavenCentral()
    }
}

// 模块级 `build.gradle`
dependencies {
    
}
```

**快照版本: 此版本包含最新的 API**

```
// Project 级别的 `build.gradle`
repositories {
    maven { url 'https://oss.sonatype.org/content/repositories/snapshots/' }

}

// 模块级 `build.gradle`
dependencies {
    
}
```

**如果这个仓库对你有帮助，请在仓库右上角帮我 star 一下，非常感谢你的支持，同时也欢迎你提交 PR**  ❤️❤️❤️


**执行 commit 或者 push 之前请先执行 `./gradlew spotlessApply`  会按照定义好的规则去格式化代码**

## 如何使用

**在 Activity 或者 Fragment 中获取传递过来的参数**

以下两种方式根据实际情况使用即可

```
// 方式一： 根据 key 获取参数
private val userPassword by intent<String>(KEY_USER_PASSWORD)

// 方式二：如果获取失败，返回一个默认值
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
            KEY_PEOPLE_PARCELIZE to PeopleModel("hi-dhl")
    )
}

// 方式二
context.startActivity<ProfileActivity>(
        KEY_USER_NAME to "ByteCode",
        KEY_USER_PASSWORD to "1024"
)
```

**Activity 之间跳转 及传递参数 和 回传参数**
以下两种方式根据实际情况使用即可

```
// 方式一
context.startActivityForResult<ProfileActivity>(KEY_REQUEST_CODE,
        KEY_USER_NAME to "ByteCode",
        KEY_USER_PASSWORD to "1024",
        KEY_PEOPLE_PARCELIZE to PeopleModel("hi-dhl")
)

// 方式二
context.startActivityForResult<ProfileActivity>(KEY_REQUEST_CODE) {
    arrayOf(
            KEY_USER_NAME to "ByteCode",
            KEY_USER_PASSWORD to "1024",
            KEY_PEOPLE_PARCELIZE to PeopleModel("hi-dhl")
    )
}

// 回传参数
setActivityResult(
        Activity.RESULT_OK,
        KEY_RESULT to "success",
        KEY_USER_NAME to "ByteCode"
)
finish()
```

**Fragment 跳转 及 传递参数**

```
// 方式一
fun newInstance1(): Fragment {
    return LoginFragment().makeBundle(
            ProfileActivity.KEY_USER_NAME to "ByteCode",
            ProfileActivity.KEY_USER_PASSWORD to "1024",
            ProfileActivity.KEY_PEOPLE_PARCELIZE to PeopleModel("hi-dhl")
    )
}

// 方式二
fun newInstance2(): Fragment {
    return LoginFragment().makeBundle {
        arrayOf(
                KEY_USER_NAME to "ByteCode",
                KEY_USER_PASSWORD to "1024",
                KEY_PEOPLE_PARCELIZE to PeopleModel("hi-dhl")
        )
    }
}
```

**设置状态的颜色**

```
setSatatusBarColor(android.R.color.darker_gray)
```

更多 API 使用方式点击这里前往查看

### 联系我

* 个人微信：hi-dhl
* 公众号：ByteCode，包含 Jetpack ，Kotlin ，Android 10 系列源码，译文，LeetCode / 剑指 Offer / 多线程 / 国内外大厂算法题 等等一系列文章

<img src='http://cdn.51git.cn/2020-10-20-151047.png' width = 350px/>

---

最后推荐我一直在更新维护的项目和网站：

* 计划建立一个最全、最新的 AndroidX Jetpack 相关组件的实战项目 以及 相关组件原理分析文章，正在逐渐增加 Jetpack 新成员，仓库持续更新，欢迎前去查看：[AndroidX-Jetpack-Practice](https://github.com/hi-dhl/AndroidX-Jetpack-Practice)

* LeetCode / 剑指 offer / 国内外大厂面试题 / 多线程 题解，语言 Java 和 kotlin，包含多种解法、解题思路、时间复杂度、空间复杂度分析<br/>

    <image src="http://cdn.51git.cn/2020-10-04-16017884626310.jpg" width = "500px"/>
  
    * 剑指 offer 及国内外大厂面试题解：[在线阅读](https://offer.hi-dhl.com)
    * LeetCode 系列题解：[在线阅读](https://leetcode.hi-dhl.com)

* 最新 Android 10 源码分析系列文章，了解系统源码，不仅有助于分析问题，在面试过程中，对我们也是非常有帮助的，仓库持续更新，欢迎前去查看 [Android10-Source-Analysis](https://github.com/hi-dhl/Android10-Source-Analysis)

* 整理和翻译一系列精选国外的技术文章，每篇文章都会有**译者思考**部分，对原文的更加深入的解读，仓库持续更新，欢迎前去查看 [Technical-Article-Translation](https://github.com/hi-dhl/Technical-Article-Translation)

* 「为互联网人而设计，国内国外名站导航」涵括新闻、体育、生活、娱乐、设计、产品、运营、前端开发、Android 开发等等网址，欢迎前去查看 [为互联网人而设计导航网站](https://site.51git.cn)

## License



