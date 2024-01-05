package by.clevertec.filter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class CodingFilter extends HttpFilter {

    private static final String UTF_8 = "UTF-8";

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        req.setCharacterEncoding(UTF_8);
        res.setCharacterEncoding(UTF_8);
        super.doFilter(req, res, chain);
    }
}

