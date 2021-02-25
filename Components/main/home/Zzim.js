import React, {Component} from 'react';
import {StyleSheet, View, Text} from "react-native";

import Zzim_item from "./Zzim_item";

export class Zzim extends Component {
    constructor(props) {
        super(props);
        this.state = {
            zzim_item: 'null'
        };
    }

    render() {
        return (
            <View style={styles.container}>
                <View style={styles.elem}>
                    <View style={styles.title_container}>
                        <Text
                            adjustsFontSizeToFit={true}
                            numberOfLines={1}
                            style={styles.title}>찜한 지원사업</Text>
                    </View>

                    <View style={styles.item_container}>
                        <Zzim_item data={this.state.zzim_item}/>
                    </View>

                </View>
            </View>
        )
    }
}
const styles = StyleSheet.create({
    container: {
        width: '100%',
        height: '100%'
    },
    elem: {
        backgroundColor: "#E6E6E6",
        shadowColor: "rgba(0,0,0,1)",
        shadowOffset: {
            width: 3,
            height: 3
        },
        elevation: 5,
        shadowOpacity: 0.12,
        shadowRadius: 0,
        borderRadius: 20,
        width: '100%',
        height: '100%'
    },
    title_container: {

    },
    title: {
        marginLeft: '7%',
        marginTop: '3%',
        fontSize: 17,
    },
    item_container: {

    }
});

export default Zzim
