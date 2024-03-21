package com.example.Backend.Service.User;

import com.example.Backend.Entity.model.User;
import com.example.Backend.Request.User.RequestChangePasword;
import com.example.Backend.Request.User.RequestCreateAccount;
import com.example.Backend.Request.User.RequestForgetPass;
import com.example.Backend.Request.User.RequestLogin;
import com.example.Backend.Request.User.RequestTracking;
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
public class UserImpl implements UserService {
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
        mongoTemplate.insert(user, "users");
    }

    @Override
    public ApiResponse<User> loginAccount(RequestLogin requestLogin) throws Exception {
        Query query = new Query(Criteria.where("email").is(requestLogin.getEmail()));
        User user = mongoTemplate.findOne(query, User.class, "users");
        //System.out.println(new Gson().toJson(requestCreateLogin));
        if (requestLogin.isFromGoogle()) {
            if (user == null) {
                User savedUser = new User();
                savedUser.setEmail(requestLogin.getEmail());
                savedUser.setUsername(requestLogin.getUsername());
                savedUser.setAvatarImg(requestLogin.getAvatarImg());
                savedUser.setFromGoogle(true);
                savedUser.setStatus(true);
                return new ApiResponse<User>(true, "", mongoTemplate.insert(savedUser, "users"));
            } else {
                if (user.isFromGoogle()){
                    user.setStatus(true);
                    mongoTemplate.save(user);
                    return new ApiResponse<User>(true, "", user);
                }
                else
                    return new ApiResponse<User>(false, "Email này đã được đăng ký, vui lòng đăng nhập bằng phương thức khác", null);
            }
        } else {
            if (user == null) return new ApiResponse<User>(false, "Không tìm thấy email này", null);

            boolean matches = BCrypt.checkpw(requestLogin.getPassword(), user.getPassword());
            if (matches) {
                user.setStatus(true);
                mongoTemplate.save(user);
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

        if (user != null) {
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
        return new ApiResponse<String>(true, "Mã OTP đã được gửi đến email của bạn", otp.toString());
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
    public ApiResponse<User> changePW(RequestForgetPass requestForgetPass) {
        Query query = new Query(Criteria.where("email").is(requestForgetPass.getEmail()));
        User user = mongoTemplate.findOne(query, User.class, "users");
        if (user == null) {
            return new ApiResponse<User>(false, "Không tìm thấy ngườ dùng với Email này!",null);
        }
        user.setPassword(BCrypt.hashpw(requestForgetPass.getNewPass(),BCrypt.gensalt()));
        mongoTemplate.save(user,"users");
        return new ApiResponse<>(true, "Đổi mật khẩu thành công!",null);


    }
    @Override
    public ApiResponse<List<User>> getAllUsers() {
        List<User> userList = mongoTemplate.findAll(User.class, "users");
        return new ApiResponse<List<User>>(true, "Lấy tất cả dữ liệu user thành công!", userList);
    }

    @Override
    public ApiResponse<User> changePassword(RequestChangePasword requestChangePass) throws Exception {
        // Tạo query để tìm người dùng dựa trên email
        Query query = new Query(Criteria.where("username").is(requestChangePass.getUsername()));
        // Tìm người dùng trong cơ sở dữ liệu
        User user = mongoTemplate.findOne(query, User.class, "users");
        boolean matches = BCrypt.checkpw(requestChangePass.getCurrentpass(), user.getPassword());
        // Nếu không tìm thấy người dùng, trả về thông báo lỗi
        if (user == null) {
            return new ApiResponse<>(false, "Không tìm thấy người dùng với username này", null);
        }else if(!matches) {
            return new ApiResponse<>(false, "Current password incorrect. Please check and try again.", null);
        }else{
            user.setPassword(BCrypt.hashpw(requestChangePass.getNewpass(), BCrypt.gensalt()));
            // Lưu người dùng đã được cập nhật vào cơ sở dữ liệu
            mongoTemplate.save(user, "users");
            // Trả về thông báo thành cônlg
            return new ApiResponse<>(true, "Đổi mật khẩu thành công", user);
             }
        }

    @Override
    public ApiResponse<User> requestTrackingUser(RequestTracking requestTracking) {
        return null;
    }
}



