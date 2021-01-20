public class IncreaseSize {
    int time = 0;
    int current = 0;
    int wanted = 0;
    int increment;
    public IncreaseSize(int current, int wanted) {
        this.current = current;
        this.wanted = wanted;
    }
    public void setSleepTime(int t) {
        this.time = t;
    }
    public void setIncrement(int i) {
        this.increment = i;
    }
    public int getSize() {
        return current;
    }
    public int getIncrementSize() {
        if(current < wanted) {
            current+=increment;
        }
        try {
            Thread.sleep(time);
        } catch(Exception err1) {}
        return current;
    }
}
