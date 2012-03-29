package inet.data.ip;

import inet.data.InetDataException;

public class IPException extends InetDataException {

public
IPException(String string) {
  super( string );
}


public
IPException(String string, Throwable throwable) {
    super(string, throwable);
}

public
IPException( Throwable throwable ) {
    super( throwable );
}

}
