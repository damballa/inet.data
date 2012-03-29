package inet.data.ip;

public class IPAddressException extends IPException {

public
IPAddressException(String string) {
  super( string );
}


public
IPAddressException(String string, Throwable throwable) {
    super(string, throwable);
}

public
IPAddressException( Throwable throwable ) {
    super( throwable );
}

}
