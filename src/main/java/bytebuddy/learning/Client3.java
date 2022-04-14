package bytebuddy.learning;

import java.lang.reflect.Method;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.This;
import net.bytebuddy.matcher.ElementMatchers;

public class Client3 {
    /**
 * 可以唱歌的
 */
public interface Singable {
    /**
     * 唱歌
     */
    void sing();
  }

  /**
 * 歌手
 */
public static class Singer implements Singable {
    @Override
    public void sing() {
      System.out.println("I am singing...");
    }
  }

  public static void main(String[] args) throws Exception {
    Singable proxy = createByteBuddyDynamicProxy();
    proxy.sing();
    System.out.println(proxy.toString());
  }

  private static Singable createByteBuddyDynamicProxy() throws Exception {
    return (Singable) new ByteBuddy().subclass(Object.class)
        .implement(Singable.class)
        .method(ElementMatchers.named("sing"))
        .intercept(MethodDelegation.to(new SingerAgentInterceptor(new Singer())))
        .make()
        .load(Client.class.getClassLoader())
        .getLoaded()
        .getDeclaredConstructor()
        .newInstance();
  }

  public static class SingerAgentInterceptor {

    private Object delegate;

    public SingerAgentInterceptor(Object delegate) {
      this.delegate = delegate;
    }

    /**
     * @param proxy 代理对象
     * @param method 代理方法
     * @param args 方法参数
     */
    public Object interceptor(@This Object proxy, @Origin Method method,
        @AllArguments Object[] args) throws Exception {
      System.out.println("bytebuddy delegate proxy before sing ");
      Object ret = method.invoke(delegate, args);
      System.out.println("bytebuddy delegate proxy after sing ");
      return ret;
    }
  }
}
