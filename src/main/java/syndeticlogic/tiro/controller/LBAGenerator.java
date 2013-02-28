package syndeticlogic.tiro.controller;

public interface LBAGenerator {
    long getNextOffset(int lastIOSize);
}
