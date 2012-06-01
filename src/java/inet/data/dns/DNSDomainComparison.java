package inet.data.dns;

public class DNSDomainComparison {

private static final int charA = (int)'A';
private static final int charZ = (int)'Z';

public static int
domainCompare(byte[] left, long leftLen, byte[] right, long rightLen) {
    return domainCompare(true, left, leftLen, right, rightLen);
}

public static int
domainCompare(boolean stable, byte[] left, long leftLen,
              byte[] right, long rightLen) {
    int length = leftLen < rightLen ? (int)leftLen : (int)rightLen;
    for (int i = 0; i < length; ++i) {
        int b1 = (int)left[i] & 0xff;
        int b2 = (int)right[i] & 0xff;
        if (charA <= b1 && b1 <= charZ) b1 = b1 | 0x20;
        if (charA <= b2 && b2 <= charZ) b2 = b2 | 0x20;
        if (b1 != b2)
            return b1 - b2;
    }
    return stable ? (int)(leftLen - rightLen) : 0;
}

public static boolean
domainEquals(byte[] left, byte[] right, int length) {
    for (int i = 0; i < length; ++i) {
        if (left[i] != right[i])
            return false;
    }
    return true;
}

}
