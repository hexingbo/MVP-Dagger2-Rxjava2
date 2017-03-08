#为什么需要JSBridge
为了追求开发的效率以及移植的便利性，一些展示性强的页面我们会偏向于使用h5来完成，功能性强的页面我们会偏向于使用native来完成，而一旦使用了h5，为了在h5中尽可能的得到native的体验，我们native层需要暴露一些方法给js调用，比如，支付，定位，弹Dialog，分享等等，有时候甚至把h5的网络请求放着native去完成  

#怎样实现Java 和 JS的相互通信  
- Java 调用 JS 
在WebView中，如果Java要调用js的方法，是非常容易做到的，使用WebView.loadUrl(“JavaScript:function()”)即可
- H5中怎样调用Native 呢   
Android中可以给WebView.setWebChromeClient,然后在当js调用window对象的window.prompt 方法后进行拦截
iOS 是否也可以使用这种方法呢？待确定！不行就进行URL拦截 shouldOverrideUrlLoading,不管怎样iOS 和Android 
Native 和JS 之间的通信协议还是可以通用的

#JS 与 Native 的通信协议    
http://host:port/path?param=value  我们可以参考http 的通信协议，然后结合我们的业务逻辑制定JsBridge 通信协议。
jsbridge://className:callbackAddr（port）/methodName?jsonObj

jsbridge:
className:
callbackAddr(port):
methodName:
jsonObj:

#JS 调用Native然后Native返回数据给JS工作流程
这里Native 返回数据给JS 都是指的异步，下面用图解释一下这个处理流程的闭环
