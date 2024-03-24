package com.example.frontend.repository;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.frontend.request.User.RequestChangePW;
import com.example.frontend.request.User.RequestChangePass;
import com.example.frontend.request.User.RequestCreateAccount;
import com.example.frontend.request.User.RequestLogin;
import com.example.frontend.request.User.RequestUpdateUser;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.service.UserService;
import com.example.frontend.utils.CallApi;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    UserService userService;

    public UserRepository() {
        userService = CallApi.getRetrofitInstance().create(UserService.class);
    }

    public void registerUser(RequestCreateAccount request) {
        MutableLiveData<ApiResponse<String>> mutableLiveData = new MutableLiveData<>();

        userService.registerUser(request).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<String> apiResponse = response.body();
                    mutableLiveData.setValue(apiResponse);
                } else {
                    // Xử lý khi phản hồi không thành công
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                // Xử lý khi gọi API thất bại
            }
        });
    }

    public MutableLiveData<ApiResponse<UserResponse>> login(RequestLogin request) {
        MutableLiveData<ApiResponse<UserResponse>> mutableLiveData = new MutableLiveData<>();

        userService.login(request).enqueue(new Callback<ApiResponse<UserResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserResponse>> call, Response<ApiResponse<UserResponse>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<UserResponse> apiResponse = response.body();
                    mutableLiveData.setValue(apiResponse);
                    Log.d("log1", new Gson().toJson(apiResponse));
                } else {
                    // Xử lý khi phản hồi không thành công
                    Log.d("log1", "Lỗi");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserResponse>> call, Throwable t) {
                // Xử lý khi gọi API thất bại
                Log.d("log1", new Gson().toJson(t));
            }
        });
        return mutableLiveData;
    }

    public MutableLiveData<ApiResponse<String>> sendOTP(String email) {
        MutableLiveData<ApiResponse<String>> mutableLiveData = new MutableLiveData<>();

        userService.sendOTP(email).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<String> apiResponse = response.body();
                    mutableLiveData.setValue(apiResponse);
                    Log.d("sendOTP1", mutableLiveData.getValue().getData());
                } else {
                    // Xử lý khi phản hồi không thành công
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                // Xử lý khi gọi API thất bại
            }
        });

        return mutableLiveData;
    }
    public MutableLiveData<ApiResponse<String>> sendOTP_forgotpassword(String email) {
        MutableLiveData<ApiResponse<String>> mutableLiveData = new MutableLiveData<>();
        Log.d("email:",email);
        userService.sendOTP_forgotpassword(email).enqueue(new Callback<ApiResponse<String>>() {

            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful()) {
                    Log.d("email:",email);
                    ApiResponse<String> apiResponse = response.body();
                    mutableLiveData.setValue(apiResponse);
                    Log.d("sendOTP1", mutableLiveData.getValue().getData());
                } else {
                    // Xử lý khi phản hồi không thành công
                    Log.d("Lỗi","");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                // Xử lý khi gọi API thất bại
                Gson gson = new Gson();
                String json = gson.toJson(t);
                Log.d("Lỗi",json);
            }
        });

        return mutableLiveData;
    }

    public MutableLiveData<ApiResponse<List<UserResponse>>> getAllUsers() {
        MutableLiveData<ApiResponse<List<UserResponse>>> mutableLiveData = new MutableLiveData<>();

        userService.getAllUsers().enqueue(new Callback<ApiResponse<List<UserResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<UserResponse>>> call, Response<ApiResponse<List<UserResponse>>> response) {
                if (response.isSuccessful()) {
                    try {
                        ApiResponse<List<UserResponse>> listApiResponse = response.body();
                        mutableLiveData.setValue(listApiResponse);
                        Log.d("allUsers", mutableLiveData.getValue().getData().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<UserResponse>>> call, Throwable t) {
                Log.i(TAG, "Unable to get data");
            }
        });

        return mutableLiveData;
    }
    public MutableLiveData<ApiResponse<UserResponse>> changePass(RequestChangePass request) {
        MutableLiveData<ApiResponse<UserResponse>> mutableLiveData = new MutableLiveData<>();

        if (request == null) {
            // Xử lý khi request là null
            Log.e("changePass", "Request is null");
            return mutableLiveData;
        }

        Log.d("log1", request.getUsername());

        userService.changePass(request).enqueue(new Callback<ApiResponse<UserResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserResponse>> call, Response<ApiResponse<UserResponse>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<UserResponse> apiResponse = response.body();
                    mutableLiveData.setValue(apiResponse);
                } else {
                    // Xử lý khi phản hồi không thành công
                //    Log.d("changePass", "Request failed: " + response.code());
                    // Gửi ApiResponse với trạng thái lỗi và thông báo lỗi
                    mutableLiveData.setValue(new ApiResponse<UserResponse>(false, "Request failed:" , null));
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<UserResponse>> call, Throwable t) {
                // Xử lý khi gọi API thất bại
             //   Log.e("changePass", "Request failed", t);
                // Gửi ApiResponse với trạng thái lỗi và thông báo lỗi
                mutableLiveData.setValue(new ApiResponse<UserResponse>(false, "Request failed:" , null));
            }
        });
        return mutableLiveData;
    }
    public MutableLiveData<ApiResponse<UserResponse>> changePW(RequestChangePW request) {
        MutableLiveData<ApiResponse<UserResponse>> mutableLiveData = new MutableLiveData<>();

        if (request == null) {
            // Xử lý khi request là null
            Log.e("changePW", "Request is null");
            return mutableLiveData;
        }

        Log.d("log1", request.getEmail());

        userService.changePW(request).enqueue(new Callback<ApiResponse<UserResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserResponse>> call, Response<ApiResponse<UserResponse>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<UserResponse> apiResponse = response.body();
                    mutableLiveData.setValue(apiResponse);
                } else {
                    // Xử lý khi phản hồi không thành công
                    Log.d("changePW", "Request failed: " + response.code());
                    // Gửi ApiResponse với trạng thái lỗi và thông báo lỗi
                    mutableLiveData.setValue(new ApiResponse<UserResponse>(false, "Request failed:" , null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserResponse>> call, Throwable t) {
                // Xử lý khi gọi API thất bại
                Log.e("changePW", "Request failed", t);
                // Gửi ApiResponse với trạng thái lỗi và thông báo lỗi
                mutableLiveData.setValue(new ApiResponse<UserResponse>(false, "Request failed:" , null));
            }
        });

        return mutableLiveData;
    }

    public MutableLiveData<ApiResponse<List<UserResponse>>> getRequestTrackingUser() {
        MutableLiveData<ApiResponse<List<UserResponse>>> mutableLiveData = new MutableLiveData<>();

        userService.getRequestTrackingUser().enqueue(new Callback<ApiResponse<List<UserResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<UserResponse>>> call, Response<ApiResponse<List<UserResponse>>> response) {
                if(response.isSuccessful()) {
                    ApiResponse<List<UserResponse>> listApiResponse = response.body();
                    mutableLiveData.setValue(listApiResponse);
                    Log.d("Request Tracking",mutableLiveData.getValue().getData().toString());
                } else {
                    // Xử lý khi phản hồi không thành công
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<UserResponse>>> call, Throwable t) {
                // Xử lý khi gọi API thất bại
            }
        });

        return mutableLiveData;
    }

    // HANDLE GET DETAIL USER BY ID
    public MutableLiveData<ApiResponse<UserResponse>> getDetailUserById(String id) {
        MutableLiveData<ApiResponse<UserResponse>> mutableLiveData = new MutableLiveData<>();

        userService.getDetailUserById(id).enqueue(new Callback<ApiResponse<UserResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserResponse>> call, Response<ApiResponse<UserResponse>> response) {
                if(response.isSuccessful()) {
                    ApiResponse<UserResponse> user = response.body();
                    mutableLiveData.setValue(user);
                    Log.d("Detail User:",mutableLiveData.getValue().getData().toString());
                } else {
                    // Xử lý khi phản hồi không thành công
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserResponse>> call, Throwable t) {
                // Xử lý khi gọi API thất bại
            }
        });

        return mutableLiveData;
    }

    // HANDLE UPDATE USER
    public MutableLiveData<ApiResponse<UserResponse>> updateUser(RequestUpdateUser requestUpdateUser) {
        MutableLiveData<ApiResponse<UserResponse>> mutableLiveData = new MutableLiveData<>();

        userService.updateUser(requestUpdateUser).enqueue(new Callback<ApiResponse<UserResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserResponse>> call, Response<ApiResponse<UserResponse>> response) {
                if(response.isSuccessful()) {
                    ApiResponse<UserResponse> user = response.body();
                    mutableLiveData.setValue(user);
                } else {
                    // Xử lý khi phản hồi không thành công
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserResponse>> call, Throwable t) {
                // Xử lý khi gọi API thất bại
            }
        });

        return mutableLiveData;
    }
}
