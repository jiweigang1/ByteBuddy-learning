package bytebuddy.learning.multiple;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

public class ExampleInterceptor2 {
    @Advice.OnMethodEnter
    public static void enter(@Advice.Local("state") State state , @Advice.Argument(0) String arg1, @Advice.Argument(1) String arg2) {
        System.out.println("Entering method 2 with arguments: " + arg1 + " " + arg2);
        state = new State("state 2");
    }
    
    @Advice.OnMethodExit
    public static void exit(@Advice.Local("state") State state, @Advice.Return(readOnly = false) String returnValue) {
        System.out.println("Exiting method 2 with return value: " + returnValue);
        System.out.println(state);
        returnValue = "Modified: " + returnValue;
    }
}
