package com.zachpuls.website.url;

import org.ocpsoft.rewrite.annotation.RewriteConfiguration;
import org.ocpsoft.rewrite.config.Configuration;
import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.config.Direction;
import org.ocpsoft.rewrite.servlet.config.Forward;
import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
import org.ocpsoft.rewrite.servlet.config.Path;

import javax.servlet.ServletContext;

/**
 * Created by zpuls on 3/15/2016.
 */

@RewriteConfiguration
public class ConfigurationProvider extends HttpConfigurationProvider {

    @Override
    public Configuration getConfiguration(final ServletContext servletContext) {
        return ConfigurationBuilder.begin()
                .addRule().when(Direction.isInbound().and(Path.matches("/")))
                .perform(Forward.to("/f/index.xhtml"))
                .addRule().when(Direction.isInbound().and(Path.matches("/about")))
                .perform(Forward.to("/f/index.xhtml"))
                .addRule().when(Direction.isInbound().and(Path.matches("/homelab")))
                .perform(Forward.to("/f/index.xhtml"))
                .addRule().when(Direction.isInbound().and(Path.matches("/contact")))
                .perform(Forward.to("/f/index.xhtml"))
                .addRule().when(Direction.isInbound().and(Path.matches("/blog")))
                .perform(Forward.to("/f/index.xhtml"));
    }

    @Override
    public int priority() {
        return 10;
    }
}
