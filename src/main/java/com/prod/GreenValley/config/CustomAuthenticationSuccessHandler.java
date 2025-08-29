package com.prod.GreenValley.config;

import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.prod.GreenValley.Entities.User;
import com.prod.GreenValley.service.UserService;
import com.prod.GreenValley.util.SessionStorage;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        // Custom logic on successful authentication
        System.out.println(
                "---------------------------------***********************************----------------------------------------");
        System.out.println("request " + request);
        System.out.println("response " + response);
        System.out.println("authentication " + authentication.getAuthorities());

        HttpSession session = request.getSession();

        // Get the username directly from the authentication object
        String user_Name = authentication.getName();

        User user = userService.findByUserName(user_Name);

        SessionStorage st = new SessionStorage();
        st.setUserId(user.getId());
        // The authorities collection contains GrantedAuthority objects, not simple
        // strings.
        // We must check the authority string of each object.
        // We use a stream to check if any authority has the role "ROLE_ADMIN".
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            st.setRole("ADMIN");
        } else {
            st.setRole("STAFF");
        }
        session.setAttribute("sessionObj", st);

        // if(user.getRole().equals("ADMIN")){
        // response.sendRedirect("/admin");
        // }else{
        // response.sendRedirect("/service-provider");
        // }

        response.sendRedirect("/home");

    }
}