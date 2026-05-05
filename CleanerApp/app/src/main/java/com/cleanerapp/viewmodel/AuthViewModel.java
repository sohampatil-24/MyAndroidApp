package com.cleanerapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cleanerapp.data.db.entities.UserEntity;
import com.cleanerapp.data.repository.UserRepository;
import com.cleanerapp.model.AuthResult;
import com.cleanerapp.utils.SessionManager;
import com.cleanerapp.utils.ValidationUtils;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AuthViewModel extends ViewModel {

    private final UserRepository userRepository;
    private final SessionManager sessionManager;

    private final MutableLiveData<AuthResult> authResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    @Inject
    public AuthViewModel(UserRepository userRepository, SessionManager sessionManager) {
        this.userRepository = userRepository;
        this.sessionManager = sessionManager;
    }

    public LiveData<AuthResult> getAuthResult() { return authResult; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }

    public void login(String email, String password) {
        String emailError = ValidationUtils.getEmailError(email);
        String passwordError = ValidationUtils.getPasswordError(password);

        if (emailError != null) {
            authResult.setValue(AuthResult.error(emailError));
            return;
        }
        if (passwordError != null) {
            authResult.setValue(AuthResult.error(passwordError));
            return;
        }

        isLoading.setValue(true);
        userRepository.login(email, password, result -> {
            isLoading.postValue(false);
            if (result.success && result.user != null) {
                sessionManager.saveUserSession(
                        result.user.id,
                        result.user.name,
                        result.user.email,
                        result.user.role
                );
            }
            authResult.postValue(result);
        });
    }

    public void register(String name, String email, String phone, String password, String role) {
        String nameError = ValidationUtils.getNameError(name);
        String emailError = ValidationUtils.getEmailError(email);
        String phoneError = ValidationUtils.getPhoneError(phone);
        String passwordError = ValidationUtils.getPasswordError(password);

        if (nameError != null) { authResult.setValue(AuthResult.error(nameError)); return; }
        if (emailError != null) { authResult.setValue(AuthResult.error(emailError)); return; }
        if (phoneError != null) { authResult.setValue(AuthResult.error(phoneError)); return; }
        if (passwordError != null) { authResult.setValue(AuthResult.error(passwordError)); return; }

        isLoading.setValue(true);
        UserEntity user = new UserEntity(name.trim(), email.trim(), password, phone.trim(), role);
        userRepository.register(user, result -> {
            isLoading.postValue(false);
            if (result.success && result.user != null) {
                sessionManager.saveUserSession(
                        result.user.id,
                        result.user.name,
                        result.user.email,
                        result.user.role
                );
            }
            authResult.postValue(result);
        });
    }

    public boolean isLoggedIn() {
        return sessionManager.isLoggedIn();
    }

    public String getUserRole() {
        return sessionManager.getUserRole();
    }
}
