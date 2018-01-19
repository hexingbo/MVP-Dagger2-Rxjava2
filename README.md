# FBI WARMIMNG:api 仅供交流，不要外传 ！


# 关于本Demo
  一直在纠结在何种开发模式之中，重构希望能把关注点集中到代码结构、整体架构、可测试性、可维护性这四个方面
  Rxjava2 + retrofit2 + MVP + Drager2 + xxx,应该是当前Android开发主流的框架 ，我们都会参考Google的官方框架https://github.com/googlesamples/android-architecture 或者 Google 的最新的项目架构组件https://github.com/googlesamples/android-architecture-components
  

# 关于Http网络请求 (Rxjava2+Retrofit2)
  应该没有比Retrofit2 更好的了吧？不过api 不是restful 就需要再封装一下了，网路模块就是数据命脉，做好了
  整个app 的结构会简化很多，结合Rxjava2不是更快哉;配合RxLifeCycle 控制生命周期;
  BaseObserver 中getErrorMsg(HttpException httpException) 方法中的处理和我们的Api  结构有关，请知悉。可以在Activity，fragment，service，broadcast 等发起http请求。


# UI架构模型
  Android应用的UI架构模型经历了MVC,MVP 和 MVVM 的演变过程。MVC中View 层（Activity，Fragment/自定义的View）
  可能代码会随着业务的复杂变得很大，里面不但要处理界面，还要处理很多业务逻辑里面承载了太多的东西，试试MVP吧，
  已经是很流行的UI架构模型了。
  使用MVP多关注代码结构、整体架构、可测试性、可维护性这四个方面

# 关于Dagger
  以前在使用dagger2的时候感觉理解绕，而且也违背依赖注入的核心原则：一个类不应该知道如何实现依赖注入；它要求注射类型知道  
  其注射器; 即使这是通过接口而不是具体类型完成的。dagger.android 出来后还大大的减少了模版代码😄, 不用在需要Inject 的地方写xx.build().inject(this);
  
  如果没有Dagger.android 我是不想使用dagger2的。写下面的类似代码实在太多了
  
  More：https://google.github.io/dagger//android.html
  
  ```
  public class FrombulationActivity extends Activity {
    @Inject Frombulator frombulator;
  
    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      
      // DO THIS FIRST. Otherwise frombulator might be null!
      ((SomeApplicationBaseType) getContext().getApplicationContext())
          .getApplicationComponent()
          .newActivityComponentBuilder()
          .activity(this)
          .build()
          .inject(this);
      // ... now you can write the exciting code
      
    }
  }
 
 ```
  
# 关于调试工具
 推荐Facebook stetho ，可以网络请求（抓包），不root查看DB 文件和sharepreference，甚至在4.4 以后webview
 可以远程调试；提高生产力的利器啊。

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
- Dagger.android 大大的优化Dagger 在android 中的使用，
- Toolbar 的处理
- Fragment 的懒加载
- 通用的BaseActivity 和BaseFragment的封装（跳转PV打点，事件打点，不放和base 无关的东西）
- 通用的Lib module 的封装
- Http (Rxjava2+Retrofit2)的闭环处理
- Proguard 混淆 打包优化
- BaseWebView 的 处理（未完善，Android 的坑很多）
- support lib 和 动画
- [混淆压缩打包优化 Proguard　proguard-android-optimize　和 proguard-android 区别 ？](https://github.com/D-clock/Doc/blob/master/Android/Gradle/4_AndroidStudio%E4%B8%8BProGuard%E6%B7%B7%E6%B7%86%E6%89%93%E5%8C%85.md)
































