package com.booking.featureToggle;

import org.togglz.core.Feature;
import org.togglz.core.annotation.EnabledByDefault;
import org.togglz.core.annotation.Label;
import org.togglz.core.context.FeatureContext;

public enum Features implements Feature {
    @EnabledByDefault
    @Label("User profile")
    USERPROFILE_FEATURE,

    @EnabledByDefault
    @Label("Schedule Movie")
    SECHEDULEMOVIE_FEATURE,

    @EnabledByDefault
    @Label("Book movie admin")
    BOOKMOVIEADMIN_FEATURE,

    @EnabledByDefault
    @Label("Book movie customer")
    BOOKMOVIECUSTOMER_FEATURE;

    public boolean isActive() {
        return FeatureContext.getFeatureManager().isActive(this);
    }

}
