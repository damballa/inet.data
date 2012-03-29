package inet.data.dns;

import inet.data.InetDataException;

public class DNSDomainException extends InetDataException {

public
DNSDomainException(String string) {
  super( string );
}


public
DNSDomainException(String string, Throwable throwable) {
    super(string, throwable);
}

public
DNSDomainException( Throwable throwable ) {
    super( throwable );
}

}
