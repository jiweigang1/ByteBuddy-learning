package bytebuddy.learning;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.DynamicType.Loaded;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;


/**
 * 使用 InvocationHandler 动态代理
 */
public class InvocationHandlerClass {

    public void call(){
        System.out.println(" call ");
    }

    public void test(){
        System.out.println(" test ");
    }

    public static interface HandlerSetter {
        InvocationHandler getHandler();
        void setHandler(InvocationHandler handler);
    }


    public static void main(String[] args) throws Exception {
        Loaded<InvocationHandlerClass> proxy = new ByteBuddy()
                        .subclass(InvocationHandlerClass.class)
                        .defineField("handler", InvocationHandler.class, Visibility.PUBLIC)
                        .implement(HandlerSetter.class)
                        //设置属性的 get  和 set 方法
                        .intercept(FieldAccessor.ofField("handler"))
                        .method(ElementMatchers.isDeclaredBy(InvocationHandlerClass.class))
                        //通过属性对象拦截方法
                        .intercept(InvocationHandlerAdapter.toField("handler"))
                        .make()
                        .load(InvocationHandlerClass.class.getClassLoader());

    proxy.saveIn(new File("./so/"));

    final InvocationHandlerClass origin  = new InvocationHandlerClass(); 
          InvocationHandlerClass some    = proxy.getLoaded().getDeclaredConstructor().newInstance();
    ((HandlerSetter)some).setHandler(new InvocationHandler() {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println(" call it ");
            return method.invoke(origin, args);
        }
        
    });;
                    some.call();
  }
}
