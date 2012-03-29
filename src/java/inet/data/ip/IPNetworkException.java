package inet.data.ip;

public class IPNetworkException extends IPException {

public
IPNetworkException(String string) {
  super( string );
}


public
IPNetworkException(String string, Throwable throwable) {
    super(string, throwable);
}

public
IPNetworkException( Throwable throwable ) {
    super( throwable );
}

}
