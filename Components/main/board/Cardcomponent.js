import React, { Component } from 'react';
import { View, Image, Text, StyleSheet } from 'react-native';
import { Card, CardItem, Thumbnail, Body, Left, Right, Button, Icon} from "native-base";

export default class Cardcomponent extends Component{
    render() {
        return (
            <Card>
                <CardItem>
                    <Left>
                        <Thumbnail source={require('./윤득렬.jpg')}/>
                        <Body>
                            <Text>득렬</Text>
                            <Text note>hello world!!</Text>
                        </Body>
                    </Left>
                </CardItem>
                <CardItem style={{height:45}}>
                    <Left>
                        <Button transparent style={{ width:20}}>
                            <Icon name="ios-heart-outline" style={{color:'red'}}/>
                        </Button>
                        <Text style={{color:'red'}}>{this.props.likes}</Text>
                    </Left>
                    <Left>
                        <Button transparent>
                            <Icon name="ios-chatbubbles-outline" style={{color:'blue'}}/>
                        </Button>
                        <Text style={{color:'blue'}}>{this.props.comments}</Text>
                    </Left>
                </CardItem>
            </Card>
        );
    }
}

const style = StyleSheet.create({
    container: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center',
    }
});
