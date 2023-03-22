package bytebuddy.learning.multiple;

public class State {
    private String  v = "";
    public State(String v){
        this.v = v;
    }

    public String toString(){
        return this.v;
    }
}
