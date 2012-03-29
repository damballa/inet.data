package inet.data;

public class InetDataException extends RuntimeException {

public
InetDataException(String string) {
  super( string );
}


public
InetDataException(String string, Throwable throwable) {
    super(string, throwable);
}

public
InetDataException(Throwable throwable) {
    super(throwable);
}

}
