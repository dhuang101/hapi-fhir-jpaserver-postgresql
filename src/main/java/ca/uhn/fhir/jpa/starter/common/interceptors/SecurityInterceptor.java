package ca.uhn.fhir.jpa.starter.common.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;

@Interceptor
public class SecurityInterceptor {

    /**
     * This interceptor implements HTTP Basic Auth, which specifies that
     * a username and password are provided in a header called authentication.
     */
    @Hook(Pointcut.SERVER_INCOMING_REQUEST_POST_PROCESSED)
    public boolean incomingRequestPostProcessed(
            RequestDetails theRequestDetails, HttpServletRequest theRequest, HttpServletResponse theResponse)
            throws AuthenticationException {
        String authHeader = theRequest.getHeader("authentication");

        // The format of the header must be:
        // authentication: username:password
        if (authHeader == null) {
            throw new AuthenticationException(Msg.code(642) + "Missing authentication header");
        }

        System.out.println(authHeader);

        // String base64 = authHeader.substring("Basic ".length());
        // String base64decoded = new String(Base64.decodeBase64(base64));
        String[] parts = authHeader.split(":");

        String username = parts[0];
        String password = parts[1];

        /*
         * Here we test for a hardcoded username & password. This is
         * not typically how you would implement this in a production
         * system of course..
         */
        if (!username.equals("someuser") || !password.equals("thepassword")) {
            throw new AuthenticationException(Msg.code(643) + "Invalid username or password");
        }

        // Return true to allow the request to proceed
        return true;
    }
}