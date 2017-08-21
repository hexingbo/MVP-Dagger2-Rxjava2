# FBI WARMIMNG:api 仅供交流，不要外传！


# 关于本Demo
  本项目作为轻量化的练习Demo,方便以后新项目进行参考，主要练习基础项目封装，GreenDao3,Material-Animations,
  Retrofit2,mvp,Proguard混淆和JSbridge(webview 在单独的进程中),baseActivity(不要放那么多和Base 无关的东西)
  慢慢的也会加入Rxjava2 和 其他的，反对过度封装。大部分App基本rxjava2(慎用) + retrofit2 +MVP 就够了,反对过度
  的封装。
  一直在纠结在何种开发模式之中，重构希望能把关注点集中到代码结构、整体架构、可测试性、可维护性这四个方面(https://github.com/googlesamples/android-architecture).


# 关于Http网络请求 (Rxjava2+Retrofit2)
  应该没有比Retrofit2 更好的了吧？不过api 不是restful 就需要再封装一下了，网路模块就是数据命脉，做好了
  整个app 的结构会简化很多，结合Rxjava2不是更快哉;配合RxLifeCycle 控制生命周期;
  BaseObserver 中getErrorMsg(HttpException httpException) 方法中的处理和我们的Api  结构有关，请知悉。可以在Activity，fragment，service，broadcast 等发起http请求。


# 关于JSBridge 和 Webview 中的拍照问题
  演示Demo中assets 目录内置了index.html 和JSbridge.js 来演示拍照问题，拍照和上传图片都是用了JsBridge
  因为部分4.x 的Webview 手机无法处理 html中的type=file 标记
  可以给WebView.setWebChromeClient,然后在当js调用window对象的window.prompt 方法后进行拦截,通信协议为：
  ```
  jsbridge://className:callbackAddr（port）/methodName?jsonObj
  this.goWebView("file:///android_asset/index.html");
  ```
  Android 实现JSBridge的库有好几个，无复杂需要用轻量级的吧。
  独立新的进程后，每次打开webview Activity就是新的进程，因为栈中只会有一个webview Activity，关闭页面的时
  候也关闭了进程，没有数据同步的问题；不过cookies 同步问题要注意同步刷新。如android8一样，应用的 WebView 
  对象将在多进程模式下运行。网页内容在独立的进程中处理，此进程与包含应用的进程相隔离，以提高安全性

  ## 低端机调用系统的相机拍照的问题
  比如红米手机，调用系统相机拍照时需要耗费大量内存，我们的app就成了后台app,很容易被系统干掉。
  单独一个进程不能解决所有的问题，问题根源在于我们的app不能成为后台进程，所有自定义拍照功能吧。

# 关于动态权限分配PermissionsDispatcher
  Runtime permissions are great for users, but can be tedious to implement correctly for developers,
  requiring a lot of boilerplate code.
  PermissionsDispatcher provides a simple annotation-based API to handle runtime permissions。
  还是那句话：既然是动态权限就不要第一次打开app就拼命的申请权限，在用的时候申请吧。

# UI架构模型
  Android应用的UI架构模型经历了MVC,MVP 和 MVVM 的演变过程。MVC中View 层（Activity，Fragment/自定义的View）
  可能代码会随着业务的复杂变得很大，里面不但要处理界面，还要处理很多业务逻辑里面承载了太多的东西，试试MVP吧，
  已经是很流行的UI架构模型了。
  使用MVP多关注代码结构、整体架构、可测试性、可维护性这四个方面

## 关于MVP
  多参考Google官网的 + 这个：https://github.com/anylifezlb/android-guidelines

  - View层
  包含界面相关的功能（Activity,Fragment,View,Adapter）,专注用户的交互，实现设计师给出的界面，动画.View层
  一般会持有Presenter 层的引用，或者也可以通过依赖注入(dragger/2)的方式获取Presenter 实例，非UI逻辑的操作
  委托给Presenter.

  - Presenter 逻辑控制层
  充当中间人的角色，隔离model层和View层，该层从View 层剥离控制逻辑部分形成的，主要负责View层和Model 层的
  交互。例如接收view 层的网络数据请求，并分发给对应的Model层处理，同时监听Model层的处理结果，最终反馈给
  View 层，从而实现界面的刷新。

  - Model 层
  封装数据来源，一个程序的本质是处理各种数据，input data ,proces data,output data.例如Android 的网络数据
  ，本地数据库数据，对Presenter 层提供简单易用的接口。

# 关于Proguard
  Proguard 能混淆代码，能减少Apk 的体积，优化结构，不管怎样没有理由不Proguard吧，至于第三方的加固感觉没有必要，
  more:https://my.oschina.net/zengliubao/blog/841296

# 关于持久化数据的保存
- SharePrence (考虑有几个进程可能要content p)
- Datebase
   sqlite ? no!  ORMDB please,Now maybe the best is greendao3.Rxjava2+GreenDao3 may be wonderful!

# 关于过渡动画
  要适当的有过度动画，不要太生硬,material transtion

# 关于调试工具
 推荐Facebook stetho ，可以网络请求（抓包），不root查看DB 文件和sharepreference，甚至在4.4 以后webview
 可以远程调试；提高生产力的利器啊。

# 推送
  会越来越严格，目前没有很好的第三方，绿色联盟？如果企业对推送有强需求，还是建议用短信和微信消息做补充

# 关于热修复
  阿里的HotFix&微信的Tinker
  android的热修复原理大体上分为两种，其一是通过dex的执行顺序实现Apk热修复的功能(Tinker)，但是其需要将App重启才能生 效;其二是通过Native修改函数指针的方式实现热修复(HotFix)。

  显然对于修复紧急BUG这个场景，阿里百川HotFix的更为合适，它更加轻量，可以在不重启的情况下生效，且对性能几乎没有影
  微信Tinker、QQ空间超级补丁技术更多地把场景定位在发布小的新功能上，采用ClassLoader的模式，牺牲较高的性能代价去实现 类、资源新增或替换的功能。
  阿里百川HotFix对应用本身做到无侵入，无性能损耗。

 [2017年6月阿里手淘推出了首个非侵入式移动热更新解决方案——Sophix。
 在Android热修复的三大领域：代码修复、资源修复、SO修复方面，以及方案的安全性和易用性方面，Sophix都做到了业界领先，可是要收费](https://mp.weixin.qq.com/s?__biz=MzAxMTI4MTkwNQ==&mid=2650823404&idx=1&sn=c56458a97561f54b893b33a80635d399&chksm=80b78e72b7c00764b26972bd21cd3e4fe5bb075a8d80890340b2a7a0a565779add0757b161e8&mpshare=1&scene=1&srcid=0704C7XraNsOGvDsgN9bCNii&pass_ticket=AZhM9mvZM8BzU28oFsdChz0QSuCBcgFEhhet1%2FD2hXnrM%2FSkdWA5TsZ06l%2F%2Fhbwm#rd)

# Rxjava2
  目前只是在http 请求的时候用了Rxjava2+retrofit2,没有侵入UI，打好基础才是王道
  读取数据库等IO操作还在主线程，虽然影响不大，还是会卡一下的，坐等GreenDao 支持Rxjava2
  这样UI就会更加的流畅起来啊

  https://realm.io/cn/news/kau-felipe-lima-adopting-rxjava-airbnb-android/

# 项目中包含的基本的通用模块
- Toolbar 的处理
- Fragment 的懒加载
- 通用的BaseActivity 和BaseFragment的封装（跳转PV打点，事件打点，不放和base 无关的东西）
- 通用的Lib module 的封装
- Http (Rxjava2+Retrofit2)的闭环处理
- Proguard 混淆 打包优化
- BaseWebView 的 处理（未完善，Android 的坑很多）
- support lib 和 动画
- [混淆压缩打包优化 Proguard　proguard-android-optimize　和 proguard-android 区别 ？](https://github.com/D-clock/Doc/blob/master/Android/Gradle/4_AndroidStudio%E4%B8%8BProGuard%E6%B7%B7%E6%B7%86%E6%89%93%E5%8C%85.md)
