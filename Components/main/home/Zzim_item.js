import React from 'react';
import {View, Text, StyleSheet} from "react-native";


export default function Zzim_item(props) {
    if (props.data === null) {
        return (
            <View>
                <Text>찜한 지원사업이 없습니다.</Text>
            </View>
        )
    }
    else {
        return (
            <View>
                <Text>{props.data}</Text>
            </View>
        )
    }
}

const styles = StyleSheet.create({

});
