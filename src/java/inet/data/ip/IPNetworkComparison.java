package inet.data.ip;

public class IPNetworkComparison {

public static int
networkCompare(byte[] left, long leftLen, byte[] right, long rightLen) {
    return networkCompare(true, left, leftLen, right, rightLen);
}

public static int
networkCompare(boolean stable,
               byte[] prefix1, long plen1,
               byte[] prefix2, long plen2) {
    int length = Math.min((int)plen1, (int)plen2);
    int len1 = prefix1.length;
    int len2 = prefix2.length;

    if (len1 != len2)
        return (len1 - len2);
    for (int i = 0, rem = length; rem > 0; ++i, rem -= 8) {
        int mask = (rem > 8) ? 0xff : (0xff & ~(0xff >> rem));
        int b1 = (int)prefix1[i] & mask;
        int b2 = (int)prefix2[i] & mask;
        if (b1 != b2)
            return (b1 - b2);
    }
    return stable ? (int)(plen1 - plen2) : 0;
}

}
