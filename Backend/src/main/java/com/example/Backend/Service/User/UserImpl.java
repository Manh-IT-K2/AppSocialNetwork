package com.example.Backend.Service.User;

import com.example.Backend.Entity.model.User;
import com.example.Backend.Request.User.RequestCreateAccount;
import com.example.Backend.Request.User.RequestLogin;
import com.example.Backend.Response.ApiResponse.ApiResponse;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;

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
        user.setFromGoogle(false);
        mongoTemplate.insert(user,"users");
    }

    @Override
    public ApiResponse<User> loginAccount(RequestLogin requestLogin) throws Exception {
        Query query = new Query(Criteria.where("email").is(requestLogin.getEmail()));
        User user = mongoTemplate.findOne(query, User.class, "users");
        //System.out.println(new Gson().toJson(requestCreateLogin));
        if(requestLogin.isFromGoogle()){
            if(user == null){
                User savedUser = new User();
                savedUser.setEmail(requestLogin.getEmail());
                savedUser.setUsername(requestLogin.getUsername());
                savedUser.setAvatarImg(requestLogin.getAvatarImg());
                savedUser.setFromGoogle(true);
                return new ApiResponse<User>(true, "", mongoTemplate.insert(savedUser, "users"));
            }else{
                if(user.isFromGoogle()) return new ApiResponse<User>(true, "", user);
                else return new ApiResponse<User>(false, "Email này đã được đăng ký, vui lòng đăng nhập bằng phương thức khác", null);
            }
        }else{
            if(user == null) return new ApiResponse<User>(false, "Không tìm thấy email này", null);

            boolean matches = BCrypt.checkpw(requestLogin.getPassword(),user.getPassword());
            if(matches) {
                return new ApiResponse<User>(true, "", user);
            }
            //System.out.println(new Gson().toJson(user));
            return new ApiResponse<User>(false, "Sai mật khẩu", null);
        }
    }

    @Override
    public ApiResponse<String> sendOtp(String email) {
        Query query = new Query(Criteria.where("email").is(email));
        User user = mongoTemplate.findOne(query, User.class, "users");

        if(user != null){
            return new ApiResponse<String>(false, "Email đã được sử dụng", "");
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
        return new ApiResponse<String>(true, "Mã OTP đã được gửi đến email của bạn" , otp.toString());
    }
    @Override
    public ApiResponse<String> sendOtp_forgotpassword(String email) {
        Query query = new Query(Criteria.where("email").is(email));
        User user = mongoTemplate.findOne(query, User.class, "users");

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
        return new ApiResponse<String>(true, "Mã OTP đã được gửi đến email của bạn" , otp.toString());
    }

    @Override
    public ApiResponse<List<User>> getAllUsers() {
        List<User> userList = mongoTemplate.findAll(User.class, "users");
        return new ApiResponse<List<User>>(true, "Lấy tất cả dữ liệu user thành công!", userList);
    }
}
