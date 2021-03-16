package com.bluemango.bokjipang;

import android.util.Log;
import com.kakao.auth.ApiErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;

public class KaKaoCallBack implements ISessionCallback {
    fragment_login mainActivity = new fragment_login();
    private fragment_signup fragment_signup = new fragment_signup();

    @Override
    public void onSessionOpened() {
        UserManagement.getInstance().me(new MeV2ResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                int result = errorResult.getErrorCode();

                if (result == ApiErrorCode.CLIENT_ERROR_CODE) mainActivity.kakaoError("네트워크 연결이 불안정합니다. 다시 시도해 주세요.");
                else mainActivity.kakaoError("로그인 도중 오류가 발생했습니다.");
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                mainActivity.kakaoError("세션이 닫혔습니다. 다시 시도해 주세요.");
            }

            @Override
            public void onSuccess(MeV2Response result) {
                Log.d("닉네임 확인 : ",result.getNickname()); //닉네임
                Log.d("이메일 확인 : ",result.getKakaoAccount().getEmail()); //이메일
                Log.d("이미지 확인 : ",result.getThumbnailImagePath()); //프로필 사진
            }
        });
    }

    @Override
    public void onSessionOpenFailed (KakaoException e){
        mainActivity.kakaoError("로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요.");
    }
}
