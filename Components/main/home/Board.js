import React, {Component} from 'react';
import {StyleSheet, View, Text} from "react-native";

import Board_item from "./Board_item";

export class Board extends Component {
    constructor(props) {
        super(props);
        this.state = {
            Common_board: 'aaaaaaa',

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
                            style={styles.title}>Board content</Text>
                    </View>

                    <View style={styles.item_container}>
                        <Board_item data={this.state.Board_item}/>
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
        marginTop: '3%',
        fontSize: 17,
        fontWeight: 500,
        color: 'grey',
    },
    item_container: {

    }
});

export default Board;