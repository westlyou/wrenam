/*
 * The contents of this file are subject to the terms of the Common Development and
 * Distribution License (the License). You may not use this file except in compliance with the
 * License.
 *
 * You can obtain a copy of the License at legal/CDDLv1.0.txt. See the License for the
 * specific language governing permission and limitations under the License.
 *
 * When distributing Covered Software, include this CDDL Header Notice in each file and include
 * the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
 * Header, with the fields enclosed by brackets [] replaced by your own identifying
 * information: "Portions copyright [year] [name of copyright owner]".
 *
 * Copyright 2013 ForgeRock Inc.
 */

package org.forgerock.openam.forgerockrest.authn;

import com.sun.identity.shared.debug.Debug;
import org.forgerock.openam.forgerockrest.authn.exceptions.RestAuthException;

import javax.inject.Inject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.ws.rs.core.Response;
import java.text.MessageFormat;

/**
 * Factory class for getting the appropriate RestAuthCallbackHandlers for the given Callbacks.
 */
public class RestAuthCallbackHandlerFactory {

    private static final Debug logger = Debug.getInstance("amIdentityServices");

    /**
     * Singleton approach by using a static inner class.
     */
    private static final class SingletonHolder {
        private static final RestAuthCallbackHandlerFactory INSTANCE = new RestAuthCallbackHandlerFactory();
    }

    /**
     * Private constructor to ensure RestAuthCallbackHandlerFactory remains a Singleton.
     */
    @Inject
    private RestAuthCallbackHandlerFactory() {
    }

    /**
     * Gets the RestAuthCallbackHandlerFactory instance.
     *
     * @return The RestAuthCallbackHandlerFactory singleton instance.
     */
    public static RestAuthCallbackHandlerFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * Retrieves the appropriate RestAuthCallbackHandlers for the given Callback class.
     *
     * @param callbackClass The class of the Callback to get the RestAuthCallbackHandler for.
     * @param <T> A class which implements the Callback interface.
     * @return The RestAuthCallbackHandler for the given Callback class.
     */
    public <T extends Callback> RestAuthCallbackHandler getRestAuthCallbackHandler(Class<T> callbackClass) {

        if (NameCallback.class.isAssignableFrom(callbackClass)) {
            return new RestAuthNameCallbackHandler();
        } else if (PasswordCallback.class.isAssignableFrom(callbackClass)) {
            return new RestAuthPasswordCallbackHandler();
        }

        logger.error(MessageFormat.format("Unsupported Callback, {0}", callbackClass.getSimpleName()));
        throw new RestAuthException(Response.Status.INTERNAL_SERVER_ERROR,
                MessageFormat.format("Unsupported Callback, {0}", callbackClass.getSimpleName()));
    }
}
