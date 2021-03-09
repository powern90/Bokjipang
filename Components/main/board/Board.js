//개시판
import React, {Component} from 'react';
import { StyleSheet, View, Text } from "react-native";
import Cardcomponent from './Cardcomponent';
import { Container, Content, Icon } from 'native-base';

export class Board extends Component {

    render() {
        return (
            <Container style={style.container}>
                <Content>
                    <Cardcomponent likes="101" comments="2" />
                </Content>
            </Container>
        );
    }
}

const style = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: 'white'
    }
});
export default Board
