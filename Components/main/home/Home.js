//메인페이지
import React from 'react';
import {View, Text, StyleSheet} from "react-native";

import Header from '../Header'
import Link from './Link';
import Zzim from './Zzim';
import Banner from './Banner';

export default function Home() {
    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <Header/>
            </View>
            <View style={styles.link}>
                <Link/>
            </View>
            <View style={styles.banner}>
                <Banner/>
            </View>
            <View style={styles.content}>
                <View style={styles.zzim}>
                    <Zzim/>
                </View>
            </View>
        </View>
    )
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        padding: 10,
        backgroundColor: 'black',
    },
    header: {
        width:'100%',
        height:'5%',
        backgroundColor: '#ff9a9a',
    },
    link: {
        width:'100%',
        height:'15%',
        justifyContent: 'center',
        backgroundColor: '#9aa9ff',
    },
    banner: {
        width:'100%',
        height:'10%',
        justifyContent: 'center',
        backgroundColor: '#4e9826',
    },
    content: {
        flex: 1,
        marginTop: '10%',
        // justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#d6ca1a',
    },
    zzim: {
        width:'100%',
        height:'20%',
    },
});
