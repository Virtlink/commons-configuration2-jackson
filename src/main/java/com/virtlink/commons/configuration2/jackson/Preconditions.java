/*
 * Copyright 2015-2022 - Daniel A. A. Pelsmaeker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.virtlink.commons.configuration2.jackson;

/**
 * Preconditions.
 */
/* package private */ class Preconditions {

    /**
     * Ensures the reference is not null.
     *
     * @param reference the reference to test
     * @return the reference
     * @throws NullPointerException if the reference is null
     */
    @SuppressWarnings("UnusedReturnValue")
    public static <T> T checkNotNull(final T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

}
