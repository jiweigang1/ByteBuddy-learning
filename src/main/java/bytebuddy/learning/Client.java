package bytebuddy.learning;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType.Loaded;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;

public class Client {
   
    public interface MethodCaller {
        void call1();
        void call2();
    }

    /**
     * 歌手
     */
    public static class Call implements MethodCaller {
        @Override
        public void call1() {
            System.out.println(" call 1 ");
        }

        @Override
        public void call2() {
            System.out.println(" call 2 ");
        }
    }


    public static void main(String[] args) throws Exception {
      MethodCaller proxy = createByteBuddyDynamicProxy();
      proxy.call1();
      System.out.println(proxy.toString());
    }
  
    private static MethodCaller createByteBuddyDynamicProxy() throws Exception {
        Loaded<? extends MethodCaller> proxy = (Loaded<? extends MethodCaller>)new ByteBuddy().subclass(Object.class)
          .implement(MethodCaller.class)
          .method(ElementMatchers.isDeclaredBy(MethodCaller.class))
          .intercept(InvocationHandlerAdapter.of(new SingerInvocationHandler(new Call())))
          .make()
          .load(Client.class.getClassLoader());

          proxy.saveIn(new File("./so/"));
          
          return (MethodCaller)proxy.getLoaded().getDeclaredConstructor().newInstance();

          
    }
  
    public static class SingerInvocationHandler implements InvocationHandler {
  
      private Object delegate;
  
      public SingerInvocationHandler(Object delegate) {
        this.delegate = delegate;
      }
  
      /**
       * 动态代理调用方法
       *
       * @param proxy 生成的代理对象
       * @param method 代理的方法
       * @param args 方法参数
       */
      @Override
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("bytebuddy proxy before sing ");
        Object ret = method.invoke(delegate, args);
        System.out.println("bytebuddy proxy after sing ");
        return ret;
      }
  
    }
  }