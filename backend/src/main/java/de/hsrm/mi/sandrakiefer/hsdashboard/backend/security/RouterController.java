package de.hsrm.mi.sandrakiefer.hsdashboard.backend.security;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class RouterController {
    
    @RequestMapping(value = {" ", "/", "/dashboard", "/api/**"})
    public ModelAndView redirect() {
        return new ModelAndView("forward:/index.html");
    }

}
