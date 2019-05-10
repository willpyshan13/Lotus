# Lotus
模块化路由Lotus
  
原理：  
1、通过注解+APT将接口类与代理类生成映射表  
2、通过groovy+asm将映射表注入lotus  
3、动态代理+反射调用
    
lotus -- lotus入口  
    
annotation -- 注解+接口module  
  
compiler -- apt：生成注解类的映射表  
  
buildSrc -- plugin，将映射表注入lotus  
  
使用姿势：  
公用mudule依赖：  
api project(':lotus')  
  
重要：有用到Lotus注解的module都要依赖：  
annotationProcessor project(':compiler')  
配置：  
android {  
  defaultConfig {  
      javaCompileOptions {  
          annotationProcessorOptions {  
              arguments = [moduleName: project.getName()]  
          }  
      }  
  }  
}  
  
  
例子：模块1调用模块2的代码  

模块1：  
创建接口  
@LotusImpl("路由路径")  
XXXImpl{  
    
  void method1(参数1,参数2)  
    
  返回值 method2()  
}    
    
模块2：    
创建代理类  
@LotusProxy("路由路径")  //路由路径必须与模块1的路由路径一致  
XXXProxy{  
  
  //方法名与参数必须与模块1的方法名与参数一致  
  public void method1(参数1,参数2){  
      // do something  
  }  
    
  public 返回值 method2(){  
     //do something  
     return 返回值  
  }  
}  
    
  
模块1调用模块2  
Lotus.getInstance().invoke(XXXInterface.class).method1(参数1,参数2)  
返回值 = Lotus.getInstance().invoke(XXXInterface.class).method2()  
  
  
