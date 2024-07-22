package com.sovcom.authCode

import com.sovcom.domain.common.model.ApiError
import com.sovcom.domain.common.model.CommonBackendFailure

val failure: Throwable = CommonBackendFailure(
    ApiError(
        extraMessage = "CommonBackendFailure extraMessage",
        code = 0,
        message = "CommonBackendFailure message",
        violations = emptyList(),
        key = "CommonBackendFailure key",
        email = "CommonBackendFailure email"
    )
)