// 애플 로그인
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import LogInScreen from '../Login';
import * as AuthSession from 'expo-auth-session';
// import { withNavigation } from 'react-navigation';

const KAKAO_APP_KEY = '73d102eb4295ca8d726aa81a91fd2b1b';

class Container extends React.Component {

    static propTypes = {
        setToken: PropTypes.func.isRequired,
        setUser: PropTypes.func.isRequired,
        setUserProfile: PropTypes.func.isRequired,
        setLogIn: PropTypes.func.isRequired,
    };

    render() {
        return (
            <LogInScreen
                {...this.props}
                kakaoLogIn={this._kakaoLogin}
            />
        );
    }

    // API Actions
    _kakaoLogin = async () => {
        /* ↓ [1단계] authorization_code 수령해오기 */
        console.log('test');
        let redirectUrl = AuthSession.getRedirectUrl();
        console.log(encodeURIComponent(redirectUrl));
        // let result = await AuthSession.startAsync({
        //     authUrl:
        //         `https://kauth.kakao.com/oauth/authorize?response_type=code&` +
        //         'client_id=73d102eb4295ca8d726aa81a91fd2b1b' +
        //         `&redirect_uri=${redirectUrl}`
        // });
        fetch(
            `https://kauth.kakao.com/oauth/authorize?response_type=code&` +
            'client_id=73d102eb4295ca8d726aa81a91fd2b1b' +
            `&redirect_uri=${redirectUrl}`,
            {headers: { Authorization}}
        )
            .then(response=>response.json())
            .then(json => {
                console.log(json);
            })
        // if (result.type !== 'success') {
        //     console.log(result.type);
        //     // this.setState({ didError: true });
        // } else {
        //     /* ↓ [2단계] access_token 및 refresh_token 수령
        //     ※ V_2(async&await 만으로 fetch를 구현, V_1은 보류file/kakaoAPI.js 하단에 有)
        //     ※ log 찍히는 순서: 1 → 2 → 3 */
        //     try {
        //         let body =
        //             `grant_type=authorization_code` +
        //             `&client_id=${KAKAO_APP_KEY}` +
        //             `&code=${result.params.code}` +
        //             `&redirect_uri=${encodeURIComponent(redirectUrl)}`;
        //         let response = await fetch('https://kauth.kakao.com/oauth/token', {
        //             method: 'POST',
        //             headers: {
        //                 'Accept': 'application/json;charset=UTF-8',
        //                 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8',
        //             },
        //             body: body
        //         });
        //         // console.log(1);
        //         let json = await response.json();
        //         this.props.setToken(json);
        //
        //         /* ↓ [3단계] 사용자 정보 요청(token 이용)
        //         ※ V_2(async&await 만으로 fetch를 구현, V_1은 보류file/kakaoAPI.js 하단에 有) */
        //         try {
        //             response = await fetch('https://kapi.kakao.com/v2/user/me', {
        //                 method: 'GET',
        //                 headers: {
        //                     'Authorization': `Bearer ${json.access_token}`,
        //                 }
        //             });
        //             json = await response.json();
        //             this.props.setUser(json);
        //             /* [4단계] DB와 대조(kakaoID 이용)
        //             ※ V_2(async&await 만으로 fetch를 구현) */
        //
        //         }
        //         catch(error) {
        //             console.log('error_kakaoLogin(3단계)');
        //         }
        //         // console.log(2);
        //     }
        //     catch(error) {
        //         console.log('error_kakaoLogin(2단계)');
        //     }
        //     // console.log(3);
        // }
    };
}

export default Container;