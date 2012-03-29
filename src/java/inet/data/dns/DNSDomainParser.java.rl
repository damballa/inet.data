package inet.data.dns;

public class DNSDomainParser {

public static final int MAX_DOMAIN_LENGTH = 256;
public static final int MAX_LABEL_LENGTH = 63;

%% machine domain;
%% write data;

public static boolean
isValid(byte[] data) {
    if (data.length > MAX_DOMAIN_LENGTH)
        return false;

    int length = 0;

    int cs = 0;
    int p = 0;
    int pe = data.length;
    int eof = pe;

    %%{
        action beg_label { length = ((int) fc) & 0xff; }
        action in_label { (length-- > 0) }

        length = 1..63 $beg_label;
        content = ( any when in_label )+;
        label = length content;
        domain = label+;

        main := domain;

        alphtype byte;
    }%%

    %% write init;
    %% write exec;

    if (cs < domain_first_final)
        return false;
    return true;
}


%% machine hostname;
%% write data;

public static boolean
isValidHostname(byte[] data) {
    if (data.length > MAX_DOMAIN_LENGTH)
        return false;

    int length = 0;

    int cs = 0;
    int p = 0;
    int pe = data.length;
    int eof = pe;

    %%{
        action beg_label { length = ((int) fc) & 0xff; }
        action in_label { (--length > 0) }

        length = 1..63 $beg_label;
        let_dig = alnum when in_label;
        hyp = "-" when in_label;
        let_dig_hyp = let_dig | hyp;
        ldh_str = let_dig_hyp+;
        content = ( let_dig (ldh_str? let_dig)? "." );
        label = length content;
        domain = label+;

        main := domain;

        alphtype byte;
    }%%

    %% write init;
    %% write exec;

    if (cs < domain_first_final)
        return false;
    return true;
}

}
