package ca.uvic.ecg.token;

import ca.uvic.ecg.model.Nurse;
import ca.uvic.ecg.model.resultJson;
import com.alibaba.fastjson.JSONObject;
import ca.uvic.ecg.repository.ClinicRepository;
import ca.uvic.ecg.repository.NurseRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    @Autowired
    private NurseRepository userRepository;

    @Autowired
    private ClinicRepository clinicRepo;

    private String username = "";
    private Gson gson = new Gson();


    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;

        setFilterProcessesUrl(SecurityConstants.AUTH_LOGIN_URL);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {


        String password = "";
        String clinic;
        resultJson result = new resultJson("");

        String[] urlParmeter = request.getRequestURI().split("/");
        clinic = urlParmeter[2];

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String str = null;
            StringBuilder sb = new StringBuilder();
            while ((str = reader.readLine()) != null) {
                sb.append(str);
            }
            JSONObject json = JSONObject.parseObject(sb.toString());
            String param = json.toJSONString();
            username = json.getString("username");
            password = json.getString("password");
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
            Calendar nowTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            if (userRepository == null) {
                ServletContext servletContext = request.getServletContext();
                WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
                userRepository = webApplicationContext.getBean(NurseRepository.class);
            }
            Nurse user = userRepository.findByNurseEmailAndDeletedFalse(username);
            if(user.getNextRequest()!=null){
                if(user.getNumberOfFailures()%3 == 0 && user.getNumberOfFailures() != 0 && user.getNextRequest().after(nowTime)){
                    result.setMessage("Login failed because of trying too many times, please wait");
                    String responseStr = this.gson.toJson(result);

                    PrintWriter out = response.getWriter();
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.setStatus(400);
                    out.print(responseStr);
                    out.flush();
                    return null;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            response.setStatus(400);
            //result.setMessage("Invalid Username or password, Please check again");

        }
        var authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authentication) throws IOException {

        resultJson result = new resultJson("");
        String[] urlParmeter = request.getRequestURI().split("/");
        String clinic = urlParmeter[2];

        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Calendar nowTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        var user = ((User) authentication.getPrincipal());
        if (userRepository == null) {
            ServletContext servletContext = request.getServletContext();
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            userRepository = webApplicationContext.getBean(NurseRepository.class);
        }
        Nurse theUser = userRepository.findByNurseEmailAndDeletedFalse(user.getUsername());
        if (clinicRepo == null) {
            ServletContext servletContext = request.getServletContext();
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            clinicRepo = webApplicationContext.getBean(ClinicRepository.class);
        }
        if (clinicRepo.findByClinicNameAndDeletedFalse(clinic) == null || userRepository.findByNurseEmailAndDeletedFalse(user.getUsername()).getClinicId()
                != clinicRepo.findByClinicNameAndDeletedFalse(clinic).getClinicId()) {
            result.setMessage("Login failed because of this user is not belong to your clinic");
            String responseStr = this.gson.toJson(result);

            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(401);
            out.print(responseStr);
            out.flush();
        } else {
            if(theUser.getNumberOfFailures()>0){
                theUser.setNumberOfFailures(0);
                theUser.setNextRequest(null);
                userRepository.save(theUser);
            }
            var roles = user.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            var signingKey = SecurityConstants.JWT_SECRET.getBytes();

            var token = Jwts.builder()
                    .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS256)
                    .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
                    .setIssuer(SecurityConstants.TOKEN_ISSUER)
                    .setAudience(SecurityConstants.TOKEN_AUDIENCE)
                    .setSubject(((User) user).getUsername())
                    .setExpiration(new Date(System.currentTimeMillis() + 28800000))
                    .claim("rol", roles)
                    .compact();
            //Done Todo: return nurse id
            String nurseId = userRepository.findByNurseEmailAndDeletedFalse(user.getUsername()).getNurseId().toString();
            result.setMessage(nurseId);
            String responseStr = this.gson.toJson(result);

            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            //Add the token to the header of response
            response.addHeader(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + token);
            //write the Json result to the body of response
            out.print(responseStr);
            out.flush();
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {

        try{
            resultJson result = new resultJson("");
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
            Calendar nowTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            if (userRepository == null) {
                ServletContext servletContext = request.getServletContext();
                WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
                userRepository = webApplicationContext.getBean(NurseRepository.class);
            }
            Nurse user = userRepository.findByNurseEmailAndDeletedFalse(username);
            user.setNumberOfFailures(user.getNumberOfFailures()+1);
            if(user.getNumberOfFailures()%3 == 0 && user.getNumberOfFailures()!=0){
                nowTime.add(Calendar.MINUTE,30);
                user.setNextRequest(nowTime);
            }

            userRepository.save(user);
            response.setStatus(401);
            result.setMessage("Username and password is not match, please check again");
            String responseStr = this.gson.toJson(result);

            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(400);
            //Add the token to the header of response
            //write the Json result to the body of response
            out.print(responseStr);
            out.flush();

        }catch (Exception e){
            e.printStackTrace();
            response.setStatus(400);
        }

    }
}
