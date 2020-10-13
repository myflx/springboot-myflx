package number;

/**
 * 分段状态
 */
public class Status {
    public int lSum;
    public int rSum;
    public int mSum;
    public int iSum;

    public Status(int lSum, int rSum, int mSum, int iSum) {
        this.lSum = lSum;
        this.rSum = rSum;
        this.mSum = mSum;
        this.iSum = iSum;
    }
}
