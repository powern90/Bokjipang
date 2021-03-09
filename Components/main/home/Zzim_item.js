import React from 'react';
import {View, Text, StyleSheet} from "react-native";


export default function Zzim_item(props) {
    if (props.title === null) {
        return (
            <View>
                <Text>찜한 지원사업이 없습니다.</Text>
            </View>
        )
    }
    else {
        return (
            <View>
                <Text style={styles.zzim_title}>{props.title}</Text>
                <Text style={styles.zzim_content} numberOfLines={1} ellipsizeMode="tail">{props.content}</Text>
            </View>
        )
    }
}

const styles = StyleSheet.create({
    zzim_title: {
        marginLeft: '5%',
        marginTop: '3%',
    },
    zzim_content: {
        marginLeft: '5%',
        color: 'dimgrey',
    },
});
