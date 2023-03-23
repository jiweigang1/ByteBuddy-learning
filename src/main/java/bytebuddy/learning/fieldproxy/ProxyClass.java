package bytebuddy.learning.fieldproxy;

import net.bytebuddy.implementation.bind.annotation.*;

public class ProxyClass {
    
    private String messageProxy = "1111";

    @RuntimeType
    public Object intercept(@FieldProxy("_ok")  ProxyClassInterceptorSetter  proxyClassInterceptorSetter) {
        System.out.println("Intercepted message: " + proxyClassInterceptorSetter.getValue());
        return "123213213";
    }

    public  static interface ProxyClassInterceptorSetter{
        Object getValue();
        void setValue(Object value);
    }

}