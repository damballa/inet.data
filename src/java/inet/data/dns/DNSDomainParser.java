
// line 1 "src/java/inet/data/dns/DNSDomainParser.java.rl"
package inet.data.dns;

public class DNSDomainParser {

public static final int MAX_DOMAIN_LENGTH = 255;
public static final int MAX_LABEL_LENGTH = 63;


// line 9 "src/java/inet/data/dns/DNSDomainParser.java.rl"

// line 14 "src/java/inet/data/dns/DNSDomainParser.java"
private static byte[] init__domain_actions_0()
{
	return new byte [] {
	    0,    1,    0
	};
}

private static final byte _domain_actions[] = init__domain_actions_0();


private static byte[] init__domain_cond_offsets_0()
{
	return new byte [] {
	    0,    0,    0,    1
	};
}

private static final byte _domain_cond_offsets[] = init__domain_cond_offsets_0();


private static byte[] init__domain_cond_lengths_0()
{
	return new byte [] {
	    0,    0,    1,    1
	};
}

private static final byte _domain_cond_lengths[] = init__domain_cond_lengths_0();


private static short[] init__domain_cond_keys_0()
{
	return new short [] {
	 -128,  127, -128,  127,    0
	};
}

private static final short _domain_cond_keys[] = init__domain_cond_keys_0();


private static byte[] init__domain_cond_spaces_0()
{
	return new byte [] {
	    0,    0,    0
	};
}

private static final byte _domain_cond_spaces[] = init__domain_cond_spaces_0();


private static byte[] init__domain_key_offsets_0()
{
	return new byte [] {
	    0,    0,    2,    4
	};
}

private static final byte _domain_key_offsets[] = init__domain_key_offsets_0();


private static short[] init__domain_trans_keys_0()
{
	return new short [] {
	    1,   63,  384,  639,  257,  319,  384,  639,    0
	};
}

private static final short _domain_trans_keys[] = init__domain_trans_keys_0();


private static byte[] init__domain_single_lengths_0()
{
	return new byte [] {
	    0,    0,    0,    0
	};
}

private static final byte _domain_single_lengths[] = init__domain_single_lengths_0();


private static byte[] init__domain_range_lengths_0()
{
	return new byte [] {
	    0,    1,    1,    2
	};
}

private static final byte _domain_range_lengths[] = init__domain_range_lengths_0();


private static byte[] init__domain_index_offsets_0()
{
	return new byte [] {
	    0,    0,    2,    4
	};
}

private static final byte _domain_index_offsets[] = init__domain_index_offsets_0();


private static byte[] init__domain_indicies_0()
{
	return new byte [] {
	    0,    1,    2,    1,    0,    2,    1,    0
	};
}

private static final byte _domain_indicies[] = init__domain_indicies_0();


private static byte[] init__domain_trans_targs_0()
{
	return new byte [] {
	    2,    0,    3
	};
}

private static final byte _domain_trans_targs[] = init__domain_trans_targs_0();


private static byte[] init__domain_trans_actions_0()
{
	return new byte [] {
	    1,    0,    0
	};
}

private static final byte _domain_trans_actions[] = init__domain_trans_actions_0();


static final int domain_start = 1;
static final int domain_first_final = 3;
static final int domain_error = 0;

static final int domain_en_main = 1;


// line 10 "src/java/inet/data/dns/DNSDomainParser.java.rl"

public static boolean
isValid(byte[] data) {
    if (data.length > MAX_DOMAIN_LENGTH)
        return false;

    int length = 0;

    int cs = 0;
    int p = 0;
    int pe = data.length;
    int eof = pe;

    
// line 37 "src/java/inet/data/dns/DNSDomainParser.java.rl"


    
// line 171 "src/java/inet/data/dns/DNSDomainParser.java"
	{
	cs = domain_start;
	}

// line 40 "src/java/inet/data/dns/DNSDomainParser.java.rl"
    
// line 178 "src/java/inet/data/dns/DNSDomainParser.java"
	{
	int _klen;
	int _trans = 0;
	int _widec;
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
	_widec = data[p];
	_keys = _domain_cond_offsets[cs]*2
;	_klen = _domain_cond_lengths[cs];
	if ( _klen > 0 ) {
		int _lower = _keys
;		int _mid;
		int _upper = _keys + (_klen<<1) - 2;
		while (true) {
			if ( _upper < _lower )
				break;

			_mid = _lower + (((_upper-_lower) >> 1) & ~1);
			if ( _widec < _domain_cond_keys[_mid] )
				_upper = _mid - 2;
			else if ( _widec > _domain_cond_keys[_mid+1] )
				_lower = _mid + 2;
			else {
				switch ( _domain_cond_spaces[_domain_cond_offsets[cs] + ((_mid - _keys)>>1)] ) {
	case 0: {
		_widec = 128 + (data[p] - -128);
		if ( 
// line 25 "src/java/inet/data/dns/DNSDomainParser.java.rl"
 (length-- > 0)  ) _widec += 256;
		break;
	}
				}
				break;
			}
		}
	}

	_match: do {
	_keys = _domain_key_offsets[cs];
	_trans = _domain_index_offsets[cs];
	_klen = _domain_single_lengths[cs];
	if ( _klen > 0 ) {
		int _lower = _keys;
		int _mid;
		int _upper = _keys + _klen - 1;
		while (true) {
			if ( _upper < _lower )
				break;

			_mid = _lower + ((_upper-_lower) >> 1);
			if ( _widec < _domain_trans_keys[_mid] )
				_upper = _mid - 1;
			else if ( _widec > _domain_trans_keys[_mid] )
				_lower = _mid + 1;
			else {
				_trans += (_mid - _keys);
				break _match;
			}
		}
		_keys += _klen;
		_trans += _klen;
	}

	_klen = _domain_range_lengths[cs];
	if ( _klen > 0 ) {
		int _lower = _keys;
		int _mid;
		int _upper = _keys + (_klen<<1) - 2;
		while (true) {
			if ( _upper < _lower )
				break;

			_mid = _lower + (((_upper-_lower) >> 1) & ~1);
			if ( _widec < _domain_trans_keys[_mid] )
				_upper = _mid - 2;
			else if ( _widec > _domain_trans_keys[_mid+1] )
				_lower = _mid + 2;
			else {
				_trans += ((_mid - _keys)>>1);
				break _match;
			}
		}
		_trans += _klen;
	}
	} while (false);

	_trans = _domain_indicies[_trans];
	cs = _domain_trans_targs[_trans];

	if ( _domain_trans_actions[_trans] != 0 ) {
		_acts = _domain_trans_actions[_trans];
		_nacts = (int) _domain_actions[_acts++];
		while ( _nacts-- > 0 )
	{
			switch ( _domain_actions[_acts++] )
			{
	case 0:
// line 24 "src/java/inet/data/dns/DNSDomainParser.java.rl"
	{ length = ((int) data[p]) & 0xff; }
	break;
// line 294 "src/java/inet/data/dns/DNSDomainParser.java"
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
case 5:
	}
	break; }
	}

// line 41 "src/java/inet/data/dns/DNSDomainParser.java.rl"

    if (cs < domain_first_final || length > 0)
        return false;
    return true;
}



// line 49 "src/java/inet/data/dns/DNSDomainParser.java.rl"

// line 325 "src/java/inet/data/dns/DNSDomainParser.java"
private static byte[] init__hostname_actions_0()
{
	return new byte [] {
	    0,    1,    0
	};
}

private static final byte _hostname_actions[] = init__hostname_actions_0();


private static byte[] init__hostname_cond_offsets_0()
{
	return new byte [] {
	    0,    0,    0,    3,    7
	};
}

private static final byte _hostname_cond_offsets[] = init__hostname_cond_offsets_0();


private static byte[] init__hostname_cond_lengths_0()
{
	return new byte [] {
	    0,    0,    3,    4,    4
	};
}

private static final byte _hostname_cond_lengths[] = init__hostname_cond_lengths_0();


private static short[] init__hostname_cond_keys_0()
{
	return new short [] {
	   48,   57,   65,   90,   97,  122,   45,   45,   48,   57,   65,   90,
	   97,  122,   45,   45,   48,   57,   65,   90,   97,  122,    0
	};
}

private static final short _hostname_cond_keys[] = init__hostname_cond_keys_0();


private static byte[] init__hostname_cond_spaces_0()
{
	return new byte [] {
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0
	};
}

private static final byte _hostname_cond_spaces[] = init__hostname_cond_spaces_0();


private static byte[] init__hostname_key_offsets_0()
{
	return new byte [] {
	    0,    0,    2,    8,   15
	};
}

private static final byte _hostname_key_offsets[] = init__hostname_key_offsets_0();


private static short[] init__hostname_trans_keys_0()
{
	return new short [] {
	    1,   63,  560,  569,  577,  602,  609,  634,  557,  560,  569,  577,
	  602,  609,  634,  301,  557,    1,   44,   46,   47,   58,   63,  304,
	  313,  560,  569,  577,  602,  609,  634,    0
	};
}

private static final short _hostname_trans_keys[] = init__hostname_trans_keys_0();


private static byte[] init__hostname_single_lengths_0()
{
	return new byte [] {
	    0,    0,    0,    1,    2
	};
}

private static final byte _hostname_single_lengths[] = init__hostname_single_lengths_0();


private static byte[] init__hostname_range_lengths_0()
{
	return new byte [] {
	    0,    1,    3,    3,    7
	};
}

private static final byte _hostname_range_lengths[] = init__hostname_range_lengths_0();


private static byte[] init__hostname_index_offsets_0()
{
	return new byte [] {
	    0,    0,    2,    6,   11
	};
}

private static final byte _hostname_index_offsets[] = init__hostname_index_offsets_0();


private static byte[] init__hostname_indicies_0()
{
	return new byte [] {
	    0,    1,    2,    2,    2,    1,    3,    2,    2,    2,    1,    0,
	    3,    0,    0,    0,    0,    2,    2,    2,    1,    0
	};
}

private static final byte _hostname_indicies[] = init__hostname_indicies_0();


private static byte[] init__hostname_trans_targs_0()
{
	return new byte [] {
	    2,    0,    4,    3
	};
}

private static final byte _hostname_trans_targs[] = init__hostname_trans_targs_0();


private static byte[] init__hostname_trans_actions_0()
{
	return new byte [] {
	    1,    0,    0,    0
	};
}

private static final byte _hostname_trans_actions[] = init__hostname_trans_actions_0();


static final int hostname_start = 1;
static final int hostname_first_final = 4;
static final int hostname_error = 0;

static final int hostname_en_main = 1;


// line 50 "src/java/inet/data/dns/DNSDomainParser.java.rl"

public static boolean
isValidHostname(byte[] data) {
    if (data.length > MAX_DOMAIN_LENGTH)
        return false;

    int length = 0;

    int cs = 0;
    int p = 0;
    int pe = data.length;
    int eof = pe;

    
// line 81 "src/java/inet/data/dns/DNSDomainParser.java.rl"


    
// line 486 "src/java/inet/data/dns/DNSDomainParser.java"
	{
	cs = hostname_start;
	}

// line 84 "src/java/inet/data/dns/DNSDomainParser.java.rl"
    
// line 493 "src/java/inet/data/dns/DNSDomainParser.java"
	{
	int _klen;
	int _trans = 0;
	int _widec;
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
	_widec = data[p];
	_keys = _hostname_cond_offsets[cs]*2
;	_klen = _hostname_cond_lengths[cs];
	if ( _klen > 0 ) {
		int _lower = _keys
;		int _mid;
		int _upper = _keys + (_klen<<1) - 2;
		while (true) {
			if ( _upper < _lower )
				break;

			_mid = _lower + (((_upper-_lower) >> 1) & ~1);
			if ( _widec < _hostname_cond_keys[_mid] )
				_upper = _mid - 2;
			else if ( _widec > _hostname_cond_keys[_mid+1] )
				_lower = _mid + 2;
			else {
				switch ( _hostname_cond_spaces[_hostname_cond_offsets[cs] + ((_mid - _keys)>>1)] ) {
	case 0: {
		_widec = 128 + (data[p] - -128);
		if ( 
// line 65 "src/java/inet/data/dns/DNSDomainParser.java.rl"
 (length-- > 0)  ) _widec += 256;
		break;
	}
				}
				break;
			}
		}
	}

	_match: do {
	_keys = _hostname_key_offsets[cs];
	_trans = _hostname_index_offsets[cs];
	_klen = _hostname_single_lengths[cs];
	if ( _klen > 0 ) {
		int _lower = _keys;
		int _mid;
		int _upper = _keys + _klen - 1;
		while (true) {
			if ( _upper < _lower )
				break;

			_mid = _lower + ((_upper-_lower) >> 1);
			if ( _widec < _hostname_trans_keys[_mid] )
				_upper = _mid - 1;
			else if ( _widec > _hostname_trans_keys[_mid] )
				_lower = _mid + 1;
			else {
				_trans += (_mid - _keys);
				break _match;
			}
		}
		_keys += _klen;
		_trans += _klen;
	}

	_klen = _hostname_range_lengths[cs];
	if ( _klen > 0 ) {
		int _lower = _keys;
		int _mid;
		int _upper = _keys + (_klen<<1) - 2;
		while (true) {
			if ( _upper < _lower )
				break;

			_mid = _lower + (((_upper-_lower) >> 1) & ~1);
			if ( _widec < _hostname_trans_keys[_mid] )
				_upper = _mid - 2;
			else if ( _widec > _hostname_trans_keys[_mid+1] )
				_lower = _mid + 2;
			else {
				_trans += ((_mid - _keys)>>1);
				break _match;
			}
		}
		_trans += _klen;
	}
	} while (false);

	_trans = _hostname_indicies[_trans];
	cs = _hostname_trans_targs[_trans];

	if ( _hostname_trans_actions[_trans] != 0 ) {
		_acts = _hostname_trans_actions[_trans];
		_nacts = (int) _hostname_actions[_acts++];
		while ( _nacts-- > 0 )
	{
			switch ( _hostname_actions[_acts++] )
			{
	case 0:
// line 64 "src/java/inet/data/dns/DNSDomainParser.java.rl"
	{ length = ((int) data[p]) & 0xff; }
	break;
// line 609 "src/java/inet/data/dns/DNSDomainParser.java"
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
case 5:
	}
	break; }
	}

// line 85 "src/java/inet/data/dns/DNSDomainParser.java.rl"

    if (cs < domain_first_final || length > 0)
        return false;
    return true;
}

}
