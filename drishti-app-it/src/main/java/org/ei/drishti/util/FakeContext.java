package org.ei.drishti.util;

import org.ei.drishti.Context;
import org.ei.drishti.service.DrishtiService;
import org.ei.drishti.service.UserService;

import java.util.Date;

public class FakeContext {
    public static Context setupService(final DrishtiService drishtiService, final long numberOfMillisecondsAfterNowThatThisSessionEnds) {
        Context context = Context.setInstance(new Context() {
            @Override
            protected DrishtiService drishtiService() {
                return drishtiService;
            }

            @Override
            protected String repositoryName() {
                return "drishti.db." + (new Date().getTime() - 1);
            }
        });
        context.startSession(numberOfMillisecondsAfterNowThatThisSessionEnds).setPassword("password");
        return context;
    }

    public static Context setupService(final DrishtiService drishtiService, final UserService loginService, final int numberOfMillisecondsAfterNowThatThisSessionEnds) {
        Context context = Context.setInstance(new Context() {
            @Override
            protected DrishtiService drishtiService() {
                return drishtiService;
            }

            @Override
            public UserService userService() {
                return loginService;
            }

            @Override
            protected String repositoryName() {
                return "drishti.db." + (new Date().getTime() - 1);
            }
        });
        context.startSession(numberOfMillisecondsAfterNowThatThisSessionEnds).setPassword("password");
        return context;
    }
}
