package inet.data.ip;

public class IPParser {

public static final int IPV4_BYTE_LEN = 4;
public static final int IPV6_BYTE_LEN = 16;

public static final int IPV4_BIT_LEN = 32;
public static final int IPV6_BIT_LEN = 128;

public static final int INVALID = 0;
public static final int VERSION4 = 4;
public static final int VERSION6 = 6;

private static final byte[] INVALID_ADDR_BYTES = new byte[0];

// data
%% machine ipaddr;
%% write data;

public static int
parse(String addr, byte[] buffer) {
    if (addr == null) return INVALID;
    int version = INVALID;
    int octet = 0;
    int h16 = 0;
    int nbytes = 0;
    int elision = -1;

    int cs = 0;
    int p = 0;
    int pe = addr.length();
    int eof = pe;

    %%{
        action beg_octet { octet = 0; }
        action ddigit { octet = (octet * 10) + (((int) fc) - 0x30); }
        action end_octet {
            if (buffer != null) {
                buffer[nbytes++] = (byte) (octet & 0xff);
            }
        }

        action beg_h16 { h16 = 0; }
        action hdigit {
            h16 <<= 4;
            int d = (int) fc;
            if      (d <= 0x39) h16 += d - 0x30;
            else if (d <= 0x46) h16 += d - 0x41 + 10;
            else                h16 += d - 0x61 + 10;
        }
        action end_h16 {
            if (buffer != null) {
                buffer[nbytes++] = (byte) ((h16 >>> 8) & 0xff);
                buffer[nbytes++] = (byte) (h16 & 0xff);
            }
        }
        action elision { elision = nbytes; }

        action ipv4 { version = VERSION4; }
        action ipv6 { version = VERSION6; }

        dec_octet = ( (           digit ) |
                      (     [1-9] digit ) |
                      ( "1" digit digit ) |
                      ( "2" [0-4] digit ) |
                      ( "2"   "5" [0-5] ) )
            >beg_octet $ddigit %end_octet;
        IPv4address = dec_octet ( "." dec_octet ){3};

        h16 = xdigit{1,4} >beg_h16 $hdigit %end_h16;
        ls32 = ( h16 ":" h16 ) | IPv4address;
        elision = "::" %elision;
        IPv6address =
            ( (                                  ( h16 ":" ){6} ls32 ) |
              (                          elision ( h16 ":" ){5} ls32 ) |
              ( (                 h16 )? elision ( h16 ":" ){4} ls32 ) |
              ( ( ( h16 ":" ){,1} h16 )? elision ( h16 ":" ){3} ls32 ) |
              ( ( ( h16 ":" ){,2} h16 )? elision ( h16 ":" ){2} ls32 ) |
              ( ( ( h16 ":" ){,3} h16 )? elision   h16 ":"      ls32 ) |
              ( ( ( h16 ":" ){,4} h16 )? elision                ls32 ) |
              ( ( ( h16 ":" ){,5} h16 )? elision                 h16 ) |
              ( ( ( h16 ":" ){,6} h16 )? elision                       ) );

        main := IPv4address %ipv4 | IPv6address %ipv6;

        alphtype char;
        getkey addr.charAt(p);
    }%%

    %% write init;
    %% write exec;

    if (cs < ipaddr_first_final)
        return INVALID;

    if (buffer != null && elision >= 0) {
        int diff = IPV6_BYTE_LEN - nbytes;
        for (int i = elision; i < nbytes; i++) {
            buffer[i + diff] = buffer[i];
            buffer[i] = 0;
        }
    }

    return version;
}

public static byte[]
parse(String addr) {
    byte[] result = null;
    int type = parse(addr, null);

    switch (type) {
    case VERSION4: result = new byte[IPV4_BYTE_LEN]; break;
    case VERSION6: result = new byte[IPV6_BYTE_LEN]; break;
    default:       return INVALID_ADDR_BYTES;
    }
    parse(addr, result);

    return result;
}

public static int
version(String addr) {
    return parse(addr, null);
}

public static boolean
isValid(String addr) {
    return (version(addr) != INVALID);
}

public static boolean
isValidIPv4(String addr) {
    return (version(addr) == VERSION4);
}

public static boolean
isValidIPv6(String addr) {
    return (version(addr) == VERSION6);
}

public static int
length(String addr) {
    switch (version(addr)) {
    case VERSION4: return IPV4_BIT_LEN;
    case VERSION6: return IPV6_BIT_LEN;
    default:       return -1;
    }
}

}
