package bytebuddy.learning;

import java.io.File;
import java.lang.reflect.Method;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice.Origin;
import net.bytebuddy.asm.Advice.This;
import net.bytebuddy.dynamic.DynamicType.Loaded;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperMethod;
import net.bytebuddy.matcher.ElementMatchers;

public class Client2 {
   
    public interface MethodCaller1 {
        String call1(String ok);
        void call2();
    }

    /**
     * 歌手
     */
    public static class Call implements MethodCaller1 {
        @Override
        public String call1(String ok) {
            System.out.println(" call 1 ");
            return "";
        }

        @Override
        public void call2() {
            System.out.println(" call 2 ");
        }
    }


    public static void main(String[] args) throws Exception {
      MethodCaller1 proxy = createByteBuddyDynamicProxy();
      proxy.call1("");
    }
  
    private static MethodCaller1 createByteBuddyDynamicProxy() throws Exception {
        Loaded<? extends MethodCaller1> proxy = (Loaded<? extends MethodCaller1>)new ByteBuddy().subclass(Object.class)
          .implement(MethodCaller1.class)
          .method(ElementMatchers.isDeclaredBy(MethodCaller1.class))
          .intercept(MethodDelegation.to(new SingerAgentInterceptor()))
          .make()
          .load(Client.class.getClassLoader());

          proxy.saveIn(new File("./so/"));
          
          return (MethodCaller1)proxy.getLoaded().getDeclaredConstructor().newInstance();

          
    }
  
    public static class SingerAgentInterceptor {
      @RuntimeType
      public Object interceptor(@AllArguments Object[] args) throws Exception {
        System.out.println("bytebuddy delegate proxy2 before sing ");
       
        System.out.println("bytebuddy delegate proxy2 after sing ");
        return null;
      }
    }
  }