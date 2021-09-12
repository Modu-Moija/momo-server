package com.momo.server.integration.setup;

import com.momo.server.dto.auth.SessionUser;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigInteger;

public class SessionSetup {

    public static MockHttpSession sessionSetUp() throws Exception {
        BigInteger userId = new BigInteger("2051448780");

        //세션 셋업
        SessionUser sessionUser = new SessionUser();
        sessionUser.setMeetId("88183217cfe7de3");
        sessionUser.setUserId(userId);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("sessionuser", sessionUser);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setSession(session);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        return session;
    }
}
