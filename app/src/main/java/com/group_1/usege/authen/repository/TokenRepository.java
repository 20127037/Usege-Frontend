package com.group_1.usege.authen.repository;


import com.group_1.usege.authen.model.CacheToken;
import com.group_1.usege.utilities.localStorage.SharedRefLocalStorage;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * <pre>
 * Store current user JWT token
 * Use {@link Inject} to inject the singleton instance
 * </pre>
 */
@Singleton
public class TokenRepository {
    private final String TOKEN_KEY = "TOKEN";
    @Inject
    public TokenRepository(SharedRefLocalStorage localStorage)
    {
        this.localStorage = localStorage;
    }
    private SharedRefLocalStorage localStorage;
    public CacheToken getToken() {
        return wrappedToken;
    }
    public void setToken(CacheToken wrappedToken) {
        this.wrappedToken = wrappedToken;
        if (wrappedToken == null)
            localStorage.delete(TOKEN_KEY);
        else
            localStorage.putValue(TOKEN_KEY, wrappedToken);
    }
    private CacheToken wrappedToken;
}
