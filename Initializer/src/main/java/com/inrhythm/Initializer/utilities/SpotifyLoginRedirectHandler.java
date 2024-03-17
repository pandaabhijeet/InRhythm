package com.inrhythm.Initializer.utilities;

import com.inrhythm.Initializer.constants.ApiPathConstants;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.RedirectView;

@Component
public class SpotifyLoginRedirectHandler {

    public RedirectView redirectToSpotifyLoginController(){
        return new RedirectView("http://localhost:8080/login");
    }
}
