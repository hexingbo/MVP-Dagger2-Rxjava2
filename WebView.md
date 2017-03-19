# 号称IE6 的Android webview
结合H5页面开发的App日渐多了起来，而WebView正是Html与Native的纽带，将适配好的网页打包成App，并能够调用系
统摄像头进行二维码识别、拍照或是选择本地图片上传、获取用户位置等。但不同Android系统版本差异性太大，Webkit
的各个版本特性也不一样，甚至Android4.2 以前的webkit 还有非常非常严重的漏洞，让人滴血，就一个拍照的问题能
让开发者哀声一片。More ：https://www.zhihu.com/question/31316646

# 关于WebView 中拍照/选图上传
因为部分Android4.4 webview 无法处理type=file 标记，也就是点击了没有反应，我们的做法就是使用JS调用Native
方法来处理，最后图片Bitmap会通过Base64编码给JS。
param.content = "data:image/jpg;base64," + Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)。
当然本段文字要说的不是这个，要说的是加入在拍照过程中暂停去使用其他App后发现回来页面填写的数据被重置(其实
是Activity 被回收了)有些系统厂商的 ROM 会给自带相机应用做优化，当某个 app 通过 intent 进入相机拍照界面时，
系统会把这个 app 当前最上层的 Activity 销毁回收（测试的是M3 Note，但是onDestroy 并没有回调），不知道大家有
没有好的方法，我是简单的启动了一个前台service，提高App进程的优先级，让系统不会清理发起相机调用的Activity。

# _)(*&^%$#@^$#%^$#%$#


绝对的防止被杀死是不可能的。 可以用下面的方法尽量避免。
1. 调用startForegound，让你的Service所在的进程成为前台进程
2. Service的onStartCommond返回START_STICKY或START_REDELIVER_INTENT
3. Service的onDestory里面重新启动自己

