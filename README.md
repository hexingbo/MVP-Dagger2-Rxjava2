#FBI WARMIMNG:api 仅供交流，不要外传！

# 关于本Demo
  本项目作为轻量化的练习Demo,方便以后新项目进行参考，主要练习基础项目封装，GreenDao3,Material-Animations,
  Retrofit2,mvp,Proguard混淆和JSbridge(webview 在单独的进程中),baseActivity(不要放那么多和Base 无关的东西)
  慢慢的也会加入Rxjava2 和 其他的，反对过度封装。大部分App基本rxjava2(慎用) + retrofit2 +MVP 就够了,反对过度
  的封装，反对拿J2EE 的一些思想放Android 上，当然打好基础任何时候都必要的。

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

# 关于Http网络请求 (Rxjava2+Retrofit2)
  应该没有比retrofit2 更好的了吧？不过api 不是restful 就需要再封装一下了，网路模块就是数据命脉，做好了
  整个app 的结构会简化很多，结合Rxjava（/2）不是更快哉。现在项目http 请求是自由切换是否用Rxjava

# UI架构模型
  Android应用的UI架构模型经历了MVC,MVP 和 MVVM 的演变过程。MVC中View 层（Activity，Fragment/自定义的View）
  可能代码会随着业务的复杂变得很大，里面不但要处理界面，还要处理很多业务逻辑里面承载了太多的东西，试试MVP吧，
  已经是很流行的UI架构模型了。
  使用MVP多关注代码结构、整体架构、可测试性、可维护性这四个方面

## 关于MVP
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


# 关于持久化数据的保存
- SharePrence (考虑有几个进程可能要content p)
- Datebase
   sqlite ? no!  ORMDB please,Now maybe the best is greendao3.Rxjava2+GreenDao3 may be wonderful!

# 关于过渡动画
  要适当的有过度动画，不要太生硬,material transtion

# 关于图片
  这个需要看项目的业务了，也就那几个老牌的库UIL，Glide,Picasso,fresco,bitmapFun;Glide与Picasso非常相似。

# 关于Crash 的采集
  Bugly吧

# 关于项目中的素材
  多用webp,.9.png,还有很多新的工程构建

# 关于5大布局 的发展
  现在不只是FrameLayout，LinearLayouy  等等以前的，还有很多新的如percentLayout,flexlayout coordilayout
  等等都是Google 官方新出来的推荐使用的布局样式，多使用新的

# toolbar
  设计师会遵循Android 的规范设计了吧!讲真现在很多设计师如果还不管Android 的一些设计，我想他们除了懒不想去
  了解更多的东西我想不出其他原因，还拿iOS 的那一套，不管Android 系统规范就算了。甚至不知SP，DP 单位，cry

# 关于依赖注解
  Dragger2 吧 ... ...

# 关于调试工具
 推荐Facebook stetho ，可以网络请求（抓包），不root查看DB 文件和sharepreference，甚至在4.4 以后webview
 可以远程调试；提高生产力的利器啊。

# 安全加密
  混淆Proguard，https,数据库加密，密码明文传输是什么鬼，多多oauth。

# 推送
  会越来越严格，目前没有很好的第三方，绿色联盟？如果企业对推送有强需求，还是建议用短信和微信消息做补充

# Rxjava
  https://realm.io/cn/news/kau-felipe-lima-adopting-rxjava-airbnb-android/

# 应用中使用的第三方框架
  其实大部分的App 最难以做好的模块是Http，http 模块封装的好，做起业务来很方便。关于第三方框架不要聚合型
  的Afinal,xUtils,issues很多不解决的，仿XXUI的慎用（仿你妹，MD不行吗？）
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
  - butterknife（view注入）/dragger(性能优)/dragger2 (power by google base dragger)
  - rxjava2,rxAndroid（Google 也有agera）,square 公司的套件包
  - Bugly
  - leakcanary
  - Facebook stetho
  - safeJsbridge
  - kotlin

# 项目中包含的基本的通用模块
- [混淆压缩打包优化 Proguard　proguard-android-optimize　和 proguard-android 区别 ？](https://github.com/D-clock/Doc/blob/master/Android/Gradle/4_AndroidStudio%E4%B8%8BProGuard%E6%B7%B7%E6%B7%86%E6%89%93%E5%8C%85.md)
- Toolbar 的处理
- Fragment 的懒加载
- 通用的BaseActivity 和BaseFragment的封装（跳转PV打点，事件打点，不放和base 无关的东西）
- 通用的Lib module 的封装
- Http 的闭环处理
- Proguard 混淆 打包优化
- BaseWebView 的处理（未完善，Android 的坑很多）
- support lib 和动画