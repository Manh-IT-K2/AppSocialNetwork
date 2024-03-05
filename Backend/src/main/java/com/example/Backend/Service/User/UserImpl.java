package com.example.Backend.Service.User;

import com.example.Backend.Entity.model.User;
import com.example.Backend.Request.User.RequestCreateAccount;
import com.example.Backend.Request.User.RequestLogin;
import com.example.Backend.Response.ApiResponse.ApiResponse;
import com.google.gson.Gson;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
@Service
public class UserImpl implements UserService{
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    JavaMailSender javaMailSender;

    public UserImpl() {
    }


    @Override
    public void createAccount(RequestCreateAccount requestCreateAccount) throws Exception {
        User user = new User();
        user.setEmail(requestCreateAccount.getEmail());
        user.setUsername(requestCreateAccount.getUsername());
        user.setPassword(BCrypt.hashpw(requestCreateAccount.getPassword(), BCrypt.gensalt()));
        mongoTemplate.insert(user,"users");
    }

    @Override
    public ApiResponse<User> loginAccount(RequestLogin requestCreateLogin) throws Exception {
        Query query = new Query(Criteria.where("email").gt(requestCreateLogin.getEmail()));
        User user = mongoTemplate.findOne(query, User.class, "users");

        if(requestCreateLogin.isFromGoogle()){
            if(user == null){
                User savedUser = new User();
                savedUser.setEmail(requestCreateLogin.getEmail());
                savedUser.setUsername(requestCreateLogin.getUsername());
                savedUser.setAvatarImg(requestCreateLogin.getAvatarImg());
                return new ApiResponse<User>(true, "", mongoTemplate.insert(savedUser, "users"));
            }else{
                return new ApiResponse<User>(true, "", user);
            }
        }else{
            if(user == null) return new ApiResponse<User>(false, "Không tìm thấy email này", null);

            boolean matches = BCrypt.checkpw(requestCreateLogin.getPassword(),user.getPassword());
            if(matches) {
                return new ApiResponse<User>(true, "", user);
            }
            //System.out.println(new Gson().toJson(user));
            return new ApiResponse<User>(false, "Sai mặt khẩu", null);
        }
    }

    @Override
    public String sendOtp(String email) {
        Query query = new Query(Criteria.where("email").gt(email));
        User user = mongoTemplate.findOne(query, User.class, "users");
        if(user != null){
            return "Email đã được sử dụng";
        }
        String CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int OTP_LENGTH = 6;
        StringBuilder otp = new StringBuilder();

        SecureRandom random = new SecureRandom();
        for (int i = 0; i < OTP_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            otp.append(CHARACTERS.charAt(index));
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("OTP Verification");
        message.setText("Your OTP is: " + otp);
        javaMailSender.send(message);
        return otp.toString();
    }
}
