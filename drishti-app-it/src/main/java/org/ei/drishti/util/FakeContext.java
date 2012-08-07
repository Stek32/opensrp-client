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
        });

        Session session = context.session().start(numberOfMillisecondsAfterNowThatThisSessionEnds);
        session.setPassword("password").setRepositoryName("drishti.db." + (new Date().getTime() - 1));

        return context;
    }

    public static Context setupService(final DrishtiService drishtiService, final FakeUserService userService, final int numberOfMillisecondsAfterNowThatThisSessionEnds) {
        Context context = Context.setInstance(new Context() {
            @Override
            protected DrishtiService drishtiService() {
                return drishtiService;
            }

            @Override
            public UserService userService() {
                return userService;
            }
        });

        Session properties = context.session().start(numberOfMillisecondsAfterNowThatThisSessionEnds);
        properties.setPassword("password").setRepositoryName("drishti.db." + (new Date().getTime() - 1));
        userService.setSession(context.session());

        return context;
    }
}
