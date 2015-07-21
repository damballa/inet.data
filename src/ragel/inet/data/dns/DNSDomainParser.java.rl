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

    int lablen = 0;

    int cs = 0;
    int p = 0;
    int pe = data.length;
    int eof = pe;

    %%{
        action beg_label { lablen = ((int) fc) & 0xff; }
        action in_label { (lablen-- > 0) }
        action not_in_label { (lablen <= 0) }

        lablen = (1..63 when not_in_label) $beg_label;
        content = ( any when in_label )+;
        terminal = '' when not_in_label;
        label = lablen content <: terminal;
        domain = label+;

        main := domain;

        alphtype byte;
    }%%

    %% write init;
    %% write exec;

    if (cs < domain_first_final || lablen > 0)
        return false;
    return true;
}


%% machine hostname;
%% write data;

public static boolean
isValidHostname(byte[] data, int length, boolean underscores) {
    if (length > MAX_DOMAIN_LENGTH)
        return false;

    int lablen = 0;
    boolean letter = false;

    int cs = 0;
    int p = 0;
    int pe = length;
    int eof = pe;

    %%{
        action beg_label { lablen = ((int) fc) & 0xff; }
        action in_label { (lablen-- > 0) }
        action not_in_label { (lablen <= 0) }
        action underscores { underscores }
        action letter { letter = true; }

        lablen = (1..63 when not_in_label) $beg_label;
        let = ( alpha when in_label ) $letter;
        dig = digit when in_label;
        let_dig = let | dig;
        hyp = ( "-" | "_" when underscores ) when in_label;
        let_dig_hyp = let_dig | hyp;
        ldh_str = let_dig_hyp+;
        content = let_dig (ldh_str? let_dig)?;
        terminal = '' when not_in_label;
        label = lablen content <: terminal;
        domain = label+;

        main := domain;

        alphtype byte;
    }%%

    %% write init;
    %% write exec;

    if (cs < hostname_first_final || lablen > 0 || !letter)
        return false;
    return true;
}

}
