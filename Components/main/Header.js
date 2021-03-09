//메인페이지
import React from 'react';
import {View, Text, Image, StyleSheet} from "react-native";


export default function Header() {
    return (
        <View style={styles.container}>
            <View style={styles.elem}>
                <View style={styles.app_name_container}>
                    <Text
                        adjustsFontSizeToFit={true}
                        numberOfLines={1}
                        style={styles.app_name}>복지팡</Text>
                </View>
                <View style={styles.my_page_icon_container}>
                    <Image
                        style={styles.my_page_icon}
                        source={require("../../assets/my_page_icon.png")}
                    />
                </View>
            </View>
        </View>
    )
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        flexDirection: 'row'
    },
    elem: {
        width: '100%',
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between'
    },
    app_name_container: {
        width: '20%',
        height: '100%',
        flexDirection: 'row',
        alignItems: 'center',
        backgroundColor: '#961a90',
    },
    app_name: {
        marginLeft: '20%',
        fontSize: 20,
        color: '#63A8F9'
    },
    my_page_icon_container: {
        padding: '3%',
        width: '15%',
        height: '100%',
        backgroundColor: '#217a37',
    },
    my_page_icon: {
        resizeMode: 'contain',
        width: '100%',
        height: '100%',
    }
});
