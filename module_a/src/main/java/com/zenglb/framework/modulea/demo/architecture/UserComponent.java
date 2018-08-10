package com.zenglb.framework.modulea.demo.architecture;

import dagger.Component;

/**
 *
 * Created by zlb on 2017/11/20.
 */
//3 指明Component在哪些Module中查找依赖
@Component(modules={UserModule.class})
//4 所有的Component都必须以接口形式定义。Dagger2框架将自动生成Component的实现类，对应的类名是Dagger×××××，
//这个例子中对应的实现类是DaggerFruitComponent
public interface UserComponent {
    //5  注入方法，在Container中调用 ，一般使用inject做为方法名，方法参数为对应的Container
    void inject(UserProfileViewModel container);
}

