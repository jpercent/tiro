package syndeticlogic.tiro;

public interface LBAGenerator {
    long getNextOffset(int lastIOSize);
}
