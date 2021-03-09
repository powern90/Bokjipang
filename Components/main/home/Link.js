//메인페이지
import React, {Component} from 'react';
import {StyleSheet, View, Image} from "react-native";

export class Link extends Component {
    render() {
        return (
            <View style={styles.container}>
                <View style={styles.image_container}>
                    <Image
                        style={{height: '100%'}}
                        source={require("../../../assets/bokjiro.png")}
                        resizeMode="contain"
                    />
                </View>

                <View style={styles.image_container}>
                    <Image
                        style={{height: '100%'}}
                        source={require("../../../assets/gov24.png")}
                        resizeMode="contain"
                    />
                </View>

                <View style={styles.image_container}>
                    <Image
                        style={{height: '100%'}}
                        source={require("../../../assets/chest.png")}
                        resizeMode="contain"
                    />
                </View>
            </View>
        )
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        width: '100%',
        height: '100%',
        flexDirection: 'row'
    },
    image_container: {
        width: '29%',
        height: '100%',
        marginRight: '2%',
        marginLeft: '2%'
    }
});

export default Link
