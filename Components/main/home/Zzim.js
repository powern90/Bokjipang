import React, {Component} from 'react';
import {StyleSheet, View, Text} from "react-native";

import Zzim_item from "./Zzim_item";

export class Zzim extends Component {
    constructor(props) {
        super(props);
        this.state = {
            zzim_title: 'zzim_title',
            zzim_content: 'zzim_contentaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa',
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
                            style={styles.title}>Zzim support</Text>
                    </View>

                    <View style={styles.item_container}>
                        <Zzim_item title={this.state.zzim_title} content={this.state.zzim_content} />
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
        marginLeft: '5%',
        marginTop: '1%',
        fontSize: 17,
        fontWeight: 500,
        color: 'grey',
    },
    item_container: {

    }
});

export default Zzim
