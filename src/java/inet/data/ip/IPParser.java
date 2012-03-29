
// line 1 "src/java/inet/data/ip/IPParser.java.rl"
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

// line 19 "src/java/inet/data/ip/IPParser.java.rl"

// line 24 "src/java/inet/data/ip/IPParser.java"
private static byte[] init__ipaddr_actions_0()
{
	return new byte [] {
	    0,    1,    1,    1,    2,    1,    4,    1,    5,    2,    0,    1,
	    2,    1,    4,    2,    2,    7,    2,    2,    8,    2,    3,    4,
	    2,    4,    1,    2,    5,    8,    2,    6,    8,    3,    6,    3,
	    4,    4,    0,    1,    3,    4,    4,    3,    4,    0,    1,    5,
	    6,    3,    4,    0,    1
	};
}

private static final byte _ipaddr_actions[] = init__ipaddr_actions_0();


private static short[] init__ipaddr_key_offsets_0()
{
	return new short [] {
	    0,    0,   10,   18,   23,   24,   29,   30,   35,   38,   41,   47,
	   50,   53,   56,   62,   65,   72,   79,   80,   87,   94,  101,  108,
	  109,  116,  123,  130,  137,  138,  145,  152,  159,  166,  167,  174,
	  181,  188,  195,  196,  203,  210,  217,  224,  225,  235,  243,  248,
	  249,  254,  255,  260,  263,  266,  272,  275,  278,  281,  287,  290,
	  297,  304,  305,  312,  320,  328,  336,  347,  357,  365,  373,  380,
	  386,  395,  404,  413,  422,  430,  438,  446,  457,  467,  475,  483,
	  484,  493,  500,  500,  502,  504,  509,  511,  511,  513,  515,  520,
	  522,  528,  534,  540,  540,  540,  546,  555,  563,  570,  577,  578,
	  586,  594,  602,  613,  623,  631,  639,  646,  655,  663,  670,  677,
	  678,  686,  694,  702,  713,  723,  731,  739,  746,  755,  763,  770,
	  777,  778,  786,  794,  802,  813,  823,  831,  839,  846,  855,  863,
	  870,  877,  878,  886,  894,  902,  913,  923,  931,  939,  946,  955,
	  963,  970,  977,  978,  986,  994, 1002, 1013, 1023, 1031, 1039, 1046,
	 1055, 1063, 1070, 1077, 1078, 1086, 1094, 1102, 1113, 1123, 1131, 1139
	};
}

private static final short _ipaddr_key_offsets[] = init__ipaddr_key_offsets_0();


private static char[] init__ipaddr_trans_keys_0()
{
	return new char [] {
	   48,   49,   50,   58,   51,   57,   65,   70,   97,  102,   46,   58,
	   48,   57,   65,   70,   97,  102,   48,   49,   50,   51,   57,   46,
	   48,   49,   50,   51,   57,   46,   48,   49,   50,   51,   57,   46,
	   48,   57,   46,   48,   57,   46,   53,   48,   52,   54,   57,   46,
	   48,   53,   46,   48,   57,   46,   48,   57,   46,   53,   48,   52,
	   54,   57,   46,   48,   53,   58,   48,   57,   65,   70,   97,  102,
	   58,   48,   57,   65,   70,   97,  102,   58,   58,   48,   57,   65,
	   70,   97,  102,   58,   48,   57,   65,   70,   97,  102,   58,   48,
	   57,   65,   70,   97,  102,   58,   48,   57,   65,   70,   97,  102,
	   58,   58,   48,   57,   65,   70,   97,  102,   58,   48,   57,   65,
	   70,   97,  102,   58,   48,   57,   65,   70,   97,  102,   58,   48,
	   57,   65,   70,   97,  102,   58,   58,   48,   57,   65,   70,   97,
	  102,   58,   48,   57,   65,   70,   97,  102,   58,   48,   57,   65,
	   70,   97,  102,   58,   48,   57,   65,   70,   97,  102,   58,   58,
	   48,   57,   65,   70,   97,  102,   58,   48,   57,   65,   70,   97,
	  102,   58,   48,   57,   65,   70,   97,  102,   58,   48,   57,   65,
	   70,   97,  102,   58,   58,   48,   57,   65,   70,   97,  102,   58,
	   48,   57,   65,   70,   97,  102,   58,   48,   57,   65,   70,   97,
	  102,   58,   48,   57,   65,   70,   97,  102,   58,   48,   49,   50,
	   58,   51,   57,   65,   70,   97,  102,   46,   58,   48,   57,   65,
	   70,   97,  102,   48,   49,   50,   51,   57,   46,   48,   49,   50,
	   51,   57,   46,   48,   49,   50,   51,   57,   46,   48,   57,   46,
	   48,   57,   46,   53,   48,   52,   54,   57,   46,   48,   53,   46,
	   48,   57,   46,   48,   57,   46,   53,   48,   52,   54,   57,   46,
	   48,   53,   58,   48,   57,   65,   70,   97,  102,   58,   48,   57,
	   65,   70,   97,  102,   58,   58,   48,   57,   65,   70,   97,  102,
	   46,   58,   48,   57,   65,   70,   97,  102,   46,   58,   48,   57,
	   65,   70,   97,  102,   46,   58,   48,   57,   65,   70,   97,  102,
	   46,   53,   58,   48,   52,   54,   57,   65,   70,   97,  102,   46,
	   58,   48,   53,   54,   57,   65,   70,   97,  102,   46,   58,   48,
	   57,   65,   70,   97,  102,   46,   58,   48,   57,   65,   70,   97,
	  102,   58,   48,   57,   65,   70,   97,  102,   48,   57,   65,   70,
	   97,  102,   48,   49,   50,   51,   57,   65,   70,   97,  102,   48,
	   49,   50,   51,   57,   65,   70,   97,  102,   48,   49,   50,   51,
	   57,   65,   70,   97,  102,   48,   49,   50,   51,   57,   65,   70,
	   97,  102,   46,   58,   48,   57,   65,   70,   97,  102,   46,   58,
	   48,   57,   65,   70,   97,  102,   46,   58,   48,   57,   65,   70,
	   97,  102,   46,   53,   58,   48,   52,   54,   57,   65,   70,   97,
	  102,   46,   58,   48,   53,   54,   57,   65,   70,   97,  102,   46,
	   58,   48,   57,   65,   70,   97,  102,   46,   58,   48,   57,   65,
	   70,   97,  102,   58,   48,   49,   50,   51,   57,   65,   70,   97,
	  102,   58,   48,   57,   65,   70,   97,  102,   48,   57,   48,   57,
	   53,   48,   52,   54,   57,   48,   53,   48,   57,   48,   57,   53,
	   48,   52,   54,   57,   48,   53,   48,   57,   65,   70,   97,  102,
	   48,   57,   65,   70,   97,  102,   48,   57,   65,   70,   97,  102,
	   48,   57,   65,   70,   97,  102,   48,   49,   50,   51,   57,   65,
	   70,   97,  102,   46,   58,   48,   57,   65,   70,   97,  102,   58,
	   48,   57,   65,   70,   97,  102,   58,   48,   57,   65,   70,   97,
	  102,   58,   46,   58,   48,   57,   65,   70,   97,  102,   46,   58,
	   48,   57,   65,   70,   97,  102,   46,   58,   48,   57,   65,   70,
	   97,  102,   46,   53,   58,   48,   52,   54,   57,   65,   70,   97,
	  102,   46,   58,   48,   53,   54,   57,   65,   70,   97,  102,   46,
	   58,   48,   57,   65,   70,   97,  102,   46,   58,   48,   57,   65,
	   70,   97,  102,   58,   48,   57,   65,   70,   97,  102,   48,   49,
	   50,   51,   57,   65,   70,   97,  102,   46,   58,   48,   57,   65,
	   70,   97,  102,   58,   48,   57,   65,   70,   97,  102,   58,   48,
	   57,   65,   70,   97,  102,   58,   46,   58,   48,   57,   65,   70,
	   97,  102,   46,   58,   48,   57,   65,   70,   97,  102,   46,   58,
	   48,   57,   65,   70,   97,  102,   46,   53,   58,   48,   52,   54,
	   57,   65,   70,   97,  102,   46,   58,   48,   53,   54,   57,   65,
	   70,   97,  102,   46,   58,   48,   57,   65,   70,   97,  102,   46,
	   58,   48,   57,   65,   70,   97,  102,   58,   48,   57,   65,   70,
	   97,  102,   48,   49,   50,   51,   57,   65,   70,   97,  102,   46,
	   58,   48,   57,   65,   70,   97,  102,   58,   48,   57,   65,   70,
	   97,  102,   58,   48,   57,   65,   70,   97,  102,   58,   46,   58,
	   48,   57,   65,   70,   97,  102,   46,   58,   48,   57,   65,   70,
	   97,  102,   46,   58,   48,   57,   65,   70,   97,  102,   46,   53,
	   58,   48,   52,   54,   57,   65,   70,   97,  102,   46,   58,   48,
	   53,   54,   57,   65,   70,   97,  102,   46,   58,   48,   57,   65,
	   70,   97,  102,   46,   58,   48,   57,   65,   70,   97,  102,   58,
	   48,   57,   65,   70,   97,  102,   48,   49,   50,   51,   57,   65,
	   70,   97,  102,   46,   58,   48,   57,   65,   70,   97,  102,   58,
	   48,   57,   65,   70,   97,  102,   58,   48,   57,   65,   70,   97,
	  102,   58,   46,   58,   48,   57,   65,   70,   97,  102,   46,   58,
	   48,   57,   65,   70,   97,  102,   46,   58,   48,   57,   65,   70,
	   97,  102,   46,   53,   58,   48,   52,   54,   57,   65,   70,   97,
	  102,   46,   58,   48,   53,   54,   57,   65,   70,   97,  102,   46,
	   58,   48,   57,   65,   70,   97,  102,   46,   58,   48,   57,   65,
	   70,   97,  102,   58,   48,   57,   65,   70,   97,  102,   48,   49,
	   50,   51,   57,   65,   70,   97,  102,   46,   58,   48,   57,   65,
	   70,   97,  102,   58,   48,   57,   65,   70,   97,  102,   58,   48,
	   57,   65,   70,   97,  102,   58,   46,   58,   48,   57,   65,   70,
	   97,  102,   46,   58,   48,   57,   65,   70,   97,  102,   46,   58,
	   48,   57,   65,   70,   97,  102,   46,   53,   58,   48,   52,   54,
	   57,   65,   70,   97,  102,   46,   58,   48,   53,   54,   57,   65,
	   70,   97,  102,   46,   58,   48,   57,   65,   70,   97,  102,   46,
	   58,   48,   57,   65,   70,   97,  102,   58,   48,   57,   65,   70,
	   97,  102,   48,   49,   50,   51,   57,   65,   70,   97,  102,   46,
	   58,   48,   57,   65,   70,   97,  102,   58,   48,   57,   65,   70,
	   97,  102,   58,   48,   57,   65,   70,   97,  102,   58,   46,   58,
	   48,   57,   65,   70,   97,  102,   46,   58,   48,   57,   65,   70,
	   97,  102,   46,   58,   48,   57,   65,   70,   97,  102,   46,   53,
	   58,   48,   52,   54,   57,   65,   70,   97,  102,   46,   58,   48,
	   53,   54,   57,   65,   70,   97,  102,   46,   58,   48,   57,   65,
	   70,   97,  102,   46,   58,   48,   57,   65,   70,   97,  102,   58,
	   48,   57,   65,   70,   97,  102,    0
	};
}

private static final char _ipaddr_trans_keys[] = init__ipaddr_trans_keys_0();


private static byte[] init__ipaddr_single_lengths_0()
{
	return new byte [] {
	    0,    4,    2,    3,    1,    3,    1,    3,    1,    1,    2,    1,
	    1,    1,    2,    1,    1,    1,    1,    1,    1,    1,    1,    1,
	    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,
	    1,    1,    1,    1,    1,    1,    1,    1,    4,    2,    3,    1,
	    3,    1,    3,    1,    1,    2,    1,    1,    1,    2,    1,    1,
	    1,    1,    1,    2,    2,    2,    3,    2,    2,    2,    1,    0,
	    3,    3,    3,    3,    2,    2,    2,    3,    2,    2,    2,    1,
	    3,    1,    0,    0,    0,    1,    0,    0,    0,    0,    1,    0,
	    0,    0,    0,    0,    0,    0,    3,    2,    1,    1,    1,    2,
	    2,    2,    3,    2,    2,    2,    1,    3,    2,    1,    1,    1,
	    2,    2,    2,    3,    2,    2,    2,    1,    3,    2,    1,    1,
	    1,    2,    2,    2,    3,    2,    2,    2,    1,    3,    2,    1,
	    1,    1,    2,    2,    2,    3,    2,    2,    2,    1,    3,    2,
	    1,    1,    1,    2,    2,    2,    3,    2,    2,    2,    1,    3,
	    2,    1,    1,    1,    2,    2,    2,    3,    2,    2,    2,    1
	};
}

private static final byte _ipaddr_single_lengths[] = init__ipaddr_single_lengths_0();


private static byte[] init__ipaddr_range_lengths_0()
{
	return new byte [] {
	    0,    3,    3,    1,    0,    1,    0,    1,    1,    1,    2,    1,
	    1,    1,    2,    1,    3,    3,    0,    3,    3,    3,    3,    0,
	    3,    3,    3,    3,    0,    3,    3,    3,    3,    0,    3,    3,
	    3,    3,    0,    3,    3,    3,    3,    0,    3,    3,    1,    0,
	    1,    0,    1,    1,    1,    2,    1,    1,    1,    2,    1,    3,
	    3,    0,    3,    3,    3,    3,    4,    4,    3,    3,    3,    3,
	    3,    3,    3,    3,    3,    3,    3,    4,    4,    3,    3,    0,
	    3,    3,    0,    1,    1,    2,    1,    0,    1,    1,    2,    1,
	    3,    3,    3,    0,    0,    3,    3,    3,    3,    3,    0,    3,
	    3,    3,    4,    4,    3,    3,    3,    3,    3,    3,    3,    0,
	    3,    3,    3,    4,    4,    3,    3,    3,    3,    3,    3,    3,
	    0,    3,    3,    3,    4,    4,    3,    3,    3,    3,    3,    3,
	    3,    0,    3,    3,    3,    4,    4,    3,    3,    3,    3,    3,
	    3,    3,    0,    3,    3,    3,    4,    4,    3,    3,    3,    3,
	    3,    3,    3,    0,    3,    3,    3,    4,    4,    3,    3,    3
	};
}

private static final byte _ipaddr_range_lengths[] = init__ipaddr_range_lengths_0();


private static short[] init__ipaddr_index_offsets_0()
{
	return new short [] {
	    0,    0,    8,   14,   19,   21,   26,   28,   33,   36,   39,   44,
	   47,   50,   53,   58,   61,   66,   71,   73,   78,   83,   88,   93,
	   95,  100,  105,  110,  115,  117,  122,  127,  132,  137,  139,  144,
	  149,  154,  159,  161,  166,  171,  176,  181,  183,  191,  197,  202,
	  204,  209,  211,  216,  219,  222,  227,  230,  233,  236,  241,  244,
	  249,  254,  256,  261,  267,  273,  279,  287,  294,  300,  306,  311,
	  315,  322,  329,  336,  343,  349,  355,  361,  369,  376,  382,  388,
	  390,  397,  402,  403,  405,  407,  411,  413,  414,  416,  418,  422,
	  424,  428,  432,  436,  437,  438,  442,  449,  455,  460,  465,  467,
	  473,  479,  485,  493,  500,  506,  512,  517,  524,  530,  535,  540,
	  542,  548,  554,  560,  568,  575,  581,  587,  592,  599,  605,  610,
	  615,  617,  623,  629,  635,  643,  650,  656,  662,  667,  674,  680,
	  685,  690,  692,  698,  704,  710,  718,  725,  731,  737,  742,  749,
	  755,  760,  765,  767,  773,  779,  785,  793,  800,  806,  812,  817,
	  824,  830,  835,  840,  842,  848,  854,  860,  868,  875,  881,  887
	};
}

private static final short _ipaddr_index_offsets[] = init__ipaddr_index_offsets_0();


private static short[] init__ipaddr_indicies_0()
{
	return new short [] {
	    0,    2,    3,    5,    4,    6,    6,    1,    7,    9,    8,    8,
	    8,    1,   10,   11,   12,   13,    1,   14,    1,   15,   16,   17,
	   18,    1,   19,    1,   20,   21,   22,   23,    1,   19,   24,    1,
	   19,   25,    1,   19,   26,   24,   25,    1,   19,   25,    1,   14,
	   27,    1,   14,   28,    1,   14,   29,   27,   28,    1,   14,   28,
	    1,    9,   30,   30,   30,    1,    9,   31,   31,   31,    1,    9,
	    1,   33,   32,   32,   32,    1,   35,   34,   34,   34,    1,   35,
	   36,   36,   36,    1,   35,   37,   37,   37,    1,   35,    1,   39,
	   38,   38,   38,    1,   41,   40,   40,   40,    1,   41,   42,   42,
	   42,    1,   41,   43,   43,   43,    1,   41,    1,   45,   44,   44,
	   44,    1,   47,   46,   46,   46,    1,   47,   48,   48,   48,    1,
	   47,   49,   49,   49,    1,   47,    1,   51,   50,   50,   50,    1,
	   53,   52,   52,   52,    1,   53,   54,   54,   54,    1,   53,   55,
	   55,   55,    1,   53,    1,   57,   56,   56,   56,    1,   59,   58,
	   58,   58,    1,   59,   60,   60,   60,    1,   59,   61,   61,   61,
	    1,   59,    1,   62,   63,   64,   66,   65,   67,   67,    1,   68,
	   70,   69,   69,   69,    1,   71,   72,   73,   74,    1,   75,    1,
	   76,   77,   78,   79,    1,   80,    1,   81,   82,   83,   84,    1,
	   80,   85,    1,   80,   86,    1,   80,   87,   85,   86,    1,   80,
	   86,    1,   75,   88,    1,   75,   89,    1,   75,   90,   88,   89,
	    1,   75,   89,    1,   70,   91,   91,   91,    1,   70,   92,   92,
	   92,    1,   70,    1,   94,   93,   93,   93,    1,   68,   70,   95,
	   69,   69,    1,   68,   70,   96,   91,   91,    1,   68,   70,   92,
	   92,   92,    1,   68,   97,   70,   95,   98,   69,   69,    1,   68,
	   70,   96,   91,   91,   91,    1,   68,   70,   91,   91,   91,    1,
	   68,   70,   98,   69,   69,    1,   70,   69,   69,   69,    1,   93,
	   93,   93,    1,   99,  100,  101,  102,  103,  103,    1,  104,  105,
	  106,  107,  108,  108,    1,  109,  110,  111,  112,  113,  113,    1,
	  114,  115,  116,  117,  118,  118,    1,    7,    9,  119,    8,    8,
	    1,    7,    9,  120,   30,   30,    1,    7,    9,   31,   31,   31,
	    1,    7,  121,    9,  119,  122,    8,    8,    1,    7,    9,  120,
	   30,   30,   30,    1,    7,    9,   30,   30,   30,    1,    7,    9,
	  122,    8,    8,    1,  123,    1,  124,  125,  126,  127,  128,  128,
	    1,    9,    8,    8,    8,    1,    1,  129,    1,  130,    1,  131,
	  129,  130,    1,  130,    1,    1,  132,    1,  133,    1,  134,  132,
	  133,    1,  133,    1,  135,  135,  135,    1,  136,  136,  136,    1,
	  137,  137,  137,    1,    1,    1,  138,  138,  138,    1,  139,  140,
	  141,  142,  143,  143,    1,   68,  145,  144,  144,  144,    1,  145,
	  146,  146,  146,    1,  145,  147,  147,  147,    1,  145,    1,   68,
	  145,  148,  144,  144,    1,   68,  145,  149,  146,  146,    1,   68,
	  145,  147,  147,  147,    1,   68,  150,  145,  148,  151,  144,  144,
	    1,   68,  145,  149,  146,  146,  146,    1,   68,  145,  146,  146,
	  146,    1,   68,  145,  151,  144,  144,    1,  145,  144,  144,  144,
	    1,  152,  153,  154,  155,  156,  156,    1,   68,  158,  157,  157,
	  157,    1,  158,  159,  159,  159,    1,  158,  160,  160,  160,    1,
	  158,    1,   68,  158,  161,  157,  157,    1,   68,  158,  162,  159,
	  159,    1,   68,  158,  160,  160,  160,    1,   68,  163,  158,  161,
	  164,  157,  157,    1,   68,  158,  162,  159,  159,  159,    1,   68,
	  158,  159,  159,  159,    1,   68,  158,  164,  157,  157,    1,  158,
	  157,  157,  157,    1,  165,  166,  167,  168,  169,  169,    1,   68,
	  171,  170,  170,  170,    1,  171,  172,  172,  172,    1,  171,  173,
	  173,  173,    1,  171,    1,   68,  171,  174,  170,  170,    1,   68,
	  171,  175,  172,  172,    1,   68,  171,  173,  173,  173,    1,   68,
	  176,  171,  174,  177,  170,  170,    1,   68,  171,  175,  172,  172,
	  172,    1,   68,  171,  172,  172,  172,    1,   68,  171,  177,  170,
	  170,    1,  171,  170,  170,  170,    1,  178,  179,  180,  181,  182,
	  182,    1,   68,  184,  183,  183,  183,    1,  184,  185,  185,  185,
	    1,  184,  186,  186,  186,    1,  184,    1,   68,  184,  187,  183,
	  183,    1,   68,  184,  188,  185,  185,    1,   68,  184,  186,  186,
	  186,    1,   68,  189,  184,  187,  190,  183,  183,    1,   68,  184,
	  188,  185,  185,  185,    1,   68,  184,  185,  185,  185,    1,   68,
	  184,  190,  183,  183,    1,  184,  183,  183,  183,    1,  191,  192,
	  193,  194,  195,  195,    1,   68,  197,  196,  196,  196,    1,  197,
	  198,  198,  198,    1,  197,  199,  199,  199,    1,  197,    1,   68,
	  197,  200,  196,  196,    1,   68,  197,  201,  198,  198,    1,   68,
	  197,  199,  199,  199,    1,   68,  202,  197,  200,  203,  196,  196,
	    1,   68,  197,  201,  198,  198,  198,    1,   68,  197,  198,  198,
	  198,    1,   68,  197,  203,  196,  196,    1,  197,  196,  196,  196,
	    1,  204,  205,  206,  207,  208,  208,    1,   68,  210,  209,  209,
	  209,    1,  210,  211,  211,  211,    1,  210,  212,  212,  212,    1,
	  210,    1,   68,  210,  213,  209,  209,    1,   68,  210,  214,  211,
	  211,    1,   68,  210,  212,  212,  212,    1,   68,  215,  210,  213,
	  216,  209,  209,    1,   68,  210,  214,  211,  211,  211,    1,   68,
	  210,  211,  211,  211,    1,   68,  210,  216,  209,  209,    1,  210,
	  209,  209,  209,    1,    0
	};
}

private static final short _ipaddr_indicies[] = init__ipaddr_indicies_0();


private static short[] init__ipaddr_trans_targs_0()
{
	return new short [] {
	    2,    0,   76,   79,   82,   83,   85,    3,   16,   19,    4,   12,
	   14,   13,    5,    6,    8,   10,    9,    7,   86,   87,   89,   88,
	    9,    6,   11,   13,    4,   15,   17,   18,   20,  154,   21,   24,
	   22,   23,   25,  141,   26,   29,   27,   28,   30,  128,   31,   34,
	   32,   33,   35,  115,   36,   39,   37,   38,   40,  102,   41,   44,
	   42,   43,   45,   63,   66,   69,  101,   70,   46,   59,   62,   47,
	   55,   57,   56,   48,   49,   51,   53,   52,   50,   91,   92,   94,
	   93,   52,   49,   54,   56,   47,   58,   60,   61,   96,  100,   64,
	   65,   67,   68,  103,  107,  110,  113,  114,  116,  120,  123,  126,
	  127,  129,  133,  136,  139,  140,  142,  146,  149,  152,  153,   77,
	   78,   80,   81,  167,  155,  159,  162,  165,  166,   88,   86,   90,
	   93,   91,   95,   97,   98,   99,   96,  103,  107,  110,  113,  114,
	  104,   71,  105,  106,  108,  109,  111,  112,  116,  120,  123,  126,
	  127,  117,   72,  118,  119,  121,  122,  124,  125,  129,  133,  136,
	  139,  140,  130,   73,  131,  132,  134,  135,  137,  138,  142,  146,
	  149,  152,  153,  143,   74,  144,  145,  147,  148,  150,  151,  155,
	  159,  162,  165,  166,  156,   75,  157,  158,  160,  161,  163,  164,
	  168,  172,  175,  178,  179,  169,   84,  170,  171,  173,  174,  176,
	  177
	};
}

private static final short _ipaddr_trans_targs[] = init__ipaddr_trans_targs_0();


private static byte[] init__ipaddr_trans_actions_0()
{
	return new byte [] {
	   37,    0,   37,   37,   37,    0,   21,    3,    5,    7,    9,    9,
	    9,    9,    3,    9,    9,    9,    9,    3,    9,    9,    9,    9,
	    1,    1,    1,    1,    1,    1,    5,    5,   21,    0,    5,    7,
	    5,    5,   21,    0,    5,    7,    5,    5,   21,    0,    5,    7,
	    5,    5,   21,    0,    5,    7,    5,    5,   21,    0,    5,    7,
	    5,    5,   42,   42,   42,   42,    0,   21,    3,    5,    7,    9,
	    9,    9,    9,    3,    9,    9,    9,    9,    3,    9,    9,    9,
	    9,    1,    1,    1,    1,    1,    1,    5,    5,   21,    0,   24,
	   24,   24,   24,   42,   42,   42,   42,   21,   42,   42,   42,   42,
	   21,   42,   42,   42,   42,   21,   42,   42,   42,   42,   21,   12,
	   12,   12,   12,    0,   42,   42,   42,   42,   21,    1,    1,    1,
	    1,    1,    1,    5,    5,    5,   33,   47,   47,   47,   47,   33,
	    5,    7,    5,    5,   24,   24,   24,   24,   47,   47,   47,   47,
	   33,    5,    7,    5,    5,   24,   24,   24,   24,   47,   47,   47,
	   47,   33,    5,    7,    5,    5,   24,   24,   24,   24,   47,   47,
	   47,   47,   33,    5,    7,    5,    5,   24,   24,   24,   24,   47,
	   47,   47,   47,   33,    5,    7,    5,    5,   24,   24,   24,   24,
	   47,   47,   47,   47,   33,    5,    7,    5,    5,   24,   24,   24,
	   24
	};
}

private static final byte _ipaddr_trans_actions[] = init__ipaddr_trans_actions_0();


private static byte[] init__ipaddr_eof_actions_0()
{
	return new byte [] {
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,   15,   15,   15,   15,   15,   18,   18,   18,   18,   18,
	   27,   27,   27,   27,   30,   30,   30,   27,   27,   27,   27,   27,
	   27,   27,   27,   27,   27,   27,   27,   30,   27,   27,   27,   27,
	   27,   27,   27,   27,   27,   27,   27,   27,   30,   27,   27,   27,
	   27,   27,   27,   27,   27,   27,   27,   27,   27,   30,   27,   27,
	   27,   27,   27,   27,   27,   27,   27,   27,   27,   27,   30,   27,
	   27,   27,   27,   27,   27,   27,   27,   27,   27,   27,   27,   30,
	   27,   27,   27,   27,   27,   27,   27,   27,   27,   27,   27,   27
	};
}

private static final byte _ipaddr_eof_actions[] = init__ipaddr_eof_actions_0();


static final int ipaddr_start = 1;
static final int ipaddr_first_final = 86;
static final int ipaddr_error = 0;

static final int ipaddr_en_main = 1;


// line 20 "src/java/inet/data/ip/IPParser.java.rl"

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

    
// line 89 "src/java/inet/data/ip/IPParser.java.rl"


    
// line 432 "src/java/inet/data/ip/IPParser.java"
	{
	cs = ipaddr_start;
	}

// line 92 "src/java/inet/data/ip/IPParser.java.rl"
    
// line 439 "src/java/inet/data/ip/IPParser.java"
	{
	int _klen;
	int _trans = 0;
	int _acts;
	int _nacts;
	int _keys;
	int _goto_targ = 0;

	_goto: while (true) {
	switch ( _goto_targ ) {
	case 0:
	if ( p == pe ) {
		_goto_targ = 4;
		continue _goto;
	}
	if ( cs == 0 ) {
		_goto_targ = 5;
		continue _goto;
	}
case 1:
	_match: do {
	_keys = _ipaddr_key_offsets[cs];
	_trans = _ipaddr_index_offsets[cs];
	_klen = _ipaddr_single_lengths[cs];
	if ( _klen > 0 ) {
		int _lower = _keys;
		int _mid;
		int _upper = _keys + _klen - 1;
		while (true) {
			if ( _upper < _lower )
				break;

			_mid = _lower + ((_upper-_lower) >> 1);
			if ( ( addr.charAt(p)) < _ipaddr_trans_keys[_mid] )
				_upper = _mid - 1;
			else if ( ( addr.charAt(p)) > _ipaddr_trans_keys[_mid] )
				_lower = _mid + 1;
			else {
				_trans += (_mid - _keys);
				break _match;
			}
		}
		_keys += _klen;
		_trans += _klen;
	}

	_klen = _ipaddr_range_lengths[cs];
	if ( _klen > 0 ) {
		int _lower = _keys;
		int _mid;
		int _upper = _keys + (_klen<<1) - 2;
		while (true) {
			if ( _upper < _lower )
				break;

			_mid = _lower + (((_upper-_lower) >> 1) & ~1);
			if ( ( addr.charAt(p)) < _ipaddr_trans_keys[_mid] )
				_upper = _mid - 2;
			else if ( ( addr.charAt(p)) > _ipaddr_trans_keys[_mid+1] )
				_lower = _mid + 2;
			else {
				_trans += ((_mid - _keys)>>1);
				break _match;
			}
		}
		_trans += _klen;
	}
	} while (false);

	_trans = _ipaddr_indicies[_trans];
	cs = _ipaddr_trans_targs[_trans];

	if ( _ipaddr_trans_actions[_trans] != 0 ) {
		_acts = _ipaddr_trans_actions[_trans];
		_nacts = (int) _ipaddr_actions[_acts++];
		while ( _nacts-- > 0 )
	{
			switch ( _ipaddr_actions[_acts++] )
			{
	case 0:
// line 36 "src/java/inet/data/ip/IPParser.java.rl"
	{ octet = 0; }
	break;
	case 1:
// line 37 "src/java/inet/data/ip/IPParser.java.rl"
	{ octet = (octet * 10) + (((int) ( addr.charAt(p))) - 0x30); }
	break;
	case 2:
// line 38 "src/java/inet/data/ip/IPParser.java.rl"
	{
            if (buffer != null) {
                buffer[nbytes++] = (byte) (octet & 0xff);
            }
        }
	break;
	case 3:
// line 44 "src/java/inet/data/ip/IPParser.java.rl"
	{ h16 = 0; }
	break;
	case 4:
// line 45 "src/java/inet/data/ip/IPParser.java.rl"
	{
            h16 = (h16 * 16);
            int d = (int) ( addr.charAt(p));
            if      (d <= 0x39) h16 += d - 0x30;
            else if (d <= 0x46) h16 += d - 0x41 + 10;
            else                h16 += d - 0x61 + 10;
        }
	break;
	case 5:
// line 52 "src/java/inet/data/ip/IPParser.java.rl"
	{
            if (buffer != null) {
                buffer[nbytes++] = (byte) ((h16 >>> 8) & 0xff);
                buffer[nbytes++] = (byte) (h16 & 0xff);
            }
        }
	break;
	case 6:
// line 58 "src/java/inet/data/ip/IPParser.java.rl"
	{ elision = nbytes; }
	break;
// line 562 "src/java/inet/data/ip/IPParser.java"
			}
		}
	}

case 2:
	if ( cs == 0 ) {
		_goto_targ = 5;
		continue _goto;
	}
	if ( ++p != pe ) {
		_goto_targ = 1;
		continue _goto;
	}
case 4:
	if ( p == eof )
	{
	int __acts = _ipaddr_eof_actions[cs];
	int __nacts = (int) _ipaddr_actions[__acts++];
	while ( __nacts-- > 0 ) {
		switch ( _ipaddr_actions[__acts++] ) {
	case 2:
// line 38 "src/java/inet/data/ip/IPParser.java.rl"
	{
            if (buffer != null) {
                buffer[nbytes++] = (byte) (octet & 0xff);
            }
        }
	break;
	case 5:
// line 52 "src/java/inet/data/ip/IPParser.java.rl"
	{
            if (buffer != null) {
                buffer[nbytes++] = (byte) ((h16 >>> 8) & 0xff);
                buffer[nbytes++] = (byte) (h16 & 0xff);
            }
        }
	break;
	case 6:
// line 58 "src/java/inet/data/ip/IPParser.java.rl"
	{ elision = nbytes; }
	break;
	case 7:
// line 60 "src/java/inet/data/ip/IPParser.java.rl"
	{ version = VERSION4; }
	break;
	case 8:
// line 61 "src/java/inet/data/ip/IPParser.java.rl"
	{ version = VERSION6; }
	break;
// line 612 "src/java/inet/data/ip/IPParser.java"
		}
	}
	}

case 5:
	}
	break; }
	}

// line 93 "src/java/inet/data/ip/IPParser.java.rl"

    if (cs < ipaddr_first_final)
        return INVALID;

    if (elision >= 0) {
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