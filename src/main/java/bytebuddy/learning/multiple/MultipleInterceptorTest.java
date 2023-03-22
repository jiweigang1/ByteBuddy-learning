package bytebuddy.learning.multiple;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ImplementationDefinition;
import net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ReceiverTypeDefinition;
import net.bytebuddy.matcher.ElementMatchers;

public class MultipleInterceptorTest {
    
    public static void main(String[] args) throws Exception {
        
        ImplementationDefinition<ExampleClass> im = new ByteBuddy()
                .subclass(ExampleClass.class)
                .method(ElementMatchers.named("exampleMethod"));
                
                im.intercept(Advice.to(ExampleInterceptor2.class));
        //  intercept 每一次调用都是返回多个实例，这样是不能绑定多个函数的      
        ReceiverTypeDefinition<ExampleClass>  re =        im.intercept(Advice.to(ExampleInterceptor2.class));

        ExampleClass exampleClass = re.make()
                .load(ExampleClass.class.getClassLoader())
                .getLoaded()
                .newInstance();
        
        String result = exampleClass.exampleMethod("foo", "bar");
        System.out.println(result); // "Modified: foo bar"
    }
    
}
