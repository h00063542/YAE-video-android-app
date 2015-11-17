package com.yilos.nailstar.aboutme.presenter;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.entity.PersonInfo;
import com.yilos.nailstar.aboutme.model.LoginAPI;
import com.yilos.nailstar.aboutme.model.LoginServiceImpl;
import com.yilos.nailstar.aboutme.view.ILoginView;
import com.yilos.nailstar.framework.entity.CommonResult;
import com.yilos.nailstar.framework.exception.CommonException;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.util.TaskManager;

/**
 * Created by yangdan on 15/11/10.
 */
public class LoginPresenter {
    private static LoginPresenter instance = new LoginPresenter();

    private ILoginView loginView;

    private LoginServiceImpl loginService = LoginServiceImpl.getInstance();

    private LoginPresenter(){}

    public static LoginPresenter getInstance(ILoginView loginView){
        instance.loginView = loginView;
        return instance;
    }

    /**
     * 账号登录
     */
    public void login(){
        loginView.setLoginButtonEnable(false);
        final String userName = loginView.getUserAccount();
        final String password = loginView.getPassword();

        if(userName == null || userName.trim().equals("")) {
            loginView.showMessageDialog(null, loginView.getResourceStringById(R.string.enter_account).toString());
            loginView.setLoginButtonEnable(true);
            return;
        }
        if(password == null || password.trim().equals("")) {
            loginView.showMessageDialog(null, loginView.getResourceStringById(R.string.enter_password).toString());
            loginView.setLoginButtonEnable(true);
            return;
        }

        loginView.showLoading("正在登录...");

        TaskManager.BackgroundTask<CommonResult> loginTask = new TaskManager.BackgroundTask<CommonResult>() {
            @Override
            public Object doWork(CommonResult data) {
                CommonResult commonResult = new CommonResult();
                try {
                    String uid = loginService.login(userName, password);
                    commonResult.setResult(uid);
                } catch (CommonException e) {
                    commonResult.setError(true);
                    commonResult.setErrorMsg(e.getMessage());
                } catch (NetworkDisconnectException e) {
                    commonResult.setError(true);
                    commonResult.setErrorMsg(e.getMessage());
                }

                return commonResult;
            }
        };

        TaskManager.BackgroundTask<CommonResult<String>> getPersonInfoTask = new TaskManager.BackgroundTask<CommonResult<String>>() {
            @Override
            public Object doWork(CommonResult<String> data) {
                CommonResult<PersonInfo> commonResult = new CommonResult<>();
                if(data.isError()) {
                    commonResult.setError(true);
                    commonResult.setErrorMsg(data.getErrorMsg());
                    return commonResult;
                } else {
                    try {
                        PersonInfo personInfo = loginService.queryPersonInfo(data.getResult());
                        commonResult.setResult(personInfo);
                        return commonResult;
                    } catch (NetworkDisconnectException e) {
                        commonResult.setError(true);
                        commonResult.setErrorMsg(e.getMessage());
                    } catch (CommonException e) {
                        commonResult.setError(true);
                        commonResult.setErrorMsg(e.getMessage());
                    }

                    return commonResult;
                }
            }
        };

        TaskManager.UITask<CommonResult<PersonInfo>> updateUI = new TaskManager.UITask<CommonResult<PersonInfo>>() {
            @Override
            public Object doWork(CommonResult<PersonInfo> data) {
                loginView.setLoginButtonEnable(true);
                loginView.hideLoading();
                if(data.isError()) {
                    loginView.showMessageDialog("登录失败", data.getErrorMsg());
                } else {
                    //登录成功
                    LoginAPI.getInstance().saveLoginStatus(userName, data.getResult());
                    loginView.showShortToast("登录成功");
                    loginView.close();
                }

                return null;
            }
        };

        new TaskManager()
                .next(loginTask)
                .next(getPersonInfoTask)
                .next(updateUI)
                .start();
    }
}
