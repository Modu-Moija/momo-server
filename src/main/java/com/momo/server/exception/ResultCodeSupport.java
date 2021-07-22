package com.momo.server.exception;

import java.util.Optional;

public interface ResultCodeSupport {

    default Optional<ResultCodeSupport> getResultCode() {
	return Optional.empty();
    }

}
