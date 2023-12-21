package by.clevertec.filter;

import by.clevertec.dto.CarDto;
import com.google.gson.Gson;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebFilter("/cars/*")
public class CheckNullFilter extends HttpFilter {
    private final Gson gson = new Gson();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        if (req.getMethod().equalsIgnoreCase("POST") || req.getMethod().equalsIgnoreCase("PUT")) {

            req.setCharacterEncoding("UTF-8");

            BufferedReader reader = req.getReader();
            StringBuilder result = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            CarDto carDto = gson.fromJson(result.toString(), CarDto.class);

            if (carDto == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "CarDto cannot be null");
                return;
            }
            req.setAttribute("carDto", carDto);
        }
        chain.doFilter(request, response);
    }
}
