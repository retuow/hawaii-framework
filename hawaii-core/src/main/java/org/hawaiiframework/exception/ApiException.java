/*
 * Copyright 2015-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hawaiiframework.exception;

/**
 * Exception with a predefined error code.
 *
 * @author Paul Klos
 * @since 2.0.0
 */
public class ApiException extends HawaiiException {

    /**
     * Unique serial id, generated by IntelliJ.
     */
    private static final long serialVersionUID = 9146345197810622183L;

    /**
     * ApiError associated with this exception.
     */
    private final ApiError apiError;

    /**
     * Construct an API error.
     *
     * @param apiError the error code.
     */
    protected ApiException(final ApiError apiError) {
        this(apiError, null, null);
    }

    /**
     * Construct an API error with a custome message.
     *
     * @param apiError the error code.
     */
    protected ApiException(final ApiError apiError, final String message) {
        this(apiError, null, message);
    }

    /**
     * Construct an API error from a Throwable.
     *
     * @param apiError the error code
     * @param orig the cause of the error
     */
    protected ApiException(final ApiError apiError, final Throwable orig) {
        this(apiError, orig, null);
    }

    /**
     * Construct an API error from a Throwable with a custom message.
     *
     * @param apiError the error code
     * @param orig the cause of the error
     */
    protected ApiException(final ApiError apiError, final Throwable orig, final String message) {
        super(message, orig);
        this.apiError = apiError;
    }

    /**
     * Getter for the ApiError.
     *
     * @return the ApiError
     */
    public ApiError getApiError() {
        return apiError;
    }

    /**
     * Convenience method to retrieve the error code from the contained API error.
     *
     * @return the error code
     */
    public String getErrorCode() {
        return apiError == null ? null : apiError.getErrorCode();
    }

    /**
     * Convenience method to retrieve the reason from the contained API error.
     *
     * @return the reason
     */
    public String getReason() {
        return apiError == null ? null : apiError.getReason();
    }

}
