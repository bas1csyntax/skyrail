package net.md_5.bungee;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerConnectEvent;

/**
 * A request to connect a server
 */
@Getter
@Builder
public class ServerConnectRequest
{

    public interface Callback
    {

        void done(Result result, Throwable error);

        enum Result {

            /**
             * ServerConnectEvent to the new server was canceled.
             */
            EVENT_CANCEL,
            /**
             * Already connected to target server.
             */
            ALREADY_CONNECTED,
            /**
             * Already connecting to target server.
             */
            ALREADY_CONNECTING,
            /**
             * Successfully connected to server.
             */
            SUCCESS,
            /**
             * Connection failed, error can be accessed from callback method handle.
             */
            FAIL
        }
    }

    public static class LegacyCallback implements net.md_5.bungee.api.Callback<Boolean>, Callback
    {

        private final net.md_5.bungee.api.Callback<Boolean> callback;

        public LegacyCallback(net.md_5.bungee.api.Callback<Boolean> callback)
        {
            this.callback = callback;
        }

        @Override
        public final void done(Result result, Throwable error)
        {
            done( ( result == Result.SUCCESS ) ? Boolean.TRUE : Boolean.FALSE, error );
        }

        @Override
        public void done(Boolean result, Throwable error)
        {
            callback.done( result, error );
        }
    }

    /**
     * Target server to connect to.
     */
    @NonNull
    private final ServerInfo target;
    /**
     * Reason for connecting to server.
     */
    @NonNull
    private final ServerConnectEvent.Reason reason;
    /**
     * Callback sent back from request.
     */
    private final ServerConnectRequest.Callback callback;
    /**
     * Timeout in milliseconds for request.
     */
    private final int connectTimeout;
    /**
     * Should the player be attempted to connect to the next server
     * in their queue if the initial request fails.
     */
    private final boolean retry;

    public static class ServerConnectRequestBuilder
    {

        private ServerConnectRequest.Callback callback;
        private int connectTimeout = 5000; //TODO: Configurable

        // Backwards compat callback.
        public ServerConnectRequestBuilder callback(final net.md_5.bungee.api.Callback<Boolean> callback)
        {
            this.callback = new LegacyCallback( callback );
            return this;
        }
    }
}
