import React from 'react';
import {View, Text, StyleSheet} from "react-native";


export default function Board_item(props) {
    if (props.data === null) {
        return (
            <View>
                <Text>not exist.</Text>
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
