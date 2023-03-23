package bytebuddy.learning.fieldproxy;


public class TargetClass {
    private String message ="adsad";
    //private String _ok = "12213312";

    public TargetClass() {
        
    }
    public TargetClass(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}