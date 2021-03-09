//로그인
import React, { Component } from 'react';
import { ImageBackground, StyleSheet, View, TouchableOpacity, Image, StatusBar, Button } from 'react-native';

export default class LogInScreen extends Component {
    render() {
        return (
            <View style={styles.container}>
                <StatusBar hidden={true}/>
                {/*<ImageBackground*/}
                {/*    source={require('../../assets/splash.png')}*/}
                {/*    style={{width: '100%', height: '100%'}} resizeMode={'cover'}>*/}
                    <View style={styles.logo}/>
                    <View style={styles.login}>
                        <Button onPress={this.props.kakaoLogIn}>
                            {/* ↑ parent 컴포넌트의 _handleKakaoLogIn을 child 컴포넌트에게 주니깐,
                            handleKakaoLogIn의 실행이 parent 단에서 돌아가더라! */}
                            {/*<Image*/}
                            {/*    source={require('../../assets/icon.png')}*/}
                            {/*    style={{width: '60%', height: '10%'}}*/}
                            {/*    resizeMode={'contain'}*/}
                            {/*/>*/}
                        </Button>
                    </View>
                {/*</ImageBackground>*/}
            </View>
        );
    }
}
const styles = StyleSheet.create({
    container: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center',
    },
    logo: {
        flex: 2,
        alignItems: 'center',
        justifyContent: 'center',
    },
    login: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'flex-start',
    },
});
