package bytebuddy.learning.fieldproxy;

import bytebuddy.learning.fieldproxy.ProxyClass.ProxyClassInterceptorSetter;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.FieldProxy;
import net.bytebuddy.jar.asm.Opcodes;
import net.bytebuddy.matcher.ElementMatchers;

public class Example {
    public static void main(String[] args) throws Exception {
        TargetClass target = new TargetClass("Hello, world!");
        ProxyClass proxy = new ProxyClass();

        target = new ByteBuddy()
            .subclass(TargetClass.class)
            //.defineField("_ok", ProxyClass.class)
            .defineField("_ok", String.class, Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL).value("world")
            .method(ElementMatchers.named("getMessage"))
            
            //.intercept(MethodDelegation.to(proxy))
            .intercept(MethodDelegation.withDefaultConfiguration().withBinders(FieldProxy.Binder.install(ProxyClassInterceptorSetter.class)).to(proxy))
            .make()
            .load(TargetClass.class.getClassLoader())
            .getLoaded()
            .newInstance();

        //target.getMessage(); // Prints "Intercepted message: Hello, world!"
        String ok  = target.getMessage();
        System.out.println( ok.toString() + ">>>");
        System.out.println("end");
    }

    //MethodDelegation.withDefaultConfiguration().withBinders(FieldProxy.Binder.install(ProxyClassInterceptorSetter.class)).to(proxy)
    
}