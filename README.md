#FBI WARMIMNG:api 仅供交流，不要外传！

# 关于本Demo
  本项目作为轻量化的练习Demo,方便以后新项目进行参考，主要练习基础项目封装，GreenDao3,Material-Animations,
  Retrofit2,mvp,Proguard混淆和JSbridge(webview 在单独的进程中),baseActivity(不要放那么多和Base 无关的东西)

# 关于Proguard
  Proguard 能混淆代码，能减少Apk 的体积，优化结构，不管怎样没有理由不Proguard吧，至于第三方的加固感觉没有必要，
  more:https://my.oschina.net/zengliubao/blog/841296

# 关于JSBridge 和 Webview 中的拍照问题
  可以给WebView.setWebChromeClient,然后在当js调用window对象的window.prompt 方法后进行拦截,通信协议为：
  jsbridge://className:callbackAddr（port）/methodName?jsonObj
  演示Demo中assets 目录内置了index.html 和JSbridge.js 来演示，this.goWebView("file:///android_asset/index.html");
  独立新的进程后，每次打开webview Activity就是新的进程，因为栈中只会有一个webview Activity，关闭页面的时
  候也关闭了进程，没有数据同步的问题。不过cookies 同步问题要注意同步刷新。

# 关于动态权限分配PermissionsDispatcher
  Runtime permissions are great for users, but can be tedious to implement correctly for developers,
  requiring a lot of boilerplate code.
  PermissionsDispatcher provides a simple annotation-based API to handle runtime permissions。
  还是那句话：既然是动态权限就不要第一次打开app就拼命的申请权限，在用的时候申请吧。

# 关于Http网络请求
  应该没有比retrofit2 更好的了吧？不过api 不是restful 就需要再封装一下了，网路模块就是数据命脉，做好了
  整个app 的结构会简化很多。

# 关于MVC，MVP (什么？MVVM)
  MVC 中Activity 可能代码会随着业务的复杂变得很大，里面承载了太多的东西，试试MVP吧。当然如果是小项目的话
  使用MVP 估计并不会提高很多生产力，还会产生大量的接口类，MVVM 不说了

# 关于持久化数据的保存
- SharePrence (考虑有几个进程可能要content p)
- Datebase
   sqlite ? no!  ORMDB please,Now maybe the best is greendao3.

# 关于动画
  要适当的有过度动画，不要太生硬

# 关于图片
  这个需要看项目的业务了，也就那几个老牌的库

# 关于Crash 的采集
  Bugly

# 关于项目中的素材
  多用webp,.9.png,还有很多新的工程构建

# 关于5大布局 的发展
  现在不只是FrameLayout，LinearLayouy  等等以前的，还有很多新的如percentLayout,flexlayout coordilayout
  等等都是Google 官方新出来的推荐使用的布局样式，多使用新的

# toolbar
  设计师会遵循Android 的规范设计了，吧！会的

# 运营统计
  再说吧。

#关于调试工具
 推荐Facebook stetho ，可以网络请求（抓包），不root查看DB 文件和sharepreference，甚至在4.4 以后webview
 可以远程调试；提高生产力的利器啊。

# 安全加密
  混淆Proguard，https,数据库加密，密码明文传输是什么鬼，多多oauth


# 应用中使用的lib
  其实大部分的App 最难以做好的模块是Http，http 模块封装的好，做起业务来很方便。
  UX 部分多考虑Material design和动画Material-Animations
  ## 需选
  - 数据库 GreenDao3(如果有需要使用数据库，建议使用，配置很简洁了)
  - 过度动画 Material-Animations
  - Http请求 retrofit2
  - 列表数据上拉下拉 官方/springview
  - 图片展示 UIL不再维护了，可选用glide,pissico等
  - 权限管理 PermissionsDispatcher
  - 二维码 com.journeyapps:zxing-android-embedded

  ## 可选
  - butterknife/databing
  - rxjava,rxAndroid,Eventbus ,square 公司的套件包
  - Bugly
  - leakcanary
  - Facebook stetho
  - safeJsbridge
  - kotlin

# 项目中包含的基本的通用模块
- 混淆压缩打包优化 Proguard　proguard-android-optimize　和 proguard-android 区别 ？
  https://github.com/D-clock/Doc/blob/master/Android/Gradle/4_AndroidStudio%E4%B8%8BProGuard%E6%B7%B7%E6%B7%86%E6%89%93%E5%8C%85.md
- Toolbar 的处理
- Fragment 的懒加载
- 通用的BaseActivity 和BaseFragment的封装（跳转PV打点，事件打点，不放和base 无关的东西）
- 通用的Lib module 的封装
- Http 的闭环处理
- Proguard 混淆 打包优化
- BaseWebView 的处理（未完善，Android 的坑很多）
- support lib 和动画





