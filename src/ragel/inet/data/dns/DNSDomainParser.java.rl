package inet.data.dns;

public class DNSDomainParser {

public static final int MAX_DOMAIN_LENGTH = 255;
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
        action not_in_label { (length <= 0) }

        length = 1..63 $beg_label;
        content = ( any when in_label )+;
        terminal = '' when not_in_label;
        label = length content <: terminal;
        domain = label+;

        main := domain;

        alphtype byte;
    }%%

    %% write init;
    %% write exec;

    if (cs < domain_first_final || length > 0)
        return false;
    return true;
}


%% machine hostname;
%% write data;

public static boolean
isValidHostname(byte[] data, boolean underscores) {
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
        action not_in_label { (length <= 0) }
        action underscores { underscores }

        length = 1..63 $beg_label;
        let_dig = alnum when in_label;
        hyp = ( "-" | "_" when underscores ) when in_label;
        let_dig_hyp = let_dig | hyp;
        ldh_str = let_dig_hyp+;
        content = let_dig (ldh_str? let_dig)?;
        terminal = '' when not_in_label;
        label = length content <: terminal;
        domain = label+;

        main := domain;

        alphtype byte;
    }%%

    %% write init;
    %% write exec;

    if (cs < domain_first_final || length > 0)
        return false;
    return true;
}

}
