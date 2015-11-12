package com.yilos.nailstar.aboutme.presenter;

import com.yilos.nailstar.aboutme.model.LoginServiceImpl;
import com.yilos.nailstar.aboutme.view.IRegisterView;
import com.yilos.nailstar.framework.entity.CommonResult;
import com.yilos.nailstar.framework.entity.NailStarApplicationContext;
import com.yilos.nailstar.framework.exception.CommonException;
import com.yilos.nailstar.util.TaskManager;

/**
 * Created by yangdan on 15/11/11.
 */
public class RegisterPresenter {
    private static RegisterPresenter instance = new RegisterPresenter();

    private IRegisterView view;

    private LoginServiceImpl loginService = LoginServiceImpl.getInstance();

    private RegisterPresenter(){}

    public static RegisterPresenter getInstance(IRegisterView view) {
        instance.view = view;
        return instance;
    }

    public void getValidateCode(){
        view.setValidateCodeButtonEnable(false);
        final String phoneNumber = view.getPhoneNumber();
        if(null == phoneNumber || phoneNumber.trim().equals("")) {
            view.showMessageDialog(null, "请输入正确的手机号码");
            view.setValidateCodeButtonEnable(true);
            return;
        }

        TaskManager.BackgroundTask<CommonResult> backgroundTask = new TaskManager.BackgroundTask<CommonResult>() {
            @Override
            public Object doWork(CommonResult data) {
                CommonResult commonResult = new CommonResult();
                try {
                    if(!NailStarApplicationContext.getInstance().isNetworkConnected()) {
                        commonResult.setError(true);
                        commonResult.setErrorMsg("网络不给力哦，请检查网络设置！");
                        return commonResult;
                    }
                    loginService.sendValidateCode(phoneNumber);
                    return commonResult;
                } catch (CommonException e) {
                    //e.printStackTrace();
                    commonResult.setError(true);
                    commonResult.setErrorMsg(e.getMessage());
                    return commonResult;
                }
            }
        };

        TaskManager.UITask<CommonResult> updateUI = new TaskManager.UITask<CommonResult>() {
            @Override
            public Object doWork(CommonResult data) {
                if(data.isError()) {
                    view.setValidateCodeButtonEnable(true);
                    view.showMessageDialog(null, data.getErrorMsg());
                } else {
                    view.timeValidateCodeButton(60);
                }
                return null;
            }
        };

        new TaskManager()
                .next(backgroundTask)
                .next(updateUI)
                .start();
    }

    public void register() {
        view.setRegisterButtonEnable(false);
        final String phoneNumber = view.getPhoneNumber();
        final String password = view.getPassword();
        final String validateCode = view.getValidateCode();

        if(phoneNumber == null || phoneNumber.trim().equals("")) {
            view.showMessageDialog(null, "请输入正确的手机号码");
            view.setRegisterButtonEnable(true);
            return;
        }
        if(password == null || password.trim().equals("") || password.trim().length() < 6 || password.trim().length() > 20) {
            view.showMessageDialog(null, "请设置6-20位的密码");
            view.setRegisterButtonEnable(true);
            return;
        }
        if(validateCode == null || validateCode.trim().equals("")) {
            view.showMessageDialog(null, "请输入短信验证码");
            view.setRegisterButtonEnable(true);
            return;
        }

        view.showLoading("正在注册...");

        TaskManager.BackgroundTask checkCode = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                CommonResult commonResult = new CommonResult();
                try {
                    if(loginService.checkValidateCode(phoneNumber, validateCode)) {
                        return commonResult;
                    } else {
                        commonResult.setError(true);
                        commonResult.setErrorMsg("验证码错误");
                        return commonResult;
                    }
                } catch (CommonException e) {
                    commonResult.setError(true);
                    commonResult.setErrorMsg(e.getMessage());
                    return commonResult;
                }
            }
        };

        TaskManager.BackgroundTask<CommonResult> register = new TaskManager.BackgroundTask<CommonResult>() {
            @Override
            public Object doWork(CommonResult data) {
                if(data.isError()) {
                    return data;
                }

                CommonResult commonResult = new CommonResult();
                try {
                    loginService.register(phoneNumber, password);
                    return commonResult;
                } catch (CommonException e) {
                    commonResult.setError(true);
                    commonResult.setErrorMsg(e.getMessage());
                    return commonResult;
                }
            }
        };

        TaskManager.UITask<CommonResult> updateUI = new TaskManager.UITask<CommonResult>() {
            @Override
            public Object doWork(CommonResult data) {
                view.setRegisterButtonEnable(true);
                view.hideLoading();
                if(data.isError()) {
                    view.showMessageDialog(null, data.getErrorMsg());
                    return null;
                } else {
                    view.showToast("注册成功，请使用账号登录");
                    view.close();
                    return null;
                }
            }
        };

        new TaskManager()
                .next(checkCode)
                .next(register)
                .next(updateUI)
                .start();
    }

    public void resetPassword() {
        view.setResetPasswordButtonEnable(false);
        final String phoneNumber = view.getPhoneNumber();
        final String password = view.getPassword();
        final String validateCode = view.getValidateCode();

        if(phoneNumber == null || phoneNumber.trim().equals("")) {
            view.showMessageDialog(null, "请输入正确的手机号码");
            view.setResetPasswordButtonEnable(true);
            return;
        }
        if(password == null || password.trim().equals("") || password.trim().length() < 6 || password.trim().length() > 20) {
            view.showMessageDialog(null, "请设置6-20位的密码");
            view.setResetPasswordButtonEnable(true);
            return;
        }
        if(validateCode == null || validateCode.trim().equals("")) {
            view.showMessageDialog(null, "请输入短信验证码");
            view.setResetPasswordButtonEnable(true);
            return;
        }

        view.showLoading("正在重置密码...");

        TaskManager.BackgroundTask checkCode = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                CommonResult commonResult = new CommonResult();
                try {
                    if(loginService.checkValidateCode(phoneNumber, validateCode)) {
                        return commonResult;
                    } else {
                        commonResult.setError(true);
                        commonResult.setErrorMsg("验证码错误");
                        return commonResult;
                    }
                } catch (CommonException e) {
                    commonResult.setError(true);
                    commonResult.setErrorMsg(e.getMessage());
                    return commonResult;
                }
            }
        };

        TaskManager.BackgroundTask<CommonResult> register = new TaskManager.BackgroundTask<CommonResult>() {
            @Override
            public Object doWork(CommonResult data) {
                if(data.isError()) {
                    return data;
                }

                CommonResult commonResult = new CommonResult();
                try {
                    loginService.resetPassword(phoneNumber, password, validateCode);
                    return commonResult;
                } catch (CommonException e) {
                    commonResult.setError(true);
                    commonResult.setErrorMsg(e.getMessage());
                    return commonResult;
                }
            }
        };

        TaskManager.UITask<CommonResult> updateUI = new TaskManager.UITask<CommonResult>() {
            @Override
            public Object doWork(CommonResult data) {
                view.setResetPasswordButtonEnable(true);
                view.hideLoading();
                if(data.isError()) {
                    view.showMessageDialog(null, data.getErrorMsg());
                    return null;
                } else {
                    view.showToast("密码重置成功，请使用新密码登录");
                    view.close();
                    return null;
                }
            }
        };

        new TaskManager()
                .next(checkCode)
                .next(register)
                .next(updateUI)
                .start();
    }
}
