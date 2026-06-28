package CPSF.com.demo.exception;

import CPSF.com.demo.model.constant.Operation;

public class UnsupportedSqlOperationException extends ClientSideException {

    public UnsupportedSqlOperationException() {
        super("Unsupported operation was used. The supported Operations are: " + Operation.values());
    }

}
