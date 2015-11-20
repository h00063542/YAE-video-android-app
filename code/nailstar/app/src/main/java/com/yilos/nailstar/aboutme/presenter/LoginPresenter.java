package com.yilos.nailstar.aboutme.presenter;

import android.app.Activity;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.entity.PersonInfo;
import com.yilos.nailstar.aboutme.model.LoginAPI;
import com.yilos.nailstar.aboutme.model.LoginServiceImpl;
import com.yilos.nailstar.aboutme.view.ILoginView;
import com.yilos.nailstar.framework.entity.CommonResult;
import com.yilos.nailstar.framework.exception.CommonException;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.social.model.SocialLoginAPI;
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
        loginDaka(userName, password, new LoginServiceSelector<String>() {
            @Override
            public String loginAndGetUid(String userName, String loginData) throws CommonException, NetworkDisconnectException {
                return loginService.login(userName, loginData);
            }

            @Override
            public void afterLogin() {
                loginView.setLoginButtonEnable(true);
            }

            @Override
            public LoginAPI.LoginFromType getLoginFromType() {
                return LoginAPI.LoginFromType.PHONE;
            }
        });
    }

    public void weixinLogin() {
        loginView.setWeixinLoginButtonEnable(false);
        loginView.showLoading("正在登录美甲大咖...");
        SocialLoginAPI.getInstance().weixinLogin((Activity) loginView, new SocialLoginAPI.SocialLoginResultListener<SocialLoginAPI.LoginData>() {
            @Override
            public void beginLoadData() {

            }

            @Override
            public void loginSuccess(final SocialLoginAPI.LoginData loginData) {
                loginDaka(null, loginData, new LoginServiceSelector<SocialLoginAPI.LoginData>() {
                    @Override
                    public String loginAndGetUid(String userName, SocialLoginAPI.LoginData loginData) throws CommonException, NetworkDisconnectException {
                        return loginService.weixinLogin(loginData.getUnionId(), loginData.getOpenId(), loginData.getNickName(), loginData.getAvatarUrl());
                    }

                    @Override
                    public void afterLogin() {
                        loginView.setWeixinLoginButtonEnable(true);
                    }

                    @Override
                    public LoginAPI.LoginFromType getLoginFromType() {
                        return LoginAPI.LoginFromType.WEI_XIN;
                    }
                });
            }

            @Override
            public void loginFail(String errMsg) {
                loginView.setWeixinLoginButtonEnable(true);
                loginView.showLongToast(errMsg);
                loginView.hideLoading();
            }
        });
    }

    public void weiboLogin() {
        loginView.setWeiboLoginButtonEnable(false);
        loginView.showLoading("正在登录美甲大咖...");

        SocialLoginAPI.getInstance().weiboLogin((Activity) loginView, new SocialLoginAPI.SocialLoginResultListener<SocialLoginAPI.LoginData>() {
            @Override
            public void beginLoadData() {
            }

            @Override
            public void loginSuccess(final SocialLoginAPI.LoginData loginData) {
                loginDaka(null, loginData, new LoginServiceSelector<SocialLoginAPI.LoginData>() {
                    @Override
                    public String loginAndGetUid(String userName, SocialLoginAPI.LoginData loginData) throws CommonException, NetworkDisconnectException {
                        return loginService.weiboLogin(loginData.getUnionId(), loginData.getOpenId(), loginData.getNickName(), loginData.getAvatarUrl(), loginData.getThirdUserId());
                    }

                    @Override
                    public void afterLogin() {
                        loginView.setWeiboLoginButtonEnable(true);
                    }

                    @Override
                    public LoginAPI.LoginFromType getLoginFromType() {
                        return LoginAPI.LoginFromType.SINA_WEI_BO;
                    }
                });
            }

            @Override
            public void loginFail(String errMsg) {
                loginView.setWeiboLoginButtonEnable(true);
                loginView.showLongToast(errMsg);
                loginView.hideLoading();
            }
        });
    }

    public void qqLogin() {
        loginView.setQQLoginButtonEnable(false);
        loginView.showLoading("正在登录美甲大咖...");

        SocialLoginAPI.getInstance().qqLogin((Activity) loginView, new SocialLoginAPI.SocialLoginResultListener<SocialLoginAPI.LoginData>() {
            @Override
            public void beginLoadData() {
            }

            @Override
            public void loginSuccess(final SocialLoginAPI.LoginData loginData) {
                loginDaka(null, loginData, new LoginServiceSelector<SocialLoginAPI.LoginData>() {
                    @Override
                    public String loginAndGetUid(String userName, SocialLoginAPI.LoginData loginData) throws CommonException, NetworkDisconnectException {
                        return loginService.qqLogin(loginData.getUnionId(), loginData.getOpenId(), loginData.getNickName(), loginData.getAvatarUrl(), loginData.getThirdUserId());
                    }

                    @Override
                    public void afterLogin() {
                        loginView.setQQLoginButtonEnable(true);
                    }

                    @Override
                    public LoginAPI.LoginFromType getLoginFromType() {
                        return LoginAPI.LoginFromType.QQ;
                    }
                });
            }

            @Override
            public void loginFail(String errMsg) {
                loginView.setQQLoginButtonEnable(true);
                loginView.showLongToast(errMsg);
                loginView.hideLoading();
            }
        });
    }

    private void loginDaka(final String userName, final Object loginData, final LoginServiceSelector loginServiceSelector) {
        TaskManager.BackgroundTask<CommonResult> loginTask = new TaskManager.BackgroundTask<CommonResult>() {
            @Override
            public Object doWork(CommonResult data) {
                CommonResult commonResult = new CommonResult();
                try {
                    String uid = loginServiceSelector.loginAndGetUid(userName, loginData);
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
                loginServiceSelector.afterLogin();
                loginView.hideLoading();
                if(data.isError()) {
                    loginView.showMessageDialog("登录失败", data.getErrorMsg());
                } else {
                    //登录成功
                    LoginAPI.getInstance().saveLoginStatus(userName, loginServiceSelector.getLoginFromType(), data.getResult());
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

    private interface LoginServiceSelector<T> {
        String loginAndGetUid(String userName, T loginData) throws CommonException, NetworkDisconnectException;

        void afterLogin();

        LoginAPI.LoginFromType getLoginFromType();
    }
}
