package com.swings.ck.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.swings.ck.GameStart;
import com.swings.ck.MyGdxGame;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(640, 960);
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return GameStart.getInstance();
        }
}