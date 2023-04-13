package com.malte3d.suturo.sme.domain.model.application.exception;

import com.malte3d.suturo.commons.messages.Messages;
import lombok.NonNull;

public class SmeException extends RuntimeException {

    private SmeException(String message) {
        super(message);
    }

    private SmeException(String message, Throwable cause) {
        super(message, cause);
    }

    public static SmeException of(@NonNull String messageKey) {
        return new SmeException(Messages.getString(messageKey));
    }

    public static SmeException of(@NonNull String messageKey, Object... args) {

        if (args.length > 0 && args[args.length - 1] instanceof Throwable)
            return new SmeException(Messages.getString(messageKey, args), (Throwable) args[args.length - 1]);
        else
            return new SmeException(Messages.getString(messageKey, args));
    }

}
